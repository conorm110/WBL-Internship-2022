package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import java.io.IOException;
import java.util.List;

public class MirrorDance extends SubsystemBase {
  public MirrorDance() 
  {
    
  }

  private static double target_y_angle = 3;
  private static double y_angle_tolerance = 2;
  private static double target_x_angle = -12; // needs to be changed a bit, needed bc limelight isnt centered
  private static double x_angle_tolerance = 5;
  private static double speed = 4.20;

  public static void FollowPlayer(LucyLimeLight lucy)
  {
    GetMediapipe();
    lucy.check();
    double left_voltage = 0;
    double right_voltage = 0;
    if ((-1*y_angle_tolerance) < lucy.lucy_angles[1] && y_angle_tolerance > lucy.lucy_angles[1])
    {
        // in good y position
    }
    else if (lucy.lucy_angles[1] < target_y_angle)
    {
        left_voltage = speed;
        right_voltage = -1 * speed;
    }
    else 
    {
        left_voltage = -1 * speed;
        right_voltage = speed;
    }

    if ((-1*x_angle_tolerance) < lucy.lucy_angles[0] && x_angle_tolerance > lucy.lucy_angles[0])
    {
        // in good x position
    }
    else if (lucy.lucy_angles[0] < target_x_angle)
    {
        left_voltage -= 3;
        right_voltage -= 3;
    }
    else 
    {
        left_voltage += 3;
        right_voltage += 3;
    }


    Movement.leftJaguar.setVoltage(left_voltage);
    Movement.rightJaguar.setVoltage(right_voltage);

  }
  public static void GetMediapipe()
    {
        
    }
}
