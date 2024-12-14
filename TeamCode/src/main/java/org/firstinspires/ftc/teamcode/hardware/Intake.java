package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.MotorUtils;

public class Intake
{
    //--- Constants for motorIntake positions
    private static final int MOTOR_INTAKE_MAX_POSITION = 150; // Maximum position (fully extended)
    private static final int MOTOR_INTAKE_MIN_POSITION = -30; // Minimum position (fully retracted)

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

    //--- Handles intake motor movement based on encoder positions
    public static void intakeByEncoder(DcMotor motorIntake, Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        if (gamepad.left_trigger > 0.1) //--- Extend when left trigger is pressed
        {
            MotorUtils.setTargetPosition(motorIntake, MOTOR_INTAKE_MAX_POSITION, 1.0);
        }
        else if (gamepad.left_bumper) //--- Retract when left bumper is pressed
        {
            MotorUtils.setTargetPosition(motorIntake, MOTOR_INTAKE_MIN_POSITION, 1.0);
        }
        else
        {
            //--- Stop the motor when no input
            MotorUtils.stopMotor(motorIntake);
        }

        //--- Show telemetry if enabled
        if (showInfo)
        {
            telemetry.addData("Intake Target", motorIntake.getTargetPosition());
            telemetry.addData("Intake Current Position", motorIntake.getCurrentPosition());
        }
    }
}
