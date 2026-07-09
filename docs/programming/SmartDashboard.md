# SmartDashboard

## Overview

In this section we will be going over

1. What SmartDashboard is and how it connects to your dashboard
2. How to put (publish) data from the robot to the dashboard
3. How to get (read) data from the dashboard on the robot
4. Common patterns: telemetry display and live tuning

***

## What is SmartDashboard

- **SmartDashboard** is a WPILib class that lets your robot publish and read values over **NetworkTables** — a key-value store shared between the robot and any connected driver-station application
- Every value is stored under a **key**, which is a string name you choose — both the robot code and the dashboard use the same key to refer to the same value
- Data you publish appears on any dashboard that reads NetworkTables, including **Elastic**, **Shuffleboard**, and the built-in SmartDashboard app — no code changes are needed to switch between them
- You publish data using `put` methods and read it back using `get` methods

!!! tip
    To use SmartDashboard, add this import to any file that calls it:

    ```java title="AnySubsystem.java"
    import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
    ```

***

## Putting Data

Use `put` methods to send data **from the robot to the dashboard**. Call these in a periodic method so the dashboard always shows the current values.

### putNumber

Publishes a `double` value under a key. Use this for any numeric sensor reading such as encoder positions, velocities, or voltages.

```java title="Subsystem.java"
SmartDashboard.putNumber("Drive Encoder Count", encoder.getPosition());
SmartDashboard.putNumber("Shooter RPM", shooter.getVelocity());
```

### putBoolean

Publishes a `boolean` value. Use this for limit switches, sensors, and any true/false state on the robot.

```java title="Subsystem.java"
SmartDashboard.putBoolean("Shooter Switch", shooter.isAtLimit());
SmartDashboard.putBoolean("Is Intaking", isIntaking);
```

### putString

Publishes a `String`. Use this for robot state names, mode descriptions, or any text you want to display.

```java title="Subsystem.java"
SmartDashboard.putString("Robot State", "Idle");
SmartDashboard.putString("Drive Mode", "Arcade");
```

### putData

Publishes a `Command` as a **clickable button** on the dashboard, or a `Sendable` object (such as a subsystem or PID controller) as an interactive widget.

```java title="Telemetry.java"
// Add a button to run a command from the dashboard
SmartDashboard.putData("Reset Drive Encoder", new DriveResetEncoder());
SmartDashboard.putData("Shoot", new ShootCommand());

// Send a subsystem for its built-in widget
SmartDashboard.putData("Drivetrain", m_drivetrain);
```

!!! tip
    Commands registered with `putData` show up as buttons in Elastic and Shuffleboard. Clicking the button schedules the command on the robot — useful for testing subsystems during development without needing driver input.

***

## Getting Data

Use `get` methods to read values **from the dashboard into the robot**. Every `get` method requires a **default value**, which is returned when the key does not exist yet or no dashboard has sent a value.

### getNumber

Returns a `double` stored under the given key.

```java title="Subsystem.java"
double voltage = SmartDashboard.getNumber("Feeder Voltage", 6.0);
motor.setVoltage(voltage);
```

### getBoolean

Returns a `boolean` stored under the given key.

```java title="Subsystem.java"
boolean enabled = SmartDashboard.getBoolean("Enable Shooter", false);
```

### getString

Returns a `String` stored under the given key.

```java title="Subsystem.java"
String mode = SmartDashboard.getString("Drive Mode", "Arcade");
```

!!! note "Always Provide a Safe Default"
    The default value is used the very first time the code runs, before any dashboard is connected. Choose a default that keeps the robot safe — for example, a voltage of `0.0` rather than a large value — so the robot behaves correctly even if the dashboard has never sent a value.

***

## Common Patterns

### Telemetry — Monitoring Robot State

The most common use of SmartDashboard is publishing sensor data so you can watch what the robot is doing in real time.

- In the **constructor**, register command buttons with `putData`
- In the **update** method, publish sensor readings with `putNumber` and `putBoolean`
- Call `update()` at the end of each periodic method in `Robot.java`

```java title="Telemetry.java"
--8<-- "docs/code_examples/Telemetry.java:full-class"
```

!!! note "Why call update() in periodic methods instead of robotPeriodic?"
    `robotPeriodic` runs at the very start of every loop, *before* the command scheduler runs. Putting `update()` there means the displayed values lag one cycle behind. By calling `update()` in `disabledPeriodic`, `autonomousPeriodic`, and `teleopPeriodic`, the dashboard reflects the most current values after all commands have finished executing for that loop.

### Live Tuning — Adjusting Values Without Redeploying

SmartDashboard lets you change a value on a running robot from the dashboard and see the effect immediately, without redeploying code. The pattern is:

1. In the **constructor**, call `putNumber` once to publish the starting value so the dashboard shows it right away
2. In your **methods**, call `getNumber` each time you need the value instead of using a hardcoded constant

```java title="MySubsystem.java"
public class MySubsystem extends SubsystemBase {
    private static final double DEFAULT_VOLTAGE = 6.0;

    public MySubsystem() {
        // Publish starting value once so the dashboard shows it immediately
        SmartDashboard.putNumber("Motor Voltage", DEFAULT_VOLTAGE);
    }

    public void run() {
        // Read the current dashboard value every time
        double voltage = SmartDashboard.getNumber("Motor Voltage", DEFAULT_VOLTAGE);
        motor.setVoltage(voltage);
    }
}
```

!!! warning "Only call putNumber once when live tuning"
    In the live tuning pattern, only call `put` in the **constructor**. Calling `put` inside a periodic method or `run()` will overwrite whatever value the dashboard operator entered, making live tuning impossible.

!!! tip
    Once you've found values you're happy with, copy them back into your `Constants.java` file so they are permanently saved. Dashboard values are not persisted between robot reboots unless you use the `Preferences` API — see [RobotPreferences](robotpreferences.md){target=_blank} for long-term storage.

***

## Knowledge Check

<!-- mkdocs-quiz intro -->

<quiz>
What class do you use to publish data to both SmartDashboard and Elastic?
- [ ] `Dashboard`
- [ ] `NetworkTables`
- [x] `SmartDashboard`
- [ ] `ElasticDashboard`

Both dashboards read from NetworkTables, but SmartDashboard is the API class. Use SmartDashboard.putNumber() and the data appears in both dashboards automatically.
</quiz>

<quiz>
What does `putNumber("Key", value)` do?
- [ ] Reads a number from the dashboard
- [x] Publishes a number value under the specified key
- [ ] Creates a new preference
- [ ] Opens a connection

`put` methods publish data from robot to dashboard. `get` methods read data from dashboard into robot.
</quiz>

<quiz>
What is the second parameter of `getNumber("Key", defaultValue)` used for?
- [ ] Maximum value allowed
- [ ] The key type
- [x] Returned when no value has been published yet
- [ ] The value units

The default Value is what the robot uses if nothing has been published to that key yet. This is important for the first run before any dashboard is connected.
</quiz>

<quiz>
The `putData()` method can publish a Command as a clickable button on the dashboard.
- [x] True
- [ ] False

`putData(someCommand)` sends the command to the dashboard, where it appears as a button. Clicking it schedules the command on the robot.
</quiz>

<quiz>
In the live tuning pattern, where should you call `putNumber()`?
- [ ] In the periodic method
- [ ] In the constructor (once)
- [ ] Every time you read the value
- [x] In the constructor (once)

The key is call putNumber once in the constructor. Then call getNumber every time you need the value. This lets the dashboard update change the value.
</quiz>

<!-- mkdocs-quiz results -->
