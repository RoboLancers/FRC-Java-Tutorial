# AdvantageKit

> AdvantageKit is a logging framework developed by team 6328 (Mechanical Advantage).

***

## What is Logging?

Logging is recording some or all of the state of the robot — such as the current values of variables, inputs and outputs, and which Commands are running — so that it can be played back and analyzed later.

## Why Log?

Logging helps with debugging by letting you see exactly what was happening to the robot and what it was doing when something broke. This is especially useful at competition, where you have limited time and testing ability to diagnose problems.

!!! example "Real-world example"
    At Houston 2023, team 8033 had an issue in their second quals match where the grabber stopped responding to input. Using the logs from that match, they could see that the sensor readings of the grabber had stopped updating, which pointed to the CAN bus connection to the grabber having broken — a diagnosis that would have been nearly impossible without logs.

## Why AdvantageKit?

AdvantageKit logs **every input and output** to and from the robot. This means the full state of the robot can be perfectly recreated from a log file.

<img alt="Diagram of AdvantageKit data flow" src="../../assets/akit.png" width="800">

It also means that the same inputs can be run through modified code to simulate how the robot *would have* responded — AdvantageKit calls this **replay**. For example, team 6328 used replay to diagnose a vision-tracking issue: they adjusted their tracking algorithm, then confirmed with replay that the change would have produced the correct outputs given the original match inputs.

AdvantageKit is a mature, actively maintained framework and is closely integrated with **AdvantageScope**, a log and simulation viewer also built by 6328.

## Drawbacks

- **Performance overhead** — logging every input and output uses CPU time each loop on the roboRIO.
- **Architecture change** — AdvantageKit requires an IO layer beneath each subsystem, which takes additional effort to write. However, this same IO layer makes simulating subsystems significantly easier, so it is generally a worthwhile trade-off.

!!! note
    8033-specific usage of AdvantageKit features is covered in more detail in the following lessons.

***

## Resources

| Resource | Link |
|---|---|
| AdvantageKit docs | [docs.advantagekit.org](https://docs.advantagekit.org/) |
| AdvantageKit repository | [github.com/Mechanical-Advantage/AdvantageKit](https://github.com/Mechanical-Advantage/AdvantageKit) |
| AdvantageScope log viewer | [github.com/Mechanical-Advantage/AdvantageScope](https://github.com/Mechanical-Advantage/AdvantageScope) |
| 6328 logging talk | [6328 logging talk (YouTube)](https://youtu.be/mmNJjKJG8mw) |

## Examples

- [6328 2023 robot code](https://github.com/Mechanical-Advantage/RobotCode2023)
- [3476 2023 code](https://github.com/FRC3476/FRC-2023)
- [8033 2025 code](https://github.com/HighlanderRobotics/Reefscape)

### Exercises

- Follow this tutorial to add AdvantageKit to your kitbot code. *(coming soon)*

### Notes

- See the [AdvantageKit Structure Reference](AKitStructureReference.md) article for more on the IO layer structure
- [Here](https://drive.google.com/drive/folders/1qNMZ7aYOGI31dQNAwt7rhFo97NR7mxtr) are some logs from our 2023-2024 season
