// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.FuelConstants.*;

public class CANFuelSubsystem extends SubsystemBase {
  private final TalonFX feederRoller;
  private final TalonFX intakeLauncherRoller;

  /** Creates a new CANBallSubsystem. */
  public CANFuelSubsystem() {
    // create TalonFX motors for each of the motors on the launcher mechanism
    intakeLauncherRoller = new TalonFX(INTAKE_LAUNCHER_MOTOR_ID);
    feederRoller = new TalonFX(FEEDER_MOTOR_ID);

    // put default values for various fuel operations onto the dashboard
    // all methods in this subsystem pull their values from the dashbaord to allow
    // you to tune the values easily, and then replace the values in Constants.java
    // with your new values. For more information, see the Software Guide.
    SmartDashboard.putNumber("Intaking feeder roller value", INTAKING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Intaking intake roller value", INTAKING_INTAKE_VOLTAGE);
    SmartDashboard.putNumber("Launching feeder roller value", LAUNCHING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Launching launcher roller value", LAUNCHING_LAUNCHER_VOLTAGE);
    SmartDashboard.putNumber("Spin-up feeder roller value", SPIN_UP_FEEDER_VOLTAGE);

    // create the configuration for the feeder roller, set a current limit and apply
    // the config to the controller
    TalonFXConfiguration feederConfig = new TalonFXConfiguration();
    var feederCurrentLimits = feederConfig.CurrentLimits;
    feederCurrentLimits.StatorCurrentLimit = FEEDER_MOTOR_CURRENT_LIMIT;
    feederCurrentLimits.StatorCurrentLimitEnable = true;
    feederConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    feederRoller.getConfigurator().apply(feederConfig);

    // create the configuration for the launcher roller, set a current limit, set
    // the motor to inverted so that positive values are used for both intaking and
    // launching, and apply the config to the controller
    TalonFXConfiguration launcherConfig = new TalonFXConfiguration();
    var launcherCurrentLimits = launcherConfig.CurrentLimits;
    launcherCurrentLimits.StatorCurrentLimit = LAUNCHER_MOTOR_CURRENT_LIMIT;
    launcherCurrentLimits.StatorCurrentLimitEnable = true;
    launcherConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    launcherConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    intakeLauncherRoller.getConfigurator().apply(launcherConfig);
  }

  // A method to set the rollers to values for intaking
  public void intake() {
    feederRoller.setControl(new VoltageOut(SmartDashboard.getNumber("Intaking feeder roller value", INTAKING_FEEDER_VOLTAGE)));
    intakeLauncherRoller
        .setControl(new VoltageOut(SmartDashboard.getNumber("Intaking intake roller value", INTAKING_INTAKE_VOLTAGE)));
  }

  // A method to set the rollers to values for ejecting fuel out the intake. Uses
  // the same values as intaking, but in the opposite direction.
  public void eject() {
    feederRoller
        .setControl(new VoltageOut(-1 * SmartDashboard.getNumber("Intaking feeder roller value", INTAKING_FEEDER_VOLTAGE)));
    intakeLauncherRoller
        .setControl(new VoltageOut(-1 * SmartDashboard.getNumber("Intaking launcher roller value", INTAKING_INTAKE_VOLTAGE)));
  }

  // A method to set the rollers to values for launching.
  public void launch() {
    feederRoller.setControl(new VoltageOut(SmartDashboard.getNumber("Launching feeder roller value", LAUNCHING_FEEDER_VOLTAGE)));
    intakeLauncherRoller
        .setControl(new VoltageOut(SmartDashboard.getNumber("Launching launcher roller value", LAUNCHING_LAUNCHER_VOLTAGE)));
  }

  // A method to stop the rollers
  public void stop() {
    feederRoller.setControl(new VoltageOut(0));
    intakeLauncherRoller.setControl(new VoltageOut(0));
  }

  // A method to spin up the launcher roller while spinning the feeder roller to
  // push Fuel away from the launcher
  public void spinUp() {
    feederRoller
        .setControl(new VoltageOut(SmartDashboard.getNumber("Spin-up feeder roller value", SPIN_UP_FEEDER_VOLTAGE)));
    intakeLauncherRoller
        .setControl(new VoltageOut(SmartDashboard.getNumber("Launching launcher roller value", LAUNCHING_LAUNCHER_VOLTAGE)));
  }

  // A command factory to turn the spinUp method into a command that requires this
  // subsystem
  public Command spinUpCommand() {
    return this.run(() -> spinUp());
  }

  // A command factory to turn the launch method into a command that requires this
  // subsystem
  public Command launchCommand() {
    return this.run(() -> launch());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
