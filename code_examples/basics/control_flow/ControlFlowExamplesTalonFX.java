package frc.robot.examples;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * TalonFX (Phoenix 6) versions of code examples for the Control Flow tutorial page.
 * Not a deployable class — shows syntax patterns only.
 */
public class ControlFlowExamplesTalonFX {

    private static final double MAX_HEIGHT = 50.0;
    private static final double LOW_SETPOINT = 10.0;
    private static final double HIGH_SETPOINT = 40.0;
    private TalonFX motor = new TalonFX(5);

    public void limitSwitchExample(DigitalInput limitSwitch, TalonFX motor) {
        // --8<-- [start:if_else_limit_talon]
        if (limitSwitch.get()) {
            motor.setControl(new DutyCycleOut(0));      // stop if limit switch is triggered
        } else {
            motor.setControl(new DutyCycleOut(0.5));    // otherwise keep moving
        }
        // --8<-- [end:if_else_limit_talon]
    }

    public void elevatorPositionExample(double encoderPosition, TalonFX elevator) {
        // --8<-- [start:else_if_chain_talon]
        if (encoderPosition < LOW_SETPOINT) {
            elevator.setControl(new DutyCycleOut(0.3));
        } else if (encoderPosition > HIGH_SETPOINT) {
            elevator.setControl(new DutyCycleOut(-0.3));
        } else {
            elevator.setControl(new DutyCycleOut(0));    // within range — hold position
        }
        // --8<-- [end:else_if_chain_talon]
    }

    // --8<-- [start:soft_limit_method_talon]
    public void moveUp(double speed) {
        if (getHeight() < MAX_HEIGHT) {
            motor.setControl(new DutyCycleOut(speed));
        } else {
            motor.setControl(new DutyCycleOut(0));
        }
    }
    // --8<-- [end:soft_limit_method_talon]

    public void whileLoopExample(TalonFX motor) {
        // --8<-- [start:while_loop_talon]
        while (!atTarget()) {
            motor.setControl(new DutyCycleOut(0.5));
        }
        motor.setControl(new DutyCycleOut(0));
        // --8<-- [end:while_loop_talon]
    }

    public void forEachExample(TalonFX leftLeader, TalonFX leftFollower,
                               TalonFX rightLeader, TalonFX rightFollower) {
        // --8<-- [start:for_each_basic_talon]
        TalonFX[] motors = {leftLeader, leftFollower, rightLeader, rightFollower};
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.CurrentLimits.SupplyCurrentLimit = 40;   // cap current draw to 40 amps

        for (TalonFX motor : motors) {
            motor.getConfigurator().apply(config);
        }
        // --8<-- [end:for_each_basic_talon]
    }

    public void configureMotorsExample(TalonFX leftLeader, TalonFX leftFollower,
                                       TalonFX rightLeader, TalonFX rightFollower) {
        // --8<-- [start:frc_motor_config_talon]
        TalonFX[] motors = {leftLeader, leftFollower, rightLeader, rightFollower};
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.CurrentLimits.SupplyCurrentLimit = 40;           // CTRE: cap current draw to 40 amps
        config.CurrentLimits.SupplyCurrentLimitEnable = true;
        config.MotorOutput.NeutralMode = NeutralModeValue.Brake; // CTRE: hold position when not powered

        for (TalonFX motor : motors) {
            motor.getConfigurator().apply(config);
        }
        // --8<-- [end:frc_motor_config_talon]
    }

    public boolean atTarget() { return false; }
    private double getHeight() { return 0.0; }
}

// --8<-- [start:elevator_subsystem_talon]
public class ElevatorSubsystemTalonFX extends SubsystemBase {

    private final TalonFX motor;
    private static final double MAX_HEIGHT = 50.0;  // rotations
    private static final double MIN_HEIGHT = 0.0;

    public ElevatorSubsystemTalonFX() {
        motor = new TalonFX(5);
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        motor.getConfigurator().apply(config);
    }

    public void moveUp(double speed) {
        if (getHeight() < MAX_HEIGHT) {
            motor.setControl(new DutyCycleOut(speed));
        } else {
            motor.setControl(new DutyCycleOut(0));
        }
    }

    public void moveDown(double speed) {
        if (getHeight() > MIN_HEIGHT) {
            motor.setControl(new DutyCycleOut(-speed));
        } else {
            motor.setControl(new DutyCycleOut(0));
        }
    }

    public double getHeight() {
        return motor.getPosition().getValueAsDouble();
    }

    @Override
    public void periodic() {
        // Could log getHeight() to the dashboard here
    }
}
// --8<-- [end:elevator_subsystem_talon]
