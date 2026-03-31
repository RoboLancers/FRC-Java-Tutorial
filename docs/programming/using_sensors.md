# [WIP] Using Sensors and Switches

<!-- ![Image Title](imageURL)  -->

## Overview 

In this section we will be going over

1. Creating and using a switch in a shooter subsystem
      1. Create this subsystem now. (See [Creating a New Subsystem](new_project.md#creating-a-new-subsystem))
2. Creating an encoder in the [drivetrain subsystem](driving_robot.md) (See creating a driving robot)

***

## What Are Sensors

- There are different types of sensors that give feedback on different things (i.e. **Encoders** measure distance, **switches** detect contact, **gyros** give orientation).
- Most of these interface with the roboRIO through either the DIO, analog input, or custom electronics port.

## Programming Switches (i.e. Limit Switches)

!!! abstract ""
    **1)** For this tutorial we are going to add a **switch** to a **shooter subsystem** to automatically change the pitch of the shooter
    
    - Inside the shooter subsystem we are going to create a **switch** called **shooterSwitch**
    - It will be created as a **DigitalInput**
    - The **DigitalInput** constructor only takes 1 parameter - DigitalInput(port)
    - The port refers to the port numbers on the RoboRIO’s DIO
    - Store the port in **Constants**
    
??? Example

	The code you typed should be this
	
	```java
	DigitalInput shooterSwitch = null;
	```
	   
	In the constructor
	
	```java
	shooterSwitch = new DigitalInput(Constants.SHOOTER_SWITCH);
	```
	   
	In **Constants.Java**
	
	```java
	// Digital Inputs
  	public static final int SHOOTER_SWITCH = 0;
    ```

### Creating isShooterSwitchClosed Method

!!! abstract ""
    **1)** Create a **public boolean** method called **isShooterSwitchClosed**
    
    - This method will tell us when the shooter switch is pressed

!!! abstract ""
    **2)** Inside type:
   
    ```java
    return shooterSwitch.get();
    ```

   - Switches have 2 states: open and closed.
   - Make sure you know which is true or false or you may have to invert the switch by rewiring or using the `!` operator

!!! note "Normally Open vs Normally Closed"
    Switches come in two configurations:

    - **Normally Open (NO)** — the circuit is open (disconnected) when the switch is not pressed. `DigitalInput.get()` returns `true` when unpressed.
    - **Normally Closed (NC)** — the circuit is closed (connected) when the switch is not pressed. `DigitalInput.get()` returns `false` when unpressed.

    **Recommendation:** Use a Normally Open switch and invert the reading in code: `return !shooterSwitch.get();`. This way, if the switch wiring becomes disconnected, the robot reads the sensor as "not pressed" (`false`) instead of "pressed" (`true`). A disconnected sensor should never accidentally trigger an action.

!!! tip "Keep inversions in the subsystem"
    Always handle sensor inversions inside the subsystem method, not in commands or other classes. This way every caller gets the correct logical value without needing to know about the hardware wiring. For example, use `return !shooterSwitch.get();` here rather than inverting in each command that uses the switch.
   
??? Example
	
	Your **isShooterSwitchClosed()** should look like this
	
	```java
	public boolean isShooterSwitchClosed() {
   	   return shooterSwitch.get();
  	}
	```

??? Example "Full Shooter.java Example"

    ```java
    package frc.robot.subsystems;

    import edu.wpi.first.wpilibj.DigitalInput;
    import edu.wpi.first.wpilibj2.command.SubsystemBase;
    import frc.robot.Constants;

    public class Shooter extends SubsystemBase {
      /**
       * Creates a new Shooter.
       */
      DigitalInput shooterSwitch = null;

      public Shooter() {
        shooterSwitch = new DigitalInput(Constants.SHOOTER_SWITCH);
      }

      public boolean isShooterSwitchClosed() {
        return shooterSwitch.get();
      }

      @Override
      public void periodic() {
        // This method will be called once per scheduler run
      }
    }
    ```  

### Creating ShooterUpAuto Command

- We will create a **command** that gives an example of how a Shooter switch may be used

!!! abstract ""
     **1)** For this tutorial we will use the switch to create a button that automatically pitches the shooter up after the switch is pressed

!!! abstract ""
    **2)** Create a new **command** called **ShooterUpAuto**
   
!!! abstract "" 
    **3)** In the constructor add requires(Robot.m_Shooter)
    
!!! abstract ""
    **4)** In isFinished return our **isShooterSwitchClosed** method
    
    - we will not put anything in initialize or execute because we don't want anything to happen until the switch is closed
    
!!! abstract "" 
    **5)** In end add our **pitchUp** method
     
    - we will not put end in interrupted either because we only want to change the pitch of the shooter if the switch is closed
    
??? Example

    Your full **ShooterUpAuto.java** should look like this
    
    ```java
    package frc.robot.commands;

    import edu.wpi.first.wpilibj.command.Command;
    import frc.robot.Robot;

    public class ShooterUpAuto extends Command {
      public ShooterUpAuto() {
      // Use requires() here to declare subsystem dependencies
      // eg. requires(chassis);
      requires(Robot.m_shooter);
      }

      // Called just before this Command runs the first time
      @Override
      protected void initialize() {
      }

      // Called repeatedly when this Command is scheduled to run
      @Override
      protected void execute() {
      }

      // Make this return true when this Command no longer needs to run execute()
      @Override
      protected boolean isFinished() {
        return Robot.m_shooter.isShooterSwitchClosed();
      }

      // Called once after isFinished returns true
      @Override
      protected void end() {
        Robot.m_shooter.pitchUp();
      }

      // Called when another command which requires one or more of the same
      // subsystems is scheduled to run
      @Override
      protected void interrupted() {
      }
    }
    ```

#### Mapping ShooterAutoUpCommand
	
- To be able to test our command right now we can map it to a joystick button like we did our other Shooter commands
- It would be best to make it a whenPressed or whileHeld button
   - whileHeld will run normally while the button is being held and be interrupted when released
   
??? Example

	The code you typed should be this
	
	```java
	D3.whenPressed(new ShooterUpAuto());
    ```
	   
	Or this
	
	```java
	D3.whileHeld(new ShooterUpAuto());
	```
	   
## Programming Encoders

!!! abstract ""
    **1)** For this tutorial we are going to add a **encoder** to the **Drivetrain** subsystem to keep track of the distance the robot has driven
    
!!! abstract ""
    **2)** Inside the **Drivetrain** subsystem we are going to create an **encoder** called **driveEncoder**
    
    - It will be created as an Encoder
    - The Encoder constructor takes 2 parameters - (new Encoder(port1,port2))
    - These are DIO ports on the RoboRIO
    - Store ports in Constants as DRIVE_ENCODER_A and B
    
??? Example 

	The code you typed outside the constructor should be this
	
	```java
	Encoder driveEncoder = null;
	```
	   
	Inside the constructor
	
	```java
	driveEncoder = new Encoder(Constants.DRIVETRAIN_ENCODER_A, Constants.DRIVETRAIN_ENCODER_B);
	```
	   
### Creating Drive Encoder Methods

!!! abstract ""
    **1)** Create a public double method called **getDriveEncoderCount**
    
!!! abstract ""
    **2)** Inside type: 
    
    ```java
    return driveEncoder.get();
    ```
       
    !!! note
        Even though `driveEncoder.get()` returns an `int`, we declare `getDriveEncoderCount()` as returning a `double`. This avoids integer division later — for example, dividing by a `double` preference value like `driveEncoderCountsPerFoot` without a manual cast, so the result is always a precise decimal rather than a truncated whole number.

    - Encoders will return counts as an int
    - Depending which direction the encoder shaft rotates the value will increase or decrease
    
!!! abstract ""
    **3)** Create a public method called **resetDriveEncoderCount**
    
!!! abstract ""
    **4)** Inside type:
    
    ```java
    driveEncoder.reset();
    ```
    
    - This method will reset the drive encoder to zero which is useful for autonomous or when we use the robot as a ruler
 
??? Example
	
	The code you typed should be this
	
	```java
	public double getDriveEncoderCount() {
    	  return driveEncoder.get();
  	}

  	public void resetDriveEncoder() {
    	  driveEncoder.reset();
  	}
	```
	   
### Creating ResetDriveEncoder InstantCommand

- We need to create a command to use the **resetDriveEncoder** method since it’s a **void** method
- We will create a **InstantCommand** since we will only use it to reset the drive encoder

!!! abstract ""
    **1)** Create a new **InstantCommand** called **DriveResetEncoder**
    
!!! abstract ""
    **2)** In the constructor add requires(Robot.m_drivetrain)
    
!!! abstract ""
    **3)** In initialize() add our **resetDriveEncoder** method
    
??? Example

	Your full **DriveResetEncoder** command should look like this
	
	```java
	package frc.robot.commands;

	import edu.wpi.first.wpilibj.command.InstantCommand;
	import frc.robot.Robot;

	/**
 	* Add your docs here.
 	*/
	public class DriveResetEncoder extends InstantCommand {
  	/**
   	* Add your docs here.
   	*/
  	 public DriveResetEncoder() {
    	   super();
    	   // Use requires() here to declare subsystem dependencies
    	   // eg. requires(chassis);
    	   requires(Robot.m_drivetrain);
  	 }

  	 // Called once when the command executes
  	 @Override
  	 protected void initialize() {
    	   Robot.m_drivetrain.resetDriveEncoder();
  	 }
	}
    ```
