package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LucyLimeLight extends SubsystemBase {
  private String name;
    public NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    public NetworkTable table2 = NetworkTableInstance.getDefault().getTable("SmartDashboard");
    public NetworkTableEntry tx = table.getEntry("tx");
    public NetworkTableEntry ty = table.getEntry("ty");
    public NetworkTableEntry ta = table.getEntry("ta");
    
    public static double head_x = 0.0;
    public static double head_y = 0.0;
    public static double head_z = 0.0;
    public static double l_arm = 0.0;
    public static double r_arm = 0.0;

    public double lucy_angles[] = {0,0};

    public LucyLimeLight(String name){
      this.name = name;
    }

    public void talk(){
      System.out.println("Hello I am" + name);
    }

    public void check(){
      vogue();
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

      SmartDashboard.putNumber("lucy_xangles", lucy_angles[0]);
      SmartDashboard.putNumber("lucy_yangles", lucy_angles[1]);
      SmartDashboard.putNumber("head_x", head_x);
      SmartDashboard.putNumber("head_y", head_y);
      SmartDashboard.putNumber("head_z", head_z);
      SmartDashboard.putNumber("l_arm", l_arm);
      SmartDashboard.putNumber("r_arm", r_arm);
    }

    public void vogue(){
      lucy_angles[0] = tx.getDouble(0.0);
      lucy_angles[1] = ty.getDouble(0.0);
    }
}
