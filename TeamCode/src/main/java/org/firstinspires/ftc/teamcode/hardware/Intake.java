package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.MotorUtils;
import org.firstinspires.ftc.teamcode.utils.ServoUtils;

public class Intake
{
    //--- Constants for motorIntake positions
    private static final int MOTOR_INTAKE_MAX_POSITION = 150; //--- Maximum position (fully extended)
    private static final int MOTOR_INTAKE_MIN_POSITION = -30; //--- Minimum position (fully retracted)
    private static final double SERVO_INTAKE_LIFT_IN = 0.85;  //--- Lift in position
    private static final double SERVO_INTAKE_LIFT_OUT = 0.43; //--- Lift out position

    //--- State variables
    private static String spinState = "OFF";  //--- Tracks spinner state
    private static String liftState = "IN";  //--- Tracks lift state

    //--- Hardware components
    private final DcMotor motorIntake;
    private final CRServo servoIntakeSpinLeft;
    private final CRServo servoIntakeSpinRight;
    private final Servo servoIntakeLiftLeft;
    private final Servo servoIntakeLiftRight;

    //--- Gamepad and Telemetry
    private final Gamepad gamepad;
    private final Telemetry telemetry;
    private final boolean showInfo;

    //region --- Constructor ---
    public Intake(
            DcMotor motorIntake,
            CRServo servoIntakeSpinLeft, CRServo servoIntakeSpinRight,
            Servo servoIntakeLiftLeft, Servo servoIntakeLiftRight,
            Gamepad gamepad, Telemetry telemetry, boolean showInfo
    )
    {
        this.motorIntake = motorIntake;
        this.servoIntakeSpinLeft = servoIntakeSpinLeft;
        this.servoIntakeSpinRight = servoIntakeSpinRight;
        this.servoIntakeLiftLeft = servoIntakeLiftLeft;
        this.servoIntakeLiftRight = servoIntakeLiftRight;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
        this.showInfo = showInfo;
    }
    //endregion

    //--- Handles intake motor power based on gamepad input
    public void intakeByPower()
    {
        //--- Configure motors
        MotorUtils.configureForPower(motorIntake);

        //--- Handle extension and retraction with lift state checks
        if (gamepad.left_trigger > 0.1)
        {
            motorIntake.setPower(gamepad.left_trigger); //--- Scale power by trigger pressure
            spinIn(); //--- Automatically spin in
            if (liftState.equals("IN"))
            {
                liftDrop(); //--- Automatically drop lift
            }
        }
        else if (gamepad.left_bumper)
        {
            motorIntake.setPower(-1); //--- Full power in reverse
            spinOff(); //--- Automatically stop spinning
        }
        else
        {
            motorIntake.setPower(0); //--- Stop motorIntake when no input
        }

        //--- Call spinner controls
        setSpinControls();

        //--- Show telemetry if enabled
        if (showInfo)
        {
            telemetry.addData("Intake -> Motor Power", "%4.2f", motorIntake.getPower());
            telemetry.addData("Intake -> Current Position", motorIntake.getCurrentPosition());
            telemetry.addData("Intake -> Spinner", spinState);
            telemetry.addData("Intake -> Lift State", liftState);
            telemetry.addData("Intake -> Lift Left", "%4.2f", servoIntakeLiftLeft.getPosition());
            telemetry.addData("Intake -> Lift Right", "%4.2f", servoIntakeLiftRight.getPosition());
        }
    }

    //--- Handles intake motor movement based on encoder positions
    public void intakeByEncoder()
    {
        //--- Reset and configure motors
        MotorUtils.configureForEncoder(motorIntake);

        //--- Handle extension and retraction with lift state checks
        if (gamepad.left_trigger > 0.1)
        {
            MotorUtils.setTargetPosition(motorIntake, MOTOR_INTAKE_MAX_POSITION, 1.0);
            spinIn(); //--- Automatically spin in
            if (liftState.equals("IN"))
            {
                liftDrop(); //--- Automatically drop lift
            }
        }
        else if (gamepad.left_bumper)
        {
            MotorUtils.setTargetPosition(motorIntake, MOTOR_INTAKE_MIN_POSITION, 1.0);
            spinOff(); //--- Automatically stop spinning
        }
        else
        {
            //--- Stop the motor when no input
            MotorUtils.stopMotor(motorIntake);
        }

        //--- Call spinner controls
        setSpinControls();

        //--- Show telemetry if enabled
        if (showInfo)
        {
            telemetry.addData("Intake -> Motor Power", "%4.2f", motorIntake.getPower());
            telemetry.addData("Intake -> Target Position", motorIntake.getTargetPosition());
            telemetry.addData("Intake -> Current Position", motorIntake.getCurrentPosition());
            telemetry.addData("Intake -> Spinner", spinState);
            telemetry.addData("Intake -> Lift State", liftState);
            telemetry.addData("Intake -> Lift Left", "%4.2f", servoIntakeLiftLeft.getPosition());
            telemetry.addData("Intake -> Lift Right", "%4.2f", servoIntakeLiftRight.getPosition());
        }
    }

    //region --- Spinners ---

    //--- Handles spinner controls based on gamepad input
    private void setSpinControls()
    {
        if (gamepad.right_trigger > 0.1)
        {
            spinIn();
        }
        else if (gamepad.right_bumper)
        {
            spinOut();
        }
    }

    //--- Method to spin intake inwards
    public void spinIn()
    {
        servoIntakeSpinLeft.setPower(1);  //--- Spin left servo forward
        servoIntakeSpinRight.setPower(-1); //--- Spin right servo backward
        spinState = "IN"; //--- Update spin state
    }

    //--- Method to spin intake outwards
    public void spinOut()
    {
        servoIntakeSpinLeft.setPower(-1); //--- Spin left servo backward
        servoIntakeSpinRight.setPower(1); //--- Spin right servo forward
        spinState = "OUT"; //--- Update spin state
    }

    //--- Method to stop the intake spinner
    public void spinOff()
    {
        servoIntakeSpinLeft.setPower(0); //--- Stop left servo
        servoIntakeSpinRight.setPower(0); //--- Stop right servo
        spinState = "OFF"; //--- Update spin state
    }

    //endregion

    //region --- Lift ---

    //--- Test Method for Manually Controlling Intake Lift
    public void testLiftControl()
    {
        //--- Gamepad Button Assignments
        if (gamepad.y) //--- Y Button: Set to Lift In
        {
            liftIn();
            if (showInfo) telemetry.addData("Lift -> Action", "IN");
        }
        else if (gamepad.b) //--- B Button: Set to Lift Hold
        {
            liftHold();
            if (showInfo) telemetry.addData("Lift -> Action", "HOLD");
        }
        else if (gamepad.a) //--- A Button: Set to Lift Drop
        {
            liftDrop();
            if (showInfo) telemetry.addData("Lift -> Action", "DROP");
        }

        //--- Show Telemetry for Lift State
        if (showInfo)
        {
            telemetry.addData("Lift -> Left Servo Position", "%4.2f", servoIntakeLiftLeft.getPosition());
            telemetry.addData("Lift -> Right Servo Position", "%4.2f", servoIntakeLiftRight.getPosition());
            telemetry.addData("Lift -> Current State", liftState);
        }
    }

    //--- Moves the intake lift to the in position
    public void liftIn()
    {
        servoIntakeLiftLeft.setPosition(SERVO_INTAKE_LIFT_IN);
        servoIntakeLiftRight.setPosition(SERVO_INTAKE_LIFT_IN);
        liftState = "IN";
    }

    //--- Moves the intake lift to the out position and holds
    public void liftHold()
    {
        servoIntakeLiftLeft.setPosition(SERVO_INTAKE_LIFT_OUT);
        servoIntakeLiftRight.setPosition(SERVO_INTAKE_LIFT_OUT);
        liftState = "HOLD";
    }

    //--- Moves the intake lift to the out position and drops
    public void liftDrop()
    {
        ServoUtils.moveToPositionAndDisable(servoIntakeLiftLeft, SERVO_INTAKE_LIFT_OUT, 750);
        ServoUtils.moveToPositionAndDisable(servoIntakeLiftRight, SERVO_INTAKE_LIFT_OUT, 750);
        liftState = "DROP";
    }

    //endregion
}
