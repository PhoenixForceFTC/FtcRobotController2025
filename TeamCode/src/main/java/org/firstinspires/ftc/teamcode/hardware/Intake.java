package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake
{
    //--- Handles intake motor power based on gamepad input
    public static void intakeByPower(DcMotor motorIntake, Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        if (gamepad.left_trigger > 0.1) //--- Extend motorIntake when left trigger is pressed
        {
            motorIntake.setPower(gamepad.left_trigger); //--- Scale power by trigger pressure
        }
        else if (gamepad.left_bumper) //--- Retract motorIntake when left bumper is pressed
        {
            motorIntake.setPower(-1); //--- Full power in reverse
        }
        else
        {
            motorIntake.setPower(0); // Stop motorIntake when no input
        }

        //--- Show telemetry if enabled
        if (showInfo)
        {
            telemetry.addData("Intake Power", "%4.2f", motorIntake.getPower());
            telemetry.addData("Intake Position", motorIntake.getCurrentPosition());
        }
    }
}
