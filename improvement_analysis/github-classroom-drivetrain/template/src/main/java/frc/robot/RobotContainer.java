// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.CANDriveSubsystem;

import static frc.robot.Constants.OperatorConstants.*;

public class RobotContainer {

    // The drive subsystem — declared and instantiated for you.
    private final CANDriveSubsystem driveSubsystem = new CANDriveSubsystem();

    // The driver's Xbox controller — reads joystick and button input.
    private final CommandXboxController driverController =
        new CommandXboxController(DRIVER_CONTROLLER_PORT);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // TODO Unit 4: Set driveArcade as the default command for driveSubsystem.
        //
        // Use driveSubsystem.setDefaultCommand(...) with driveSubsystem.driveArcade(...)
        // as the argument. Pass lambdas that read the controller axes, negating each
        // axis and multiplying by the scaling constants from OperatorConstants.
        //
        // Reference: https://robolancers.github.io/FRC-Java-Tutorial/programming/driving_robot.html#wiring-up-in-robotcontainer
    }

    public Command getAutonomousCommand() {
        return Commands.none();
    }
}
