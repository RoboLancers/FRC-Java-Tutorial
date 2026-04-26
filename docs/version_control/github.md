# Using GitHub
<!-- This page was contributed by: Carl Stanton -->

GitHub is where your team's code lives in the cloud. If Git is the engine that tracks your changes, GitHub is the garage where everyone parks, shares, and reviews those changes. This page will walk you through the GitHub-specific skills you need to collaborate on an FRC robot project.

!!! note
    This page assumes you are comfortable with the core Git commands (`add`, `commit`, `push`, `pull`, `branch`) covered in [Basic Git](BasicGit.md). If those feel unfamiliar, start there first.

***

## Creating a GitHub Account

If you do not already have a GitHub account, you will need one before you can push code or collaborate with your team.

1. Go to [github.com/signup](https://github.com/signup){target=_blank} and create a free account.
2. Choose a username that is professional — your team leads and potential employers may see it.
3. **Enable two-factor authentication (2FA).** GitHub requires 2FA before you can push code. Follow the [GitHub 2FA setup guide](https://docs.github.com/en/authentication/securing-your-account-with-two-factor-authentication-2fa/configuring-two-factor-authentication){target=_blank} to set it up with an authenticator app.

!!! warning
    Do not skip 2FA. GitHub will block your pushes until it is enabled. Set it up right after you create your account.

Once your account is ready, ask your team lead or mentor to add you to your team's GitHub organization so you can access the robot code repositories.

***

## Creating a Repository

A repository (repo) is a project folder tracked by Git. For most FRC work you will be cloning an existing repository — but knowing how to create one is useful for personal projects and new robot seasons.

### Creating a repo on GitHub

1. Click the **+** icon in the top-right corner of GitHub and choose **New repository**.
2. Give the repository a name (e.g., `2026-Robot-Code`).
3. Choose **Private** unless you want the code to be public.
4. Check **Add a README file** to initialize the repo.
5. In the **Add .gitignore** dropdown, you can choose a template — but for WPILib projects you will want to customize it yourself (covered in the [`.gitignore` section](#gitignore-for-wpilib-projects) below).
6. Click **Create repository**.

!!! tip
    Your team's organization should own the repository, not your personal account. That way, the code stays with the team even if you graduate. Ask a lead or mentor to create the repo under the organization.

***

## Cloning a Repository

Cloning downloads a copy of a remote repository to your computer and sets up the connection so you can push and pull changes.

### How to clone

1. On the GitHub repository page, click the green **Code** button.
2. Copy the HTTPS URL (e.g., `https://github.com/RoboLancers/2026-Robot-Code.git`).
3. Open a terminal (or Git Bash on Windows) and run:

```bash
git clone https://github.com/RoboLancers/2026-Robot-Code.git
```

This creates a folder with the repository contents in your current directory. Inside that folder, Git is already initialized and the remote (`origin`) is set to the GitHub URL — you can `push` and `pull` right away.

### Cloning in GitHub Desktop

If you prefer a graphical interface, open GitHub Desktop, click **File → Clone repository**, and paste the URL. GitHub Desktop will handle the rest.

!!! note
    In VS Code, you can also use **File → New Window**, then click **Clone Git Repository** on the welcome screen and paste the URL directly.

***

## Fork vs. Clone — What Is the Difference?

These two words are easy to confuse, but they describe different things:

| | Clone | Fork |
|---|---|---|
| **What it does** | Downloads a repo to your computer | Creates your own copy of a repo on GitHub |
| **Where the copy lives** | Your local machine | Your GitHub account (in the cloud) |
| **Linked to the original?** | Yes — `origin` points to the source repo | Loosely — you can add the original as `upstream` |
| **When to use it** | Getting code from a repo you have access to | Contributing to a repo you do not own |

**For FRC team work:** you almost always want to **clone** the team's repository directly. Everyone clones the same repo and uses branches to work in parallel.

**Forks are for open-source contributions.** If you wanted to contribute to the WPILib project itself, you would fork it to your own GitHub account, make changes, and then open a pull request back to the original. Your team's robot repo is not open source in that way — just clone it.

!!! tip
    If you ever see instructions online that say "fork this repo," read carefully. For your team's robot code, you probably want to clone, not fork.

***

## Pull Requests

A pull request (PR) is the official way to propose that your branch's changes be merged into another branch (usually `main`). It is also where code review happens.

The [Git Workflow page](GitWorkflow.md) covers how PRs fit into the overall development flow. This section goes deeper on the mechanics.

### Why pull requests matter

- They prevent unreviewed code from breaking `main`.
- They give your teammates a chance to catch bugs before they end up on the robot.
- They create a permanent record of what changed and why.

### Opening a pull request

1. Make sure your branch is pushed to GitHub (`git push`). If it is a new branch, Git will tell you to run `git push --set-upstream origin <branch-name>`.
2. Go to the repository on GitHub. You will usually see a yellow banner saying your branch had recent pushes — click **Compare & pull request**. If the banner is gone, click the **Pull requests** tab and then **New pull request**.
3. Set the **base** branch to `main` (or whichever branch you are merging into) and the **compare** branch to your feature branch.
4. Write a clear title and description. Explain *what* changed and *why*. If the PR fixes an Issue, write `Closes #42` (with the issue number) in the description.
5. Under **Reviewers**, request at least one teammate, lead, or mentor to review your code.
6. Click **Create pull request**.

### The review process

Your reviewer will look at the **Files changed** tab to see exactly what lines you added or removed. They can leave comments on specific lines, ask questions, or request changes.

- If they **approve** the PR, you (or a lead) can merge it.
- If they **request changes**, address the feedback, push new commits to your branch, and the PR updates automatically.

!!! tip
    Be a good reviewer yourself. When you review a teammate's PR, look for things like: off-by-one errors in sensor thresholds, motors spinning the wrong direction, missing null checks, and inconsistent naming. Leave encouraging comments too — good reviews build team trust.

### Merging

Once approved, click **Merge pull request** on GitHub. Choose **Squash and merge** if you want to combine all your commits into one clean commit on `main`. After merging, delete the feature branch — it has served its purpose.

!!! note
    On FRC repositories, the `main` branch is often protected, meaning only leads or mentors can actually click the merge button. That is by design — it keeps `main` stable.

***

## Branch Management

Branches let multiple people work simultaneously without stepping on each other's code. Here is a quick reference for the most common branch tasks.

### Creating and switching branches

```bash
# Create a new branch and switch to it
git checkout -b your-name/feature-description

# Switch to an existing branch
git checkout branch-name

# See all branches (local and remote)
git branch -a
```

### Naming your branches

Good branch names are short, lowercase, and descriptive. Many teams use a `name/feature` format:

```
mar-liu/auto-balance
carl-stanton/fix-intake-pid
add-vision-subsystem
```

Avoid generic names like `my-branch` or `test` — after a few weeks of build season there will be dozens of branches and nobody will know what is in them.

### Keeping your branch up to date

If `main` has received new commits while you were working on your branch, you should pull those changes in before you open your PR:

```bash
# While on your feature branch:
git fetch origin
git merge origin/main
```

This reduces the chance of merge conflicts when your PR is reviewed.

!!! warning
    If you and a teammate both edit the same lines of the same file, Git cannot automatically merge the changes — this is a merge conflict. VS Code highlights conflicts and gives you options to accept one side or combine both. Resolve conflicts carefully, then commit the result. When in doubt, ask a teammate.

### Deleting stale branches

After a branch is merged, delete it to keep the repo tidy:

```bash
# Delete a local branch (after it is merged)
git branch -d branch-name

# Delete the remote branch on GitHub
git push origin --delete branch-name
```

GitHub also offers a **Delete branch** button directly on a merged PR page.

***

## GitHub for FRC Team Collaboration

### Team organization structure

Your team's code should live under a **GitHub Organization**, not under any one person's personal account. An organization lets you:

- Add and remove members without losing access to repos.
- Set different permission levels for different members.
- Keep all repos in one place across seasons.

Ask a mentor or lead to create the organization (e.g., `github.com/RoboLancers`) if your team does not already have one.

### Repository permissions

GitHub has several permission levels. Here is how most FRC teams set them up:

| Role | Typical Members | What They Can Do |
|---|---|---|
| **Owner** | Mentors | Full admin control |
| **Maintainer / Lead** | Software leads | Merge PRs, manage branches, edit settings |
| **Write** | Active programmers | Push branches, open PRs |
| **Read** | Other team members | View and clone code |

As a student programmer, you will usually have **Write** access — enough to push your branches and open PRs, but not enough to accidentally break protected branches.

### Using Issues to track work

GitHub Issues are how your team keeps track of what needs to be built, fixed, or investigated. Before you start coding a new feature, check if there is already an open Issue for it. If there is not, create one.

A good issue includes:
- A clear title (e.g., "Intake rollers spin backwards on deploy")
- Steps to reproduce the problem (for bugs) or a description of the desired behavior (for features)
- Any relevant context (match video clip, sensor readings, etc.)

Link your PR to an issue by writing `Closes #<issue number>` in the PR description — GitHub will automatically close the issue when the PR merges.

### Protecting the `main` branch

In your repository settings, you can require pull request reviews before merging. To set this up (owners and maintainers only):

1. Go to **Settings → Branches**.
2. Click **Add branch protection rule**.
3. Enter `main` as the branch name pattern.
4. Check **Require a pull request before merging** and set the required number of approvals to 1.
5. Click **Create**.

This makes it impossible to push directly to `main` by accident, which keeps your production robot code stable.

***

## `.gitignore` for WPILib Projects

A `.gitignore` file tells Git which files and folders to leave out of version control. You do not want to commit auto-generated build output, local configuration, or binary files — they are large, change constantly, and can be regenerated from the source code.

When you create a new WPILib project, it will generate a `.gitignore` for you. A typical FRC `.gitignore` should exclude at least these entries:

```
# WPILib build output
build/
.gradle/

# WPILib local configuration (machine-specific paths, deploy targets)
.wpilib/

# Gradle wrapper native binaries
gradle/

# VS Code local settings (some teams commit .vscode/, but workspace-specific
# files like launch.json are usually excluded)
.vscode/

# macOS filesystem metadata
.DS_Store

# Windows thumbnail cache
Thumbs.db
```

### What should you commit?

Commit everything that defines your robot's behavior:

- All `.java` source files under `src/`
- `build.gradle` and `settings.gradle`
- `vendordeps/` folder (contains JSON files for REV, CTRE, etc.)
- `.gitignore` itself
- `gradlew` and `gradlew.bat` (so anyone can build without installing Gradle separately)

!!! warning
    Do not commit the `build/` directory. It contains compiled bytecode and native binaries that are regenerated every time you build. Committing it wastes space, causes unnecessary merge conflicts, and can hide real source changes in the diff noise.

!!! tip
    If a file you want to ignore is already tracked by Git (because it was committed before you added it to `.gitignore`), you need to un-track it first:
    ```bash
    git rm --cached path/to/file
    ```
    Then commit that removal. After that, `.gitignore` will prevent the file from being tracked again.

***

## Quick Reference: Common GitHub Workflows

### Starting work on a new feature

```bash
git checkout main
git pull                          # get the latest code
git checkout -b your-name/feature # create your branch
# ... write code ...
git add .
git commit -m "Add feature X"
git push --set-upstream origin your-name/feature
# Open a pull request on GitHub
```

### Picking up someone else's branch

```bash
git fetch origin
git checkout their-name/feature   # Git sets up tracking automatically
git pull                          # get the latest commits on that branch
```

### Resolving a merge conflict in VS Code

1. Run `git merge origin/main` on your branch.
2. VS Code will highlight conflicted files in red in the Source Control panel.
3. Open each conflicted file. You will see markers like `<<<<<<< HEAD` and `>>>>>>> origin/main` around the conflicting sections.
4. Use the inline buttons (**Accept Current**, **Accept Incoming**, **Accept Both**) or edit the file manually.
5. After resolving all conflicts, stage the files with `git add` and commit.

***

## Summary

| Concept | What to remember |
|---|---|
| **Clone** | Download a repo you have access to. |
| **Fork** | Copy a repo you do not own, to contribute back via PR. |
| **Pull Request** | The official way to propose and review changes before merging. |
| **Branch protection** | Prevents direct pushes to `main`; enforces PR review. |
| **`.gitignore`** | Exclude `build/`, `.wpilib/`, `.gradle/` from commits. |
| **Issues** | Track bugs and features; link them to PRs with `Closes #N`. |

For the overall day-to-day branching and PR workflow your team uses, see [Git Workflow](GitWorkflow.md). For a refresher on Git commands themselves, see [Basic Git](BasicGit.md).
