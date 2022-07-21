package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AutoDance extends SubsystemBase {

  private String name;
  static private int tesetingplsdelete = 1200;
  private Timer timmy;
  public AutoDance(String name, Timer timstwinfr)
  {
      this.name = name;
      timmy = timstwinfr;
  }
  
  public void start()
  {
      timmy.reset();
      timmy.start();
  }
  public void check()
  {

  }

  public static void AutoLoop() 
  {
    Movement.leftJaguar.setVoltage(tesetingplsdelete/100);
    tesetingplsdelete--;
  }

  @Override
  public void periodic() {  }
  @Override
  public void simulationPeriodic() {  }
}
