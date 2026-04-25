package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import com.revrobotics.spark.SparkMax;
import frc.robot.subsystems.CANDriveSubsystem;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

/**
 * Unit 1: Verifies that CANDriveSubsystem declares the four required SparkMax fields.
 *
 * <p>These tests use Java reflection to inspect the class structure without instantiating the
 * subsystem, so they work even before Unit 2 (constructor setup) is complete.
 *
 * <p>The DifferentialDrive {@code drive} field is declared and verified in Unit 3.
 */
class Unit1FieldDeclarationTest {

    private static boolean unit1NotStarted = false;

    static {
        // Check if Unit 1 has been started by trying to find the leftLeader field.
        // If it doesn't exist, Unit 1 hasn't been started yet.
        try {
            CANDriveSubsystem.class.getDeclaredField("leftLeader");
        } catch (NoSuchFieldException e) {
            unit1NotStarted = true;
        }
    }

    private boolean isUnit1NotStarted() {
        return unit1NotStarted;
    }

    @Test
    @DisplayName("Unit 1: leftLeader is declared as private final SparkMax")
    @DisabledIf("isUnit1NotStarted")
    void leftLeaderField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("leftLeader");
        assertEquals(SparkMax.class, f.getType(), "leftLeader must be of type SparkMax");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "leftLeader must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "leftLeader must be final");
    }

    @Test
    @DisplayName("Unit 1: leftFollower is declared as private final SparkMax")
    @DisabledIf("isUnit1NotStarted")
    void leftFollowerField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("leftFollower");
        assertEquals(SparkMax.class, f.getType(), "leftFollower must be of type SparkMax");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "leftFollower must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "leftFollower must be final");
    }

    @Test
    @DisplayName("Unit 1: rightLeader is declared as private final SparkMax")
    @DisabledIf("isUnit1NotStarted")
    void rightLeaderField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("rightLeader");
        assertEquals(SparkMax.class, f.getType(), "rightLeader must be of type SparkMax");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "rightLeader must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "rightLeader must be final");
    }

    @Test
    @DisplayName("Unit 1: rightFollower is declared as private final SparkMax")
    @DisabledIf("isUnit1NotStarted")
    void rightFollowerField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("rightFollower");
        assertEquals(SparkMax.class, f.getType(), "rightFollower must be of type SparkMax");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "rightFollower must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "rightFollower must be final");
    }

    @Test
    @DisplayName("Unit 1: CANDriveSubsystem has exactly 4 SparkMax fields")
    @DisabledIf("isUnit1NotStarted")
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

}
