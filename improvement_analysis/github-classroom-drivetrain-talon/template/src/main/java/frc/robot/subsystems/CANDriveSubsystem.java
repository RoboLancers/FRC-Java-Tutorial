// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// TODO Unit 1: Add imports for TalonFX, DifferentialDrive using VSCode quick-fix (Ctrl+.)
// TODO Unit 2: Add import for TalonFXConfiguration
// TODO Unit 2: Add static import for DriveConstants: import static frc.robot.Constants.DriveConstants.*;
// TODO Unit 3: Add imports for Follower, InvertedValue, Command, and DoubleSupplier

public class CANDriveSubsystem extends SubsystemBase {

  // TODO Unit 1: Declare four private final TalonFX fields:
  //   leftLeader, leftFollower, rightLeader, rightFollower

  // TODO Unit 1: Declare a private final DifferentialDrive field named drive

  /** Creates a new CANDriveSubsystem. */
  public CANDriveSubsystem() {
    // TODO Unit 2: Initialize the four TalonFX objects using DriveConstants CAN IDs
    //              Example: leftLeader = new TalonFX(LEFT_LEADER_ID);

    // TODO Unit 2: Create a TalonFXConfiguration and set current limits on all motors
    //              Example: config.CurrentLimits.SupplyCurrentLimit = DRIVE_MOTOR_CURRENT_LIMIT;
    //              Then apply with getConfigurator().apply(config);

    // TODO Unit 2: Initialize the drive field with DifferentialDrive(leftLeader, rightLeader)
    //              Place this right after motor creation, before configuration

    // TODO Unit 3: Set up rightFollower to follow rightLeader using Follower control class

    // TODO Unit 3: Set up leftFollower to follow leftLeader using Follower control class

    // TODO Unit 3: Configure left side inversion using InvertedValue.Clockwise_Positive
    //              Apply the config to both leftLeader and leftFollower
  }

  // TODO Unit 3: Add the driveArcade command factory method below.
 
  // Reference: See Unit 3 assignment and docs/programming/driving_robot.md (TalonFX tab)

  @Override
  public void periodic() {
    // This method will be called once per scheduler run.
  }
}
