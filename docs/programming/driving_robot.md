# Creating a Basic Driving Robot

<!-- TODO: Maybe split this into different pages -->

Lets get moving!

![Drive base](../assets/images/driving_robot/kitbot.jpg)
> [Picture source: Team 2984](http://ljrobotics.org/games/power-up-2018/){target=_blank}

## Overview

This section is designed to help you program a basic driving robot, start to finish.

**See table of contents for a breakdown of this section.**

***

## Creating the Drivetrain Subsystem

Before we begin we must create the class file for the drivetrain subsystem. See [Creating a New Subsystem](new_project.md#creating-a-new-subsystem){target=_blank} for info on how to do this.

### What will be added to the Drivetrain

In the Drivetrain class we will tell the subsystem what type of components it will be using.

- A Drivetrain needs motor controllers. In our case we will use Neo SparkMaxes (a brand of controller for motors made by Rev Robotics).
    - You could use other motor controllers such as Victor SPs or Talon SRs but we will be using NEO SparkMaxes
      - If you are using other motor controllers, replace SparkMax with Talon, TalonSRX, Victor, or VictorSP in the code you write depending on the type you use.
    - You can use 2 motors (left and right), but for this tutorial we will use 4.

!!! Tip
		Be sure to read [Visual Studio Code Tips](../basics/vscode_tips.md){target=_blank} before getting started! It will make your life a lot easier.

### Creating the SparkMax Variables

!!! summary ""
    **1)** Create 4 global variables of data type **SparkMax** and name them: `leftLeader`, `rightLeader`, `leftFollower`, `rightFollower`
   
    - To get started type the word SparkMax followed by the name i.e. `#!java private Final SparkMax leftLeader;`
    - These will eventually hold the object values for SparkMaxes, their port numbers, and their motor type (brushed or brushless).

!!! summary ""
    **2)** These are declared without values right now.
   
    - We do this to make sure it is empty at this point. 
    - When we assign these variables a value, we will be getting the motor controller's port numbers out of Constants
        - This means we cannot assign them at the global level

<details><summary> Example</summary>

	The code you typed should be this:

    ```java
      private final SparkMax leftLeader;
      private final SparkMax leftFollower;
      private final SparkMax rightLeader;
      private final SparkMax rightFollower; 
    ```

	Your full **Drivetrain.java** should look like this:

    ```java
    package frc.robot.subsystems;

    import com.revrobotics.spark.SparkLowLevel.MotorType;
    import com.revrobotics.spark.SparkMax;
    import edu.wpi.first.wpilibj.command.Subsystem;

    /**
     * Add your docs here.
     */
    public class Drivetrain extends Subsystem {
      // Put methods for controlling this subsystem
      // here. Call these from Commands.
      private final SparkMax leftLeader;
      private final SparkMax leftFollower;
      private final SparkMax rightLeader;
      private final SparkMax rightFollower;
      
      @Override
      public void periodic() {
        // This method will be called once per scheduler run
      }
    }
	```
</details>
<!-- TODO: Generalize this more -->

<details><summary>If an error occurs (red squiggles)"</summary> 
  1. Mouse Over the word SparkMax: The following menu should appear.
  ![](../assets/images/driving_robot/fix_error_1.png)
  2. 💡 Click "quick fix" 
  ![](../assets/images/driving_robot/Quick_fix_click.png)
  3. Select "Import 'SparkMax' (com.revrobotics.spark)"
  ![](../assets/images/driving_robot/quick_fix_import.PNG)
  4. Your error should be gone!
</details>

### Creating and filling the constructor

!!! summary ""
	Now that we have created the SparkMaxes  and the Drive Constants we must initialize them and tell them what port on the roboRIO they are on.

    **1)** Initialize (set value of) `leftLeader` to `#!java new SparkMax(LEFT_LEADER_ID, MotorType.KBrushed)`. 
	- This initializes a new SparkMax, `leftLeader`, in a new piece of memory and states it is on the port defined by `LEFT_LEADER_ID`. 
	- This should be done within the constructor `#!java Drivetrain()`
	- This calls the constructor `#!java Talon(int)` in the Talon class. 
    	- The constructor `#!java Talon(int)` takes a variable of type `#!java int`. In this case the `#!java int` (integer) refers to the port number on the roboRIO. 
  		
		??? Info "roboRIO port diagram"
    		  ![](../assets/images/driving_robot/roboRIO_port.png)

<deatils> <summary>Example</summary>

	The code you typed should be this:

    ```java
    public Drivetrain() {
      // Motors
      leftLeader = new SparkMax(DriveConstants.LEFT_LEADER_ID, MotorType.kBrushed);
    }
    ```

	Your full **Drivetrain.java** should look like this:

    ```java
    package frc.robot.subsystems;

    import com.revrobotics.spark.SparkLowLevel.MotorType;
    import com.revrobotics.spark.SparkMax;

    /**
     * Add your docs here.
     */
    public class Drivetrain extends Subsystem {
      // Put methods for controlling this subsystem
      // here. Call these from Commands.

      private final SparkMax leftLeader;
      private final SparkMax leftFollower;
      private final SparkMax rightLeader;
      private final SparkMax rightFollower;

	  public Drivetrain() {
        // Talons
        leftLeader = new SparkMax(0, MotorType.kBrushed);
        leftFollower = new SparkMax(1 , MotorType.kBrushed);
        rightLeader = new SparkMax(2, MotorType.kBrushed);
        rightFollower = new SparkMax(3, MotorType.kBrushed);
      }

      @Override
      public void periodic() {
        // This method will be called once per scheduler run
      }
	```
</details>

### Using Constants

<!-- TODO: Link this as example where robot map mentioned -->

Since each subsystem has its own components with their own ports, it is easy to lose track of which ports are being used and for what. To counter this you can use a class called **Constants** to hold all these values in a single location.


    - Names should follow the pattern SUBSYSTEM_NAME_OF_COMPONENT
    - The name is all caps since it is a **constant** ([more info on constants](../basics/java_basics.md#constants){target=_blank}).

!!! summary ""
      **1)**
      Before we initalize the SparkMax objects we are going to create constants to hold the CAN ID's of the motors. This will happen in constants.java
      - Inside the constants class, create a new class called `public static DriveConstants`.
      - Inside `DriveConstants` class, create for constants called `LEFT_LEADER_ID`, `LEFT_FOLLOWER_ID`, `RIGHT_LEADER_ID`, and `RIGHT_FOLLOWER_ID`.
      - Back in your `DriveTrain` class in `drivetrain.java`, import the `DriveConstants` class as follows: `Import frc.robot.Constants.DriveConstants;`.

  !!! Tip 
    Make sure to declare constants with `public static final` so they cannot be changed at runtime. 
  
  !!! Danger
    ***If you set this to the wrong value, you could damage your robot when it tries to move!***
!!! summary ""
    **1)** To use Constants, instead of putting `0` for the port in the SparkMax type: 
	```Java
	DriveConstants.LEFT_LEADER_ID
	```
   
!!! summary ""
	  **2)** Replace the remaining numbers with constants.

	!!! Tip
    	Remember to save both **Drivetrain.java** and **Constants.java**

<details><summary>Example</summary>

	The code you type should be this:

      ```Java
        public static final class DriveConstants {
        public static final int LEFT_LEADER_ID = 1;
        public static final int LEFT_FOLLOWER_ID = 2;
        public static final int RIGHT_LEADER_ID = 3;
        public static final int RIGHT_FOLLOWER_ID = 4;
      }
      ```

	Your full **Drivetrain.java** should look like this:

    ```java
    package frc.robot.subsystems;

    import com.revrobotics.spark.SparkLowLevel.MotorType;
    import com.revrobotics.spark.SparkMax;
    import edu.wpi.first.wpilibj.command.Subsystem;
    import frc.robot.Constants.DriveConstants;

    /**
     * Add your docs here.
     */
    public class Drivetrain extends Subsystem {
      // Put methods for controlling this subsystem
      // here. Call these from Commands.

      private final SparkMax leftLeader;
      private final SparkMax leftFollower;
      private final SparkMax rightLeader;
      private final SparkMax rightFollower;

      public Drivetrain() {
        // Talons
        leftLeader = new SparkMax(DriveConstants.LEFT_LEADER_ID, MotorType.kBrushed);
        leftFollower = new SparkMax(DriveConstants.LEFT_FOLLOWER_ID, MotorType.kBrushed);
        rightLeader = new SparkMax(DriveConstants.RIGHT_LEADER_ID, MotorType.kBrushed);
        rightFollower = new SparkMax(DriveConstants.RIGHT_FOLLOWER_ID, MotorType.kBrushed);

      @Override
      public void periodic() {
        // This method will be called once per scheduler run
      }
    }
	```

	Your full **Constants.java** should look similar to this:	

    ```java
    package frc.robot;

    public class Constants {
      public static final class DriveConstants {
	      public static final int LEFT_LEADER_ID = 1;
        public static final int LEFT_FOLLOWER_ID = 2;
        public static final int RIGHT_LEADER_ID = 3;
        public static final int RIGHT_FOLLOWER_ID = 4;
      }
    }
	```

	!!! Warning
      	Remember to use the values for **YOUR** specific robot or you could risk damaging it!
</details>

### Confguring the SparkMaxes
Each SparkMax motor must be configured with a CANTimeout. (How long to wait for a response from the motor controller) This is done as follows: 
```Java
// Set can timeout. Because this project only sets parameters once on construction, the timeout can be long without blocking robot operation. Code which sets or gets parameters during operation may need a shorter timeout.
    leftLeader.setCANTimeout(250);
    rightLeader.setCANTimeout(250);
    leftFollower.setCANTimeout(250);
    rightFollower.setCANTimeout(250);
```
Create the configuration to apply to motors. Voltage compensation helps the robot perform more similarly on different battery voltages (at the cost of a little bit of top speed on a fully charged battery). The current limit helps prevent tripping breakers.
```Java
    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(12);
    config.smartCurrentLimit(DriveConstants.DRIVE_MOTOR_CURRENT_LIMIT);
```

### Creating the arcade drive

#### What is the Drive Class

- The FIRST Drive class has many pre-configured methods available to us including DifferentialDrive, and many alterations of MecanumDrive.
- DifferentialDrive contains subsections such as TankDrive and ArcadeDrive.
    <!-- TODO: add back - For more information and details on drive bases see the WPILib documentation -->
- For our tutorial we will be creating an ArcadeDrive
- Arcade drives run by taking a moveSpeed and rotateSpeed. moveSpeed defines the forward and reverse speed and rotateSpeed defines the turning left and right speed.
- To create an arcade drive we will be using our already existing Drivetrain class and adding to it.

#### Programing a RobotDrive

<!-- TODO: Add instructions for TalonSRX -->

!!! summary ""
    **1)** Create the DifferentialDrive object.

    Outside of the constructor type:
  
    ```java 
     private final DifferentialDrive drive;
    ```
    This defines the drive object that we will use to drive the robot.
    In the constructor type:
  
    ```java

    drive = new DifferentialDrive(leftMotors, rightMotors);
    ```
    This defines the drive object.

    - Since DifferentialDrive only takes 2 parameters we need to create speed controller groups to combine like motor controllers together.
        - In this case we will combine the left motors together and the right motors together.

    !!! Warning
        You should only group motors that are spinning the same direction physically when positive power is being applied otherwise you could damage your robot.

!!! summary ""
    **2)** In order to configure the motors to drive correctly, we need to configure one on each side as the leader and one as the follower. 
    In the constructor we are goint to set the followe motors and link them to the leader motors. To do this we will need to include a couple more classes from the REV Library:
    ```Java
    import com.revrobotics.spark.SparkBase.PersistMode;
    import com.revrobotics.spark.SparkBase.ResetMode;
    ```
    Then in the constructor, create the follower/leader config:
    - Set configuration to follow leader and then apply it to correspondingfollower. Resetting in case a new controller is swapped in and persisting in case of a controller reset due to breaker trip.

    ```Java
    config.follow(leftLeader);
    leftFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    config.follow(rightLeader);
    rightFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    ```

    Remove following, then apply config to right leader
    ```Java
    config.disableFollowerMode();
    rightLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    ```
    Set conifg to inverted and then apply to left leader. Set Left side inverted so that postive values drive both sides forward
    ```Java
    config.inverted(true);
    leftLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    ```

<details><summary>Example</summary>

    ```Java
    package frc.robot.subsystems;

    import com.revrobotics.spark.SparkLowLevel.MotorType;
    import com.revrobotics.spark.SparkMax;
    import edu.wpi.first.wpilibj.command.Subsystem;
    import frc.robot.Constants.DriveConstants;

    /**
     * Add your docs here.
     */
    public class Drivetrain extends Subsystem {
      // Put methods for controlling this subsystem
      // here. Call these from Commands.

      private final SparkMax leftLeader;
      private final SparkMax leftFollower;
      private final SparkMax rightLeader;
      private final SparkMax rightFollower;

      public Drivetrain() {
        // Talons
        leftLeader = new SparkMax(DriveConstants.LEFT_LEADER_ID, MotorType.kBrushed);
        leftFollower = new SparkMax(DriveConstants.LEFT_FOLLOWER_ID, MotorType.kBrushed);
        rightLeader = new SparkMax(DriveConstants.RIGHT_LEADER_ID, MotorType.kBrushed);
        rightFollower = new SparkMax(DriveConstants.RIGHT_FOLLOWER_ID, MotorType.kBrushed);

       SparkMaxConfig config = new SparkMaxConfig();
        config.voltageCompensation(12);
        config.smartCurrentLimit(DriveConstants.DRIVE_MOTOR_CURRENT_LIMIT);

        leftLeader.setCANTimeout(250);
        rightLeader.setCANTimeout(250);
        leftFollower.setCANTimeout(250);
        rightFollower.setCANTimeout(250);

        config.follow(leftLeader);
        leftFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        config.follow(rightLeader);
        rightFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Remove following, then apply config to right leader
        config.disableFollowerMode();
        rightLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        // Set conifg to inverted and then apply to left leader. Set Left side inverted
        // so that postive values drive both sides forward
        config.inverted(true);
        leftLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
      @Override
      public void periodic() {
        // This method will be called once per scheduler run
      }
    }
}
	```
</details>

#### Creating the arcadeDrive method

Now it’s time to make an arcadeDrive from our differentialDrive!

!!! summary ""
    **1)** Let’s create a public void method called “arcadeDrive” with type “double” parameters moveSpeed and rotateSpeed.

    Below the `periodic` method create a new method called `arcadeDrive`. This method will be called from our Drive command to actually move the robot.

    ```java
    public void arcadeDrive(double moveSpeed, double rotateSpeed) {

    }
    ```

    !!! Tip
        By putting something in the parentheses it makes the method require a parameter when it is used. When the method gets used and parameters are passed, they will be store in moveSpeed and rotateSpeed (in that order). See [parameters](../basics/java_basics.md#parameters){target=_blank} for more info.

!!! summary ""
    **2)** Now lets make our method call the differentialDrive's arcadeDrive method.

    Inside our method type:

    ```Java
    drive.arcadeDrive(moveSpeed, rotateSpeed);
    ```

    DifferentialDrive's arcadeDrive method takes parameters moveValue and rotateValue.

    !!! Note
        At this point you could instead create a tank drive, however implementation differs slightly.
        To do so type `#!java differentialDrive.tankDrive(moveSpeed, rotateSpeed);` instead of `#!java differentialDrive.arcadeDrive(moveSpeed, rotateSpeed);` and change the method name reflect this.

    !!! Tip
        If you want to limit the max speed you can multiple the speeds by a decimal (i.e. 0.5*moveSpeed will make the motors only move half of their maximum speed)

        You may want to do this for initial testing to make sure everything is going the right direction.

<details><summary>Drive Example</summary>

	The code you type should be this:

    ```Java
    public void arcadeDrive(double moveSpeed, double rotateSpeed) {
      drive.arcadeDrive(moveSpeed, rotateSpeed);
    }
    ```
  
	Your full **Drivetrain.java** should look like this:

    ```Java
   package frc.robot.subsystems;

    import com.revrobotics.spark.SparkLowLevel.MotorType;
    import com.revrobotics.spark.SparkMax;
    import edu.wpi.first.wpilibj.command.Subsystem;
    import frc.robot.Constants.DriveConstants;

    /**
     * Add your docs here.
     */
    public class Drivetrain extends Subsystem {
      // Put methods for controlling this subsystem
      // here. Call these from Commands.

      private final SparkMax leftLeader;
      private final SparkMax leftFollower;
      private final SparkMax rightLeader;
      private final SparkMax rightFollower;

      public Drivetrain() {
        // Talons
        leftLeader = new SparkMax(DriveConstants.LEFT_LEADER_ID, MotorType.kBrushed);
        leftFollower = new SparkMax(DriveConstants.LEFT_FOLLOWER_ID, MotorType.kBrushed);
        rightLeader = new SparkMax(DriveConstants.RIGHT_LEADER_ID, MotorType.kBrushed);
        rightFollower = new SparkMax(DriveConstants.RIGHT_FOLLOWER_ID, MotorType.kBrushed);

       SparkMaxConfig config = new SparkMaxConfig();
        config.voltageCompensation(12);
        config.smartCurrentLimit(DriveConstants.DRIVE_MOTOR_CURRENT_LIMIT);

        leftLeader.setCANTimeout(250);
        rightLeader.setCANTimeout(250);
        leftFollower.setCANTimeout(250);
        rightFollower.setCANTimeout(250);

        config.follow(leftLeader);
        leftFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        config.follow(rightLeader);
        rightFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Remove following, then apply config to right leader
        config.disableFollowerMode();
        rightLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        // Set conifg to inverted and then apply to left leader. Set Left side inverted
        // so that postive values drive both sides forward
        config.inverted(true);
        leftLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
      @Override
      public void periodic() {
        // This method will be called once per scheduler run
      }
    }
    public void arcadeDrive(double moveSpeed, double rotateSpeed) {
      drive.arcadeDrive(moveSpeed, rotateSpeed);
    }
	```
</details>

### Making our robot controllable

### Creating the Drivearcade Command

- Remember that **methods** tell the robot what it can do but in order to make it do these things we must give it a **command**. See [Command Based Robot](../basics/wpilib.md#command-based-robot){target=_blank}
- Now that we have created the method, we need to create a command to call and use that method.
- Let’s create a new command called **DriveArcade** that calls arcadeDrive method we just created!

Before we begin we must create the class file for the DriveArcade command. See [Creating a New Command](new_project.md#creating-a-new-command){target=_blank} for info on how to do this and info on what each pre-created method does.

#### Define variables

!!! summary ""
    **1)** Create `xspeed` and `zrotation` variables. (to be passed to drive subsystem). These will be declared as `DoubleSuppliers`, which is a function that return a type. This is important for later.
    **2)** Create an emtpy `driveSubsystem` instance of `DriveSubsystem`

    !!! Warning
        `DoubleSupplier` and `DriveSubsystem` will have to be imported as follows:
        ```Java
        import frc.robot.subsystems.DriveSubsystem;
        import java.util.function.DoubleSupplier; 
        ```

    ```Java
    private final DoubleSupplier xSpeed;
    private final DoubleSupplier zRotation;
    private final DriveSubsystem driveSubsystem;
    ```

#### In the constructor

!!! summary ""
    **1)** Inside the parenthesis of the  the constructor `driveArcade()` add 3 variables:
    ```Java
      public DriveCommand(
      DoubleSupplier xSpeed, DoubleSupplier zRotation, DriveSubsystem driveSubsystem)
    ```
    These are values that will be passed into the command in `RobotContainer.java`

!!! summary ""
    **2)** Inside constructor `#!java DriveArcade()` type:

    ```java
    this.xSpeed = xSpeed;
    this.zRotation = zRotation;
    this.driveSubsystem = driveSubsystem;
    addRequirements(this.drivetrain);
    ```
   
    - The 3 lines starting with `this` set the global variables we defined at the top of our class file to the values being passed into the consturctor.
!!! Tip
   `this` is how the class instance `object` refers to itself in code.

!!! summary ""
    - `addRequirements` means this command will end all other commands currently using drivetrain and will run instead when executed.
    - It also means, other commands that require drivetrain will stop this command and run instead when executed.

    !!! Warning
        If you use the light bulb to import ‘Robot', be sure to import the one with “frc.robot”

#### In the execute method

!!! summary ""
    **1)** In the execute method we will we want to call the **arcadeDrive** method we created in **Drivetrain** and give it the variables **moveSpeed** `xspeed` and **rotateSpeed** `zrotation` we created as parameters.

    In the execute() method below rotateSpeed type:

    ```Java
    driveSubsystem.driveArcade(xSpeed.getAsDouble(), zRotation.getAsDouble());
    ```

#### In the isFinished method

Since we will be using this command to control the robot we want it to run indefinitely.

!!! summary ""
    **1)** To do this we are going to continue having isFinished return false, meaning the command will never finish. 
    
    (We don't need to change anything as this is the default)

    !!! Tip
        - If we did want a command to finish, we make this return true.
          - This can be done by replacing false with true to make it finish instantly
          - Alternatively we can make a condition which can return true
              - For example `(timePassed > 10)` will return true after 10 seconds but return false anytime before 10 seconds have passed.

#### In the end method

!!! summary ""
    **1)** We will call the arcadeDrive method and give it 0 and 0 as the parameters. 

    In the end() method type:

    ```java
    RobotContainer.m_drivetrain.arcadeDrive(0, 0);
    ```

    - This make the motors stop running when the command ends by setting the movement speed to zero and rotation speed to zero.

#### Completed Example

<details><summary>Example</summary>

	Your full **Constants.java** should look similar to this:	

    ```java
    package frc.robot;

    public class Constants {
  	  public static final class DriveConstants {
      public static final int LEFT_LEADER_ID = 1;
      public static final int LEFT_FOLLOWER_ID = 2;
      public static final int RIGHT_LEADER_ID = 3;
      public static final int RIGHT_FOLLOWER_ID = 4;
      public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60;
    }

      public static final class OperatorConstants {
      public static final int DRIVER_CONTROLLER_PORT = 0;
      public static final int OPERATOR_CONTROLLER_PORT = 1;
    }
    }
	```

	Your full **DriveArcade.java** should look like this:

    ```Java
    import edu.wpi.first.wpilibj2.command.Command;
    import frc.robot.subsystems.DriveSubsystem;
    import java.util.function.DoubleSupplier;

    // Command to drive the robot
    public class DriveCommand extends Command {
      private final DoubleSupplier xSpeed;
      private final DoubleSupplier zRotation;
      private final DriveSubsystem driveSubsystem;

      // Constructor. Runs only once when the command is first created.
      public DriveCommand(
          DoubleSupplier xSpeed, DoubleSupplier zRotation, DriveSubsystem driveSubsystem) {
        // Save parameters to local variables for use later
        this.xSpeed = xSpeed;
        this.zRotation = zRotation;
        this.driveSubsystem = driveSubsystem;

        // Declare subsystems required by this command. This should include any
        // subsystem this command sets and output of
        addRequirements(this.driveSubsystem);
      }

      // Called just before this Command runs the first time
      @Override
      protected void initialize() {
      }

      // Called repeatedly when this Command is scheduled to run
      @Override
      protected void execute() {
        driveSubsystem.driveArcade(xSpeed.getAsDouble(), zRotation.getAsDouble());
      }

      // Called once the command ends or is interrupted.
      @Override
      protected void end(boolean interrupted) {
        driveSubSystem.arcadeDrive(0, 0);
      }

      // Make this return true when this Command no longer needs to run execute()
      @Override
      protected boolean isFinished() {
        return false;
      }
    }
	```
  </details>

### Creating the Joystick

In order to drive our robot, it needs to know what will be controlling it. To do so, we will use the joystick in `RobotContainer.java`, as `m_drivecontroller`.

!!! summary ""
    **1)** Open Constants.java
      Check and make sure the `kDriverControllerPort` constant is present.
    **2)** Open RobotContainer.java
    - Change all `ExampleSubsystem` references to `DriveSubsystem`

<!-- TODO: add details on how to find joystick port in driverstation tips -->

### Using setDefaultCommand

  
!!! summary ""
    **1)** Back in **RobotContainer.java** We will need to remove everything inside the `configureBindings` method.
    **2)** in the `configureBindings`we will call the `setDefaultCommand` of `m_drivetrain` and create a new `DriveArcade` command with parameters. 

    - Commands in this method will run when the robot is enabled.
      - They also run if no other commands using the subsystem are running.
      - This is why we write **addRequirements(Robot.m_subsystemName)** in the commands we create, it ends currently running commands using that subsystem to allow a new command is run.
    - We will the default command for the drive subsystem to an instance of the `DriveArcade` with the values provided by the joystick axes on the driver controller. 
      - The Y axis of the controller is inverted so that pushing the stick away from you (a negative value) drives the robot forwards (a positive value). 
      - Similarly for the X axis where we need to flip the value so the joystick matches the WPILib convention of counter-clockwise positive
  
    ```Java
    driveSubsystem.setDefaultCommand(new DriveArcade(
        () -> -m_driverController.getLeftY() *
            (m_driverController.getHID().getRightBumperButton() ? 1 : 0.5),
        () -> -m_driverController.getRightX(),
        driveSubsystem));
    ```

    !!! Tip
        Remember to use the light bulb for importing if needed!
    !!! Tip
        The `New` keyword creates a new instance of a class (object)

<details><summary>Example</summary>

	Your full **RobotContainer.java** should look like this:

    ```java
    package frc.robot;

    import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
    import edu.wpi.first.wpilibj2.command.button.Trigger;
    import frc.robot.Constants.OperatorConstants;
    import frc.robot.commands.Autos;
    import frc.robot.commands.ExampleCommand;
    import frc.robot.subsystems.ExampleSubsystem;

    /**
     * This class is where the bulk of the robot should be declared.  Since Command-based is a
     * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
     * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
     * (including subsystems, commands, and button mappings) should be declared here.
     */
    public class RobotContainer {
      // The robot's subsystems and commands are defined here...
      public static final DriveSubsystem drivetrain = new DriveSubsystem();
      private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

      private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);
      private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

      /**
       * The container for the robot.  Contains subsystems, OI devices, and commands.
       */
      public RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();

        
      }

      /**
       * Use this method to define your button->command mappings.  Buttons can be created by
       * instantiating a {@link GenericHID} or one of its subclasses ({@link
       * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
       * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
       */
      private void configureButtonBindings() {
        driveSubsystem.setDefaultCommand(new DriveSubSystem(
        () -> -driverController.getLeftY() *
            (driverController.getHID().getRightBumperButton() ? 1 : 0.5),
        () -> -driverController.getRightX(),
        driveSubsystem));
      }


      /**
       * Use this to pass the autonomous command to the main {@link Robot} class.
       *
       * @return the command to run in autonomous
       */
      public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return m_autoCommand;
      }
    }
	```
</details>