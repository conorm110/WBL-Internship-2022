package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import java.io.IOException;
import java.util.List;

public class MirrorDance extends SubsystemBase {
  public MirrorDance() 
  {
    
  }

  public static NetworkTable table2 = NetworkTableInstance.getDefault().getTable("SmartDashboard");
  public static double head_x = 0.0;
  public static double head_y = 0.0;
  public static double head_z = 0.0;
  public static double l_arm = 0.0;
  public static double r_arm = 0.0;
  private static double target_y_angle = 3;
  private static double y_angle_tolerance = 2;
  private static double target_x_angle = -12; // needs to be changed a bit, needed bc limelight isnt centered
  private static double x_angle_tolerance = 5;
  private static double speed = 4.20;

  private static double old_lv = 0.0;
  private static double old_rv = 0.0;
  public static void FollowPlayer(LucyLimeLight lucy)
  {
    lucy.check();
    double left_voltage = 0;
    double right_voltage = 0;
    if (lucy.lucy_angles[1] == 0.0){
        if (old_lv > 0){
            left_voltage = old_lv - 0.2;
        }
        else if (old_lv < 0){
            left_voltage = old_lv + 0.2;
        }
        if (old_rv > 0){
            right_voltage = old_rv - 0.2;
        }
        else if (old_lv < 0){
            right_voltage = old_rv + 0.2;
        }
    }
    else if (lucy.lucy_angles[1] < -15){
        // we need to move fast if your too far away because it looses detection very fast, ~20d depending on lighting
        left_voltage = 4;
        right_voltage = -4;
    }
    else if (lucy.lucy_angles[1] > -11){
        left_voltage = -3;
        right_voltage = 3;
    } else {
        left_voltage = 0;
        right_voltage = 0;
    }

    // if we are due right, add -2 to both
    // 3-6
    // if less than 3-6, turn right
    // otherwise turn left
    if (lucy.lucy_angles[0] == 0.0) {

    }
    else if (lucy.lucy_angles[0] < -10) {
        left_voltage -= 2.5;
        right_voltage -= 2.5;
    }
    else if (lucy.lucy_angles[0] > 16) {
        left_voltage +=2.5;
        right_voltage +=2.5;
    }
    else if (lucy.lucy_angles[0] < 2) {
        left_voltage -= 1;
        right_voltage -= 1;
    }
    else if (lucy.lucy_angles[0] > 6) {
        left_voltage +=1;
        right_voltage +=1;
    }


    old_lv = left_voltage;
    old_rv = right_voltage;
    Movement.leftJaguar.setVoltage(left_voltage);
    Movement.rightJaguar.setVoltage(right_voltage);

  }
  public static void UpdateMediapipe()
    {
        NetworkTableEntry head_x_mp = table2.getEntry("head_x");
        NetworkTableEntry head_y_mp = table2.getEntry("head_y");
        NetworkTableEntry head_z_mp = table2.getEntry("head_z");
        NetworkTableEntry l_arm_mp = table2.getEntry("l_arm");
        NetworkTableEntry r_arm_mp = table2.getEntry("r_arm");
        head_x = head_x_mp.getDouble(head_x);
        head_y = head_y_mp.getDouble(head_y);
        head_z = head_z_mp.getDouble(head_z);
        l_arm = l_arm_mp.getDouble(l_arm);
        r_arm = r_arm_mp.getDouble(r_arm);
        SmartDashboard.putNumber("head_x", head_x);
        SmartDashboard.putNumber("head_y", head_y);
        SmartDashboard.putNumber("head_z", head_z);
        SmartDashboard.putNumber("l_arm", l_arm);
        SmartDashboard.putNumber("r_arm", r_arm);
    }
}
