# WPILib FRC Third-Party Vendor Libraries — AI Agent Reference

> **Source:** Compiled from [docs.wpilib.org](https://docs.wpilib.org/en/stable/) and each vendor's official documentation.  
> **Season Applicability:** Primarily 2025–2026 FRC season.  
> **Purpose:** Context document for Claude or another AI agent to reason about FRC third-party vendor libraries, their APIs, and example code.

---

## How Vendor Libraries Work in WPILib

Vendor libraries are installed via **VendorDep JSON files**. In VS Code with WPILib:

- `Ctrl+Shift+P` → **WPILib: Manage Vendor Libraries** → Install new libraries (online or offline)
- Online installation: paste the vendor JSON URL
- Offline installation: download the vendor ZIP, extract to `C:\Users\Public\wpilib\2026\` (Windows) or `~/wpilib/2026/` (macOS/Linux)

JSON files are placed in the project's `vendordeps/` folder and processed by GradleRIO.

**WPILib Vendor JSON Repo (community registry):**  
https://github.com/wpilibsuite/vendor-json-repo

---

## Vendor/Third-Party Library Index

| # | Vendor / Library | Category | Languages | Vendor JSON URL |
|---|---|---|---|---|
| 1 | CTRE Phoenix 6 | Motor Controllers & Sensors | Java, C++, Python | `https://maven.ctr-electronics.com/release/com/ctre/phoenix6/latest/Phoenix6-frc2026-latest.json` |
| 2 | CTRE Phoenix 5 (Legacy) | Motor Controllers & Sensors | Java, C++ | `https://maven.ctr-electronics.com/release/com/ctre/phoenix/Phoenix5-frc2026-latest.json` |
| 3 | REV Robotics REVLib | Motor Controllers & Sensors | Java, C++ | `https://software-metadata.revrobotics.com/REVLib-2025.json` |
| 4 | The Thrifty Bot — ThriftyLib | Motor Controllers & Sensors | Java (C++ in progress) | Via docs.home.thethriftybot.com (copy link from site) |
| 5 | Playing With Fusion | Motor Controllers & Sensors | Java, C++ | `https://www.playingwithfusion.com/frc/playingwithfusion2025.json` |
| 6 | Redux Robotics ReduxLib | Sensors (CAN) | Java, C++ | `https://frcsdk.reduxrobotics.com/ReduxLib_2026.json` |
| 7 | Studica / Kauai Labs NavX | IMU / Navigation | Java, C++ | `https://dev.studica.com/maven/release/2026/json/Studica-2026.0.0.json` |
| 8 | PhotonVision / PhotonLib | Computer Vision | Java, C++, Python | `https://maven.photonvision.org/repository/internal/org/photonvision/PhotonLib-json/1.0/PhotonLib-json-1.0.json` |
| 9 | Limelight (LimelightLib) | Computer Vision | Java, C++ | Single-file (no vendordep — copy `.java` file directly) |
| 10 | PathPlanner (PathPlannerLib) | Path Planning / Autonomous | Java, C++, Python | Installed via WPILib Dependency Manager (search "PathPlanner") |
| 11 | Choreo (ChoreoLib) | Path Planning / Autonomous | Java, C++, Python | `https://choreo.autos/lib/ChoreoLib2026.json` |
| 12 | YAGSL | Swerve Drive Framework | Java | `https://broncbotz3481.github.io/YAGSL-Lib/yagsl/yagsl.json` |
| 13 | maple-sim | Physics Simulation | Java | `https://shenzhen-robotics-alliance.github.io/maple-sim/vendordep/maple-sim.json` |
| 14 | AdvantageKit | Telemetry / Logging | Java only | Available via WPILib Dependency Manager |
| 15 | Monologue | Telemetry / Logging | Java only | Via Maven / Dependency Manager |
| 16 | Elastic Dashboard | Driver Dashboard | Standalone app | N/A |
| 17 | AdvantageScope | Telemetry Viewer | Standalone app | N/A |
| 18 | DSLOG | Telemetry Viewer | Standalone app | N/A |
| 19 | RobotPy | Python Framework | Python | `pip install robotpy` |

---

## Detailed Vendor Documentation

---

### 1. CTR Electronics (CTRE) — Phoenix 6

**Overview:** CTRE manufactures the TalonFX motor controller (used in Falcon 500, Kraken x60, Kraken x44), as well as the CANcoder, Pigeon 2.0 IMU, CANifier, and CANdle LED controller. Phoenix 6 is the current API; Phoenix 5 is legacy and required only for older devices (TalonSRX, VictorSPX).

**Supported Devices:**
- **TalonFX** (Falcon 500, Kraken x60, Kraken x44) — brushless motor controller
- **TalonSRX** — brushed motor controller (Phoenix 5 only)
- **VictorSPX** — basic motor controller (Phoenix 5 only)
- **CANcoder** — absolute magnetic encoder
- **Pigeon 2.0** — 9-axis IMU
- **CANifier** — general-purpose CAN I/O
- **CANdle** — CAN-controlled LED controller
- **TalonFXS** — new for 2026 (with CANdi support)

#### Links

| Resource | URL |
|---|---|
| **Main Documentation (Phoenix 6)** | https://v6.docs.ctr-electronics.com/en/stable/ |
| **Main Documentation (Phoenix 5 Legacy)** | https://v5.docs.ctr-electronics.com/ |
| **API Reference — Java (Phoenix 6)** | https://api.ctr-electronics.com/phoenix6/stable/java/ |
| **API Reference — C++ (Phoenix 6)** | https://api.ctr-electronics.com/phoenix6/stable/cpp/ |
| **API Reference — Python (Phoenix 6)** | https://api.ctr-electronics.com/phoenix6/stable/python/ |
| **API Reference — Java (Phoenix 5)** | https://api.ctr-electronics.com/phoenix/stable/java/ |
| **API Reference — C++ (Phoenix 5)** | https://api.ctr-electronics.com/phoenix/stable/cpp/ |
| **Example Code (GitHub)** | https://github.com/CrossTheRoadElec/Phoenix6-Examples |
| **Source / Issue Tracking** | https://github.com/CrossTheRoadElec/Phoenix-Releases |
| **Full Changelog** | https://api.ctr-electronics.com/changelog |
| **Known Issues** | https://api.ctr-electronics.com/known_issues |
| **Installation Guide** | https://v6.docs.ctr-electronics.com/en/stable/docs/installation/installation-frc.html |
| **Phoenix Tuner X** (device configuration app) | https://v6.docs.ctr-electronics.com/en/stable/docs/tuner/index.html |
| **Vendordep JSON (Phoenix 6, 2026)** | `https://maven.ctr-electronics.com/release/com/ctre/phoenix6/latest/Phoenix6-frc2026-latest.json` |
| **Vendordep JSON (Phoenix 5, 2026)** | `https://maven.ctr-electronics.com/release/com/ctre/phoenix/Phoenix5-frc2026-latest.json` |
| **Vendordep JSON (Phoenix 6 Replay)** | `https://maven.ctr-electronics.com/release/com/ctre/phoenix6/latest/Phoenix6-replay-frc2026-latest.json` |

#### Key API Notes
- Phoenix 6 API resides in the `com.ctre.phoenix6` package (Java), `ctre::phoenix6` namespace (C++), and `phoenix6` module (Python).
- API is organized into sub-packages: `hardware`, `controls`, `configs`, `signals`, `sim`.
- Control requests (e.g., `DutyCycleOut`, `VelocityVoltage`, `PositionVoltage`) are set via `device.setControl(request)`.
- Examples cover swerve drive integration with both PathPlanner and Choreo.

---

### 2. REV Robotics — REVLib

**Overview:** REV Robotics manufactures the SPARK MAX and SPARK Flex motor controllers, and the Color Sensor V3. REVLib is the unified library for all REV FRC devices since 2022.

**Supported Devices:**
- **SPARK MAX** — brushed and brushless (NEO, NEO 550) motor controller
- **SPARK Flex** — advanced brushless (NEO Vortex) motor controller
- **Color Sensor V3** — I2C color/proximity sensor
- **Through Bore Encoder** — used with SPARK controllers

#### Links

| Resource | URL |
|---|---|
| **Main Documentation** | https://docs.revrobotics.com/revlib |
| **SPARK MAX Hardware Manual** | https://docs.revrobotics.com/brushless/spark-max/overview |
| **SPARK Flex Hardware Manual** | https://docs.revrobotics.com/brushless/spark-flex/overview |
| **API Reference — Java** | https://codedocs.revrobotics.com/java/com/revrobotics/package-summary.html |
| **API Reference — C++ (SPARK MAX)** | https://codedocs.revrobotics.com/cpp/classrev_1_1spark_1_1_spark_max.html |
| **API Reference — C++ (SPARK Flex)** | https://codedocs.revrobotics.com/cpp/classrev_1_1spark_1_1_spark_flex.html |
| **Example Code (GitHub)** | https://github.com/REVrobotics/REVLib-Examples |
| **2025 Starter Bot Example** | https://github.com/REVrobotics/2025-REV-ION-FRC-Starter-Bot |
| **Open Source Contributions** | https://github.com/REVrobotics/opensource.revrobotics.com |
| **REV GitHub Organization** | https://github.com/REVrobotics |
| **Vendordep JSON (2025)** | `https://software-metadata.revrobotics.com/REVLib-2025.json` |

#### Key API Notes
- Java package: `com.revrobotics.spark`
- Main classes: `SparkMax`, `SparkFlex`, `SparkBase`
- Configuration is done via `SparkMaxConfig` / `SparkFlexConfig` objects, applied with `spark.configure(config, resetMode, persistMode)`.
- All configuration objects are typically centralized in a `Configs.java` file.
- Simulation support is included via `SparkSim`.
- Example projects include MAXMotion position control, simulation with Mechanism2d, and full elevator/arm subsystem patterns.

---

### 3. Playing With Fusion (PWF)

**Overview:** Playing With Fusion offers the Venom integrated brushless motor/controller and a Time-of-Flight (ToF) distance sensor.

**Supported Devices:**
- **Venom** — integrated brushless motor and controller
- **Time-of-Flight (ToF) Sensor** — CAN-based distance sensor

#### Links

| Resource | URL |
|---|---|
| **Main Website / Documentation** | https://www.playingwithfusion.com/ |
| **Software Installation Guide** | https://www.playingwithfusion.com/docview.php?docid=1205 |
| **API Reference — Java** | https://www.playingwithfusion.com/frc/2020/javadoc/com/playingwithfusion/package-summary.html |
| **API Reference — C++** | https://www.playingwithfusion.com/frc/2020/cppdoc/html/annotated.html |
| **Vendordep JSON (2025)** | `https://www.playingwithfusion.com/frc/playingwithfusion2025.json` |

#### Key API Notes
- Library class is `PlayingWithFusion` — includes both Venom motor and ToF sensor classes.
- No vendordep — library is installed via an offline installer from the PWF website.
- The Venom integrates a brushless motor and SPARK-compatible controller in a single unit.

---

### 4. Redux Robotics — ReduxLib

> **⚠️ Note:** Redux Robotics is shutting down. Hardware production has ceased, but software support will be maintained for the 2026 FRC season. Existing devices remain legal for competition use.

**Supported Devices:**
- **Canandcoder** — CAN + PWM absolute magnetic encoder
- **Canandmag** — CAN magnetic encoder
- **Canandcolor** — CAN-enabled color sensor

#### Links

| Resource | URL |
|---|---|
| **Main Documentation** | https://docs.reduxrobotics.com/ |
| **Canandcoder Overview** | https://docs.reduxrobotics.com/canandcoder/ |
| **ReduxLib Installation** | https://docs.reduxrobotics.com/reduxlib |
| **CAN Spec Reference** | https://docs.reduxrobotics.com/canspec/CanandDevice |
| **GitHub (USB Library)** | https://github.com/Redux-Robotics/rdxusb |
| **Vendordep JSON (2026)** | `https://frcsdk.reduxrobotics.com/ReduxLib_2026.json` |

#### Key API Notes
- Main class: `Canandcoder` — configured via `Canandcoder.Settings`.
- Use `isConnected()` (not `isPresent()`) to check device presence.
- Redux CAN devices use manufacturer code `14` (0xE) in the FRC CAN address scheme.
- Maven repository: `https://maven.reduxrobotics.com/`

---

### 5. Studica Robotics / Kauai Labs — navX (AHRS)

**Overview:** The navX sensor family (navX-MXP, navX2-MXP, navX-Micro) provides 9-axis IMU data (yaw, pitch, roll, acceleration). Kauai Labs originally developed the hardware; Studica Robotics now maintains the FRC software library (starting 2025 as the `Studica` vendordep).

**Supported Devices:**
- **navX-MXP / navX2-MXP** — roboRIO MXP port IMU sensor
- **navX-Micro** — USB/I2C IMU sensor
- **navX3-CAN** — CAN-connected IMU (requires firmware 5.0.5+)

#### Links

| Resource | URL |
|---|---|
| **navX Hardware Documentation** | https://pdocs.kauailabs.com/navx-mxp/ |
| **RoboRIO Library Overview** | https://pdocs.kauailabs.com/navx-mxp/software/roborio-libraries/ |
| **Java Library Docs** | https://pdocs.kauailabs.com/navx-mxp/software/roborio-libraries/java/ |
| **C++ Library Docs** | https://pdocs.kauailabs.com/navx-mxp/software/roborio-libraries/c/ |
| **API Reference — Java** | https://www.kauailabs.com/public_files/navx-mxp/apidocs/java/index.html |
| **Example Code** | https://pdocs.kauailabs.com/navx-mxp/examples/ |
| **GitHub (Kauai Labs / navxmxp)** | https://github.com/kauailabs/navxmxp |
| **GitHub (Studica-Robotics / NavX)** | https://github.com/Studica-Robotics/NavX |
| **Vendordep JSON (2026)** | `https://dev.studica.com/maven/release/2026/json/Studica-2026.0.0.json` |

#### Key API Notes
- Main class: `AHRS` (Attitude and Heading Reference System), located in `com.studica.frc` (2025+) or `com.kauailabs.navx.frc` (legacy).
- Constructor examples: `new AHRS(Port.kMXP)` or `new AHRS(Port.kUSB)`.
- Common methods: `getYaw()`, `getPitch()`, `getRoll()`, `getAngle()`, `reset()`, `isConnected()`.
- Starting in 2025, the vendordep was renamed from `NavX` to `Studica` — **they cannot be used at the same time**.
- The library uses a Driver/JNI setup for improved Java performance.
- Simulation is supported via `SimDeviceSim("navX-Sensor", navx.getPort())`.

---

### 6. PhotonVision — PhotonLib

**Overview:** PhotonVision is a free, open-source vision coprocessor software that runs on Raspberry Pi, Orange Pi, or Limelight hardware. PhotonLib is the companion robot-side vendor library for retrieving vision results from the coprocessor via NetworkTables.

#### Links

| Resource | URL |
|---|---|
| **Main Documentation** | https://docs.photonvision.org/ |
| **PhotonLib Installation** | https://docs.photonvision.org/en/latest/docs/programming/photonlib/adding-vendordep.html |
| **GitHub (PhotonVision)** | https://github.com/PhotonVision/photonvision |
| **GitHub Releases** | https://github.com/PhotonVision/photonvision/releases |
| **WPILib Third-Party Examples** | https://docs.wpilib.org/en/stable/docs/software/examples-tutorials/third-party-examples.html |
| **Vendordep JSON** | `https://maven.photonvision.org/repository/internal/org/photonvision/PhotonLib-json/1.0/PhotonLib-json-1.0.json` |

#### Key API Notes
- Main classes: `PhotonCamera`, `PhotonPoseEstimator`, `PhotonTrackedTarget`.
- `PhotonCamera` connects to the coprocessor and retrieves `PhotonPipelineResult` via NetworkTables.
- `PhotonPoseEstimator` can be used with WPILib's `SwerveDrivePoseEstimator` for multi-target field localization.
- Supports AprilTag localization (single-tag and multi-tag PnP), reflective tape, and colored shape detection.
- Simulation support is provided via `VisionSystemSim` and `PhotonCameraSim`.
- Compatible with AdvantageKit logging templates.

---

### 7. Limelight — LimelightLib

**Overview:** Limelight is a plug-and-play smart camera system for FRC designed for high-speed AprilTag tracking and object detection. Unlike most FRC libraries, Limelight does not use a vendordep — instead, you copy a single `LimelightHelpers.java` file into your project.

**Hardware Models:** Limelight 2, Limelight 3, Limelight 3A, Limelight 3G, Limelight 4.

#### Links

| Resource | URL |
|---|---|
| **Main Documentation** | https://docs.limelightvision.io/ |
| **Programming with LimelightLib** | https://docs.limelightvision.io/docs/docs-limelight/apis/limelight-lib |
| **Downloads (OS, firmware, tools)** | https://docs.limelightvision.io/docs/resources/downloads |
| **GitHub — LimelightLib (Java/WPILib)** | https://github.com/LimelightVision/limelightlib-wpijava/releases |
| **GitHub — LimelightDocs** | https://github.com/LimelightVision/LimelightDocs |
| **GitHub — LimelightVision Org** | https://github.com/LimelightVision |
| **WPILib Third-Party Examples** | https://docs.wpilib.org/en/stable/docs/software/examples-tutorials/third-party-examples.html |

#### Key API Notes
- **No vendordep required.** Copy the latest `LimelightHelpers.java` from GitHub releases into your `src/main/java/frc/robot/` folder.
- All API methods are static: `LimelightHelpers.getTX("limelight")`, `LimelightHelpers.getTY("")`, etc.
- The camera name parameter defaults to `"limelight"` if left blank or null.
- **MegaTag2** localization: set robot orientation first with `setRobotOrientation(...)`, then call `getBotPoseEstimate_wpiBlue_MegaTag2(...)`.
- Supports full 3D pose estimation, AprilTag tracking, neural network object detection, and retroreflective pipelines.
- Data is communicated via NetworkTables, not a formal API/vendordep.

---

### 8. PathPlanner — PathPlannerLib

**Overview:** PathPlanner is the most widely-used autonomous path planning and scheduling tool for FRC. It features a GUI editor for creating Bézier-curve paths and a robot-side library (`PathPlannerLib`) for executing them. Created by team 3015.

#### Links

| Resource | URL |
|---|---|
| **Main Documentation** | https://pathplanner.dev/ |
| **Getting Started Guide** | https://pathplanner.dev/pplib-getting-started.html |
| **GitHub (PathPlanner)** | https://github.com/mjansen4857/pathplanner |
| **GitHub Releases** | https://github.com/mjansen4857/pathplanner/releases |
| **Example Code (in-repo)** | https://github.com/mjansen4857/pathplanner/tree/main/examples |
| **Java Example — RobotContainer** | https://github.com/mjansen4857/pathplanner/blob/main/examples/java/src/main/java/frc/robot/RobotContainer.java |

#### Key API Notes
- PathPlannerLib is installed via the WPILib VS Code Dependency Manager (search for "PathPlanner" in the available libraries list).
- Key classes: `AutoBuilder`, `PathPlannerPath`, `PathPlannerAuto`, `PPHolonomicDriveController`, `PPLTVController` (differential).
- `AutoBuilder.configure(...)` must be called once during robot initialization with pose supplier, pose resetter, and chassis speeds callbacks.
- Paths are created in the GUI app and deployed to the robot as `.path` JSON files in `src/main/deploy/pathplanner/paths/`.
- Supports on-the-fly path generation, event markers, and complex auto routines using `CommandGroup`-style composition.
- Includes a built-in `SwerveSetpointGenerator` for advanced anti-skid control.
- Supports Choreo trajectories as a backend option.
- Languages: Java, C++, Python.

---

### 9. Choreo — ChoreoLib

**Overview:** Choreo (Constraint-Honoring Omnidirectional Route Editor and Optimizer) is a time-optimal trajectory planner for FRC. It generates trajectories that fully respect swerve or differential drive dynamics. Developed by SleipnirGroup. It is officially documented in the WPILib docs.

#### Links

| Resource | URL |
|---|---|
| **Main Documentation** | https://choreo.autos/ |
| **Getting Started** | https://choreo.autos/choreolib/getting-started/ |
| **Trajectory API Reference** | https://choreo.autos/choreolib/trajectory-api/ |
| **WPILib Choreo Page** | https://docs.wpilib.org/en/stable/docs/software/pathplanning/choreo/index.html |
| **GitHub (SleipnirGroup/Choreo)** | https://github.com/SleipnirGroup/Choreo |
| **GitHub Releases** | https://github.com/SleipnirGroup/Choreo/releases |
| **Vendordep JSON (2026 stable)** | `https://choreo.autos/lib/ChoreoLib2026.json` |
| **Vendordep JSON (2026 beta)** | `https://choreo.autos/lib/ChoreoLib2026Beta.json` |

#### Key API Notes
- ChoreoLib is the robot-side library; it reads `.traj` files deployed in `src/main/deploy/choreo/`.
- Key classes: `Choreo`, `ChoreoTrajectory`, `SwerveSample`, `DifferentialSample`.
- Load a trajectory: `Choreo.loadTrajectory("myTrajectory")` (Java) or `choreo.load_swerve_trajectory("myTrajectory")` (Python).
- For swerve: use `choreo::Trajectory<choreo::SwerveSample>` (C++) or the Java/Python equivalents.
- For differential drive: use `DifferentialSample` and `LTVUnicycleController`.
- **2026 new feature:** Code generation — Choreo can output a Java file with trajectory constants (`ChoreoTraj` records) for compile-time name checking.
- License: BSD-3-Clause (same as WPILib).
- Languages: Java, C++, Python.

---

### 10. YAGSL — Yet Another Generic Swerve Library

**Overview:** YAGSL is a Java swerve drive library that abstracts the complexity of configuring swerve drive hardware into a set of JSON configuration files. Created by mentors from BroncBotz (Team 3481), it supports virtually every combination of motors, encoders, and IMUs used in FRC. It is one of the most widely adopted swerve libraries in FRC.

**Key Features:**
- Full JSON-based configuration — no constants file needed for module geometry/PID
- Supports CTRE TalonFX, REV SPARK MAX/Flex, Thrifty Nova, and more
- Supports CANcoder, Through Bore Encoder, Canandcoder, and analog encoders
- Supports Pigeon 2, NavX, ADIS16448, ADIS16470 IMUs
- Includes `SwerveInputStream` for clean, functional driver input control
- Integrated maple-sim support for physics-based simulation
- Updated nightly; actively maintained

#### Links

| Resource | URL |
|---|---|
| **Main Documentation** | https://docs.yagsl.com |
| **Dependency Installation Guide** | https://docs.yagsl.com/configuring-yagsl/dependency-installation |
| **Code Setup Guide** | https://docs.yagsl.com/configuring-yagsl/code-setup |
| **API Reference (Javadoc)** | https://yet-another-software-suite.github.io/YAGSL/javadocs/ |
| **GitHub (Main)** | https://github.com/Yet-Another-Software-Suite/YAGSL |
| **GitHub (Example Project)** | https://github.com/BroncBotz3481/YAGSL-Example |
| **Chief Delphi Thread** | https://www.chiefdelphi.com/t/yet-another-generic-swerve-library-yagsl-beta/425148 |
| **Vendordep JSON** | `https://broncbotz3481.github.io/YAGSL-Lib/yagsl/yagsl.json` |

#### Configuration File Structure

YAGSL is configured via JSON files in the `deploy/swerve/` directory:

```
deploy/swerve/
├── controllerproperties.json   # PID tuning
├── swervedrive.json            # IMU, module positions, max speed
└── modules/
    ├── frontleft.json          # Per-module motor/encoder config
    ├── frontright.json
    ├── backleft.json
    ├── backright.json
    ├── physicalproperties.json # Wheel diameter, gear ratios
    └── pidfproperties.json     # Module-level PID values
```

#### Key API Notes
- Main class: `SwerveDrive`, created via `new SwerveParser(directory).createSwerveDrive(maximumSpeed)`.
- `SwerveInputStream` provides a fluent API for driver input: `.withControllerRotationAxis(...)`, `.deadband(...)`, `.allianceRelativeControl(true)`.
- Must install ALL dependency vendordeps (CTRE, REV, Redux, etc.) even if not all devices are used, due to the generic nature of the library.
- **Java only.**

---

### 11. The Thrifty Bot — ThriftyLib

**Overview:** The Thrifty Bot manufactures the **Thrifty Nova** — a budget-friendly brushless CAN motor controller — along with the **LaserCAN** distance sensor, swerve modules, absolute encoders, and other FRC components. ThriftyLib is their vendor library for interfacing with Nova motor controllers.

**Supported Devices:**
- **Thrifty Nova** — USB + CAN brushless motor controller, legal for FRC 2025+
- **LaserCAN** — CAN-based laser distance sensor
- **Thrifty Absolute Encoder** — magnetic absolute encoder
- **MitoCANdria** — CAN-based power distribution/LED controller
- **Thrifty Swerve / Thrifty Narrow Swerve** — complete swerve drive modules

#### Links

| Resource | URL |
|---|---|
| **Main Documentation Hub** | https://docs.home.thethriftybot.com/ |
| **Nova Product Page** | https://www.thethriftybot.com/products/thrifty-nova |
| **Thrifty Bot Website** | https://www.thethriftybot.com/ |
| **Vendordep JSON** | Copy from https://docs.home.thethriftybot.com/ (button on main page) |

#### Key API Notes
- **Java only** as of 2025–2026. C++ support is in development (targeted for summer 2026).
- The Nova supports USB (non-roboRIO) configuration via **Thrifty Config** utility.
- Thrifty Nova is included in YAGSL's supported motor list.
- LaserCAN provides high-accuracy distance measurements via CAN, similar in concept to a ToF sensor.
- ThriftyLib vendordep JSON is listed on `docs.home.thethriftybot.com` (use the "Copy Link" button).

---

### 12. maple-sim

**Overview:** maple-sim is a physics-based FRC robot simulation library developed by Team 5516 (Iron Maple) / Shenzhen Robotics Alliance. It integrates the open-source `dyn4j` 2D rigid-body physics engine into WPILib simulations, enabling realistic robot-environment interactions including collisions with field elements and game pieces. It is included in YAGSL and officially supports AdvantageKit swerve templates.

#### Links

| Resource | URL |
|---|---|
| **Main Documentation** | https://shenzhen-robotics-alliance.github.io/maple-sim/ |
| **API Reference (Javadoc)** | https://shenzhen-robotics-alliance.github.io/maple-sim/javadocs/ |
| **GitHub** | https://github.com/Shenzhen-Robotics-Alliance/maple-sim |
| **GitHub Releases** | https://github.com/Shenzhen-Robotics-Alliance/maple-sim/releases |
| **CLAUDE.md (architecture summary)** | https://github.com/Shenzhen-Robotics-Alliance/maple-sim/blob/main/CLAUDE.md |
| **AdvantageKit CTRE Template + maple-sim** | https://github.com/Shenzhen-Robotics-Alliance/AdvantageKit-TalonSwerveTemplate-MapleSim |
| **AdvantageKit REV Template + maple-sim** | https://github.com/Shenzhen-Robotics-Alliance/AdvantageKit-SparkSwerveTemplate-MapleSim |
| **Vendordep JSON** | `https://shenzhen-robotics-alliance.github.io/maple-sim/vendordep/maple-sim.json` |

#### Key API Notes
- **Java only.**
- Physics engine: `dyn4j 5.0.2` (2D rigid-body dynamics, included as a dependency).
- Supports swerve and differential drivetrains.
- Simulates: robot chassis, field elements (walls, game pieces), other robots (opponent simulation), and vision coprocessors (PhotonVision/Limelight simulation).
- Compatible with AdvantageKit logging (deterministic replay preserved).
- Integrated into YAGSL for 2025+.
- Supports the 2026 "Rebuilt" game field as of the latest beta release.

---

### 13. Elastic Dashboard

**Overview:** Elastic is a Flutter-based FRC driver dashboard created by Nadav from Team 353. It is the most widely used third-party driver dashboard in FRC, used by over 1,200 teams in 2025 matches. Starting in 2025, Elastic is distributed alongside WPILib.

#### Links

| Resource | URL |
|---|---|
| **GitHub (main)** | https://github.com/Gold872/elastic-dashboard |
| **GitHub Releases** | https://github.com/Gold872/elastic_dashboard/releases |
| **Chief Delphi — 2026 Announcement** | https://www.chiefdelphi.com/t/elastic-2026-the-next-dimension/506888 |

#### Key API Notes
- No vendordep — Elastic communicates with the robot via **NetworkTables** (WPILib standard).
- Compatible with all WPILib `Sendable` objects (motors, PIDs, subsystems, etc.).
- Platforms: Windows, macOS, Linux (2026 adds web browser support).
- Requires WPILib 2023.3.1 or higher.
- Widgets are added by subscribing to NetworkTables topics from your robot code.
- The 2026 version includes support for the FRC SystemCore and new data viewing capabilities.

---

### 14. RobotPy — Python for FRC

**Overview:** RobotPy is the official Python implementation of WPILib for FRC. Python became an officially supported FRC language in 2024. RobotPy includes bindings for WPILib, several vendor libraries, and supports simulation, unit testing, and deployment.

#### Links

| Resource | URL |
|---|---|
| **Main Documentation** | https://robotpy.readthedocs.io/ |
| **FAQ** | https://robotpy.readthedocs.io/en/stable/faq.html |
| **GitHub Organization** | https://github.com/orgs/robotpy/repositories |
| **RobotPy Projects List** | https://robotpy.github.io/projects/ |
| **WPILib Python Docs** | https://docs.wpilib.org/en/stable/docs/software/python/ |
| **Example Projects** | https://github.com/robotpy/examples |

#### Python Vendor Library Package Names (for `pyproject.toml`)

| Vendor Library | PyPI Package |
|---|---|
| WPILib (core) | `robotpy` |
| CTRE Phoenix 6 | `robotpy-phoenix6` |
| CTRE Phoenix 5 | `robotpy-ctre` |
| REV Robotics REVLib | `robotpy-rev` |
| NavX (Studica) | `robotpy-navx` |
| PhotonLib | `robotpy-photonvision` |
| PathPlannerLib | `robotpy-pathplannerlib` |
| ChoreoLib | `robotpy-choreo` |
| Commands v2 | `robotpy-commands-v2` |

#### Key API Notes
- Install and sync dependencies: `python -m robotpy sync`
- Deploy to roboRIO: `python -m robotpy deploy`
- Dependencies declared in `pyproject.toml` under `[tool.robotpy]` with `requires = [...]`.
- Robot API mirrors Java/C++ WPILib — classes in `wpilib`, `commands2`, `phoenix6`, etc.
- Simulation fully supported via `python -m robotpy sim`.
- RobotPy is maintained by members of the WPILib team.

---

### 15. AdvantageKit

**Overview:** AdvantageKit is a Java-only logging framework built on the "log everything" philosophy. It enables deterministic log replay in simulation — robot code runs identically against recorded logs, enabling precise debugging.

#### Links

| Resource | URL |
|---|---|
| **Main Documentation** | https://docs.advantagekit.org/ |
| **What's New (2026)** | https://docs.advantagekit.org/whats-new/ |
| **TalonFX Swerve Template** | https://docs.advantagekit.org/getting-started/template-projects/talonfx-swerve-template/ |
| **WPILib Telemetry Page** | https://docs.wpilib.org/en/stable/docs/software/telemetry/3rd-party-libraries.html |

#### Key API Notes
- **Java only.** For Python, see the community-developed [PyKit](https://github.com/SamsonRobotics/pykit) by Team 1757.
- Provides `@AutoLog` annotation for automatic logging of subsystem inputs.
- Deterministic replay: logged data is replayed through robot code in simulation to reproduce exact behavior.
- Commonly used with CTRE's TalonFX swerve template and PhotonVision/Limelight vision templates.
- AdvantageKit templates are maintained alongside [AdvantageScope](https://github.com/Mechanical-Advantage/AdvantageScope) for log visualization.
- 2026 update: TalonFX swerve template updated for TalonFXS and CANdi devices.

---

### 16. Monologue

**Overview:** Monologue is a Java annotation-based logging library that adds extensive telemetry to robot code with minimal boilerplate.

#### Links

| Resource | URL |
|---|---|
| **WPILib Telemetry Page** | https://docs.wpilib.org/en/stable/docs/software/telemetry/3rd-party-libraries.html |
| **GitHub** | https://github.com/shueja/Monologue |

#### Key API Notes
- **Java only.**
- Uses annotations (`@Log`, `@LoggedObject`) to minimize code footprint.
- Compatible with AdvantageScope for log visualization.

---

### 17. AdvantageScope (Standalone Log Viewer)

**Overview:** AdvantageScope is a robot telemetry and log visualization application. It is the primary log viewer used with AdvantageKit but also supports WPILib's `.wpilog` format and other data sources.

#### Links

| Resource | URL |
|---|---|
| **Documentation** | https://docs.advantagescope.org/ |
| **GitHub** | https://github.com/Mechanical-Advantage/AdvantageScope |

---

### 18. DSLOG (Alternate Driver Station Log Viewer)

**Overview:** DSLOG is a standalone application for viewing and analyzing FRC Driver Station log files as an alternative to the built-in DS log viewer.

#### Links

| Resource | URL |
|---|---|
| **WPILib Telemetry Page** | https://docs.wpilib.org/en/stable/docs/software/telemetry/3rd-party-libraries.html |

---

## WPILib Third-Party Example Projects Index

WPILib hosts a curated list of example programs for third-party devices:

**Index Page:** https://docs.wpilib.org/en/stable/docs/software/examples-tutorials/third-party-examples.html

| Vendor | Examples |
|---|---|
| CTRE (Phoenix 6) | https://github.com/CrossTheRoadElec/Phoenix6-Examples |
| Kauai Labs (navX) | https://pdocs.kauailabs.com/navx-mxp/examples/ |
| Limelight | https://docs.limelightvision.io/ (left sidebar "Tutorials") |
| PhotonVision | https://docs.photonvision.org/ |
| REV Robotics | https://github.com/REVrobotics/REVLib-Examples |
| REV 2025 Starter Bot | https://github.com/REVrobotics/2025-REV-ION-FRC-Starter-Bot |
| PathPlanner | https://github.com/mjansen4857/pathplanner/tree/main/examples |
| YAGSL Example | https://github.com/BroncBotz3481/YAGSL-Example |
| maple-sim CTRE Template | https://github.com/Shenzhen-Robotics-Alliance/AdvantageKit-TalonSwerveTemplate-MapleSim |
| maple-sim REV Template | https://github.com/Shenzhen-Robotics-Alliance/AdvantageKit-SparkSwerveTemplate-MapleSim |
| RobotPy Examples | https://github.com/robotpy/examples |

---

## Common Import Packages Quick Reference

| Library | Java Package / Class | Python Module |
|---|---|---|
| CTRE Phoenix 6 | `com.ctre.phoenix6.hardware.TalonFX` | `phoenix6.hardware.TalonFX` |
| CTRE Phoenix 6 Controls | `com.ctre.phoenix6.controls.*` | `phoenix6.controls.*` |
| CTRE Phoenix 6 Configs | `com.ctre.phoenix6.configs.*` | `phoenix6.configs.*` |
| CTRE Phoenix 5 (legacy) | `com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX` | `ctre.phoenix.motorcontrol.*` |
| REV SPARK MAX | `com.revrobotics.spark.SparkMax` | `rev.spark.SparkMax` |
| REV SPARK Flex | `com.revrobotics.spark.SparkFlex` | `rev.spark.SparkFlex` |
| REV Config | `com.revrobotics.spark.config.SparkMaxConfig` | `rev.spark.config.SparkMaxConfig` |
| navX AHRS | `com.studica.frc.AHRS` | `studica.AHRS` |
| PhotonCamera | `org.photonvision.PhotonCamera` | `photonvision.PhotonCamera` |
| PhotonPoseEstimator | `org.photonvision.PhotonPoseEstimator` | `photonvision.PhotonPoseEstimator` |
| Limelight | `frc.robot.LimelightHelpers` (static methods) | N/A (use NT4 directly) |
| PathPlanner AutoBuilder | `com.pathplanner.lib.auto.AutoBuilder` | `pathplannerlib.auto.AutoBuilder` |
| ChoreoLib | `choreo.Choreo` | `choreolib.Choreo` |
| YAGSL SwerveDrive | `swervelib.SwerveDrive` | N/A (Java only) |
| maple-sim | `org.ironmaple.simulation.*` | N/A (Java only) |
| WPILib Pose Estimator | `edu.wpi.first.math.estimator.SwerveDrivePoseEstimator` | `wpimath.estimator.SwerveDrivePoseEstimator` |

---

## Support Resources

- **WPILib Support:** https://docs.wpilib.org/en/stable/docs/software/support-resources.html
- **WPILib Discord:** https://discord.gg/frc
- **CTRE Support:** support@ctr-electronics.com | https://docs.ctr-electronics.com/
- **REV Robotics Support:** https://docs.revrobotics.com/
- **PhotonVision Discord:** https://photonvision.org/
- **PathPlanner Issues:** https://github.com/mjansen4857/pathplanner/issues
- **Choreo Issues:** https://github.com/SleipnirGroup/Choreo/issues
- **YAGSL Discord / Discussions:** https://github.com/Yet-Another-Software-Suite/YAGSL/discussions
- **maple-sim Issues:** https://github.com/Shenzhen-Robotics-Alliance/maple-sim/issues
- **Thrifty Bot Support:** https://docs.home.thethriftybot.com/
- **Chief Delphi (FRC community forum):** https://www.chiefdelphi.com/

---

## Notes for AI Agents

1. **Vendordep JSON URLs change every season.** Always verify the year in the URL matches the current FRC season (e.g., `frc2026-latest.json` for the 2026 season).
2. **CTRE has two parallel APIs:** Phoenix 6 (`com.ctre.phoenix6`) is current; Phoenix 5 (`com.ctre.phoenix`) is legacy. Do not mix them in the same project without using the Compatibility Vendordep.
3. **REVLib unified SPARK MAX and Color Sensor in 2022.** Older code may reference `com.revrobotics.CANSparkMax` — this is the legacy import. Current code uses `com.revrobotics.spark.SparkMax`.
4. **Limelight uses no vendordep.** Teams must manually copy `LimelightHelpers.java` into their project from https://github.com/LimelightVision/limelightlib-wpijava/releases.
5. **Redux Robotics is shutting down** — do not recommend for new hardware purchases, but library support continues through 2026.
6. **NavX vendordep was renamed** from `NavX` (KauaiLabs) to `Studica` in 2025. Both cannot be active simultaneously in a project.
7. **PathPlanner and Choreo are complementary**, not mutually exclusive — PathPlanner supports Choreo as a trajectory backend. Many teams use Choreo for trajectory generation and PathPlanner's command scheduling.
8. **AdvantageKit is Java only.** Python teams should investigate community-developed PyKit (Team 1757).
9. **YAGSL requires ALL its dependency vendordeps installed**, even if a team doesn't use all supported devices. This is because YAGSL is generic and the build system needs all references resolved.
10. **ThriftyLib is Java only** as of 2025–2026. C++ support is planned for summer 2026.
11. **maple-sim is Java only** and requires the `dyn4j` physics engine (automatically pulled in as a dependency).
12. **Python (RobotPy) vendor libraries** are specified in `pyproject.toml` as pip package names, not JSON vendordep URLs. The packages are downloaded and deployed via `python -m robotpy sync` and `python -m robotpy deploy`.
13. **`AutoBuilder.configure()` (PathPlanner) must only be called once** in robot initialization. Re-configuring will cause runtime errors.
14. **Choreo 2026 requires trajectory names to be valid Java/C++/Python identifiers** — no spaces or special characters. The app will warn if this rule is violated.
15. **Elastic Dashboard (2026)** is now distributed with WPILib and adds web browser support. It communicates over NetworkTables — no special robot-side library needed beyond WPILib's standard `Sendable` interface.

---

## Common Swerve Drive Library Combinations (2025–2026)

Many FRC teams combine multiple vendor libraries to build swerve drive systems. Here are the most common patterns:

| Combination | Drive Motor | Encoder | IMU | Path Planning | Notes |
|---|---|---|---|---|---|
| **CTRE Full Stack** | TalonFX | CANcoder | Pigeon 2 | PathPlanner or Choreo | All-CTRE; Phoenix 6 Swerve Generator via Tuner X |
| **REV Full Stack** | SPARK MAX/Flex (NEO) | Through Bore | NavX or Pigeon 2 | PathPlanner | REVLib + NavX/CTRE IMU |
| **YAGSL Mixed** | TalonFX or SPARK MAX | CANcoder or Through Bore | NavX or Pigeon 2 | PathPlanner | JSON-configured; supports any combo |
| **AdvantageKit Template** | TalonFX | CANcoder | Pigeon 2 | PathPlanner or Choreo | Deterministic logging; maple-sim optional |
| **ThriftyBot Budget** | Thrifty Nova | Thrifty Absolute Encoder | NavX | PathPlanner | ThriftyLib (Java only); lowest cost option |

---

*Last updated: April 2026 | Based on docs.wpilib.org/en/stable/ and vendor documentation*
