package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// --8<-- [start:class_skeleton]
public class Drivetrain extends SubsystemBase {
    // fields and methods go here
}
// --8<-- [end:class_skeleton]

// --8<-- [start:inheritance]
public class Drivetrain extends SubsystemBase {
    // Drivetrain automatically has everything SubsystemBase provides
}
// --8<-- [end:inheritance]

// --8<-- [start:fields]
public class Drivetrain extends SubsystemBase {
    private SparkMax leftLeader;
    private SparkMax rightLeader;
    private double topSpeed;
}
// --8<-- [end:fields]

// --8<-- [start:constructor]
public class Drivetrain extends SubsystemBase {
    private SparkMax leftLeader;
    private SparkMax rightLeader;

    // Constructor
    public Drivetrain() {
        leftLeader  = new SparkMax(1, MotorType.kBrushless);
        rightLeader = new SparkMax(2, MotorType.kBrushless);
    }
}
// --8<-- [end:constructor]

// --8<-- [start:this_keyword]
public class Drivetrain extends SubsystemBase {
    private double topSpeed;

    public Drivetrain(double topSpeed) {
        this.topSpeed = topSpeed;  // "this.topSpeed" is the field; "topSpeed" alone is the parameter
    }
}
// --8<-- [end:this_keyword]

public class Drivetrain extends SubsystemBase {

    // --8<-- [start:override_periodic]
    @Override
    public void periodic() {
        // Replaces the empty version in SubsystemBase;
        // runs every robot loop cycle
    }
    // --8<-- [end:override_periodic]

    // --8<-- [start:super_periodic]
    @Override
    public void periodic() {
        super.periodic();  // runs the parent version (if it does anything)
        // then add your own code here
    }
    // --8<-- [end:super_periodic]
}

// --8<-- [start:putting_it_together]
package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    private final SparkMax leftLeader;
    private final SparkMax rightLeader;

    public Drivetrain() {
        leftLeader  = new SparkMax(1, MotorType.kBrushless);
        rightLeader = new SparkMax(2, MotorType.kBrushless);
    }

    public void setSpeed(double speed) {
        leftLeader.set(speed);
        rightLeader.set(speed);
    }

    @Override
    public void periodic() {
        // Update dashboard values here if needed
    }
}
// --8<-- [end:putting_it_together]
