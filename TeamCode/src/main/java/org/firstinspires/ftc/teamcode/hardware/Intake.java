package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.MotorUtils;

public class Intake
{
    //--- Constants for motorIntake positions
    private static final int MOTOR_INTAKE_MAX_POSITION = 150; // Maximum position (fully extended)
    private static final int MOTOR_INTAKE_MIN_POSITION = -30; // Minimum position (fully retracted)

    //--- State variable for spinner telemetry
    private static String spinState = "OFF"; // Default state

    //--- Handles intake motor power based on gamepad input
    public static void intakeByPower(DcMotor motorIntake, CRServo servoIntakeSpinLeft, CRServo servoIntakeSpinRight, Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        if (gamepad.left_trigger > 0.1) //--- Extend motorIntake when left trigger is pressed
        {
            motorIntake.setPower(gamepad.left_trigger); //--- Scale power by trigger pressure
            spinIn(servoIntakeSpinLeft, servoIntakeSpinRight); //--- Automatically spin in
        }
        else if (gamepad.left_bumper) //--- Retract motorIntake when left bumper is pressed
        {
            motorIntake.setPower(-1); //--- Full power in reverse
            spinOff(servoIntakeSpinLeft, servoIntakeSpinRight); //--- Automatically stop spinning
        }
        else
        {
            motorIntake.setPower(0); // Stop motorIntake when no input
        }

        // Call spinner controls
        setSpinControls(servoIntakeSpinLeft, servoIntakeSpinRight, gamepad);

        //--- Show telemetry if enabled
        if (showInfo)
        {
            telemetry.addData("Intake -> Motor Power", "%4.2f", motorIntake.getPower());
            telemetry.addData("Intake -> Current Position", motorIntake.getCurrentPosition());
            telemetry.addData("Intake -> Spinner", spinState);
        }
    }

    //--- Handles intake motor movement based on encoder positions
    public static void intakeByEncoder(DcMotor motorIntake, CRServo servoIntakeSpinLeft, CRServo servoIntakeSpinRight, Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        if (gamepad.left_trigger > 0.1) //--- Extend when left trigger is pressed
        {
            MotorUtils.setTargetPosition(motorIntake, MOTOR_INTAKE_MAX_POSITION, 1.0);
            spinIn(servoIntakeSpinLeft, servoIntakeSpinRight); //--- Automatically spin in
        }
        else if (gamepad.left_bumper) //--- Retract when left bumper is pressed
        {
            MotorUtils.setTargetPosition(motorIntake, MOTOR_INTAKE_MIN_POSITION, 1.0);
            spinOff(servoIntakeSpinLeft, servoIntakeSpinRight); //--- Automatically stop spinning
        }
        else
        {
            //--- Stop the motor when no input
            MotorUtils.stopMotor(motorIntake);
        }

        // Call spinner controls
        setSpinControls(servoIntakeSpinLeft, servoIntakeSpinRight, gamepad);

        //--- Show telemetry if enabled
        if (showInfo)
        {
            telemetry.addData("Intake -> Motor Power", "%4.2f", motorIntake.getPower());
            telemetry.addData("Intake -> Target Position", motorIntake.getTargetPosition());
            telemetry.addData("Intake -> Current Position", motorIntake.getCurrentPosition());
            telemetry.addData("Intake -> Spinner", spinState);
        }
    }

    //--- Handles spinner controls (spinIn and spinOut) based on gamepad input
    private static void setSpinControls(CRServo servoIntakeSpinLeft, CRServo servoIntakeSpinRight, Gamepad gamepad)
    {
        if (gamepad.right_trigger > 0.1) // Trigger pressed for spinIn
        {
            spinIn(servoIntakeSpinLeft, servoIntakeSpinRight);
        }
        else if (gamepad.right_bumper) // Bumper pressed for spinOut
        {
            spinOut(servoIntakeSpinLeft, servoIntakeSpinRight);
        }
    }

    //--- Method to spin intake inwards
    public static void spinIn(CRServo servoIntakeSpinLeft, CRServo servoIntakeSpinRight)
    {
        servoIntakeSpinLeft.setPower(1);  //--- Spin left servo forward
        servoIntakeSpinRight.setPower(-1); //--- Spin right servo backward
        spinState = "IN"; //--- Update spin state
    }

    //--- Method to spin intake outwards
    public static void spinOut(CRServo servoIntakeSpinLeft, CRServo servoIntakeSpinRight)
    {
        servoIntakeSpinLeft.setPower(-1); //--- Spin left servo backward
        servoIntakeSpinRight.setPower(1); //--- Spin right servo forward
        spinState = "OUT"; //--- Update spin state
    }

    //--- Method to stop the intake spinner
    public static void spinOff(CRServo servoIntakeSpinLeft, CRServo servoIntakeSpinRight)
    {
        servoIntakeSpinLeft.setPower(0); //--- Stop left servo
        servoIntakeSpinRight.setPower(0); //--- Stop right servo
        spinState = "OFF"; //--- Update spin state
    }
}
