# Unit 3: Follower Configuration and Drive Command Factory

**Objective:** Set up the follower motors using Phoenix 6's `Follower` control class, configure motor inversion, and create the `driveArcade()` command factory.

**Estimated time:** 15 minutes

## What You'll Do

1. Set up left and right follower motors to follow their respective leaders
2. Invert the left side motors so positive voltage drives the robot forward
3. Create a `driveArcade()` command factory that takes joystick input and drives the robot

## Part 1: Set Up Follower Motors

At the end of the constructor (after applying the current limit configuration), tell the right follower to follow the right leader, and the left follower to follow the left leader. Use Phoenix 6's `Follower` control class and each motor's `setControl()` method. Pass the leader's CAN ID constant and `false` for the invert parameter — the follower should mirror the leader's direction exactly.

> **Follower Persistence:** In Phoenix 6, the follower configuration persists even after the motor is powered off. Once set, the follower will automatically track the leader without requiring code to be rerun.

You will need to import `Follower` from `com.ctre.phoenix6.controls`.

## Part 2: Invert the Left Side

After the follower setup, create a second `TalonFXConfiguration` for the left motors and set its `MotorOutput.Inverted` field to `InvertedValue.Clockwise_Positive`. Apply this configuration to both `leftLeader` and `leftFollower` using their `getConfigurator().apply()` methods.

> **Why Invert?** On a typical FRC robot, the left and right drivetrains spin in opposite directions. Without inversion, positive voltage applied to both sides would turn the robot in a circle instead of driving it straight. Inverting the left side ensures both sides push the robot in the same direction when the same voltage is applied.

You will need to import `InvertedValue` from `com.ctre.phoenix6.signals`.

See the **Configuring Motor Controllers** section of the [Driving Robot tutorial](../../docs/programming/driving_robot.md) for the TalonFX inversion and follower patterns, and the [full TalonFX subsystem example](../../docs/code_examples/2026KitBotInlineTalonFX/subsystems/CANDriveSubsystem.java) to check your complete constructor.

## Part 3: Create the driveArcade() Command Factory

Below the `periodic()` method, add a public method named `driveArcade`. It should:

- Accept two `DoubleSupplier` parameters: one for forward/backward speed and one for rotation
- Return a `Command` created with `this.run(...)`
- Inside the lambda passed to `run`, call `drive.arcadeDrive(...)` using the double values obtained from each supplier via `getAsDouble()`

You will need to import `DoubleSupplier` from `java.util.function` and `Command` from `edu.wpi.first.wpilibj2.command`.

See the **Creating the driveArcade Command Factory** section of the [Driving Robot tutorial](../../docs/programming/driving_robot.md) for an explanation of the command factory pattern.

## How This Works

- **`run()`** — creates a command that calls the lambda **every robot loop** (20 ms) for as long as the command is scheduled
- **`arcadeDrive(speed, rotation)`** — WPILib's arcade drive implementation; moves the robot forward/backward and rotates based on input
- **`DoubleSupplier`** — a functional interface that provides a `double` value; in `RobotContainer`, you'll pass joystick axis methods like `() -> -controller.getLeftY()`

## Verification

Your constructor should now initialize all four motors, apply current limits to all four, set up the two followers, invert the left side, and create the `DifferentialDrive`. The `driveArcade()` method should be present and return a `Command`.

Build the project (**Ctrl+Shift+P → WPILib: Build Robot Code**) and verify there are no errors before continuing.

## Common Issues

**"Cannot find symbol Follower"**
- Ensure Phoenix 6 is installed and the import is correct: `com.ctre.phoenix6.controls.Follower`.

**Robot drives backward when you push forward**
- The left or right side inversion is reversed. Check your `InvertedValue` and experiment with `Clockwise_Positive` vs `CounterClockwise_Positive`.

**Followers don't move**
- Verify that the CAN ID passed to the `Follower` constructor matches the leader's actual CAN ID constant.

## Next Step

Proceed to **Unit 4: Wire Up in RobotContainer** to bind the drive command to joystick input.
