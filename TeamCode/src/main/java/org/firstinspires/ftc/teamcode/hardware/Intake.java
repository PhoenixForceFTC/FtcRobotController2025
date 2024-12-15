package org.firstinspires.ftc.teamcode.hardware;

import static java.lang.Thread.sleep;

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
    public String spinState = "OFF";  //--- Tracks spinner state
    public String liftState = "IN";  //--- Tracks lift state
    private boolean isLiftIn = true;

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

    public void initialize()
    {
        liftState = "IN";
        spinState = "OFF";

        liftIn();

        MotorUtils.moveToTargetPosition(motorIntake, 0, 1.0);

        //MotorUtils.setPower(motorIntake,0);

        //MotorUtils.brakeAtTargetPosition(motorIntake, 0, 1.0);
    }

    //--- Handles intake motor power based on gamepad input
    public void intakeByPower()
    {
        //--- Configure motors
        MotorUtils.configureForPower(motorIntake);

        //--- Handle extension and retraction with lift state checks
        if (gamepad.left_trigger > 0.1)
        {
            spinIn(); //--- Automatically spin in
            motorIntake.setPower(gamepad.left_trigger); //--- Scale power by trigger pressure

        }
        else if (gamepad.left_bumper)
        {
            spinOff(); //--- Automatically stop spinning
            motorIntake.setPower(-1); //--- Full power in reverse
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
        //--- Handle extension and retraction with lift state checks
        if (gamepad.left_trigger > 0.1)
        {
            MotorUtils.moveToTargetPosition(motorIntake, MOTOR_INTAKE_MAX_POSITION, 1.0);
            spinIn(); //--- Automatically spin in
        }
        else if (gamepad.left_bumper)
        {
            MotorUtils.moveToTargetPosition(motorIntake, MOTOR_INTAKE_MIN_POSITION, 1.0);
            spinOff(); //--- Automatically stop spinning
        }
//        else
//        {
//            //--- Stop the motor when no input
//            MotorUtils.stopMotor(motorIntake);
//        }

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

    //--- Handles lift controls based on gamepad input

    public void setLiftControls()
    {
        //--- Toggle lift position on 'x' button press
        if (gamepad.x)
        {
            if (isLiftIn)
            {
                liftDrop(); //--- Drop intake
            }
            else
            {
                liftIn(); //--- Retract intake into robot
            }

            //--- Toggle the state
            isLiftIn = !isLiftIn;

            //--- Add a small delay to prevent repeated toggling due to button hold
            try
            {
                Thread.sleep(250); // Adjust the delay as needed
            }
            catch (InterruptedException e) { }
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
