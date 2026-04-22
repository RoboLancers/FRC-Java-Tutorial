# Unit 4: Wire Up in RobotContainer

**Objective:** Instantiate the drivetrain subsystem and bind the drive command to joystick input in `RobotContainer.java`.

**Estimated time:** 5 minutes

## What You'll Do

1. Create an instance of `CANDriveSubsystem` in `RobotContainer`
2. Bind the arcade-drive command to Xbox controller joystick axes
3. Set it as the default command so the robot always responds to the joystick

## Part 1: Instantiate the Subsystem

Open `src/main/java/frc/robot/RobotContainer.java` and declare a `private final CANDriveSubsystem` field named `m_driveSubsystem`. Initialize it directly in the declaration with `new CANDriveSubsystem()`. This follows the same pattern used for the `CommandXboxController` field that is already in the file.

Make sure `CANDriveSubsystem` is imported at the top of the file (VSCode Quick Fix can add this for you).

## Part 2: Bind the Drive Command

Inside the `configureBindings()` method, call `m_driveSubsystem.setDefaultCommand(...)` and pass it the result of calling `m_driveSubsystem.driveArcade(...)`.

The `driveArcade` method expects two `DoubleSupplier` arguments — provide them as lambdas (using `() ->` syntax):

- **Speed supplier:** negate the controller's left Y axis value and scale it by `OperatorConstants.DRIVE_SCALING`. 
  - Negating makes "stick pushed away from you" (negative joystick value) turn into "positive speed" (driving forward).
  - scaling prevents the robot from driving at full speed immediately, making it easier to verify direction and control during initial testing. Adjust `DRIVE_SCALING` in `Constants.java` to tune responsiveness on your robot.
- **Rotation supplier:** negate the controller's right X axis value and scale it by `OperatorConstants.ROTATION_SCALING`.
    - Rotation scaling is similar to drive scaling — it reduces the maximum turn rate for easier control. Adjust `ROTATION_SCALING` in `Constants.java` to tune turning responsiveness.

> **Why Invert the Axes?** On the Xbox controller, the left Y axis returns a negative value when pushed forward. Negating it maps that intuitive motion to positive forward speed. The right X axis sign depends on your motor direction — if rotation is reversed, remove the negation.

> **Why Scale?** The scaling factors (defined in `OperatorConstants` in `Constants.java`) prevent the robot from driving at full speed immediately, making it easier to verify direction and control during initial testing. Adjust these values to tune responsiveness on your robot.

See the **Wiring Up in RobotContainer** section of the [Driving Robot tutorial](../../docs/programming/driving_robot.md) for the full explanation, and the [TalonFX RobotContainer example](../../docs/code_examples/2026KitBotInlineTalonFX/RobotContainer.java) to check your work.

## Part 3: Verify Default Command Set

`setDefaultCommand()` ensures the subsystem always has a command scheduled. When no other command requires the drivetrain, the default command runs automatically. When another command needs the drivetrain, the default is interrupted and replaced.

## Testing

1. **Build the code** — use **Ctrl+Shift+P → WPILib: Build Robot Code** and verify no errors.

2. **Deploy to the robot** (if connected to an actual Robot) — use **Ctrl+Shift+P → WPILib: Deploy Robot Code**.

3. **Test in teleop:**
   - Push the left joystick forward/backward to drive.
   - Push the right joystick left/right to rotate.
   - If the robot drives backward when you push forward, check your motor inversion in Unit 3.

## Common Issues

**"Cannot find symbol CANDriveSubsystem"**
- Ensure the import is present at the top of `RobotContainer.java`.

**Robot doesn't respond to joystick**
- Verify in the Driver Station USB tab that your Xbox controller is on port 0 (or update `DRIVER_CONTROLLER_PORT` in `Constants.java` to match).
- Check that the `setDefaultCommand()` call is inside `configureBindings()`, which is called from the `RobotContainer` constructor.

**Motors spin in teleop but robot doesn't move**
- Verify CAN IDs with Phoenix Tuner X; the motor objects may be assigned to the wrong physical motors.
- Ensure TalonFX firmware is up to date (Phoenix Tuner X can update firmware).

## Summary: What You've Built

- Four `TalonFX` motors configured with current limits
- Followers set up via the `Follower` control class
- Motor inversions configured so positive voltage drives forward
- `driveArcade()` command factory that responds to joystick input
- Default command bound in `RobotContainer` so the robot drives in teleop

## Next Steps

Your drivetrain is now functional! Consider extending it:

- **Add autonomous routines** — create commands that drive specific distances using encoders
- **Add odometry** — track robot position using encoder data
- **Add smoothing** — filter joystick input to reduce jitter
- **Tune scaling factors** — adjust `DRIVE_SCALING` and `ROTATION_SCALING` in `Constants.java` to match your robot's behavior

See the [WPILib documentation](https://docs.wpilib.org/en/stable/) for more advanced drivetrain topics.
