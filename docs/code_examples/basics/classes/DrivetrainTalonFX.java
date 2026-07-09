package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// --8<-- [start:fields_talon]
public class Drivetrain extends SubsystemBase {
    private TalonFX leftLeader;
    private TalonFX rightLeader;
    private double topSpeed;
}
// --8<-- [end:fields_talon]

// --8<-- [start:constructor_talon]
public class Drivetrain extends SubsystemBase {
    private TalonFX leftLeader;
    private TalonFX rightLeader;

    // Constructor
    public Drivetrain() {
        leftLeader  = new TalonFX(1);
        rightLeader = new TalonFX(2);
    }
}
// --8<-- [end:constructor_talon]

// --8<-- [start:new_keyword_talon]
Drivetrain myDrivetrain = new Drivetrain();
TalonFX motor = new TalonFX(1);
// --8<-- [end:new_keyword_talon]

// --8<-- [start:putting_it_together_talon]
package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    private final TalonFX leftLeader;
    private final TalonFX rightLeader;

    public Drivetrain() {
        leftLeader  = new TalonFX(1);
        rightLeader = new TalonFX(2);
    }

    public void setSpeed(double speed) {
        leftLeader.setControl(new DutyCycleOut(speed));
        rightLeader.setControl(new DutyCycleOut(speed));
    }

    @Override
    public void periodic() {
        // Update dashboard values here if needed
    }
}
// --8<-- [end:putting_it_together_talon]
