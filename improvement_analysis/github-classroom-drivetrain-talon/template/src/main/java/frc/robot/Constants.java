// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class DriveConstants {
    // TODO Unit 2: Add four CAN ID constants (LEFT_LEADER_ID, LEFT_FOLLOWER_ID, RIGHT_LEADER_ID, RIGHT_FOLLOWER_ID)
    //              These should match your robot's motor CAN IDs. Typical values: 1, 2, 3, 4

    // TODO Unit 2: Add DRIVE_MOTOR_CURRENT_LIMIT constant
    //              Typical value: 60 Amps (prevents breaker trips and motor damage)
  }

  public static final class OperatorConstants {
    // Port constants for driver and operator controllers. These should match the
    // values in the Joystick tab of the Driver Station software
    public static final int DRIVER_CONTROLLER_PORT = 0;

    // This value is multiplied by the joystick value when driving the robot to
    // help avoid driving and turning too fast and being difficult to control
    public static final double DRIVE_SCALING = 0.7;
    public static final double ROTATION_SCALING = 0.8;
  }

  private Constants() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
