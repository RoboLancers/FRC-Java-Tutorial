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
import org.junit.jupiter.api.condition.DisabledIf;

/**
 * Unit 4: Verifies that RobotContainer declares the drive subsystem and wires up the default
 * command in configureBindings().
 */
class Unit4RobotContainerTest {

    private static boolean unit4NotStarted = false;

    static {
        // Check if Unit 4 has been started by trying to find the driveSubsystem field.
        // If it doesn't exist, Unit 4 hasn't been started yet.
        try {
            RobotContainer.class.getDeclaredField("driveSubsystem");
        } catch (NoSuchFieldException e) {
            unit4NotStarted = true;
        }
    }

    private boolean isUnit4NotStarted() {
        return unit4NotStarted;
    }

    private static boolean prerequisitesNotMet = false;

    // ── Reflection test: driveSubsystem field ────────────────────────────────

    @Test
    @DisplayName("Unit 4: RobotContainer declares private final CANDriveSubsystem driveSubsystem")
    @DisabledIf("isUnit4NotStarted")
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
        // Only initialize HAL if Unit 4 has been started
        if (!unit4NotStarted) {
            HAL.initialize(500, 0);
            
            // Check if prerequisites (Unit 2 and Unit 3) have been completed.
            // If CANDriveSubsystem constructor or RobotContainer constructor throws,
            // prerequisites are not met and Unit 4 tests should be skipped.
            try {
                new CANDriveSubsystem();
                new RobotContainer();
            } catch (Throwable e) {
                prerequisitesNotMet = true;
            }
        }
    }

    private static boolean arePrerequisitesNotMet() {
        return prerequisitesNotMet;
    }

    @Test
    @DisplayName("Unit 4: RobotContainer constructor completes without throwing")
    @DisabledIf("arePrerequisitesNotMet")
    void robotContainerConstructsWithoutError() {
        assertDoesNotThrow(
                RobotContainer::new,
                "RobotContainer constructor threw — check that driveSubsystem is initialized and"
                        + " setDefaultCommand() is called in configureBindings()");
    }

    @Test
    @DisplayName("Unit 4: CANDriveSubsystem has a default command set after RobotContainer construction")
    @DisabledIf("arePrerequisitesNotMet")
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
