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
    //region --- Constants ---
    private static final int MOTOR_INTAKE_MAX_POSITION = 150; //--- Maximum position (fully extended)
    private static final int MOTOR_INTAKE_MIN_POSITION = -30; //--- Minimum position (fully retracted)
    private static final double SERVO_INTAKE_LIFT_IN = 0.85;  //--- Lift in position
    private static final double SERVO_INTAKE_LIFT_OUT = 0.43; //--- Lift out position
    //endregion

    //region --- Variables ---
    public String _spinState = "OFF";  //--- Tracks spinner state
    public String _liftState = "IN";  //--- Tracks lift state
    private boolean _isLiftIn = true;
    //endregion

    //region --- Hardware ---
    private final DcMotor _motorIntake;
    private final CRServo _servoIntakeSpinLeft;
    private final CRServo _servoIntakeSpinRight;
    private final Servo _servoIntakeLiftLeft;
    private final Servo _servoIntakeLiftRight;
    private final Gamepad _gamepad;
    private final Telemetry _telemetry;
    private final boolean _showInfo;
    //endregion

    //region --- Constructor ---
    public Intake(
            DcMotor motorIntake,
            CRServo servoIntakeSpinLeft, CRServo servoIntakeSpinRight,
            Servo servoIntakeLiftLeft, Servo servoIntakeLiftRight,
            Gamepad gamepad, Telemetry telemetry, boolean showInfo
    )
    {
        this._motorIntake = motorIntake;
        this._servoIntakeSpinLeft = servoIntakeSpinLeft;
        this._servoIntakeSpinRight = servoIntakeSpinRight;
        this._servoIntakeLiftLeft = servoIntakeLiftLeft;
        this._servoIntakeLiftRight = servoIntakeLiftRight;
        this._gamepad = gamepad;
        this._telemetry = telemetry;
        this._showInfo = showInfo;
    }
    //endregion

    public void initialize()
    {
        _liftState = "IN";
        _spinState = "OFF";

        liftIn();

        //--- Keep the intake in place by default
        MotorUtils.moveToTargetPosition(_motorIntake, 0, 1.0);
    }

    //--- Handles intake motor power based on gamepad input
    public void intakeByPower()
    {
        //--- Configure motors
        MotorUtils.configureForPower(_motorIntake);

        //--- Handle extension and retraction with lift state checks
        if (_gamepad.left_trigger > 0.1)
        {
            spinIn(); //--- Automatically spin in
            _motorIntake.setPower(_gamepad.left_trigger); //--- Scale power by trigger pressure

        }
        else if (_gamepad.left_bumper)
        {
            spinOff(); //--- Automatically stop spinning
            _motorIntake.setPower(-1); //--- Full power in reverse
        }
        else
        {
            _motorIntake.setPower(0); //--- Stop motorIntake when no input
        }

        //--- Show telemetry if enabled
        if (_showInfo)
        {
            _telemetry.addData("Intake -> Motor Power", "%4.2f", _motorIntake.getPower());
            _telemetry.addData("Intake -> Current Position", _motorIntake.getCurrentPosition());
            _telemetry.addData("Intake -> Spinner", _spinState);
            _telemetry.addData("Intake -> Lift State", _liftState);
            _telemetry.addData("Intake -> Lift Left", "%4.2f", _servoIntakeLiftLeft.getPosition());
            _telemetry.addData("Intake -> Lift Right", "%4.2f", _servoIntakeLiftRight.getPosition());
        }
    }

    //--- Handles intake motor movement based on encoder positions
    public void intakeByEncoder()
    {
        //--- Handle extension and retraction with lift state checks
        if (_gamepad.left_trigger > 0.1)
        {
            MotorUtils.moveToTargetPosition(_motorIntake, MOTOR_INTAKE_MAX_POSITION, 1.0);
            spinIn(); //--- Automatically spin in
        }
        else if (_gamepad.left_bumper)
        {
            MotorUtils.moveToTargetPosition(_motorIntake, MOTOR_INTAKE_MIN_POSITION, 1.0);
            spinOff(); //--- Automatically stop spinning
        }
        else
        {
            //--- Only turn off the motor when not close to robot, this allows for the responsive
            //---  control for the user moving it in and out -- and also when set to brake at zero
            //---  power this makes it so the arm is locked into position when close to robot
            if (_motorIntake.getCurrentPosition() > 20)
            {
                //--- Stop the motor when no input
                MotorUtils.stopMotor(_motorIntake);
            }
        }

        //--- Show telemetry if enabled
        if (_showInfo)
        {
            _telemetry.addData("Intake -> Motor Power", "%4.2f", _motorIntake.getPower());
            _telemetry.addData("Intake -> Target Position", _motorIntake.getTargetPosition());
            _telemetry.addData("Intake -> Current Position", _motorIntake.getCurrentPosition());
            _telemetry.addData("Intake -> Spinner", _spinState);
            _telemetry.addData("Intake -> Lift State", _liftState);
            _telemetry.addData("Intake -> Lift Left", "%4.2f", _servoIntakeLiftLeft.getPosition());
            _telemetry.addData("Intake -> Lift Right", "%4.2f", _servoIntakeLiftRight.getPosition());
        }
    }

    //region --- Spinners ---

    //--- Handles spinner controls based on gamepad input
    public void setSpinnerControls()
    {
        if (_gamepad.right_trigger > 0.1)
        {
            spinIn();
        }
        else if (_gamepad.right_bumper)
        {
            spinOut();
        }
    }

    //--- Method to spin intake inwards
    public void spinIn()
    {
        _servoIntakeSpinLeft.setPower(1);  //--- Spin left servo forward
        _servoIntakeSpinRight.setPower(-1); //--- Spin right servo backward
        _spinState = "IN"; //--- Update spin state
    }

    //--- Method to spin intake outwards
    public void spinOut()
    {
        _servoIntakeSpinLeft.setPower(-1); //--- Spin left servo backward
        _servoIntakeSpinRight.setPower(1); //--- Spin right servo forward
        _spinState = "OUT"; //--- Update spin state
    }

    //--- Method to stop the intake spinner
    public void spinOff()
    {
        _servoIntakeSpinLeft.setPower(0); //--- Stop left servo
        _servoIntakeSpinRight.setPower(0); //--- Stop right servo
        _spinState = "OFF"; //--- Update spin state
    }

    //endregion

    //region --- Lift ---

    //--- Handles lift controls based on gamepad input

    public void setLiftArmControls()
    {
        //--- Toggle lift position on 'x' button press
        if (_gamepad.x)
        {
            if (_isLiftIn)
            {
                liftDrop(); //--- Drop intake
            }
            else
            {
                liftIn(); //--- Retract intake into robot
            }

            //--- Toggle the state
            _isLiftIn = !_isLiftIn;

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
        _servoIntakeLiftLeft.setPosition(SERVO_INTAKE_LIFT_IN);
        _servoIntakeLiftRight.setPosition(SERVO_INTAKE_LIFT_IN);
        _liftState = "IN";
    }

    //--- Moves the intake lift to the out position and holds
    public void liftHold()
    {
        _servoIntakeLiftLeft.setPosition(SERVO_INTAKE_LIFT_OUT);
        _servoIntakeLiftRight.setPosition(SERVO_INTAKE_LIFT_OUT);
        _liftState = "HOLD";
    }

    //--- Moves the intake lift to the out position and drops
    public void liftDrop()
    {
        ServoUtils.moveToPositionAndDisable(_servoIntakeLiftLeft, SERVO_INTAKE_LIFT_OUT, 750);
        ServoUtils.moveToPositionAndDisable(_servoIntakeLiftRight, SERVO_INTAKE_LIFT_OUT, 750);
        _liftState = "DROP";
    }

    //endregion
}
