-- FRC Java Tutorial — Supabase schema
--
-- Source of truth for the quiz-progress-tracking backend described in
-- improvement_analysis/quiz-progress-tracking.md. There is no migration
-- tooling wired up — run statements manually in the Supabase SQL editor
-- when the schema changes, and keep this file in sync.

-- ── quiz_results ──────────────────────────────────────────────────────────────
-- One row per quiz submission, synced from docs/assets/js/quiz-tracker.js.

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

CREATE INDEX ON quiz_results (github_login);
CREATE INDEX ON quiz_results (quiz_id);
CREATE INDEX ON quiz_results (submitted_at);

ALTER TABLE quiz_results ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Students insert own results"
  ON quiz_results FOR INSERT
  WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Students read own results"
  ON quiz_results FOR SELECT
  USING (auth.uid() = user_id);

-- ── teachers ──────────────────────────────────────────────────────────────────
-- Allowlist of GitHub logins granted read access to every student's results
-- via docs/admin.html. Membership is checked against the GitHub username
-- Supabase embeds in the OAuth JWT (auth.jwt() -> 'user_metadata' ->> 'user_name'),
-- so no service-role key is ever exposed client-side.

CREATE TABLE teachers (
  github_login TEXT PRIMARY KEY,
  added_at     TIMESTAMPTZ DEFAULT NOW()
);

-- RLS is enabled with no anon/authenticated policies, so this table is
-- unreadable and unwritable via the public API (anon key). It can only be
-- managed with the service-role key, i.e. via scripts/invite_teacher.py or
-- the Supabase SQL editor/dashboard — never from client-side JS.
ALTER TABLE teachers ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Teachers read all results"
  ON quiz_results FOR SELECT
  USING (
    (auth.jwt() -> 'user_metadata' ->> 'user_name') IN (SELECT github_login FROM teachers)
  );

-- To grant/revoke instructor access, run:
--
--   SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py add their-github-username
--   SUPABASE_SERVICE_ROLE_KEY=... python3 scripts/invite_teacher.py remove their-github-username
--
-- (See scripts/invite_teacher.py for details.)
