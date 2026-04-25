package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANDriveSubsystem;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit 3: Verifies that the {@code drive} field is declared, that the driveArcade command factory
 * method has the correct signature, and that follower configuration and DifferentialDrive
 * initialization work correctly at runtime.
 *
 * <p>The subsystem is created once per test class (static @BeforeAll) to avoid exhausting the
 * HAL's simulated CAN device table — each new SparkMax registers a device.
 */
class Unit3FollowerAndDriveTest {

    // ── Reflection tests: drive field declaration and driveArcade method signature ────────────────

    @Test
    @DisplayName("Unit 3: drive is declared as private final DifferentialDrive")
    void driveFieldDeclared() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("drive");
        assertEquals(
                DifferentialDrive.class, f.getType(), "drive must be of type DifferentialDrive");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "drive must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "drive must be final");
    }

    @Test
    @DisplayName("Unit 3: CANDriveSubsystem has exactly 1 DifferentialDrive field")
    void exactlyOneDifferentialDriveField() {
        Field[] fields = CANDriveSubsystem.class.getDeclaredFields();
        long count =
                Arrays.stream(fields)
                        .filter(f -> f.getType().equals(DifferentialDrive.class))
                        .count();
        assertEquals(
                1,
                count,
                "CANDriveSubsystem must have exactly 1 DifferentialDrive field named 'drive'");
    }

    // ── Reflection test: driveArcade method signature ────────────────────────

    @Test
    @DisplayName("Unit 3: driveArcade(DoubleSupplier, DoubleSupplier) is declared public and returns Command")
    void driveArcadeMethodSignature() throws NoSuchMethodException {
        Method m =
                CANDriveSubsystem.class.getDeclaredMethod(
                        "driveArcade", DoubleSupplier.class, DoubleSupplier.class);
        assertTrue(Modifier.isPublic(m.getModifiers()), "driveArcade must be public");
        assertEquals(Command.class, m.getReturnType(), "driveArcade must return Command");
    }

    // ── HAL simulation tests: runtime behavior ────────────────────────────────

    private static CANDriveSubsystem subsystem;

    @BeforeAll
    static void initHalAndSubsystem() {
        HAL.initialize(500, 0);
        subsystem = new CANDriveSubsystem();
    }

    @Test
    @DisplayName("Unit 3: drive field is non-null after construction")
    void driveFieldIsNonNull() throws Exception {
        Field f = CANDriveSubsystem.class.getDeclaredField("drive");
        f.setAccessible(true);
        assertNotNull(
                f.get(subsystem),
                "drive field must not be null — initialize it with"
                        + " new DifferentialDrive(leftLeader, rightLeader) in the constructor");
        assertInstanceOf(
                DifferentialDrive.class,
                f.get(subsystem),
                "drive must be a DifferentialDrive instance");
    }

    @Test
    @DisplayName("Unit 3: leftFollower field is non-null after construction")
    void leftFollowerNonNull() throws Exception {
        Field f = CANDriveSubsystem.class.getDeclaredField("leftFollower");
        f.setAccessible(true);
        assertNotNull(f.get(subsystem), "leftFollower must be initialized in the constructor");
    }

    @Test
    @DisplayName("Unit 3: rightFollower field is non-null after construction")
    void rightFollowerNonNull() throws Exception {
        Field f = CANDriveSubsystem.class.getDeclaredField("rightFollower");
        f.setAccessible(true);
        assertNotNull(f.get(subsystem), "rightFollower must be initialized in the constructor");
    }

    @Test
    @DisplayName("Unit 3: driveArcade returns a non-null Command")
    void driveArcadeReturnsCommand() {
        Command cmd = subsystem.driveArcade(() -> 0.0, () -> 0.0);
        assertNotNull(cmd, "driveArcade must return a non-null Command");
    }

    @Test
    @DisplayName("Unit 3: Command returned by driveArcade requires CANDriveSubsystem")
    void driveArcadeCommandRequiresSubsystem() {
        Command cmd = subsystem.driveArcade(() -> 0.0, () -> 0.0);
        assertTrue(
                cmd.getRequirements().contains(subsystem),
                "The Command returned by driveArcade must require CANDriveSubsystem —"
                        + " use 'this.run(...)' not 'Commands.run(...)'");
    }
}
