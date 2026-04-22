package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DrivetrainTalonFX extends SubsystemBase {

    private TalonFX motor;
    private TalonFX leftMotor;
    private TalonFX rightMotor;
    private double targetPosition;

    // --8<-- [start:return_types_talon]
    // Returns nothing — just sets the motor
    // --8<-- [start:declare_method_talon]
    public void doSomething(parameter_type parameterName) {
        // Do things here.
    }
    // --8<-- [end:declare_method_talon]

    // Returns a boolean — whether the encoder has reached the target
    public boolean atTarget() {
        return motor.getPosition().getValueAsDouble() >= targetPosition;
    }

    // Returns a double — the current motor output
    public double getSpeed() {
        return motor.getDutyCycle().getValueAsDouble();
    }
    // --8<-- [end:return_types_talon]

    // --8<-- [start:parameters_talon]
    // No parameters
    public void stop() {
        motor.setControl(new DutyCycleOut(0));
    }

    // One parameter — the speed to set
    public void setSpeed(double speed) {
        motor.setControl(new DutyCycleOut(speed));
    }

    // Two parameters
    public void setLeftRight(double leftSpeed, double rightSpeed) {
        leftMotor.setControl(new DutyCycleOut(leftSpeed));
        rightMotor.setControl(new DutyCycleOut(rightSpeed));
    }
    // --8<-- [end:parameters_talon]

    // --8<-- [start:override_periodic_talon]
    @Override
    public void periodic() {
        // This replaces the empty periodic() in SubsystemBase
        // and runs every robot loop cycle
    }
    // --8<-- [end:override_periodic_talon]
}
