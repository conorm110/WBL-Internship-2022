// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
//import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;





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

  Timer timmy = new Timer();
  Wheels wally = new Wheels("wally", .7);
  Auto archie = new Auto("archie");

  public class Wheels{
    private String name;
    private CANSparkMax front_LeftMotor = new CANSparkMax(1, MotorType.kBrushless);
    private CANSparkMax back_LeftMotor = new CANSparkMax(2, MotorType.kBrushless);
    private CANSparkMax front_RightMotor = new CANSparkMax(3, MotorType.kBrushless);
    private CANSparkMax back_RightMotor = new CANSparkMax(4, MotorType.kBrushless);

    private MotorControllerGroup ralf = new MotorControllerGroup(front_RightMotor, back_RightMotor);
    private MotorControllerGroup louie = new MotorControllerGroup(front_LeftMotor, back_LeftMotor);

    private final DifferentialDrive drivechain = new DifferentialDrive(louie, ralf);
    public double[] shush = {0.0, 0.0};

    public Wheels(String name, double max_speed){
      this.name = name;
      max_speed = max_speed;
      ralf.setInverted(true);
    }

    private void wheels(double speed, double turn){
      drivechain.arcadeDrive(speed, -turn);
    }

    public void auto(double speed, double turn){
      shush[0] = speed;
      shush[1] = turn;
      
    }

  }


  public class Auto{

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
      }
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

    double joy_left = joy0.getRawAxis(0);
    double joy_right = joy0.getRawAxis(1);
    
    wheels(joy_left, joy_right);



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
