# Unit 1 — Declare Motor Fields

> [!NOTE]
> Read [Creating the SparkMax Variables](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-the-sparkmax-variables) on the tutorial site before starting.

## Objective

Declare the five member variables the `CANDriveSubsystem` class needs: four `SparkMax` motor controllers and one `DifferentialDrive` object. You are not initializing them yet — that happens in Unit 2.

## File to Edit

`src/main/java/frc/robot/subsystems/CANDriveSubsystem.java`

***

## Steps

**1)** Open `CANDriveSubsystem.java`. You will see the class body with an empty constructor and a `periodic()` method. All your changes in this unit go between the class opening brace and the constructor.

**2)** Add the four `SparkMax` field declarations inside the class body, before the constructor:

```java
private final SparkMax leftLeader;
private final SparkMax leftFollower;
private final SparkMax rightLeader;
private final SparkMax rightFollower;
```

**3)** Below the SparkMax fields, add the `DifferentialDrive` field:

```java
private final DifferentialDrive drive;
```

**4)** VSCode will underline `SparkMax` and `DifferentialDrive` in red because the imports are missing. Fix each one using the 💡 quick-fix (click the light bulb or press `Ctrl+.`) and select the correct import:

- `SparkMax` → `com.revrobotics.spark.SparkMax`
- `DifferentialDrive` → `edu.wpi.first.wpilibj.drive.DifferentialDrive`

> [!TIP]
> `private final` means only this class can access these objects, and they will be assigned exactly once (in the constructor) and never reassigned. This prevents other classes from swapping out your motor controllers accidentally. See [Variables and Data Types — Constants](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_types_variables.html#constants).

***

## Expected Result

After this unit, your class should look like this (imports and constructor omitted for brevity):

```java
public class CANDriveSubsystem extends SubsystemBase {

    private final SparkMax leftLeader;
    private final SparkMax leftFollower;
    private final SparkMax rightLeader;
    private final SparkMax rightFollower;

    private final DifferentialDrive drive;

    public CANDriveSubsystem() {
        // empty for now — you will fill this in Units 2 and 3
    }

    @Override
    public void periodic() {}
}
```

> [!IMPORTANT]
> The code will not compile yet — `final` fields that are declared but not assigned cause a compiler error. This is expected. They will be assigned in the constructor in Unit 2.

***

## Commit and Push

```bash
git add src/main/java/frc/robot/subsystems/CANDriveSubsystem.java
git commit -m "Unit 1: declare SparkMax and DifferentialDrive fields"
git push
```

Check the **Actions** tab on GitHub to see the auto-grader results.

## Reference

- [Creating the SparkMax Variables](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-the-sparkmax-variables)
- [Java Classes — Fields](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_classes.html#fields)
- [VSCode Tips](https://robolancers.github.io/FRC-Java-Tutorial/basics/vscode_tips.html)
