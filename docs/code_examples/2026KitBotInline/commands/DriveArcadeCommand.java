// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANDriveSubsystem;

/**
 * An example command that uses an arcade drive subsystem. This command demonstrates
 * how to create a command that gets input from joystick axes and drives the robot.
 */
public class DriveArcadeCommand extends Command {
  // --8<-- [start:class-variables]
  private final DoubleSupplier xSpeed;
  private final DoubleSupplier zRotation;
  private final CANDriveSubsystem driveSubsystem;
  // --8<-- [end:class-variables]

  /**
   * Creates a new DriveArcadeCommand.
   *
   * @param xSpeed the input for movement speed as a DoubleSupplier (a function that returns a double)
   * @param zRotation the input for rotation speed as a DoubleSupplier
   * @param driveSubsystem the subsystem that this command requires
   */
  // --8<-- [start:constructor-signature]
  public DriveArcadeCommand(
      DoubleSupplier xSpeed, DoubleSupplier zRotation, CANDriveSubsystem driveSubsystem) {
    // --8<-- [end:constructor-signature]
    // --8<-- [start:constructor-body]
    this.xSpeed = xSpeed;
    this.zRotation = zRotation;
    this.driveSubsystem = driveSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveSubsystem);
    // --8<-- [end:constructor-body]
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  // --8<-- [start:execute-method]
  @Override
  public void execute() {
    driveSubsystem.arcadeDrive(xSpeed.getAsDouble(), zRotation.getAsDouble());
  }
  // --8<-- [end:execute-method]

  // Called once the command ends or is interrupted.
  // --8<-- [start:end-method]
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.arcadeDrive(0, 0);
  }
  // --8<-- [end:end-method]

  // Returns true when the command should end.
  // --8<-- [start:is-finished-method]
  @Override
  public boolean isFinished() {
    return false; // This command never finishes on its own; it runs continuously
  }
  // --8<-- [end:is-finished-method]
}
