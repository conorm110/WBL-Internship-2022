// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Jaguar;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveTrain extends SubsystemBase {
    PS4Controller ps4controller = new PS4Controller(0);

    Jaguar leftJaguar = new Jaguar(0); // pwm channel 0, change later
    Jaguar rightJaguar = new Jaguar(1); // pwm channel 1, change later

    DifferentialDrive m_drive = new DifferentialDrive(leftJaguar, rightJaguar);
    
    /** Creates a new ExampleSubsystem. */
    public DriveTrain() {}

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void move() {
        m_drive.tankDrive(ps4controller.getLeftY(), ps4controller.getRightY());
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }

    public void stop() {
        m_drive.stopMotor();
    }
}
