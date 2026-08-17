package frc.robot.examples;

/**
 * Code examples for the Variables and Data Types tutorial page.
 * This file is not part of a deployable robot project — it exists
 * to provide syntax-highlighted snippets for the tutorial site.
 */
public class TypesVariablesExamples {

    // --8<-- [start:public_static_final]
    public static final int LEFT_LEADER_ID = 1;
    // --8<-- [end:public_static_final]

    // --8<-- [start:naming_convention]
    public static final int MAX_SPEED = 100;   // constant
    // --8<-- [end:naming_convention]

    public void declareAndAssignExample() {
        // --8<-- [start:declare_assign]
        // Declare only — value is unset
        int motorPort;

        // Assign a value
        motorPort = 1;

        // Assign a new value later
        motorPort = 2;
        // --8<-- [end:declare_assign]
    }

    public void frcVariablesExample() {
        // --8<-- [start:frc_variables]
        double driveSpeed = 0.5;          // half speed
        boolean isShooterRunning = false;
        int leftMotorPort = 1;
        // --8<-- [end:frc_variables]
    }

    public void finalLocalExample() {
        // --8<-- [start:final_local]
        final int LEFT_MOTOR_PORT = 1;
        // --8<-- [end:final_local]
    }

    public void namingConventionLocalExample() {
        // --8<-- [start:naming_convention_local]
        double currentSpeed = 0.0;  // variable
        // --8<-- [end:naming_convention_local]
    }

    // --8<-- [start:comments]
    // Single line comment

    /*
       Multi-line comment —
       spans multiple lines
    */

    /**
     * Doc comment — appears when you hover over this item in VSCode.
     * Supports HTML formatting.
     */
    // --8<-- [end:comments]
}

// --8<-- [start:scope]
public class Drivetrain extends SubsystemBase {

    private double topSpeed = 1.0;  // field — available in all methods

    public void setSpeed(double speed) {
        double adjusted = speed * topSpeed;  // local — only exists in this method
        motor.set(adjusted);
    }
}
// --8<-- [end:scope]
