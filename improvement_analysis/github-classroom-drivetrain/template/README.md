# Assignment: Drivetrain Subsystem

> [!NOTE]
> Before starting, read the full [Drivetrain Tutorial](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html) on the FRC Java Tutorial site. Complete all four units in order.

## Overview

In this assignment you will build a complete differential drive subsystem from scratch using the WPILib command-based framework and REV SparkMax motor controllers. By the end you will have a robot that drives in teleop via an Xbox controller.

## What You Are Building

```
CANDriveSubsystem
  ├── 4 SparkMax motor controllers (left leader, left follower, right leader, right follower)
  ├── DifferentialDrive (wraps the two leaders for arcade/tank drive)
  └── driveArcade() — command factory that reads joystick input every loop cycle

RobotContainer
  └── setDefaultCommand → driveArcade (so the driver can always move the robot)

Constants.DriveConstants
  └── CAN IDs and current limit for the four drive motors
```

## Files to Edit

| File | Units |
|---|---|
| `src/main/java/frc/robot/Constants.java` | Unit 2 |
| `src/main/java/frc/robot/subsystems/CANDriveSubsystem.java` | Units 1, 2, 3 |
| `src/main/java/frc/robot/RobotContainer.java` | Unit 4 |

Do **not** modify `Main.java` or `Robot.java` — these are provided and complete.

## Units

- [Unit 1 — Declare Motor Fields](unit1-declare-fields.md)
- [Unit 2 — Constructor and Constants](unit2-constructor-constants.md)
- [Unit 3 — Motor Configuration and Arcade Drive Factory](unit3-arcade-drive-factory.md)
- [Unit 4 — Wire Up in RobotContainer](unit4-robotcontainer-bindings.md)

## Setup

1. Accept the assignment via the GitHub Classroom link provided by your instructor
2. Clone your repo and open the `template/` folder in VSCode with WPILib installed
3. Complete each unit in order, committing and pushing after each one
4. Auto-grading runs on every push — check the **Actions** tab for results

> [!IMPORTANT]
> Set your team number in `.wpilib/wpilib_preferences.json` before deploying to the robot. Replace `0` with your actual team number.

> [!WARNING]
> In `Constants.java`, set the CAN IDs to match **your specific robot's wiring**. Using the wrong IDs can damage the robot when it tries to move.

## Auto-Grading Checks

- Code compiles with Gradle (`./gradlew build`)
- All four `SparkMax` fields exist in `CANDriveSubsystem`
- `DifferentialDrive drive` field exists in `CANDriveSubsystem`
- `driveArcade` method exists and returns `Command`
- Default command is set for the drive subsystem in `RobotContainer`

## Reference

| Topic | Tutorial Page |
|---|---|
| `private final` fields | [Variables and Data Types](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_types_variables.html#constants) |
| Constructors and `new` | [Java Classes](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_classes.html#constructors) |
| Subsystems and commands | [WPILib Programming Basics](https://robolancers.github.io/FRC-Java-Tutorial/basics/wpilib.html) |
| Full drivetrain walkthrough | [Creating a Basic Driving Robot](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html) |
