# Quiz Progress Tracking — Supabase + MkDocs Integration

This document describes how to add teacher/admin progress tracking to the FRC Java Tutorial site using Supabase as a backend. The tutorial is hosted on GitHub Pages (static), so Supabase acts as the only server-side component.

---

## Architecture Overview

```
Student browser
  └── mkdocs-quiz  →  localStorage (existing behavior, unchanged)
  └── quiz-tracker.js  →  POST results  →  Supabase REST API
                                                  ↓
                                         Postgres DB (quiz_results table)
                                                  ↓
                                         Teacher dashboard (Supabase Table Editor
                                         or a simple admin HTML page)
```

- No changes to existing quiz content or mkdocs-quiz behavior
- Supabase free tier is sufficient (500 MB, unlimited API calls)
- Students authenticate via GitHub OAuth (one click, no passwords)

---

## Step 1 — Supabase Project Setup

1. Create a free account at [supabase.com](https://supabase.com)
2. Create a new project (e.g. `frc-tutorial`)
3. In the SQL editor, run the schema below

### Database Schema

```sql
-- Stores one row per quiz submission
CREATE TABLE quiz_results (
  id           BIGSERIAL PRIMARY KEY,
  user_id      UUID REFERENCES auth.users(id) ON DELETE CASCADE,
  student_name TEXT,               -- pulled from GitHub profile
  github_login TEXT,               -- e.g. "octocat"
  quiz_id      TEXT NOT NULL,      -- matches the quiz identifier in mkdocs-quiz
  page_url     TEXT,               -- which tutorial page the quiz is on
  score        INTEGER NOT NULL,   -- number of correct answers
  total        INTEGER NOT NULL,   -- total number of questions
  pct          NUMERIC(5,2),       -- score / total * 100
  answers      JSONB,              -- full answer detail for review
  submitted_at TIMESTAMPTZ DEFAULT NOW()
);

-- Index for teacher queries (by student or by quiz)
CREATE INDEX ON quiz_results (github_login);
CREATE INDEX ON quiz_results (quiz_id);
CREATE INDEX ON quiz_results (submitted_at);

-- Row-level security: students can only insert/read their own rows
ALTER TABLE quiz_results ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Students insert own results"
  ON quiz_results FOR INSERT
  WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Students read own results"
  ON quiz_results FOR SELECT
  USING (auth.uid() = user_id);

-- Teachers (Supabase dashboard / service role key) can read everything
-- No additional policy needed — service role bypasses RLS
```

### Enable GitHub OAuth

1. Go to **Authentication → Providers → GitHub** in Supabase dashboard
2. Create a GitHub OAuth App at `github.com/settings/developers`
   - Homepage URL: `https://<your-org>.github.io/FRC-Java-Tutorial/`
   - Callback URL: `https://<your-supabase-project>.supabase.co/auth/v1/callback`
3. Paste the Client ID and Secret into Supabase

---

## Step 2 — JavaScript Integration

Create this file at `docs/assets/js/quiz-tracker.js`.

```javascript
// quiz-tracker.js
// Intercepts mkdocs-quiz completions and syncs results to Supabase.
// Loaded via extra_javascript in mkdocs.yml.

const SUPABASE_URL = 'https://YOUR_PROJECT_ID.supabase.co';
const SUPABASE_ANON_KEY = 'YOUR_ANON_PUBLIC_KEY'; // safe to expose — RLS enforces security

// ── Supabase client (minimal, no SDK dependency) ─────────────────────────────

const supabase = {
  async signInWithGitHub() {
    const redirectTo = window.location.href;
    const url = `${SUPABASE_URL}/auth/v1/authorize?provider=github&redirect_to=${encodeURIComponent(redirectTo)}`;
    window.location.href = url;
  },

  async getSession() {
    // Supabase stores the session in localStorage under this key
    const raw = localStorage.getItem(`sb-${new URL(SUPABASE_URL).hostname.split('.')[0]}-auth-token`);
    return raw ? JSON.parse(raw) : null;
  },

  async insertResult(row, accessToken) {
    const res = await fetch(`${SUPABASE_URL}/rest/v1/quiz_results`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'apikey': SUPABASE_ANON_KEY,
        'Authorization': `Bearer ${accessToken}`,
        'Prefer': 'return=minimal',
      },
      body: JSON.stringify(row),
    });
    if (!res.ok) {
      console.error('quiz-tracker: failed to save result', await res.text());
    }
  },
};

// ── Auth UI ───────────────────────────────────────────────────────────────────

function injectAuthButton() {
  const nav = document.querySelector('.md-header__inner') // Material header
    || document.querySelector('nav')
    || document.body;

  const btn = document.createElement('button');
  btn.id = 'quiz-auth-btn';
  btn.style.cssText = 'margin-left:1rem;padding:4px 10px;cursor:pointer;border-radius:4px;border:1px solid #ccc;font-size:0.85rem;';
  btn.textContent = 'Sign in to save progress';
  btn.addEventListener('click', () => supabase.signInWithGitHub());
  nav.appendChild(btn);
  return btn;
}

async function initAuth() {
  const session = await supabase.getSession();
  const btn = injectAuthButton();

  if (session?.access_token) {
    const user = session.user;
    btn.textContent = `Signed in as ${user.user_metadata?.user_name || user.email}`;
    btn.disabled = true;
    btn.style.opacity = '0.7';
    btn.style.cursor = 'default';
    return session;
  }
  return null;
}

// ── mkdocs-quiz result interception ──────────────────────────────────────────
//
// mkdocs-quiz (v1.6.3) actual localStorage format (differs from earlier assumption):
//
//   Key:   "quiz_progress_<page-url>"   e.g. "quiz_progress_/basics/java_basics.html"
//   Value: { "quiz-0": { answered: bool, correct: bool, selectedValues: [...] },
//             "quiz-1": { ... }, ... }
//
// There is no top-level "submitted", "score", or "total" field.
// Score and total are derived by counting entries where correct === true / length.

function extractQuizResults() {
  const results = [];
  for (let i = 0; i < localStorage.length; i++) {
    const key = localStorage.key(i);
    if (!key?.startsWith('quiz_progress_')) continue;

    try {
      const data = JSON.parse(localStorage.getItem(key));
      const entries = Object.values(data);
      if (entries.some(e => e.answered)) {
        results.push({ key, data });
      }
    } catch (_) {}
  }
  return results;
}

function getQuizIdFromKey(key) {
  // key format: "quiz_progress_/basics/java_basics.html"
  return key.replace(/^quiz_progress_/, '');
}

async function syncResult(quizKey, session) {
  if (!session?.access_token) return;

  let data;
  try {
    data = JSON.parse(localStorage.getItem(quizKey));
  } catch (_) { return; }

  const entries = Object.values(data);
  if (!entries.some(e => e.answered)) return;

  // Check if already synced to avoid duplicate submissions
  const syncedKey = `synced-${quizKey}`;
  if (localStorage.getItem(syncedKey) === 'true') return;

  const score = entries.filter(e => e.correct).length;
  const total = entries.length;
  const quizId = getQuizIdFromKey(quizKey);
  const user = session.user;

  const row = {
    user_id:      user.id,
    student_name: user.user_metadata?.full_name || user.user_metadata?.user_name || 'Unknown',
    github_login: user.user_metadata?.user_name || null,
    quiz_id:      quizId,
    page_url:     window.location.pathname,
    score,
    total,
    pct:          total > 0 ? ((score / total) * 100).toFixed(2) : 0,
    answers:      data,
  };

  await supabase.insertResult(row, session.access_token);
  localStorage.setItem(syncedKey, 'true');
  console.log(`quiz-tracker: synced result for quiz "${quizId}"`);
}

function watchForQuizCompletions(session) {
  // 1. Same-tab: watch DOM for mkdocs-quiz result containers appearing
  const observer = new MutationObserver(() => {
    const results = extractQuizResults();
    results.forEach(r => syncResult(r.key, session));
  });
  observer.observe(document.body, { childList: true, subtree: true });

  // 2. Cross-tab: handle storage events
  window.addEventListener('storage', (e) => {
    if (e.key?.startsWith('quiz-')) {
      syncResult(e.key, session);
    }
  });
}

// ── Init ──────────────────────────────────────────────────────────────────────

document.addEventListener('DOMContentLoaded', async () => {
  const session = await initAuth();

  if (session) {
    // Sync any previously completed quizzes that weren't synced yet
    const results = extractQuizResults();
    for (const r of results) {
      await syncResult(r.key, session);
    }
    // Watch for future completions on this page
    watchForQuizCompletions(session);
  }
});
```

---

## Step 3 — Wire Up in mkdocs.yml

Add the Supabase JS SDK and your tracker script to `mkdocs.yml`:

```yaml
extra_javascript:
  - assets/js/quiz-tracker.js
```

No other changes to `mkdocs.yml` are needed — the existing `mkdocs_quiz` plugin entry stays as-is.

---

## Step 4 — Teacher Dashboard

### Option A — Supabase Table Editor (Zero setup)

The Supabase dashboard includes a full table editor. Teachers with dashboard access can:

- Filter by `github_login` to see a specific student
- Filter by `quiz_id` to see class-wide performance on a quiz
- Export to CSV for grading

No code required for this option.

---

### Option B — Simple Admin HTML Page

Create `docs/admin.html` (excluded from nav) for a basic teacher dashboard. This page uses the Supabase **service role key**, which must never be exposed publicly — either:

- Keep this page local (never deployed), or
- Host it separately behind basic auth (e.g. Netlify password protection)

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Quiz Progress — Teacher Dashboard</title>
  <style>
    body { font-family: sans-serif; max-width: 1100px; margin: 2rem auto; padding: 0 1rem; }
    table { border-collapse: collapse; width: 100%; font-size: 0.9rem; }
    th, td { border: 1px solid #ddd; padding: 8px 12px; text-align: left; }
    th { background: #f4f4f4; }
    tr:nth-child(even) { background: #fafafa; }
    select, input { padding: 6px; margin: 0 8px 1rem 0; }
  </style>
</head>
<body>
  <h1>Quiz Progress Dashboard</h1>

  <label>Filter by student:
    <input id="filter-student" type="text" placeholder="github login">
  </label>
  <label>Filter by quiz:
    <input id="filter-quiz" type="text" placeholder="quiz id">
  </label>
  <button onclick="loadResults()">Refresh</button>

  <table id="results-table">
    <thead>
      <tr>
        <th>Student</th>
        <th>GitHub Login</th>
        <th>Quiz ID</th>
        <th>Page</th>
        <th>Score</th>
        <th>Pct</th>
        <th>Submitted</th>
      </tr>
    </thead>
    <tbody id="results-body"></tbody>
  </table>

  <script>
    const SUPABASE_URL = 'https://YOUR_PROJECT_ID.supabase.co';
    // WARNING: service role key — never deploy this page publicly
    const SERVICE_ROLE_KEY = 'YOUR_SERVICE_ROLE_KEY';

    async function loadResults() {
      const student = document.getElementById('filter-student').value.trim();
      const quiz    = document.getElementById('filter-quiz').value.trim();

      let url = `${SUPABASE_URL}/rest/v1/quiz_results?order=submitted_at.desc&limit=500`;
      if (student) url += `&github_login=eq.${encodeURIComponent(student)}`;
      if (quiz)    url += `&quiz_id=eq.${encodeURIComponent(quiz)}`;

      const res = await fetch(url, {
        headers: {
          'apikey': SERVICE_ROLE_KEY,
          'Authorization': `Bearer ${SERVICE_ROLE_KEY}`,
        }
      });

      const rows = await res.json();
      const tbody = document.getElementById('results-body');
      tbody.innerHTML = rows.map(r => `
        <tr>
          <td>${r.student_name}</td>
          <td><a href="https://github.com/${r.github_login}" target="_blank">${r.github_login}</a></td>
          <td>${r.quiz_id}</td>
          <td>${r.page_url}</td>
          <td>${r.score} / ${r.total}</td>
          <td>${r.pct}%</td>
          <td>${new Date(r.submitted_at).toLocaleString()}</td>
        </tr>
      `).join('');
    }

    loadResults();
  </script>
</body>
</html>
```

---

## Configuration Checklist

- [ ] Create Supabase project and run schema SQL
- [ ] Enable GitHub OAuth in Supabase → Authentication → Providers
- [ ] Create GitHub OAuth App, set callback URL
- [ ] Replace `YOUR_PROJECT_ID` and `YOUR_ANON_PUBLIC_KEY` in `quiz-tracker.js`
- [ ] Place `quiz-tracker.js` at `docs/assets/js/quiz-tracker.js`
- [ ] Add `extra_javascript` entry to `mkdocs.yml`
- [ ] Test: sign in with GitHub on the site, complete a quiz, verify row appears in Supabase table
- [ ] (Optional) Set up teacher dashboard — either Supabase Table Editor or local `admin.html`

---

## Security Notes

| Key | Where used | Safe to expose? |
|---|---|---|
| `SUPABASE_ANON_KEY` | `quiz-tracker.js` (client-side) | Yes — RLS policies restrict what it can do |
| `SERVICE_ROLE_KEY` | `admin.html` only | **No — bypasses RLS. Never deploy publicly.** |

Row-level security ensures students can only insert and read their own rows. The service role key (teacher access) should stay in the local `admin.html` or be accessed only through the Supabase dashboard.

---

## Cost

All of the above runs within Supabase's **free tier**:

- 500 MB database storage
- Unlimited API requests
- 50,000 monthly active users
- Built-in GitHub OAuth

No credit card required.
