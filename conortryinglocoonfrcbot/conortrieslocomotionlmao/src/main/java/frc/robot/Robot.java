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

import org.w3c.dom.events.MouseEvent;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.motorcontrol.Jaguar;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  String buttons_list[] = {"na", "a", "b", "x", "y", "l_bum", "r_bum", "ttt", "tl"};
  public int count_but = 0;
  public int find_but(String button){  
    count_but = 0;  
    for(int i = 0; i < buttons_list.length; i++ ){
      count_but = i;
      if(buttons_list[i].equals(button)){
        break;
      }
    }
    return count_but;
  }

  private RobotContainer m_robotContainer;

  Timer timmy = new Timer();
  Limelight lucy = new Limelight("lucy");
  Autonomous archie = new Autonomous("archie");
  Wheels wally = new Wheels("wally");

  // Make a func where wally ask what lucy angles are and then moves depending on that
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
    // private String test_mode[][] = {
    //   {"Moving Forward", "Stop Wheels", "3.0"},
    //   {"Moving Backward", "Stop Wheels", "3.0"},
    //   {"Moving Right", "Stop Wheels", "3.0"},
    //   {"Moving Left", "Stop Wheels", "3.0"},
    //   {"Done", "this will never run", "999.9"},
    // };

    // private String track[][] = {
    //   {"give angles", "None", "10.0"},
    //   {"Done", "this will never run", "999.9"}
    // };

    private String dance_mode[][] = {
      {"Moving Forward", "Stop Wheels", "0.5"},
      {"Arms Up", "None", "0.3"},
      {"Arms Center", "None", "0.2"},
      {"Soft Right", "Stop Wheels", "1.0"},
      {"Right Arm Up", "None", "0.5"},
      {"Left Arm Up", "None", "0.5"},
      {"Arms Center", "None", "0.3"},
      {"Right Arm Up", "None", "0.5"},
      {"Left Arm Up", "None", "0.5"},
      {"Arms Center", "None", "0.3"},
      {"Moving Left", "Stop Wheels", "0.5"},
      {"Left Arm Up", "None", "0.5"},
      {"Right Arm Up", "None", "0.5"},
      {"Arms Center", "None", "0.3"},
      {"Left Arm Up", "None", "0.5"},
      {"Right Arm Up", "None", "0.5"},
      {"Arms Center", "None", "0.3"},
      {"Soft Right", "Stop Wheels", "0.5"},
      {"Moving Backward", "Stop Wheels", "0.5"},
      {"Arms Up", "None", "0.5"},
      {"Arms Center", "None", "0.3"},
      {"Right Arm Up", "None", "0.3"},
      {"Soft Right", "Stop Wheels", "0.5"},
      {"Arms Center", "None", "0.3"},
      {"Left Arm Up", "None", "0.5"},
      {"Soft Left", "Stop Wheels", "1.0"},
      {"Arms Center", "None", "0.3"},
      {"Soft Right", "Stop Wheels", "0.5"},
      {"Moving Forward", "Stop Wheels", "0.4"},
      {"Arms Up", "None", "0.3"},
      {"Done", "this will never run", "999.9"}
    };  

  //  private String dance_mode[][] = {};

    private String name;
    private int autonomous_counter = 0;
    private double lil_sam = 0;
    private String[][] spyroom;

    public Autonomous(String name){
      this.name = name;
      this.spyroom = dance_mode;
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
        Movement.moveWheelLeft(0.5);
        Movement.moveWheelRight(0.5);
        break;
        case "Moving Backward":
        System.out.println("Moving Back");
        Movement.moveWheelLeft(-.5);
        Movement.moveWheelRight(-.5);
        break;
        case "Moving Right":
        System.out.println("Moving Right");
        Movement.moveWheelRight(0.5);
        Movement.moveArmRight(90);
        break;
        case "Soft Right":
        System.out.println("Moving Righty Softly");
        Movement.moveWheelRight(0.3);
        break;
        case "Moving Left":
        System.out.println("Moving Left");
        Movement.moveWheelRight(0.5);
        Movement.moveArmLeft(90);
        break;
        case "Soft Left":
        System.out.println("Moving Lefty Softly");
        Movement.moveWheelLeft(0.3);
        break;
        case "Arms Up":
        System.out.println("Moving Arms Up");
        Movement.moveArmLeft(.9);
        Movement.moveArmRight(.9);
        break;
        case "Arms Down":
        System.out.println("Moving Arms Down");
        Movement.moveArmLeft(-0.9);
        Movement.moveArmRight(-0.9);
        break;
        case "Arms Center":
        System.out.println("Arms Reset");
        Movement.moveArmLeft(0);
        Movement.moveArmRight(0);
        break;
        case "Right Arm Up":
        System.out.println("Moving Right Arm Up");
        Movement.moveArmRight(.9);
        Movement.moveArmLeft(-0.2);
        break;
        case "Left Arm Up":
        System.out.println("Moving Left Arm Up");
        Movement.moveArmLeft(.9);
        Movement.moveArmRight(-.2);
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
        case("give angles"):
        System.out.println("got the angles");
        wally.check();
        break;
        default:
        break;
        
      }
    }

  }

  public class Wheels{
    private String name;

    public Wheels(String name){
      this.name = name;
    }

    public void talk(){
      System.out.println(name);
    }

    
    public void giveme_angles(){
      Movement.moveWheelLeft(lucy.lucy_angles[0]);
      Movement.moveWheelRight(lucy.lucy_angles[0]);
      // wally asks for lucy angles
      // how should wally move
    }

    public void wheely(){
      Movement.leftJaguar.set(.5);
    }

    public void whooly(){
      Movement.rightJaguar.set(.5);
    }

    public void check(){
      if(lucy.lucy_angles[0] > 500){
        wheely();
      }
      else if(lucy.lucy_angles[0] < -500){
        whooly();
      }
    }
  }
  

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
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
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
    System.out.println("We are starting autonomous mode");
    archie.autonomous_counter = 0;
    archie.start();

    // m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    // if (m_autonomousCommand != null) {
    //   m_autonomousCommand.schedule();
    // }
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
      break;
      case kDefaultAuto:
      default:
      archie.check();
    }
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
    /**
    Movement.moveArmLeft(logitechController.getPOV());
    Movement.moveArmRight(logitechController.getPOV());
    Movement.moveWheelLeft(logitechController.getRawAxis(1));
    Movement.moveWheelRight(logitechController.getRawAxis(5));
    */
    lucy.check();
    double[] lucy_pos = lucy.angles();
    if (lucy_pos[0] == 0) {
      Movement.stop();
    } 
    else if (lucy_pos[0] < -12) {
      Movement.leftJaguar.setVoltage(-3);
    } 
    else if (lucy_pos[0] > -12) {
      Movement.rightJaguar.setVoltage(3);
    }
    SmartDashboard.putNumber("cum", lucy_pos[0]);

    //LucyLimeLight.check();
    
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
