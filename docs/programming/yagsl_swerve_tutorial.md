# Setting Up a Swerve Drive with YAGSL for FRC

This tutorial provides a comprehensive, step-by-step guide to setting up a swerve drive using Yet Another Generic Swerve Library (YAGSL) for FIRST Robotics Competition (FRC) projects. YAGSL is designed to simplify swerve drive implementation by providing a generic, well-documented library that works with various motor controllers and sensors, eliminating the need for custom code for each robot configuration.

## 1. Introduction to YAGSL

YAGSL (Yet Another Generic Swerve Library) is a swerve drive library developed by current and former BroncBotz mentors for FRC teams. Its primary goal is to make swerve drive programming as straightforward as using a `DifferentialDrive`, while supporting a wide range of hardware combinations.

### Key Features
- **Generic Design**: Works with mixed hardware (e.g., REV SparkMax with CTRE CANCoder, TalonFX with Pigeon2, etc.)
- **JSON-Based Configuration**: Robot-specific settings are stored in JSON files, allowing the same code to work across different robots
- **Active Maintenance**: Regularly updated and community-supported
- **Comprehensive Documentation**: Extensive guides, examples, and troubleshooting resources

### Why YAGSL?
Unlike many swerve templates that require extensive modification, YAGSL abstracts hardware differences, so teams can focus on robot logic rather than drive code. It's particularly useful for teams with multiple robots or those using non-standard hardware combinations.

!!! tip "Two hardware paths, one tutorial"
    This tutorial walks through both of the most common FRC swerve hardware combinations side-by-side, using tabs like the ones below wherever the configuration differs:

    - **REVLib**: REV SparkMax controllers driving NEO motors, with a CTRE CANcoder for absolute position.
    - **TalonFX**: CTRE TalonFX controllers driving Kraken X60 / Falcon 500 motors, with a CTRE CANcoder and Pigeon 2 IMU.

    Every Java class YAGSL gives you (`SwerveSubsystem`, drive commands, odometry, etc.) is **identical** regardless of which hardware you use — YAGSL abstracts that difference away entirely into the JSON configuration files. The only thing that ever changes in your Java code is which deploy folder you point YAGSL at.

    If you're using all-CTRE hardware and want CTRE's own first-party generator instead of YAGSL's JSON abstraction, see the [CTRE Swerve Project Generator tutorial](ctre_swerve_generator_tutorial.md) for the alternative approach.

For more details, see the [YAGSL Overview](https://docs.yagsl.com/overview/what-we-do).

## 2. Prerequisites and Dependencies

Before starting, ensure you have the following installed and configured.

### Software Requirements
- **WPILib**: Latest stable version for your season (2025 recommended)
  - Installation guide: [WPILib Setup](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/wpilib-setup.html)
- **REV Hardware Client 2**: For configuring REV devices
  - Download: [REV Hardware Client](https://docs.revrobotics.com/rev-hardware-client-2)
- **CTRE Tuner X**: For configuring CTRE devices (Phoenix 6)
  - Installation: [Phoenix 6 Installation](https://v6.docs.ctr-electronics.com/en/latest/docs/installation/installation-frc.html)

### Vendor Dependencies (Vendordeps)
YAGSL requires vendor libraries for all supported hardware, even if not used on your robot. Install these via WPILib's vendor dependency system:

- **REVLib**: `https://software-metadata.revrobotics.com/REVLib.json`
- **Phoenix 6**: `https://maven.ctr-electronics.com/release/com/ctre/phoenix6/latest/Phoenix6-frc2025-latest.json`
- **ReduxLib**: `https://frcsdk.reduxrobotics.com/ReduxLib.json`
- **PhotonVision** (optional, for vision): `https://maven.photonvision.org/repository/internal/org/photonvision/PhotonLib-json/1.0/PhotonLib-json-1.0.json`
- **YAGSL**: `https://yet-another-software-suite.github.io/YAGSL/yagsl.json`

!!! note "You only strictly need the vendordep for your own hardware"
    YAGSL's JSON parser only touches the vendor library for the hardware types referenced in your config files. Installing REVLib (NEO) and Phoenix 6 (CTRE) side by side is harmless (and required if you ever mix hardware), but if your robot is purely CTRE (TalonFX) based, for example, you don't need to worry about configuring REV devices in the REV Hardware Client.

Installation steps: [3rd Party Libraries](https://docs.wpilib.org/en/stable/docs/software/vscode-overview/3rd-party-libraries.html#installing-libraries)

### Hardware Knowledge
You should know your robot's physical characteristics while configuring YAGSL. (See section 3 below for details).

## 3. Hardware Requirements and Getting to Know Your Robot

A swerve drive consists of:
- **Gyroscope/IMU**: For heading tracking (e.g., Pigeon2, NavX, or built-in IMU)
- **Swerve Modules**: Each containing:
  - Drive motor (e.g., NEO, Falcon500, Kraken)
  - Angle/steering motor (e.g., NEO 550, TalonFXS)
  - Absolute encoder (e.g., CANCoder, Canandmag, Thrifty Encoder)
- **CAN Bus**: For communication (required for most modern FRC hardware)

### Pre-Configuration Checklist
Before configuring YAGSL, gather these details about your robot:

- **IMU Type and ID**: What gyroscope are you using and what is its CAN ID?
- **Module Configuration**: For each swerve module:
  - Drive motor type, CAN ID, and gearing (gearing can be found on the swerve module manufacturer's spec sheet)
  - Angle motor type, CAN ID, and gearing
  - Encoder type, CAN ID.
  - Physical location relative to robot center (X, Y coordinates in inches)
- **Physical Properties**:
  - Wheel diameter
  - Drive gear ratio (motor rotations per wheel rotation)
  - Angle gear ratio (motor rotations per 360° module rotation)
  - Robot track width and wheelbase
  - Maximum speed (feet per second)
- **CAN Bus Configuration**: Ensure all devices have unique IDs and proper termination

For a complete list, see [Getting to Know Your Robot](https://docs.yagsl.com/configuring-yagsl/getting-to-know-your-robot).

## 4. Configuration Steps (JSON Files, Module Setup)

YAGSL uses JSON configuration files to define your robot's swerve drive. These files are placed in the `deploy/swerve/` directory of your robot project.

### Directory Structure
```plaintext
deploy/
└── swerve/
    ├── controllerproperties.json
    ├── modules/
    │   ├── frontleft.json
    │   ├── frontright.json
    │   ├── backleft.json
    │   └── backright.json
    ├── physicalproperties.json
    ├── pidfproperties.json
    └── swervedrive.json
```

!!! note "One folder per hardware set"
    The examples below live in two parallel folders, `swerve/neo/` (REVLib) and `swerve/talonfx/` (TalonFX), each a complete, standalone set of configs for the same physical robot. You only deploy one of them — whichever matches your hardware.

### Configuration Files Overview

#### swervedrive.json - Global Drive Configuration

This file defines the overall swerve drive configuration, including the IMU (gyroscope) settings and references to the individual module configuration files.

!!! abstract "Key Properties"
    - `imu`: Configures the gyroscope/IMU used for heading tracking
      - `type`: The type of IMU ("pigeon2", "navx", "adxrs450", etc.)
      - `id`: CAN ID of the IMU device
      - `canbus`: CAN bus name (usually "rio" for roboRIO bus, or your CANivore's name)
    - `invertedIMU`: Whether to invert the IMU reading (used for orientation correction)
    - `modules`: Array of module configuration file names

**Example - Pigeon2 IMU:**
```json title="swervedrive.json - Pigeon2 Configuration"
{
  "imu": {
    "type": "pigeon2",
    "id": 13,
    "canbus": "rio"
  },
  "invertedIMU": false,
  "modules": [
    "frontleft.json",
    "frontright.json",
    "backleft.json",
    "backright.json"
  ]
}
```

**Example - NavX IMU:**
```json title="swervedrive.json - NavX Configuration"
{
  "imu": {
    "type": "navx",
    "id": 0,
    "canbus": null
  },
  "invertedIMU": false,
  "modules": [
    "frontleft.json",
    "frontright.json",
    "backleft.json",
    "backright.json"
  ]
}
```

!!! tip "IMU choice is independent of motor vendor"
    A Pigeon2 works fine on a SparkMax/NEO robot, and a NavX works fine on a TalonFX robot — the IMU is a separate hardware choice from your drive/angle motor controllers. The complete examples below use a Pigeon2 for both hardware sets to keep the comparison focused on the motor/encoder differences.

**Complete swervedrive.json Example:**

=== "SparkMax"
    ```json title="swervedrive.json - Complete Example from swerve/neo"
    --8<-- "docs/code_examples/swerve/neo/swervedrive.json"
    ```

=== "TalonFX"
    ```json title="swervedrive.json - Complete Example from swerve/talonfx"
    --8<-- "docs/code_examples/swerve/talonfx/swervedrive.json"
    ```

#### Module JSON Files - Individual Swerve Module Configuration

Each swerve module (wheel) has its own configuration file defining the drive motor, angle motor, encoder, and physical location.

!!! abstract "Key Properties"
    - `drive`: Configuration for the drive (translation) motor
      - `type`: Motor controller type — e.g. `"sparkmax_neo"` for a SparkMax driving a NEO, `"talonfx"` for a TalonFX driving a Kraken X60/Falcon 500
      - `id`: CAN ID of the motor controller
      - `canbus`: CAN bus name
    - `angle`: Configuration for the angle (steering) motor, same `type` options as `drive`
    - `encoder`: Configuration for the absolute encoder
      - `type`: Encoder type ("cancoder", "canandmag", "thrifty", "throughbore", etc.)
    - `inverted`: Motor inversion settings
      - `drive`: Whether to invert drive motor direction
      - `angle`: Whether to invert angle motor direction
    - `absoluteEncoderOffset`: Encoder offset **in degrees** from 0°. May be negative.
    - `location`: Physical location relative to robot center
      - `front`: Distance forward from center (inches)
      - `left`: Distance left from center (inches, negative for right side)

!!! note "Measuring absoluteEncoderOffset"
    Point every wheel straight forward, read each CANcoder/encoder's raw position, and set `absoluteEncoderOffset` to the negative of that reading (in degrees) so the module reports 0° when facing forward.

**Example - Front-Left Module (frontleft.json):**

=== "SparkMax"
    ```json title="frontleft.json - SparkMax NEO with CANCoder"
    --8<-- "docs/code_examples/swerve/neo/modules/frontleft.json"
    ```

=== "TalonFX"
    ```json title="frontleft.json - TalonFX with CANcoder"
    --8<-- "docs/code_examples/swerve/talonfx/modules/frontleft.json"
    ```

**Example - Front-Right Module (frontright.json):**

=== "SparkMax"
    ```json title="frontright.json"
    --8<-- "docs/code_examples/swerve/neo/modules/frontright.json"
    ```

=== "TalonFX"
    ```json title="frontright.json"
    --8<-- "docs/code_examples/swerve/talonfx/modules/frontright.json"
    ```

**Example - Back-Left Module (backleft.json):**

=== "SparkMax"
    ```json title="backleft.json"
    --8<-- "docs/code_examples/swerve/neo/modules/backleft.json"
    ```

=== "TalonFX"
    ```json title="backleft.json"
    --8<-- "docs/code_examples/swerve/talonfx/modules/backleft.json"
    ```

**Example - Back-Right Module (backright.json):**

=== "SparkMax"
    ```json title="backright.json"
    --8<-- "docs/code_examples/swerve/neo/modules/backright.json"
    ```

=== "TalonFX"
    ```json title="backright.json"
    --8<-- "docs/code_examples/swerve/talonfx/modules/backright.json"
    ```

#### physicalproperties.json - Physical Robot Parameters

This file defines the physical characteristics of your robot and swerve modules that affect calculations.

!!! abstract "Key Properties"
    - `optimalVoltage`: Battery voltage for calculations (usually 12.0V)
    - `conversionFactors.drive.diameter`: Diameter of drive wheels in inches
    - `conversionFactors.drive.gearRatio`: Gear ratio from motor to wheel (motor rotations per wheel rotation)
    - `conversionFactors.angle.gearRatio`: Gear ratio from motor to module rotation (motor rotations per 360° module turn)
    - `currentLimit`: Maximum current for each motor in amps (protects from stalling)
    - `rampRate`: How quickly motors accelerate (0.0-1.0; lower = slower acceleration)

**Complete physicalproperties.json Example:**

=== "SparkMax"
    ```json title="physicalproperties.json - Complete Example from swerve/neo"
    --8<-- "docs/code_examples/swerve/neo/modules/physicalproperties.json"
    ```

=== "TalonFX"
    ```json title="physicalproperties.json - Complete Example from swerve/talonfx"
    --8<-- "docs/code_examples/swerve/talonfx/modules/physicalproperties.json"
    ```

!!! note "Current limits differ by hardware"
    SparkMax uses a single smart current limit per motor. TalonFX has more current-limit options (supply vs. stator current) but YAGSL's `currentLimit` field maps to a sane default for either — Kraken X60 drive motors can typically handle a higher limit (60A above) than a NEO (40A) before you risk browning out.

#### pidfproperties.json - Motor Control Tuning

This file contains PIDF (Proportional, Integral, Derivative, Feedforward) tuning values for both drive and angle motors.

!!! abstract "Key Properties"
    - `drive`: PIDF values for drive motors (translation)
      - `p`: Proportional gain
      - `i`: Integral gain
      - `d`: Derivative gain
      - `f`: Feedforward gain
      - `iz`: Integral zone (error threshold for integral accumulation)
    - `angle`: PIDF values for angle motors (steering)

!!! warning "These gains are not portable between vendors"
    SparkMax and TalonFX use different internal units and closed-loop scaling, so a PIDF value tuned for one will not behave the same on the other. Always start from the vendor-appropriate values below and re-tune for your own robot — see [section 7](#7-tuning-and-debugging).

**Complete pidfproperties.json Example:**

=== "SparkMax"
    ```json title="pidfproperties.json - Complete Example from swerve/neo"
    --8<-- "docs/code_examples/swerve/neo/modules/pidfproperties.json"
    ```

=== "TalonFX"
    ```json title="pidfproperties.json - Complete Example from swerve/talonfx"
    --8<-- "docs/code_examples/swerve/talonfx/modules/pidfproperties.json"
    ```

#### controllerproperties.json - Advanced Control Settings

This file configures advanced control parameters for heading correction (usually left at defaults). It has just two fields, and — unlike the module/physical/PIDF files above — it does **not** vary by motor vendor, since it configures the drivetrain-level heading controller rather than any individual motor.

!!! abstract "Key Properties"
    - `angleJoystickRadiusDeadband`: Minimum radius of the angle-control joystick input before a heading adjustment is applied
    - `heading`: PID values for heading correction (used when driving with a target heading, e.g. `driveCommand(x, y, headingX, headingY)`)
      - `p`: Proportional gain for heading control
      - `i`: Integral gain
      - `d`: Derivative gain

**Controllerproperties.json Example:**
```json title="controllerproperties.json - Complete Example from swerve/neo"
--8<-- "docs/code_examples/swerve/neo/controllerproperties.json"
```

### Using the Configuration Tool
YAGSL provides an online configuration generator: [YAGSL Config Tool](https://broncbotz3481.github.io/YAGSL-Example/)

1. Input your robot's physical parameters
2. Select hardware types and IDs for each module — choose `SparkMax` (NEO/NEO 550/Vortex) or `TalonFX` (Falcon 500/Kraken X60) as appropriate per motor, and your absolute encoder type
3. Download the generated configuration files
4. Place them in `src/main/deploy/swerve/`

For manual configuration details, see [Configuration Documentation](https://docs.yagsl.com/configuring-yagsl/configuration).

## 5. Code Setup and Integration

### Importing YAGSL
Add YAGSL as a vendor dependency (see section 2), then import in your code:
```java title="SwerveSubsystem.java - YAGSL Imports"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:imports"
```

### Creating the SwerveDrive Object
In your subsystem constructor, initialize the swerve drive from your JSON configuration files:
```java title="SwerveSubsystem.java - Constructor"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:constructor"
```

This is instantiated in `RobotContainer` by pointing a `SwerveSubsystem` at the deploy folder that matches your hardware — this deploy-folder name is the **only** line of Java that differs between the REVLib and TalonFX paths; every other class in this tutorial is identical either way:

=== "SparkMax"
    ```java
    private final SwerveSubsystem drivebase = new SwerveSubsystem(
        new File(Filesystem.getDeployDirectory(), "swerve/neo"));
    ```

=== "TalonFX"
    ```java
    private final SwerveSubsystem drivebase = new SwerveSubsystem(
        new File(Filesystem.getDeployDirectory(), "swerve/talonfx"));
    ```

### Telemetry Setup
YAGSL provides extensive telemetry for debugging. Configure verbosity before creating the SwerveDrive:
```java title="Telemetry Configuration"
// Configure telemetry verbosity before creating SwerveDrive
SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH; // Options: NONE, LOW, HIGH
```

This adds NetworkTables entries under `/SwerveDrive/` for monitoring module states, IMU data, and odometry.

!!! note "Telemetry Levels"
    - **NONE**: No telemetry output
    - **LOW**: Basic telemetry (odometry position)
    - **HIGH**: Comprehensive telemetry (module states, IMU data, velocities, raw encoder readings)

For more code setup details, see [Code Setup Documentation](https://docs.yagsl.com/configuring-yagsl/code-setup).

## 6. Basic Driving Code Examples

### Field-Oriented Drive Command

!!! tip
    Field-oriented drive means the robot moves relative to the field coordinate system, not its own orientation. Forward on the joystick always moves the robot toward the same direction on the field (e.g., toward the opponent's goal), regardless of how the robot is currently rotated. This is the most intuitive and commonly used drive mode for FRC competition robots.

```java title="SwerveSubsystem.java - Field-Oriented Drive Command"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:drive-command"
```

!!! note "Input Scaling"
    - `SwerveMath.scaleTranslation()` applies a scaling factor (0.8) to smooth translation
    - `Math.pow(..., 3)` cubes the rotation input for smoother rotation control
    - Joystick values are multiplied by maximum chassis velocity to convert from [-1, 1] to actual m/s

### Robot-Oriented Drive

!!! tip
    Robot-oriented drive means the robot moves relative to its own orientation. Forward on the joystick always moves the robot in the direction it's currently facing. This mode is useful for precise movements or when field orientation isn't important, but can be confusing for drivers during competition.

```java title="SwerveSubsystem.java - Robot-Oriented Drive"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:drive"
```

### ChassisSpeeds Drive

!!! tip
    ChassisSpeeds drive accepts a WPILib ChassisSpeeds object, which represents the desired velocity of the robot chassis. This is useful when integrating with path planning libraries like PathPlanner or when you have calculated velocities from other sources. It provides the most control over robot motion.

```java title="SwerveSubsystem.java - driveFieldOriented (void)"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:drive-field-oriented-void"
```

```java title="SwerveSubsystem.java - driveFieldOriented (Command)"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:drive-field-oriented-command"
```

```java title="SwerveSubsystem.java - drive (ChassisSpeeds)"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:drive-chassis-speeds"
```

### Joystick Integration

!!! tip
    `SwerveInputStream` chains joystick reads, deadband filtering, scaling, and alliance-relative control into a single reusable supplier of `ChassisSpeeds`. Pass it directly to `driveFieldOriented()` as the subsystem's default command. Axes are negated because standard joysticks return negative Y when pushed forward.

```java title="RobotContainer.java - SwerveInputStream"
--8<-- "docs/code_examples/swerve/RobotContainer.java:swerve-input-stream"
```

```java title="RobotContainer.java - Binding to Default Command"
--8<-- "docs/code_examples/swerve/RobotContainer.java:configure-bindings"
```

!!! note "Why are axes inverted?"
    Standard game controller joysticks return negative values when pushed forward (Y-axis inverted convention). Multiplying by `-1` corrects this so that pushing forward on the joystick actually moves the robot forward.

### Odometry and Pose Reset

!!! tip
    Odometry tracks the robot's position and orientation on the field using wheel encoders and IMU data. Pose reset is useful for correcting odometry drift, often done at the start of autonomous or when vision systems provide accurate position data.

```java title="SwerveSubsystem.java - getPose"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:get-pose"
```

```java title="SwerveSubsystem.java - resetOdometry"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:reset-odometry"
```

```java title="SwerveSubsystem.java - zeroGyro"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:zero-gyro"
```

```java title="SwerveSubsystem.java - zeroGyroWithAlliance"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:zero-gyro-with-alliance"
```

```java title="SwerveSubsystem.java - getHeading"
--8<-- "docs/code_examples/swerve/SwerveSubsystem.java:get-heading"
```

For more examples, see the [YAGSL Examples Repository](https://github.com/Yet-Another-Software-Suite/YAGSL/tree/main/examples).

## 7. Tuning and Debugging

### PIDF Tuning
YAGSL uses PIDF controllers for both drive and angle motors. Start with these values, then tune from there:

=== "SparkMax"
    ```json title="pidfproperties.json - SparkMax Starting Point"
    --8<-- "docs/code_examples/swerve/neo/modules/pidfproperties.json"
    ```

=== "TalonFX"
    ```json title="pidfproperties.json - TalonFX Starting Point"
    --8<-- "docs/code_examples/swerve/talonfx/modules/pidfproperties.json"
    ```

Tuning process:
1. Set P, I, D, F to 0
2. Increase P until oscillation occurs
3. Increase D to reduce jitter
4. Fine-tune as needed

For detailed tuning guides, see [WPILib PID Tuning](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/tuning-turret.html) and [YAGSL PIDF Tuning](https://docs.yagsl.com/configuring-yagsl/how-to-tune-pidf).

### The Eight Steps for Inversion
If your swerve drive spins out of control or has incorrect field orientation, use these systematic steps to fix inversion issues:

1. Set `invertIMU` to `false` in `swervedrive.json` and all drive motor `inverted` to `false` in module JSONs
2. Set `invertIMU` to `true`
3. Invert all drive motors (`"drive": {"inverted": true}`)
4. Set `invertIMU` to `false`
5. Flip module locations (swap front/back or left/right in configuration)
6. Uninvert drive motors (`"drive": {"inverted": false}`)
7. Set `invertIMU` to `true`
8. Invert drive motors again (`"drive": {"inverted": true}`)

Test after each step. Most robots work after step 1, 3, or 7.

For complete details, see [When to Invert](https://docs.yagsl.com/configuring-yagsl/when-to-invert) and [The Eight Steps](https://docs.yagsl.com/configuring-yagsl/the-eight-steps).

### Common Issues
- **Modules not facing correct direction**: Check absolute encoder offsets (remember: degrees, not rotations)
- **Robot drifting in odometry**: Verify IMU orientation and module locations
- **Gears grinding**: PID tuning issue, not inversion
- **Inconsistent behavior**: Ensure all modules have same hardware configuration
- **TalonFX-specific**: double-check the CAN bus name (`"rio"` vs. your CANivore's name) matches on every device — a mismatched `canbus` field is a common first-time TalonFX/CANcoder/Pigeon2 setup mistake

## 8. Links to Relevant Documentation

- **YAGSL Main Documentation**: [docs.yagsl.com](https://docs.yagsl.com/)
- **Configuration Tool**: [YAGSL Config Generator](https://broncbotz3481.github.io/YAGSL-Example/)
- **Examples Repository**: [GitHub Examples](https://github.com/Yet-Another-Software-Suite/YAGSL/tree/main/examples)
- **WPILib Swerve Kinematics**: [Swerve Drive Kinematics](https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/swerve-drive-kinematics.html)
- **CTRE Swerve Overview**: [Phoenix 6 Swerve](https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/mechanisms/swerve/swerve-overview.html)
- **REV Swerve Resources**: [REV Swerve Documentation](https://docs.revrobotics.com/brushless/neo/vortex/vortex-shafts)
- **Alternative approach**: [CTRE Swerve Project Generator Tutorial](ctre_swerve_generator_tutorial.md) — CTRE's own first-party codegen tool for all-CTRE hardware

## Additional Resources

- **Complete YAGSL Example Project**: [YAGSL-Example Repository](https://github.com/BroncBotz3481/YAGSL-Example) - A complete, working FRC robot project demonstrating YAGSL implementation
- **YAGSL Community**: Join the [BroncBotz Discord](https://discord.gg/broncbotz) for support
- **Known Configurations**: [YAGSL Configs Repository](https://github.com/BroncBotz3481/YAGSL-Configs)
- **Advanced Features**: Check examples for PathPlanner, PhotonVision, and SysId integration

This tutorial covers the essentials for getting started with YAGSL. For advanced features like vision integration or custom control algorithms, explore the examples and documentation further. Remember to test thoroughly on a test bench before field use!

***

## Knowledge Check

<!-- mkdocs-quiz intro -->

<quiz>
What does "holonomic" mean for a drivetrain?
- [ ] The robot can only move forward and backward
- [x] The robot can move in any direction at any velocity, independent of orientation
- [ ] The robot uses holonomic encoders
- [ ] The robot has four motors

A holonomic drivetrain can move in any direction without first rotating to face that direction. Swerve drives are holonomic; tank drives are not.
</quiz>

<quiz>
A team switches their swerve robot from SparkMax/NEO to TalonFX/Kraken motors. According to this tutorial, what Java code needs to change in `SwerveSubsystem.java` or `RobotContainer.java`?
- [ ] All of the drive commands need to be rewritten for TalonFX
- [ ] The `SwerveInputStream` joystick code needs vendor-specific logic
- [x] Nothing except the deploy folder name passed into the `SwerveSubsystem` constructor (e.g. `"swerve/neo"` to `"swerve/talonfx"`)
- [ ] The odometry and pose-reset methods need to be reimplemented

YAGSL abstracts hardware differences into the JSON configuration files. Every Java class in this tutorial works identically for both vendors — the only thing that changes is which deploy folder of JSON configs you point the `SwerveSubsystem` at.
</quiz>

<quiz>
What unit is `absoluteEncoderOffset` measured in in a YAGSL module JSON file?
- [ ] Rotations, from 0.0 to 1.0
- [x] Degrees, and it may be negative
- [ ] Encoder counts
- [ ] Radians

Despite some documentation describing it as a 0.0–1.0 rotation value, YAGSL's `absoluteEncoderOffset` is measured in degrees. You measure it by pointing the wheel forward, reading the raw encoder value, and setting the offset to the negative of that reading.
</quiz>

<!-- mkdocs-quiz results -->
