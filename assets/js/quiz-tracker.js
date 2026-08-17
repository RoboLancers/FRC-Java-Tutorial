// quiz-tracker.js
// Intercepts mkdocs-quiz completions and syncs results to Supabase.
// Loaded via extra_javascript in mkdocs.yml.

const SUPABASE_URL = 'https://wltmawdleuvcjxqtzkmj.supabase.co';
const SUPABASE_ANON_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndsdG1hd2RsZXV2Y2p4cXR6a21qIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzY4MDI5MDEsImV4cCI6MjA5MjM3ODkwMX0.MGZavTM0z6n1RjOJaxR1jj1Emr0zsKCPt38L4k_CO5U'; // safe to expose — RLS enforces security

// ── Supabase client (minimal, no SDK dependency) ─────────────────────────────

const SESSION_KEY = `sb-${new URL(SUPABASE_URL).hostname.split('.')[0]}-auth-token`;

// After GitHub OAuth, Supabase redirects back with the session in the URL hash.
// The full SDK handles this automatically; we do it manually here.
// Returns a Promise that resolves when the session has been stored (or immediately if no hash).
async function handleOAuthCallback() {
  const hash = window.location.hash;
  if (!hash.includes('access_token=')) return;

  const params = new URLSearchParams(hash.slice(1)); // strip leading #
  const accessToken  = params.get('access_token');
  const refreshToken = params.get('refresh_token');
  const expiresIn    = parseInt(params.get('expires_in') || '3600', 10);

  if (!accessToken) return;

  const user = await fetch(`${SUPABASE_URL}/auth/v1/user`, {
    headers: {
      'apikey': SUPABASE_ANON_KEY,
      'Authorization': `Bearer ${accessToken}`,
    },
  }).then(r => r.json());

  const session = {
    access_token:  accessToken,
    refresh_token: refreshToken,
    expires_at:    Math.floor(Date.now() / 1000) + expiresIn,
    user,
  };
  localStorage.setItem(SESSION_KEY, JSON.stringify(session));
  history.replaceState(null, '', window.location.pathname + window.location.search);
}

const supabase = {
  async signInWithGitHub() {
    // Strip the hash before passing redirect_to so we don't re-trigger handleOAuthCallback
    const redirectTo = window.location.origin + window.location.pathname + window.location.search;
    const url = `${SUPABASE_URL}/auth/v1/authorize?provider=github&redirect_to=${encodeURIComponent(redirectTo)}`;
    window.location.href = url;
  },

  async getSession() {
    const raw = localStorage.getItem(SESSION_KEY);
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
  btn.style.cssText = 'margin-left:1rem;padding:4px 10px;cursor:pointer;border-radius:4px;border:1px solid rgba(255,255,255,0.6);font-size:0.85rem;background:transparent;color:#fff;';
  nav.appendChild(btn);
  return btn;
}

async function initAuth() {
  const session = await supabase.getSession();
  const btn = injectAuthButton();

  if (session?.access_token) {
    const user = session.user;
    btn.textContent = `${user.user_metadata?.user_name || user.email} (sign out)`;
    btn.addEventListener('click', () => {
      if (confirm('Sign out of quiz progress tracking?')) {
        localStorage.removeItem(SESSION_KEY);
        location.reload();
      }
    });
    return session;
  }

  btn.textContent = 'Sign in to save progress';
  btn.addEventListener('click', () => supabase.signInWithGitHub());
  return null;
}

// ── mkdocs-quiz result interception ──────────────────────────────────────────
//
// mkdocs-quiz writes to localStorage whenever a quiz is submitted.
// We watch for those writes using a StorageEvent listener (cross-tab)
// and a MutationObserver on the quiz result elements (same-tab).

function extractQuizResults() {
  // mkdocs-quiz stores keys like "quiz_progress_<page-url>"
  // Value shape: { "quiz-0": { answered: bool, correct: bool, selectedValues: [...] }, ... }
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
    if (e.key?.startsWith('quiz_progress_')) {
      syncResult(e.key, session);
    }
  });
}

// ── Init ──────────────────────────────────────────────────────────────────────

document.addEventListener('DOMContentLoaded', async () => {
  await handleOAuthCallback();
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