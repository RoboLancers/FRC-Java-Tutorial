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

Installation steps: [3rd Party Libraries](https://docs.wpilib.org/en/stable/docs/software/vscode-overview/3rd-party-libraries.html#installing-libraries)

### Hardware Knowledge
You should know your robot's physical characteristics before configuration. See section 3 for details.

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

- **IMU Type and ID**: What gyroscope are you using and its CAN ID?
- **Module Configuration**: For each swerve module:
  - Drive motor type, CAN ID, and gearing
  - Angle motor type, CAN ID, and gearing
  - Encoder type, CAN ID, and mounting offset
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

### Configuration Files Overview

#### swervedrive.json - Global Drive Configuration

This file defines the overall swerve drive configuration, including the IMU (gyroscope) settings and references to the individual module configuration files.

!!! summary "Key Properties"
    - `imu`: Configures the gyroscope/IMU used for heading tracking
      - `type`: The type of IMU ("pigeon2", "navx", "adxrs450", etc.)
      - `id`: CAN ID of the IMU device
      - `canbus`: CAN bus name (usually "rio" for roboRIO bus)
    - `invertedIMU`: Whether to invert the IMU reading (used for orientation correction)
    - `modules`: Array of module configuration file names

**Example - Pigeon2 IMU:**
```json
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
```json
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

**Complete swervedrive.json Example:**
```json
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

#### Module JSON Files - Individual Swerve Module Configuration

Each swerve module (wheel) has its own configuration file defining the drive motor, angle motor, encoder, and physical location.

!!! summary "Key Properties"
    - `drive`: Configuration for the drive (translation) motor
      - `type`: Motor controller type ("sparkmax", "talonfx", "talonsrx", etc.)
      - `id`: CAN ID of the motor controller
      - `canbus`: CAN bus name
    - `angle`: Configuration for the angle (steering) motor
    - `encoder`: Configuration for the absolute encoder
      - `type`: Encoder type ("cancoder", "analog", "thrifty", etc.)
    - `inverted`: Motor inversion settings
      - `drive`: Whether to invert drive motor direction
      - `angle`: Whether to invert angle motor direction
    - `absoluteEncoderInverted`: Whether to invert encoder reading
    - `absoluteEncoderOffset`: Encoder offset in rotations (0.0 to 1.0)
    - `location`: Physical location relative to robot center
      - `front`: Distance forward from center (inches)
      - `left`: Distance left from center (inches, negative for right side)

**Example - SparkMax with CANCoder:**
```json
{
  "drive": {
    "type": "sparkmax",
    "id": 2,
    "canbus": null
  },
  "angle": {
    "type": "sparkmax",
    "id": 1,
    "canbus": null
  },
  "encoder": {
    "type": "cancoder",
    "id": 10,
    "canbus": null
  },
  "inverted": {
    "drive": false,
    "angle": false
  },
  "absoluteEncoderInverted": false,
  "absoluteEncoderOffset": 0.0,
  "location": {
    "front": 12.0,
    "left": -12.0
  }
}
```

**Example - TalonFX with CANCoder:**
```json
{
  "drive": {
    "type": "talonfx",
    "id": 2,
    "canbus": null
  },
  "angle": {
    "type": "talonfx",
    "id": 1,
    "canbus": null
  },
  "encoder": {
    "type": "cancoder",
    "id": 10,
    "canbus": null
  },
  "inverted": {
    "drive": false,
    "angle": false
  },
  "absoluteEncoderInverted": false,
  "absoluteEncoderOffset": 0.0,
  "location": {
    "front": 12.0,
    "left": -12.0
  }
}
```

**Complete frontleft.json Example:**
```json
{
  "drive": {
    "type": "sparkmax",
    "id": 2,
    "canbus": null
  },
  "angle": {
    "type": "sparkmax",
    "id": 1,
    "canbus": null
  },
  "encoder": {
    "type": "cancoder",
    "id": 10,
    "canbus": null
  },
  "inverted": {
    "drive": false,
    "angle": false
  },
  "absoluteEncoderInverted": false,
  "absoluteEncoderOffset": 0.0,
  "location": {
    "front": 12.0,
    "left": -12.0
  }
}
```

#### physicalproperties.json - Physical Robot Parameters

This file defines the physical characteristics of your robot and swerve modules that affect calculations.

!!! summary "Key Properties"
    - `optimalVoltage`: Battery voltage for calculations (usually 12.0V)
    - `wheelDiameter`: Diameter of drive wheels in inches
    - `driveGearRatio`: Gear ratio from motor to wheel (motor rotations per wheel rotation)
    - `angleGearRatio`: Gear ratio from motor to module rotation (motor rotations per 360° module turn)

**Example - 4-inch wheels with 6.75:1 drive ratio:**
```json
{
  "optimalVoltage": 12.0,
  "wheelDiameter": 4.0,
  "driveGearRatio": 6.75,
  "angleGearRatio": 12.8
}
```

**Example - 3-inch wheels with 8.14:1 drive ratio:**
```json
{
  "optimalVoltage": 12.0,
  "wheelDiameter": 3.0,
  "driveGearRatio": 8.14,
  "angleGearRatio": 12.8
}
```

**Complete physicalproperties.json Example:**
```json
{
  "optimalVoltage": 12.0,
  "wheelDiameter": 4.0,
  "driveGearRatio": 6.75,
  "angleGearRatio": 12.8
}
```

#### pidfproperties.json - Motor Control Tuning

This file contains PIDF (Proportional, Integral, Derivative, Feedforward) tuning values for both drive and angle motors.

!!! summary "Key Properties"
    - `drive`: PIDF values for drive motors (translation)
      - `p`: Proportional gain
      - `i`: Integral gain
      - `d`: Derivative gain
      - `f`: Feedforward gain
      - `iz`: Integral zone (error threshold for integral accumulation)
    - `angle`: PIDF values for angle motors (steering)

**Example - SparkMax tuning values:**
```json
{
  "drive": {
    "p": 0.0020645,
    "i": 0.0,
    "d": 0.0,
    "f": 0.0,
    "iz": 0.0
  },
  "angle": {
    "p": 0.01,
    "i": 0.0,
    "d": 0.0,
    "f": 0.0,
    "iz": 0.0
  }
}
```

**Example - TalonFX tuning values:**
```json
{
  "drive": {
    "p": 1.0,
    "i": 0.0,
    "d": 0.0,
    "f": 0.0,
    "iz": 0.0
  },
  "angle": {
    "p": 50.0,
    "i": 0.0,
    "d": 0.32,
    "f": 0.0,
    "iz": 0.0
  }
}
```

**Complete pidfproperties.json Example:**
```json
{
  "drive": {
    "p": 0.0020645,
    "i": 0.0,
    "d": 0.0,
    "f": 0.0,
    "iz": 0.0
  },
  "angle": {
    "p": 0.01,
    "i": 0.0,
    "d": 0.0,
    "f": 0.0,
    "iz": 0.0
  }
}
```

#### controllerproperties.json - Advanced Control Settings

This file configures advanced control parameters for heading correction and velocity control (usually left at defaults).

!!! summary "Key Properties"
    - `heading`: PID values for heading correction
      - `p`: Proportional gain for heading control
      - `i`: Integral gain
      - `d`: Derivative gain
    - `velocity`: Velocity control PID values
      - `x`: PID for X-axis velocity control
      - `y`: PID for Y-axis velocity control

**Example - Default controller settings:**
```json
{
  "heading": {
    "p": 0.4,
    "i": 0.0,
    "d": 0.0
  },
  "velocity": {
    "x": {
      "p": 2.0,
      "i": 0.0,
      "d": 0.0
    },
    "y": {
      "p": 2.0,
      "i": 0.0,
      "d": 0.0
    }
  }
}
```

**Complete controllerproperties.json Example:**
```json
{
  "heading": {
    "p": 0.4,
    "i": 0.0,
    "d": 0.0
  },
  "velocity": {
    "x": {
      "p": 2.0,
      "i": 0.0,
      "d": 0.0
    },
    "y": {
      "p": 2.0,
      "i": 0.0,
      "d": 0.0
    }
  }
}
```

### Using the Configuration Tool
YAGSL provides an online configuration generator: [YAGSL Config Tool](https://broncbotz3481.github.io/YAGSL-Example/)

1. Input your robot's physical parameters
2. Select hardware types and IDs
3. Download the generated configuration files
4. Place them in `src/main/deploy/swerve/`

For manual configuration details, see [Configuration Documentation](https://docs.yagsl.com/configuring-yagsl/configuration).

## 5. Code Setup and Integration

### Importing YAGSL
Add YAGSL as a vendor dependency (see section 2), then import in your code:
```java
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;
```

### Creating the SwerveDrive Object
In your subsystem constructor:
```java
public class SwerveSubsystem extends SubsystemBase {
    private final SwerveDrive swerveDrive;

    public SwerveSubsystem() {
        // Configure telemetry verbosity
        SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
        
        try {
            // Create swerve drive from JSON configuration
            File swerveDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
            double maxSpeed = Units.feetToMeters(14.5); // Maximum speed in m/s
            swerveDrive = new SwerveParser(swerveDirectory).createSwerveDrive(maxSpeed);
            
            // Optional: Configure additional settings
            swerveDrive.setHeadingCorrection(true);
            swerveDrive.setCosineCompensator(true);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create swerve drive", e);
        }
    }
}
```

### Telemetry Setup
YAGSL provides extensive telemetry for debugging. Configure verbosity:
```java
SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH; // Options: NONE, LOW, HIGH
```

This adds NetworkTables entries under `/SwerveDrive/` for monitoring module states, IMU data, and odometry.

For more code setup details, see [Code Setup Documentation](https://docs.yagsl.com/configuring-yagsl/code-setup).

## 6. Basic Driving Code Examples

### Field-Oriented Drive Command

!!! tip
    Field-oriented drive means the robot moves relative to the field coordinate system, not its own orientation. Forward on the joystick always moves the robot toward the same direction on the field (e.g., toward the opponent's goal), regardless of how the robot is currently rotated. This is the most intuitive and commonly used drive mode for FRC competition robots.

```java
/**
 * Command to drive the robot using translative values and heading as angular velocity.
 */
public Command driveCommand(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier angularRotationX) {
    return run(() -> {
        // Scale inputs for smoother control
        Translation2d scaledInputs = SwerveMath.scaleTranslation(
            new Translation2d(translationX.getAsDouble(), translationY.getAsDouble()), 
            0.8
        );
        
        // Drive field-oriented
        swerveDrive.drive(
            scaledInputs, 
            angularRotationX.getAsDouble() * swerveDrive.getMaximumChassisAngularVelocity(),
            true,  // fieldRelative
            false  // openLoop
        );
    });
}
```

### Robot-Oriented Drive

!!! tip
    Robot-oriented drive means the robot moves relative to its own orientation. Forward on the joystick always moves the robot in the direction it's currently facing. This mode is useful for precise movements or when field orientation isn't important, but can be confusing for drivers during competition.

```java
public void driveRobotOriented(double xSpeed, double ySpeed, double rot) {
    swerveDrive.drive(
        new Translation2d(xSpeed, ySpeed).times(swerveDrive.getMaximumChassisVelocity()),
        rot * swerveDrive.getMaximumChassisAngularVelocity(),
        false, // fieldRelative = false
        false  // openLoop
    );
}
```

### ChassisSpeeds Drive

!!! tip
    ChassisSpeeds drive accepts a WPILib ChassisSpeeds object, which represents the desired velocity of the robot chassis. This is useful when integrating with path planning libraries like PathPlanner or when you have calculated velocities from other sources. It provides the most control over robot motion.

```java
public void driveFieldOriented(ChassisSpeeds velocity) {
    swerveDrive.driveFieldOriented(velocity);
}
```

### Joystick Integration

!!! tip
    Joystick integration shows how to connect driver inputs to the drive commands. The default command runs continuously while no other command is active. Note the axis inversions (-driverController.getLeftY()) which account for typical joystick orientations where pushing forward gives negative Y values.

```java
private final CommandXboxController driverController = new CommandXboxController(0);

public RobotContainer() {
    SwerveSubsystem drivebase = new SwerveSubsystem();
    
    // Set default command for field-oriented drive
    drivebase.setDefaultCommand(
        drivebase.driveCommand(
            () -> -driverController.getLeftY(),  // Forward/backward (inverted)
            () -> -driverController.getLeftX(),  // Left/right (inverted)
            () -> -driverController.getRightX()  // Rotation (inverted)
        )
    );
}
```

### Odometry and Pose Reset

!!! tip
    Odometry tracks the robot's position and orientation on the field using wheel encoders and IMU data. Pose reset is useful for correcting odometry drift, often done at the start of autonomous or when vision systems provide accurate position data.

```java
// Get current pose
public Pose2d getPose() {
    return swerveDrive.getPose();
}

// Reset odometry
public void resetOdometry(Pose2d pose) {
    swerveDrive.resetOdometry(pose);
}
```

For more examples, see the [YAGSL Examples Repository](https://github.com/Yet-Another-Software-Suite/YAGSL/tree/main/examples).

## 7. Tuning and Debugging

### PIDF Tuning
YAGSL uses PIDF controllers for both drive and angle motors. Start with these values:

**SparkMax-based systems:**
```json
{
  "drive": {"p": 0.0020645, "i": 0, "d": 0, "f": 0, "iz": 0},
  "angle": {"p": 0.01, "i": 0, "d": 0, "f": 0, "iz": 0}
}
```

**TalonFX-based systems:**
```json
{
  "drive": {"p": 1, "i": 0, "d": 0, "f": 0, "iz": 0},
  "angle": {"p": 50, "i": 0, "d": 0.32, "f": 0, "iz": 0}
}
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
- **Modules not facing correct direction**: Check absolute encoder offsets
- **Robot drifting in odometry**: Verify IMU orientation and module locations
- **Gears grinding**: PID tuning issue, not inversion
- **Inconsistent behavior**: Ensure all modules have same hardware configuration

## 8. Links to Relevant Documentation

- **YAGSL Main Documentation**: [docs.yagsl.com](https://docs.yagsl.com/)
- **Configuration Tool**: [YAGSL Config Generator](https://broncbotz3481.github.io/YAGSL-Example/)
- **Examples Repository**: [GitHub Examples](https://github.com/Yet-Another-Software-Suite/YAGSL/tree/main/examples)
- **WPILib Swerve Kinematics**: [Swerve Drive Kinematics](https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/swerve-drive-kinematics.html)
- **CTRE Swerve Overview**: [Phoenix 6 Swerve](https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/mechanisms/swerve/swerve-overview.html)
- **REV Swerve Resources**: [REV Swerve Documentation](https://docs.revrobotics.com/brushless/neo/vortex/vortex-shafts)

## Additional Resources

- **Complete YAGSL Example Project**: [YAGSL-Example Repository](https://github.com/BroncBotz3481/YAGSL-Example) - A complete, working FRC robot project demonstrating YAGSL implementation
- **YAGSL Community**: Join the [BroncBotz Discord](https://discord.gg/broncbotz) for support
- **Known Configurations**: [YAGSL Configs Repository](https://github.com/BroncBotz3481/YAGSL-Configs)
- **Advanced Features**: Check examples for PathPlanner, PhotonVision, and SysId integration

This tutorial covers the essentials for getting started with YAGSL. For advanced features like vision integration or custom control algorithms, explore the examples and documentation further. Remember to test thoroughly on a test bench before field use!