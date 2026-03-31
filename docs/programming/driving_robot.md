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

!!! note "More Info"
    Be sure to read [Visual Studio Code Tips](../basics/vscode_tips.md){target=_blank} before getting started! It will make your life a lot easier.

### Creating the SparkMax Variables


**1)** Create 4 global variables of data type **SparkMax** and name them: `leftLeader`, `rightLeader`, `leftFollower`, `rightFollower`

- To get started type the word SparkMax followed by the name i.e. `private Final SparkMax leftLeader;`
- These will eventually hold the object values for SparkMaxes, their port numbers, and their motor type (brushed or brushless).


**2)** These are declared without values right now.

- We do this to make sure it is empty at this point.
- When we assign these variables a value, we will be getting the motor controller's port numbers out of Constants
    - This means we cannot assign them at the global level


??? example "Example code"
    **SparkMax Motor Member Variables:**

    ```java title="CANDriveSubsystem.java"
    --8<-- "docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:motors"
    ```

<!-- TODO: Generalize this more -->

**If an error occurs (red squiggles)**

1. Mouse Over the word SparkMax: The following menu should appear.
![](../assets/images/driving_robot/fix_error_1.PNG)

2. 💡 Click "quick fix" 
![](../assets/images/driving_robot/quick_fix_click.png)

3. Select "Import 'SparkMax' (com.revrobotics.spark)"
![](../assets/images/driving_robot/Quick_fix_import.png)

4. Your error should be gone!

### Creating and filling the constructor

Now that we have created the SparkMaxes  and the Drive Constants we must initialize them and tell them what port on the roboRIO they are on.

**1)** Initialize (set value of) `leftLeader` to `new SparkMax(LEFT_LEADER_ID, MotorType.kBrushless)`. 

- This initializes a new SparkMax, `leftLeader`, in a new piece of memory and states it is on the port defined by `LEFT_LEADER_ID`. 
- This should be done within the constructor `Drivetrain()`
- This calls the constructor `SparkMax(int, MotorType)` in the SparkMax class. 
    - The constructor `SparkMax(int, MotorType)` takes a variable of type `int` for the CAN ID and `MotorType` for brushless or brushed. In this case the `int` (integer) refers to the CAN ID on the roboRIO. 
    
 **roboRIO port diagram**

![](../assets/images/driving_robot/roboRIO_port.png)


??? example "Constructor Initialization Example"
    ```java title="Constructor declaration"
    public CANDriveSubSystem () {}
    ```

    **Full Constructor: **

    ```java title="Full Constructor"
    --8<-- "docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:constructor"
    ```

    See [CANDriveSubsystem.java](../code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java) for the complete constructor implementation.

### Using Constants

<!-- TODO: Link this as example where robot map mentioned -->

Since each subsystem has its own components with their own ports, it is easy to lose track of which ports are being used and for what. To counter this you can use a class called **Constants** to hold all these values in a single location.


    - Names should follow the pattern SUBSYSTEM_NAME_OF_COMPONENT
    - The name is all caps since it is a **constant** ([more info on constants](../basics/java_basics.md#constants){target=_blank}).



Before we initalize the SparkMax objects we are going to create constants to hold the CAN ID's of the motors. This will happen in constants.java

- Inside the constants class, create a new class called `public static DriveConstants`.
- Inside `DriveConstants` class, create for constants called `LEFT_LEADER_ID`, `LEFT_FOLLOWER_ID`, `RIGHT_LEADER_ID`, and `RIGHT_FOLLOWER_ID`.
- Back in your `DriveTrain` class in `drivetrain.java`, import the `DriveConstants` class as follows: `Import frc.robot.Constants.DriveConstants;`.

!!! tip
    Make sure to declare constants with `public static final` so they cannot be changed at runtime.

!!! danger
    If you set this to the wrong value, you could damage your robot when it tries to move!
!!! note
    To use Constants, instead of putting `0` for the port in the SparkMax type:

    ```java title="constants.java"
    public static final int LEFT_LEADER_ID = 1;
    ```

!!! note
    Replace the remaining numbers with constants.

!!! tip
    Remember to save both **Drivetrain.java** and **Constants.java**

??? example "DriveConstants Example"
    **Drive Constants Definition:**

    ```java
    --8<-- "docs/code_examples/2026KitBotInline/Constants.java:constants"
    ```

**Full Constants.java with all Robot Constants:**

See [Constants.java](../code_examples/2026KitBotInline/Constants.java) for the complete constants file including OperatorConstants and other subsystem constants.

!!! warning
    Remember to use the values for **YOUR** specific robot or you could risk damaging it!

### Configuring the SparkMaxes

**Setting CAN Timeout:**

Each SparkMax motor must be configured with a CANTimeout. (How long to wait for a response from the motor controller)

```java title="CANDriveSubsystem.java"
--8<-- "docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:can-timeout"
```

**Voltage Compensation and Current Limiting:**

Create the configuration to apply to motors. Voltage compensation helps the robot perform more similarly on different battery voltages (at the cost of a little bit of top speed on a fully charged battery). The current limit helps prevent tripping breakers.

```java title="CANDriveSubsystem.java"
--8<-- "docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:voltage-compensation"
```

See [CANDriveSubsystem.java](../code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java) for the full configuration implementation in the constructor.

## Creating the arcade drive

### What is the Drive Class

- The FIRST Drive class has many pre-configured methods available to us including DifferentialDrive, and many alterations of MecanumDrive.
- DifferentialDrive contains subsections such as TankDrive and ArcadeDrive.
    <!-- TODO: add back - For more information and details on drive bases see the WPILib documentation -->
- For our tutorial we will be creating an ArcadeDrive
- Arcade drives run by taking a moveSpeed and rotateSpeed. moveSpeed defines the forward and reverse speed and rotateSpeed defines the turning left and right speed.
- To create an arcade drive we will be using our already existing Drivetrain class and adding to it.

### Programing a RobotDrive

<!-- TODO: Add instructions for TalonSRX -->

!!! summary ""
    **1)** Create the DifferentialDrive object.

    **Member Variable Declaration:**
    ```java
    --8<-- "docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:differential-drive-variable"
    ```
    This defines the drive object that we will use to drive the robot.

    **Constructor Initialization:**
    ```java
    drive = new DifferentialDrive(leftLeader, rightLeader);
    ```
    This initializes the differential drive object with the left and right leader motors.

    - Since DifferentialDrive takes 2 parameters we pass the left and right leader motors.
    - The follower motors are configured to follow these leaders through the SparkMax configuration.

    !!! warning
        You should only group motors that are spinning the same direction physically when positive power is being applied otherwise you could damage your robot.

!!! note
    **2)** In order to configure the motors to drive correctly, we need to configure one on each side as the leader and one as the follower.
    In the constructor we are going to set the follower motors and link them to the leader motors. To do this we will need to include a couple more classes from the REV Library:
    ```Java
    import com.revrobotics.spark.SparkBase.PersistMode;
    import com.revrobotics.spark.SparkBase.ResetMode;
    ```
    Then in the constructor, configure the followers to follow the leaders:

    **Set follower configuration:**
    ```java
    --8<-- "docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:follower-config"
    ```

    **Configure right leader:**
    ```java
    --8<-- "docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:right-leader-config"
    ```

    **Invert left leader for correct motor direction:**
    ```java
    --8<-- "docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:left-inversion"
    ```

??? example "Full Drive Subsystem Example"
    See [CANDriveSubsystem.java](../code_examples/2026KitBotInline/src/main/java/frc/robot/subsystems/CANDriveSubsystem.java) for the complete implementation with all motor configuration and initialization.

### Creating the arcadeDrive method

Now it’s time to make an arcadeDrive from our differentialDrive!

!!! abstract
    **1)** Let’s create a public void method called “arcadeDrive” with type “double” parameters moveSpeed and rotateSpeed.

    Below the `periodic` method create a new method called `arcadeDrive`. This method will be called from our Drive command to actually move the robot.

    ```java
    public void arcadeDrive(double moveSpeed, double rotateSpeed) {

    }
    ```

    !!! tip
        By putting something in the parentheses it makes the method require a parameter when it is used. When the method gets used and parameters are passed, they will be store in moveSpeed and rotateSpeed (in that order). See [parameters](../basics/java_basics.md#parameters){target=_blank} for more info.

!!! abstract “”
    **2)** Now lets make our method call the differentialDrive’s arcadeDrive method.

    Inside our method type the call to the differential drive:

    ```java
    --8<-- “docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:drive-arcade-method”
    ```

    DifferentialDrive’s arcadeDrive method takes parameters moveValue and rotateValue.

    !!! note
        At this point you could instead create a tank drive, however implementation differs slightly.
        To do so type `differentialDrive.tankDrive(moveSpeed, rotateSpeed);` instead of `differentialDrive.arcadeDrive(moveSpeed, rotateSpeed);` and change the method name reflect this.

    !!! tip
        If you want to limit the max speed you can multiple the speeds by a decimal (i.e. 0.5*moveSpeed will make the motors only move half of their maximum speed)

        You may want to do this for initial testing to make sure everything is going the right direction.

### Making our robot controllable

## Creating the Drivearcade Command

- Remember that **methods** tell the robot what it can do but in order to make it do these things we must give it a **command**. See [Command Based Robot](../basics/wpilib.md#command-based-robot){target=_blank}
- Now that we have created the method, we need to create a command to call and use that method.
- Let’s create a new command called **DriveArcade** that calls arcadeDrive method we just created!

Before we begin we must create the class file for the DriveArcade command. See [Creating a New Command](new_project.md#creating-a-new-command){target=_blank} for info on how to do this and info on what each pre-created method does.

### Define variables

!!! summary “”
    **1)** Create `xspeed` and `zrotation` variables. (to be passed to drive subsystem). These will be declared as `DoubleSuppliers`, which is a function that return a type. This is important for later.
    **2)** Create an emtpy `driveSubsystem` instance of `Drivetrain`

    !!! warning
        `DoubleSupplier` and `Drivetrain` will have to be imported as follows:
        ```Java
        import frc.robot.subsystems.CANDriveSubsystem;
        import java.util.function.DoubleSupplier;
        ```

    ```java title=”DriveArcadeCommand.java”
    --8<-- “docs/code_examples/2026KitBotInline/commands/DriveArcadeCommand.java:class-variables”
    ```

### In the constructor

!!! summary “”
    **1)** Inside the parenthesis of the constructor `driveArcade()` add 3 variables:
    ```java title=”DriveArcadeCommand.java”
    --8<-- “docs/code_examples/2026KitBotInline/commands/DriveArcadeCommand.java:constructor-signature”
    ```
    These are values that will be passed into the command in `RobotContainer.java`

!!! summary “”
    **2)** Inside constructor implementation type:

    ```java title=”DriveArcadeCommand.java”
    --8<-- “docs/code_examples/2026KitBotInline/commands/DriveArcadeCommand.java:constructor-body”
    ```

    - The lines starting with `this` set the global variables we defined at the top of our class file to the values being passed into the consturctor.
    !!! tip
        `this` is how the class instance `object` refers to itself in code.

!!! summary “”
    - `addRequirements` means this command will end all other commands currently using drivetrain and will run instead when executed.
    - It also means, other commands that require drivetrain will stop this command and run instead when executed.

    !!! warning
        If you use the light bulb to import ‘Robot’, be sure to import the one with “frc.robot”

### In the execute method

!!! summary “”
    **1)** In the execute method we will we want to call the **arcadeDrive** method we created in **Drivetrain** and give it the variables **moveSpeed** `xspeed` and **rotateSpeed** `zrotation` we created as parameters.

    ```java title=”DriveArcadeCommand.java”
    --8<-- “docs/code_examples/2026KitBotInline/commands/DriveArcadeCommand.java:execute-method”
    ```

### In the isFinished method

Since we will be using this command to control the robot we want it to run indefinitely.

!!! summary “”
    **1)** To do this we are going to continue having isFinished return false, meaning the command will never finish.

    ```java title=”DriveArcadeCommand.java”
    --8<-- “docs/code_examples/2026KitBotInline/commands/DriveArcadeCommand.java:is-finished-method”
    ```

    !!! tip
        - If we did want a command to finish, we make this return true.
          - This can be done by replacing false with true to make it finish instantly
          - Alternatively we can make a condition which can return true
              - For example `(timePassed > 10)` will return true after 10 seconds but return false anytime before 10 seconds have passed.

### In the end method

!!! summary “”
    **1)** We will call the arcadeDrive method and give it 0 and 0 as the parameters. this will stop the robot when the command completes.

    ```java title=”DriveArcadeCommand.java”
    --8<-- “docs/code_examples/2026KitBotInline/commands/DriveArcadeCommand.java:end-method”
    ```

    - This make the motors stop running when the command ends by setting the movement speed to zero and rotation speed to zero.

### Completed Example

See [DriveArcadeCommand.java](../code_examples/2026KitBotInline/commands/DriveArcadeCommand.java) for the complete command class implementation.

### Creating the Joystick

In order to drive our robot, it needs to know what will be controlling it. To do so, we will use the joystick in `RobotContainer.java`, as `m_drivecontroller`.

!!! summary ""
    **1)** Open Constants.java
      Check and make sure the `kDriverControllerPort` constant is present.
    **2)** Open RobotContainer.java
    - in the imports section, change `ExampleCommand` to `DriveArcade`.
    - inside the class, find the line ` private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();` and change `ExampleSubsystem` to `Drivetrain` and `m_exampleSubsystem` to `drivetrain`.

<!-- TODO: add details on how to find joystick port in driverstation tips -->

### Using setDefaultCommand

!!! summary ""
    **1)** Back in **RobotContainer.java** We will need to remove everything inside the `configureBindings` method.
    **2)** in the `configureBindings`we will call the `setDefaultCommand` of `drivetrain` and create a new `DriveArcade` command with parameters. 

    !!! tip
        - Commands in this method will run when the robot is enabled.
          - They also run if no other commands using the subsystem are running.
          - This is why we write **addRequirements(Robot.subsystemName)** in the commands we create, it ends currently running commands using that subsystem to allow a new command is run.
    - We will the default command for the drive subsystem to an instance of the `DriveArcade` with the values provided by the joystick axes on the driver controller.
      - The Y axis of the controller is inverted so that pushing the stick away from you (a negative value) drives the robot forwards (a positive value).
      - Similarly for the X axis where we need to flip the value so the joystick matches the WPILib convention of counter-clockwise positive
  
    ```java title="RobotContainer.java"
    --8<-- "docs/code_examples/2026KitBotInline/RobotContainer.java:drive-config"
    ```
    !!! tip
        - Notice the `()->` notation above. This notation creates lamdas or anonymous methods. [More about Lambdas](https://www.w3schools.com/java/java_lambda.asp){target=blank}
        - The lambas are required because we set the parameter types of `xpeed` and 'zrotation' in our `DriveArcade` to be `DoubleSuppliers`, which are methods that return doubles. (Which is what the lambdas above return.)
        - These are declared as such so that they get and send the updated values from `m_driverController.getLeftY()` and `m_driverController.getRightX()` to the drive motors continuously.

    !!! tip
        Remember to use the light bulb for importing if needed!
    !!! tip
        The `New` keyword creates a new instance of a class (object)

??? example "Full RobotContainer Example"
    See [RobotContainer.java](../code_examples/2026KitBotInline/RobotContainer.java) for the complete RobotContainer implementation.

    The key part for drive configuration is in `configureBindings()`:

    ```java title="RobotContainer.java"
    --8<-- "docs/code_examples/2026KitBotInline/RobotContainer.java:drive-config"
    ```

    This sets arcade drive as the default command, using:

    - The negative Y-axis of the left joystick (inverted so pushing away drives forward)
    - The negative X-axis of the right joystick (inverted for WPILib counter-clockwise positive convention)
    - Both axes scaled for controllability