# Building a Swerve Drivetrain with the CTRE Swerve Project Generator

<!-- This page was contributed by: -->

A guide to CTRE's own code-generation workflow for swerve drivetrains, and how to wire the generated code into a command-based FRC robot project.

***

## Overview

If your entire swerve drivetrain is CTRE hardware — TalonFX (or TalonFXS) drive/steer motors, CANcoders, and a Pigeon 2 — Tuner X can generate a complete, tuned drivetrain subsystem for you directly from Phoenix 6, without any third-party library. This is CTRE's **Swerve Project Generator**, and it's a different approach from the [YAGSL tutorial](yagsl_swerve_tutorial.md) already on this site.

!!! abstract "YAGSL vs. the CTRE Swerve Generator"
    - **YAGSL** is a community library that reads JSON files at runtime and works with mixed hardware (REV, CTRE, Redux, etc.). It's a good default if your team's hardware varies year to year, or you mix vendors.
    - **CTRE's generator** writes real, compiled Java (`TunerConstants.java` + `CommandSwerveDrivetrain.java`) directly against the Phoenix 6 API. It only supports CTRE devices, but in exchange you get first-party support and direct access to Phoenix 6/Pro features (advanced closed-loop control, CAN FD odometry rates, etc.) that YAGSL's abstraction layer doesn't expose.

    Neither is "better" — pick based on your hardware and how much you want direct control over the Phoenix 6 API versus a simpler JSON config.

This page assumes you're comfortable with the [swerve drive concepts](SwerveDriveIntro.md) (holonomic motion, field- vs. robot-oriented driving, kinematics) already covered on this site — it focuses on what's specific to CTRE's generator.

***

## Prerequisites

- **[CTRE Tuner X](https://v6.docs.ctr-electronics.com/en/stable/docs/installation/installation-frc.html){target=_blank}** installed and up to date for the current season.
- **Phoenix 6 vendordep** added to your robot project (see [3rd Party Libraries](../setup/3rd_party_libs.md)): `https://maven.ctr-electronics.com/release/com/ctre/phoenix6/latest/Phoenix6-frc2025-latest.json`
- All swerve hardware must be CTRE: **TalonFX or TalonFXS** for both drive and steer motors, **CANcoder** for absolute position, and a **Pigeon 2** for heading.
- Firmware on every device, and your Tuner X version, must match the current season's Phoenix 6 release — mismatched versions are a common source of generator failures.

!!! note "Exact version requirements"
    CTRE's requirements change with each season's Phoenix 6 release. Check [Swerve System Requirements](https://v6.docs.ctr-electronics.com/en/stable/docs/tuner/tuner-swerve/swerve-system-requirements.html){target=_blank} for the current version matrix rather than relying on this page — it will go stale faster than CTRE's own docs.

***

## Running the Generator

The Swerve Project Generator lives inside Tuner X, under the **Mechanisms** tab. At a high level, it walks you through:

!!! abstract "What the generator asks for"
    - Your **swerve module type** (e.g. a supported WCP/SDS module, or a fully custom configuration) — this fills in gear ratios for you if you pick a known module.
    - **Per-module CAN IDs** for each drive motor, steer motor, and CANcoder. CTRE's convention is Front-Left (1, 2, 3), Front-Right (4, 5, 6), Back-Left (7, 8, 9), Back-Right (10, 11, 12) for (drive, steer, encoder) — you're free to use your own numbering, but IDs must be unique across the whole CAN bus.
    - **CANcoder offsets**, which the generator measures for you via a self-test rather than you guessing them — point every wheel forward, run the self-test, and it reads and stores the offset.
    - **Track width, wheelbase, and Pigeon 2 CAN ID.**
    - Motor/encoder **inverts**, usually determined the same way — verified with the self-test rather than trial and error.

For the exact click-by-click steps and screenshots, follow CTRE's own walkthrough: [Creating your Project](https://v6.docs.ctr-electronics.com/en/stable/docs/tuner/tuner-swerve/creating-your-project.html){target=_blank}.

!!! tip "TunerConstants only, vs. a full project"
    The generator can output just `TunerConstants.java`, or a full project that also includes `CommandSwerveDrivetrain.java`. Once you've customized `CommandSwerveDrivetrain` (e.g. added vision integration), **re-run the generator in "TunerConstants only" mode** when you re-measure your robot — that updates your constants without overwriting the subsystem code you've since edited.

***

## What the Generator Gives You

The generated `generated/` package (under `src/main/java/frc/robot/generated/`) contains two files:

- **`TunerConstants.java`** — every hardware constant: CAN bus name, per-module CAN IDs/offsets/positions, gear ratios, wheel radius, and default PID/feedforward gains. It also exposes a `createDrivetrain()` factory method.
- **`CommandSwerveDrivetrain.java`** — a `Subsystem` (via `TunerSwerveDrivetrain`) that wraps Phoenix 6's `SwerveDrivetrain` for command-based use, exposing `applyRequest(...)`, `getState()`, `seedFieldCentric()`, and SysId characterization commands.

Here's a trimmed excerpt of what a generated `TunerConstants.java` looks like, so you recognize the shape when you open the real one — **don't hand-type this**, the generator writes the actual values from your robot:

```java title="TunerConstants.java (illustrative excerpt — generator output, not hand-written)"
--8<-- "docs/code_examples/ctre_swerve/TunerConstants.java:canbus"

--8<-- "docs/code_examples/ctre_swerve/TunerConstants.java:module-constants"
```

```java title="TunerConstants.java - createDrivetrain()"
--8<-- "docs/code_examples/ctre_swerve/TunerConstants.java:create-drivetrain"
```

!!! note "Rotations, not degrees"
    Notice `kFrontLeftEncoderOffset` is expressed in **rotations** (`Angle`/`Rotations.of(...)`), not degrees. If you've also read the YAGSL tutorial, don't carry that library's degree-based `absoluteEncoderOffset` convention over here — the two systems measure the same physical thing in different units.

For the full generated-file reference, see CTRE's [Swerve Builder API](https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/mechanisms/swerve/swerve-builder-api.html){target=_blank} docs.

***

## Wiring It Into a Command-Based Robot

This is the part CTRE's own docs gloss over, since the generator assumes a fresh project: dropping the generated code into `RobotContainer` alongside your other subsystems and commands.

1. Copy the generator's `generated/` package (and `CommandSwerveDrivetrain.java`, if you generated the full project) into your existing project's `src/main/java/frc/robot/`.
2. Instantiate the drivetrain once from `TunerConstants.createDrivetrain()`.
3. Build a `SwerveRequest.FieldCentric` request and bind it as the drivetrain's default command, reading joystick axes.

```java title="RobotContainer.java - Fields"
--8<-- "docs/code_examples/ctre_swerve/RobotContainer.java:fields"
```

```java title="RobotContainer.java - configureBindings()"
--8<-- "docs/code_examples/ctre_swerve/RobotContainer.java:configure-bindings"
```

!!! note "Why negate the joystick axes?"
    Same reason as every other drive code on this site: standard joysticks report negative Y when pushed forward, so `-driverXbox.getLeftY()` corrects it so "forward" on the stick means forward on the field.

***

## Driving Concepts: `SwerveRequest`

Instead of calling a `drive(...)` method directly like YAGSL's `SwerveSubsystem`, CTRE's generated drivetrain is driven by handing it a **`SwerveRequest`** — a small, reusable, mutable object describing "what should the drivetrain do right now." You build one request per behavior, then continuously re-apply it (usually via `applyRequest(() -> request)` as a default or triggered command) with fresh values each loop.

The handful of request types you'll actually use day-to-day:

- **`SwerveRequest.FieldCentric`** — drive relative to the field (the normal teleop mode).
- **`SwerveRequest.RobotCentric`** — drive relative to the robot's current facing.
- **`SwerveRequest.SwerveDriveBrake`** — lock the wheels in an X to resist being pushed.
- **`SwerveRequest.PointWheelsAt`** — point every wheel at a given angle without driving (diagnostics, defense).
- **`SwerveRequest.Idle`** — do nothing; useful bound to the disabled trigger so neutral mode still applies.

The full request catalog (including `FieldCentricFacingAngle`, `RobotCentricFacingAngle`, and Pro-only requests) is documented in CTRE's [Swerve Requests](https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/mechanisms/swerve/swerve-requests.html){target=_blank} reference — read it once you're comfortable with the pattern above, since most of it is variations on the same idea.

***

## Tuning & Troubleshooting

!!! abstract "Practical notes"
    - **PID/feedforward gains** live in `TunerConstants` as `Slot0Configs` for the drive and steer motors — tune these the same way you'd tune any Phoenix 6 closed-loop controller.
    - **CANcoder offsets are measured, not guessed.** Re-run the generator's self-test (or Tuner X's self-test tool directly) any time you re-mount a module, rather than hand-editing the offset.
    - **CAN bus name mismatches** are the most common first-run failure: every device (TalonFX, CANcoder, Pigeon 2) must agree on whether it's on `"rio"` or your CANivore's specific name. `TunerConstants.kCANBus` sets this in one place — check it first if devices aren't responding.
    - **Inverted drive/wrong module ordering** shows up as the robot driving diagonally or spinning instead of translating — re-verify CAN IDs and invert flags against what the self-test reported, rather than guessing new values.

CTRE's own tuning guidance (Slot0 gain tuning, current limits, closed-loop output types) is more complete and versioned to the current season, so treat it as the source of truth: [Swerve System Requirements](https://v6.docs.ctr-electronics.com/en/stable/docs/tuner/tuner-swerve/swerve-system-requirements.html){target=_blank} and the [Swerve Overview](https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/mechanisms/swerve/swerve-overview.html){target=_blank}.

***

## Links to Relevant Documentation

- **Swerve Project Generator**: [Tuner X Swerve docs](https://v6.docs.ctr-electronics.com/en/stable/docs/tuner/tuner-swerve/){target=_blank}
- **Creating your Project (step-by-step)**: [CTRE docs](https://v6.docs.ctr-electronics.com/en/stable/docs/tuner/tuner-swerve/creating-your-project.html){target=_blank}
- **System Requirements**: [CTRE docs](https://v6.docs.ctr-electronics.com/en/stable/docs/tuner/tuner-swerve/swerve-system-requirements.html){target=_blank}
- **Swerve Requests API**: [CTRE docs](https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/mechanisms/swerve/swerve-requests.html){target=_blank}
- **Swerve Overview**: [CTRE docs](https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/mechanisms/swerve/swerve-overview.html){target=_blank}
- **Full working examples**: [Phoenix6-Examples repository](https://github.com/CrossTheRoadElec/Phoenix6-Examples){target=_blank} (see `SwerveWithPathPlanner` and `SwerveWithChoreo` for complete generated projects)
- **Alternative approach**: [YAGSL Swerve Tutorial](yagsl_swerve_tutorial.md) — a mixed-hardware, JSON-config alternative to this generator

***

## Knowledge Check

<!-- mkdocs-quiz intro -->

<quiz>
Your team's swerve drivetrain uses only TalonFX motors, CANcoders, and a Pigeon 2. Based on this page, what's the main advantage of using CTRE's Swerve Project Generator instead of YAGSL?
- [ ] It supports mixed hardware from different vendors
- [x] It generates real Phoenix 6 code with direct access to advanced CTRE features, since you're not going through a hardware-abstraction layer
- [ ] It doesn't require any CAN bus configuration
- [ ] It works with REV SparkMax controllers as well

Because the generator only targets CTRE hardware, it can write code directly against the Phoenix 6 API instead of going through a generic abstraction layer — giving you access to features like advanced closed-loop control and CAN FD odometry rates that a mixed-hardware library like YAGSL doesn't expose.
</quiz>

<quiz>
In the generated `TunerConstants.java`, what does calling `TunerConstants.createDrivetrain()` do?
- [ ] It opens Tuner X so you can re-run the generator
- [x] It constructs a `CommandSwerveDrivetrain` from the generated drivetrain-wide and per-module constants
- [ ] It resets all CANcoder offsets to zero
- [ ] It creates a new RobotContainer

`createDrivetrain()` is a factory method the generator writes for you that instantiates `CommandSwerveDrivetrain` using `DrivetrainConstants` and the four generated `SwerveModuleConstants` (FrontLeft, FrontRight, BackLeft, BackRight). It's meant to be called once, typically to initialize a field in `RobotContainer`.
</quiz>

<quiz>
What is a `SwerveRequest` in the CTRE swerve API?
- [ ] A one-time command that drives the robot for exactly one second
- [ ] A JSON file describing the drivetrain's hardware
- [x] A reusable, mutable object describing what the drivetrain should do right now, which you continuously re-apply via `applyRequest(...)`
- [ ] A network request sent to the Driver Station

Rather than calling a `drive(...)` method with fresh arguments, CTRE's generated drivetrain is driven by handing it a `SwerveRequest` object (like `FieldCentric` or `SwerveDriveBrake`) whose fields you update each loop, typically via `drivetrain.applyRequest(() -> request.withVelocityX(...)...)` bound as a default or triggered command.
</quiz>

<quiz>
A generated `TunerConstants.java` expresses `kFrontLeftEncoderOffset` as `Rotations.of(0.152...)`. What unit does YAGSL's `absoluteEncoderOffset` use for the equivalent value, per the YAGSL tutorial on this site?
- [ ] Rotations, same as CTRE
- [ ] Radians
- [x] Degrees
- [ ] Encoder counts

The two systems measure the same physical quantity (how far the wheel's absolute encoder reading is from "pointing forward") but in different units — CTRE's generated code uses rotations, while YAGSL's JSON config uses degrees. Mixing them up is a common source of "my wheels are all facing the wrong way" bugs when switching between the two approaches.
</quiz>

<!-- mkdocs-quiz results -->
