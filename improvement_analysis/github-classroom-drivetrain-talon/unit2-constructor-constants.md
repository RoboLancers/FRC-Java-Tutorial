# Unit 2: Initialize Motors and Configure Constants

**Objective:** Fill in `Constants.java` with CAN IDs and current limits, then initialize the four TalonFX motors in the constructor.

**Estimated time:** 10 minutes

## What You'll Do

1. Add motor CAN IDs and a current limit constant to `Constants.java`
2. Initialize all four TalonFX motors in the `CANDriveSubsystem` constructor
3. Apply a `TalonFXConfiguration` with current limits to prevent motor damage and breaker trips

## Part 1: Update Constants.java

Open `src/main/java/frc/robot/Constants.java` and locate (or create) a `public static final class DriveConstants` inner class inside `Constants`. It should contain:

- Four `int` constants for motor CAN IDs: `LEFT_LEADER_ID`, `LEFT_FOLLOWER_ID`, `RIGHT_LEADER_ID`, `RIGHT_FOLLOWER_ID`. The IDs correspond to the CAN bus addresses configured on your robot — 1 through 4 are the most common defaults for a kitbot.
- One `int` constant for current limiting: `DRIVE_MOTOR_CURRENT_LIMIT`. A value of 60 (amps) is a reasonable starting point to reduce the likelihood of tripping breakers.

All constants should be declared `public static final int`. See the **Using Constants** section of the [Driving Robot tutorial](../../docs/programming/driving_robot.md) and the [TalonFX Constants.java example](../../docs/code_examples/2026KitBotInlineTalonFX/Constants.java) to check your work.

> **Verifying CAN IDs:** (This is only relevant if the code is being loaded on an actual robot) 
Use [Phoenix Tuner X](https://docs.ctr-electronics.com/en/stable/docs/tuner/index.html) to confirm your motor CAN IDs before running code. Connect the roboRIO to power and a computer with Phoenix Tuner X installed, then navigate to **Devices** to see all TalonFX motors on the CAN bus.

## Part 2: Initialize Motors in the Constructor

In `CANDriveSubsystem.java`, the constructor should do the following in order:

1. **Create the four TalonFX motor objects**, one for each field declared in Unit 1. Each takes the corresponding CAN ID constant from `DriveConstants` as its only constructor argument. Unlike SparkMax, TalonFX does not require a motor type parameter.

2. **Build a `TalonFXConfiguration` object** and enable supply current limiting on it, using `DRIVE_MOTOR_CURRENT_LIMIT` from `DriveConstants`. The configuration is set through the `CurrentLimits` sub-object — you need to set both the limit value and the enable flag.

3. **Apply that configuration to all four motors** using each motor's `getConfigurator().apply(config)` method.

4. **Create the `DifferentialDrive`** by passing the left leader and right leader motors to its constructor. Only the leaders are passed — followers are wired up in Unit 3.

> **Why only leader motors in DifferentialDrive?** `DifferentialDrive` only needs the leader motors. The followers are controlled separately via the `Follower` control class (set up in Unit 3). This keeps `DifferentialDrive` simple and lets you manage follower logic explicitly.

## Imports Needed

You will need imports for the following classes at the top of `CANDriveSubsystem.java` (use VSCode Quick Fix to add them, or type them manually):

- `TalonFXConfiguration` — from `com.ctre.phoenix6.configs`
- `TalonFX` — from `com.ctre.phoenix6.hardware`
- `DifferentialDrive` — from `edu.wpi.first.wpilibj.drive`
- `SubsystemBase` — from `edu.wpi.first.wpilibj2.command`

You should also add a static import for `DriveConstants` so you can reference the constants without the class prefix. This import goes at the top of the file alongside the other imports, not inside the constructor.

See the [full TalonFX subsystem example](../../docs/code_examples/2026KitBotInlineTalonFX/subsystems/CANDriveSubsystem.java) to check your work.

## Why This Matters

- **Constants** — centralizes all tunable values in one place, making it easy to adjust CAN IDs or current limits without hunting through code
- **TalonFXConfiguration** — safely configures the motor before the robot runs; current limits protect hardware
- **Current Limits** — prevent breaker trips and motor overheating by capping the maximum current draw

## Testing

Build the project in VSCode (**Ctrl+Shift+P → WPILib: Build Robot Code**) and verify there are no compilation errors. If you get an error about `TalonFXConfiguration`, ensure Phoenix 6 is installed via **WPILib Command Palette → Manage Vendor Libraries → Install new library (online)**.

## Common Issues

**"Cannot find symbol TalonFXConfiguration"**
- Phoenix 6 vendor library is not installed. Use **WPILib Command Palette → Manage Vendor Libraries → Install new library (online)** and paste the Phoenix 6 JSON URL.

**CAN IDs don't match your robot**
- Update the constants in `Constants.java` to match your actual motor CAN IDs (verify in Phoenix Tuner X).

## Next Step

Proceed to **Unit 3: Follower Configuration and Drive Command Factory** to set up motor followers and create the arcade-drive command.
