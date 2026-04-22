package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.subsystems.CANDriveSubsystem;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit 1: Verifies that CANDriveSubsystem declares the required motor controller fields.
 *
 * <p>These tests use Java reflection to inspect the class structure without instantiating the
 * subsystem, so they work even before Unit 2 (constructor setup) is complete.
 */
class Unit1FieldDeclarationTest {

    @Test
    @DisplayName("Unit 1: leftLeader is declared as private final SparkMax")
    void leftLeaderField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("leftLeader");
        assertEquals(SparkMax.class, f.getType(), "leftLeader must be of type SparkMax");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "leftLeader must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "leftLeader must be final");
    }

    @Test
    @DisplayName("Unit 1: leftFollower is declared as private final SparkMax")
    void leftFollowerField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("leftFollower");
        assertEquals(SparkMax.class, f.getType(), "leftFollower must be of type SparkMax");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "leftFollower must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "leftFollower must be final");
    }

    @Test
    @DisplayName("Unit 1: rightLeader is declared as private final SparkMax")
    void rightLeaderField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("rightLeader");
        assertEquals(SparkMax.class, f.getType(), "rightLeader must be of type SparkMax");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "rightLeader must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "rightLeader must be final");
    }

    @Test
    @DisplayName("Unit 1: rightFollower is declared as private final SparkMax")
    void rightFollowerField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("rightFollower");
        assertEquals(SparkMax.class, f.getType(), "rightFollower must be of type SparkMax");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "rightFollower must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "rightFollower must be final");
    }

    @Test
    @DisplayName("Unit 1: drive is declared as private final DifferentialDrive")
    void driveField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("drive");
        assertEquals(
                DifferentialDrive.class, f.getType(), "drive must be of type DifferentialDrive");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "drive must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "drive must be final");
    }

    @Test
    @DisplayName("Unit 1: CANDriveSubsystem has exactly 4 SparkMax fields")
    void exactlyFourSparkMaxFields() {
        Field[] fields = CANDriveSubsystem.class.getDeclaredFields();
        long count =
                Arrays.stream(fields).filter(f -> f.getType().equals(SparkMax.class)).count();
        assertEquals(
                4,
                count,
                "CANDriveSubsystem must have exactly 4 SparkMax fields"
                        + " (leftLeader, leftFollower, rightLeader, rightFollower)");
    }

    @Test
    @DisplayName("Unit 1: CANDriveSubsystem has exactly 1 DifferentialDrive field")
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
}
