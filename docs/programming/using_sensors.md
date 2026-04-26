# Using Sensors and Switches

Sensors provide real-time feedback about your robot’s state. This guide covers using digital input switches and encoders in the command-based framework.

## What Are Sensors

- **Limit switches** detect contact and are connected to the roboRIO’s Digital Input (DIO) ports
- **Encoders** measure motor rotation or linear distance and are also connected to DIO ports (WPILib Encoder), or built into motor controllers (SparkMax RelativeEncoder, TalonFX rotor position)
- Other sensor types include gyroscopes, analog inputs, and vendor-specific devices (CANcoder, Limelight, etc.)

***

## Programming Limit Switches

Limit switches are simple binary sensors: pressed or not pressed. This section shows how to safely integrate them into a subsystem.

### Creating the Subsystem with a Limit Switch
    ```java title="ShooterSubsystem.java"
    --8<
    -- "docs/code_examples/basics/sensors/LimitSwitchExamples.java:limit-switch-field"
    ```

Declare the `DigitalInput` as a `private final` field. This represents the physical limit switch wired to a DIO port.


### Initializing the Limit Switch


=== "Limit Switch Initialization"

    ```java title="ShooterSubsystem.java"
    --8<-- "docs/code_examples/basics/sensors/LimitSwitchExamples.java:limit-switch-constructor-talon"
    ```

The `DigitalInput` constructor takes one parameter: the DIO port number on the roboRIO (typically 0–9).

### Reading the Limit Switch

=== "Limit Switch Reading"

    ```java title="ShooterSubsystem.java"
    --8<-- "docs/code_examples/basics/sensors/LimitSwitchExamples.java:limit-switch-method"
    ```

!!! note "Normally Open vs Normally Closed"
    Switches come in two configurations:

    - **Normally Open (NO)** — the circuit is open (disconnected) when the switch is not pressed. `DigitalInput.get()` returns `true` when unpressed.
    - **Normally Closed (NC)** — the circuit is closed (connected) when the switch is not pressed. `DigitalInput.get()` returns `false` when unpressed.

    **Recommendation:** Use a Normally Open switch and invert the reading in code: `return !shooterSwitch.get();`. This way, if the switch wiring becomes disconnected, the robot reads the sensor as "not pressed" (`false`) instead of "pressed" (`true`). A disconnected sensor should never accidentally trigger an action.

!!! tip "Keep inversions in the subsystem"
    Always handle sensor inversions inside the subsystem method, not in commands or other classes. This way every caller gets the correct logical value without needing to know about the hardware wiring.

### Using the Limit Switch in RobotContainer

To automatically stop the shooter when the limit switch is pressed, bind it as a command in `RobotContainer`:

```java title="RobotContainer.java"
// When the limit switch is pressed, run the auto-position command
new Trigger(() -> shooter.isLimitSwitchPressed())
    .onTrue(shooter.autoPositionCommand());
```

This `Trigger` automatically runs the command when the condition becomes true. See the [WPILib Trigger documentation](https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html){target=_blank} for more binding options.

***

## Programming Encoders

Encoders measure rotation (motor encoders) or linear distance (external encoders). There are three common ways to use encoders in FRC:

1. **External WPILib Encoder** — a standalone quadrature encoder on DIO ports
2. **Built-in motor encoder** — SparkMax has `RelativeEncoder`, TalonFX has rotor position feedback

### External WPILib Encoder

An external quadrature encoder connects to two DIO ports and measures shaft rotation independent of the motor controller.

```java title="DrivetrainSubsystem.java"
--8<-- "docs/code_examples/basics/sensors/EncoderExamples.java:external-encoder-field"
```

Initialize it in the constructor:

```java title="DrivetrainSubsystem.java"
--8<-- "docs/code_examples/basics/sensors/EncoderExamples.java:external-encoder-constructor"
```

The `Encoder` constructor takes two DIO port numbers. The `setDistancePerPulse()` method converts raw counts to a meaningful unit (usually feet or meters).

### Reading Encoder Distance

=== "SparkMax (External + Built-in)"

    ```java title="DrivetrainSubsystem.java"
    --8<-- "docs/code_examples/basics/sensors/EncoderExamples.java:get-distance"
    ```

    `getDistance()` on the external encoder returns distance in the units you configured. `getPosition()` on the SparkMax built-in encoder returns rotations (or your configured units).

=== "TalonFX (External + Built-in)"

    ```java title="DrivetrainSubsystem.java"
    --8<-- "docs/code_examples/basics/sensors/EncoderExamplesTalonFX.java:get-distance-talon"
    ```

    TalonFX uses Phoenix 6’s signal system. `motor.getPosition().getValueAsDouble()` returns rotor position in rotations. `motor.getVelocity().getValueAsDouble()` returns rotations per second.

### Resetting Encoders

=== "SparkMax"

    ```java title="DrivetrainSubsystem.java"
    --8<-- "docs/code_examples/basics/sensors/EncoderExamples.java:reset-encoder"
    ```

=== "TalonFX"

    ```java title="DrivetrainSubsystem.java"
    --8<-- "docs/code_examples/basics/sensors/EncoderExamplesTalonFX.java:reset-encoder-talon"
    ```

!!! note "Why `setPosition()` for TalonFX?"
    TalonFX encoder resets use `motor.setPosition(0)` instead of a dedicated `reset()` method. This sets the rotor position to zero in the motor’s feedback system, which is how Phoenix 6 manages encoder state.

### Using Encoder Commands

Both controller types expose the same subsystem interface, so binding encoder commands in `RobotContainer` is identical:

```java title="RobotContainer.java"
// Reset both encoders at the start of autonomous
m_drivetrain.setDefaultCommand(
    m_drivetrain.driveArcade(
        () -> -controller.getLeftY(),
        () -> -controller.getRightX()
    )
);

// Bind a button to reset encoders
new JoystickButton(controller, Button.kStart.value)
    .onTrue(m_drivetrain.resetEncodersCommand());
```

The `resetEncodersCommand()` command factory is defined in the subsystem and calls the reset logic. Since both SparkMax and TalonFX implementations expose the same interface, the binding code remains unchanged — only the subsystem implementation details differ.
