// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.motorcontrol.Jaguar;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Movement extends SubsystemBase {
  static Jaguar leftJaguar = new Jaguar(0); // pwm channel 0, change later
  static Jaguar rightJaguar = new Jaguar(1); // pwm channel 1, change later
  static Servo servo_a = new Servo(2);
  static Servo servo_b = new Servo(3);

  public Movement() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public static void moveArmLeft(double degrees) {
    servo_a.setAngle(degrees);
  }

  public static void moveArmRight(double degrees) {
    servo_b.setAngle(degrees);
  }

  public static void moveWheelLeft(double location) {
    leftJaguar.set(location);
  }

  public static void moveWheelRight(double location) {
    rightJaguar.set(location);
  }

  public void move() {
    // dont want controller to actually do anything rn, normally this is called once
    // per scheduler run
  }

  public void stop() {
  }
}