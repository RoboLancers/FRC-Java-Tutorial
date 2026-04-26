# Unit Test Plan: github-classroom-drivetrain-talon

## Context

The `github-classroom-drivetrain-talon/template/` folder is a 4-unit GitHub Classroom assignment where students build a TalonFX differential drivetrain subsystem incrementally. This plan adds JUnit 5 tests that auto-grade each unit. Students can also run them locally via `./gradlew test` to self-check progress.

---

## Approach: Two-Layer Tests

- **Reflection-based tests** (no hardware/HAL needed): verify field names, types, modifiers, and constant values directly from classfile metadata. Always reliable in CI.
- **HAL simulation behavioral tests**: call `HAL.initialize(500, 0)` once per test class (`@BeforeAll`), then instantiate the subsystem to verify runtime behavior.

TalonFX simulation works headlessly because Phoenix 6's `simTutils` native library is available for `linuxx86-64` (GitHub Actions runners). It is pulled in by `wpi.java.vendor.jniDebug/jniRelease(wpi.platforms.desktop)` in `build.gradle`, and `wpi.java.configureTestTasks(test)` injects it onto `java.library.path` for the test JVM.

---

## Files Created / Modified

### `template/build.gradle` — Full rewrite

The original file used the old `robotConfig` / `apply from: "${TOOLCHAIN_DIR}/..."` style that breaks in CI (`TOOLCHAIN_DIR` is not set by GitHub Actions). Replaced with the standard 2026 GradleRIO structure.

Key additions over the original:

```gradle
repositories {
    maven { url = "https://maven.ctr-electronics.com/release/" }  // required for simTutils
}

dependencies {
    // Desktop JNI — pulls Phoenix 6 simTutils for linuxx86-64
    nativeDebug wpi.java.deps.wpilibJniDebug(wpi.platforms.desktop)
    nativeDebug wpi.java.vendor.jniDebug(wpi.platforms.desktop)
    simulationDebug wpi.sim.enableDebug()
    nativeRelease wpi.java.deps.wpilibJniRelease(wpi.platforms.desktop)
    nativeRelease wpi.java.vendor.jniRelease(wpi.platforms.desktop)
    simulationRelease wpi.sim.enableRelease()

    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.1'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

test {
    useJUnitPlatform()
}

// GUI disabled so tests run headlessly in CI
wpi.sim.addGui().defaultEnabled = false
wpi.sim.addDriverstation()

wpi.java.configureExecutableTasks(jar)
wpi.java.configureTestTasks(test)  // injects JNI lib path into test JVM — mandatory
```

Reference for the complete structure: `improvement_analysis/github-classroom-drivetrain/template/build.gradle`

---

## Test Files

All four files live in `template/src/test/java/frc/robot/`.

### `Unit1FieldDeclarationTest.java` — Pure reflection, no HAL

Verifies `CANDriveSubsystem` field declarations without instantiating the class (safe to run even when the constructor is incomplete).

| Test | What it checks |
|------|---------------|
| `leftLeaderField` | `private final TalonFX leftLeader` exists |
| `leftFollowerField` | `private final TalonFX leftFollower` exists |
| `rightLeaderField` | `private final TalonFX rightLeader` exists |
| `rightFollowerField` | `private final TalonFX rightFollower` exists |
| `driveField` | `private final DifferentialDrive drive` exists |
| `exactlyFourTalonFXFields` | Total TalonFX field count == 4 |
| `exactlyOneDifferentialDriveField` | Total DifferentialDrive field count == 1 |

### `Unit2ConstantsAndInitTest.java` — Reflection + HAL simulation

**Reflection tests** (no HAL): verify `Constants.DriveConstants` has all required constants as `public static final int` with correct values.

| Constant | Expected value |
|----------|---------------|
| `LEFT_LEADER_ID` | 1 |
| `LEFT_FOLLOWER_ID` | 2 |
| `RIGHT_LEADER_ID` | 3 |
| `RIGHT_FOLLOWER_ID` | 4 |
| `DRIVE_MOTOR_CURRENT_LIMIT` | 60 |

**HAL simulation tests** (`@BeforeAll: HAL.initialize(500, 0)`):
- `CANDriveSubsystem::new` does not throw
- `drive` field is non-null after construction (verified via `setAccessible(true)`)

### `Unit3FollowerAndDriveTest.java` — Reflection + HAL simulation

**Reflection test** (no HAL):
- `driveArcade(DoubleSupplier, DoubleSupplier)` is `public`, returns `Command`
- Uses `getDeclaredMethod` with exact parameter types (catches `Supplier<Double>` / `double` mistakes)

**HAL simulation tests** (one `static CANDriveSubsystem subsystem` in `@BeforeAll`):
- `driveArcade(() -> 0.0, () -> 0.0)` returns non-null
- `cmd.getRequirements().contains(subsystem)` — verifies `this.run(...)` was used, not a free-standing command
- `leftFollower` and `rightFollower` fields are non-null

> One subsystem instance shared across all tests in the class to avoid exhausting the HAL simulated CAN device table (each `new TalonFX(id)` registers a device).

### `Unit4RobotContainerTest.java` — Reflection + HAL simulation

**Reflection test** (no HAL):
- `RobotContainer` has `private final CANDriveSubsystem m_driveSubsystem`

**HAL simulation tests**:
- `RobotContainer::new` does not throw (its constructor calls `configureBindings()` automatically)
- After construction, `drive.getDefaultCommand()` is non-null — verifies `setDefaultCommand()` was called

---

## GitHub Classroom Integration

### `.github/classroom/autograding.json`

```json
{
  "tests": [
    { "name": "Unit 1: Field Declarations",           "setup": "chmod +x ./gradlew", "run": "./gradlew test --tests 'frc.robot.Unit1FieldDeclarationTest' --info",  "timeout": 300, "points": 20 },
    { "name": "Unit 2: Constants and Initialization", "setup": "",                   "run": "./gradlew test --tests 'frc.robot.Unit2ConstantsAndInitTest' --info",   "timeout": 300, "points": 25 },
    { "name": "Unit 3: Followers and driveArcade()",  "setup": "",                   "run": "./gradlew test --tests 'frc.robot.Unit3FollowerAndDriveTest' --info",   "timeout": 300, "points": 30 },
    { "name": "Unit 4: RobotContainer Wiring",        "setup": "",                   "run": "./gradlew test --tests 'frc.robot.Unit4RobotContainerTest' --info",    "timeout": 300, "points": 25 }
  ]
}
```

Points: **20 / 25 / 30 / 25** = 100. Unit 3 is weighted highest (most moving parts: followers + inversion + command factory).

### `.github/workflows/classroom.yml`

- Runs on every push/PR
- `actions/setup-java@v4` with Temurin 17
- Gradle + WPILib cache keyed on `vendordeps/**/*.json` (invalidates on Phoenix 6 version bump)
- `DISPLAY: ""` env var suppresses X11 errors in headless CI
- Uploads test HTML report as an artifact

---

## Key Pitfalls

| Pitfall | How the plan avoids it |
|---------|----------------------|
| `UnsatisfiedLinkError` on `TalonFX` construction | `wpi.java.configureTestTasks(test)` sets `java.library.path` for the test JVM |
| `simTutils` not found | CTRE maven repo declared in `repositories {}` |
| HAL crashes if initialized twice | `HAL.initialize` returns `false` (not throws) if called again — safe for `@BeforeAll` across multiple test classes |
| HAL CAN device table exhaustion | One `static CANDriveSubsystem` instance per test class, not one per test method |
| CI fails with missing `TOOLCHAIN_DIR` | Old `apply from: "${TOOLCHAIN_DIR}/..."` replaced with explicit `deploy {}` + `jar {}` block |

---

## Verification Checklist

- [ ] Run tests against **skeleton template** → all tests fail (expected — no student code yet)
- [ ] Fill in all 4 units with correct implementations → all 100 points pass
- [ ] Push to GitHub Actions → JNI loads, HAL initializes, no `UnsatisfiedLinkError`
- [ ] Verify test report at `build/reports/tests/test/index.html`
