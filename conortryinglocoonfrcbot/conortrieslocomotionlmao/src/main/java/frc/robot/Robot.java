// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.LucyLimeLight; // keep included i dont want to add it back later lmao
import frc.robot.subsystems.Movement;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  Timer timmy = new Timer();
  Limelight lucy = new Limelight("lucy");
  Autonomous archie = new Autonomous("archie");

  public class Limelight{
    private String name;
    public NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    public NetworkTableEntry tx = table.getEntry("tx");
    public NetworkTableEntry ty = table.getEntry("ty");
    public NetworkTableEntry ta = table.getEntry("ta");
    public double lucy_angles[] = {0,0};

    public Limelight(String name){
      this.name = name;
    }

    public void talk(){
      System.out.println("Hello I am" + name);
    }

    public void check(){
      vogue();
      SmartDashboard.putNumber("lucy_xangles", lucy_angles[0]);
      SmartDashboard.putNumber("lucy_yangles", lucy_angles[1]);
    }

    public void vogue(){
      lucy_angles[0] = tx.getDouble(0.0);
      lucy_angles[1] = ty.getDouble(0.0);
    }

    public double[] angles(){
      return lucy_angles;
    }

    
  }

  public class Autonomous{
    private String dancemode[][] = {
      {"Moving forward", "None", "0.1"}
    };

    private String name;
    private int autonomous_counter = 0;
    private double lil_sam = 0;
    private String[][] spyroom;

    public Autonomous(String name){
      this.name = name;
      this.spyroom = dancemode;
    }

    public void talk(){
      System.out.println("hello I am" + name);
    }

    public void start(){
      timmy.reset();
      timmy.start();
      briefcase(spyroom[0][0]);
      lil_sam = lil_sam + Double.parseDouble(spyroom[0][2]);
    }
    
    public void check(){
      if(timmy.get() > lil_sam){
        briefcase(spyroom[autonomous_counter][1]);
        autonomous_counter++;
        briefcase(spyroom[autonomous_counter][0]);
        lil_sam = lil_sam + Double.parseDouble(spyroom[autonomous_counter][2]);
      }
    }

    private void briefcase(String task){
      System.out.println(task);
      switch(task){
        case "Moving Forward":
        System.out.println("Moving Forward");
        Movement.moveWheelLeft(0.3);
        Movement.moveWheelRight(0.3);
        break;
        case "Moving Backward":
        System.out.println("Moving Back");
        Movement.moveWheelLeft(-.3);
        Movement.moveWheelRight(-.3);
        break;
        case "Moving Right":
        System.out.println("Moving Right");
        Movement.moveWheelRight(0.3);
        Movement.moveArmRight(90);
        break;
        case "Moving Left":
        System.out.println("Moving Left");
        Movement.moveWheelRight(0.3);
        Movement.moveArmLeft(90);
        break;
        case "None":
        System.out.println("Nothing");
        break;
        case("Stop Wheels"):
        System.out.println("Stoping Wheels");
        Movement.moveWheelLeft(0);
        Movement.moveWheelRight(0);
        break;
        case("Reset Arms"):
        System.out.println("Reset Arms");
        Movement.moveArmLeft(0);
        Movement.moveArmRight(0);
        break;
        case("Stop all"):
        System.out.println("All has stoped");
        Movement.moveArmLeft(0);
        Movement.moveArmRight(0);
        Movement.moveWheelLeft(0);
        Movement.moveWheelRight(0);
        break;
        
      }
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
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  Joystick logitechController = new Joystick(0);

  @Override
  public void teleopPeriodic() {
    Movement.moveArmLeft(logitechController.getPOV());
    Movement.moveArmRight(logitechController.getPOV());
    Movement.moveWheelLeft(logitechController.getRawAxis(1));
    Movement.moveWheelRight(logitechController.getRawAxis(5));

    //LucyLimeLight.check();

    lucy.check();
    
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
  }
}
