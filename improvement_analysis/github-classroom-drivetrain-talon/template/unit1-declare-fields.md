# Unit 1: Declare Motor Variables

**Objective:** Declare four `TalonFX` motor fields in the `CANDriveSubsystem` class. The `DifferentialDrive` field will be added in Unit 2, alongside its initialization.

**Estimated time:** 5 minutes

## What You'll Do

Open `src/main/java/frc/robot/subsystems/CANDriveSubsystem.java` and add the motor field declarations at the class level (below the class declaration, above the constructor).

## Instructions

1. Add an import for `TalonFX` (from `com.ctre.phoenix6.hardware`) at the top of the file. VSCode can add this automatically — hover over the red-underlined class name and choose **Quick Fix → Import**.

2. Declare four `private final TalonFX` fields for the motors:

   - **leftLeader** — left side leader motor
   - **leftFollower** — left side follower motor
   - **rightLeader** — right side leader motor
   - **rightFollower** — right side follower motor

   Each field is declared with its type and name only — do not assign a value here. The value is set in the constructor (Unit 2).

> **Note:** The `DifferentialDrive drive` field will be declared and initialized together in Unit 2, since it needs the motor objects to exist first.

See the **Declaring Motor Variables** section of the [Driving Robot tutorial](../../docs/programming/driving_robot.md) for the TalonFX field declaration pattern, and the [full TalonFX subsystem example](../../docs/code_examples/2026KitBotInlineTalonFX/subsystems/CANDriveSubsystem.java) to check your work.

## Why This Matters

- **`private final`** — prevents accidental reassignment and limits scope to this subsystem
- **TalonFX fields** — represent the physical motors; you'll initialize them in Unit 2

## Key Concepts

**Motor Controller Interface:** TalonFX implements WPILib's `MotorController` interface, which defines methods like `set()`, `setVoltage()`, and `get()`. This allows `DifferentialDrive` to work with TalonFX the same way it works with SparkMax.

## Verification

After adding the declarations, your code should compile without errors. In VSCode, confirm there are no red underlines on the new field declarations. If you see an error about an unresolved symbol, use the Quick Fix import option or verify that Phoenix 6 is installed via the WPILib Command Palette → **Manage Vendor Libraries**.

> **Tip:** The constructor currently has a `throw` placeholder that lets the class compile even though the `final` fields aren't assigned yet. You will replace the entire constructor body in Unit 2.

## Next Step

Proceed to **Unit 2: Initialize Motors and Configure Constants** to fill in `Constants.java` and initialize these fields in the constructor.
