# [WIP] Using Shuffleboard

<!-- ![Image Title](imageURL)  -->

## Overview

In this section we will be going over

1. Using and organizing the Shuffleboard
2. Creating the Telemetry subsystem and adding buttons and data to be viewed in Shuffleboard

***

## What is Shuffleboard

- **Shuffleboard** is one of the boards the driverstation displays robot data with
- It can have widgets like graphs, camera streams, and meters
- Unique to **shuffleboard** is the ability to have tabs for different boards

## What is Telemetry

- **Telemetry** is where we add data to be viewed or command buttons on **shuffleboard** or **smartdashboard** 
- For this section of our tutorial we will be adding switch and encoder data to **shuffleboard**

## Creating the Telemetry Subsystem

!!! abstract ""
    **1)** Create a new **Subsystem** called **Telemetry**
    
!!! abstract ""
    **2)** Create a constructor for the **Telemetry** class
    
    - The constructor is where we will create buttons for shuffleboard
    
!!! abstract ""
    **3)** Inside type: 
    
    ```java
    SmartDashboard.putData(“Reset Drive Encoder”, new DriveResetEncoder());
    ```
       
!!! abstract ""
    **4)** Create a public method called update
    
    - This method will run periodically in Robot.java to update sensor data on shuffleboard
    
!!! abstract "" 
    **5)** Inside type: 
    
    ```java 
    SmartDashboard.putNumber(“Drivetrain Encoder Count”, Robot.m_drivetrain.getDriveEncoderCount());
    ```
       
!!! abstract ""
    **6)** Do the same for the **getDriveEncoderDistance** method
    
!!! abstract ""
    **7)** Try adding the **Shooter** Subsystem commands and sensor methods where they should be
    
??? Example 

	Your full **Telemetry.java** should look like this
	
	```java
	package frc.robot.subsystems;

	import edu.wpi.first.wpilibj.command.Subsystem;
	import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
	import frc.robot.Robot;
	import frc.robot.commands.*;

	/**
 	* Add your docs here.
 	*/
	public class Telemetry extends Subsystem {
  	  // Put methods for controlling this subsystem
  	  // here. Call these from Commands.

  	  public Telemetry() {
    	    // Drivetrain
    	    SmartDashboard.putData("Reset Drive Encoder", new DriveResetEncoder());

    	    // Shooter
   	    SmartDashboard.putData("Shooter Up", new ShooterUp());
    	    SmartDashboard.putData("Shooter Down", new ShooterDown());
    	    SmartDashboard.putData("Shooter Up Auto", new ShooterUpAuto());
  	  }

  	  public void update() {
    	    // Drivetrain
    	    SmartDashboard.putNumber("Drive Encoder Count", Robot.m_drivetrain.getDriveEncoderCount());

    	    // Shooter
    	    SmartDashboard.putBoolean("Shooter Switch", Robot.m_shooter.isShooterSwitchClosed());
  	  }

  	  @Override
  	  public void initDefaultCommand() {
    	    // Set the default command for a subsystem here.
    	    // setDefaultCommand(new MySpecialCommand());
  	  }
	}
  	```
	   
## Adding The Telemetry Subsystem to Robot.java

!!! abstract ""
    **1)** When adding **Telemetry** to **Robot.java**, in **robotInit** we must add **Telemetry** after the other subsystems
  
    - This is because the **Telemetry** subsystem relies on methods that are created in other subsystems before it
    - It can be added before or after **OI** since they don’t use methods from each other

    !!! note "Why not robotPeriodic?"
        `robotPeriodic` runs at the very start of every loop, *before* the command scheduler runs. Putting `update()` there means the displayed values would lag one loop cycle behind the current robot state. By placing `update()` in mode-specific methods (`disabledPeriodic`, `autonomousPeriodic`, `teleopPeriodic`), the dashboard always reflects the most current values after all commands for that loop have executed.

!!! abstract ""

    **2)** It is **important** that we add the **update** method to **disabledPeriodic, autonomousPeriodic**, and **teleopPeriodic** so that the **Shuffleboard** is always being updated with information on our sensors.

??? Example

	The code you typed before **robotInit** should be this
	
	```java
	public static Telemetry m_telemetry; 
	```
	  
	The code you typed in **robotInit** should be this
	
	```java
	m_telemetry = new Telemetry(); //This must be initialized after all other robot subsystems
	```
	   
	The code you typed in **disabledPeriodic, autonomousPeriodic**, and **teleopPeriodic** should be this
	
	```java
	Robot.m_telemetry.update();
	```
	   
## Testing Shuffleboard

<!-- TODO: Add pictures for this section -->

- After saving and deploying code, open the driver station
- Click the gear on the left side and configure your team number and set the dashboard type to “ShuffleBoard”
- If you are still connected to the robot you should see boxes for the buttons and data we added in Telemetry

## Using Shuffleboard

Shuffleboard has several features that help you organize and save your dashboard layout.

### Tabs

Tabs let you separate widgets into logical groups — for example one tab for drivetrain data and another for shooter data.

- Click the **+** button in the top-right corner of Shuffleboard to add a new tab.
- Double-click a tab name to rename it.
- You can drag widgets between tabs by selecting them and using cut/paste, or by dragging them to the tab bar.

### Moving and Resizing Widgets

- Click and drag a widget to reposition it on the grid.
- Hover over the bottom-right corner of a widget until a resize cursor appears, then drag to resize it.
- Right-click a widget to access options such as changing the widget type (e.g. switch between a number display and a graph).

### Grouping Widgets

- You can place related widgets near each other and use a **Layout** container to visually group them.
- Right-click on an empty area of the tab and select **Add Layout** to add a List Layout or Grid Layout container.
- Drag widgets into the layout container to group them.

### Saving Layouts

Shuffleboard saves your layout automatically when you close it, but you can also save a named layout file to restore later.

- Go to **File → Save Layout** to save the current arrangement to a `.json` file.
- Go to **File → Load Layout** to restore a previously saved layout.

!!! tip
    Save your layout to a file in your robot project's repository so that the whole team can use the same dashboard configuration.
