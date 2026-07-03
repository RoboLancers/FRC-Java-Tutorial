# GitHub Basics Assignment: classroom50 Migration

Summary of the plan and implementation for migrating
`improvement_analysis/github-classroom-github-basics/` from the old GitHub
Classroom architecture to classroom50, plus new grading tests that check
repository state (branch, commit, PR, Issue) instead of just file contents.

## Why

`.claude/CLAUDE.md` designates [classroom50](https://github.com/foundation50/classroom50/wiki/)
as the GitHub Classroom replacement for this repo. The `github-basics`
assignment mockup was still built for old GitHub Classroom
(`.github/classroom/autograding.json` + `education/autograding@v1` action),
and the user also wanted the grading tests to actually verify repo state —
that a branch, commit, pull request, and issue exist — rather than only
checking file contents in the working tree.

## Research: how classroom50 actually differs

Fetched the classroom50 wiki (Autograders, Assignment Templates, GitHub
Integration pages) rather than relying on training data, since it's a
newer/less-known tool. Key architectural differences from GitHub Classroom:

- **Two separate repos, not one.** The **assignment template repo** becomes
  the student's repo on accept. Autograding config does *not* live there —
  it lives in a separate **classroom config repo** owned by the instructor,
  as `assignments.json` (assignment metadata) plus
  `autograders/<slug>/tests.json` (declarative tests).
- **The template must not contain autograding config.** The wiki explicitly
  warns never to include `.github/workflows/autograde.yaml` (or any
  GH-Classroom-style autograding folder) in the template — classroom50
  injects the real grading workflow at accept-time. Our old
  `.github/classroom/autograding.json` and `.github/workflows/classroom.yml`
  were both wrong for this architecture.
- **Declarative test types**: `io` (stdin/stdout comparison), `run` (shell
  command + exit code), `python` (pytest). Our existing grading scripts
  (`check_student_notes.sh`, `check_gitignore.sh`, `check_commit_message.sh`)
  already fit the `run` type as-is — only the JSON wrapper needed to move.
- **Known risk found during research**: the documented default
  `GITHUB_TOKEN` for autograder jobs only has `contents: write` and
  `statuses: write` — there's no documented `pull-requests: read` or
  `issues: read` grant, and no documented way to request broader scopes per
  assignment. This directly affects the new PR/Issue checks (see below).
  Branch and commit checks don't have this problem since they only need
  plain `git` access.

## Decisions made with the user

Two things were ambiguous enough to check before implementing:

1. **PR/Issue checks given the token-scope gap** — implement them anyway
   (best-effort via `gh pr list` / `gh issue list`), with the scripts
   clearly distinguishing a permission/auth error from a genuine
   "not found" result, and the risk documented for whoever deploys this.
2. **Where the config-repo-side files should live** — initially planned as
   a mockup `classroom-config/` folder inside this analysis directory. The
   user clarified the classroom50 config repo **already exists** elsewhere,
   so the deliverable changed to a `for-classroom-config-repo/` staging
   folder: exactly the files to copy, plus instructions on where they go.

## What was implemented

```
improvement_analysis/github-classroom-github-basics/
├── template/                              # the classroom50 student repo
│   ├── .devcontainer/                     # unchanged
│   ├── .githooks/commit-msg               # unchanged
│   ├── .gitignore                         # unchanged
│   ├── README.md                          # unchanged (no GH-Classroom-specific wording found)
│   ├── student-notes.md                   # unchanged
│   ├── wpilib.gitignore                   # unchanged
│   └── grading/
│       ├── check_student_notes.sh         # unchanged
│       ├── check_gitignore.sh             # unchanged
│       ├── check_commit_message.sh        # unchanged
│       ├── check_branch_exists.sh         # NEW
│       ├── check_pr_exists.sh             # NEW
│       └── check_issue_exists.sh          # NEW
└── for-classroom-config-repo/             # NEW — staging folder, not deployed itself
    ├── INSTRUCTIONS.md                    # where each file below goes + the token-scope risk
    ├── assignments.json                   # one assignment entry to merge in
    └── autograders/github-basics/
        └── tests.json                     # 6 declarative tests
```

**Removed** from `template/`: `.github/classroom/autograding.json` and
`.github/workflows/classroom.yml` (old GitHub Classroom autograder config —
not valid in a classroom50 template).

**New grading scripts** (all `type: "run"`, exit 0 = pass):

- `check_branch_exists.sh` — self-contained `git fetch` of all remote
  branches, then checks for at least one branch matching the FRC
  `name/feature` naming convention (e.g. `firstname-lastname/intro`) that
  isn't `main`/`master`. No special token scope needed.
- `check_pr_exists.sh` — `gh pr list --state all`; distinguishes a
  permission/auth error from "no PR found" in its output.
- `check_issue_exists.sh` — same approach via `gh issue list --state all`.

**`for-classroom-config-repo/tests.json`** — all 6 tests (3 existing +
3 new), points rebalanced to sum to 100: student notes 25, `.gitignore` 20,
commit message 15, branch 15, PR 15, issue 10.

**`for-classroom-config-repo/assignments.json`** — a single assignment
entry (slug `github-basics`) meant to be merged into the config repo's
existing `assignments.json` array, not to replace the file.

## Verification performed

- `python3 -m json.tool` on both new JSON files — valid.
- `bash -n` on all six grading scripts — valid syntax.
- `check_branch_exists.sh` run against a real synthetic git repo (bare
  "origin" + clone) for both a passing case (branch
  `firstname-lastname/intro` pushed) and a failing case (branch deleted,
  only a badly-named branch pushed) — both produced the correct PASS/FAIL
  and exit code.
- `check_pr_exists.sh` / `check_issue_exists.sh` run in this environment
  (no `gh` CLI installed) to confirm they fail with a clear, distinct
  "gh CLI not available" message rather than a silent or misleading error.
- Could not run the PR/Issue scripts against a real classroom50-graded
  repo with the actual autograder token — that's the one part of this
  work that still needs validation against the real deployment, per the
  risk noted in `for-classroom-config-repo/INSTRUCTIONS.md`.
