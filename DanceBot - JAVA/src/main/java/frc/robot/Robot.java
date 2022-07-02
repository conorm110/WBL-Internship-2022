// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.Jaguar;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  public XboxController joy0 = new XboxController(0);
  String axis_list[] = {"l_stick_x", "l_stick_y", "l_trig", "r_trig", "r_stick_x", "r_stick_y"};

  Driver driver = new Driver("driver");
  DriveTrain wally = new DriveTrain("wally");
  LimeLight lucy = new LimeLight("lucy");

  public class Driver{
    private String name;
    private XboxController joy;
    private String control_stick[] = {"l_stick_y", "l_stick_x"};
    private double stick_control[] = {0.0, 0.0};
  
    public Driver(String name){
      this.name = name;
      joy = new XboxController(0);
    }
  
    public double[] control_panel(){
      for(int i = 0; i < stick_control.length; i++){
          stick_control[i] = get_axis(control_stick[i]);
      }
      return stick_control; 
    }

    public double get_axis(String axis_plane){
      return joy.getRawAxis(find_axis(axis_plane));
    }
  
  }
  
  public class DriveTrain{
    private String name;
    private MotorController leftWheel;
    private MotorController rightWheel;
    private String turn_axis = "l_stick_x";
    private String speed_axis = "l_stick_y";
  
    public DriveTrain(String name){
      this.name = name;
      leftWheel = new Jaguar(0);
      rightWheel = new Jaguar(1);
    }

    public void setSpeed(double speed) {
      leftWheel.set(.6);
      rightWheel.set(.6);
    }

    private void move(){
      driver.get_axis(speed_axis);
      driver.get_axis(turn_axis);
    }
  }
  
  public class Audo{}

  public class LimeLight{
    private String name;

    public LimeLight(String name){
      this.name = name;
    }
  }  





































  @Override
  public void robotInit() {
 
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

 
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
