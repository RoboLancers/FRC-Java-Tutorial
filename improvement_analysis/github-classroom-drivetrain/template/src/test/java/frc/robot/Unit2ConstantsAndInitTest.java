package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import com.revrobotics.spark.SparkMax;
import edu.wpi.first.hal.HAL;
import frc.robot.subsystems.CANDriveSubsystem;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit 2: Verifies that DriveConstants defines the required constants with correct values, and that
 * the CANDriveSubsystem constructor initializes the SparkMax motor objects.
 *
 * <p>Reflection tests check constants without needing hardware. HAL simulation tests instantiate
 * the subsystem in WPILib's desktop simulation mode (no physical robot required).
 *
 * <p>Note: the constructor only fully compiles once Unit 3 assigns the {@code drive} final field,
 * so the HAL tests below earn points after both Unit 2 and Unit 3 work are committed.
 */
class Unit2ConstantsAndInitTest {

    // ── Reflection tests: DriveConstants values ──────────────────────────────

    @Test
    @DisplayName("Unit 2: DriveConstants.LEFT_LEADER_ID is public static final int 1")
    void leftLeaderId() throws Exception {
        Field f = Constants.DriveConstants.class.getDeclaredField("LEFT_LEADER_ID");
        assertTrue(Modifier.isPublic(f.getModifiers()), "LEFT_LEADER_ID must be public");
        assertTrue(Modifier.isStatic(f.getModifiers()), "LEFT_LEADER_ID must be static");
        assertTrue(Modifier.isFinal(f.getModifiers()), "LEFT_LEADER_ID must be final");
        assertEquals(int.class, f.getType(), "LEFT_LEADER_ID must be type int");
        assertEquals(1, (int) f.get(null), "LEFT_LEADER_ID must equal 1");
    }

    @Test
    @DisplayName("Unit 2: DriveConstants.LEFT_FOLLOWER_ID is public static final int 2")
    void leftFollowerId() throws Exception {
        Field f = Constants.DriveConstants.class.getDeclaredField("LEFT_FOLLOWER_ID");
        assertTrue(Modifier.isPublic(f.getModifiers()), "LEFT_FOLLOWER_ID must be public");
        assertTrue(Modifier.isStatic(f.getModifiers()), "LEFT_FOLLOWER_ID must be static");
        assertTrue(Modifier.isFinal(f.getModifiers()), "LEFT_FOLLOWER_ID must be final");
        assertEquals(int.class, f.getType(), "LEFT_FOLLOWER_ID must be type int");
        assertEquals(2, (int) f.get(null), "LEFT_FOLLOWER_ID must equal 2");
    }

    @Test
    @DisplayName("Unit 2: DriveConstants.RIGHT_LEADER_ID is public static final int 3")
    void rightLeaderId() throws Exception {
        Field f = Constants.DriveConstants.class.getDeclaredField("RIGHT_LEADER_ID");
        assertTrue(Modifier.isPublic(f.getModifiers()), "RIGHT_LEADER_ID must be public");
        assertTrue(Modifier.isStatic(f.getModifiers()), "RIGHT_LEADER_ID must be static");
        assertTrue(Modifier.isFinal(f.getModifiers()), "RIGHT_LEADER_ID must be final");
        assertEquals(int.class, f.getType(), "RIGHT_LEADER_ID must be type int");
        assertEquals(3, (int) f.get(null), "RIGHT_LEADER_ID must equal 3");
    }

    @Test
    @DisplayName("Unit 2: DriveConstants.RIGHT_FOLLOWER_ID is public static final int 4")
    void rightFollowerId() throws Exception {
        Field f = Constants.DriveConstants.class.getDeclaredField("RIGHT_FOLLOWER_ID");
        assertTrue(Modifier.isPublic(f.getModifiers()), "RIGHT_FOLLOWER_ID must be public");
        assertTrue(Modifier.isStatic(f.getModifiers()), "RIGHT_FOLLOWER_ID must be static");
        assertTrue(Modifier.isFinal(f.getModifiers()), "RIGHT_FOLLOWER_ID must be final");
        assertEquals(int.class, f.getType(), "RIGHT_FOLLOWER_ID must be type int");
        assertEquals(4, (int) f.get(null), "RIGHT_FOLLOWER_ID must equal 4");
    }

    @Test
    @DisplayName("Unit 2: DriveConstants.DRIVE_MOTOR_CURRENT_LIMIT is public static final int 60")
    void driveMotorCurrentLimit() throws Exception {
        Field f = Constants.DriveConstants.class.getDeclaredField("DRIVE_MOTOR_CURRENT_LIMIT");
        assertTrue(
                Modifier.isPublic(f.getModifiers()), "DRIVE_MOTOR_CURRENT_LIMIT must be public");
        assertTrue(
                Modifier.isStatic(f.getModifiers()), "DRIVE_MOTOR_CURRENT_LIMIT must be static");
        assertTrue(
                Modifier.isFinal(f.getModifiers()), "DRIVE_MOTOR_CURRENT_LIMIT must be final");
        assertEquals(int.class, f.getType(), "DRIVE_MOTOR_CURRENT_LIMIT must be type int");
        assertEquals(60, (int) f.get(null), "DRIVE_MOTOR_CURRENT_LIMIT must equal 60 (amps)");
    }

    // ── HAL simulation tests: constructor behavior ────────────────────────────

    @BeforeAll
    static void initHal() {
        // HAL.initialize returns false (not throws) if already initialized — safe across classes.
        assertTrue(
                HAL.initialize(500, 0),
                "WPILib HAL initialization failed — check that JNI native libs are on"
                        + " java.library.path");
    }

    @Test
    @DisplayName("Unit 2: CANDriveSubsystem constructor runs without throwing")
    void constructorDoesNotThrow() {
        assertDoesNotThrow(
                CANDriveSubsystem::new,
                "CANDriveSubsystem constructor threw an exception — check that all SparkMax"
                        + " objects are created and SparkMaxConfig is applied");
    }

    @Test
    @DisplayName("Unit 2: leftLeader SparkMax field is non-null after construction")
    void leftLeaderIsNonNull() throws Exception {
        CANDriveSubsystem subsystem = new CANDriveSubsystem();
        Field f = CANDriveSubsystem.class.getDeclaredField("leftLeader");
        f.setAccessible(true);
        assertNotNull(
                f.get(subsystem),
                "leftLeader must not be null — initialize it with"
                        + " new SparkMax(LEFT_LEADER_ID, MotorType.kBrushed) in the constructor");
        assertInstanceOf(
                SparkMax.class,
                f.get(subsystem),
                "leftLeader must be a SparkMax instance");
    }
}
