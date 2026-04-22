package frc.robot;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import java.io.File;

public class RobotContainer {

  private final CommandXboxController driverController = new CommandXboxController(0);

  // --8<-- [start:joystick-integration]
  public RobotContainer() {
    SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));

    drivebase.setDefaultCommand(
        drivebase.driveCommand(
            () -> -driverController.getLeftY(),   // Forward/backward (inverted because joystick Y is inverted)
            () -> -driverController.getLeftX(),   // Left/right (inverted)
            () -> -driverController.getRightX()   // Rotation (inverted)
        )
    );
  }
  // --8<-- [end:joystick-integration]
}
