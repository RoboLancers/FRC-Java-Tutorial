# Getting Started with PID
<!-- This page was contributed by:  -->

PID control lets your robot move to precise positions automatically — driving an elevator to exactly 24 inches, spinning a shooter wheel to exactly 3000 RPM, or turning to a specific angle — all without a driver manually adjusting the output.

**See the table of contents for a breakdown of this section.**

***

## Open-Loop vs. Closed-Loop Control

Every mechanism on your robot needs some kind of control. There are two fundamentally different approaches:

**Open-loop control** applies a fixed motor output with no feedback. You set the motor to 50% and hope the mechanism reaches the right position. Simple to implement, but unreliable — the motor output needed changes based on battery voltage, mechanical load, and friction.

**Closed-loop control** continuously measures where the mechanism actually is and adjusts the motor output to correct the error. PID is the most common closed-loop algorithm in FRC.

| | Open-Loop | Closed-Loop (PID) |
|---|---|---|
| Uses sensor feedback | No | Yes |
| Consistent across battery charge | No | Yes |
| Can hold a position | No | Yes |
| Complexity | Low | Medium |

!!! tip "When to use PID"
    Use PID any time you need to reach or hold a **specific position or velocity**. If you only need "up" and "down" with no target, open-loop is often enough.

***

## How PID Works

A PID controller measures the **error** between where the mechanism is and where you want it to be, then calculates a corrective output. It does this using three terms:

### Proportional (P)

The P term outputs a correction **proportional to the current error**. If the error is large, push harder. If the error is small, back off.

```
P output = kP × error
```

A higher `kP` means a stronger, faster response — but too high causes the mechanism to overshoot and oscillate.

### Integral (I)

The I term accumulates error over time and corrects for **persistent steady-state error** — cases where P alone can't quite reach the setpoint.

```
I output = kI × (sum of all past errors)
```

!!! warning "Avoid integral gain unless necessary"
    In FRC, integral gain often causes **integral windup** (accumulated error explodes) and oscillations. WPILib's own documentation recommends using feedforward instead of integral gain for most mechanisms. Start with kI = 0.

### Derivative (D)

The D term measures how fast the error is **changing** and dampens the response. It acts like a shock absorber — slowing the mechanism before it overshoots.

```
D output = kD × (rate of change of error)
```

A small `kD` helps prevent oscillations. Too much D causes sluggish, twitchy behavior.

### The Full Formula

The controller combines all three:

```
output = kP × error + kI × Σ(error) + kD × (Δerror / Δt)
```

In practice, the robot loop runs this calculation every 20 ms. WPILib's `PIDController` handles all the math automatically.

***

## WPILib's PIDController

WPILib provides a ready-to-use `PIDController` class in `edu.wpi.first.math.controller`.

### Creating a PIDController

```java
PIDController pidController = new PIDController(kP, kI, kD);
```

| Parameter | Type | Description |
|---|---|---|
| `kP` | `double` | Proportional gain |
| `kI` | `double` | Integral gain (start at 0) |
| `kD` | `double` | Derivative gain |

### Key Methods

**`calculate(measurement)`** — runs the PID algorithm and returns the corrective output. Call this every loop with the current sensor reading.

```java
double output = pidController.calculate(encoder.getPosition());
```

**`setSetpoint(setpoint)`** — sets the desired target value. The controller drives the measurement toward this value.

```java
pidController.setSetpoint(24.0); // target: 24 inches
```

**`setTolerance(positionTolerance)`** — defines what counts as "close enough" to the setpoint.

```java
pidController.setTolerance(0.5); // within 0.5 inches = at setpoint
```

**`atSetpoint()`** — returns `true` when the measurement is within the tolerance band.

```java
if (pidController.atSetpoint()) {
    // mechanism has arrived
}
```

**`reset()`** — clears the integral accumulator. Call this when re-enabling the controller after it has been idle.

```java
pidController.reset();
```

***

## Building a PID Elevator Subsystem

The cleanest pattern is to own the `PIDController` inside the subsystem, update it every loop in `periodic()`, and expose a `setHeight()` method for commands to call.

### Fields

```java title="PIDElevatorExample.java"
--8<-- "docs/code_examples/basics/pid/PIDElevatorExample.java:pid-fields"
```

Declare the `PIDController` and `ElevatorFeedforward` as `private final` fields alongside the motor and encoder.

### Constructor

```java title="PIDElevatorExample.java"
--8<-- "docs/code_examples/basics/pid/PIDElevatorExample.java:pid-constructor"
```

The `PIDController` is initialized with starting gains and a position tolerance. The feedforward handles gravity compensation — see [Adding Feedforward](#adding-feedforward) below.

### Setting the Target

```java title="PIDElevatorExample.java"
--8<-- "docs/code_examples/basics/pid/PIDElevatorExample.java:pid-setpoint"
```

Commands call `setHeight()` to update the setpoint. The controller starts correcting toward the new target on the next `periodic()` call.

### Checking Arrival

```java title="PIDElevatorExample.java"
--8<-- "docs/code_examples/basics/pid/PIDElevatorExample.java:pid-at-setpoint"
```

### The Periodic Loop

```java title="PIDElevatorExample.java"
--8<-- "docs/code_examples/basics/pid/PIDElevatorExample.java:pid-periodic"
```

`periodic()` runs every 20 ms. Each cycle:

1. Read the current position from the encoder
2. `calculate()` computes the PID correction voltage
3. `feedforward.calculate(0)` adds gravity compensation
4. `MathUtil.clamp()` prevents commanding more than ±12 V
5. `setVoltage()` applies the result to the motor

!!! note "Why setVoltage() instead of set()?"
    `setVoltage()` applies a specific number of volts regardless of battery voltage, which makes PID gains consistent across matches. `set()` applies a duty cycle that varies with battery voltage, which changes the effective gain as the battery drains.

### Command Factory

```java title="PIDElevatorExample.java"
--8<-- "docs/code_examples/basics/pid/PIDElevatorExample.java:pid-command"
```

`goToHeightCommand()` runs the setpoint update every loop and finishes automatically when `atSetpoint()` returns `true`. Bind it to a button in `RobotContainer`:

```java title="RobotContainer.java"
// Move elevator to 24 inches when A is pressed
driverController.a()
    .onTrue(m_elevator.goToHeightCommand(24.0));
```

***

## Tuning Your PID Controller

Start with all gains at zero and add them one at a time. Always use SmartDashboard or Elastic to monitor position and setpoint live.

!!! abstract ""
    **1) Start with P only.** Set `kI = 0`, `kD = 0`. Increase `kP` slowly until the mechanism moves toward the setpoint. Stop when it starts oscillating around the target.

!!! abstract ""
    **2) Reduce overshoot with D.** Increase `kD` from zero in small steps. You should see oscillations dampen. Stop before the response becomes sluggish.

!!! abstract ""
    **3) Add I only if needed.** If the mechanism consistently stops short of the setpoint, you may need a small `kI`. Keep it very small — even 0.001 can be significant.

!!! tip "Use SmartDashboard to tune live"
    Add `SmartDashboard.putNumber("Elevator/Position", getPosition())` in `periodic()` and use [Robot Preferences](robotpreferences.md) to adjust gains from the dashboard without redeploying code.

| Symptom | Likely Cause | Fix |
|---|---|---|
| Mechanism barely moves | kP too low | Increase kP |
| Overshoots and oscillates | kP too high, or no kD | Decrease kP, add kD |
| Slow, twitchy response | kD too high | Decrease kD |
| Stops consistently short | Steady-state error, gravity | Add feedforward |
| Integral winds up wildly | kI too high | Decrease kI, or remove it |

***

## Adding Feedforward

PID feedback corrects error after it happens. **Feedforward** predicts the output needed and applies it proactively — before any error builds up.

For an elevator, the most important feedforward term is **gravity compensation**: the motor must constantly push against gravity to hold the elevator at any height. PID alone can handle this, but it requires a non-zero steady-state error or integral gain. Feedforward handles it cleanly.

WPILib provides `ElevatorFeedforward` for this:

```java
ElevatorFeedforward feedforward = new ElevatorFeedforward(kS, kG, kV);
```

| Gain | Description | Start value |
|---|---|---|
| `kS` | Volts to overcome static friction | 0.0–0.5 V |
| `kG` | Volts to hold against gravity | 0.3–1.0 V (tune on robot) |
| `kV` | Volts per inch/second of velocity | 0.0 for simple position hold |

In `periodic()`, add the feedforward output to the PID output:

```java
double pidVolts = pidController.calculate(currentPosition);
double ffVolts  = feedforward.calculate(0); // velocity = 0 for holding
motor.setVoltage(MathUtil.clamp(pidVolts + ffVolts, -12.0, 12.0));
```

!!! tip "Tune kG first"
    Command the elevator to several heights with only `kG` applied (kP = 0). Increase `kG` until the elevator barely holds its position at mid-height. Then add `kP` to reach the setpoint precisely.

!!! note "Other feedforward types"
    WPILib also provides `SimpleMotorFeedforward` (flywheels, drivetrains) and `ArmFeedforward` (rotating arms). See the [WPILib feedforward documentation](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/feedforward.html){target=_blank} for details.

***

## Knowledge Check

<!-- mkdocs-quiz intro -->

<quiz>
What does the Proportional (P) term in a PID controller do?
- [ ] Accumulates past errors to eliminate steady-state error
- [x] Applies a correction proportional to the current error
- [ ] Dampens the rate of change to prevent overshoot
- [ ] Predicts the output needed before any error occurs

The P term multiplies the current error by kP. Large error → large correction. Small error → small correction.
</quiz>

<quiz>
Where should you call `pidController.calculate()` in a subsystem?
- [ ] In the constructor, once during initialization
- [ ] Inside a command's execute() method
- [x] In the subsystem's periodic() method, every loop
- [ ] Only when the setpoint changes

`calculate()` must run every loop (every 20 ms) to continuously correct for error. Placing it in `periodic()` ensures it runs regardless of which command is active.
</quiz>

<quiz>
Why is kI (integral gain) generally avoided in FRC?
- [ ] It makes the robot move faster than allowed
- [x] It can cause integral windup, leading to oscillations or runaway output
- [ ] WPILib's PIDController does not support integral gain
- [ ] It only works with TalonFX, not SparkMax

Integral gain accumulates error over time. If the mechanism is held away from the setpoint (e.g., against a hard stop), the integral term grows uncontrolled. Feedforward is usually a better solution for steady-state error.
</quiz>

<quiz>
What does `pidController.setTolerance(0.5)` do?
- [ ] Limits the maximum motor output to 0.5 volts
- [ ] Sets the maximum allowed error before the robot disables
- [x] Defines that within 0.5 units of the setpoint counts as "at setpoint"
- [ ] Sets how often the PID controller runs its calculation

`setTolerance()` configures the band around the setpoint where `atSetpoint()` returns true. A tolerance of 0.5 inches means the mechanism is considered "arrived" when within 0.5 inches of the target.
</quiz>

<!-- mkdocs-quiz results -->

***

## Next Steps

- [PID Elevator Example](../examples/pid_elevator.md) — a complete elevator subsystem with PID position control
- [PID Shooter Example](../examples/pid_shooter.md) — velocity PID for a flywheel shooter
- [Using Sensors](using_sensors.md) — encoders and limit switches that feed into the PID controller
- [WPILib PID Documentation](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/pidcontroller.html){target=_blank} — full API reference
- [WPILib Control Theory Introduction](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/introduction-to-pid.html){target=_blank} — deeper mathematical background
