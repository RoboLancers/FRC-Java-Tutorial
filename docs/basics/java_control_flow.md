# Control Flow
<!-- This page was contributed by: Carl Stanton -->

Making decisions and repeating actions

## Overview

Programs rarely run straight from top to bottom. **Control flow** statements let your code make decisions, repeat actions, and respond to changing robot state — all essential skills for writing useful robot behavior.

**See table of contents for a breakdown of this section.**

***

## `if` / `else` (conditional statements)

An `if` statement runs a block of code only when a condition is `true`.

```java title="Speed clamp"
--8<-- "docs/code_examples/basics/control_flow/ControlFlowExamples.java:if_clamp"
```

Add `else` to handle the `false` case:

/// tab | SparkMax
    ```java title="Limit switch: stop on trigger"
    --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamples.java:if_else_limit"
    ```
///
/// tab | TalonFX
    ```java title="Limit switch: stop on trigger"
    --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamplesTalonFX.java:if_else_limit_talon"
    ```
///

Chain multiple conditions with `else if`:

/// tab | SparkMax
    ```java title="Elevator position control"
    --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamples.java:else_if_chain"
    ```
///
/// tab | TalonFX
    ```java title="Elevator position control"
    --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamplesTalonFX.java:else_if_chain_talon"
    ```
///

!!! tip
    Conditions must evaluate to a `boolean` (`true` or `false`). See [Variables and Data Types](java_types_variables.md#operators) for comparison and logical operators.

!!! example "FRC Example: soft limit"
    /// tab | SparkMax
        ```java title="Soft limit with if/else"
        --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamples.java:soft_limit_method"
        ```
    ///
    /// tab | TalonFX
        ```java title="Soft limit with if/else"
        --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamplesTalonFX.java:soft_limit_method_talon"
        ```
    ///
!!! warning
    If you are setting a value based on a condition, **always** include an `else` case to handle the `false` condition. In FRC code, this is a critical safety practice — for example, if you only set motor power in the `if` block, then when the condition is `false` the motor will continue doing whatever it was doing before, which could lead to dangerous runaway behavior.
***

## `while` Loop

A `while` loop repeats its body as long as its condition remains `true`.

/// tab | SparkMax
    ```java title="While loop example"
    --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamples.java:while_loop"
    ```
///
/// tab | TalonFX
    ```java title="While loop example"
    --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamplesTalonFX.java:while_loop_talon"
    ```
///

!!! warning "Do not use while loops inside robot commands"
    In FRC robot code, **do not put `while` loops inside `execute()`**. The robot scheduler calls `execute()` repeatedly on its own. An inner `while` loop would block the scheduler and freeze the robot. Use `isFinished()` to signal when a command is done instead:

```java title="isFinished() pattern"
--8<-- "docs/code_examples/basics/control_flow/ControlFlowExamples.java:is_finished"
```

`while` loops are appropriate in:

- Standalone Java programs (learning exercises, GitHub Classroom assignments)
- Utility code that runs outside the robot framework

***

## `for` Loop

A `for` loop runs a fixed number of times, or once for each item in a collection. It is the most common loop for counting and iterating.

### Counting loop

```java title="Counting loop"
--8<-- "docs/code_examples/basics/control_flow/ControlFlowExamples.java:for_counting"
```

The three parts of the `for` header:

| Part | Example | Purpose |
|---|---|---|
| Initializer | `int i = 0` | Runs once before the loop starts |
| Condition | `i < 5` | Checked before each iteration; loop stops when `false` |
| Update | `i++` | Runs after each iteration — `i++` is shorthand for `i = i + 1` (see [Arithmetic Operators](java_types_variables.md#arithmetic)) |

### Enhanced for-each loop

When you want every item in a collection and don't need the index, the for-each form is cleaner:

/// tab | SparkMax
    ```java title="For-each loop"
    --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamples.java:for_each_basic"
    ```
///
/// tab | TalonFX
    ```java title="For-each loop"
    --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamplesTalonFX.java:for_each_basic_talon"
    ```
///

!!! example "FRC Example: configuring multiple motors"
    Instead of calling the configuration method four separate times, apply config to all motors in a loop:

    /// tab | SparkMax
        ```java title="Motor configuration loop"
        --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamples.java:frc_motor_config"
        ```
    ///
    /// tab | TalonFX
        ```java title="Motor configuration loop"
        --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamplesTalonFX.java:frc_motor_config_talon"
        ```
    ///

***

## Putting It Together

A realistic example combining `if`/`else` and a field to implement elevator soft limits:

/// tab | SparkMax
    ```java title="Elevator subsystem"
    --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamples.java:elevator_subsystem"
    ```
///
/// tab | TalonFX
    ```java title="Elevator subsystem"
    --8<-- "docs/code_examples/basics/control_flow/ControlFlowExamplesTalonFX.java:elevator_subsystem_talon"
    ```
///

!!! note
    Notice that `moveUp` and `moveDown` each contain an `if`/`else` — the motor only runs if the elevator is within safe bounds. This pattern of checking limits before applying power is a fundamental safety practice in FRC.

***

## Knowledge Check

<!-- mkdocs-quiz intro -->

<quiz>
Why should you **not** put a `while` loop inside a command's `execute()` method in FRC robot code?
- [ ] `while` loops are slower than `for` loops
- [ ] `while` loops require a return value
- [x] It blocks the command scheduler, preventing other commands from running and potentially freezing the robot
- [ ] `while` loops cannot use boolean conditions inside robot code

The WPILib command scheduler calls `execute()` repeatedly — that repetition IS the loop. Adding your own `while` loop inside `execute()` traps the scheduler and stops the rest of the robot program from running.
</quiz>

<quiz>
In a `for` loop header (`for (int i = 0; i < 5; i++)`), which part runs exactly once before the loop begins?
- [x] The initializer (`int i = 0`)
- [ ] The condition (`i < 5`)
- [ ] The update statement (`i++`)
- [ ] The loop body

The initializer runs once to set up the counter variable. The condition is checked before every iteration, and the update runs after every iteration.
</quiz>

<quiz>
Which loop form is the cleanest choice when you need to visit every element in an array and do not need the index?
- [ ] `while` loop
- [ ] Traditional `for` loop with an index variable
- [x] Enhanced for-each loop (`for (Type item : collection)`)
- [ ] `do`-`while` loop

The for-each loop (`for (SparkMax motor : motors)`) removes the index variable entirely, reducing the chance of off-by-one errors and making the intent clearer.
</quiz>

<quiz>
What must always be true about the condition inside an `if` statement?
- [ ] It must use the `==` operator
- [ ] It must compare two `int` values
- [ ] It must call a method
- [x] It must evaluate to a `boolean` (`true` or `false`)

Java `if` statements require a `boolean` condition. Comparison operators (`<`, `>`, `==`) and logical operators (`&&`, `||`, `!`) all produce `boolean` results and are the typical building blocks of conditions.
</quiz>

<!-- mkdocs-quiz results -->
