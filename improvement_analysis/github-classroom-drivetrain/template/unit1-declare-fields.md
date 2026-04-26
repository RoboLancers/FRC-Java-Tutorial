# Unit 1 — Declare Motor Fields

> [!NOTE]
> Read [Creating the SparkMax Variables](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-the-sparkmax-variables) on the tutorial site before starting.

## Objective

Declare the four `SparkMax` motor controller fields that `CANDriveSubsystem` needs. You are not initializing them yet — that happens in Unit 2. The `DifferentialDrive` field will be declared in Unit 3, once the motors are ready to pass to it.

## File to Edit

`src/main/java/frc/robot/subsystems/CANDriveSubsystem.java`

***

## Steps

**1)** Open `CANDriveSubsystem.java`. You will see the class body with an empty constructor and a `periodic()` method. All your changes in this unit go between the class opening brace and the constructor.

**2)** Declare four `private final SparkMax` fields inside the class body, before the constructor. Name them `leftLeader`, `leftFollower`, `rightLeader`, and `rightFollower`. Each declaration ends with a semicolon and has no assigned value — initialization happens in the constructor.

**3)** VSCode will underline `SparkMax` in red because the import is missing. Fix it using the 💡 quick-fix (click the light bulb or press `Ctrl+.`) and select:

- `SparkMax` → `com.revrobotics.spark.SparkMax`

> [!NOTE]
> You will also need `MotorType` later (Unit 2). You can add it now via quick-fix, or wait until you need it.

> [!NOTE]
> The `DifferentialDrive` field (`drive`) will be added in Unit 3, once the motors exist to pass to it.

> [!TIP]
> `private final` means only this class can access these objects, and they will be assigned exactly once (in the constructor) and never reassigned. This prevents other classes from swapping out your motor controllers accidentally. See [Variables and Data Types — Constants](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_types_variables.html#constants).

***

## Expected Result

After this unit, the class body should contain four field declarations — all `SparkMax`, all `private final`, all unassigned — placed above the constructor. The constructor placeholder (a `throw` statement) remains unchanged for now; you will replace it in Unit 2.

> [!TIP]
> The `throw` in the constructor is intentional — it lets the class compile even though the `final` fields aren't assigned yet. You will replace the entire constructor body in Unit 2.

***

## Commit and Push

Stage `CANDriveSubsystem.java`, commit with a message like `"Unit 1: declare SparkMax and DifferentialDrive fields"`, and push to trigger the auto-grader. Check the **Actions** tab on GitHub to see the results.

## Reference

- [Creating the SparkMax Variables](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-the-sparkmax-variables)
- [Java Classes — Fields](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_classes.html#fields)
- [VSCode Tips](https://robolancers.github.io/FRC-Java-Tutorial/basics/vscode_tips.html)
