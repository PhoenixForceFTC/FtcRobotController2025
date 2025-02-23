package org.firstinspires.ftc.teamcode.hardware;

//region --- Imports ---
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.MotorUtils;
import org.firstinspires.ftc.teamcode.utils.ServoUtils;
//endregion

public class Intake
{
    //region --- Constants ---
    private static int MOTOR_INTAKE_MAX_POSITION = 1200; //--- Maximum position (fully extended)
    private static int MOTOR_INTAKE_MIN_POSITION = -30; //--- Minimum position (fully retracted)
    private static double SERVO_INTAKE_LIFT_IN = 0.85;  //--- Lift in position
    private static double SERVO_INTAKE_LIFT_OUT = 0.43; //--- Lift out position

    //--- Constants for incremental adjustments
    private static int INCREMENT_UP = 5;    //--- Increment value when extending
    private static int INCREMENT_DOWN = 50;  //--- Decrement value when retracting
    //endregion

    //region --- Variables ---
    public String _spinState = "OFF";  //--- Tracks spinner state
    public String _liftState = "IN";  //--- Tracks lift state

    //--- Variable to track the current target position for the intake motor
    private int _currentIntakeTargetPosition = MOTOR_INTAKE_MIN_POSITION;
    //endregion

    //region --- Hardware ---
    private final DcMotor _motorIntake;
    private final CRServo _servoIntakeSpinLeft;
    private final CRServo _servoIntakeSpinRight;
    private final Servo _servoIntakeLiftLeft;
    private final Servo _servoIntakeLiftRight;
    private final Gamepad _gamepad1;
    private final Gamepad _gamepad2;
    private final Telemetry _telemetry;
    private final boolean _showInfo;

    private int _robotVersion;
    //endregion

    //region --- Constructor ---
    public Intake(
            DcMotor motorIntake,
            CRServo servoIntakeSpinLeft, CRServo servoIntakeSpinRight,
            Servo servoIntakeLiftLeft, Servo servoIntakeLiftRight,
            Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, int robotVersion, boolean showInfo
    )
    {
        this._motorIntake = motorIntake;
        this._servoIntakeSpinLeft = servoIntakeSpinLeft;
        this._servoIntakeSpinRight = servoIntakeSpinRight;
        this._servoIntakeLiftLeft = servoIntakeLiftLeft;
        this._servoIntakeLiftRight = servoIntakeLiftRight;
        this._gamepad1 = gamepad1;
        this._gamepad2 = gamepad2;
        this._telemetry = telemetry;
        this._showInfo = showInfo;
    }
    //endregion

    //region --- Initialize ---
    public void initialize()
    {
        _liftState = "IN";
        _spinState = "OFF";

        if (_robotVersion == 1) //--- CRAB-IER
        {
            MOTOR_INTAKE_MAX_POSITION = 1200; //--- Maximum position (fully extended)
            MOTOR_INTAKE_MIN_POSITION = -30; //--- Minimum position (fully retracted)
            SERVO_INTAKE_LIFT_IN = 0.85;  //--- Lift in position
            SERVO_INTAKE_LIFT_OUT = 0.43; //--- Lift out position
            //endregion

            //--- Constants for incremental adjustments
            INCREMENT_UP = 5;    //--- Increment value when extending
            INCREMENT_DOWN = 50;  //--- Decrement value when retracting
        }
        else //--- ARIEL
        {
            MOTOR_INTAKE_MAX_POSITION = 1200; //--- Maximum position (fully extended)
            MOTOR_INTAKE_MIN_POSITION = -30; //--- Minimum position (fully retracted)
            SERVO_INTAKE_LIFT_IN = 0.85;  //--- Lift in position
            SERVO_INTAKE_LIFT_OUT = 0.43; //--- Lift out position
            //endregion

            //--- Constants for incremental adjustments
            INCREMENT_UP = 5;    //--- Increment value when extending
            INCREMENT_DOWN = 50;  //--- Decrement value when retracting
        }

        //--- Keep the intake in place by default
        //TODO check about reducing power -- see if that overheats
//        MotorUtils.moveToTargetPosition(_motorIntake, 0, 1.0);
    }
    //endregion

    //region --- Control Mapping ---

    //--- Handles intake motor power based on gamepad input
    public void intakeByPower()
    {
        //--- Configure motors
        MotorUtils.configureForPower(_motorIntake);

        //--- Handle extension and retraction with lift state checks
        if (_gamepad1.left_trigger > 0.1)
        {
            spinIn(); //--- Automatically spin in
            _motorIntake.setPower(_gamepad1.left_trigger); //--- Scale power by trigger pressure

        }
        else if (_gamepad1.left_bumper)
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

    //--- Handles intake motor movement based on encoder positions using incremental control
    public void intakeByEncoder()
    {
        //--- Handle extension and retraction with lift state checks using incremental target position
        if (_gamepad1.left_trigger > 0.1)
        {
            _lastControlMode = ControlMode.ENCODER;

            //--- Increment the target position by INCREMENT_UP when extending
            _currentIntakeTargetPosition += INCREMENT_UP;
            //--- Bound the target position to the maximum allowed
            if (_currentIntakeTargetPosition > MOTOR_INTAKE_MAX_POSITION)
            {
                _currentIntakeTargetPosition = MOTOR_INTAKE_MAX_POSITION;
            }
            //--- Command the motor to move to the updated target position
            MotorUtils.moveToTargetPosition(_motorIntake, _currentIntakeTargetPosition, 1.0);
            spinIn(); //--- Automatically spin in
        }
        else if (_gamepad1.left_bumper)
        {
            _lastControlMode = ControlMode.ENCODER;

            //--- Decrement the target position by INCREMENT_DOWN when retracting
            _currentIntakeTargetPosition -= INCREMENT_DOWN;
            //--- Bound the target position to the minimum allowed
            if (_currentIntakeTargetPosition < MOTOR_INTAKE_MIN_POSITION)
            {
                _currentIntakeTargetPosition = MOTOR_INTAKE_MIN_POSITION;
            }
            //--- Command the motor to move to the updated target position
            MotorUtils.moveToTargetPosition(_motorIntake, _currentIntakeTargetPosition, 1.0);
            spinOff(); //--- Automatically stop spinning
        }
        else
        {
            //--- Only turn off the motor when not close to the robot, ensuring responsive control
            if (_motorIntake.getCurrentPosition() > 20)
            {
                //--- Stop the motor when no input is detected
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

    public void intakeByEncoderFullSpeed()
    {
        //--- Handle extension and retraction with lift state checks
        if (_gamepad1.left_trigger > 0.1)
        {
            _lastControlMode = ControlMode.ENCODER;

            MotorUtils.moveToTargetPosition(_motorIntake, MOTOR_INTAKE_MAX_POSITION, 1.0);
            spinIn(); //--- Automatically spin in
        }
        else if (_gamepad1.left_bumper)
        {
            _lastControlMode = ControlMode.ENCODER;

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

    // Add this field to your class
    private boolean _isLiftIn = true;
    private boolean _isHold = false;

    public void setLiftArmControls()
    {
        //--- Toggle lift position on 'x' button press (between intakeIn and intakeDrop)
        if (_gamepad1.x)
        {
            if (_isLiftIn)
            {
                intakeDrop(); //--- Drop intake
            }
            else
            {
                intakeIn(); //--- Retract intake into robot
            }

            //--- Toggle the state
            _isLiftIn = !_isLiftIn;

            //--- Add a small delay to prevent repeated toggling due to button hold
            try
            {
                Thread.sleep(250); //--- Adjust the delay as needed
            }
            catch (InterruptedException e)
            {
            }
        }

        //--- Toggle hold position on 'b' button press (between intakeHold and intakeDrop)
        if (_gamepad1.b)
        {
            if (_isHold)
            {
                intakeDrop(); //--- Drop intake
            }
            else
            {
                intakeHold(); //--- Hold intake
            }

            //--- Toggle the hold state
            _isHold = !_isHold;

            //--- Add a small delay to prevent repeated toggling due to button hold
            try
            {
                Thread.sleep(250); //--- Adjust the delay as needed
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public void controlArm() {



        if (_gamepad2.dpad_right)
        {
            intakeByPowerIn(1.0);
        }
        else if (_gamepad2.dpad_left)
        {
            intakeByPowerOut(1.0);
        }
        else
        {
            stopIntakeIfManual(); //--- Stop if running manually
        }

        //--- Reset the intake encoder
        if (_gamepad2.right_stick_button)
        {
            intakeResetEncoder();
        }
    }

    //endregion

    //region --- Manual Control ---

    //--- Keeps track of the last control mode
    private enum ControlMode {
        MANUAL, //--- Controlled by liftByPowerUp or liftByPowerDown
        ENCODER, //--- Controlled by encoder position methods
        NONE //--- No active control (e.g., stopped)
    }

    private ControlMode _lastControlMode = ControlMode.NONE;

    public void intakeByPowerOut(double power)
    {
        //--- Ensure power is positive for upward movement
        power = Math.abs(power);

        //--- Call the centralized liftByPower method
        intakeByPower(power);
    }

    //--- Moves the lift down by power and resets the encoder
    public void intakeByPowerIn(double power)
    {
        //--- Ensure power is positive, then make it negative for downward movement
        power = -Math.abs(power);

        //--- Call the centralized liftByPower method
        intakeByPower(power);
    }

    public void stopIntakeIfManual()
    {
        if (_lastControlMode == Intake.ControlMode.MANUAL)
        {
            MotorUtils.stopMotor(_motorIntake);

            //--- Update the control mode to NONE
            _lastControlMode = Intake.ControlMode.NONE;
        }
    }

    //--- Reset the encoder
    public void intakeResetEncoder()
    {
        //--- Reset encoder values for motor
        _motorIntake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //--- Reconfigure for power mode after resetting
        MotorUtils.configureForPower(_motorIntake);

        //--- Show telemetry if enabled
        if (_showInfo)
        {
            _telemetry.addData("Intake -> Encoder Reset", "Intake encoder reset to zero.");
        }
    }

    //--- Moves the lift by specified power
    private void intakeByPower(double power)
    {
        _lastControlMode = Intake.ControlMode.MANUAL;

        //--- Configure motor for no encoder mode
        MotorUtils.configureForPower(_motorIntake);

        //--- Set the power to motor
        MotorUtils.setPower(_motorIntake, power);

        //--- Show telemetry if enabled
        if (_showInfo)
        {
            _telemetry.addData("Intake -> Power", "%4.2f", _motorIntake.getPower());
        }
    }

    //endregion

    //region --- Spinners ---

    //--- Handles spinner controls based on gamepad input
    public void setSpinnerControls()
    {
        if (_gamepad1.right_trigger > 0.1)
        {
            spinIn();
        }
        else if (_gamepad1.right_bumper)
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

    //--- Moves the intake lift to the in position
    public void intakeIn()
    {
        _servoIntakeLiftLeft.setPosition(SERVO_INTAKE_LIFT_IN);
        _servoIntakeLiftRight.setPosition(SERVO_INTAKE_LIFT_IN);
        _liftState = "IN";
    }

    //--- Moves the intake lift to the out position and holds
    public void intakeHold()
    {
        _servoIntakeLiftLeft.setPosition(SERVO_INTAKE_LIFT_OUT);
        _servoIntakeLiftRight.setPosition(SERVO_INTAKE_LIFT_OUT);
        _liftState = "HOLD";
    }

    //--- Moves the intake lift to the out position and drops
    public void intakeDrop()
    {
        ServoUtils.moveToPositionAndDisable(_servoIntakeLiftLeft, SERVO_INTAKE_LIFT_OUT, 750);
        ServoUtils.moveToPositionAndDisable(_servoIntakeLiftRight, SERVO_INTAKE_LIFT_OUT, 750);
        _liftState = "DROP";
    }

    //endregion
}
