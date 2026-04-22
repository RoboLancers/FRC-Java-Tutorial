package frc.robot.examples;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Drivetrain;

/**
 * Code examples for the Methods tutorial page showing how commands
 * call subsystem methods. The ShooterSubsystem type below represents a
 * hypothetical shooter subsystem used to illustrate method calling patterns.
 */
public class CommandExamples {

    private Drivetrain m_drivetrain;
    private XboxController driverController;

    public void callingExamples(Drivetrain drivetrain, ShooterSubsystem shooter) {
        // --8<-- [start:calling_methods]
        // Calling a void method
        drivetrain.setSpeed(0.5);

        // Calling a method and storing the return value
        boolean done = shooter.atTarget();

        // Using a return value directly in a condition
        if (shooter.atTarget()) {
            shooter.stop();
        }
        // --8<-- [end:calling_methods]
    }

    public void executeDrive() {
        // --8<-- [start:frc_drive_call]
        m_drivetrain.setLeftRight(
            -driverController.getLeftY(),
            -driverController.getRightY()
        );
        // --8<-- [end:frc_drive_call]
    }
}

public class MethodsCallingMethodsExample {

    private SparkMax leftMotor;
    private SparkMax rightMotor;
    private Drivetrain m_drivetrain;

    // --8<-- [start:methods_calling_methods]
    // A subsystem method that calls motor methods
    public void stop() {
        leftMotor.set(0);
        rightMotor.set(0);
    }

    // A command calling a subsystem method
    protected void execute() {
        m_drivetrain.stop();
    }
    // --8<-- [end:methods_calling_methods]
}
