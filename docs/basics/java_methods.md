# Methods
<!-- This page was contributed by: Carl Stanton -->

Reusable blocks of code

## Overview

A **method** is a named block of code that performs a specific task. Methods let you write logic once and call (use) it as many times as needed — avoiding copying and pasting the same code in multiple places.

**See table of contents for a breakdown of this section.**

***

## Declaring a Method

```java
public void setSpeed(double speed) {
    motor.set(speed);
}
```

Every method declaration has four parts:

| Part | Example | Meaning |
|---|---|---|
| Access modifier | `public` | Who can call this method |
| Return type | `void` | What value this method gives back (`void` = nothing) |
| Name | `setSpeed` | How you call it |
| Parameters | `(double speed)` | Inputs the method needs to do its work |

***

## Return Types

A method either returns a value or returns nothing.

- `void` — the method does something but gives nothing back to the caller
- Any data type — the method computes and returns a value of that type

```java
// Returns nothing — just sets the motor
public void setSpeed(double speed) {
    motor.set(speed);
}

// Returns a boolean — whether the encoder has reached the target
public boolean atTarget() {
    return encoder.getPosition() >= targetPosition;
}

// Returns a double — the current motor output
public double getSpeed() {
    return motor.get();
}
```

!!! tip
    Use the `return` keyword to send a value back to the caller. The type after `return` must match the declared return type. A `void` method may use `return;` (no value) to exit early.

***

## Parameters

Parameters are inputs you pass to a method when calling it. You can have zero or more, separated by commas.

```java
// No parameters
public void stop() {
    motor.set(0);
}

// One parameter
public void setSpeed(double speed) {
    motor.set(speed);
}

// Two parameters
public void setLeftRight(double leftSpeed, double rightSpeed) {
    leftMotor.set(leftSpeed);
    rightMotor.set(rightSpeed);
}
```

!!! note
    Parameters are **local variables** — they only exist inside the method and disappear when the method finishes.

***

## Calling a Method

To run a method, write its name followed by parentheses containing any required arguments:

```java
// Calling a void method
drivetrain.setSpeed(0.5);

// Calling a method and storing the return value
boolean done = shooter.atTarget();

// Using a return value directly in a condition
if (shooter.atTarget()) {
    shooter.stop();
}
```

!!! example "FRC Example"
    In a command's `execute()` method, calling the drivetrain subsystem to move the robot:
    ```java
    m_drivetrain.setLeftRight(
        -driverController.getLeftY(),
        -driverController.getRightY()
    );
    ```

***

## `@Override`

When a class **extends** a parent class (see [Java Classes](java_classes.md#inheritance)), it can replace one of the parent's methods with its own version. The `@Override` annotation marks this intentionally.

```java
@Override
public void periodic() {
    // This replaces the empty periodic() in SubsystemBase
    // and runs every robot loop cycle
}
```

`@Override` is optional but strongly recommended — if you misspell the method name, the compiler will warn you that there is no matching method to override, catching the typo before it becomes a bug.

***

## Methods Calling Other Methods

Methods can call other methods, including methods of other objects. This is how FRC subsystems and commands interact.

```java
// A subsystem method that calls motor methods
public void stop() {
    leftMotor.set(0);
    rightMotor.set(0);
}

// A command calling a subsystem method
protected void execute() {
    m_drivetrain.stop();
}
```

!!! tip
    Methods that do one thing and do it well are easier to test, reuse, and understand. If a method is getting long, consider splitting it into smaller helpers.
