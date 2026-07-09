# WIP Completion Plan: Creating an Autonomous Command

## Location
`docs/programming/autonomous.md`

## Current State
- **Status**: Mostly complete but outdated
- **Content**: 306 lines covering autonomous concepts, DriveDistance command, DoDelay command, Autonomous command group
- **Issue**: Uses legacy WPILib API (Commands v1 - `extends Command`, `extends CommandGroup`, `edu.wpi.first.wpilibj.command.*`)
- **Code Style**: Uses old `RobotPreferences` static class pattern, legacy timer APIs

## Required Work

### 1. Migrate to Command v2 (WPILib 2024+)
- [ ] Replace `extends Command` with `extends CommandBase` or use `@FunctionalCommand`
- [ ] Replace `CommandGroup` with `SequentialCommandGroup`
- [ ] Migrate from `edu.wpi.first.wpilibj.command` to `edu.wpi.first.wpilibj2.command`
- [ ] Convert static imports for MotorControllers to vendor API patterns (CTRE TalonFX or REV SPARK MAX)

### 2. Update Code Examples
- [ ] Refactor `DriveDistance.java` to use `CommandBuilder` or `@FunctionalCommand`
- [ ] Refactor `DoDelay.java` to use `WaitCommand` or new timer patterns
- [ ] Refactor `Autonomous.java` to use `SequentialCommandGroup` with lambda commands
- [ ] Update code embed anchors in Java files

### 3. Update RobotPreferences Section
- [ ] Replace legacy `Preferences.getInstance()` with `Preferences.getDouble()` pattern
- [ ] Consider migrating to `SendableChooser` for autonomous selection
- [ ] Add guidance for using NetworkTables preferences directly

### 4. Add Modern Content
- [ ] Update references showing SendableChooser pattern (from 14-15)
- [ ] Add PathPlanner integration (optional but modern)
- [ ] Add Choreo integration reference

### 5. Remove [WIP] Prefix
- [ ] Once fully updated, rename page title from `[WIP] Creating an Autonomous Command` to `Creating an Autonomous Command`

## Available Resources
- WPILib Command v2 migration: https://docs.wpilib.org/en/stable/docs/software/commandbased-commandlib.html
- Vendor library references: `.claude/wpilib-vendor-libraries-reference.md`
- Example code in `docs/code_examples/2026KitBotInline/` - uses Command v2
- FRC-Docs MCP server: query `autonomous command`, `command group`
- Modern examples: PathPlanner AutoBuilder, Choreo trajectories

## Implementation Steps
1. Read full autonomous.md page to identify all code blocks
2. Query FRC docs for Command v2 patterns
3. Create or update Java example files in `docs/code_examples/`
4. Update all command classes to use Command v2
5. Add `--8<--` anchors for embedded code
6. Test with MkDocs build
7. Remove [WIP] prefix from title

## Priority
**HIGH** - Page has substantial content but uses deprecated APIs that will break with future WPILib versions.