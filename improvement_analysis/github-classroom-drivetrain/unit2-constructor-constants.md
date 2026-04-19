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

**1)** Open `Constants.java`. A `DriveConstants` inner class has been started for you with placeholder comments. Fill in the four CAN ID constants and the current limit:

```java
public static final class DriveConstants {
    public static final int LEFT_LEADER_ID   = 1;  // replace with your robot's actual CAN IDs
    public static final int LEFT_FOLLOWER_ID = 2;
    public static final int RIGHT_LEADER_ID  = 3;
    public static final int RIGHT_FOLLOWER_ID = 4;

    public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60;
}
```

> [!WARNING]
> The CAN IDs above are examples. You **must** set them to match your specific robot's wiring. Check the wiring diagram or use the REV Hardware Client / Phoenix Tuner to find the actual IDs. Using wrong IDs can cause unexpected motor movement.

> [!TIP]
> Constants are written in `ALL_CAPS_UNDERSCORES` and declared `public static final` so they are readable from anywhere in the project without creating an object. See [Variables and Data Types — Constants](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_types_variables.html#constants).

***

## Part B — Initialize Motors in the Constructor

**2)** Open `CANDriveSubsystem.java`. Add a static import at the top of the file so you can use constant names directly:

```java
import static frc.robot.Constants.DriveConstants.*;
```

**3)** Inside the constructor `CANDriveSubsystem()`, initialize the four motors using the constants you just defined. The KitBot uses brushed CIM motors:

```java
leftLeader   = new SparkMax(LEFT_LEADER_ID,   MotorType.kBrushed);
leftFollower  = new SparkMax(LEFT_FOLLOWER_ID,  MotorType.kBrushed);
rightLeader  = new SparkMax(RIGHT_LEADER_ID,  MotorType.kBrushed);
rightFollower = new SparkMax(RIGHT_FOLLOWER_ID, MotorType.kBrushed);
```

> [!NOTE]
> Use `MotorType.kBrushless` instead if your robot uses NEO motors. Add the import for `MotorType` via the 💡 quick-fix: `com.revrobotics.spark.SparkLowLevel.MotorType`.

**4)** After the motor declarations, set a CAN timeout on each motor so configuration calls do not block robot operation indefinitely:

```java
leftLeader.setCANTimeout(250);
leftFollower.setCANTimeout(250);
rightLeader.setCANTimeout(250);
rightFollower.setCANTimeout(250);
```

**5)** Create a `SparkMaxConfig` and apply voltage compensation and a current limit. This helps the robot behave consistently across different battery charge levels and protects the breakers:

```java
SparkMaxConfig config = new SparkMaxConfig();
config.voltageCompensation(12);
config.smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT);
```

> [!NOTE]
> Add the `SparkMaxConfig` import via 💡 quick-fix: `com.revrobotics.spark.config.SparkMaxConfig`.

***

## Expected Result

Your constructor should now look like this (follower config and DifferentialDrive initialization come in Unit 3):

```java
public CANDriveSubsystem() {
    leftLeader   = new SparkMax(LEFT_LEADER_ID,   MotorType.kBrushed);
    leftFollower  = new SparkMax(LEFT_FOLLOWER_ID,  MotorType.kBrushed);
    rightLeader  = new SparkMax(RIGHT_LEADER_ID,  MotorType.kBrushed);
    rightFollower = new SparkMax(RIGHT_FOLLOWER_ID, MotorType.kBrushed);

    leftLeader.setCANTimeout(250);
    leftFollower.setCANTimeout(250);
    rightLeader.setCANTimeout(250);
    rightFollower.setCANTimeout(250);

    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(12);
    config.smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT);

    // (follower config and drive initialization added in Unit 3)
}
```

> [!IMPORTANT]
> The code still will not compile at this point — `drive` has not been assigned yet. That happens in Unit 3.

***

## Commit and Push

```bash
git add src/main/java/frc/robot/Constants.java
git add src/main/java/frc/robot/subsystems/CANDriveSubsystem.java
git commit -m "Unit 2: define DriveConstants and initialize motors in constructor"
git push
```

## Reference

- [Creating and Filling the Constructor](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-and-filling-the-constructor)
- [Using Constants](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#using-constants)
- [Configuring the SparkMaxes](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#configuring-the-sparkmaxes)
- [Java Classes — Constructors](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_classes.html#constructors)
