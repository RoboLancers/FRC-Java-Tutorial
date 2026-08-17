package frc.robot.examples;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Code examples for the Control Flow tutorial page.
 * Not a deployable class — shows syntax patterns only.
 */
public class ControlFlowExamples {

    private static final double MAX_HEIGHT = 50.0;
    private static final double LOW_SETPOINT = 10.0;
    private static final double HIGH_SETPOINT = 40.0;
    private SparkMax motor = new SparkMax(5, MotorType.kBrushless);

    public void clampSpeedExample(double speed) {
        // --8<-- [start:if_clamp]
        if (speed > 1.0) {
            speed = 1.0;  // clamp to maximum
        }
        // --8<-- [end:if_clamp]
    }

    public void limitSwitchExample(DigitalInput limitSwitch, SparkMax motor) {
        // --8<-- [start:if_else_limit]
        if (limitSwitch.get()) {
            motor.set(0);       // stop if limit switch is triggered
        } else {
            motor.set(0.5);     // otherwise keep moving
        }
        // --8<-- [end:if_else_limit]
    }

    public void elevatorPositionExample(double encoderPosition, SparkMax elevator) {
        // --8<-- [start:else_if_chain]
        if (encoderPosition < LOW_SETPOINT) {
            elevator.set(0.3);
        } else if (encoderPosition > HIGH_SETPOINT) {
            elevator.set(-0.3);
        } else {
            elevator.set(0);    // within range — hold position
        }
        // --8<-- [end:else_if_chain]
    }

    // --8<-- [start:soft_limit_method]
    public void moveUp(double speed) {
        if (getHeight() < MAX_HEIGHT) {
            motor.set(speed);
        } else {
            motor.set(0);
        }
    }
    // --8<-- [end:soft_limit_method]

    public void whileLoopExample(SparkMax motor) {
        // --8<-- [start:while_loop]
        while (!atTarget()) {
            motor.set(0.5);
        }
        motor.set(0);
        // --8<-- [end:while_loop]
    }

    public void countingLoopExample() {
        // --8<-- [start:for_counting]
        for (int i = 0; i < 5; i++) {
            System.out.println("Iteration: " + i);
        }
        // --8<-- [end:for_counting]
    }

    public void forEachExample(SparkMax leftLeader, SparkMax leftFollower,
                               SparkMax rightLeader, SparkMax rightFollower) {
        // --8<-- [start:for_each_basic]
        SparkMax[] motors = {leftLeader, leftFollower, rightLeader, rightFollower};

        for (SparkMax motor : motors) {
            motor.setSmartCurrentLimit(40);
        }
        // --8<-- [end:for_each_basic]
    }

    public void configureMotorsExample(SparkMax leftLeader, SparkMax leftFollower,
                                       SparkMax rightLeader, SparkMax rightFollower) {
        // --8<-- [start:frc_motor_config]
        SparkMax[] motors = {leftLeader, leftFollower, rightLeader, rightFollower};

        for (SparkMax motor : motors) {
            motor.setSmartCurrentLimit(40);       // REV API: cap current draw to 40 amps
            motor.setIdleMode(IdleMode.kBrake);   // REV API: hold position when not powered
        }
        // --8<-- [end:frc_motor_config]
    }

    // Helper methods referenced in examples above
    public boolean atTarget() { return false; }
    private double getHeight() { return 0.0; }
}

// --8<-- [start:elevator_subsystem]
public class ElevatorSubsystem extends SubsystemBase {

    private final SparkMax motor;
    private static final double MAX_HEIGHT = 50.0;  // encoder rotations
    private static final double MIN_HEIGHT = 0.0;

    public ElevatorSubsystem() {
        motor = new SparkMax(5, MotorType.kBrushless);
    }

    public void moveUp(double speed) {
        if (getHeight() < MAX_HEIGHT) {
            motor.set(speed);
        } else {
            motor.set(0);
        }
    }

    public void moveDown(double speed) {
        if (getHeight() > MIN_HEIGHT) {
            motor.set(-speed);
        } else {
            motor.set(0);
        }
    }

    public double getHeight() {
        return motor.getEncoder().getPosition();
    }

    @Override
    public void periodic() {
        // Could log getHeight() to the dashboard here
    }
}
// --8<-- [end:elevator_subsystem]

public class IsFinishedExample extends Command {

    private ControlFlowExamples m_subsystem;

    public IsFinishedExample(ControlFlowExamples subsystem) {
        m_subsystem = subsystem;
    }

    // --8<-- [start:is_finished]
    @Override
    public boolean isFinished() {
        return m_subsystem.atTarget();
    }
    // --8<-- [end:is_finished]
}
