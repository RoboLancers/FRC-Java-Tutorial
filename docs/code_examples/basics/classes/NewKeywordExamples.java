package frc.robot.examples;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.subsystems.Drivetrain;

/**
 * Examples showing the new keyword creating objects from classes.
 */
public class NewKeywordExamples {

    public void createObjects() {
        // --8<-- [start:new_keyword]
        Drivetrain myDrivetrain = new Drivetrain();
        SparkMax motor = new SparkMax(1, MotorType.kBrushless);
        // --8<-- [end:new_keyword]
    }
}
