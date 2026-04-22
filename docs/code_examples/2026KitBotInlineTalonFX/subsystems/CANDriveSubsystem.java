
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.DriveConstants.*;

public class CANDriveSubsystem extends SubsystemBase {
  // --8<-- [start:motors]
  private final TalonFX leftLeader;
  private final TalonFX leftFollower;
  private final TalonFX rightLeader;
  private final TalonFX rightFollower;
  // --8<-- [end:motors]

  // --8<-- [start:differential-drive-variable]
  private final DifferentialDrive drive;
  // --8<-- [end:differential-drive-variable]

  // --8<-- [start:constructor]
  public CANDriveSubsystem() {
    // Create TalonFX motors for drive
    leftLeader = new TalonFX(LEFT_LEADER_ID);
    leftFollower = new TalonFX(LEFT_FOLLOWER_ID);
    rightLeader = new TalonFX(RIGHT_LEADER_ID);
    rightFollower = new TalonFX(RIGHT_FOLLOWER_ID);

    // Set up differential drive class
    drive = new DifferentialDrive(leftLeader, rightLeader);

    // TalonFX communicates over CAN via Phoenix 6 — no explicit CAN timeout is needed.
    // --8<-- [start:can-timeout]
    // Configuration is applied via getConfigurator().apply() below.
    // --8<-- [end:can-timeout]

    // Create the configuration. Phoenix 6 does not include a voltageCompensation()
    // equivalent. The current limit helps prevent tripping breakers.
    // --8<-- [start:voltage-compensation]
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.CurrentLimits.SupplyCurrentLimit = DRIVE_MOTOR_CURRENT_LIMIT;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    // --8<-- [end:voltage-compensation]

    // Set followers to follow their respective leaders using Follower control mode.
    // The Follower mode persists once set.
    // --8<-- [start:follower-config]
    leftFollower.setControl(new Follower(LEFT_LEADER_ID, false));
    rightFollower.setControl(new Follower(RIGHT_LEADER_ID, false));
    // --8<-- [end:follower-config]

    // Remove following, then apply config to right leader with neutral mode set to Brake
    // --8<-- [start:right-leader-config]
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    rightLeader.getConfigurator().apply(config);
    // --8<-- [end:right-leader-config]

    // Set config to inverted and then apply to left leader. Set Left side inverted
    // so that positive values drive both sides forward
    // --8<-- [start:left-inversion]
    config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    leftLeader.getConfigurator().apply(config);
    // --8<-- [end:left-inversion]
  }
  // --8<-- [end:constructor]

  @Override
  public void periodic() {
  }

  // Command factory to create command to drive the robot with joystick inputs.
  // --8<-- [start:drive-arcade-method]
  public Command driveArcade(DoubleSupplier xSpeed, DoubleSupplier zRotation) {
    return this.run(
        () -> drive.arcadeDrive(xSpeed.getAsDouble(), zRotation.getAsDouble()));
  }
  // --8<-- [end:drive-arcade-method]
}
