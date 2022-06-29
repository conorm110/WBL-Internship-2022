// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.motorcontrol.MotorController;
// import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;
//import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
//import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;





/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  private final XboxController joy0 = new XboxController(0);

  String axis_list[] = {"l_stick_x", "l_stick_y", "l_trig", "r_trig", "r_stick_x", "r_stick_y"};
  
  Timer timmy = new Timer();
  Wheels wally = new Wheels("wally", .7);
  Auto archie = new Auto("archie");
  Limelight lucy = new Limelight("lucy");
  Driver driver = new Driver("driver");

  public class Driver{
    private String name;
    private XboxController joy;

    public Driver(String name){
      this.name = name;
    }

    public double get_axis(int raw_axis){
      return joy.getRawAxis(raw_axis);
    }
  }


  public class Wheels{
    private String name;
    private double max_speed;
    private double max_turn = 0.4;

    private PWMSparkMax ralf = new PWMSparkMax(0);
    private PWMSparkMax louie = new PWMSparkMax(1);

   // private CANSparkMax front_LeftMotor = new CANSparkMax(1, MotorType.kBrushless);
   // private CANSparkMax back_LeftMotor = new CANSparkMax(2, MotorType.kBrushless);
   // private CANSparkMax front_RightMotor = new CANSparkMax(3, MotorType.kBrushless);
    //private CANSparkMax back_RightMotor = new CANSparkMax(4, MotorType.kBrushless);

    //private MotorControllerGroup ralf = new MotorControllerGroup(front_RightMotor, back_RightMotor);
    //private MotorControllerGroup louie = new MotorControllerGroup(front_LeftMotor, back_LeftMotor);

    private final DifferentialDrive drivechain = new DifferentialDrive(ralf, louie);
    public double[] shush = {0.0, 0.0};

    public Wheels(String name, double _max_speed){
      this.name = name;
      max_speed = _max_speed;
      ralf.setInverted(true);
    }

    private void check(double speed, double turn){
      sensitive(speed, turn);
    }

    private void drive(double speed, double turn){
      drivechain.arcadeDrive(speed, -turn);
    }

    private void sensitive(double speed, double raw_turn){
      double turn = Math.pow(raw_turn, 2.0);
      if (raw_turn < 0){
        turn = -turn;
      }

      speed = max_speed * speed;
      turn = max_turn * turn;

      drive(speed, turn);

    }

    public void auto(double speed, double turn){
      shush[0] = speed;
      shush[1] = turn;
    }

    public void driving(){
      drive(shush[0], shush[1]);
    }

  }


  public class Auto{

    private String moving [][]={
      {"Moving forward", "None", "0.1"},
      {"Moving backwards", "None", "0.3"},
      {"Moving Left", "None", "0.4"},
      {"Moving Right", "None", "0.2"},
    };

    private int autonomous_counter = 0;
    private String name;
    private String[][] auto;
    
    public Auto(String name){
      this.name = name;
    }

    public void start(){
      timmy.reset();
      timmy.start();
      briefcase(auto[0][0]);
    }

    private void briefcase(String task){
      System.out.println(task);
      switch(task){
      case "Moving forward":
        System.out.println("Moving forward");
        wally.auto(-3,0);
        break;
      case "Moving backwards":
        System.out.println("Moving back");
        wally.auto(-0.6, 0);
        break;
      case "Moving right":
        System.out.println("Moving right");
        wally.auto(0, .4);
        break;
      case "Moving left":
        System.out.println("Moving left");
        wally.auto(0, -.3);
        break;
      case "None":
        System.out.print("Nothing");
        break;
      
        
      }
    }

  }

  
  public class Limelight{
    private String name;

    public Limelight(String name){
      this.name = name;
    }
  }


  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    wally.check(driver.get_axis(1), driver.get_axis(1));

  }


  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
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
 
   /** This function is called periodically during autonomous. */
   @Override
   public void autonomousPeriodic() {}

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

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
