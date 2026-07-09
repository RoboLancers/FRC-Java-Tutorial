# WIP Completion Plan: PID Driven Shooter

## Location
`docs/examples/pid_shooter.md`

## Current State
- **Status**: Empty template - 33 lines total
- **Content**: Has basic structure with placeholder sections (Section One, Section Two, subsection)
- **Issue**: Completely empty - "This section will help you learn to BLANK"

## Required Work

### 1. Shooter PID Tutorial Content
- [ ] Add shooter wheel speed control with PID
- [ ] Document shooter wheel mechanism (flywheel, hood)
- [ ] Explain RPM setpoint and measurement
- [ ] Cover velocity sensor feedback (encoder)

### 2. Vendor Implementation Options
- [ ] CTRE TalonFX velocity control
- [ ] REV SPARK MAX with closed-loop velocity
- [ ] WPILib PIDController with encoder feedback

### 3. Create/Update Code Examples
- [ ] Create `ShooterSubsystem.java` with PID control
- [ ] Add `SetShooterSpeed.java` command
- [ ] Include Constants for PID/RPM values
- [ ] Add proper `--8<--` anchors

### 4. Tuning Guide
- [ ] Document PID tuning for shooters (velocity)
- [ ] Include FF (Feed Forward) calculation
- [ ] Add tips for optimal shooting angles
- [ ] Cover velocity vs. position control differences

### 5. Integrate with Existing Docs
- [ ] Link to `docs/examples/basic_shooter.md`
- [ ] Cross-reference PID introduction page
- [ ] Connect to pneumatics (hood angle if applicable)

### 6. Remove [WIP] Prefix
- [ ] Rename from `[WIP] PID Driven Shooter` to `PID Driven Shooter`

## Available Resources
- Existing shooter examples: `docs/code_examples/BasicShooter*.java`
- WPILib Shooter example: https://docs.wpilib.org/en/stable/docs/software/examples-tutorials/
- REV documentation: https://docs.revrobotics.com/revlib/
- CTRE documentation: https://v6.docs.ctr-electronics.com/
- FRC-Docs MCP: query `shooter`, `velocity`, `flywheel`
- Template: `template_page.md`

## Implementation Steps
1. Determine which vendor (CTRE or REV) to showcase primarily
2. Draft shooter PID tutorial structure
3. Create complete Java code examples
4. Write detailed tutorial following pattern
5. Test with MkDocs build
6. Remove [WIP] prefix

## Priority
**MEDIUM** - Needs to be filled out as one of two PID examples