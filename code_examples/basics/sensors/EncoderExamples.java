// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class EncoderExamples extends SubsystemBase {

  // Example 1: External WPILib Encoder
  // --8<-- [start:external-encoder-field]
  private final Encoder externalEncoder;
  // --8<-- [end:external-encoder-field]

  // Example 2: SparkMax with built-in encoder
  private final SparkMax motor;
  // --8<-- [start:builtin-encoder-access]
  private final RelativeEncoder motorEncoder;
  // --8<-- [end:builtin-encoder-access]

  // --8<-- [start:external-encoder-constructor]
  public EncoderExamples() {
    // Create an external encoder on DIO ports 0 and 1
    externalEncoder = new Encoder(0, 1);

    // Configure the external encoder
    // 1 rotation = 360 counts (4x encoding on a 90 CPR encoder)
    externalEncoder.setDistancePerPulse(1.0 / 360.0);
  }
  // --8<-- [end:external-encoder-constructor]

  // Initialize SparkMax with built-in encoder
  {
    motor = new SparkMax(1, SparkMax.MotorType.kBrushless);
    SparkMaxConfig config = new SparkMaxConfig();
    motor.configure(config, null, null);
  }

  // Get the built-in RelativeEncoder from the SparkMax
  {
    motorEncoder = motor.getEncoder();
    // Optional: configure encoder units if needed
    motorEncoder.setPositionConversionFactor(1.0 / 42.0); // Convert rotations to meters (gear ratio)
  }

  // --8<-- [start:get-distance]
  /**
   * Gets the distance traveled using the external encoder.
   * Use this method to query the current distance in the units
   * you configured (via setDistancePerPulse).
   */
  public double getExternalEncoderDistance() {
    return externalEncoder.getDistance();
  }

  /**
   * Gets the distance traveled using the SparkMax built-in encoder.
   * This returns the position in rotations (or your configured units).
   */
  public double getMotorEncoderDistance() {
    return motorEncoder.getPosition();
  }
  // --8<-- [end:get-distance]

  // --8<-- [start:reset-encoder]
  /**
   * Resets the external encoder to zero.
   * Use this at the start of autonomous or when you need a fresh baseline.
   */
  public void resetExternalEncoder() {
    externalEncoder.reset();
  }

  /**
   * Resets the SparkMax built-in encoder to zero.
   */
  public void resetMotorEncoder() {
    motorEncoder.setPosition(0);
  }
  // --8<-- [end:reset-encoder]

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
