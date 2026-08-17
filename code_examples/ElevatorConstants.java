// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

// --8<-- [start:elevator_constants]
public final class ElevatorConstants {
  // Motor IDs (SparkMax CAN IDs or TalonFX CAN IDs)
  public static final int LEADER_MOTOR_ID = 5;
  public static final int FOLLOWER_MOTOR_ID = 6;

  // Current limits
  public static final int MOTOR_CURRENT_LIMIT = 40;

  // Motor voltages for up/down movement
  public static final double UP_VOLTAGE = 8.0;
  public static final double DOWN_VOLTAGE = -8.0;

  // Limit switch DIO ports
  public static final int TOP_LIMIT_SWITCH_PORT = 2;
  public static final int BOTTOM_LIMIT_SWITCH_PORT = 3;

  // Encoder constants for the leader motor
  // This example assumes a 10:1 gear ratio and counts per revolution of encoder
  public static final double ENCODER_COUNTS_PER_REVOLUTION = 42.0; // NEO encoder CPR
  public static final double GEAR_RATIO = 10.0;
  public static final double ELEVATOR_SPROCKET_CIRCUMFERENCE = 1.432; // inches

  // Calculate distance per pulse: (circumference / CPR) * (1 / gear_ratio)
  public static final double DISTANCE_PER_PULSE =
      ELEVATOR_SPROCKET_CIRCUMFERENCE / (ENCODER_COUNTS_PER_REVOLUTION * GEAR_RATIO);

  // Position setpoints (in inches)
  public static final double ELEVATOR_MIN_HEIGHT = 0.0; // Bottom position
  public static final double ELEVATOR_MAX_HEIGHT = 48.0; // Top position
  public static final double ELEVATOR_SAFE_HEIGHT = 6.0; // Minimum height for safe operation

  private ElevatorConstants() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
// --8<-- [end:elevator_constants]
