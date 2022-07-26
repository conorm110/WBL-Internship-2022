package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LucyLimeLight extends SubsystemBase {
  private String name;
    public NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    public NetworkTableEntry tx = table.getEntry("tx");
    public NetworkTableEntry ty = table.getEntry("ty");
    public NetworkTableEntry ta = table.getEntry("ta");

    public double lucy_angles[] = {0,0};

    public LucyLimeLight(String name){
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
}
