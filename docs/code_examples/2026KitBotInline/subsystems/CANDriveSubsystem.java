
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.DriveConstants.*;

public class CANDriveSubsystem extends SubsystemBase {
  // --8<-- [start: motors]
  private final TalonFX leftLeader;
  private final TalonFX leftFollower;
  private final TalonFX rightLeader;
  private final TalonFX rightFollower;

  // --8<-- [end: motors]
  private final DifferentialDrive drive;

  // --8<-- [start: constructor]

  public CANDriveSubsystem() {
    // Create TalonFX motors for drive
    leftLeader = new TalonFX(LEFT_LEADER_ID);
    leftFollower = new TalonFX(LEFT_FOLLOWER_ID);
    rightLeader = new TalonFX(RIGHT_LEADER_ID);
    rightFollower = new TalonFX(RIGHT_FOLLOWER_ID);

    // set up differential drive class
    drive = new DifferentialDrive(leftLeader, rightLeader);

    // Create the configuration to apply to motors. Current limits help prevent
    // tripping breakers or damaging motors. TalonFX uses stator current limiting
    // which is more effective for FOC motors than supply current limiting.
    TalonFXConfiguration config = new TalonFXConfiguration();

    // Configure current limits
    var currentLimits = config.CurrentLimits;
    currentLimits.StatorCurrentLimit = DRIVE_MOTOR_CURRENT_LIMIT;
    currentLimits.StatorCurrentLimitEnable = true;

    // Set neutral mode to brake for better control
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    // Apply base configuration to right leader
    rightLeader.getConfigurator().apply(config);

    // Set left leader to inverted so that positive values drive both sides forward
    config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    leftLeader.getConfigurator().apply(config);

    // Configure followers using TalonFX's Follower control request
    // Right follower follows right leader without inversion
    rightFollower.setControl(new Follower(RIGHT_LEADER_ID, false));

    // Left follower follows left leader without additional inversion
    // (the left leader already handles the inversion)
    leftFollower.setControl(new Follower(LEFT_LEADER_ID, false));
  }

  // --8<-- [end: constructor]

  @Override
  public void periodic() {
  }

  // Command factory to create command to drive the robot with joystick inputs.
  public Command driveArcade(DoubleSupplier xSpeed, DoubleSupplier zRotation) {
    return this.run(
        () -> drive.arcadeDrive(xSpeed.getAsDouble(), zRotation.getAsDouble()));
  }
}
