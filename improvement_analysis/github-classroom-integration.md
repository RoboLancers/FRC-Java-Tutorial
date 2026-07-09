# GitHub Classroom Integration Analysis

---

## Approach 1: MkDocs for Content + GitHub Classroom for Assignments

**Pros**
- Each tool does what it's best at — MkDocs for structured reading, GitHub Classroom for code practice and submission tracking
- Quiz-tracking (Supabase plan) and `mkdocs-quiz` remain fully intact for knowledge checks
- Students have a polished, searchable reference site to return to while working on assignments
- Assignment READMEs can link directly to the relevant MkDocs pages ("read the Subsystems page before starting")
- Auto-grading via GitHub Actions (JUnit tests, compilation checks) is natural in Classroom

**Cons**
- Two systems to maintain and keep synchronized
- Students switch contexts between the site and their repo

---

## Approach 2: All Content in GitHub Classroom

**Pros**
- Single platform, everything version-controlled

**Cons**
- GitHub renders only GitHub Flavored Markdown (GFM) — you lose admonitions, quiz plugin, `pymdownx` extensions, snippet system, search, and custom navigation
- GitHub repo directory trees are not a tutorial — no concept of "next page" or structured learning paths
- No way to keep `mkdocs-quiz` functionality, which has been identified as valuable for knowledge checks
- The polished MkDocs Material experience is a meaningful quality advantage for student engagement

**Verdict: Approach 1 is clearly superior.** GitHub Classroom is designed for assignment distribution and grading, not reference documentation. Moving content there would be a significant downgrade to the student experience for no meaningful gain.

---

## Recommended Structure

```
MkDocs site (reference + knowledge checks)
  └── conceptual pages + inline quizzes (mkdocs-quiz)
  └── "Practice Assignment" callout at end of each topic
        → links to the GitHub Classroom assignment

GitHub Classroom assignments (code practice)
  └── template repo per assignment
        ├── README.md  (assignment description in GFM)
        ├── src/       (starter Java code with TODO stubs)
        └── .github/workflows/autograder.yml
```

---

## Markdown in GitHub Classroom Assignment READMEs

GitHub does **not** render MkDocs extensions, so assignment READMEs need to be written in plain GFM. The key things to know:

**GitHub's alert syntax** (renders as colored callout boxes, equivalent to MkDocs admonitions):

```markdown
> [!NOTE]
> Read the Subsystems page on the tutorial site before starting.

> [!TIP]
> The `periodic()` method runs every 20ms — keep it fast.

> [!WARNING]
> Don't set motor output without a safety check first.

> [!IMPORTANT]
> Your code must compile before submission counts as complete.
```

**What works natively in GFM:**
- Fenced code blocks with `java` syntax highlighting
- Tables, task lists (`- [ ]`), and section headers
- Links to the MkDocs site pages for reference

**What to avoid in READMEs:**
- MkDocs `!!!` admonition syntax (renders as plain text)
- `--8<--` snippet references
- Any `pymdownx` extensions

---

## Auto-Grading Considerations

For FRC Java, you can't run a robot in CI, but you can verify:
- Code **compiles** (Gradle build check)
- Required **methods and classes exist** (reflection-based JUnit tests)
- **Behavior** of pure-Java logic (unit tests on non-hardware-dependent code)
- **Style** via Checkstyle if desired

The split between MkDocs quizzes (conceptual knowledge) and Classroom auto-grading (code correctness) gives teachers two distinct signals — a student who passes quizzes but fails autograding has a reading-vs-writing gap, which is actionable feedback.
