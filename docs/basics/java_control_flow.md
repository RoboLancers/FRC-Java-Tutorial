# Control Flow
<!-- This page was contributed by: Carl Stanton -->

Making decisions and repeating actions

## Overview

Programs rarely run straight from top to bottom. **Control flow** statements let your code make decisions, repeat actions, and respond to changing robot state — all essential skills for writing useful robot behavior.

**See table of contents for a breakdown of this section.**

***

## `if` / `else`

An `if` statement runs a block of code only when a condition is `true`.

```java
if (speed > 1.0) {
    speed = 1.0;  // clamp to maximum
}
```

Add `else` to handle the `false` case:

```java
if (limitSwitch.get()) {
    motor.set(0);       // stop if limit switch is triggered
} else {
    motor.set(0.5);     // otherwise keep moving
}
```

Chain multiple conditions with `else if`:

```java
if (encoderPosition < LOW_SETPOINT) {
    elevator.set(0.3);
} else if (encoderPosition > HIGH_SETPOINT) {
    elevator.set(-0.3);
} else {
    elevator.set(0);    // within range — hold position
}
```

!!! tip
    Conditions must evaluate to a `boolean` (`true` or `false`). See [Variables and Data Types](java_types_variables.md#operators) for comparison and logical operators.

!!! example "FRC Example — soft limit"
    ```java
    public void moveUp(double speed) {
        if (getHeight() < MAX_HEIGHT) {
            motor.set(speed);
        } else {
            motor.set(0);
        }
    }
    ```

***

## `while` Loop

A `while` loop repeats its body as long as its condition remains `true`.

```java
while (!atTarget()) {
    motor.set(0.5);
}
motor.set(0);
```

!!! warning "Do not use `while` loops inside robot commands"
    In FRC robot code, **do not put `while` loops inside `execute()`**. The robot scheduler calls `execute()` repeatedly on its own. An inner `while` loop would block the scheduler and freeze the robot — it would stop responding to the driver and could be a safety hazard.

    Use `isFinished()` to signal when a command is done instead:
    ```java
    @Override
    public boolean isFinished() {
        return m_subsystem.atTarget();
    }
    ```

`while` loops are appropriate in:

- Standalone Java programs (learning exercises, GitHub Classroom assignments)
- Utility code that runs outside the robot framework

***

## `for` Loop

A `for` loop runs a fixed number of times, or once for each item in a collection. It is the most common loop for counting and iterating.

### Counting loop

```java
for (int i = 0; i < 5; i++) {
    System.out.println("Iteration: " + i);
}
```

The three parts of the `for` header:

| Part | Example | Purpose |
|---|---|---|
| Initializer | `int i = 0` | Runs once before the loop starts |
| Condition | `i < 5` | Checked before each iteration; loop stops when `false` |
| Update | `i++` | Runs after each iteration (`i++` adds 1 to `i`) |

### Enhanced for-each loop

When you want every item in a collection and don't need the index, the for-each form is cleaner:

```java
SparkMax[] motors = {leftLeader, leftFollower, rightLeader, rightFollower};

for (SparkMax motor : motors) {
    motor.setSmartCurrentLimit(40);
}
```

!!! example "FRC Example — configuring multiple motors"
    ```java
    SparkMax[] motors = {leftLeader, leftFollower, rightLeader, rightFollower};

    for (SparkMax motor : motors) {
        motor.setSmartCurrentLimit(40);
        motor.setIdleMode(IdleMode.kBrake);
    }
    ```

    This is much cleaner than calling `setSmartCurrentLimit` four separate times.

***

## Putting It Together

A realistic example combining `if`/`else` and a field to implement elevator soft limits:

```java
public class ElevatorSubsystem extends SubsystemBase {

    private final SparkMax motor;
    private static final double MAX_HEIGHT = 50.0;  // encoder rotations
    private static final double MIN_HEIGHT = 0.0;

    public ElevatorSubsystem() {
        motor = new SparkMax(5, MotorType.kBrushless);
    }

    public void moveUp(double speed) {
        if (getHeight() < MAX_HEIGHT) {
            motor.set(speed);
        } else {
            motor.set(0);
        }
    }

    public void moveDown(double speed) {
        if (getHeight() > MIN_HEIGHT) {
            motor.set(-speed);
        } else {
            motor.set(0);
        }
    }

    public double getHeight() {
        return motor.getEncoder().getPosition();
    }

    @Override
    public void periodic() {
        // Could log getHeight() to the dashboard here
    }
}
```

!!! note
    Notice that `moveUp` and `moveDown` each contain an `if`/`else` — the motor only runs if the elevator is within safe bounds. This pattern of checking limits before applying power is a fundamental safety practice in FRC.
