package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.Servo;

public class ServoUtils
{
    //--- Moves the servo to the specified position and holds it
    public static double moveToPosition(Servo servo, double position)
    {
        servo.setPosition(position);
        //--- The servo will maintain this position as PWM remains enabled
        return servo.getPosition();
    }

    //--- Moves the servo to the specified position and disables PWM after a delay
    public static double moveToPositionAndDisable(Servo servo, double position, int delayMs)
    {
        //--- Create a new thread to handle the servo movement and PWM disable
        new Thread(() ->
        {
            servo.setPosition(position);

            try
            {
                //--- Wait for the servo to reach the desired position
                Thread.sleep(delayMs); //--- Adjust based on servo speed
            }
            catch (InterruptedException e)
            {
                //--- No action needed
            }

            //--- Disable PWM to stop holding the position
            servo.getController().pwmDisable();
        }).start();

        return servo.getPosition();
    }
}
