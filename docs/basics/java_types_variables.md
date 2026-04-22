# Variables and Data Types
<!-- This page was contributed by: Carl Stanton -->

The building blocks of every Java program

## Overview

Every piece of information in a Java program is stored in a **variable**. Before you can use a variable, you must declare it — give it a **name** and a **data type** that defines what kind of value it holds.

**See table of contents for a breakdown of this section.**

***

## Primitive Data Types

Java has several built-in data types. The four you will use most in FRC programming are:

| Type | Stores | Example |
|---|---|---|
| `int` | Whole numbers | `int motorPort = 1;` |
| `double` | Decimal numbers | `double speed = 0.75;` |
| `boolean` | True or false | `boolean isRunning = false;` |
| `String` | Text | `String subsystemName = "Drivetrain";` |

!!! note
    `String` starts with a capital letter because text behaves differently from numbers in Java — don't worry about why yet, just remember to capitalize it. You will learn more in [Java Classes](java_classes.md).

!!! warning "Java is case sensitive"
    `Int` and `int` are different. `Boolean` and `boolean` are different. Always use lowercase for primitive type names.

***

## Declaring and Assigning Variables

**Declaration** creates the variable. **Assignment** gives it a value. You can do both at once or separately:

```java
// Declare only — value is unset
int motorPort;

// Declare and assign together
int motorPort = 1;

// Assign later
motorPort = 2;
```

!!! example "FRC Example"
    ```java
    double driveSpeed = 0.5;          // half speed
    boolean isShooterRunning = false;
    int leftMotorPort = 1;
    ```

***

## Constants and `final`

If a variable should never change after it is first set, declare it with `final`. This prevents accidental reassignment and makes your intent clear.

```java
final int LEFT_MOTOR_PORT = 1;
```

In FRC code you will often see `public static final` together:

```java
public static final int LEFT_LEADER_ID = 1;
```

- `public` — any class can read this value
- `static` — shared across all uses of the class; accessible without creating an object first
- `final` — the value cannot be changed after assignment

!!! tip "Naming convention"
    Java constants are written in `ALL_CAPS_WITH_UNDERSCORES`. Regular variables use `lowerCamelCase`.

    ```java
    public static final int MAX_SPEED = 100;   // constant
    double currentSpeed = 0.0;                  // variable
    ```

***

## Scope

Where you declare a variable determines where it can be used — this is called **scope**.

- **Field** (class-level variable) — declared outside any method, available to all methods in the class. Fields are covered in depth in [Java Classes](java_classes.md#fields).
- **Local variable** — declared inside a method, only exists while that method is running

```java
public class Drivetrain extends SubsystemBase {

    private double topSpeed = 1.0;  // field — available in all methods

    public void setSpeed(double speed) {
        double adjusted = speed * topSpeed;  // local — only exists in this method
        motor.set(adjusted);
    }
}
```

!!! tip
    Declare hardware objects (motors, sensors) as fields so every method in the class can reach them. Use local variables for temporary calculations inside a single method.

***

## Operators

### Arithmetic

| Operator | Meaning | Example |
|---|---|---|
| `+` | Add | `score + 1` |
| `-` | Subtract | `distance - offset` |
| `*` | Multiply | `speed * 0.5` |
| `/` | Divide | `ticks / 2048.0` |
| `%` | Remainder (leftover after division) | `7 % 2` → `1` |

### Comparison (result is always `boolean`)

| Operator | Meaning |
|---|---|
| `==` | Equal to |
| `!=` | Not equal to |
| `<` | Less than |
| `>` | Greater than |
| `<=` | Less than or equal |
| `>=` | Greater than or equal |

### Logical (combine `boolean` values)

| Operator | Meaning | Example |
|---|---|---|
| `&&` | AND — both must be true | `isRunning && !isStopped` |
| `\|\|` | OR — at least one must be true | `buttonA \|\| buttonB` |
| `!` | NOT — flips true to false and vice versa | `!isFinished` |

***

## Comments

Comments are ignored by the compiler and are used to explain code to other programmers (including your future self).

```java
// Single line comment

/*
   Multi-line comment —
   spans multiple lines
*/

/**
 * Doc comment — appears when you hover over this item in VSCode.
 * Supports HTML formatting.
 */
```

***

## Naming Conventions

Consistent naming makes code readable across a team.

| What | Convention | Example |
|---|---|---|
| Variables and methods | `lowerCamelCase` | `driveSpeed`, `setMotorOutput` |
| Classes | `UpperCamelCase` | `Drivetrain`, `ShooterSubsystem` |
| Constants | `ALL_CAPS_UNDERSCORES` | `MAX_SPEED`, `LEFT_LEADER_ID` |
| Packages | all lowercase | `frc.robot.subsystems` |

***

## Knowledge Check

<!-- mkdocs-quiz intro -->

<quiz>
Which data type stores whole numbers without a decimal point?
- [x] `int`
- [ ] `double`
- [ ] `boolean`
- [ ] `String`

`int` holds integers like `1`, `42`, or `-7`. Use `double` when you need decimal precision (e.g., `0.75`).
</quiz>

<quiz>
What naming convention should Java constants follow?
- [ ] `lowerCamelCase`
- [ ] `UpperCamelCase`
- [x] `ALL_CAPS_UNDERSCORES`
- [ ] `kebab-case`

Constants use `ALL_CAPS_WITH_UNDERSCORES` to visually distinguish them from regular variables. Example: `public static final int MAX_SPEED = 100;`
</quiz>

<quiz>
A variable declared inside a method is called a:
- [ ] Field
- [ ] Constant
- [ ] Parameter
- [x] Local variable

Local variables exist only while the method runs. Fields are declared outside methods and are available to all methods in the class.
</quiz>

<quiz>
Which arithmetic operator gives the **remainder** after division?
- [ ] `/`
- [ ] `*`
- [ ] `-`
- [x] `%`

`%` is the modulo operator. For example, `7 % 3` equals `1` because 3 goes into 7 twice with 1 left over.
</quiz>

<quiz>
Which is the correct way to declare a motor port constant that any class can read and that can never change?
- [ ] `int LEFT_MOTOR_PORT = 1;`
- [ ] `private final int leftMotorPort = 1;`
- [x] `public static final int LEFT_MOTOR_PORT = 1;`
- [ ] `public int LEFT_MOTOR_PORT = 1;`

`public` makes it readable by other classes, `static` means it belongs to the class itself (no object needed), and `final` prevents reassignment. Together they form the standard Java constant pattern.
</quiz>

<!-- mkdocs-quiz results -->
