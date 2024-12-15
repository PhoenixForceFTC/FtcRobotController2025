package org.firstinspires.ftc.teamcode.utils;

//region --- Imports ---
import com.qualcomm.robotcore.hardware.DcMotor;
//endregion

public class MotorUtils
{
    //--- Sets a motor to run to a specific target position with specified power
    public static void moveToTargetPosition(DcMotor motor, int targetPosition, double power)
    {
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
    }

    //--- Moves the motor to a target position and sets it to brake/hold the position
    public static void brakeAtTargetPosition(DcMotor motor, int targetPosition, double power)
    {
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);

        //--- Wait until the motor reaches the target position
        while (motor.isBusy())
        {
//            //--- Optionally add a small delay to prevent hogging the CPU
//            try
//            {
//                Thread.sleep(10);
//            }
//            catch (InterruptedException e)
//            {
//                //--- Handle exception if necessary
//            }
        }

        //--- Set motor mode to BRAKE to hold position
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //--- Set power to zero to stop actively driving the motor
        motor.setPower(0);
    }

    //--- Sets power for a motor without changing mode
    public static void setPower(DcMotor motor, double power)
    {
        motor.setPower(power);
    }

    //--- Stops a motor by setting power to zero
    public static void stopMotor(DcMotor motor)
    {
        setPower(motor, 0);
    }

    //--- Resets the encoder for the motor
    public static void resetEncoder(DcMotor motor)
    {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    //--- Configures a motor to use encoders for regular movement
    public static void configureForEncoder(DcMotor motor)
    {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //--- Sets the motor to run without encoders
    public static void configureForPower(DcMotor motor)
    {
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //--- Retrieves the current position of the motor
    public static int getCurrentPosition(DcMotor motor)
    {
        return motor.getCurrentPosition();
    }
}
