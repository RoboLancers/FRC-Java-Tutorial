# Java Programming Basics

![Java](../assets/images/logos/java.png)

## Overview

Java is an **object-oriented**, general-purpose programming language. Everything in a Java program is organized into **classes** — blueprints that define objects made up of **variables** (data) and **methods** (behavior).

Java is the primary language used for FRC robot programming through the WPILib library.

!!! warning "Java is case sensitive"
    `Int` and `int` are different things. `drivetrain` and `Drivetrain` are different things. Capitalization always matters.

***

## Common Keywords

These keywords appear constantly in Java code. Keep them in mind as you work through the sections below.

| Keyword | Meaning |
|---|---|
| `public` | Accessible by any other class |
| `private` | Accessible only within the class it is declared in |
| `protected` | Accessible within the class and any subclasses |
| `static` | Belongs to the class itself, not a specific object |
| `final` | Cannot be changed after it is first assigned |
| `void` | The method returns no value |
| `return` | Sends a value back to the caller and exits the method |
| `null` | Represents the absence of a value (nothing / empty) |
| `new` | Creates a new object from a class |
| `this` | Refers to the current object inside a class |
| `extends` | Inherits fields and methods from another class |

***

## Knowledge Check

<!-- mkdocs-quiz intro -->

<quiz>
Java is case-sensitive, meaning `Drivetrain` and `drivetrain` are treated as different things.
- [x] True
- [ ] False

Java distinguishes uppercase from lowercase everywhere — in class names, variable names, and keywords. `Int` is not the same as `int`.
</quiz>

<quiz>
What does the `final` keyword do when applied to a variable?
- [ ] Marks the variable as publicly accessible
- [x] Prevents the variable from being reassigned after it is first set
- [ ] Makes the variable available to all classes
- [ ] Hides the variable from subclasses

`final` locks the variable's value. Once assigned, it cannot be changed — useful for constants like motor port numbers that should never move.
</quiz>

<quiz>
In Java, a class is best described as:
- [ ] A single executable statement
- [ ] A list of import statements
- [x] A blueprint that defines an object's fields and methods
- [ ] A keyword that creates a new object

Classes are templates. You define the blueprint once, then use `new` to create as many objects from it as you need.
</quiz>

<quiz>
Which keyword makes a variable belong to the class itself rather than a specific object?
- [ ] `public`
- [ ] `private`
- [x] `static`
- [ ] `final`

`static` means the variable or method belongs to the class, not to any instance (object). Access it as `ClassName.variable` without creating an object.
</quiz>

<!-- mkdocs-quiz results -->

***

## What to Read Next

The following pages each cover one core concept in depth, with FRC-focused examples throughout:

- [Variables and Data Types](java_types_variables.md) — types, constants, scope, operators, naming conventions
- [Methods](java_methods.md) — declaring methods, return types, parameters, calling methods
- [Classes and Objects](java_classes.md) — fields, constructors, inheritance, `@Override`
