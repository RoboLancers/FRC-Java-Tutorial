# Session Context — FRC Java Tutorial

## Project Overview

MkDocs Material tutorial site teaching FRC Java programming to high school students. Hosted on GitHub Pages from the `main` branch. Repo: `https://github.com/RoboLancers/FRC-Java-Tutorial`.

Branch worked on: `robolancer_updates`

---

## Decisions Made This Session

### 1. Hybrid Java Teaching Approach (agreed)

**Recommendation accepted:** "Minimal Prerequisite + Embedded Teaching"
- Short Java Fundamentals section (4 pages) students complete before FRC content
- Ruthlessly minimal — only what's needed to read the first FRC page
- `!!! note "Java concept: ..."` admonitions embedded inline in FRC pages when a Java pattern appears for the first time

### 2. GitHub Classroom Integration (agreed)

- Keep MkDocs site for conceptual content + mkdocs-quiz knowledge checks
- Use GitHub Classroom for code practice assignments
- Assignment READMEs use GitHub GFM with `> [!NOTE]` / `> [!TIP]` / `> [!WARNING]` alert syntax (NOT MkDocs `!!!` admonitions)
- Each assignment links back to live site: `https://robolancers.github.io/FRC-Java-Tutorial/`

### 3. Supabase + mkdocs-quiz Progress Tracking (planned, not implemented)

See `improvement_analysis/quiz-progress-tracking.md` for full architecture.

---

## Changes Made This Session

### New Files Created

**Java Fundamentals pages (new nav section):**
- `docs/basics/java_types_variables.md` — primitive types, variables, `final`, scope, operators, comments, naming conventions
- `docs/basics/java_methods.md` — declaring methods, return types, parameters, calling, `@Override`
- `docs/basics/java_classes.md` — class structure, fields, access modifiers, constructors, `new`, inheritance, `this`
- `docs/basics/java_control_flow.md` — `if`/`else`, `while` (with FRC scheduler warning), `for`/for-each, elevator example

**GitHub Classroom drivetrain assignment:**
- `improvement_analysis/github-classroom-drivetrain/README.md`
- `improvement_analysis/github-classroom-drivetrain/unit1-declare-fields.md`
- `improvement_analysis/github-classroom-drivetrain/unit2-constructor-constants.md`
- `improvement_analysis/github-classroom-drivetrain/unit3-arcade-drive-factory.md`
- `improvement_analysis/github-classroom-drivetrain/unit4-robotcontainer-bindings.md`
- `improvement_analysis/github-classroom-drivetrain/template/` — full WPILib project template with TODO stubs in:
  - `src/main/java/frc/robot/Main.java` (complete boilerplate)
  - `src/main/java/frc/robot/Robot.java` (complete boilerplate)
  - `src/main/java/frc/robot/Constants.java` (OperatorConstants complete; DriveConstants has TODOs)
  - `src/main/java/frc/robot/RobotContainer.java` (fields declared; configureBindings has TODO)
  - `src/main/java/frc/robot/subsystems/CANDriveSubsystem.java` (skeleton with labeled TODO comments)
  - `build.gradle`, `settings.gradle`, `.gitignore`, `.wpilib/wpilib_preferences.json`, `vendordeps/REVLib.json`

**Analysis docs:**
- `improvement_analysis/github-classroom-integration.md` — analysis of GitHub Classroom integration approaches

### Modified Files

**`mkdocs.yml` nav:**
- "Programming Basics" split into "Java Fundamentals" (4 new pages) + "FRC Background" (hardware pages)
- `basics/java_basics.md` removed from nav (file kept — direct links to `#constants` still resolve; the driving_robot.md link was updated to point to `java_types_variables.md#constants`)

**Embedded teaching notes added:**
- `docs/basics/wpilib.md` — `!!! note` about inheritance (`extends SubsystemBase`) when subsystems are introduced
- `docs/programming/new_project.md` — `!!! note` about package/import/extends before the default subsystem code example
- `docs/programming/driving_robot.md` — two `!!! note` notes: `private final` fields and constructor; constants link updated

---

## Known Issues / Bugs Fixed

### mkdocs build AttributeError (Python 3.14 + pymdownx 10.20.1)

**Symptom:** `'NoneType' object has no attribute 'replace'` when building pages with untitled code blocks.

**Root cause:** `pymdownx/highlight.py` line 410 passes `filename=title` where `title=None` for untitled code blocks. `html.escape(None)` fails in Python 3.14 (calls `None.replace(...)` which raises `AttributeError`).

**Fix applied:** Patched `.venv/Lib/site-packages/pymdownx/highlight.py` line 410:
```python
# Before:
filename=title if not inline else "",
# After:
filename=(title or "") if not inline else "",
```

**Caveat:** This patch is in the venv and will be lost if `pip install -r requirements.txt` recreates the venv. Permanent fix: upgrade pymdownx when a compatible release ships. Consider adding this to requirements.txt or creating a post-install script.

---

## Site URL

`https://robolancers.github.io/FRC-Java-Tutorial/`

Pages use `use_directory_urls: false`, so links use `.html` extension:
- `https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html`
- `https://robolancers.github.io/FRC-Java-Tutorial/basics/java_classes.html`

---

## Code Example Reference

The drivetrain code examples live in `docs/code_examples/2026KitBotInline/`:
- `subsystems/CANDriveSubsystem.java` — uses brushed motors (`MotorType.kBrushed`) for KitBot CIM motors
- `RobotContainer.java` — includes both drive and fuel subsystems (the classroom template simplifies to drive-only)
- `Constants.java` — DriveConstants CAN IDs are 1-4, current limit 60A

The KitBot example uses `kBrushed` (CIM motors). The tutorial text mentions `kBrushless` (NEO) as an alternative. Students should use whichever matches their robot.

---

## Next Steps (suggested)

1. Add `mkdocs-quiz` questions to each Java Fundamentals page
2. Create GitHub Classroom organization and import the drivetrain template
3. Implement the Supabase quiz-tracking backend (see `improvement_analysis/quiz-progress-tracking.md`)
4. Create additional GitHub Classroom assignments for: subsystem basics, sensors, autonomous
5. Fix the missing `programming/AdvantageKit.md` file referenced in nav (currently causes a build warning)
6. Pin a pymdownx version in `requirements.txt` that doesn't have the Python 3.14 `filename=None` bug, or add a post-install patch script
