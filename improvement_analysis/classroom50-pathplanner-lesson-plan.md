# Plan: Classroom50 Lesson — "PathPlanner-Autonomous"

## Context

[pathplanner-tutorial-plan.md](pathplanner-tutorial-plan.md) plans two *reference* docs pages (`pathplanner_overview.md`, `pathplanner_autonomous.md`) for this tutorial site. This plan is the hands-on practice companion: a classroom50 assignment in the separate **RoboLancers-Java-Learning/management** repo, which is the org's classroom50 config/template repo (confirmed structure: `.github/workflows/setup-lesson.yml` provisions a per-student repo from a `workflow_dispatch` choice of lesson name; `shared-devcontainer/.devcontainer/` is the common Codespaces environment for all WPILib-based lessons; each lesson directory — `Basic-DriveTrain/`, `GitHub-Basics/`, `Swerve-Drive/` — is a full Gradle project with `unitN-topic.md` lesson pages, matching `src/test/java/frc/robot/UnitNXxxTest.java` JUnit graders, and (for JSON-config units) `grading/*.sh` scripts).

This lesson assumes the student has completed the **Swerve-Drive** lesson (a working, filled-in `SwerveSubsystem` + `deploy/swerve/` JSON) — it is not re-teaching swerve config, it's teaching the PathPlanner GUI → deploy folder → Java autonomous pipeline on top of that.

Per the user's request, this plan covers:
1. A branch in `RoboLancers-Java-Learning/management` to do the work on.
2. A `shared-devcontainer` update to install the PathPlanner GUI (verified concretely below).
3. Five units: **Creating a Path**, **Creating an Auto**, **Creating a NamedCommand**, **Adding an AutoChooser**, **Full choose-and-run sequence verified in sim**.

Everything below was checked against the real repo content (not assumed): `shared-devcontainer/.devcontainer/{Dockerfile,post-create.sh,post-attach.sh}`, `Swerve-Drive/{unit1..5}-*.md`, `Swerve-Drive/grading/check_swerve_config.sh`, `Swerve-Drive/src/test/java/frc/robot/Unit3DriveCommandTest.java`, `Swerve-Drive/vendordeps/`, `docs/TEMPLATES.md`, `docs/CONFIGURATION.md`, and `.github/workflows/setup-lesson.yml`. The PathPlanner GUI's actual Linux release (`mjansen4857/pathplanner` v2026.1.2) was downloaded and inspected — it's a self-contained Flutter/GTK Linux bundle (top-level `pathplanner` binary + `lib/` + `data/`), **not** an Electron app, so it doesn't need the `--no-sandbox` wrapper that `AdvantageScope`/`Elastic` do.

**`management` vs. `classroom50` — two different repos, two different jobs.** `RoboLancers-Java-Learning/management`'s `setup-lesson.yml` is a homegrown workflow that authors/refreshes **template repos** (`Basic-DriveTrain-2026`, `Swerve-Drive-2026`, etc., each flagged `is_template: true`). It is *not* classroom50 itself. The actual classroom50 config repo is the separate `RoboLancers-Java-Learning/classroom50` (public; holds `frc-java/{classroom.json,assignments.json,students.csv,scores.json}` and `autograders/`), and it's what turns a template repo into something a student can actually `gh student accept`. Steps 1–5 below build the template; Step 6 is the classroom50 registration that was missing from the first version of this plan.

***

## Step 1 — Branch

```sh
git clone https://github.com/RoboLancers-Java-Learning/management.git
cd management
git checkout -b lesson/pathplanner-autonomous
```

All work below happens on this branch; open a PR into `main` when done (the repo's default branch protection requires 1 approving review + passing `spotless`/`build` checks per `docs/CONFIGURATION.md` — a mentor review is expected here, not a direct merge).

***

## Step 2 — Update `shared-devcontainer` to install PathPlanner

This is shared across **all** WPILib lessons (`Basic-DriveTrain`, `Swerve-Drive`, and the new lesson), which is correct — it's a generally useful tool, not lesson-specific.

**`shared-devcontainer/.devcontainer/post-create.sh`** — add a block immediately after the existing "Installing Elastic..." block (same pattern as `AdvantageScope`/`Elastic`, but simpler since PathPlanner's Linux bundle isn't Electron):

```bash
echo "Installing PathPlanner..."
mkdir -p ~/wpilib/2026/pathplanner
wget -q "https://github.com/mjansen4857/pathplanner/releases/download/v2026.1.2/PathPlanner-Linux-v2026.1.2.zip" -O /tmp/pathplanner.zip
unzip -q /tmp/pathplanner.zip -d ~/wpilib/2026/pathplanner
chmod +x ~/wpilib/2026/pathplanner/pathplanner
rm /tmp/pathplanner.zip
```

Unlike `AdvantageScope`/`Elastic`, **do not** add this to `$TOOLS_DIR`/`tools.json` — those exist specifically for the vscode-wpilib extension's built-in "WPILib: Start Tool" quick-pick, which has a fixed list of known WPILib tool names; PathPlanner isn't one of them, and dropping a same-named binary in that directory won't add it to that menu. Instead, expose it directly on `PATH` so students can run `pathplanner` from the integrated terminal or a `.vscode/tasks.json` task:

**`shared-devcontainer/.devcontainer/devcontainer.json`** — extend the existing `remoteEnv`:

```json
"remoteEnv": {
  "DISPLAY": ":1",
  "WPILIB_SIMULATION": "true",
  "PATH": "${containerEnv:PATH}:/home/vscode/wpilib/2026/pathplanner"
}
```

**`shared-devcontainer/.devcontainer/Dockerfile`** or the `apt-get install` list in `post-create.sh` — PathPlanner's Linux bundle links GTK3, which isn't currently in the apt list (`libgl1`, `libxrender1`, `libxrandr2`, `libxinerama1`, `libxi6`, `libxext6`, `libx11-dev`, `xauth`, `x11-apps` — no GTK). Add `libgtk-3-0` to the `sudo apt-get install` list in `post-create.sh`. **Verify the exact package name at implementation time** — Ubuntu 24.04 (the Dockerfile's base image) renamed many `t64`-transitioned packages, so it may need to be `libgtk-3-0t64` instead. Confirm with `apt-cache search libgtk-3` inside the container before committing.

Students launch it with `pathplanner` in the terminal (auto-detects the project's `src/main/deploy/pathplanner/` folder when run from the repo root), viewable through the same desktop-lite noVNC session (port 6080) already forwarded for WPILib sim GUIs — no new forwarded port needed.

***

## Step 3 — New lesson directory: `PathPlanner-Autonomous/`

Mirror `Swerve-Drive/`'s layout exactly (confirmed file-for-file from the real repo):

```
PathPlanner-Autonomous/
├── .githooks/pre-push                      # copy from Swerve-Drive/.githooks
├── .gitignore                              # copy from Swerve-Drive/.gitignore
├── .vscode/{launch.json,settings.json}     # copy from Swerve-Drive/.vscode
├── .wpilib/wpilib_preferences.json         # copy from Swerve-Drive/.wpilib (projectYear templated by setup-lesson.yml)
├── WPILib-License.md                       # copy from Swerve-Drive
├── build.gradle, settings.gradle           # copy from Swerve-Drive
├── gradle/wrapper/*, gradlew, gradlew.bat  # copy from Swerve-Drive
├── README.md                                # one-liner title, matches Basic-DriveTrain's minimal style
├── vendordeps/
│   ├── Phoenix6-26.3.0.json                # copied from Swerve-Drive (mixed-hardware YAGSL support)
│   ├── REVLib.json, ReduxLib.json, ThriftyLib.json, WPILibNewCommands.json
│   ├── yagsl-2026.4.1.json
│   └── PathplannerLib-2026.1.2.json        # NEW — https://3015rangerrobotics.github.io/pathplannerlib/PathplannerLib.json
├── src/main/deploy/
│   ├── swerve/...                          # copied from Swerve-Drive's SOLUTION state (already filled in — this
│   │                                        # lesson is not re-teaching swerve config)
│   └── pathplanner/
│       ├── settings.json                   # pre-seeded robot config for "RoboLancers Practice Swerve Bot"
│       │                                    # (reuse the module CAN IDs/offsets from Swerve-Drive's unit1 spec sheet;
│       │                                    # add fictional mass/MOI/motor-type numbers this lesson introduces)
│       ├── navgrid.json                    # default navgrid, from PathPlanner's own default_navgrid.json
│       ├── paths/                          # empty — student creates PracticePath.path here in Unit 1
│       └── autos/                          # empty — student creates PracticeAuto.auto here in Unit 2
├── src/main/java/frc/robot/
│   ├── Robot.java, Main.java                # copy from Swerve-Drive
│   ├── Constants.java                       # copy from Swerve-Drive
│   ├── RobotContainer.java                  # forked from Swerve-Drive's SOLUTION RobotContainer, but with
│   │                                         # AutoBuilder.configure(...) already wired (pre-completed — that
│   │                                         # plumbing was covered in the Swerve-Drive lesson's spirit and in
│   │                                         # docs/programming/pathplanner_autonomous.md, not a goal of *this*
│   │                                         # lesson), plus TWO new stub points for Units 3 & 4 (see below)
│   ├── subsystems/swervedrive/SwerveSubsystem.java   # copy from Swerve-Drive's SOLUTION (already implemented)
│   └── subsystems/ExampleSubsystem.java     # NEW, minimal — one boolean field (`running`) + a `setRunning(boolean)`
│                                             # command factory, so Unit 3's NamedCommand has something to observably
│                                             # affect without needing real hardware
├── src/test/java/frc/robot/
│   ├── Unit3NamedCommandTest.java           # NEW
│   ├── Unit4AutoChooserTest.java            # NEW
│   └── Unit5AutonomousSimTest.java          # NEW
├── grading/
│   ├── check_path_waypoints.py              # NEW (Unit 1)
│   └── check_auto_structure.py              # NEW (Unit 2)
├── unit1-creating-a-path.md                 # NEW
├── unit2-creating-an-auto.md                # NEW
├── unit3-named-command.md                   # NEW
├── unit4-auto-chooser.md                    # NEW
└── unit5-full-auto-sim-verification.md      # NEW
```

### Starter-code stub points (what the student actually fills in)

- **`RobotContainer.java`**: an unfinished `configureNamedCommands()` method (Unit 3 fills in the `NamedCommands.registerCommand(...)` call) and an unfinished `configureAutoChooser()` + `getAutonomousCommand()` (Unit 4 fills in `AutoBuilder.buildAutoChooser()` / `SmartDashboard.putData(...)` / `return autoChooser.getSelected();`) — same "stub method throws `UnsupportedOperationException` until implemented" pattern already used in `Unit3DriveCommandTest`'s target class.
- **PathPlanner GUI-authored files** (Units 1–2) aren't Java — they're `.path`/`.auto` JSON the student creates *in the app itself*, per the existing precedent of GUI/config-tool-authored JSON not going through hand-editing.

***

## Step 4 — The five units

Each unit follows the established format exactly (`Objective` / `Estimated time` / `What You'll Do` / `Why This Matters` / `Verification` / `Next Step`), and cross-links back to `docs/programming/pathplanner_overview.md` and `docs/programming/pathplanner_autonomous.md` from [pathplanner-tutorial-plan.md](pathplanner-tutorial-plan.md) the same way `Swerve-Drive`'s units link to `yagsl_swerve_tutorial.md`.

### Unit 1 — Creating a Path
- **Objective**: launch `pathplanner` from the terminal, open the repo root as the project, fill in the robot config (mass/MOI/module geometry — reuses the "RoboLancers Practice Swerve Bot" identity from Swerve-Drive's spec sheet), then draw a path named `PracticePath` matching a given waypoint spec sheet (start pose, one interior waypoint, end pose, a max-velocity constraint zone).
- **Verification**: `python3 grading/check_path_waypoints.py` — parses `src/main/deploy/pathplanner/paths/PracticePath.path` JSON and checks waypoint anchor positions / constraint values against the spec, tolerant float comparison, `PASS`/`FAIL` per field (same shape as `check_swerve_config.sh`, ported to Python since `.path` JSON nesting is deeper).

### Unit 2 — Creating an Auto
- **Objective**: in the GUI, create `PracticeAuto` that runs `PracticePath`, with a named-command event marker `"IntakeDown"` placed partway through (typed as plain text in the GUI — it doesn't need to resolve to a real Java command yet).
- **Verification**: `python3 grading/check_auto_structure.py` — parses `autos/PracticeAuto.auto`, checks it references `PracticePath` and contains an event marker whose name is exactly `IntakeDown` (event-marker string must match the `NamedCommands` key exactly — call this out explicitly, it's the single most common real-world PathPlanner mistake per both reference repos surveyed in the docs-page plan).

### Unit 3 — Creating a NamedCommand
- **Objective**: in `RobotContainer.configureNamedCommands()`, call `NamedCommands.registerCommand("IntakeDown", exampleSubsystem.setRunning(true).withTimeout(1))` (or equivalent) so the Unit 2 event marker resolves to something real.
- **Verification**: `Unit3NamedCommandTest.java` — HAL-sim JUnit test (same style as `Unit3DriveCommandTest`) asserting `NamedCommands.hasCommand("IntakeDown")` and that scheduling the registered command drives `ExampleSubsystem.isRunning()` to `true`.

### Unit 4 — Adding an AutoChooser
- **Objective**: in `RobotContainer.configureAutoChooser()` / `getAutonomousCommand()`, wire `autoChooser = AutoBuilder.buildAutoChooser(); SmartDashboard.putData("Auto Chooser", autoChooser);` and return `autoChooser.getSelected()`.
- **Verification**: `Unit4AutoChooserTest.java` — reflection check that `getAutonomousCommand()` is public/non-throwing, plus a HAL-sim check that it returns a non-null `Command` once `AutoBuilder` has paths loaded from the deploy directory.

### Unit 5 — Full autonomous sequence: choose, run, verify in sim
- **Objective**: put it all together — confirm `PracticeAuto` is selectable and selected in the chooser, then actually run it.
- **What You'll Do**: `./gradlew simulateJava` (or VS Code's "WPILib: Simulate Robot Code"), open the sim GUI (Glass or AdvantageScope, both already installed) through the desktop-lite noVNC session, enable autonomous, and visually confirm the robot follows `PracticePath` on the field view and that the `IntakeDown` marker visibly fires (e.g. print a line to the console, or flip a SmartDashboard boolean, from `ExampleSubsystem` — there's no real hardware to observe).
- **Verification** (automated): `Unit5AutonomousSimTest.java` — an integration JUnit test that initializes HAL, schedules `robotContainer.getAutonomousCommand()` on `CommandScheduler`, advances simulated time in a loop (`SimHooks.stepTiming(...)` + `CommandScheduler.getInstance().run()`), and asserts (a) the command finishes within an expected time bound, (b) the drivetrain's final simulated pose is within tolerance of `PracticePath`'s end pose, and (c) `ExampleSubsystem.isRunning()` was observed `true` at some point during the run. This is the automated proxy for "verify it in sim" since the visual GUI confirmation itself can't be scripted — the unit's markdown explicitly frames the GUI step as the *real* verification and the JUnit test as the graded proxy.
- **Next Step**: none — this is the capstone; point back to `docs/programming/pathplanner_autonomous.md`'s debugging/telemetry section for what to do if the robot doesn't follow the path correctly (alliance-flip flag, event-marker name mismatches, `PPHolonomicDriveController` gains).

***

## Step 5 — Wire into `setup-lesson.yml` (builds the *template repo*, not the student's copy)

Two changes to `.github/workflows/setup-lesson.yml`:

1. Add `"PathPlanner-Autonomous"` to the `lesson_name` `workflow_dispatch` choice `options` list.
2. Add a `setup-pathplanner-autonomous:` job, copy-pasted from `setup-swerve-drive:` with `if: inputs.lesson_name == 'PathPlanner-Autonomous'` and all `${{inputs.lesson_name}}` path references pointing at the new directory — it already copies `.devcontainer` from `shared-devcontainer` (Step 2 lands there automatically), `.githooks`, `.vscode`, `.wpilib`, gradle/build files, `grading/`, `src/`, and `vendordeps/`, which is exactly the same shape as this lesson.

**Important correction from the original version of this plan:** this step only produces `RoboLancers-Java-Learning/PathPlanner-Autonomous-2026` — the shared **template** repo (`is_template: true`), same as `Swerve-Drive-2026` already is. It does **not** put the lesson in front of any student, and re-running it for each student is the wrong model. The earlier "Setup CI Workflow" step / branch-protection concern in the old Step 5 was also a conflation — `docs/CONFIGURATION.md`'s `spotless`/`build` branch-protection story governs the **team-season robot code repos** provisioned by `setup-repository.yml`, a different system from how classroom50 grades assignment repos (see Step 6). No CI workflow needs to be authored here at all.

***

## Step 6 — Register it as a real classroom50 assignment (this is how students actually get it)

Verified against the live config repo `RoboLancers-Java-Learning/classroom50`: it has one classroom, `frc-java`, with two assignments already registered in `frc-java/assignments.json` — `basic-driving-robot-talon` (template `Basic-DriveTrain-2026`) and `github-basics` (template `Github-Basics-2026`). **`Swerve-Drive-2026` is not yet registered as an assignment**, even though the template repo exists — that gap should probably be closed alongside this work, since `PathPlanner-Autonomous` is meant to follow it in sequence.

**Register the assignment** (run once, by whoever holds the teacher role for `frc-java`):

```sh
gh teacher assignment add RoboLancers-Java-Learning frc-java pathplanner-autonomous \
  --name "PathPlanner Autonomous" \
  --template RoboLancers-Java-Learning/PathPlanner-Autonomous-2026 \
  --description "Build and run an autonomous routine with PathPlanner, on top of a completed swerve drivetrain." \
  --due <ISO-8601 date>
```

This writes a new entry into `frc-java/assignments.json`, exactly like the two existing entries. If `Swerve-Drive-2026` also gets registered (see above), do it the same way with slug e.g. `swerve-drive`.

**Author the five units' grading as declarative tests** — this is how `basic-driving-robot-talon` is already graded (its `tests` array is inline in `assignments.json`, each entry running `./gradlew test --tests '...'`), not via a bespoke CI/branch-protection workflow. Author one test per unit:

```sh
gh teacher assignment test add RoboLancers-Java-Learning frc-java pathplanner-autonomous \
  --name "Unit 1: Creating a Path" --type run \
  --run "python3 grading/check_path_waypoints.py" --points 15

gh teacher assignment test add RoboLancers-Java-Learning frc-java pathplanner-autonomous \
  --name "Unit 2: Creating an Auto" --type run \
  --run "python3 grading/check_auto_structure.py" --points 15

gh teacher assignment test add RoboLancers-Java-Learning frc-java pathplanner-autonomous \
  --name "Unit 3: Creating a NamedCommand" --type run \
  --setup "chmod +x ./gradlew" --run "./gradlew test --tests 'frc.robot.Unit3NamedCommandTest'" --points 20 --timeout 300

gh teacher assignment test add RoboLancers-Java-Learning frc-java pathplanner-autonomous \
  --name "Unit 4: Adding an AutoChooser" --type run \
  --run "./gradlew test --tests 'frc.robot.Unit4AutoChooserTest'" --points 20 --timeout 300

gh teacher assignment test add RoboLancers-Java-Learning frc-java pathplanner-autonomous \
  --name "Unit 5: Full autonomous sequence in sim" --type run \
  --run "./gradlew test --tests 'frc.robot.Unit5AutonomousSimTest'" --points 30 --timeout 300
```

(Or author all five at once as a bare JSON array and pass `--tests units.json` to `assignment add`.) On the next push to the config repo, `publish-pages.yaml` runs `materialize_tests.py`, which writes these into `frc-java/autograders/pathplanner-autonomous/tests.json` and bundles it for `runner.py` to fetch at grade time — no hand-written `autograder.py` needed, matching the pattern already used for `basic-driving-robot-talon`.

**Why this resolves "build off a completed lesson":** classroom50's `gh student accept` always generates a student's repo from the *same static template* — there is no mechanism to fork forward from each individual student's own prior submission, and there shouldn't be (a buggy Swerve-Drive solution would otherwise propagate into every later assignment's grading). The correct model, and the one this plan already uses in Step 3, is: the **template's starter files** are the canonical, already-solved Swerve-Drive state (the answer key), authored once by a mentor into `management/PathPlanner-Autonomous/`, not sourced from any individual student's repo. Every student who accepts this assignment starts from that same known-good baseline, same as how `Swerve-Drive`'s own starter doesn't literally fork anyone's `Basic-DriveTrain` submission either.

**How a student actually gets the lesson**, once registered:

```sh
gh student accept RoboLancers-Java-Learning frc-java pathplanner-autonomous
```

This creates `RoboLancers-Java-Learning/frc-java-pathplanner-autonomous-<username>`, generated from the template, with the autograde shim already wired in. `gh student submit` (or a plain `git push origin main`) triggers grading against the five declarative tests above, and `collect-scores.yaml` rolls the result into `frc-java/scores.json` like every other assignment.

***

## Verification (end-to-end, before opening the PR)

1. Locally build the devcontainer image (or push the branch and let Codespaces prebuild) and confirm `pathplanner` launches from the integrated terminal and shows the field view over noVNC.
2. From a scratch checkout of `PathPlanner-Autonomous/`, complete Units 1–2 by hand in the GUI and confirm `grading/check_path_waypoints.py` and `grading/check_auto_structure.py` both print all-`PASS`.
3. Implement Units 3–5's fill-ins and run `./gradlew test` — all `UnitNXxxTest` classes should pass.
4. Run `./gradlew simulateJava`, confirm the auto actually drives the simulated pose along the path in the sim GUI.
5. Trigger `workflow_dispatch` on `setup-lesson.yml` with `lesson_name: PathPlanner-Autonomous` to produce `PathPlanner-Autonomous-2026`, and confirm it's flagged as a template repo.
6. Run Step 6's `gh teacher assignment add` / `assignment test add` commands against `frc-java`, then `gh teacher assignment list RoboLancers-Java-Learning frc-java --json` to confirm the new entry and its tests landed correctly.
7. As a test student (or with `--dry-run` if available), run `gh student accept RoboLancers-Java-Learning frc-java pathplanner-autonomous` and confirm the generated repo builds, and that a throwaway push triggers the autograde workflow and produces a `result.json` release with all five tests reporting.
8. Open the PR from `lesson/pathplanner-autonomous` into `management`'s `main` for mentor review (required per branch protection) — this only covers Steps 1–5; Step 6 happens separately against the `classroom50` config repo once the template PR is merged.
