// --8<-- [start:full-example]
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.RobotPreferences;

public class Autonomous extends SequentialCommandGroup {
	public Autonomous(Drivetrain drivetrain) {
		addCommands(
			new DriveDistance(drivetrain, RobotPreferences.autoDriveDistance()),
			new WaitCommand(RobotPreferences.autoDelay()),
			new ShooterUp()
		);
	}
}
// --8<-- [end:full-example]
