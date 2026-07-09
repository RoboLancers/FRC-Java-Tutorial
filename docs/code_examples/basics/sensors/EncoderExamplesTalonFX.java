// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class EncoderExamplesTalonFX extends SubsystemBase {

  // Example 1: External WPILib Encoder (works with TalonFX too)
  private final Encoder externalEncoder;

  // Example 2: TalonFX with built-in encoder
  private final TalonFX motor;

  public EncoderExamplesTalonFX() {
    // Create an external encoder on DIO ports 0 and 1
    externalEncoder = new Encoder(0, 1);
    externalEncoder.setDistancePerPulse(1.0 / 360.0);

    // Create a TalonFX motor
    motor = new TalonFX(1);
  }

  // --8<-- [start:builtin-encoder-access-talon]
  /**
   * Gets the distance traveled using the TalonFX built-in encoder.
   *
   * TalonFX uses Phoenix 6's signal system to retrieve encoder data.
   * This returns the rotor position in rotations.
   */
  public double getMotorEncoderDistance() {
    return motor.getPosition().getValueAsDouble();
  }
  // --8<-- [end:builtin-encoder-access-talon]

  /**
   * Gets the distance traveled using the external encoder.
   */
  public double getExternalEncoderDistance() {
    return externalEncoder.getDistance();
  }

  // --8<-- [start:get-distance-talon]
  /**
   * Gets the velocity of the TalonFX in rotations per second.
   * Useful for feedback control or monitoring motor speed.
   */
  public double getMotorVelocity() {
    return motor.getVelocity().getValueAsDouble();
  }
  // --8<-- [end:get-distance-talon]

  // --8<-- [start:reset-encoder-talon]
  /**
   * Resets the TalonFX built-in encoder to zero.
   *
   * Note: TalonFX encoder resets require setting the position via
   * the control system. The setPosition() method in Phoenix 6 is used
   * to reset the rotor position.
   */
  public void resetMotorEncoder() {
    motor.setPosition(0);
  }
  // --8<-- [end:reset-encoder-talon]

  /**
   * Resets the external encoder to zero.
   */
  public void resetExternalEncoder() {
    externalEncoder.reset();
  }

  /**
   * Command factory that resets both encoders.
   */
  public Command resetEncodersCommand() {
    return this.runOnce(() -> {
      resetExternalEncoder();
      resetMotorEncoder();
    });
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
