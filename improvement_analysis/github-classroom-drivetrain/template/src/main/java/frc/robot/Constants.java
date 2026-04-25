// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public final class Constants {

    public static final class DriveConstants {
        // TODO Unit 2: Define the four CAN IDs for the drive motors.
        // Replace the placeholder values below with the actual CAN IDs
        // for your specific robot. Check the REV Hardware Client or wiring
        // diagram to find the correct values.
        //
        public static final int LEFT_LEADER_ID   = 1;
        public static final int LEFT_FOLLOWER_ID = 2;
        public static final int RIGHT_LEADER_ID  = 3;
        public static final int RIGHT_FOLLOWER_ID = 4;
        // public static final int LEFT_LEADER_ID   = ???;
        // public static final int LEFT_FOLLOWER_ID = ???;
        // public static final int RIGHT_LEADER_ID  = ???;
        // public static final int RIGHT_FOLLOWER_ID = ???;
        public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60;

        // TODO Unit 2: Define the current limit for drive motors (in amps).
        // 60A is a reasonable maximum for CIM or NEO motors on a KitBot drivetrain.
        //
        // public static final int DRIVE_MOTOR_CURRENT_LIMIT = ???;
    }

    public static final class OperatorConstants {
        // Port number for the driver's Xbox controller.
        // 0 is the default if only one controller is plugged in.
        // Check the USB tab in the Driver Station application to confirm.
        public static final int DRIVER_CONTROLLER_PORT = 0;

        // Joystick scaling constants. Both axes are multiplied by these values
        // before being passed to arcadeDrive. Reduce during initial testing.
        public static final double DRIVE_SCALING    = 0.7;
        public static final double ROTATION_SCALING = 0.8;
    }
}
