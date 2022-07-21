package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MirrorDance extends SubsystemBase {
  public MirrorDance() 
  {
    
  }

  public static void FollowPlayer(LucyLimeLight lucy)
  {
    lucy.check();
    if (lucy.lucy_angles[0] == 0) {
        Movement.stop();
      } 
      else if (lucy.lucy_angles[0] < -12) {
        Movement.leftJaguar.setVoltage(-3);
      } 
      else if (lucy.lucy_angles[0] > -12) {
        Movement.rightJaguar.setVoltage(3);
      }
  }
}
