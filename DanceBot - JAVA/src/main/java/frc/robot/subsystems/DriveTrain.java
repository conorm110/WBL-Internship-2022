// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Jaguar;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;

public class DriveTrain extends SubsystemBase {
  /** Creates a new DriveTrain. */
  public XboxController joy0 = new XboxController(0);
  private MotorController leftWheel;
  private MotorController rightWheel;
  String axis_list[] = {"l_stick_x", "l_stick_y", "l_trig", "r_trig", "r_stick_x", "r_stick_y"};


  public DriveTrain() {
    leftWheel = new Jaguar(0);
    rightWheel = new Jaguar(1);
  }

  public void setSpeed(double speed) {
    leftWheel.set(.6);
    rightWheel.set(.6);
  }

  public void move(){
   
  }

  

  



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
