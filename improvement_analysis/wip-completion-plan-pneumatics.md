# WIP Completion Plan: Using Pneumatics

## Location
`docs/programming/pneumatics.md`

## Current State
- **Status**: Substantial content but outdated
- **Content**: 315 lines covering pneumatics, solenoids, DoubleSolenoid, commands
- **Issue**: Uses legacy Command API (`extends Command`, `extends InstantCommand` from `edu.wpi.first.wpilibj.command.*`)

## Required Work

### 1. Migrate to Command v2
- [ ] Replace `extends InstantCommand` with `InstantCommand` from `edu.wpi.first.wpilibj2.command`
- [ ] Update all command imports
- [ ] Use new `addRequirements()` pattern via constructor
- [ ] Replace `initialize()` override with proper constructor-based command definition

### 2. Update Solenoid Examples
- [ ] Update `DoubleSolenoid` examples to use vendor APIs
- [ ] Consider REV pneumatics (if available) vs. PCM
- [ ] Add modern `Solenoid` usage patterns
- [ ] Include proper CAN PCM configuration

### 3. Code Examples Update
- [ ] Refactor `ShooterUp.java` to use Command v2 style
- [ ] Refactor `ShooterDown.java` to use Command v2 style
- [ ] Update OI (Operator Interface) patterns if shown
- [ ] Add `--8<--` anchors for embedded code

### 4. Modern Enhancements
- [ ] Add mention of solenoid types for modern control
- [ ] Include pressure sensor references (if needed)
- [ ] Document Compressor configuration alternatives

### 5. Add Images/Assets
- [ ] Verify all image references are valid
- [ ] Consider adding wiring diagrams
- [ ] Check for broken links

### 6. Remove [WIP] Prefix
- [ ] Rename from `[WIP] Using Pneumatics` to `Using Pneumatics`

## Available Resources
- WPILib Pneumatics: https://docs.wpilib.org/en/stable/docs/software/hardware-apis/pneumatics/
- CTRE PCM documentation: https://v6.docs.ctr-electronics.com/
- REV documentation (if available): https://docs.revrobotics.com/
- FRC-Docs MCP: query `pneumatics`, `solenoid`, `compressor`
- Template: `template_page.md`

## Implementation Steps
1. Read full pneumatics.md and identify all code blocks
2. Query FRC docs for modern pneumatic control
3. Update all command classes to Command v2
4. Add embedded Java code example files
5. Test with MkDocs build
6. Remove [WIP] prefix

## Priority
**HIGH** - Substantial content but uses deprecated Command API.