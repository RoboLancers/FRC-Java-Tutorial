// Snippet file: Robot.java integration code for the Telemetry subsystem.
// These are not a standalone class — they show the relevant lines to add to Robot.java.

// --8<-- [start:field-declaration]
public static Telemetry m_telemetry;
// --8<-- [end:field-declaration]

// --8<-- [start:robot-init]
m_telemetry = new Telemetry(); //This must be initialized after all other robot subsystems
// --8<-- [end:robot-init]

// --8<-- [start:periodic-update]
Robot.m_telemetry.update();
// --8<-- [end:periodic-update]
