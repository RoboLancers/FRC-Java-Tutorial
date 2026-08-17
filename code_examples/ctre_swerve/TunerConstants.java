// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// This file is a TRIMMED, illustrative excerpt of what the CTRE Tuner X Swerve Project
// Generator actually produces in src/main/java/frc/robot/generated/TunerConstants.java.
// Do not hand-type this file — run the generator (see the tutorial) and let it fill in
// the real CAN IDs, gear ratios, and CANcoder offsets measured from your own robot.
// Full reference: https://v6.docs.ctr-electronics.com/en/stable/docs/tuner/tuner-swerve/index.html
package frc.robot.generated;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstantsFactory;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class TunerConstants
{
  // --8<-- [start:canbus]
  // The CAN bus every drivetrain device lives on: "rio" for the roboRIO's native bus,
  // or the name assigned to a CANivore.
  public static final CANBus kCANBus = new CANBus("canivore", "./logs/example.hoot");
  // --8<-- [end:canbus]

  private static final int kPigeonId = 1;

  public static final SwerveDrivetrainConstants DrivetrainConstants = new SwerveDrivetrainConstants()
      .withCANBusName(kCANBus.getName())
      .withPigeon2Id(kPigeonId);

  // Gear ratios and wheel radius, generated from your swerve module type selection.
  private static final double   kDriveGearRatio = 6.746_031_75;
  private static final double   kSteerGearRatio = 21.428_571_43;
  private static final Distance kWheelRadius    = Inches.of(2);

  private static final SwerveModuleConstantsFactory<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> ConstantCreator =
      new SwerveModuleConstantsFactory<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration>()
          .withDriveMotorGearRatio(kDriveGearRatio)
          .withSteerMotorGearRatio(kSteerGearRatio)
          .withWheelRadius(kWheelRadius);

  // --8<-- [start:module-constants]
  // Front-Left module: drive/steer motor CAN IDs, CANcoder ID, mounting location, and the
  // CANcoder offset measured by the generator's self-test — note this is in ROTATIONS, not
  // the degrees YAGSL's absoluteEncoderOffset uses.
  private static final int      kFrontLeftDriveMotorId       = 3;
  private static final int      kFrontLeftSteerMotorId       = 2;
  private static final int      kFrontLeftEncoderId          = 1;
  private static final Angle    kFrontLeftEncoderOffset      = Rotations.of(0.152_343_75);
  private static final boolean  kFrontLeftSteerMotorInverted = true;
  private static final Distance kFrontLeftXPos               = Inches.of(10);
  private static final Distance kFrontLeftYPos               = Inches.of(10);

  public static final SwerveModuleConstants<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> FrontLeft =
      ConstantCreator.createModuleConstants(
          kFrontLeftSteerMotorId, kFrontLeftDriveMotorId, kFrontLeftEncoderId, kFrontLeftEncoderOffset,
          kFrontLeftXPos, kFrontLeftYPos, /* driveInverted */ false, kFrontLeftSteerMotorInverted, /* encoderInverted */ false);
  // --8<-- [end:module-constants]

  // FrontRight, BackLeft, and BackRight are generated the exact same way, each with their
  // own CAN IDs, mounting location, and measured offset.
  public static final SwerveModuleConstants<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> FrontRight =
      ConstantCreator.createModuleConstants(0, 1, 0, Rotations.of(-0.487_304_69),
          Inches.of(10), Inches.of(-10), true, true, false);

  public static final SwerveModuleConstants<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> BackLeft =
      ConstantCreator.createModuleConstants(6, 7, 3, Rotations.of(-0.219_482_42),
          Inches.of(-10), Inches.of(10), false, true, false);

  public static final SwerveModuleConstants<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> BackRight =
      ConstantCreator.createModuleConstants(4, 5, 2, Rotations.of(0.172_363_28),
          Inches.of(-10), Inches.of(-10), true, true, false);

  // --8<-- [start:create-drivetrain]
  /**
   * Creates the drivetrain with the generated constants for all four modules.
   * This should only be called once, from RobotContainer.
   */
  public static CommandSwerveDrivetrain createDrivetrain()
  {
    return new CommandSwerveDrivetrain(DrivetrainConstants, FrontLeft, FrontRight, BackLeft, BackRight);
  }
  // --8<-- [end:create-drivetrain]
}
