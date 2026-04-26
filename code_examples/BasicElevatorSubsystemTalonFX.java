// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.subsystems.ElevatorConstants.*;

// --8<-- [start:elevator_fields_talon]
public class BasicElevatorSubsystemTalonFX extends SubsystemBase {
  private final TalonFX leaderMotor;
  private final TalonFX followerMotor;
  private final DigitalInput topLimitSwitch;
  private final DigitalInput bottomLimitSwitch;
// --8<-- [end:elevator_fields_talon]

  // --8<-- [start:elevator_constructor_talon]
  public BasicElevatorSubsystemTalonFX() {
    // Create the motors
    leaderMotor = new TalonFX(LEADER_MOTOR_ID);
    followerMotor = new TalonFX(FOLLOWER_MOTOR_ID);

    // Configure with current limits
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.CurrentLimits.SupplyCurrentLimit = MOTOR_CURRENT_LIMIT;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;

    leaderMotor.getConfigurator().apply(config);
    followerMotor.getConfigurator().apply(config);

    // Set the follower to follow the leader
    followerMotor.setControl(new Follower(LEADER_MOTOR_ID, false));

    // Create the limit switches
    topLimitSwitch = new DigitalInput(TOP_LIMIT_SWITCH_PORT);
    bottomLimitSwitch = new DigitalInput(BOTTOM_LIMIT_SWITCH_PORT);
  }
// --8<-- [end:elevator_constructor_talon]

  // --8<-- [start:elevator_limits_talon]
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
  // --8<-- [end:elevator_limits_talon]

  // --8<-- [start:elevator_position_talon]
  /**
   * Returns the current height of the elevator in rotations.
   * TalonFX rotor position is in rotations; convert to inches if needed
   * using the DISTANCE_PER_PULSE constant.
   */
  public double getPosition() {
    return leaderMotor.getPosition().getValueAsDouble() * DISTANCE_PER_PULSE;
  }

  /**
   * Resets the encoder to zero (at current position).
   */
  public void resetPosition() {
    leaderMotor.setPosition(0.0);
  }
  // --8<-- [end:elevator_position_talon]

  // --8<-- [start:elevator_movement_talon]
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
  // --8<-- [end:elevator_movement_talon]

  // --8<-- [start:elevator_commands_talon]
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
  // --8<-- [end:elevator_commands_talon]

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
