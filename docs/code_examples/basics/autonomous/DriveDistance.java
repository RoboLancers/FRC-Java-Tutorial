// --8<-- [start:full-example]
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.RobotPreferences;

public class DriveDistance extends Command {

	private final Drivetrain drivetrain;
	// --8<-- [start:distance-field]
	private final double distance;
	// --8<-- [end:distance-field]

	public DriveDistance(Drivetrain drivetrain, double inches) {
		this.drivetrain = drivetrain;
		// --8<-- [start:constructor-body]
		this.distance = inches;
		addRequirements(drivetrain);
		// --8<-- [end:constructor-body]
	}

	@Override
	public void initialize() {
		drivetrain.resetDriveEncoder();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	public void execute() {
		drivetrain.arcadeDrive(RobotPreferences.driveDistanceSpeed(), 0.0);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		// --8<-- [start:is-finished-body]
		return drivetrain.getDriveEncoderDistance() >= distance;
		// --8<-- [end:is-finished-body]
	}

	// Called once after isFinished returns true, or if the command is interrupted
	@Override
	public void end(boolean interrupted) {
		drivetrain.arcadeDrive(0.0, 0.0);
	}
}
// --8<-- [end:full-example]
