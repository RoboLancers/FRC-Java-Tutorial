# Classes and Objects
<!-- This page was contributed by: Carl Stanton -->

Classes are the blueprints of a Java program.

## Overview

Java is an **object-oriented** language. Almost everything in Java is an **object** — a bundle of data and behavior — and **classes** are the blueprints that define what an object looks like and what it can do.

In FRC, every subsystem, every command, and every piece of configuration is its own class. Many of these classes are provided for you by the WPILib library, but you will also write your own to define your robot's unique behavior.

***

## What is a Class?

A class is a template that defines a type of object. Classes contains:

- **Fields** — variables that store the object's state (e.g., motor controllers, sensors)
- **Methods** — functions that define what the object can do (e.g., `setSpeed`, `stop`)
- A **constructor** — a special method that sets the object up when it is first created


!!! note
    Classes are typically defined in their own file, and the file name must match the class name exactly (including capitalization). For example, a class named `Drivetrain` must be in a file named `Drivetrain.java`.
***

## Declaring a Class

```java title="Drivetrain.java"
package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
    // fields and methods go here
}
```

- `package` — declares which namespace this file belongs to (think of it as a folder path)
- `import` — brings in a class from another package so you can use it by short name
- `public` — this class can be used by other classes in the project
- `class Drivetrain` — the class name, which must match the file name exactly
- `extends SubsystemBase` — inherits built-in FRC subsystem behavior (see [Inheritance](#inheritance))

!!! note "Packages and imports in VSCode"
    VSCode can add `import` statements automatically. When the editor underlines a class name in red because it is not imported, click the 💡 light bulb (or press ++ctrl+period++) and choose the correct import. See [VSCode Tips](../basics/vscode_tips.md).

***

## Fields

Fields (also called instance variables or member variables) store the object's data. They are declared at the top of the class body, outside any method.

```java title="Fields example"
public class Drivetrain extends SubsystemBase {
    private SparkMax leftLeader;
    private SparkMax rightLeader;
    private double topSpeed;
}
```

### Access Modifiers

| Modifier | Meaning |
|---|---|
| `public` | Any class can read or modify this |
| `private` | Only this class can access it |
| `protected` | This class and any subclasses can access it |

!!! tip
    Default to `private` for fields. This prevents other classes from accidentally changing values they should not touch. Expose data only through `public` methods when needed. Accessing and setting protected fields is typeically done through "getter" and "setter" methods. This is called **encapsulation** and is a core principle of object-oriented programming.

***

## Constructors

A **constructor** is a special method that runs exactly once — when an object is created with `new`. Its job is to initialize all the fields and any other necessary setup.

- The constructor name must exactly match the class name
- Constructors have no return type (not even `void`)

```java title="Constructor example"
public class Drivetrain extends SubsystemBase {
    private SparkMax leftLeader;
    private SparkMax rightLeader;

    // Constructor
    public Drivetrain() {
        leftLeader  = new SparkMax(1, MotorType.kBrushless);
        rightLeader = new SparkMax(2, MotorType.kBrushless);
    }
}
```

### The `new` Keyword

`new` creates an object from a class and calls its constructor:

```java
Drivetrain myDrivetrain = new Drivetrain();
SparkMax motor = new SparkMax(1, MotorType.kBrushless);
```

The variable on the left holds a **reference** to the object — think of it as a label pointing to the object in memory.

### `this`

Inside a class, `this` refers to the current object. It is most commonly used when a constructor parameter has the same name as a field:

```java title="Example usage of this"
public class Drivetrain extends SubsystemBase {
    private double topSpeed;

    public Drivetrain(double topSpeed) {
        this.topSpeed = topSpeed;  // field = parameter
    }
}
```

***

## Inheritance

A class can `extend` another class to inherit all of its fields and methods. The class being extended is the **parent** (or superclass); the extending class is the **child** (or subclass).

```java title="Inheritance example"
public class Drivetrain extends SubsystemBase {
    // Drivetrain automatically has everything SubsystemBase provides
}
```

In FRC:

- All subsystems extend `SubsystemBase`
- Commands extend `Command` or are created via command factory methods on subsystems

Inheritance lets you build on top of pre-built FRC plumbing without writing it from scratch.

### `@Override`

When a child class replaces a method from the parent, it marks it with `@Override`:

```java
@Override
public void periodic() {
    // Replaces the empty version in SubsystemBase;
    // runs every robot loop cycle
}
```

The annotation is optional but recommended — if you misspell the method name, the compiler will tell you there is nothing to override, catching the typo early.

A method that is overridden replaces the parent version completely with your own implementation. If you want to run the parent method as well, you can call it with `super`:

```java title="Example of calling super"
@Override
public void periodic() {
    super.periodic();  // runs the parent version (if it does anything)
    // then add your own code here
}
```

***

## Putting It Together


/// details | A complete minimal subsystem class:
```java
package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    private final SparkMax leftLeader;
    private final SparkMax rightLeader;

    public Drivetrain() {
        leftLeader  = new SparkMax(1, MotorType.kBrushless);
        rightLeader = new SparkMax(2, MotorType.kBrushless);
    }

    public void setSpeed(double speed) {
        leftLeader.set(speed);
        rightLeader.set(speed);
    }

    @Override
    public void periodic() {
        // Update dashboard values here if needed
    }
}
```
///

!!! note
    `private final` on the fields means each motor controller is assigned once in the constructor and never swapped out. See [Variables and Data Types](java_types_variables.md#constants) for more on `final`.
