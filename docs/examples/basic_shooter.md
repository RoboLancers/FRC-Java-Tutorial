# Basic Shooter Subsystem

A basic, open-loop shooter subsystem with a flywheel and feeder using REV SparkMax motor controllers.

## Overview

This guide walks you through building a shooter subsystem from scratch. By the end you will have a working subsystem that can spin up a flywheel, feed a game piece, and stop — all controlled from driver buttons.

The subsystem has two motors:

- **Flywheel motor** — a brushless NEO that spins a wheel to launch game pieces
- **Feeder motor** — a brushless NEO that pushes game pieces from a hopper/indexer into the flywheel

**See the table of contents for a breakdown of each section.**

***

## What You'll Need

- Two [REV SparkMax](https://docs.revrobotics.com/brushless/revlib/sparkmax-overview){target=_blank} motor controllers with NEO brushless motors
- [REVLib](https://docs.revrobotics.com/brushless/revlib/overview){target=_blank} vendor dependency installed in your project

!!! tip
    If you haven't installed REVLib yet, go to **WPILib Command Palette → Manage Vendor Libraries → Install new library (online)** and paste in the REVLib JSON URL from the [REV documentation](https://docs.revrobotics.com){target=_blank}.

***

## The Full Example Files

The complete subsystem code is split across two files in the same folder:

- [`docs/code_examples/ShooterConstants.java`](../code_examples/ShooterConstants.java) — all tunable values
- [`docs/code_examples/BasicShooterSubsystem.java`](../code_examples/BasicShooterSubsystem.java) — the subsystem logic

Every snippet in this guide is pulled directly from those files.

***

## Constants

Constants are stored in their own file, `ShooterConstants.java`, placed in the same folder as the subsystem. This keeps all the numbers that need tuning in one easy-to-find place, and prevents `BasicShooterSubsystem.java` from becoming cluttered with magic numbers.

!!! note
    In a real robot project your subsystem files live in `src/main/java/frc/robot/subsystems/`. Put `ShooterConstants.java` in that same folder so it stays with the code it belongs to.

```java title="ShooterConstants.java"
--8<-- "docs/code_examples/ShooterConstants.java:shooter_constants"
```

| Constant | Purpose |
| --- | --- |
| `FLYWHEEL_MOTOR_ID` | CAN ID of the flywheel SparkMax |
| `FEEDER_MOTOR_ID` | CAN ID of the feeder SparkMax |
| `FLYWHEEL_CURRENT_LIMIT` | Protects the flywheel motor and prevents breaker trips |
| `FEEDER_CURRENT_LIMIT` | The feeder needs less torque, so its limit is lower |
| `FLYWHEEL_SPINUP_VOLTAGE` | Voltage used during spin-up (slightly less than shoot) |
| `FLYWHEEL_SHOOT_VOLTAGE` | Full shooting voltage for the flywheel |
| `FEEDER_SHOOT_VOLTAGE` | Voltage used to push the game piece into the flywheel |

!!! tip
    These voltage values are starting points. You will almost certainly need to tune them on your real robot. Since they all live in `ShooterConstants.java`, you only need to open one file when tuning.

To use the constants in `BasicShooterSubsystem.java` without prefixing every value with `ShooterConstants.`, add a static import at the top of the subsystem file:

```java title="BasicShooterSubsystem.java"
--8<-- "docs/code_examples/BasicShooterSubsystem.java:constants_import"
```

This lets you write `FLYWHEEL_SHOOT_VOLTAGE` directly in the subsystem instead of `ShooterConstants.FLYWHEEL_SHOOT_VOLTAGE`.

***

## Declaring the Motors

At the class level, declare `private final` fields for each SparkMax. Marking them `final` prevents them from accidentally being reassigned later.

```java title="BasicShooterSubsystem.java"
--8<-- "docs/code_examples/BasicShooterSubsystem.java:motors"
```

***

## The Constructor

The constructor instantiates each SparkMax and applies a configuration to it. Configuration lets you set things like current limits and motor inversion before the robot runs.

```java title="BasicShooterSubsystem.java"
--8<-- "docs/code_examples/BasicShooterSubsystem.java:constructor"
```

!!! note
    The feeder motor is inverted so that positive voltage always means "feed toward the flywheel." Without this, you would need to use negative voltages for the feeder, which is confusing to read.

!!! warning
    `ResetMode.kResetSafeParameters` clears any previously saved configuration on the SparkMax before applying the new one. This ensures the motor controller is in a known state each time the robot starts.

***

## Shooter Methods

### spinUp()

Spins the flywheel to a lower spin-up voltage while keeping the feeder stopped. Use this to get the flywheel up to speed *before* feeding a game piece.

```java title="BasicShooterSubsystem.java"
--8<-- "docs/code_examples/BasicShooterSubsystem.java:spinup"
```

!!! tip
    Spinning up before shooting prevents the flywheel from slowing down the moment a game piece enters it. This gives you a more consistent launch every time.

***

### shoot()

Runs the flywheel at full shoot voltage/speed and engages the feeder to push the game piece(s) into the flywheel.

```java title="BasicShooterSubsystem.java"
--8<-- "docs/code_examples/BasicShooterSubsystem.java:shoot"
```

***

### stop()

Stops both motors. Call this when the shoot button is released or when a command ends.

```java title="BasicShooterSubsystem.java"
--8<-- "docs/code_examples/BasicShooterSubsystem.java:stop"
```

***

## Command Factories

WPILib's [command-based framework](https://docs.wpilib.org/en/stable/docs/software/commandbased/index.html){target=_blank} lets you convert methods into [`Command`](https://docs.wpilib.org/en/stable/docs/software/commandbased/commands.html){target=_blank} objects that the scheduler can run, interrupt, and chain together. Each factory method below wraps one of the shooter methods in a command. This allows you to easily bind them to buttons and use them in more complex command sequences later on.
Commands can also be written as separate classes, but for simple one-line methods like these, factory methods are a quick and easy way to get commands without extra boilerplate. Separate commmand classes are better when complex logic, multiple steps, or additional requirements are involved.

```java title="BasicShooterSubsystem.java"
--8<-- "docs/code_examples/BasicShooterSubsystem.java:commands"
```

- `this.run(...)` creates a command that calls the lambda **every robot loop** (20 ms) for as long as the command is scheduled.
- `this.runOnce(...)` creates a command that calls the lambda **once** and then immediately finishes. This is appropriate for `stop()` since you only need to set the motors to 0 one time.

!!! note
    Both `run` and `runOnce` automatically require `this` subsystem, so the scheduler will cancel any conflicting command that also requires this subsystem when one of these is scheduled.

***

## Hooking It Up in RobotContainer

Instantiate the subsystem and bind buttons in `RobotContainer.java`. See the WPILib docs on [binding commands to triggers](https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html){target=_blank} for more options.

### Instantiate the Subsystem

```java title="RobotContainer.java"
private final BasicShooterSubsystem m_shooter = new BasicShooterSubsystem();
```

### Set a Default Command

Set `stopCommand()` as the default so motors turn off whenever no button is held.

```java title="RobotContainer.java"
m_shooter.setDefaultCommand(m_shooter.stopCommand());
```

### Bind Buttons

A typical workflow binds spin-up to one button and the full shoot sequence to another, so the driver can pre-spin the flywheel before feeding balls into the shooter. This ensures the flywheel is up to speed and prevents it from slowing down when a game piece enters.

```java title="RobotContainer.java"
// Hold the right bumper to spin the flywheel up to speed
new JoystickButton(m_controller, Button.kRightBumper.value)
    .whileTrue(m_shooter.spinUpCommand());

// Hold the left bumper to spin up AND feed (full shoot)
new JoystickButton(m_controller, Button.kLeftBumper.value)
    .whileTrue(m_shooter.shootCommand());
```

!!! tip
    `whileTrue` schedules the command when the button is pressed and cancels it (triggering the default command) when the button is released. This means the motors will automatically stop when the driver lets go.

***

## Next Steps

- **Add a sensor** — use a beam break or limit switch to detect when a game piece is loaded, and automate the spin-up/feed sequence.
- **Add velocity control** — use a PID controller and the built-in SparkMax encoder to shoot at a precise RPM instead of open-loop voltage. See the [PID Shooter](pid_shooter.md) guide for details.
