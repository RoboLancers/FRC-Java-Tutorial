// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// --8<-- [start:limit-switch-import]
// --8<-- [end:limit-switch-import]

public class ShooterSubsystem extends SubsystemBase {

  // --8<-- [start:limit-switch-field]
  private final DigitalInput shooterLimitSwitch;
  // --8<-- [end:limit-switch-field]

  // --8<-- [start:limit-switch-constructor]
  public ShooterSubsystem() {
    // Create the limit switch on DIO port 0
    shooterLimitSwitch = new DigitalInput(0);
  }
  // --8<-- [end:limit-switch-constructor]

  // --8<-- [start:limit-switch-method]
  /**
   * Returns true if the shooter limit switch is pressed (closed).
   *
   * Handles inversion here in the subsystem so callers always get
   * the correct logical value without needing to know about hardware wiring.
   * This assumes a Normally Open (NO) switch.
   */
  public boolean isLimitSwitchPressed() {
    return !shooterLimitSwitch.get();
  }
  // --8<-- [end:limit-switch-method]

  /**
   * Spins the shooter flywheel.
   */
  public void spinUp() {
    // Flywheel logic here
  }

  /**
   * Stops the shooter.
   */
  public void stop() {
    // Stop logic here
  }

  /**
   * A command that waits for the limit switch to be pressed, then stops the shooter.
   * Useful for automatic shooter positioning.
   */
  public Command autoPositionCommand() {
    return this.runOnce(() -> {
      if (isLimitSwitchPressed()) {
        stop();
      }
    });
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
