package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.RelativeEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    private SparkMax motor;
    private SparkMax leftMotor;
    private SparkMax rightMotor;
    private RelativeEncoder encoder;
    private double targetPosition;

    // --8<-- [start:return_types]
    // Returns nothing — just sets the motor
    // --8<-- [start:declare_method]
    public void doSomething(parameter_type parameterName) {
        // Do things here.
    }
    // --8<-- [end:declare_method]

    // Returns a boolean — whether the encoder has reached the target
    public boolean atTarget() {
        return encoder.getPosition() >= targetPosition;
    }

    // Returns a double — the current motor output
    public double getSpeed() {
        return motor.get();
    }
    // --8<-- [end:return_types]

    // --8<-- [start:parameters]
    // No parameters
    public void stop() {
        motor.set(0);
    }

    // One parameter — the speed to set
    public void setSpeed(double speed) {
        motor.set(speed);
    }

    // Two parameters
    public void setLeftRight(double leftSpeed, double rightSpeed) {
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
    }
    // --8<-- [end:parameters]

    // --8<-- [start:stopAll]
    public void stopAll() {
        leftMotor.set(0);
        rightMotor.set(0);
    }
    // --8<-- [end:stopAll]

    // --8<-- [start:override_periodic]
    @Override
    public void periodic() {
        // This replaces the empty periodic() in SubsystemBase
        // and runs every robot loop cycle
    }
    // --8<-- [end:override_periodic]
}
