# :octocat: GitHub Basics — FRC Edition

Welcome to the GitHub Basics assignment for your FRC programming team. By the end of this assignment you will have hands-on practice with every GitHub workflow your team uses during build season.

---

## :books: Before you start

Make sure you have:
- [ ] A GitHub account with **two-factor authentication (2FA) enabled** ([setup guide](https://docs.github.com/en/authentication/securing-your-account-with-two-factor-authentication-2fa/configuring-two-factor-authentication))
- [ ] Read through the **[Using GitHub](https://robolancers-java-learning.github.io/FRC-Java-Tutorial/version_control/github/)** tutorial page

If you are not comfortable with basic Git commands (`add`, `commit`, `push`, `pull`, `branch`), review [Basic Git](https://robolancers-java-learning.github.io/FRC-Java-Tutorial/version_control/BasicGit/) first.

---

## :octocat: Concepts review

### Repositories

A **repository** (repo) is the project folder that Git tracks. It stores every version of every file. When GitHub hosts a repository, your whole team can share and sync changes through it.

Every FRC robot project should live in a repository under your **team's GitHub Organization** — not a personal account. That way the code stays with the team even after students graduate.

### Cloning vs. Forking

These two words describe very different things:

| | Clone | Fork |
|---|---|---|
| **What it does** | Downloads a repo to your computer | Creates your own copy on GitHub |
| **When to use it** | Getting team code you already have access to | Contributing to a project you don't own |

**FRC rule of thumb:** you almost always **clone** the team repo. Everyone clones the same repo and uses branches to work in parallel. Forks are for contributing to open-source projects like WPILib.

### Branches

A branch is a parallel copy of the code where you can make changes safely. When your work is ready, you merge it back into `main` via a pull request.

FRC teams use a `name/feature` naming convention:
```
mar-liu/auto-balance
carl-stanton/fix-intake-pid
add-vision-subsystem
```

Avoid generic names like `test` or `my-branch`.

### Committing and pushing

A **commit** is a saved checkpoint with a message describing what you changed. A **push** sends your local commits to GitHub so teammates can see them.

Write commit messages that describe *what* and *why*:
- ❌ `fix stuff`
- ✓ `Fix intake rollers spinning backwards by negating motor speed`

### Pull Requests

A **pull request (PR)** is how you propose merging your branch into `main`. It is also where your team reviews your code before it goes on the robot.

When opening a PR:
1. Write a clear title and description (explain what changed and why).
2. Request at least one reviewer.
3. If the PR closes a GitHub Issue, write `Closes #42` in the description.

The reviewer looks at the **Files changed** tab. They can approve the PR or request changes. Address feedback by pushing new commits — the PR updates automatically.

### Issues

An **Issue** is a GitHub task card for tracking bugs, features, or questions. Before starting work on something, check if there is already an open Issue for it. If not, create one.

Link a PR to an Issue with `Closes #<issue number>` — GitHub will close the Issue automatically when the PR merges.

### `.gitignore` for WPILib projects

A `.gitignore` file tells Git which files to skip. For WPILib projects, you should **never commit** the following:

```
build/        # compiled output — regenerated every build
.gradle/      # Gradle cache
.wpilib/      # machine-specific deploy configuration
.vscode/      # editor settings (usually)
.DS_Store     # macOS metadata
Thumbs.db     # Windows thumbnail cache
```

You **should commit** everything that defines your robot's behavior: `src/`, `build.gradle`, `vendordeps/`, `gradlew`, `.gitignore`.

### Markdown

GitHub renders Markdown in README files, Issues, and PR descriptions. Use it to make your writing clear:

```
# Heading
**bold**  _italic_  `inline code`
- bullet list
1. numbered list
[link text](https://example.com)
```

---

## :clipboard: Tasks

Complete each task in order. Check off each item as you finish it.

### Task 1 — Clone this repository

Clone this repository to your local machine using the command line (not GitHub Desktop):

```bash
git clone <paste the HTTPS URL from the green Code button>
```

- [ ] Repository cloned locally

---

### Task 2 — Create a branch

Create a new branch following the FRC naming convention. Replace `firstname` and `lastname` with your own name:

```bash
git checkout -b firstname-lastname/intro
```

- [ ] Branch created with the correct naming format

---

### Task 3 — Fill in your student notes

Open [`student-notes.md`](student-notes.md) in your editor and answer all five questions. Save the file when you are done.

- [ ] All five questions answered in `student-notes.md`

---

### Task 4 — Complete the `.gitignore` exercise

Open [`wpilib.gitignore`](wpilib.gitignore). Some entries are intentionally missing or incorrect. Fix the file so it correctly ignores the right directories and files for a WPILib project.

Read the comments in the file for hints.

- [ ] `wpilib.gitignore` corrected with all required entries

---

### Task 5 — Commit your changes

Stage and commit both files you edited with a meaningful commit message:

```bash
git add student-notes.md wpilib.gitignore
git commit -m "Complete GitHub basics assignment"
```

- [ ] Changes committed with a descriptive commit message

---

### Task 6 — Push and open a pull request

Push your branch to GitHub and open a pull request:

```bash
git push --set-upstream origin firstname-lastname/intro
```

Then go to this repository on GitHub, click **Compare & pull request**, and:
- Write a title like: `GitHub basics — Firstname Lastname`
- In the description, briefly explain one thing that surprised you or that you want to remember
- Request your instructor or a teammate as a reviewer

- [ ] Branch pushed to GitHub
- [ ] Pull request opened with a title, description, and reviewer

---

### Task 7 — Open an Issue

Go to the **Issues** tab of this repository and open a new Issue. In the Issue, describe:
- One concept from this assignment that was confusing or that you want to learn more about
- One question you have about how your FRC team uses GitHub

- [ ] Issue opened with a question or reflection

---

## :star: Optional challenges

These are not required, but they will prepare you for more advanced team workflows.

- **Review a teammate's PR.** Go to the Pull Requests tab, open a classmate's PR, and leave at least one constructive comment on their `student-notes.md` answers.
- **Resolve a merge conflict.** Ask a teammate to edit the same line of `student-notes.md` on a different branch. Then try merging both branches and resolve the conflict in VS Code.
- **Protect the `main` branch.** If you have admin access, go to **Settings → Branches** and add a branch protection rule requiring one PR review before merging.
- **Create your profile README.** Create a repository named exactly after your GitHub username. Add a `README.md` to it — it will appear on your GitHub profile page.

---

## :books: Resources

- [Using GitHub — FRC Tutorial](https://robolancers-java-learning.github.io/FRC-Java-Tutorial/version_control/github/)
- [Basic Git — FRC Tutorial](https://robolancers-java-learning.github.io/FRC-Java-Tutorial/version_control/BasicGit/)
- [Git Workflow — FRC Tutorial](https://robolancers-java-learning.github.io/FRC-Java-Tutorial/version_control/GitWorkflow/)
- [GitHub Docs: Understanding the GitHub flow](https://docs.github.com/en/get-started/using-github/github-flow)
- [GitHub Docs: About branches](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-branches)
- [GitHub Docs: About pull requests](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-pull-requests)
- [Basic writing and formatting syntax (Markdown)](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax)
