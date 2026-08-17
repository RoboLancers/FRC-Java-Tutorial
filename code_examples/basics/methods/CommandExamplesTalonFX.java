package frc.robot.examples;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import frc.robot.subsystems.DrivetrainTalonFX;

public class CommandExamplesTalonFX {

    private TalonFX leftMotor;
    private TalonFX rightMotor;
    private DrivetrainTalonFX m_drivetrain;

    // --8<-- [start:methods_calling_methods_talon]
    // A subsystem method that calls motor methods
    public void stop() {
        leftMotor.setControl(new DutyCycleOut(0));
        rightMotor.setControl(new DutyCycleOut(0));
    }

    // A command calling a subsystem method
    protected void execute() {
        m_drivetrain.stop();
    }
    // --8<-- [end:methods_calling_methods_talon]
}
