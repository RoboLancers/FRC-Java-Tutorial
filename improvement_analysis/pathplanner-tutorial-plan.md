# Plan: PathPlanner Overview + Creating Autonomous Routines with PathPlanner

## Context

`docs/programming/autonomous.md` currently teaches only hand-coded autonomous (`SequentialCommandGroup`, `WaitCommand`, drive-by-encoder-distance) and explicitly flags PathPlanner as future work in its own text ("replacing this line with a `SendableChooser` is a natural next step") and in `improvement_analysis/wip-completion-plan-autonomous.md` ("Add PathPlanner integration (optional but modern)"). The site also already has two complete swerve tutorials — [yagsl_swerve_tutorial.md](../docs/programming/yagsl_swerve_tutorial.md) and [ctre_swerve_generator_tutorial.md](../docs/programming/ctre_swerve_generator_tutorial.md) — whose code examples (`docs/code_examples/swerve/*`, `docs/code_examples/ctre_swerve/*`) already stub out an unused `SendableChooser<Command> autoChooser` and a `getAutonomousCommand()` returning `Commands.none()`. PathPlanner is the natural next tutorial to fill in those stubs.

Two real, current (2026) team codebases were pulled as ground truth so the tutorial reflects actual usage instead of generic docs paraphrasing:
- **RoboLancers/321-Rebuilt-2026** (`develop` branch) — CTRE swerve generator (`Drivetrain extends TunerSwerveDrivetrain`), hand-wires `AutoBuilder.configure(...)` in its constructor, registers `NamedCommands`, uses `PathPlannerLogging` callbacks into a `Field2d`.
- **RoboLancers/427-Rebuilt-2026** (`main` branch) — YAGSL (`SwerveSubsystem`), same `AutoBuilder.configure(...)` pattern via `SwerveSetpointGenerator`, registers `NamedCommands` + an `EventTrigger`, and exposes a `followPathCommand(String)` helper using `PathPlannerPath.fromPathFile` + `AutoBuilder.followPath`.

Both repos pin `vendordeps/PathplannerLib-2026.1.2.json`, confirming the current vendordep version/URL (`https://3015rangerrobotics.github.io/pathplannerlib/PathplannerLib.json`, resolves to 2026.1.2) and the standard deploy layout `src/main/deploy/pathplanner/{paths/, autos/, navgrid.json, settings.json}`.

## Pages to Add

### 1. `docs/programming/pathplanner_overview.md` — "PathPlanner Overview"

Covers the GUI application and project setup, not robot code. Structure (per `template_page.md` / `documentation-writer-agent.md` conventions — contributor comment, `## Overview`, `***` dividers, `!!! tip/note` admonitions):

- **What PathPlanner is** and why to use it over hand-coded autonomous (visual path editor, holonomic + differential drive support, on-the-fly replanning, GUI-driven event markers) — link back to `autonomous.md` as "what you'd otherwise hand-code."
- **Prerequisites**: a working swerve subsystem (link both swerve tutorials), PathPlanner GUI app installed, robot must be able to report/accept `RobotConfig` (mass, MOI, module offsets — same numbers already gathered for YAGSL/CTRE setup).
- **Installing PathplannerLib vendordep**: `https://3015rangerrobotics.github.io/pathplannerlib/PathplannerLib.json` via VSCode "Install New Libraries (online)", cross-reference `setup/3rd_party_libs.md`. Verify current season's exact URL/version via `frc-docs` MCP at write time (policy requirement) rather than hardcoding beyond what's confirmed here.
- **Deploy directory structure**: `src/main/deploy/pathplanner/paths/*.path`, `autos/*.auto`, `navgrid.json`, `settings.json` — shown as plain fenced code/dir-tree blocks (not `--8<--`, since these are GUI-generated artifacts, matching how the YAGSL tutorial inlines its GUI/config-tool-generated JSON rather than snippet-embedding it).
- **GUI walkthrough**: creating a project, entering robot config (mass/MOI/module locations/motor type — this is what `RobotConfig.fromGUISettings()` reads at runtime), drawing a path (waypoints, constraints, rotation targets, the navgrid for obstacle avoidance), building an Auto (chaining paths + named-command markers + wait commands), adding event markers. Use `![Image Title](imageURL)` placeholders per template since screenshots weren't captured in this pass — flag for a follow-up contributor/mentor to fill in.
- **Knowledge check** quiz block (matches `yagsl_swerve_tutorial.md` / `ctre_swerve_generator_tutorial.md` closing pattern).
- **Links to Relevant Documentation**: pathplanner.dev, GitHub repo, forward link to the second new page.

No new Java code examples needed for this page — it's GUI/config focused.

### 2. `docs/programming/pathplanner_autonomous.md` — "Creating Autonomous Routines with PathPlanner"

Assumes the reader finished page 1 (paths/autos already exist in the deploy folder) and one of the two swerve tutorials. This is the code-heavy page, tabbed by vendor (`===` tabs, matching site convention) since the two existing swerve code bases diverge here:

- **Wiring `AutoBuilder.configure`**:
  - **YAGSL tab**: extend `docs/code_examples/swerve/SwerveSubsystem.java`'s constructor with a new `--8<-- [start:autobuilder-config]` anchor block modeled on 427-Rebuilt-2026's `SwerveSubsystem` — `RobotConfig.fromGUISettings()` in a try/catch, `SwerveSetpointGenerator`, `AutoBuilder.configure(this::getPose, this::resetPose, this::getSpeeds, (speeds, ff) -> driveFieldOriented(speeds), new PPHolonomicDriveController(translationPID, rotationPID), config, allianceBooleanSupplier, this)`.
  - **CTRE Swerve Generator tab**: note that CTRE's generator emits this wiring itself in newer templates (verify exact current behavior via `frc-docs` MCP before writing — don't assert unconfirmed API shape); show the illustrative excerpt as a new file `docs/code_examples/ctre_swerve/CommandSwerveDrivetrain.java` (excerpt-only, "generator output, don't hand-type this" — same treatment as the existing `TunerConstants.java` illustrative file), based on 321-Rebuilt-2026's `Drivetrain.java` constructor pattern (`RobotConfig.fromGUISettings()` → `AutoBuilder.configure(getPose, resetPose, getChassisSpeeds, driveRobotRelative, ppHolonomicDriveController, config, MyAlliance::isRed, this)`).
- **Registering Named Commands**: `NamedCommands.registerCommand("Name", command)` in `RobotContainer`'s constructor, using the site's existing example subsystems (`ShooterSubsystem`/`ElevatorSubsystem` from `docs/examples/`) for realistic-but-generic command names rather than inventing new domain concepts — mirrors both real repos' pattern of registering intake/shoot commands before `configureBindings()`.
- **Filling in the `SendableChooser` stub**: complete `docs/code_examples/swerve/RobotContainer.java`'s existing unused `autoChooser` field and `docs/code_examples/ctre_swerve/RobotContainer.java`'s `getAutonomousCommand()` with `AutoBuilder.buildAutoChooser()` + `SmartDashboard.putData("Auto Chooser", autoChooser)` + `return autoChooser.getSelected();` — explicitly closes the loop `autonomous.md` opened ("a natural next step").
- **Optional/advanced section**: `EventTrigger` for in-path-triggered behavior (427's pattern) and on-the-fly single-path following via `PathPlannerPath.fromPathFile(name)` + `AutoBuilder.followPath(path)` (427's `followPathCommand` helper) for cases simpler than a full GUI-built Auto.
- **Debugging/telemetry**: `PathPlannerLogging.setLogCurrentPoseCallback` / `setLogTargetPoseCallback` / `setLogActivePathCallback` wired to a `Field2d`, viewable in Elastic/Shuffleboard — cross-link `programming/Elastic.md`. Common gotchas: alliance-flip flag (`MyAlliance::isRed` / `DriverStation.getAlliance()`), event-marker string must exactly match the `NamedCommands` key, bumper-on-blocks test before field use.
- **Knowledge check** quiz + **Links to Relevant Documentation** (pathplanner.dev, back-links to `autonomous.md` and both swerve tutorials).

New/modified code example files:
- Edit `docs/code_examples/swerve/SwerveSubsystem.java` — add `autobuilder-config` anchor.
- Edit `docs/code_examples/swerve/RobotContainer.java` — fill in `autoChooser` wiring + `NamedCommands` anchor.
- Edit `docs/code_examples/ctre_swerve/RobotContainer.java` — fill in chooser wiring + `NamedCommands` anchor.
- New `docs/code_examples/ctre_swerve/CommandSwerveDrivetrain.java` — illustrative generator-output excerpt showing `configureAutoBuilder()` (or whatever the generator currently calls it — confirm via `frc-docs` MCP).

## Navigation

Add both pages to `mkdocs.yml`'s `FRC Programming` nav block, after `autonomous.md` and before `pid.md`:

```yaml
  - 'programming/autonomous.md'
  - 'programming/pathplanner_overview.md'
  - 'programming/pathplanner_autonomous.md'
  - 'programming/pid.md'
```

## Cross-linking existing pages (small edits, not new pages)

- `docs/programming/autonomous.md`: turn the existing `!!! note "Why not use the chooser?"` aside into a forward-link to `pathplanner_autonomous.md`.
- `docs/programming/yagsl_swerve_tutorial.md` and `docs/programming/ctre_swerve_generator_tutorial.md`: their existing "PathPlanner" mentions (in the ChassisSpeeds note and Phoenix6-Examples link respectively) become real links to `pathplanner_overview.md`.

## Execution notes (per AGENT.md policy)

- Query the `frc-docs` MCP server before writing any WPILib/vendor API content, especially: (a) whether CTRE's current swerve generator auto-generates `AutoBuilder` wiring in `CommandSwerveDrivetrain`, and (b) current-season `PathplannerLib` vendordep URL/version, to confirm the 2026.1.2 figures gathered here are still current at write time.
- All Java snippets go through the `--8<--` anchor pattern except GUI-generated JSON/`.path`/`.auto` artifacts, which are shown inline (matches existing YAGSL JSON-config precedent).
- Use the Documentation Writer agent (`.claude/documentation-writer-agent.md` via `general` subagent) to draft both pages in the established voice/format, feeding it this plan plus the concrete code excerpts already gathered from both reference repos.

## Verification

- `mkdocs build` (or `mkdocs serve`) from repo root with no warnings about missing snippet anchors or broken nav entries.
- Manually check both new pages render in the local `mkdocs serve` preview: tabs switch correctly, quiz blocks render, all `--8<--` includes resolve.
- Confirm every new anchor tag added to existing `.java` files is actually referenced by a page (and vice versa) so nothing is dead.

## Reference material pulled from real repos (for implementation)

Concrete code already retrieved during planning that the eventual implementer/agent should reuse rather than re-fetch:

- **427-Rebuilt-2026** `RobotContainer.java`: `NamedCommands.registerCommand(...)`, `EventTrigger`, `PathPlannerLogging` → `Field2d` callbacks, `AutoBuilder.buildAutoChooser()`.
- **427-Rebuilt-2026** `SwerveSubsystem.java`: `RobotConfig.fromGUISettings()`, `SwerveSetpointGenerator`, `AutoBuilder.configure(...)` with `PPHolonomicDriveController`, `followPathCommand(String)` via `PathPlannerPath.fromPathFile` + `AutoBuilder.followPath`.
- **321-Rebuilt-2026** `Drivetrain.java` (extends `TunerSwerveDrivetrain`): `RobotConfig.fromGUISettings()` → `AutoBuilder.configure(getPose, resetPose, getChassisSpeeds, driveRobotRelative, ppHolonomicDriveController, config, MyAlliance::isRed, this)`.
- Both repos' `vendordeps/PathplannerLib-2026.1.2.json` and `src/main/deploy/pathplanner/` layout (`autos/`, `paths/`, `navgrid.json`, `settings.json`).
