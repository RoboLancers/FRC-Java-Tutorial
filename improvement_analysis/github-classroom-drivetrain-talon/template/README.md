# FRC GitHub Classroom Assignment: TalonFX Drivetrain

**Objective:** Build a command-based drivetrain subsystem using TalonFX motor controllers and the WPILib DifferentialDrive class.

This assignment uses **Phoenix 6** and the modern command-based framework. By completing all four units, you will have a working arcade-drive drivetrain that responds to Xbox controller input.

## Prerequisites

- Installed WPILib 2026
- Installed [Phoenix 6 vendor dependency](https://docs.ctr-electronics.com/en/stable/){target=_blank}
- Familiarity with the [command-based framework](https://docs.wpilib.org/en/stable/docs/software/commandbased/index.html){target=_blank}
- Completed the [driving a robot](../../docs/programming/driving_robot.md) tutorial (TalonFX tab)

## Assignment Units

Work through units 1–4 in order. Each unit builds on the previous one.

### Unit 1: Declare Motor Variables
**Estimated time:** 5 minutes

Declare four `TalonFX` motor fields and a `DifferentialDrive` instance in the `CANDriveSubsystem` class.

**Key learning:** Understanding motor controller objects and the DifferentialDrive interface.

### Unit 2: Initialize Motors and Configure Constants
**Estimated time:** 10 minutes

Fill in `Constants.java` with CAN IDs and current limits, then initialize the four TalonFX motors in the constructor with proper configuration.

**Key learning:** Using `TalonFXConfiguration` for current limits and the role of constants in subsystems.

### Unit 3: Follower Configuration and Drive Command Factory
**Estimated time:** 15 minutes

Set up the follower motors using Phoenix 6's `Follower` control class, configure motor inversion, and create the `driveArcade()` command factory.

**Key learning:** Motor follower setup, inversions via `InvertedValue`, and command factories in the command-based paradigm.

### Unit 4: Wire Up in RobotContainer
**Estimated time:** 5 minutes

Instantiate the drivetrain subsystem and bind the drive command to joystick input in `RobotContainer.java`.

**Key learning:** Trigger-based command scheduling and the role of RobotContainer.

## Auto-Grading Checks

This assignment uses auto-grading to verify your work. The grading script checks:

1. **Unit 1:** `CANDriveSubsystem.java` contains exactly 4 `TalonFX` fields and 1 `DifferentialDrive` field.
2. **Unit 2:** `Constants.java` defines `LEFT_LEADER_ID`, `LEFT_FOLLOWER_ID`, `RIGHT_LEADER_ID`, `RIGHT_FOLLOWER_ID`, and `DRIVE_MOTOR_CURRENT_LIMIT`.
3. **Unit 2:** Constructor creates 4 `TalonFX` objects and applies `TalonFXConfiguration`.
4. **Unit 3:** Constructor uses `Follower` control class for right motors and applies inversion via `InvertedValue`.
5. **Unit 4:** `driveArcade()` method returns a `Command` that calls `drive.arcadeDrive()`.
6. **Unit 4:** `RobotContainer.java` sets the default command for the drivetrain subsystem.

## Verification with Phoenix Tuner X
(This section is only relevant if this code is running on a real robot with TalonFX motors connected via CAN.)
Use [Phoenix Tuner X](https://docs.ctr-electronics.com/en/stable/docs/tuner/index.html){target=_blank} to verify your CAN IDs:

1. Connect your roboRIO to power and a computer with Phoenix Tuner X installed.
2. Open Phoenix Tuner X and navigate to **Devices**.
3. Confirm all four TalonFX motors appear with the correct CAN IDs.

If a motor doesn't show up, check:
- CAN wiring (should be twisted pair, daisy-chained)
- CAN ID set on the motor (matches `Constants.java`)
- USB-to-CAN adapter is connected and recognized

## File Structure

```
github-classroom-drivetrain-talon/
├── README.md                     # This file
├── .github/
│   └── workflows/
│       └── classroom.yml         # Auto-grading workflow
├── unit1-declare-fields.md       # Unit 1 assignment
├── unit2-constructor-constants.md # Unit 2 assignment
├── unit3-arcade-drive-factory.md # Unit 3 assignment
├── unit4-robotcontainer-bindings.md # Unit 4 assignment
└── template/                     # Starter template (Gradle project)
    ├── build.gradle
    ├── gradlew
    ├── settings.gradle
    ├── vendordeps/
    │   ├── Phoenix6.json
    │   └── WPILib-2026.1.json
    ├── src/main/java/frc/robot/
    │   ├── Robot.java
    │   ├── Main.java
    │   ├── RobotContainer.java
    │   ├── Constants.java
    │   └── subsystems/
    │       └── CANDriveSubsystem.java
    └── .wpilib/
        └── wpilib_preferences.json
```

## Tips

- **Start with Unit 1** to understand the motor objects you're working with.
- **Constants first** in Unit 2 — use Phoenix Tuner X to verify your CAN IDs before coding.
- **Test after each unit** — run `gradlew build` to check for compile errors.
- **Review the [driving_robot.md tutorial](../../docs/programming/driving_robot.md)** (TalonFX tab) if you get stuck — the completed example is very similar to what you're building.

## Troubleshooting

**Build fails with "cannot find symbol TalonFX"**
- Ensure Phoenix 6 is installed. Run **WPILib Command Palette → Manage Vendor Libraries → Install new library (online)** and paste the Phoenix 6 JSON URL.

**Motor doesn't move in teleop**
- Check that `RobotContainer` is binding the controller axes correctly.
- Verify `DifferentialDrive.arcadeDrive()` is receiving non-zero joystick values.
- Confirm follower motors are set up via the `Follower` control class.

**CAN communication errors in DriverStation**
- Verify CAN wiring and that motor CAN IDs match `Constants.java`.
- Use Phoenix Tuner X to check motor heartbeats.

## Learning Outcomes

Upon completion, you will be able to:

- Declare and initialize TalonFX motor controllers
- Use `TalonFXConfiguration` to apply current limits and other settings
- Set up motor followers via the Phoenix 6 `Follower` control class
- Invert motors using `InvertedValue` and `getConfigurator().apply()`
- Create command factories that return `Command` objects
- Bind subsystem commands to controller input in `RobotContainer`
