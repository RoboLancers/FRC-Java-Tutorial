package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.commands.*;

/**
 * Add your docs here.
 */
// --8<-- [start:full-class]
public class Telemetry extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Telemetry() {
        // --8<-- [start:constructor-drivetrain]
        // Drivetrain
        SmartDashboard.putData("Reset Drive Encoder", new DriveResetEncoder());
        // --8<-- [end:constructor-drivetrain]

        // --8<-- [start:constructor-shooter]
        // Shooter
        SmartDashboard.putData("Shooter Up", new ShooterUp());
        SmartDashboard.putData("Shooter Down", new ShooterDown());
        SmartDashboard.putData("Shooter Up Auto", new ShooterUpAuto());
        // --8<-- [end:constructor-shooter]
    }

    public void update() {
        // --8<-- [start:update-drivetrain]
        // Drivetrain
        SmartDashboard.putNumber("Drive Encoder Count", Robot.m_drivetrain.getDriveEncoderCount());
        // --8<-- [end:update-drivetrain]

        // --8<-- [start:update-shooter]
        // Shooter
        SmartDashboard.putBoolean("Shooter Switch", Robot.m_shooter.isShooterSwitchClosed());
        // --8<-- [end:update-shooter]
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
// --8<-- [end:full-class]
