// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

/**
 * Constants for the BasicShooterSubsystem. Keeping constants in a dedicated
 * file next to the subsystem makes them easy to find and update without
 * opening the subsystem code itself.
 */
// --8<-- [start:shooter_constants]
public final class ShooterConstants {
  // CAN IDs for the shooter motors. Update these to match your robot's wiring.
  public static final int FLYWHEEL_MOTOR_ID = 5;
  public static final int FEEDER_MOTOR_ID = 6;

  // Current limits to protect the motors and prevent breakers from tripping.
  // Lower the feeder limit since it does not need as much torque as the flywheel.
  public static final int FLYWHEEL_CURRENT_LIMIT = 60;
  public static final int FEEDER_CURRENT_LIMIT = 40;

  // Voltage values for each operating mode. These may need to be tuned
  // based on your robot's specific construction and game piece weight.
  public static final double FLYWHEEL_SPINUP_VOLTAGE = 8.0;
  public static final double FLYWHEEL_SHOOT_VOLTAGE = 10.0;
  public static final double FEEDER_SHOOT_VOLTAGE = 6.0;

  private ShooterConstants() {
    // Utility class — do not instantiate
  }
}
// --8<-- [end:shooter_constants]
