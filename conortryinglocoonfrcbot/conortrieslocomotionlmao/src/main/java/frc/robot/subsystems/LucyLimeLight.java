// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.networktables.NetworkTableInstance;

public class LucyLimeLight extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  static NetworkTableEntry tx = table.getEntry("tx");
  static NetworkTableEntry ty = table.getEntry("ty");
  static NetworkTableEntry ta = table.getEntry("ta");
  static double lucy_angles[] = {0,0};

  public LucyLimeLight() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public static void check(){
    vogue();
    SmartDashboard.putNumber("lucy_xangles", lucy_angles[0]);
    SmartDashboard.putNumber("lucy_yangles", lucy_angles[1]);
  }

  public static void vogue(){
    lucy_angles[0] = tx.getDouble(0.0);
    lucy_angles[1] = ty.getDouble(0.0);
  }

  public static double[] angles(){
    return lucy_angles;
  }

  public void lights(boolean on){
    if(on){
      table.getEntry("ledMode").setNumber(3);
    }
    else{
      table.getEntry("ledMode").setNumber(1);
    }
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
