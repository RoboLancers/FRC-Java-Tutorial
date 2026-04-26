# Test Suite Debugging Notes

## SparkMax initialization in tests

### Root cause
REV's `SparkLowLevel` has a static initializer that loads a native CAN driver library. It only
runs when the class is *initialized* (e.g., `new SparkMax(...)`), not when it's merely *loaded*
(e.g., `SparkMax.class` literal). This distinction is critical:

- Unit 1 tests use `f.getType().equals(SparkMax.class)` → class *loaded* only, static init never
  runs → tests pass without native libs
- Unit 2+ tests call `new SparkMax(...)` → class *initialized* → static init runs → needs native
  libs on `java.library.path`

### Fix: `includeDesktopSupport = true` in build.gradle
GradleRIO only adds vendor JNI libs (REVLib-driver-linuxx86-64) to `java.library.path` for test
tasks when `includeDesktopSupport = true`. With `false`, `new SparkMax(...)` throws:
```
ExceptionInInitializerError
  caused by: RuntimeException
    caused by: IOException     ← REV's static init can't find/load native lib
```

After changing to `true`, run `./gradlew clean test` — the cache must be cleared for the new
java.library.path to take effect.

### VS Code test runner limitation
The VS Code Java Test Runner does NOT use GradleRIO's native lib path setup. SparkMax
instantiation will always fail when running tests via the IDE run button. HAL simulation tests
(Unit 2 constructor, Unit 3 runtime, Unit 4 default command) require `./gradlew test`.

## Test resilience patterns applied

### `catch (Throwable e)` not `catch (Exception e)`
`ExceptionInInitializerError` and `NoClassDefFoundError` are `Error` subclasses, not `Exception`.
All `@BeforeAll` blocks that probe `new CANDriveSubsystem()` must catch `Throwable` or they
re-throw as `initializationError`, crashing the entire test class rather than skipping tests.

Fixed in: Unit3FollowerAndDriveTest, Unit4RobotContainerTest.

### Probe pattern in `@BeforeAll` (Unit 2)
```java
try {
    new CANDriveSubsystem();
} catch (UnsupportedOperationException e) {
    // template placeholder throw — SparkMax loaded fine, student hasn't finished yet
} catch (Throwable e) {
    sparkMaxUnavailable = true;   // native libs missing → skip HAL tests gracefully
}
```
`UnsupportedOperationException` must be caught *first* so it isn't absorbed into the
`sparkMaxUnavailable` path. When Unit 2 is complete (placeholder throw removed), only the native
lib path matters.

## Unit detection logic

| Unit | Detection check | Skips when |
|------|----------------|------------|
| 1 | `leftLeader` field exists | field missing |
| 2 | `LEFT_LEADER_ID` constant exists | constant missing |
| 3 | `drive` field AND `driveArcade` method exist | either missing |
| 4 | `driveSubsystem` field exists in `RobotContainer` | field missing |

Unit 3 detection catches `NoSuchFieldException | NoSuchMethodException`. Since `driveArcade` stub
exists from the start, detection is effectively gated on the `drive` field alone. All Unit 3
tests (including pure reflection tests) skip until the `drive` field is declared.

## HAL initialization ordering
Test classes run alphabetically (Unit1 → Unit2 → Unit3 → Unit4). Unit 2's `@BeforeAll` asserts
`assertTrue(HAL.initialize(500, 0))` — this works because Unit 2 always runs first to initialize
HAL. If test class ordering ever changes, this assertion will fail for classes that run after
HAL has already been initialized.
