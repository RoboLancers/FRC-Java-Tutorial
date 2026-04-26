# Sensors

How does the robot see?

![camera](../assets/images/sensors/camera.png)

## Some types of sensors

- **Limit Switches** - detects contact
- **Camera** - provides sight
- **Encoders** - measures rotational or linear motion
- **Ultrasonic** - measures distances
- **Gyroscope** - measures orientation
- **Processed Vision** - measures target's distance, angle, and offset from robot
- For more info on sensors see: [High Tech High Top Hat Technicians](http://tophattechnicians.com){target=_blank} - [Electrical Tutorial](https://drive.google.com/file/d/1ip54fjNDFaq-ZWw9lQrZj6vXamX33QDP/view){target=_blank}

|                        Limit Switch                        |             Grayhill brand Quadrature Encoder             |       Kauai Labs navX Gyro/ Accelerometer        |
| :--------------------------------------------------------: | :-------------------------------------------------------: | :----------------------------------------------: |
| ![Limit Switch](../assets/images/sensors/limit_switch.png) | ![Grayhill Encoder](../assets/images/sensors/encoder.png) | ![navX](../assets/images/sensors/navX_micro.png) |

## Knowledge Check

<!-- mkdocs-quiz intro -->

<quiz>
Which sensor type is used to measure the robot's orientation?
- [ ] Limit switch
- [ ] Encoder
- [ ] Ultrasonic
- [x] Gyroscope

A gyroscope measures orientation — which direction the robot is facing. This is used for field-relative driving so the robot can move correctly regardless of which way it is pointed.
</quiz>

<quiz>
Which type of sensor would you use to measure the distance between the robot and an object?
- [ ] Camera
- [ ] Limit switch
- [x] Ultrasonic
- [ ] Encoder

Ultrasonic sensors measure distances. Limit switches detect contact, cameras provide sight, and encoders measure rotational or linear motion — not raw distance to an object.
</quiz>

<quiz>
A limit switch is used to detect physical contact, such as a mechanism reaching the end of its travel.
- [x] True
- [ ] False

As listed on this page, limit switches detect contact. They are one of the simplest sensors available and are commonly used to tell the robot when a mechanism has reached a physical stop.
</quiz>

<!-- mkdocs-quiz results -->
