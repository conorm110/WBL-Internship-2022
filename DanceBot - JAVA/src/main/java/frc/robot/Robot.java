// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.motorcontrol.Jaguar;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;

  public XboxController joy0 = new XboxController(0);
  String axis_list[] = {"l_stick_x", "l_stick_y", "l_trig", "r_trig", "r_stick_x", "r_stick_y"};    
  
  public int count_airplane = 0;
  public int find_axis(String axis){
    count_airplane = 0;
    for (int i = 0; i < axis_list.length; i++){
      if(axis_list[i].equals(axis)){
        break;
      }
    }
    return count_airplane;
  }

  Timer timmy = new Timer();
  Driver driver = new Driver("driver");
  DriveTrain wally = new DriveTrain("wally");
  LimeLight lucy = new LimeLight("lucy");
  Auto archie = new Auto("archie");

  public class Driver{
    private String name;
    private XboxController joy0;
    private String control_stick[] = {"l_stick_y", "l_stick_x"};
    private double stick_control[] = {0.0, 0.0};

    public Driver(String name){
      this.name = name;
      joy0 = new XboxController(0);
    }

    public void talk(){
      System.out.println("Hi i am" + name);
    }

    public double[] control_panel(){
      for(int i = 0; i < stick_control.length; i++){
        stick_control[i] = find_axis(control_stick[i]);
      }
      return stick_control;
    }

    public double get_axis(String axis_plane){
      return joy0.getRawAxis(find_axis(axis_plane));
    }
  }


  public class DriveTrain{
  /** Creates a new DriveTrain. */
  private String name;
  private MotorController leftWheel;
  private MotorController rightWheel;
  private double max_speed = .7;
  private double max_turn = .7;
  // private String turn_axis = "l_stick_x";
  // private String speed_axis = "l_stick_y";
  private double[] moves = {0.0, 0.0};
  
  private final DifferentialDrive drivechain = new DifferentialDrive(leftWheel, rightWheel);
 
  public DriveTrain(String name) {
    this.name = name;
    leftWheel = new Jaguar(0);
    rightWheel = new Jaguar(1);
  }

  public void talk(){
    System.out.println("Hi i am" + name);
  }

  public void setSpeed(double speed) {
    leftWheel.set(.6);
    rightWheel.set(.6);
  }

  public void drive(double speed, double turn){
    drivechain.arcadeDrive(-speed, turn);
  }

  private void move(){
    sensitive(driver.control_panel()[0], driver.control_panel()[1]);
  }

  private void sensitive(double speed, double raw_turn){
    double turn = Math.pow(raw_turn, 2.0);
    if(raw_turn < 0){
      turn = -turn;
    }

    speed = max_speed * speed;
    turn = max_turn * turn;

    drive(speed, turn);
  }

  public void auto(double speed, double turn){
    moves[0] = speed;
    moves[1] = turn; 
  }

  public void driving(){
    drive(moves[0], moves[1]);
  }
  
  }

  
  public class LimeLight{
    private String name;
    public NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    public NetworkTableEntry tv = table.getEntry("tv"); // Whether lucy has any valid targets(0 or 1)
    public NetworkTableEntry tx = table.getEntry("tx"); // Horizontal Offset from Crosshair to Target(-xyz degrees to xyz degrees)
    public NetworkTableEntry ty = table.getEntry("ty"); // Vertical Offset from Crosshair to Target(-xyz degrees to xyz degrees)
    public NetworkTableEntry ta = table.getEntry("ta"); // Target Area (0% of image to 100% of image)
    public double le_angles[] = {0,0};

    public LimeLight(String name){
      this.name = name;
    }

    public void talk(){
      System.out.println("Hi i am" + name);
    }

    public void check(){
      sight();
      System.out.println("can you see" + le_angles[1]);
      SmartDashboard.putNumber("X-angles", le_angles[0]);
      SmartDashboard.putNumber("Y-angles", le_angles[1]);
      //SmartDashboard.putNumber("lucy_area", area);
    }

    public void sight(){
      le_angles[0] = tx.getDouble(0.0);
      le_angles[1] = ty.getDouble(0.0);
    }
    public double[] angles(){
      return le_angles;
    }
    public void lights(boolean on){
      if(on){
        table.getEntry("ledMode").setNumber(3);
      }
      else{
        table.getEntry("ledMode").setNumber(1);
      }
    }
    public void tracking(){

    }


  }

  
  public class Auto{
    private String dance[][]= {
      {"Move forward", "None", "0.01"},
      {"Move backward", "None", "0.01"}
    };

    private String name;
    private double lil_sam = 0;
    private String[][] spyroom;
    private int autonomous_counter = 0;

    public Auto(String name){
      this.name = name;
      this.spyroom = dance;
    }

    public void start(){
      timmy.reset();
      timmy.start();
      briefcase(spyroom[0][0]);
      lil_sam = lil_sam + Double.parseDouble(spyroom[0][1]);
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
      switch(task){
        case "Move backward":
          System.out.println("Moving back");
          wally.auto(-.3, 0);
          break;
        case "Move forward":
          System.out.println("Moving forward");
          wally.auto(0.3, 0);
          break;
        case "None":
          System.out.println("Nothing");
          break;
        case "Left":
          System.out.println("Turing Left");
          wally.auto(0, -.4);
          break;
        case "Right":
          System.out.println("Turning Right");
          wally.auto(0, .4);
          break;
        case "Right forward":
          System.out.println("moving forward right");
          wally.auto(.4, -.3);
          break;
        case "Left forward":
          System.out.println("moving forward left");
          wally.auto(.4, .3);
          break;
        case "Right backwards":
          System.out.println("moving backwards right");
          wally.auto(-.3, -3);
          break;
        case "Left backwards":
          System.out.println("moving backwards left");
          wally.auto(-.3, .3);
          break;
        case "Stop":
          System.out.println("Stopping");
          wally.auto(0, 0);
          break;
        default:
          break;
      }
    }

  }
    


  @Override
  public void robotInit() {
    
    m_robotContainer = new RobotContainer();

    wally.talk();
    driver.talk();
    lucy.talk();
  }

  
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }


  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

 
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
  public void teleopPeriodic() {
    wally.move();
    
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}
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
