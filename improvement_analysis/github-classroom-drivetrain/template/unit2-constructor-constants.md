# Unit 2 — Constructor and Constants

> [!NOTE]
> Read [Creating and Filling the Constructor](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-and-filling-the-constructor) and [Using Constants](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#using-constants) on the tutorial site before starting.

## Objective

Define CAN IDs in `Constants.java`, then initialize the four SparkMax motor controllers inside the `CANDriveSubsystem` constructor and apply basic safety configuration.

## Files to Edit

- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/subsystems/CANDriveSubsystem.java`

***

## Part A — Define Drive Constants

**1)** Open `Constants.java`. A `DriveConstants` inner class has been started for you with placeholder comments. Fill in four `public static final int` CAN ID constants — `LEFT_LEADER_ID`, `LEFT_FOLLOWER_ID`, `RIGHT_LEADER_ID`, `RIGHT_FOLLOWER_ID` — and one `public static final int` current limit constant named `DRIVE_MOTOR_CURRENT_LIMIT`.

The CAN IDs must match your specific robot's wiring. IDs 1–4 are common kitbot defaults, but verify them using the REV Hardware Client. A current limit of 60 (amps) is a safe starting point to protect breakers.

> [!WARNING]
> The CAN IDs must match your specific robot's wiring. Check the wiring diagram or use the REV Hardware Client to find the actual IDs. Using wrong IDs can cause unexpected motor movement.

> [!TIP]
> Constants are written in `ALL_CAPS_UNDERSCORES` and declared `public static final` so they are readable from anywhere in the project without creating an object. See [Variables and Data Types — Constants](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_types_variables.html#constants).

***

## Part B — Initialize Motors in the Constructor

**2)** Open `CANDriveSubsystem.java`. Add a static import for `DriveConstants` at the top of the file (alongside the other imports, not inside any method) so you can use constant names directly without the class prefix.

**3)** Inside the constructor, **delete the placeholder `throw` statement** and replace it with real initialization. Initialize each of the four `SparkMax` fields using the corresponding CAN ID constant. The KitBot uses brushless NEO motors so pass `MotorType.kBrushless` as the second argument. Add the import for `MotorType` via 💡 quick-fix if needed.

> [!NOTE]
> Use `MotorType.kBrushed` instead if your robot uses brushed motors.

**4)** After the motor initializations, call `setCANTimeout(250)` on each of the four motors. This sets a 250 ms timeout so configuration calls do not block robot operation indefinitely if a motor is unreachable.

**5)** Create a local `SparkMaxConfig` variable, call `voltageCompensation(12)` on it to normalize output across different battery charge levels, and call `smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT)` to protect the breakers. Add the `SparkMaxConfig` import via 💡 quick-fix if needed. Do not apply the config to any motor yet — that happens in Unit 3.

***

## Expected Result

After this unit, the constructor should create all four `SparkMax` objects, set a CAN timeout on each, and build a `SparkMaxConfig` with voltage compensation and a current limit. The `drive` field will be declared and assigned in Unit 3.

The project should compile at this point — build with **Ctrl+Shift+P → WPILib: Build Robot Code** to verify.

***

## Commit and Push

Stage both `Constants.java` and `CANDriveSubsystem.java`, commit with a message like `"Unit 2: define DriveConstants and initialize motors in constructor"`, and push to trigger the auto-grader.

## Reference

- [Creating and Filling the Constructor](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-and-filling-the-constructor)
- [Using Constants](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#using-constants)
- [Configuring the SparkMaxes](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#configuring-the-sparkmaxes)
- [Java Classes — Constructors](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_classes.html#constructors)
