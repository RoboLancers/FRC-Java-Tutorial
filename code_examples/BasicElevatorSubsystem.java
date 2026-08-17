// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.subsystems.ElevatorConstants.*;

// --8<-- [start:elevator_fields]
public class BasicElevatorSubsystem extends SubsystemBase {
  private final SparkMax leaderMotor;
  private final SparkMax followerMotor;
  private final RelativeEncoder leaderEncoder;
  private final DigitalInput topLimitSwitch;
  private final DigitalInput bottomLimitSwitch;
// --8<-- [end:elevator_fields]

  // --8<-- [start:elevator_constructor]
  public BasicElevatorSubsystem() {
    // Create the motors
    leaderMotor = new SparkMax(LEADER_MOTOR_ID, SparkMax.MotorType.kBrushless);
    followerMotor = new SparkMax(FOLLOWER_MOTOR_ID, SparkMax.MotorType.kBrushless);

    // Configure the leader motor with current limit
    SparkMaxConfig config = new SparkMaxConfig();
    config.smartCurrentLimit(MOTOR_CURRENT_LIMIT);
    leaderMotor.configure(config, null, null);

    // Configure the follower motor and set it to follow the leader
    followerMotor.configure(config, null, null);
    followerMotor.follow(leaderMotor);

    // Get the built-in encoder from the leader motor
    leaderEncoder = leaderMotor.getEncoder();
    leaderEncoder.setPositionConversionFactor(DISTANCE_PER_PULSE);

    // Create the limit switches
    topLimitSwitch = new DigitalInput(TOP_LIMIT_SWITCH_PORT);
    bottomLimitSwitch = new DigitalInput(BOTTOM_LIMIT_SWITCH_PORT);
  }
// --8<-- [end:elevator_constructor]

  // --8<-- [start:elevator_limits]
  /**
   * Returns true if the top limit switch is pressed (elevator at max height).
   * Handles inversion so callers get the correct logical value.
   */
  public boolean isAtTopLimit() {
    return !topLimitSwitch.get();
  }

  /**
   * Returns true if the bottom limit switch is pressed (elevator at min height).
   * Handles inversion so callers get the correct logical value.
   */
  public boolean isAtBottomLimit() {
    return !bottomLimitSwitch.get();
  }
  // --8<-- [end:elevator_limits]

  // --8<-- [start:elevator_position]
  /**
   * Returns the current height of the elevator in inches.
   */
  public double getPosition() {
    return leaderEncoder.getPosition();
  }

  /**
   * Resets the encoder to zero (at current position).
   */
  public void resetPosition() {
    leaderEncoder.setPosition(0.0);
  }
  // --8<-- [end:elevator_position]

  // --8<-- [start:elevator_movement]
  /**
   * Moves the elevator up at constant voltage.
   * Stops if the top limit switch is pressed.
   */
  public void moveUp() {
    if (!isAtTopLimit()) {
      leaderMotor.setVoltage(UP_VOLTAGE);
    } else {
      stop();
    }
  }

  /**
   * Moves the elevator down at constant voltage.
   * Stops if the bottom limit switch is pressed.
   */
  public void moveDown() {
    if (!isAtBottomLimit()) {
      leaderMotor.setVoltage(DOWN_VOLTAGE);
    } else {
      stop();
    }
  }

  /**
   * Stops the elevator.
   */
  public void stop() {
    leaderMotor.setVoltage(0.0);
  }
  // --8<-- [end:elevator_movement]

  // --8<-- [start:elevator_commands]
  /**
   * A command that moves the elevator up.
   * The command runs continuously, checking limit switches each loop.
   */
  public Command moveUpCommand() {
    return this.run(this::moveUp);
  }

  /**
   * A command that moves the elevator down.
   * The command runs continuously, checking limit switches each loop.
   */
  public Command moveDownCommand() {
    return this.run(this::moveDown);
  }

  /**
   * A command that stops the elevator.
   * This is useful as a default command.
   */
  public Command stopCommand() {
    return this.runOnce(this::stop);
  }
  // --8<-- [end:elevator_commands]

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
