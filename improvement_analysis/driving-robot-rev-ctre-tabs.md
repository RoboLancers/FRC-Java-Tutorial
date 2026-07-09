# Plan: Add REV / CTRE Tabs to driving_robot.md

## Context

`driving_robot.md` currently only teaches the REV SparkMax path. Students with CTRE TalonFX hardware (Falcon 500, Kraken) have no guidance in this tutorial. The goal is to let a student pick their motor controller once and see matching code throughout the page.

---

## Recommended Approach: Synced Content Tabs

`content.tabs.link` is already enabled in `mkdocs.yml`. This Material theme feature makes every tab group with the same label name switch together when the user clicks any one of them — a perfect "pick once, see everywhere" selector.

However, the repo currently uses `pymdownx.blocks.tab` (blocks-style `/// tab | Label` syntax), and `content.tabs.link` **only works with `pymdownx.tabbed`** (classic `=== "Label"` syntax). The fix is a one-line addition to `mkdocs.yml`.

### What gets built

**1. New Java file: `docs/code_examples/2026KitBotInline/subsystems/TalonFXDriveSubsystem.java`**

A complete Phoenix 6 drivetrain subsystem, parallel in structure to `CANDriveSubsystem.java`. Named anchors mirror the REV anchor names (with `-talon` suffix) so each tab in the docs can pull from its own anchor.

Anchors planned:
| Anchor | Content |
|---|---|
| `motors-talon` | `TalonFX` field declarations |
| `controls-talon` | Reusable `DutyCycleOut` objects |
| `constructor-talon` | Full constructor block |
| `config-talon` | `TalonFXConfiguration` (neutral mode, current limit) |
| `inversion-talon` | Per-side inversion via `InvertedValue` |
| `follower-talon` | `new Follower(leaderId, false)` setup |
| `drive-arcade-talon` | `driveArcade()` command factory |

Key CTRE design decisions:
- **No `DifferentialDrive`** — Phoenix 6 `TalonFX` does not implement WPILib's `MotorController` interface. Instead, arcade math is done manually (`leftLeader.setControl(leftOutput.withOutput(speed + rotation))`).
- **Followers set once in constructor** — Phoenix 6 `Follower` is a persistent control mode; once set, followers mirror their leader automatically.
- **Same `DriveConstants`** — CAN IDs and `DRIVE_MOTOR_CURRENT_LIMIT` are motor-controller-agnostic; no new constants file needed.
- **`StatorCurrentLimit`** replaces the REV smart current limit.
- **No direct voltage compensation equivalent** — noted in docs; Phoenix 6 advanced users can use `TorqueCurrentFOC` but it's out of scope for beginners.

**2. Update `mkdocs.yml`**

Add under `markdown_extensions`:
```yaml
- pymdownx.tabbed:
    alternate_style: true
```

`pymdownx.blocks.tab` stays (it's harmless alongside this), but the new `pymdownx.tabbed` enables `content.tabs.link`.

**3. Restructure `docs/programming/driving_robot.md`**

Add a selector note at the top:
> Select your motor controller using the tabs in each section. Once you click a tab, all other tab groups on this page will switch automatically.

Tab syntax throughout (labels must be byte-for-byte identical for linking to work):
```markdown
=== "REV (SparkMax)"
    ... REV content / snippet ...

=== "CTRE (TalonFX)"
    ... CTRE content / snippet ...
```

Tab groups added to each of these existing sections:
1. **Creating Motor Variables** — field declarations
2. **Constructor initialization** — `new SparkMax(...)` vs `new TalonFX(...)`
3. **Configuration** — `SparkMaxConfig` / voltage compensation / CAN timeout vs `TalonFXConfiguration` / current limit / neutral mode
4. **Follower setup** — `config.follow(leader)` vs `new Follower(leaderId, false)`
5. **Motor inversion** — `config.inverted(true)` vs `InvertedValue.Clockwise_Positive`
6. **Drive variable** — `DifferentialDrive` vs `DutyCycleOut` pair
7. **`driveArcade` command factory** — `drive.arcadeDrive(...)` vs manual `speed ± rotation`
8. **RobotContainer wiring** — identical pattern for both; tabs show same code with correct class names

Sections that stay un-tabbed (shared between both):
- Constants (CAN IDs, current limit constant name)
- Xbox controller setup
- `setDefaultCommand` pattern explanation

Section headings updated to be controller-neutral (e.g., "Creating Motor Variables" instead of "Creating the SparkMax Variables"), with hardware-specific details inside the tabs.

---

## Alternative: Separate Pages

Split into `driving_robot_rev.md` and `driving_robot_ctre.md` with a landing page at `driving_robot.md`.

**Pros:** Each page is fully self-contained and shorter. Easier to add a third controller later.  
**Cons:** Student loses the ability to compare implementations. Nav adds two entries. Content that's identical (RobotContainer, Constants) is duplicated. The tab approach serves this tutorial's "pick and follow" workflow better.

**Not recommended** unless the page grows too large to read comfortably after tabs are added.

---

## Critical Files

| File | Action |
|---|---|
| `docs/code_examples/2026KitBotInline/subsystems/TalonFXDriveSubsystem.java` | **Create** |
| `docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java` | Reference only (no changes) |
| `docs/code_examples/2026KitBotInline/Constants.java` | Reference only (same constants used by both) |
| `docs/code_examples/2026KitBotInline/RobotContainer.java` | Reference only |
| `mkdocs.yml` | **Modify** — add `pymdownx.tabbed` |
| `docs/programming/driving_robot.md` | **Modify** — major restructure with tabs |

---

## Verification

1. `mkdocs serve` — confirm no build errors, page renders with tabs
2. Click "CTRE (TalonFX)" tab in any section — confirm all other tab groups switch simultaneously
3. Reload page — tab selection may reset (expected; `content.tabs.link` is session-persistent, not cookie-stored)
4. Confirm all `--8<--` snippets resolve (no "snippet not found" errors in build output)
5. Check that the existing REV content is unchanged and still renders correctly inside the REV tab
