package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANDriveSubsystem;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit 4: Verifies that RobotContainer declares the drive subsystem and wires up the default
 * command in configureBindings().
 */
class Unit4RobotContainerTest {

    // ── Reflection test: driveSubsystem field ────────────────────────────────

    @Test
    @DisplayName("Unit 4: RobotContainer declares private final CANDriveSubsystem driveSubsystem")
    void driveSubsystemFieldDeclared() throws NoSuchFieldException {
        Field f = RobotContainer.class.getDeclaredField("driveSubsystem");
        assertEquals(
                CANDriveSubsystem.class,
                f.getType(),
                "driveSubsystem must be of type CANDriveSubsystem");
        assertTrue(Modifier.isPrivate(f.getModifiers()), "driveSubsystem must be private");
        assertTrue(Modifier.isFinal(f.getModifiers()), "driveSubsystem must be final");
    }

    // ── HAL simulation tests: default command ────────────────────────────────

    @BeforeAll
    static void initHal() {
        HAL.initialize(500, 0);
    }

    @Test
    @DisplayName("Unit 4: RobotContainer constructor completes without throwing")
    void robotContainerConstructsWithoutError() {
        assertDoesNotThrow(
                RobotContainer::new,
                "RobotContainer constructor threw — check that driveSubsystem is initialized and"
                        + " setDefaultCommand() is called in configureBindings()");
    }

    @Test
    @DisplayName("Unit 4: CANDriveSubsystem has a default command set after RobotContainer construction")
    void defaultCommandIsSet() throws Exception {
        RobotContainer container = new RobotContainer();
        Field f = RobotContainer.class.getDeclaredField("driveSubsystem");
        f.setAccessible(true);
        CANDriveSubsystem drive = (CANDriveSubsystem) f.get(container);

        Command defaultCmd = drive.getDefaultCommand();
        assertNotNull(
                defaultCmd,
                "driveSubsystem must have a default command — call"
                        + " driveSubsystem.setDefaultCommand(driveSubsystem.driveArcade(...))"
                        + " in configureBindings()");
    }
}
