package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MotorUtils
{
    //--- Sets a motor to run to a specific target position with specified power
    public static void setTargetPosition(DcMotor motor, int targetPosition, double power)
    {
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
    }

    //--- Stops a motor by setting power to zero
    public static void stopMotor(DcMotor motor)
    {
        motor.setPower(0);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //--- Resets the encoder for the motor
    public static void resetEncoder(DcMotor motor)
    {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //--- Configures a motor to use encoders for regular movement
    public static void configureForEncoderMode(DcMotor motor)
    {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //--- Sets the motor to run without encoders
    public static void configureForNoEncoderMode(DcMotor motor)
    {
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //--- Retrieves the current position of the motor
    public static int getCurrentPosition(DcMotor motor)
    {
        return motor.getCurrentPosition();
    }

    //--- Sets power for a motor without changing mode
    public static void setPower(DcMotor motor, double power)
    {
        motor.setPower(power);
    }
}
