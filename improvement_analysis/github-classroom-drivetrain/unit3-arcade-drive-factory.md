# Unit 3 — Motor Configuration and Arcade Drive Factory

> [!NOTE]
> Read [Programming a RobotDrive](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#programing-a-robotdrive) and [Creating the driveArcade Command Factory](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-the-drivearcade-command-factory) on the tutorial site before starting.

## Objective

Complete the constructor by configuring leader/follower relationships and initializing `DifferentialDrive`, then add the `driveArcade` command factory method.

## File to Edit

`src/main/java/frc/robot/subsystems/CANDriveSubsystem.java`

***

## Part A — Configure Leader/Follower Relationships

The KitBot has two motors per side. One motor on each side is the **leader** — it runs directly. The other is the **follower** — it mirrors the leader. This is configured by applying a `SparkMaxConfig` to each motor using `configure()`.

**1)** Add imports for `PersistMode` and `ResetMode` at the top of the file using 💡 quick-fix. Both come from `com.revrobotics.spark.SparkBase`.

**2)** Inside the constructor, continuing after the `SparkMaxConfig` lines from Unit 2, configure the followers. Reuse the same `config` object:

- Call `config.follow(leftLeader)` to set the config to follower mode targeting the left leader, then call `leftFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)` to apply it.
- Repeat the same pattern for the right side: call `config.follow(rightLeader)`, then apply the config to `rightFollower`.

**3)** After both followers are configured, call `config.disableFollowerMode()` to clear the follower setting, then apply the config to `rightLeader`. This ensures the right leader runs independently.

**4)** Finally, call `config.inverted(true)` and apply the config to `leftLeader`. This inverts the left side so both sides drive the robot forward when positive power is applied.

> [!WARNING]
> Only invert one side — not both. If both sides are inverted (or neither is), the robot will spin in circles or fight itself. If the robot drives backwards, swap the inversion to the right leader instead of the left.

***

## Part B — Initialize DifferentialDrive

**5)** After the four `SparkMax` fields are initialized (and before the `setCANTimeout` calls), assign the `drive` field by constructing a new `DifferentialDrive` and passing it the left leader and right leader as arguments. Add the import via 💡 quick-fix if not already present.

> [!NOTE]
> `DifferentialDrive` takes the two **leader** motors only — the followers mirror them automatically through the configuration applied above.

***

## Part C — Add the driveArcade Command Factory

**6)** Below the `periodic()` method, add a public method named `driveArcade` that returns a `Command`. It should accept two `DoubleSupplier` parameters — one for forward/backward speed (e.g., `xSpeed`) and one for rotation (e.g., `zRotation`). Add imports for `DoubleSupplier` (from `java.util.function`) and `Command` (from `edu.wpi.first.wpilibj2.command`) via 💡 quick-fix.

Inside the method, return `this.run(...)` with a lambda that calls `drive.arcadeDrive(...)`, passing the current double values from each supplier via `getAsDouble()`.

> [!TIP]
> `this.run(...)` creates a command that calls the lambda on every scheduler loop while the command is scheduled. The subsystem is automatically registered as a requirement, so no other command can interrupt it without going through the scheduler.
>
> The parameters are `DoubleSupplier` (a function that returns a `double`) rather than plain `double` values. This means the joystick reading is re-evaluated every loop cycle, so the robot continuously responds to stick movement rather than being locked to a value captured at startup.

> [!TIP]
> To use tank drive instead of arcade, replace `drive.arcadeDrive(...)` with `drive.tankDrive(...)` and rename the method to `driveTank`.

***

## Expected Result

After this unit, the constructor should be complete: all four motors initialized, CAN timeouts set, a `SparkMaxConfig` applied to configure both followers (each following their respective leader), the right leader configured without follower mode, the left leader inverted, and `drive` assigned. The `driveArcade` method should be present below `periodic()` and return a `Command`.

The project should now compile cleanly. Build with **Ctrl+Shift+P → WPILib: Build Robot Code** to verify.

***

## Commit and Push

Stage `CANDriveSubsystem.java`, commit with a message like `"Unit 3: configure followers, invert, and add driveArcade factory"`, and push to trigger the auto-grader.

## Reference

- [Programming a RobotDrive](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#programing-a-robotdrive)
- [Creating the driveArcade Command Factory](https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-the-drivearcade-command-factory)
- [WPILib Command Based Robot](https://robolancers.github.io/FRC-Java-Tutorial/basics/wpilib.html#command-based-robot)
- [Java Methods](https://robolancers.github.io/FRC-Java-Tutorial/basics/java_methods.html)
