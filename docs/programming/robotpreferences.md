# [WIP] Using RobotPreferences

<!-- ![Image Title](imageURL)  -->

## Overview

In this section we will be going over

1. Creating and using RobotPreferences in Elastic
2. How to convert encoder counts to inches

***

## What Are RobotPreferences

- On SmartDashboard or ShuffleBoard there is a widget called Robot Preferences that can store variables that can be quickly changed
- For example you might have a variable that changes PID values which can be changed from Robot Preferences on SmartDashboard/ShuffleBoard
- For this section of our tutorial we will create a robot preference called driveEncoderCountsPerFoot

## Creating RobotPreferences

!!! abstract "" 
    **1)** Create a new **empty class** called **RobotPreferences**
    
    - This is where we store all of our **RobotPreferences** to access anywhere
    - If we want to use a **RobotPreference** we call RobotPreferences.preferenceName()

!!! abstract "" 
    **2)** Inside the constructor type:
    
	```java 
	public static double driveEncoderCountsPerFoot(){
	  return Preferences.getInstance().getDouble(“driveEncoderCountsPerFoot”, 1.0);
	}
	```
   
    - The format for creating a RobotPreference is
    
	```java
	public static variableType preferenceName(){
	  return Preferences.getInstance().getVariableType("preferenceName", value);
	```
	   
??? example

	Your full **RobotPreferences.java** should look like this
	
	```java
	package frc.robot;

	import edu.wpi.first.wpilibj.Preferences;

	/**
 	* Add your docs here.
 	*/
	public class RobotPreferences {
    	  // Drivetrain
    	  /**
     	  * Default value is 1.0
     	  */
    	  public static double driveEncoderCountsPerFoot() {
            return Preferences.getInstance().getDouble("driveEncoderCountsPerFoot", 1.0);
    	  }

	}
	```
	   
## Creating getDriveEncoderDistance Method

- We will use this **RobotPreference** to help us create a method that can keep track of the distance our robot has driven in inches

!!! abstract ""
    **1)** Create a method called **getDriveEncoderDistance** inside of **Drivetrain**
    
!!! abstract ""
    **2)** Inside type:
    
    ```java
	return (getDriveEncoderCount() / RobotPreferences.driveEncoderCountsPerFoot()) * 12;
	```
     
    - This will divide the current encoder count by however many counts there are in a foot then multiply that number by 12 to give us the encoder distance in inches
    
!!! note
    You may need to invert this value if your encoder counts backward when the robot is driving forward

!!! example

	The code you typed should be this
	
	```java
	public double getDriveEncoderDistance() {
   	 return (getDriveEncoderCount() / RobotPreferences.driveEncoderCountsPerFoot()) * 12;
  	}
	```
	  
!!! abstract ""
    **3)** Add the method to the **update** method in **Telemetry**
    
## Using RobotPreferences

- After deploying the code to your robot, open **Elastic** from the Driver Station.
- In the top menu go to **File → Add Widget** (or look in the left-side widget panel) and find the **Robot Preferences** widget, then drag it onto a tab.
- Alternatively, right-click on an empty area of a tab and select **Add Widget → Robot Preferences**.

Once the widget is visible:

- Click the **+** (Add) button inside the widget.
- A dialog will appear. Enter the **key name** (the string you passed to `Preferences.getInstance()`, e.g. `"driveEncoderCountsPerFoot"`) and select the matching **type** (Number for doubles and ints, String for text, Boolean for true/false).
- The preference will appear in the widget with its default value.
- Double-click the value field next to a preference to edit it — changes take effect **immediately** on the running robot without redeploying.
- If you double click on the preference value you will notice that you can change its value
- If you change a preference value it will update **immediately**

!!! tip
    If you want to save your robot preference values that you've changed make sure you hardcode them in **RobotPreferences.java** later or take a picture if you want to use them again later
    
## Measuring Distance Using Encoders

- Right now the encoders tell us distance in terms of encoder counts
- We will use our **driveEncoderCountsPerFoot** preference to save how many counts there are when the robot drives 1 foot

!!! abstract ""
    **1)** Move the wheel on your robot with the **Drivetrain** encoder attached 1 foot or drive your robot 1 foot
    
!!! abstract ""
    **2)** Read how many counts your encoder has in the **Drive Encoder Count** window
    
    - If you want to measure again press the **Reset Drive Encoder** command button to reset the **Drivetrain** encoder count
    
!!! abstract ""
    **3)** Change the value of **driveEncoderCountsPerFoot** in the widget to this number
    
!!! abstract ""
    **4)** Reset the **Drivetrain** encoder and move the wheel 1 foot or drive the robot 1 foot again
 
!!! abstract ""
    **5)** Make sure your **Drive Encoder Distance** window reads approximately 12 (this is in inches)
    
    - If not repeat these steps again
    
!!! abstract ""
    **6)** Save your **RobotPreferences** widget with this value
   
!!! abstract ""
    **7)** Hardcode this value in **RobotPreferences.java** in the **driveEncoderCountsPerFoot** method incase you cannot recover your **RobotPreferences** save
