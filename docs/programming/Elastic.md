# Using Elastic

<!-- ![Image Title](imageURL)  -->

## Overview

In this section we will be going over

1. Using and organizing the Elastic dashboard
2. Creating the Telemetry subsystem and adding buttons and data to be viewed in Elastic

***

## What is Elastic

- **Elastic** is a modern FRC robot dashboard that displays robot data during development and competition
- It can display widgets like graphs, camera streams, boolean indicators, and number readouts
- Elastic supports **tabs** to organize widgets into separate pages
- It reads data from **NetworkTables**, which is the same system used by SmartDashboard — no changes to robot code are needed to switch from Shuffleboard or SmartDashboard to Elastic

!!! tip
    Elastic is available as a standalone download from [its GitHub releases page](https://github.com/Gold872/elastic-dashboard/releases){target=_blank}. It can be run independently or configured to launch automatically from the Driver Station. It is also included in the WPILib installation and can be found in the WPILib tools directory.

## What is Telemetry

- **Telemetry** is where we add data to be viewed or command buttons on **Elastic**
- For this section of our tutorial we will be adding switch and encoder data to **Elastic**
- The Java code to publish telemetry data uses `SmartDashboard`, which writes to NetworkTables — Elastic reads that data automatically

## Creating the Telemetry Subsystem

!!! abstract ""
    **1)** Create a new **Subsystem** called **Telemetry**

!!! abstract ""
    **2)** Create a constructor for the **Telemetry** class

    - The constructor is where we will create buttons for Elastic

!!! abstract ""
    **3)** Inside type:

    ```java
    SmartDashboard.putData("Reset Drive Encoder", new DriveResetEncoder());
    ```

!!! abstract ""
    **4)** Create a public method called update

    - This method will run periodically in Robot.java to update sensor data in Elastic

!!! abstract ""
    **5)** Inside type:

    ```java
    SmartDashboard.putNumber("Drivetrain Encoder Count", Robot.m_drivetrain.getDriveEncoderCount());
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
    - It can be added before or after **OI** since they don't use methods from each other

    !!! note "Why not robotPeriodic?"
        `robotPeriodic` runs at the very start of every loop, *before* the command scheduler runs. Putting `update()` there means the displayed values would lag one loop cycle behind the current robot state. By placing `update()` in mode-specific methods (`disabledPeriodic`, `autonomousPeriodic`, `teleopPeriodic`), the dashboard always reflects the most current values after all commands for that loop have executed.

!!! abstract ""

    **2)** It is **important** that we add the **update** method to **disabledPeriodic, autonomousPeriodic**, and **teleopPeriodic** so that **Elastic** is always being updated with information on our sensors.

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

## Opening Elastic

After saving and deploying your code:

**Option A — Launch Elastic manually:**

1. Open the Elastic application directly.
2. Elastic will automatically connect to the robot's NetworkTables server when the Driver Station connects to the robot.

**Option B — Configure the Driver Station to open Elastic automatically:**

1. In the Driver Station, click the **gear icon** on the left side to open settings.
2. Under **Dashboard Type**, select **Elastic** (or browse to the Elastic executable if it does not appear in the list).
3. The Driver Station will launch Elastic automatically when it starts.

Once Elastic is open and the robot is connected, you should see your NetworkTables keys appear in the widget picker.

## Using Elastic

### Adding Widgets

- **Right-click** on any empty area of a tab to open the widget menu.
- Browse or search the list of available NetworkTables keys (the values your robot is publishing via `SmartDashboard`).
- Click a key to place it as a widget on the tab.

!!! tip
    Each key published with `SmartDashboard.putData`, `putNumber`, or `putBoolean` will appear as a separate widget option. Commands registered with `putData` appear as buttons you can click to schedule them.

### Tabs

Tabs let you separate widgets into logical groups — for example one tab for drivetrain data and another for shooter data.

- Click the **+** button in the tab bar to add a new tab.
- Double-click a tab name to rename it.
- Click a tab to switch to it.

### Moving and Resizing Widgets

- Click and drag a widget's **title bar** to reposition it.
- Drag from a widget's **corner or edge** to resize it.
- Right-click a widget for additional options such as renaming or changing its display type.

### Saving Layouts

Elastic saves your layout automatically, but you can also explicitly save and load layout files to share with the team.

- Go to **File → Save Layout** to save the current arrangement to a `.json` file.
- Go to **File → Open Layout** to restore a previously saved layout.

!!! tip
    Save your layout file in your robot project's repository so the whole team can use the same dashboard configuration.
