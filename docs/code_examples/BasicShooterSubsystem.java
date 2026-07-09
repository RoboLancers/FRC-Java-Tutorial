// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// --8<-- [start:constants_import]
import static frc.robot.subsystems.ShooterConstants.*;
// --8<-- [end:constants_import]

/**
 * A basic shooter subsystem with a flywheel and a feeder.
 *
 * <p>The flywheel spins up to shooting speed first. Once it's up to speed,
 * the feeder pushes the game piece into the flywheel to launch it.
 */
public class BasicShooterSubsystem extends SubsystemBase {

  // --8<-- [start:motors]
  private final SparkMax flywheelMotor;
  private final SparkMax feederMotor;
  // --8<-- [end:motors]

  // --8<-- [start:constructor]
  /** Creates a new BasicShooterSubsystem. */
  public BasicShooterSubsystem() {
    // Create brushless NEO motors for the flywheel and feeder
    flywheelMotor = new SparkMax(FLYWHEEL_MOTOR_ID, MotorType.kBrushless);
    feederMotor = new SparkMax(FEEDER_MOTOR_ID, MotorType.kBrushless);

    // Configure the flywheel motor with a current limit
    SparkMaxConfig flywheelConfig = new SparkMaxConfig();
    flywheelConfig.smartCurrentLimit(FLYWHEEL_CURRENT_LIMIT);
    flywheelMotor.configure(flywheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // Configure the feeder motor with a current limit and invert the direction
    // so that positive voltage feeds game pieces toward the flywheel
    SparkMaxConfig feederConfig = new SparkMaxConfig();
    feederConfig.inverted(true);
    feederConfig.smartCurrentLimit(FEEDER_CURRENT_LIMIT);
    feederMotor.configure(feederConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }
  // --8<-- [end:constructor]

  // --8<-- [start:spinup]
  /**
   * Spins the flywheel up to speed without running the feeder.
   * Call this before shoot() to avoid launching a game piece before the
   * flywheel has reached full speed.
   */
  public void spinUp() {
    flywheelMotor.setVoltage(FLYWHEEL_SPINUP_VOLTAGE);
    feederMotor.set(0);
  }
  // --8<-- [end:spinup]

  // --8<-- [start:shoot]
  /**
   * Runs the flywheel at full shoot speed and engages the feeder to launch
   * the game piece.
   */
  public void shoot() {
    flywheelMotor.setVoltage(FLYWHEEL_SHOOT_VOLTAGE);
    feederMotor.setVoltage(FEEDER_SHOOT_VOLTAGE);
  }
  // --8<-- [end:shoot]

  // --8<-- [start:stop]
  /** Stops both the flywheel and feeder motors. */
  public void stop() {
    flywheelMotor.set(0);
    feederMotor.set(0);
  }
  // --8<-- [end:stop]

  // --8<-- [start:commands]
  /**
   * A command that spins the flywheel up to speed. Use this while the robot
   * is moving into shooting position, then chain to shootCommand().
   */
  public Command spinUpCommand() {
    return this.run(() -> spinUp());
  }

  /**
   * A command that runs the full shoot sequence — flywheel at full speed and
   * feeder engaged. Run spinUpCommand() first to avoid wasted shots.
   */
  public Command shootCommand() {
    return this.run(() -> shoot());
  }

  /**
   * A command that stops all motors on this subsystem. Bind this as the default
   * command so that motors are off when no other command is running.
   */
  public Command stopCommand() {
    return this.runOnce(() -> stop());
  }
  // --8<-- [end:commands]

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
