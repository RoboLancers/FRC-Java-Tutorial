// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// --8<-- [start:pid-fields]
public class PIDElevatorExample extends SubsystemBase {
    private final SparkMax motor;
    private final RelativeEncoder encoder;
    private final PIDController pidController;
    private final ElevatorFeedforward feedforward;
// --8<-- [end:pid-fields]

    // --8<-- [start:pid-constructor]
    public PIDElevatorExample() {
        motor = new SparkMax(1, SparkMax.MotorType.kBrushless);

        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(40);
        motor.configure(config, null, null);

        encoder = motor.getEncoder();
        // Converts motor rotations to inches (10:1 gear ratio, 1.5" spool radius)
        encoder.setPositionConversionFactor((1.0 / 10.0) * (2 * Math.PI * 1.5));

        // kP=1.0 V/inch, kI=0.0, kD=0.05 V·s/inch — tune these on your robot
        pidController = new PIDController(1.0, 0.0, 0.05);
        pidController.setTolerance(0.5); // within 0.5 inches counts as "at setpoint"

        // kS=static friction (V), kG=gravity hold (V), kV=velocity gain (V·s/in)
        feedforward = new ElevatorFeedforward(0.1, 0.5, 0.0);
    }
    // --8<-- [end:pid-constructor]

    // --8<-- [start:pid-setpoint]
    public void setHeight(double heightInches) {
        pidController.setSetpoint(heightInches);
    }
    // --8<-- [end:pid-setpoint]

    // --8<-- [start:pid-at-setpoint]
    public boolean atSetpoint() {
        return pidController.atSetpoint();
    }
    // --8<-- [end:pid-at-setpoint]

    // --8<-- [start:pid-periodic]
    @Override
    public void periodic() {
        double currentPosition = encoder.getPosition();

        // PID correction: pulls the elevator toward the setpoint
        double pidVolts = pidController.calculate(currentPosition);

        // Feedforward: holds the elevator against gravity at any position
        double ffVolts = feedforward.calculate(0);

        // Clamp total output to safe motor voltage range
        double totalVolts = MathUtil.clamp(pidVolts + ffVolts, -12.0, 12.0);
        motor.setVoltage(totalVolts);

        SmartDashboard.putNumber("Elevator/Position (in)", currentPosition);
        SmartDashboard.putNumber("Elevator/Setpoint (in)", pidController.getSetpoint());
        SmartDashboard.putBoolean("Elevator/At Setpoint", pidController.atSetpoint());
    }
    // --8<-- [end:pid-periodic]

    // --8<-- [start:pid-command]
    public Command goToHeightCommand(double heightInches) {
        return this.run(() -> setHeight(heightInches))
                .until(this::atSetpoint);
    }
    // --8<-- [end:pid-command]
}
