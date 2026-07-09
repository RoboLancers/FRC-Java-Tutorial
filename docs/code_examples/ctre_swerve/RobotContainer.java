// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class RobotContainer
{
  // --8<-- [start:fields]
  // Top speed and angular rate the joysticks will command. kSpeedAt12Volts comes from the
  // generated TunerConstants and reflects your robot's actual measured free speed.
  private double MaxSpeed       = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
  private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

  // A SwerveRequest is a reusable, mutable "what should the drivetrain do right now" object.
  // FieldCentric drives relative to the field; a 10% deadband ignores joystick noise near zero.
  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.1)
      .withRotationalDeadband(MaxAngularRate * 0.1)
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

  // Locks the wheels into an "X" pattern so the robot resists being pushed.
  private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

  // Points every wheel in a given direction without driving — useful for diagnostics.
  private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

  private final CommandXboxController driverXbox = new CommandXboxController(0);

  // Built from the generator's TunerConstants — this one call replaces the entire
  // hand-written motor/encoder/kinematics setup a non-generated swerve project needs.
  private final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
  // --8<-- [end:fields]

  public RobotContainer()
  {
    configureBindings();
  }

  // --8<-- [start:configure-bindings]
  private void configureBindings()
  {
    // Default command: continuously drive field-centric off the joystick.
    // X is forward, Y is left (WPILib convention) — axes are negated because
    // joysticks report negative Y when pushed forward.
    drivetrain.setDefaultCommand(
        drivetrain.applyRequest(() ->
            drive.withVelocityX(-driverXbox.getLeftY() * MaxSpeed)
                 .withVelocityY(-driverXbox.getLeftX() * MaxSpeed)
                 .withRotationalRate(-driverXbox.getRightX() * MaxAngularRate)
        )
    );

    // Apply the drivetrain's configured neutral mode while disabled.
    final SwerveRequest.Idle idle = new SwerveRequest.Idle();
    RobotModeTriggers.disabled().whileTrue(
        drivetrain.applyRequest(() -> idle).ignoringDisable(true)
    );

    // Hold A to brake (lock wheels in an X).
    driverXbox.a().whileTrue(drivetrain.applyRequest(() -> brake));

    // Hold B to point all wheels toward the left stick's direction.
    driverXbox.b().whileTrue(drivetrain.applyRequest(() ->
        point.withModuleDirection(new Rotation2d(-driverXbox.getLeftY(), -driverXbox.getLeftX()))
    ));

    // Re-zero field-centric heading — press this when the robot's "forward" drifts from
    // the field's forward, e.g. at the start of teleop.
    driverXbox.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));
  }
  // --8<-- [end:configure-bindings]

  public Command getAutonomousCommand()
  {
    return Commands.none();
  }
}
