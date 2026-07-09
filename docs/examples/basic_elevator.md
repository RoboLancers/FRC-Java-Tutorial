# Basic Elevator Subsystem

A basic elevator subsystem with two motors (leader and follower), an encoder for position feedback, and limit switches for safety using motor controllers (REV SparkMax or CTRE TalonFX).

## Overview

This guide walks you through building an elevator subsystem from scratch. By the end you will have a working subsystem that can move up and down, respect limit switches, and track its position — all controlled from driver buttons.

The subsystem has two motors and two sensors:

- **Leader motor** — a brushless NEO that drives the elevator up and down
- **Follower motor** — a brushless NEO that mirrors the leader's movement
- **Encoder** — built into the leader motor to track elevator position
- **Limit switches** — DIO-connected switches at the top and bottom to prevent over-travel

**See the table of contents for a breakdown of each section.**

***

## What You'll Need

=== "SparkMax"
    - Two [REV SparkMax](https://docs.revrobotics.com/brushless/revlib/sparkmax-overview){target=_blank} motor controllers with NEO brushless motors
    - [REVLib](https://docs.revrobotics.com/brushless/revlib/overview){target=_blank} vendor dependency installed in your project
    - Two limit switches (Normally Open recommended)
    - 2 DIO ports for the limit switches

    !!! tip
        If you haven't installed REVLib yet, go to the Wpilib vendor dependency manager button on the left side. Find Revlib in the Available dependencies list and click install. If you don't see it, click the "Refresh" button at the top to fetch the latest list of libraries.
=== "TalonFX"
    - Two [CTRE TalonFX](https://store.ctr-electronics.com/talonfx/){target=_blank} motor controllers (brushless only)
    - [Phoenix 6](https://store.ctr-electronics.com/phoenix-6/){target=_blank} vendor dependency installed in your project
    - Two limit switches (Normally Open recommended)
    - 2 DIO ports for the limit switches

    !!! tip
        If you haven't installed Phoenix 6 yet, go to the Wpilib vendor dependency manager button on the left side. Find Phoenix 6 in the Available dependencies list and click install. If you don't see it, click the "Refresh" button at the top to fetch the latest list of libraries.

***

## The Full Example Files

The complete subsystem code is split across three files in the same folder:

- [`docs/code_examples/ElevatorConstants.java`](../code_examples/ElevatorConstants.java) — all tunable values (same for both SparkMax and TalonFX)
- [`docs/code_examples/BasicElevatorSubsystem.java`](../code_examples/BasicElevatorSubsystem.java) — SparkMax subsystem implementation
- [`docs/code_examples/BasicElevatorSubsystemTalonFX.java`](../code_examples/BasicElevatorSubsystemTalonFX.java) — TalonFX subsystem implementation

Every snippet in this guide is pulled directly from those files.

***

## Constants

Constants are stored in their own file, `ElevatorConstants.java`, placed in the same folder as the subsystem.

```java title="ElevatorConstants.java"
--8<-- "docs/code_examples/ElevatorConstants.java:elevator_constants"
```

Key constants explained:

| Constant | Purpose |
| --- | --- |
| `LEADER_MOTOR_ID` / `FOLLOWER_MOTOR_ID` | CAN IDs of the two SparkMax or TalonFX motors |
| `UP_VOLTAGE` / `DOWN_VOLTAGE` | Voltages applied to move the elevator |
| `TOP_LIMIT_SWITCH_PORT` / `BOTTOM_LIMIT_SWITCH_PORT` | DIO port numbers for the limit switches |
| `ENCODER_COUNTS_PER_REVOLUTION` | Encoder CPR (42 for NEO, depends on external encoder) |
| `GEAR_RATIO` | Mechanical advantage (10:1 in this example) |
| `DISTANCE_PER_PULSE` | Converts encoder counts to inches of elevator travel |

!!! note
    `UP_VOLTAGE` and `DOWN_VOLTAGE` are open-loop values that apply a fixed percentage of battery voltage to the motors. They are not PID setpoints — they simply control how fast the elevator moves when you call `moveUp()` or `moveDown()`. You will likely need to adjust these based on your robot's weight and gearing. 
    
    "Positive Voltage moves the elevator up, and negative voltage moves it down." This is a common convention but may be reversed on your robot depending on wiring and motor orientation. Always test with low voltages first to confirm movement direction.

!!! tip
    These voltage and distance values are starting points. You will almost certainly need to tune them on your real robot. Since they all live in `ElevatorConstants.java`, you only need to open one file when tuning.

To use the constants in `BasicElevatorSubsystem.java` without prefixing every value with `ElevatorConstants.`, add a static import at the top of the subsystem file:

```java title="BasicElevatorSubsystem.java"
import static frc.robot.subsystems.ElevatorConstants.*;
```

***

## Declaring the Motors and Sensors

At the class level, declare `private final` fields for each motor, encoder, and limit switch.

=== "SparkMax"

    ```java title="BasicElevatorSubsystem.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystem.java:elevator_fields"
    ```

=== "TalonFX"

    ```java title="BasicElevatorSubsystemTalonFX.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystemTalonFX.java:elevator_fields_talon"
    ```

The `RelativeEncoder` (SparkMax) is obtained from the motor; TalonFX gets motor position directly via the Phoenix 6 API.

***

## The Constructor

The constructor instantiates each motor, applies configuration, sets up the follower, and initializes the limit switches.

=== "SparkMax"

    ```java title="BasicElevatorSubsystem.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystem.java:elevator_constructor"
    ```

=== "TalonFX"

    ```java title="BasicElevatorSubsystemTalonFX.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystemTalonFX.java:elevator_constructor_talon"
    ```

!!! note "Follower Motors"
    The follower motor uses `follow()` (SparkMax) or the `Follower` control class (TalonFX) to automatically mirror the leader's output. This ensures both motors provide equal force without separate control logic.

!!! note "Limit Switch Initialization"
    Both implementations create `DigitalInput` objects for the limit switches. The DIO ports are specified in `ElevatorConstants.java` so you can adjust them easily.

***

## Reading Sensors

### Limit Switches

The subsystem provides methods to query limit switch state:

=== "SparkMax"

    ```java title="BasicElevatorSubsystem.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystem.java:elevator_limits"
    ```

=== "TalonFX"

    ```java title="BasicElevatorSubsystemTalonFX.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystemTalonFX.java:elevator_limits_talon"
    ```

Both methods return `true` when the elevator is at that limit. They invert the raw switch reading because limit switches are wired as Normally Open (NO) — they read `false` when pressed.

### Position Tracking

=== "SparkMax"

    ```java title="BasicElevatorSubsystem.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystem.java:elevator_position"
    ```

=== "TalonFX"

    ```java title="BasicElevatorSubsystemTalonFX.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystemTalonFX.java:elevator_position_talon"
    ```

`getPosition()` returns the elevator height in inches, calculated from encoder data. `resetPosition()` zeroes the encoder at the current location — useful for calibration.

!!! note "TalonFX Encoder Conversion"
    TalonFX reports rotor position in rotations. To convert to inches, multiply by `DISTANCE_PER_PULSE`, which accounts for the gear ratio and mechanism circumference.

***

## Elevator Movement Methods

These methods handle safe movement by checking limit switches before applying voltage:

=== "SparkMax"

    ```java title="BasicElevatorSubsystem.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystem.java:elevator_movement"
    ```

=== "TalonFX"

    ```java title="BasicElevatorSubsystemTalonFX.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystemTalonFX.java:elevator_movement_talon"
    ```

- **`moveUp()`** — applies positive voltage unless the top limit switch is pressed
- **`moveDown()`** — applies negative voltage unless the bottom limit switch is pressed
- **`stop()`** — applies zero voltage to hold the elevator in place

!!! tip "Safety First"
    The limit-check logic prevents mechanical over-travel. Always include these guards in real robot code to protect mechanism and hardware.

***

## Command Factories

WPILib's [command-based framework](https://docs.wpilib.org/en/stable/docs/software/commandbased/index.html){target=_blank} lets you convert subsystem methods into [`Command`](https://docs.wpilib.org/en/stable/docs/software/commandbased/commands.html){target=_blank} objects that the scheduler can run, interrupt, and chain together. These commands are usually bound to controller buttons in `RobotContainer.java`.

=== "SparkMax"

    ```java title="BasicElevatorSubsystem.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystem.java:elevator_commands"
    ```

=== "TalonFX"

    ```java title="BasicElevatorSubsystemTalonFX.java"
    --8<-- "docs/code_examples/BasicElevatorSubsystemTalonFX.java:elevator_commands_talon"
    ```

- **`moveUpCommand()`** — returns a `run()` command that calls `moveUp()` every loop
- **`moveDownCommand()`** — returns a `run()` command that calls `moveDown()` every loop
- **`stopCommand()`** — returns a `runOnce()` command that calls `stop()` once and immediately finishes

!!! note "run() vs runOnce()"
    - `run()` calls the lambda **every robot loop** (20 ms) while the command is scheduled
    - `runOnce()` calls the lambda **once** and then immediately finishes

    Use `run()` for continuous movement and `runOnce()` for one-time actions.

***

## Hooking It Up in RobotContainer

Instantiate the subsystem and bind buttons in `RobotContainer.java`. See the WPILib docs on [binding commands to triggers](https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html){target=_blank} for more options.

### Instantiate the Subsystem

```java title="RobotContainer.java"
private final BasicElevatorSubsystem m_elevator = new BasicElevatorSubsystem();
```

### Set a Default Command

Set `stopCommand()` as the default so motors turn off whenever no button is held:

```java title="RobotContainer.java"
m_elevator.setDefaultCommand(m_elevator.stopCommand());
```

### Bind Buttons

A typical setup binds up/down movement to Xbox controller bumpers:

```java title="RobotContainer.java"
// Hold right trigger to move elevator up
driverController.rightTrigger()
    .whileTrue(m_elevator.moveUpCommand());

// Hold left trigger to move elevator down
driverController.leftTrigger()
    .whileTrue(m_elevator.moveDownCommand());
```

!!! tip "whileTrue()"
    `whileTrue()` schedules the command while the trigger condition is true and cancels it (triggering the default command) when false. This means the elevator automatically stops when the driver lets go.

***

## Next Steps

- **Add PID control** — move to specific setpoints (e.g., "raise to 24 inches") using closed-loop feedback from the encoder. see [PID_elevator](https://docs.wpilib.org/en/stable/docs/software/commandbased/pid-commands.html){target=_blank} for more information. 
- **Add telemetry** — log position and limit switch state to SmartDashboard for tuning
- **Integrate into game logic** — use the elevator in autonomous sequences or more complex command chains
- **Tune constants** — adjust voltages, distance calculations, and gear ratios based on real robot behavior

See the [WPILib documentation](https://docs.wpilib.org/en/stable/){target=_blank} for advanced control topics like PID and state machine designs.
