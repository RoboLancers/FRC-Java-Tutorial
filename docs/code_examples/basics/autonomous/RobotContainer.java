package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.Autonomous;
import frc.robot.subsystems.Drivetrain;

public class RobotContainer {
	private final Drivetrain m_drivetrain = new Drivetrain();

	// --8<-- [start:get-autonomous-command]
	public Command getAutonomousCommand() {
		return new Autonomous(m_drivetrain);
	}
	// --8<-- [end:get-autonomous-command]
}
