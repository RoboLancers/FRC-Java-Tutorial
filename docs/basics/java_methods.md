# Methods
<!-- This page was contributed by: Carl Stanton -->

Reusable blocks of code

## Overview

A **method** is a named block of code that performs a specific task. In more simple terms, a method is a function inside a class. Methods let you write logic once and call (use) it as many times as needed. This is better than copying and pasting: if you copy the same logic into five places and later find a bug, you have to fix it five times — and if you miss one, the bug remains. With a method, you fix the bug once in the method definition, and every call to that method automatically gets the fix. 

**See table of contents for a breakdown of this section.**

***

## Declaring a Method

```java title="Method declaration"
--8<-- "docs/code_examples/basics/methods/Drivetrain.java:declare_method"
```

!!! note
    In FRC examples, `motor` is a variable that holds a motor controller object (such as a SparkMax or TalonFX — small electronic devices that control how fast a motor spins). Calling `motor.set(speed)` or `motor.setControl(...)` is itself a method call into the vendor library. You will see this pattern throughout FRC code.

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

- `void` — the method does something but gives nothing back (returns nothing) to the code that called it
- Any data type — the method computes and returns a value of that type

=== "SparkMax"
    ```java title="Return type examples"
    --8<-- "docs/code_examples/basics/methods/Drivetrain.java:return_types"
    ```

=== "TalonFX"
    ```java title="Return type examples"
    --8<-- "docs/code_examples/basics/methods/DrivetrainTalonFX.java:return_types_talon"
    ```

!!! tip
    Use the `return` keyword to send a value back to the code that called this method. The type after `return` must match the declared return type. A `void` method may use `return;` (no value) to exit early.

***

## Parameters

Parameters are inputs you pass to a method when calling it. You can have zero or more, separated by commas.

=== "SparkMax"
    ```java title="Parameter examples"
    --8<-- "docs/code_examples/basics/methods/Drivetrain.java:parameters"
    ```
=== "TalonFX"
    ```java title="Parameter examples"
    --8<-- "docs/code_examples/basics/methods/DrivetrainTalonFX.java:parameters_talon"
    ```

!!! note
    Parameters are **local variables** — they only exist inside the method and disappear when the method finishes.

***

## Method Overloading
You can have multiple methods with the same name but different parameters. This is called **method overloading**. The compiler determines which method to call based on the number and types of arguments you provide.

!!! example "Method Overloading"
    In this example, the `setSpeed` method is overloaded to accept either a single `double`, which tells the motor what speed to run at, or a `double` and a `boolean`, which controls whether to reverse the direction of the speed. 
    ```java title="Method overloading"
    --8<-- "docs/code_examples/basics/methods/Drivetrain.java:overloaded_methods"
    ``` 
Method overloading can make your code more flexible and easier to read, as you can provide different ways to call the same logical operation without needing to come up with different method names for each variation. in FRC, method overloading is often used for drivetrain control, allowing you to set speed with different levels of detail (e.g., just speed, or speed plus direction).

***
## Calling a Method

To run a method, write its name followed by parentheses containing any required arguments:

```java title="Calling methods"
--8<-- "docs/code_examples/basics/methods/CommandExamples.java:calling_methods"
```

!!! example "FRC Example"
    In a command's `execute()` method, calling the drivetrain subsystem to move the robot:
    ```java title="Drive call in execute()"
    --8<-- "docs/code_examples/basics/methods/CommandExamples.java:frc_drive_call"
    ```

***

## `@Override`

When a class **extends** a parent class (see [Java Classes](java_classes.md#inheritance)), the `@Override` annotation can replace one of the parent's methods with its own version. The annotation marks in a clear way that a method is being overridden.

```java title="Overriding periodic()"
--8<-- "docs/code_examples/basics/methods/Drivetrain.java:override_periodic"
```
!!! note
    `@Override` is optional but strongly recommended — if you misspell the method name, the compiler will warn you that there is no matching method to override, catching the typo before it becomes a bug.

***

## Methods Calling Other Methods

Methods can call other methods, including methods of other objects. This is how FRC subsystems and commands interact.

=== "SparkMax"
    ```java title="Methods calling other methods"
    --8<-- "docs/code_examples/basics/methods/CommandExamples.java:methods_calling_methods"
    ```
=== "TalonFX"
    ```java title="Methods calling other methods"
    --8<-- "docs/code_examples/basics/methods/CommandExamplesTalonFX.java:methods_calling_methods_talon"
    ```
=== "Explanation"
    ```markdown
    In this example, the `DriveForwardCommand` calls the `setSpeed()` method of the `Drivetrain` subsystem to move the robot forward. This is a common pattern in FRC code: commands call subsystem methods to control hardware.
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
