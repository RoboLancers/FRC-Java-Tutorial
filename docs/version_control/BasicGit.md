
# Basic Git
<!--Contributed by Carl Stanton -->
------------

Git is a version control system, or a way for us to collaborate on code and manage having multiple versions of code working in parallel

While many modern cloud applications, like google docs, have some form of auto-save to the cloud, we don't necessarily want a single auto-updating version of our code.
As we're working, the changes we make can cause code to break very quickly in ways that might not be immediately obvious, which means it's good to be able to take a "snapshot" of the code at a point where it worked.
So, we use a tool called Git to manage different versions of our files and save them to Github, which lets us share the codebase between computers.
Instead of automatically syncing to Github, we save our code in small named chunks called commits.
These named versions makes it easy to revert changes that break previously working behaviour and see when and by who code was written.
Git also lets us have different, parallel versions, called branches, of code.
This means that while one person works on code for the autonomous, another could work on vision, for example, without overriding each other.

### Resources

Read one of the following:

- [WPILib Git intro](https://docs.wpilib.org/en/stable/docs/software/basic-programming/git-getting-started.html)
- [Github Git intro](https://docs.github.com/en/get-started/using-git/about-git)
- [Github Git intro video (start at 0:43)](https://youtu.be/r8jQ9hVA2qs?t=43)

Install the following:

- [Windows Git install](https://gitforwindows.org/)
- [Linux/Mac Git install](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- [Github Desktop](https://desktop.github.com/download/)

### Basic Git Commands

To use Git, developers use specific commands to copy, create, change, and combine code. These commands can be executed directly from the command line or by using an application like GitHub Desktop. Here are some common commands for using Git:

**git init**: initializes a brand new Git repository and begins tracking an existing directory. It adds a hidden subfolder within the existing directory that houses the internal data structure required for version control.

**git clone** creates a local copy of a project that already exists remotely. The clone includes all the project's files, history, and branches.

**git add** stages a change. Git tracks changes to a developer's codebase, but it's necessary to stage and take a snapshot of the changes to include them in the project's history. This command performs staging, the first part of that two-step process. Any changes that are staged will become a part of the next snapshot and a part of the project's history. Staging and committing separately gives developers complete control over the history of their project without changing how they code and work.

**git commit** saves the snapshot to the project history and completes the change-tracking process. In short, a commit functions like taking a photo. Anything that's been staged with git add will become a part of the snapshot with git commit.

**git status** shows the status of changes as untracked, modified, or staged.

**git branch** shows the branches being worked on locally.

**git merge** merges lines of development together. This command is typically used to combine changes made on two distinct branches. For example, a developer would merge when they want to combine changes from a feature branch into the main branch for deployment.

**git pull** updates the local line of development with updates from its remote counterpart. Developers use this command if a teammate has made commits to a branch on a remote, and they would like to reflect those changes in their local environment.

**git push** updates the remote repository with any commits made locally to a branch.

For more information, see the [full reference guide to git commands](https://git-scm.com/docs).

**Git Log**
>Git log is a tool to view your history in Git. By default Git Log shows all of the commits in the repository, ordered from newest to oldest. in VSCode, the graph panels shows the Git log/History in a visual way. 
![Graph view in VS code](../assets/git_graph_vscode.png)

### Github Setup

Github is a website that hosts Git repositories. The Robolancers store all of their codes on Github.

1. Sign up for a Github Account here: [Sign up for Github](https://github.com/signup?ref_cta=Sign+up&ref_loc=header+logged+out&ref_page=%2F&source=header-home)
2. Enable 2 factor Authentitaction (Required to add code): [2fa setup](https://docs.github.com/en/authentication/securing-your-account-with-two-factor-authentication-2fa/configuring-two-factor-authentication)

### Examples

![A simple demonstration of committing and pushing some changes in git](../assets/GitExample.png)

- Typing `git add .` then `git commit -m "commit name"` then `git push` is the bread and butter of using Git.
  This sequence tells Git to collect all of your uncommitted changes, commit them, then push them to Github.
  If you'd like to combine these into one command, you can add an alias to your .gitconfig file (ask a lead or mentor for help).
- `git checkout branch-name` switches between branches
- `git checkout -b new-branch` makes a new branch and switches to it.
  Note that the first time you push from this branch you will need to enter some extra parameters, but the terminal should prompt you with the correct command when you enter `git push`
- `git status` displays the current branch and what changes you have uncommitted and staged
- `git pull` updates the code on your device with the latest code from Github.
  Recommended to do this whenever someone else has been working on the same branch, otherwise you might make conflicting changes
- [A simple demo video of committing some changes](../assets/GitDemoVideo.mp4)

### Managing Git in Vscode

#### Git Commands in Vscode GUI

![Git commands in vscode GUI](../assets/git_vscode.png)

#### Git branches menu (Accessed by clicking on the branch name at the bottom left)

![Github branch menu in Vscode GUI](../assets/github_branch_vscode.png)

#### Merge in another branch in vscode

![Github Merge menu](../assets/github_GUI_Merge.png)

### Notes

- We use GitHub's pull request (PR) feature to manage branches and merges.
Always make sure to merge to main using a PR.
PR's will be explained further in the [Git Workflow docs](1.3_GitWorkflow.md).
- Always commit code that at the very least compiles (you can check this by running the "Build robot code" option in WPILib's command bar)
- Commit messages should be short (~10 words), simple, and descriptive.
  If it's too long, use multiple lines in the commit message
- Commits should be frequent. Whenever you reach a working state, you should commit before you accidentally break anything again
- Don't commit directly to the `main` branch, as `main` should **always** contain working code.
  New code should be developed on separate branches and can only be merged through a pull request after being reviewed and approved.
- **ALWAYS ALWAYS ALWAYS** commit and push before you leave a meeting, especially if you are using a computer that is not yours!
  It is never fun to have to commit someone else's code at the start of the day or find out an hour in that you've been working off of someone else's uncommitted (potentially broken!) code.
  Uncommitted code also makes it harder to track what is and isn't finished.
- Run a `git status` at the start of a meeting to make sure you committed your code and that you are on the right branch

### Names
