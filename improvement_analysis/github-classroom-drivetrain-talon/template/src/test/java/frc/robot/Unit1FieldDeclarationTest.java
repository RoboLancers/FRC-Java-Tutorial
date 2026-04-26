package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.subsystems.CANDriveSubsystem;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit 1: Verifies that CANDriveSubsystem declares the four required TalonFX fields.
 *
 * <p>These tests use Java reflection to inspect the class structure without instantiating the
 * subsystem, so they work even before Unit 2 (constructor setup) is complete.
 *
 * <p>The DifferentialDrive {@code drive} field is declared and verified in Unit 2.
 */
class Unit1FieldDeclarationTest {

    @Test
    @DisplayName("Unit 1: leftLeader is declared as private final TalonFX")
    void leftLeaderField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("leftLeader");
        assertEquals(TalonFX.class, f.getType(), "leftLeader must be of type TalonFX");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "leftLeader must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "leftLeader must be final");
    }

    @Test
    @DisplayName("Unit 1: leftFollower is declared as private final TalonFX")
    void leftFollowerField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("leftFollower");
        assertEquals(TalonFX.class, f.getType(), "leftFollower must be of type TalonFX");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "leftFollower must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "leftFollower must be final");
    }

    @Test
    @DisplayName("Unit 1: rightLeader is declared as private final TalonFX")
    void rightLeaderField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("rightLeader");
        assertEquals(TalonFX.class, f.getType(), "rightLeader must be of type TalonFX");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "rightLeader must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "rightLeader must be final");
    }

    @Test
    @DisplayName("Unit 1: rightFollower is declared as private final TalonFX")
    void rightFollowerField() throws NoSuchFieldException {
        Field f = CANDriveSubsystem.class.getDeclaredField("rightFollower");
        assertEquals(TalonFX.class, f.getType(), "rightFollower must be of type TalonFX");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "rightFollower must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "rightFollower must be final");
    }

    @Test
    @DisplayName("Unit 1: CANDriveSubsystem has exactly 4 TalonFX fields")
    void exactlyFourTalonFXFields() {
        Field[] fields = CANDriveSubsystem.class.getDeclaredFields();
        long count = Arrays.stream(fields)
                .filter(f -> f.getType().equals(TalonFX.class))
                .count();
        assertEquals(4, count, "CANDriveSubsystem must have exactly 4 TalonFX fields (leftLeader, leftFollower, rightLeader, rightFollower)");
    }

}
