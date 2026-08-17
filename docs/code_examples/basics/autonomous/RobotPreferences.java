package frc.robot;

import edu.wpi.first.wpilibj.Preferences;

public class RobotPreferences {

	// --8<-- [start:drive-distance-speed]
	public static double driveDistanceSpeed() {
		return Preferences.getDouble("driveDistanceSpeed", 0.5);
	}
	// --8<-- [end:drive-distance-speed]

	// --8<-- [start:auto-delay-and-distance]
	public static double autoDelay() {
		return Preferences.getDouble("autoDelay", 5.0);
	}

	public static double autoDriveDistance() {
		return Preferences.getDouble("autoDriveDistance", 12.0);
	}
	// --8<-- [end:auto-delay-and-distance]
}
