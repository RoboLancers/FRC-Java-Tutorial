# Unit 3 — Motor Configuration and Arcade Drive Factory

> [!NOTE]
> Read [Programming a RobotDrive](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#programing-a-robotdrive) and [Creating the driveArcade Command Factory](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-the-drivearcade-command-factory) on the tutorial site before starting.

## Objective

Complete the constructor by configuring leader/follower relationships and initializing `DifferentialDrive`, then add the `driveArcade` command factory method.

## File to Edit

`src/main/java/frc/robot/subsystems/CANDriveSubsystem.java`

***

## Part A — Configure Leader/Follower Relationships

The KitBot has two motors per side. One motor on each side is the **leader** — it runs directly. The other is the **follower** — it mirrors the leader. This is configured through `SparkMaxConfig`.

**1)** Add the imports for `PersistMode` and `ResetMode` at the top of the file (use 💡 quick-fix):

```java
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
```

**2)** Inside the constructor, continuing after the `SparkMaxConfig` lines from Unit 2, configure the follower motors. The `config` object you already created will be reused:

```java
config.follow(leftLeader);
leftFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

config.follow(rightLeader);
rightFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
```

**3)** Remove following mode from the config, then apply it to the right leader:

```java
config.disableFollowerMode();
rightLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
```

**4)** Invert the left side. On a differential drive, one side's motors spin opposite the other for both sides to drive forward together:

```java
config.inverted(true);
leftLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
```

> [!WARNING]
> Only invert one side — not both. If both sides are inverted (or neither is), the robot will spin in circles or fight itself. If the robot drives backwards, swap the inversion to the other side.

***

## Part B — Initialize DifferentialDrive

**5)** After the motor configuration, assign the `drive` field. Place this line near the top of the constructor, right after creating the four `SparkMax` objects:

```java
drive = new DifferentialDrive(leftLeader, rightLeader);
```

> [!NOTE]
> `DifferentialDrive` takes the two **leader** motors only — the followers mirror them automatically through the configuration applied above.

Add the import via 💡 quick-fix if not already present:
- `DifferentialDrive` → `edu.wpi.first.wpilibj.drive.DifferentialDrive`

***

## Part C — Add the driveArcade Command Factory

**6)** Below the `periodic()` method, add the `driveArcade` factory method. Add the imports it needs via 💡 quick-fix:

```java
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
```

Then add the method:

```java
public Command driveArcade(DoubleSupplier xSpeed, DoubleSupplier zRotation) {
    return this.run(
        () -> drive.arcadeDrive(xSpeed.getAsDouble(), zRotation.getAsDouble()));
}
```

> [!TIP]
> `this.run(...)` creates a command that calls the lambda on every scheduler loop while the command is scheduled. The subsystem is automatically registered as a requirement, so no other command can interrupt it without going through the scheduler.
>
> The parameters are `DoubleSupplier` (a function that returns a `double`) rather than plain `double` values. This means the joystick reading is re-evaluated every loop cycle, so the robot continuously responds to stick movement rather than being locked to a value captured at startup.

> [!TIP]
> To use tank drive instead of arcade, replace `drive.arcadeDrive(...)` with `drive.tankDrive(...)` and rename the method to `driveTank`.

***

## Expected Result

The class should now compile cleanly. The complete constructor:

```java
public CANDriveSubsystem() {
    leftLeader   = new SparkMax(LEFT_LEADER_ID,   MotorType.kBrushed);
    leftFollower  = new SparkMax(LEFT_FOLLOWER_ID,  MotorType.kBrushed);
    rightLeader  = new SparkMax(RIGHT_LEADER_ID,  MotorType.kBrushed);
    rightFollower = new SparkMax(RIGHT_FOLLOWER_ID, MotorType.kBrushed);

    drive = new DifferentialDrive(leftLeader, rightLeader);

    leftLeader.setCANTimeout(250);
    leftFollower.setCANTimeout(250);
    rightLeader.setCANTimeout(250);
    rightFollower.setCANTimeout(250);

    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(12);
    config.smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT);

    config.follow(leftLeader);
    leftFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    config.follow(rightLeader);
    rightFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    config.disableFollowerMode();
    rightLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    config.inverted(true);
    leftLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
}
```

***

## Commit and Push

```bash
git add src/main/java/frc/robot/subsystems/CANDriveSubsystem.java
git commit -m "Unit 3: configure followers, invert, and add driveArcade factory"
git push
```

## Reference

- [Programming a RobotDrive](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#programing-a-robotdrive)
- [Creating the driveArcade Command Factory](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-the-drivearcade-command-factory)
- [WPILib Command Based Robot](https://robolancers.github.io/FRC-Java-Tutorial/basics/wpilib.html#command-based-robot)
- [Java Methods](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_methods.html)
