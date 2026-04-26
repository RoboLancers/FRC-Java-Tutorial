// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

//import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// TODO Unit 1: Add imports for SparkMax and MotorType using VSCode quick-fix (Ctrl+.)
// TODO Unit 2: Add import for SparkMaxConfig
// TODO Unit 2: Add static import for DriveConstants: import static frc.robot.Constants.DriveConstants.*;
// TODO Unit 3: Add imports for DifferentialDrive, PersistMode, ResetMode, Command, and DoubleSupplier

public class CANDriveSubsystem extends SubsystemBase {

    // TODO Unit 1: Declare four private final SparkMax fields:
    //   leftLeader, leftFollower, rightLeader, rightFollower
    // TODO Unit 3: Declare a private final DifferentialDrive field named drive (above the constructor)
    private final SparkMax leftLeader;

    public CANDriveSubsystem() {
        
        
        // TODO Unit 2: Initialize the four SparkMax objects using DriveConstants CAN IDs
        //              and MotorType.kBrushed (or kBrushless for NEO motors)

        // TODO Unit 2: Set a CAN timeout of 250ms on each motor using setCANTimeout(250)

        // TODO Unit 2: Create a SparkMaxConfig, set voltageCompensation(12),
        //              and set smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT)

        // TODO Unit 3: Configure leftFollower to follow leftLeader, then apply config
        // TODO Unit 3: Configure rightFollower to follow rightLeader, then apply config
        // TODO Unit 3: Disable follower mode and apply config to rightLeader
        // TODO Unit 3: Set inverted(true) and apply config to leftLeader

        // TODO Unit 3: Initialize the drive field:
        //              drive = new DifferentialDrive(leftLeader, rightLeader);
        //              Place this line right after creating the SparkMax objects,
        //              before the setCANTimeout calls.

        // Placeholder — remove this throw when you start Unit 2.
        // It allows the class to compile while the final fields are unassigned.
        // See unit2-constructor-constants.md and unit3-arcade-drive-factory.md for instructions.
       //throw new UnsupportedOperationException("CANDriveSubsystem not yet implemented — complete Units 2 and 3");
    }

    // TODO Unit 3: Replace this stub with the real driveArcade implementation.
    //
    // Signature: public Command driveArcade(DoubleSupplier xSpeed, DoubleSupplier zRotation)
    // Body:      return this.run(() -> drive.arcadeDrive(xSpeed.getAsDouble(), zRotation.getAsDouble()));
    //
    // Reference: https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#creating-the-drivearcade-command-factory
    public edu.wpi.first.wpilibj2.command.Command driveArcade(
            java.util.function.DoubleSupplier xSpeed,
            java.util.function.DoubleSupplier zRotation) {
        throw new UnsupportedOperationException("driveArcade not yet implemented — complete Unit 3");
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run.
        // Use it to update dashboard values if needed.
    }
}
