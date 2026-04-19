# Unit 4 — Wire Up in RobotContainer

> [!NOTE]
> Read [Wiring Up in RobotContainer](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#wiring-up-in-robotcontainer) on the tutorial site before starting.

## Objective

Connect the `CANDriveSubsystem` to the driver's Xbox controller by setting `driveArcade` as the default command. This is the final step — after this the robot will drive in teleop.

## File to Edit

`src/main/java/frc/robot/RobotContainer.java`

***

## Steps

**1)** Open `RobotContainer.java`. The `driveSubsystem` and `driverController` fields are already declared for you. Find the `configureBindings()` method — it is currently empty.

**2)** Inside `configureBindings()`, add the default command:

```java
driveSubsystem.setDefaultCommand(
    driveSubsystem.driveArcade(
        () -> -driverController.getLeftY() * DRIVE_SCALING,
        () -> -driverController.getRightX() * ROTATION_SCALING));
```

> [!NOTE]
> **Why negate the axes?**
> - Left Y: Pushing the stick away from you produces a negative value, but driving forward should be positive — so we negate it.
> - Right X: WPILib's `arcadeDrive` uses counter-clockwise-positive convention, so rightward stick movement (positive) is negated to produce clockwise (right) turns.

> [!TIP]
> The `() ->` syntax is a **lambda** — a short anonymous function. `() -> driverController.getLeftY()` creates a `DoubleSupplier` that returns the current Y axis value every time it is called. This is necessary because `driveArcade` re-reads the value every loop cycle rather than capturing it once. See [Creating the driveArcade Command Factory](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-the-drivearcade-command-factory) for more detail.

**3)** The `DRIVE_SCALING` and `ROTATION_SCALING` constants are already defined in `Constants.OperatorConstants`. They scale down joystick values to make the robot easier to control at full deflection. If your robot feels too slow or unresponsive, adjust these values in `Constants.java`.

> [!TIP]
> During initial testing, set the scaling constants to `0.3` or lower to drive the robot slowly while verifying it moves in the correct direction. Once you have confirmed forward/backward and turning work correctly, increase the values for normal operation.

***

## Understanding `setDefaultCommand`

`setDefaultCommand` tells the scheduler which command a subsystem should run when nothing else is commanding it. Since we always want the driver to be able to move the robot during teleop, the drive command is set as the default — it runs any time the subsystem is free.

If another command needs the drivetrain (such as an autonomous command), it will temporarily take over and the default command will resume when that command finishes.

***

## Expected Result

Your `configureBindings()` method should look like this:

```java
private void configureBindings() {
    driveSubsystem.setDefaultCommand(
        driveSubsystem.driveArcade(
            () -> -driverController.getLeftY() * DRIVE_SCALING,
            () -> -driverController.getRightX() * ROTATION_SCALING));
}
```

The project should now compile cleanly. Run `./gradlew build` (macOS/Linux) or `gradlew build` (Windows) to verify before deploying.

***

## Commit, Push, and Deploy

```bash
git add src/main/java/frc/robot/RobotContainer.java
git commit -m "Unit 4: set driveArcade as default command in RobotContainer"
git push
```

To deploy to the robot (connected via USB or radio):

```bash
./gradlew deploy        # macOS/Linux
gradlew deploy          # Windows
```

Or use the WPILib button in the VSCode status bar.

> [!IMPORTANT]
> Before deploying, confirm your team number is set correctly in `.wpilib/wpilib_preferences.json`.

***

## Testing on the Robot

- [ ] Robot enables in teleop without errors in the Driver Station
- [ ] Pushing the left stick forward drives the robot forward (not backward)
- [ ] Pushing the right stick right turns the robot clockwise
- [ ] Releasing the sticks stops the robot
- [ ] Robot is controllable at the default speed scaling

If the robot drives backward when the stick is pushed forward, the motor inversion in Unit 3 is on the wrong side — swap `config.inverted(true)` to the right leader instead of the left.

***

## Reference

- [Wiring Up in RobotContainer](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#wiring-up-in-robotcontainer)
- [Deploying Robot Code](https://robolancers.github.io/FRC-Java-Tutorial/programming/deploying.html)
- [WPILib — Subsystems and Commands](https://robolancers.github.io/FRC-Java-Tutorial/basics/wpilib.html#command-based-robot)
