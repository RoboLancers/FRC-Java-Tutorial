# Creating an Autonomous Command

<!-- ![Image Title](imageURL)  -->

## Overview

In this section we will be going over:

1. Creating an autonomous command group
2. Using RobotPreferences to quickly change our autonomous values
3. Using an encoder to autonomously drive
4. Using WaitCommand to pace our commands in autonomous

<!-- TODO: Implement and revamp autonomous status code from robot2018/2019? -->
<!-- 5. Creating an autonomous status readout in shuffleboard to aid in debugging autonomous -->

***

## What Is an Autonomous Command

- An autonomous command is a command that is ran during "autonomous mode" under the **autonomousInit** method in **Robot.java** 
- It could be a single **command** or a **command group**
- It's especially helpful to have if you don't have any cameras to drive the robot during autonomous (rare, but does happen)
- For this tutorial we will create a simple autonomous **command ** that makes the robot drive forward slightly.
	
## Creating Commands For Autonomous

- Since we can't control our robot during an autonomous command we will want to create commands that allow the robot to move independently of a driver

## Creating a basic Autonomous Command 

!!! abstract ""
    **1)** Create a new command called **DriveDistance** using the `create new class/command` feature in Vscode.

!!! abstract ""
    **2)** Before the constructor create a **final double** called **distance**
	```java title="distance field"
	--8<-- "docs/code_examples/basics/autonomous/DriveDistance.java:distance-field"
	```
    
    - We will use this to tell the command to finish when the robot drives the inputted distance
    
!!! abstract ""
    **3)** In the **DriveDistance** constructor add a **Drivetrain** parameter called **drivetrain**
    
!!! abstract ""
    **4)** Inside the constructor type:
    
	```java title="Constructor body"
	--8<-- "docs/code_examples/basics/autonomous/DriveDistance.java:constructor-body"
	```
    
    - **addRequirements** tells the scheduler this command needs the **Drivetrain**, so no other command using the drivetrain can run at the same time
        
!!! abstract ""
    **5)** In **initialize** add our **resetDriveEncoder** method
    
    - We want to reset the encoder before we drive so that it counts the distance from zero
    
!!! abstract ""
    **6)** In **execute** add our **arcadeDrive** method and change the **moveSpeed** parameter to a **RobotPreference** named **driveDistanceSpeed** and **rotateSpeed** to 0.0
    
    - We only want to drive the robot forward; a **RobotPreference** will help us tune the drive speed
    
!!! abstract ""
    **7)** In **isFinished** type:
    
	```java title="isFinished() body"
	--8<-- "docs/code_examples/basics/autonomous/DriveDistance.java:is-finished-body"
	```
!!! abstract ""
    **8)** In **end(boolean interrupted)** stop the **Drivetrain**
    
    - Command v2 merges the old **end** and **interrupted** methods into a single `end(boolean interrupted)` method that always runs when the command finishes, whether it finished normally or was interrupted by another command
    
??? Example
    
	Your full **DriveDistance.java** should look like this
	
	```java title="DriveDistance.java"
	--8<-- "docs/code_examples/basics/autonomous/DriveDistance.java:full-example"
	```
		   
	The code you typed in **RobotPreferences.java** should be this
	
	```java title="RobotPreferences.java"
	--8<-- "docs/code_examples/basics/autonomous/RobotPreferences.java:drive-distance-speed"
	```

## Creating The Autonomous Command

- We will create an **Autonomous command group** with the **DriveDistance** command and the **ShooterUp** command

!!! abstract ""
    **1)** Create a new class named **Autonomous** that extends `SequentialCommandGroup`
	
!!! abstract ""
 	**2)** Give the constructor a **Drivetrain** parameter called **drivetrain**

!!! abstract ""
 	**3)** In the constructor body call **addCommands**, passing in `new DriveDistance(drivetrain, RobotPreferences.autoDriveDistance())` followed by `new ShooterUp()`
	
	- **addCommands** runs the commands passed to it one at a time, in order, waiting for each one to finish before starting the next

## Adding a Delay Between Commands

- In order to pace the commands in our **command group** we need something that runs in between **DriveDistance** and **ShooterUp** and simply waits
- Command v2 already includes a command for this — `WaitCommand` — so there's no need to hand-write our own delay command like older WPILib tutorials did

!!! abstract ""
	**1)** In **Autonomous.java** import `edu.wpi.first.wpilibj2.command.WaitCommand`
	
!!! abstract ""
	**2)** In **addCommands**, add a **WaitCommand** between **DriveDistance** and **ShooterUp**, giving it a **RobotPreference** called **autoDelay**
	
	- `WaitCommand(seconds)` finishes on its own after the given number of seconds without blocking the rest of the robot code, unlike a regular `Thread.sleep`

??? Example 

	Your full **Autonomous.java** should look like this
	
	```java title="Autonomous.java"
	--8<-- "docs/code_examples/basics/autonomous/Autonomous.java:full-example"
	```
		
	The code you typed in **RobotPreferences.java** should look like this
	
	```java title="RobotPreferences.java (delay)"
	--8<-- "docs/code_examples/basics/autonomous/RobotPreferences.java:auto-delay-and-distance"
	```

## Adding Our Autonomous Command to RobotContainer.java

- In order to run our **Autonomous** command group during autonomous, `RobotContainer` needs to hand it to `Robot.java` through `getAutonomousCommand()`

- In **RobotContainer.java** find `getAutonomousCommand()` and change it to

	!!! note "Why not use the chooser?"
		`SendableChooser` allows selecting between multiple autonomous routines from the dashboard at match start, which is useful when you have several autonomous options. For this tutorial we only have one autonomous routine, so using the chooser would add boilerplate (creating options, registering them, fetching the selection) without any benefit. Once you have multiple routines worth choosing from, replacing this line with a `SendableChooser` is a natural next step.

	```java title="RobotContainer.java"
	--8<-- "docs/code_examples/basics/autonomous/RobotContainer.java:get-autonomous-command"
	```

	- `m_drivetrain` here is the `Drivetrain` subsystem instance already declared in `RobotContainer`

## Testing Our Autonomous Command

- Now that we have finished coding our **Autonomous** command deploy code and add our new **RobotPreferences** to the widget in **Elastic**
- We have three preferences that change our autonomous behavior **driveDistanceSpeed**, **autoDriveDistance** and **autoDelay**
- **driveDistanceSpeed** will determine the **direction** and how **fast** the robot drives 
- **autoDriveDistance** will determine how many **inches** the robot drives **forward** or **backward**  
- **autoDelay** will determine how long the robot **waits** before executing **ShooterUp**
- Change these values before enabling your robot in autonomous to make you get the desired results

## Tips For Debugging Our Autonomous Command

- If the robot doesn't seem to stop moving or drive in the right direction check for inversions in your **Drive** commands or in the **Drivetrain** subsystem
  - You may also need to check if your **encoder** is working, if there are inversions, or if you are using the **getEncoderCount** method instead of the **getEncoderDistanceMethod**
- If your robot doesn't move make sure you typed in the **RobotPreference** names exactly or check your talon IDs/Connection
- If nothing happens after your robot is finished driving check your **autoDelay** preference and whether your **Shooter piston** is already actuated or if your solenoids are working
