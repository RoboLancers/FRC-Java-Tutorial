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

-- Teachers/admins: see the "Instructor Access (teachers table)" section below.
```

This full schema (including the `teachers` table) is version-controlled at
[supabase/schema.sql](../supabase/schema.sql) — run it in the Supabase SQL
editor and keep it in sync with any future schema changes.

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

### Option B — Simple Admin HTML Page (implemented)

[docs/admin.html](../docs/admin.html) is a basic teacher dashboard, excluded from `nav:`
so it isn't linked from the site but is still built and reachable at
`/admin.html`. It signs the visitor in with the same GitHub OAuth flow as
students (no service role key, ever, client-side) and queries `quiz_results`
using the visitor's own access token plus the `SUPABASE_ANON_KEY`. Whether
they see every student's rows or just their own is entirely decided by RLS —
see "Instructor Access (teachers table)" below.

---

## Instructor Access (teachers table)

By default RLS only lets a signed-in user read their *own* `quiz_results` rows
(`auth.uid() = user_id`), so signing in to `admin.html` alone doesn't grant
class-wide visibility. A `teachers` table plus an additional RLS policy grants
it to an allowlist of GitHub logins, without ever exposing a service-role key:

```sql
CREATE TABLE teachers (
  github_login TEXT PRIMARY KEY,
  added_at     TIMESTAMPTZ DEFAULT NOW()
);

CREATE POLICY "Teachers read all results"
  ON quiz_results FOR SELECT
  USING (
    (auth.jwt() -> 'user_metadata' ->> 'user_name') IN (SELECT github_login FROM teachers)
  );
```

This checks the GitHub username Supabase embeds in the OAuth JWT
(`user_metadata.user_name`), which is set by Supabase from the GitHub
provider and can't be forged by the client.

The `teachers` table itself has RLS enabled with no anon/authenticated
policies, so it can't be read or written through the public API — only
through the Supabase SQL editor or with the service-role key, which is what
[scripts/invite_teacher.py](../scripts/invite_teacher.py) uses. That key must
never appear in `admin.html` or `quiz-tracker.js`; keep it in your shell
environment only.

**To grant an instructor access:**

1. Have them sign in once at `/admin.html` via "Sign in with GitHub" (this
   just confirms their OAuth login works; they'll see "no results found /
   not in the teachers table" until step 2).
2. Run:
   ```bash
   SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py add their-github-username
   ```
3. They refresh `/admin.html` and now see every student's results.

**To revoke access:**
```bash
SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py remove their-github-username
```

**To see current instructors:**
```bash
SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py list
```

The full schema, including this table, is tracked in
[supabase/schema.sql](../supabase/schema.sql) — treat it as the source of
truth and re-run any diffs in the Supabase SQL editor when it changes.

---

## Configuration Checklist

- [ ] Create Supabase project and run [supabase/schema.sql](../supabase/schema.sql)
- [ ] Enable GitHub OAuth in Supabase → Authentication → Providers
- [ ] Create GitHub OAuth App, set callback URL
- [ ] Replace `YOUR_PROJECT_ID` and `YOUR_ANON_PUBLIC_KEY` in `quiz-tracker.js`
- [ ] Place `quiz-tracker.js` at `docs/assets/js/quiz-tracker.js`
- [ ] Add `extra_javascript` entry to `mkdocs.yml`
- [ ] Test: sign in with GitHub on the site, complete a quiz, verify row appears in Supabase table
- [ ] Add each instructor's GitHub login to the `teachers` table (see above)

---

## Security Notes

| Key | Where used | Safe to expose? |
|---|---|---|
| `SUPABASE_ANON_KEY` | `quiz-tracker.js`, `admin.html` (client-side) | Yes — RLS policies restrict what it can do |
| `SUPABASE_SERVICE_ROLE_KEY` | `scripts/invite_teacher.py`, run locally only | **No — bypasses RLS. Never put it in a committed file or client-side code.** |

Row-level security ensures students can only insert/read their own rows, and
instructors get class-wide read access only if their GitHub login is present
in the `teachers` table. The service-role key exists solely to manage that
allowlist from the command line and never ships to the browser.

---

## Cost

All of the above runs within Supabase's **free tier**:

- 500 MB database storage
- Unlimited API requests
- 50,000 monthly active users
- Built-in GitHub OAuth

No credit card required.
