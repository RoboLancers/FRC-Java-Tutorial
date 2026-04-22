# Introduction to Swerve Drive

Swerve drive is one of the most powerful and versatile drivetrain designs available in FRC. This page explains the core concepts behind swerve drives and provides code examples to help you understand how they work.

If you're looking for detailed setup and configuration instructions, see the [full YAGSL tutorial](yagsl_swerve_tutorial.md).

***

## What is a Swerve Drive?

A **swerve drive** (also called omnidirectional or holonomic drive) is a drivetrain where each wheel can both:
1. **Rotate independently** (change its direction/angle)
2. **Move at different speeds** independently of other wheels

This gives the robot three independent degrees of freedom: move forward/backward, strafe left/right, and rotate. Unlike differential drives (where the robot must point in the direction it's moving), a swerve drive can move in any direction separately from rotating.

### Key Advantages

- **Omni-directional movement**: Move in any direction without rotating the robot body first
- **Improved maneuverability**: Tighter turns and more precise positioning during matches
- **Field-oriented control**: Intuitive driver control relative to the field, not the robot orientation
- **Powerful defense**: The ability to strafe makes defensive positioning and evasion much more effective

### Trade-offs

- **More complex hardware**: Each module has more motors and encoders than differential (tank) drives.
- **More complex software**: Requires more code to implement and maintain. 
- **Tuning required**: Drive and angle motors need careful PID tuning for smooth, responsive control
- **More failure points**: More motors and encoders mean more potential hardware failures, and more complex diagnostics.

---

## Understanding Swerve Modules

A **swerve module** is the building block of a swerve drive. Each module contains:

### 1. Drive Motor
- Propels the robot forward/backward
- Typically brushless motors like NEO or Falcon 500
- Geared with a reduction ratio (usually 6-8:1)
- Measured in rotation speed (RPM) but controlled in velocity

### 2. Steering (Angle) Motor
- Rotates the wheel to point in any direction
- Sometimes smaller brushless motor like NEO 550 or smaller Falcon
- Geared with a higher reduction ratio (typically 12-15:1)
- Provides independent wheel angle movement for each module.

### 3. Absolute Encoder
- Measures the current angle of the wheel
- Critical for knowing the wheel's orientation
- Common encoders: CANCoder (CTRE), Canandmag (Redux), or throughbore encoders
- "Absolute" means the encoder remembers its position even after power loss

### Module Layout
Most FRC swerve drives use 4 modules arranged in a rectangle:

```
        [Front-Left]    [Front-Right]
              ^                ^
              |                |
        Robot Center
              |                |
              v                v
        [Back-Left]     [Back-Right]
```

***

## Holonomic Drive and Kinematics

### What is Holonomic?

A **holonomic drivetrain** is one where the robot can move in any direction at any velocity, independent of its current orientation. Swerve drives are holonomic because:

- They can move forward while facing backward
- They can strafe sideways without rotating the chassis
- They can rotate while moving in a straight line

This is different from differential drives (like tank drive), which are **non-holonomic** because the robot must always move in the direction it's facing.

### ChassisSpeeds and Kinematics

[According to WPILib](https://docs.wpilib.org/en/2025/docs/software/kinematics-and-odometry/intro-and-chassis-speeds.html), **kinematics** is the study of motion without considering forces. In FRC, we use kinematics to convert between:

- **ChassisSpeeds**: The desired velocity of the robot chassis (vx, vy, omega)
- **SwerveModuleState**: The desired velocity and angle for each individual module

The `SwerveDriveKinematics` class handles this conversion automatically, so you don't need to do complex math. You just tell it how fast and in what direction you want the robot to move, and it calculates what each module should do.

***

## Field-Oriented vs. Robot-Oriented Drive

### Field-Oriented Drive (Recommended)

In **field-oriented drive**, the robot's movement is relative to the **field coordinate system**, not the robot's current orientation.

- Forward on the joystick always moves toward the same point on the field (e.g., toward the opponent's goal)
- This works regardless of which way the robot is currently facing
- Much more intuitive for drivers during competitions
- **This is the recommended mode for most FRC teams**

**Example**: If the robot is facing the alliance wall but you push forward on the joystick, the robot will strafe backward (away from the wall) because that's the "forward" direction in the field coordinate system.

From the `SwerveSubsystem` example:

```java title="Field-Oriented Drive Command"
public Command driveCommand(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier angularRotationX)
{
  return run(() -> {
    // Scale inputs for smoother control
    swerveDrive.drive(
      SwerveMath.scaleTranslation(new Translation2d(
        translationX.getAsDouble() * swerveDrive.getMaximumChassisVelocity(),
        translationY.getAsDouble() * swerveDrive.getMaximumChassisVelocity()), 0.8),
      Math.pow(angularRotationX.getAsDouble(), 3) * swerveDrive.getMaximumChassisAngularVelocity(),
      true,  // fieldRelative = true
      false
    );
  });
}
```

### Robot-Oriented Drive

In **robot-oriented drive**, the robot's movement is relative to **the robot's current orientation**.

- Forward on the joystick always moves in the direction the robot is currently facing
- Useful for precise manual control or when field orientation doesn't matter
- Can be confusing for drivers during matches since the joystick behavior changes as the robot rotates

```java title="Robot-Oriented Drive"
public void drive(Translation2d translation, double rotation, boolean fieldRelative)
{
  swerveDrive.drive(
    translation,
    rotation,
    false,  // fieldRelative = false
    false   // openLoop
  );
}
```

---

## Swerve Drive Configuration

YAGSL uses JSON configuration files to define your robot's specific swerve drive setup. These files handle all the hardware-specific details so your code can remain generic.

### Configuration Directory Structure

```
deploy/swerve/
├── swervedrive.json           # IMU and module file references
├── modules/
│   ├── frontleft.json         # Front-left module configuration
│   ├── frontright.json        # Front-right module configuration
│   ├── backleft.json          # Back-left module configuration
│   └── backright.json         # Back-right module configuration
├── physicalproperties.json    # Wheel size, gear ratios
├── pidfproperties.json        # Motor control tuning values
└── controllerproperties.json  # Heading correction and velocity control
```

### Global Configuration: swervedrive.json

This file defines the robot's IMU (gyroscope) and references the individual module configurations.

```json title="swervedrive.json Example - Pigeon2 IMU"
{
  "imu": {
    "type": "pigeon2",
    "id": 13,
    "canbus": "canivore"
  },
  "invertedIMU": true,
  "modules": [
    "frontleft.json",
    "frontright.json",
    "backleft.json",
    "backright.json"
  ]
}
```

!!! tip "IMU Selection"
    The IMU measures the robot's heading (rotation). Common options are:
    - **Pigeon 2**: CTRE's IMU (recommended for TalonFX-based systems)
    - **NavX**: A popular third-party IMU
    - **ADXRS450**: WPILib's basic gyro (analog or SPI)

### Individual Module Configuration

Each swerve module has its own JSON file that defines:
- Drive and steering motor CAN IDs and types
- Absolute encoder CAN ID and offset
- Motor inversion settings
- Physical location relative to the robot center

```json title="Module Configuration Example - SparkMax with CANCoder"
{
  "drive": {
    "type": "sparkmax_neo",
    "id": 4,
    "canbus": null
  },
  "angle": {
    "type": "sparkmax_neo",
    "id": 3,
    "canbus": null
  },
  "encoder": {
    "type": "cancoder",
    "id": 9,
    "canbus": null
  },
  "inverted": {
    "drive": false,
    "angle": false
  },
  "absoluteEncoderOffset": -114.609,
  "location": {
    "front": 12,
    "left": 12
  }
}
```

The `absoluteEncoderOffset` tells YAGSL how to interpret the encoder reading. It's the encoder value (in degrees) when the wheel is pointing "forward" (0°). You'll measure this during robot setup by pointing all wheels forward and recording the encoder values, then adjusting the offsets as needed.

### Physical Properties

This file defines your robot's physical parameters used in kinematics calculations:

```json title="physicalproperties.json Example"
{
  "conversionFactors": {
    "angle": {
      "gearRatio": 12.8,
      "factor": 0
    },
    "drive": {
      "gearRatio": 8.14,
      "diameter": 4,
      "factor": 0
    }
  },
  "currentLimit": {
    "drive": 40,
    "angle": 20
  },
  "rampRate": {
    "drive": 0.25,
    "angle": 0.25
  },
  "wheelGripCoefficientOfFriction": 1.19,
  "optimalVoltage": 12
}
```

!!! info "Physical Parameters"
    - **Gear Ratio**: Motor rotations per wheel rotation (drive) or per 360° turn (angle)
    - **Diameter**: Wheel diameter in inches
    - **Current Limit**: Maximum current for each motor in amps (protects from stalling)
    - **Ramp Rate**: How quickly motors accelerate (0.0-1.0; lower = slower acceleration)

### Motor Control Tuning (PIDF)

The `pidfproperties.json` file contains PIDF (Proportional, Integral, Derivative, Feedforward) tuning values for closed-loop motor control:

```json title="pidfproperties.json Example"
{
  "drive": {
    "p": 0.00023,
    "i": 0.0000002,
    "d": 1,
    "f": 0,
    "iz": 0
  },
  "angle": {
    "p": 0.01,
    "i": 0,
    "d": 0,
    "f": 0,
    "iz": 0
  }
}
```

!!! warning "PIDF Tuning is Important"
    These values directly affect how responsive and smooth your swerve drive feels. Poor PIDF tuning can result in:
    - Sluggish responses to driver input
    - Oscillation or jitter in the wheels
    - Inconsistent drive behavior
    
    See [Tuning PID Controllers](pid.md) for detailed tuning guidance.

---

## Creating a SwerveSubsystem

The `SwerveSubsystem` is the command-based subsystem that encapsulates all swerve drive functionality. Here's how to initialize it:

### Initializing the Swerve Drive

The YAGSL library handles most of the complexity. In your subsystem constructor, you simply point it to your configuration files:

```java title="SwerveSubsystem.java - Initialization"
public class SwerveSubsystem extends SubsystemBase {
  private final SwerveDrive swerveDrive;

  public SwerveSubsystem(File directory) {
    // Determine starting pose based on alliance color
    boolean blueAlliance = DriverStation.getAlliance().isPresent() && 
                           DriverStation.getAlliance().get() == Alliance.Blue;
    Pose2d startingPose = blueAlliance ? 
      new Pose2d(new Translation2d(Meter.of(1), Meter.of(4)), Rotation2d.fromDegrees(0)) :
      new Pose2d(new Translation2d(Meter.of(16), Meter.of(4)), Rotation2d.fromDegrees(180));

    // Configure telemetry verbosity
    SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;

    try {
      // Create the swerve drive from JSON configuration files
      swerveDrive = new SwerveParser(directory).createSwerveDrive(Constants.MAX_SPEED, startingPose);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // Configure advanced swerve drive settings
    swerveDrive.setHeadingCorrection(false);      // Disable heading correction for angular velocity control
    swerveDrive.setCosineCompensator(false);      // Disable cosine compensation (it causes sim discrepancies)
    swerveDrive.setAngularVelocityCompensation(true, true, 0.1);  // Correct for angular velocity skew
  }
}
```

!!! tip "What does the SwerveParser do?"
    The `SwerveParser` reads your JSON configuration files and creates a fully-configured `SwerveDrive` object. This means you don't have to manually instantiate motors, encoders, and PID controllers—YAGSL handles all of it based on your JSON configuration.

---

## Basic Driving Commands

### Field-Oriented Joystick Drive

The most common way to control a swerve drive during teleop is field-oriented drive with joystick input:

```java title="Field-Oriented Joystick Drive"
public Command driveCommand(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier angularRotationX) {
  return run(() -> {
    // Scale inputs for smoother, more controllable movement
    swerveDrive.drive(
      SwerveMath.scaleTranslation(new Translation2d(
        translationX.getAsDouble() * swerveDrive.getMaximumChassisVelocity(),
        translationY.getAsDouble() * swerveDrive.getMaximumChassisVelocity()), 0.8),
      Math.pow(angularRotationX.getAsDouble(), 3) * swerveDrive.getMaximumChassisAngularVelocity(),
      true,   // fieldRelative = true
      false   // openLoop = false
    );
  });
}
```

!!! note "Input Scaling"
    - `SwerveMath.scaleTranslation()` applies a scaling factor (0.8) to make translation smoother
    - `Math.pow(..., 3)` cubes the rotation input for smoother rotation control
    - Both techniques make the joystick feel more responsive at small movements and less twitchy at large ones

### Using ChassisSpeeds for Advanced Control

For autonomous routines or when you have calculated velocities (e.g., from path planning), use `ChassisSpeeds`:

```java title="ChassisSpeeds Drive"
public void driveFieldOriented(ChassisSpeeds velocity) {
  swerveDrive.driveFieldOriented(velocity);
}

public Command driveFieldOriented(Supplier<ChassisSpeeds> velocity) {
  return run(() -> {
    swerveDrive.driveFieldOriented(velocity.get());
  });
}
```

`ChassisSpeeds` contains three velocity components:
- **vxMetersPerSecond**: Velocity in the X direction (forward/backward in field coordinates)
- **vyMetersPerSecond**: Velocity in the Y direction (left/right in field coordinates)
- **omegaRadiansPerSecond**: Angular velocity (rotation rate)

---

## Odometry and Pose Tracking

One of the great features of swerve drives is accurate odometry tracking. The robot can estimate its position on the field using wheel encoders and the IMU.

### Getting the Robot's Current Pose

```java title="Odometry Example"
// Get the robot's current position and rotation
public Pose2d getPose() {
  return swerveDrive.getPose();
}

// Get just the heading
public Rotation2d getHeading() {
  return getPose().getRotation();
}
```

### Resetting Odometry

Reset the robot's position estimate when you know its actual position (e.g., at the start of autonomous):

```java title="Odometry Reset"
public void resetOdometry(Pose2d initialPose) {
  swerveDrive.resetOdometry(initialPose);
}

// Reset heading to 0 degrees
public void zeroGyro() {
  swerveDrive.zeroGyro();
}

// Align heading to alliance (blue = 0°, red = 180°)
public void zeroGyroWithAlliance() {
  if (isRedAlliance()) {
    zeroGyro();
    resetOdometry(new Pose2d(getPose().getTranslation(), Rotation2d.fromDegrees(180)));
  } else {
    zeroGyro();
  }
}
```

---

## Useful Utility Commands

### Center Modules

Point all wheels forward for a symmetric appearance (useful for demonstrations or diagnostics):

```java title="Center Modules"
public Command centerModulesCommand() {
  return run(() -> Arrays.asList(swerveDrive.getModules())
    .forEach(it -> it.setAngle(0.0)));
}
```

### Lock Pose

Prevent the robot from moving by pointing all wheels to form an "X" pattern, locking the robot in place:

```java title="Lock Pose"
public void lock() {
  swerveDrive.lockPose();
}
```

### Get Velocity

Monitor the robot's current velocity for diagnostics or control logic:

```java title="Get Current Velocity"
public ChassisSpeeds getFieldVelocity() {
  return swerveDrive.getFieldVelocity();
}

public ChassisSpeeds getRobotVelocity() {
  return swerveDrive.getRobotVelocity();
}
```

---

## Setting Up Swerve Drive in Your Robot

To use swerve drive in your FRC robot project:

### 1. Add YAGSL Vendor Dependency

Install YAGSL and all required vendor libraries:
- **YAGSL**: `https://yet-another-software-suite.github.io/YAGSL/yagsl.json`
- **REVLib**: `https://software-metadata.revrobotics.com/REVLib.json`
- **Phoenix 6**: `https://maven.ctr-electronics.com/release/com/ctre/phoenix6/latest/Phoenix6-frc2025-latest.json`
- **ReduxLib**: `https://frcsdk.reduxrobotics.com/ReduxLib.json`

See [3rd Party Libraries](3rd_party_libs.md) for installation instructions.

### 2. Generate Configuration Files

Use the [YAGSL Configuration Generator](https://broncbotz3481.github.io/YAGSL-Example/) to create your JSON configuration files based on your specific hardware.

### 3. Create Your SwerveSubsystem

Create a new subsystem class that extends `SubsystemBase` and initializes the `SwerveDrive` object (see the code examples above).

### 4. Create Driving Commands

Implement drive commands in your `RobotContainer` class that bind joystick axes to swerve driving.

### 5. Tune and Test

Test your swerve drive thoroughly:
- Verify all modules move in the correct direction
- Tune PIDF values for smooth, responsive control
- Test field-oriented and robot-oriented driving
- Verify heading correction and odometry tracking

---

## Common Issues and Debugging

### Wheels Don't Point in the Correct Direction

This usually indicates an **encoder offset** problem:
1. Point all wheels forward on the field
2. Record the values from each encoder
3. Update the `absoluteEncoderOffset` values in your module JSON files
4. The offset should be the negative of the encoder reading when the wheel is pointing forward

### Robot Spins Out of Control

Check your **inversion settings**. YAGSL provides [The Eight Steps](https://docs.yagsl.com/configuring-yagsl/the-eight-steps) for systematically fixing inversion issues.

### Drift in Odometry

Your **PIDF tuning** is likely too loose:
1. Ensure drive and angle PIDs are tuned properly
2. Verify your physical properties (wheel diameter, gear ratios) are correct
3. Make sure your IMU is mounted solidly in the center of the robot

### Jerky or Unresponsive Movement

This indicates **poor PIDF tuning**. See [Tuning PID Controllers](pid.md) for a detailed process.

---

## Next Steps

Now that you understand swerve drive concepts, check out these resources:

- **[Full YAGSL Tutorial](yagsl_swerve_tutorial.md)**: Complete setup guide with all configuration details
- **[WPILib Swerve Drive Kinematics](https://docs.wpilib.org/en/2025/docs/software/kinematics-and-odometry/swerve-drive-kinematics.html)**: Official kinematics documentation
- **[WPILib Swerve Drive Odometry](https://docs.wpilib.org/en/2025/docs/software/kinematics-and-odometry/swerve-drive-odometry.html)**: Odometry and pose tracking
- **[YAGSL Documentation](https://docs.yagsl.com/)**: Complete YAGSL reference
- **[YAGSL Examples Repository](https://github.com/Yet-Another-Software-Suite/YAGSL/tree/main/examples)**: Working example projects

---

## Key Takeaways

- **Swerve drives** are holonomic drivetrains where each wheel can rotate and move independently
- **Each module** contains a drive motor, steering motor, and absolute encoder
- **Field-oriented drive** is intuitive and recommended for competition
- **YAGSL** abstracts hardware complexity with JSON configuration files
- **Kinematics** converts between chassis velocity (what you want) and individual module states (what each wheel should do)
- **Configuration and tuning** are critical for smooth, reliable operation
