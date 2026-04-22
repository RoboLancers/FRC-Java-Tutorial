# Methods
<!-- This page was contributed by: Carl Stanton -->

Reusable blocks of code

## Overview

A **method** is a named block of code that performs a specific task. Methods let you write logic once and call (use) it as many times as needed. This is better than copying and pasting: if you copy the same logic into five places and later find a bug, you have to fix it five times — and if you miss one, the bug remains.

**See table of contents for a breakdown of this section.**

***

## Declaring a Method

```java
public void setSpeed(double speed) {
    motor.set(speed);
}
```

!!! note
    In FRC examples, `motor` is a variable that holds a motor controller object (such as a SparkMax — a small electronic device that controls how fast a motor spins). Calling `motor.set(speed)` is itself a method call into the REV library. You will see this pattern throughout FRC code.

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

- `void` — the method does something but gives nothing back to the code that called it
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
    Use the `return` keyword to send a value back to the code that called this method. The type after `return` must match the declared return type. A `void` method may use `return;` (no value) to exit early.

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

***

## Knowledge Check

<!-- mkdocs-quiz intro -->

<quiz>
What does a `void` return type mean?
- [x] The method performs an action but does not return a value to the caller
- [ ] The method returns `null`
- [ ] The method returns the integer `0`
- [ ] The method is private and cannot be called from outside the class

`void` tells Java (and the reader) that this method does something — like setting a motor speed — but the caller gets nothing back. Use a typed return like `double` or `boolean` when you need to send a value back.
</quiz>

<quiz>
What is the main benefit of adding `@Override` when replacing a parent class method?
- [ ] It makes the method run faster
- [ ] It prevents other classes from calling the method
- [x] The compiler warns you if you misspell the method name, catching the typo before it becomes a bug
- [ ] It creates a brand new method that did not exist in the parent

`@Override` is optional, but if you misspell the method name (e.g., `priodic` instead of `periodic`), the compiler will catch it immediately rather than silently creating a second, unused method.
</quiz>

<quiz>
What are parameters in a method declaration?
- [ ] The value the method returns
- [ ] Fields that store the object's state
- [x] Named inputs the method receives from the caller
- [ ] Annotations that describe the method

Parameters are local variables declared in the method signature. When the method is called, the caller provides **arguments** that are assigned to those parameters.
</quiz>

<quiz>
If a method does not return a value, its return type should be:
- [ ] `null`
- [ ] `int`
- [x] `void`
- [ ] `boolean`

`void` is the keyword used when a method performs an action but has no value to hand back to the code that called it.
</quiz>

<!-- mkdocs-quiz results -->
