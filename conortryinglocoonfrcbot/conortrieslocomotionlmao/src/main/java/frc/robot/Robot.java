// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.LucyLimeLight;
import frc.robot.subsystems.MirrorDance;
import frc.robot.subsystems.Movement;
import frc.robot.subsystems.AutoDance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  Timer timmy = new Timer();
  LucyLimeLight lucy = new LucyLimeLight("lucy");
  AutoDance archie = new AutoDance("archie", timmy);

  private RobotContainer m_robotContainer;
  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
    System.out.println("We are starting autonomous mode");

    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }

    archie.start();
  }

  @Override
  public void autonomousPeriodic() {
    AutoDance.AutoLoop();
  }

  public static boolean teleop_ready = true;
  public static int lucy_blind_counter = 0;
  @Override
  public void teleopInit() {
    Movement.stop();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    if (teleop_ready) {
      MirrorDance.UpdateMediapipe();
      MirrorDance.FollowPlayer(lucy);
      Movement.moveArmRight(MirrorDance.r_arm);
      Movement.moveArmLeft(MirrorDance.l_arm);
      Movement.moveHeadX(MirrorDance.head_x);
      Movement.moveHeadY(MirrorDance.head_z);
      if (lucy.lucy_angles[0] == 0.0 ||lucy.lucy_angles[1] == 0.0 )
      {
        lucy_blind_counter ++;
      }
      else {
        lucy_blind_counter-= 5;
        if (lucy_blind_counter < 0){
          lucy_blind_counter = 0;
        }
      }
      if (lucy_blind_counter > 70) {
        teleop_ready = true;
      }
    }
    else {
      Movement.leftJaguar.setVoltage(4);
      Movement.rightJaguar.setVoltage(4);
      lucy.check();
      if (lucy.lucy_angles[0] != 0.0 ||lucy.lucy_angles[1] != 0.0 )
      {
        teleop_ready = true;
      }
    }
  }

  // These prolly wont ever be needed but im a code hoarder so dont delete them :) - conor
  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }
  @Override
  public void testPeriodic() {}
  @Override
  public void simulationInit() {}
  @Override
  public void simulationPeriodic() {}
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }
  @Override
  public void disabledInit() {}
  @Override
  public void disabledPeriodic() {}
}
