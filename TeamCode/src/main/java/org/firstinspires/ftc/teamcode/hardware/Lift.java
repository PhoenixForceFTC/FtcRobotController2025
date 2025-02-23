package org.firstinspires.ftc.teamcode.hardware;

//region --- Imports ---
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.MotorUtils;
//endregion

public class Lift
{
    //region --- Constants ---
    private static int LIFT_TOP_BASKET_POSITION;
    private static int LIFT_LOW_BASKET_POSITION;
    private static int LIFT_BOTTOM_POSITION;

    private static int LIFT_DEL1_POSITION;
    private static int LIFT_DEL2_POSITION;

    private static int LIFT_;
    //endregion

    //region --- Hardware ---
    private final DcMotor _motorLiftLeft;
    private final DcMotor _motorLiftRight;
    private final Gamepad _gamepad;
    private final Telemetry _telemetry;
    private final boolean _showInfo;

    private int _robotVersion;
    //endregion

    //region --- Constructor
    public Lift(DcMotor motorLiftLeft, DcMotor motorLiftRight, Gamepad gamepad, Telemetry telemetry, int robotVersion, boolean showInfo)
    {
        this._motorLiftLeft = motorLiftLeft;
        this._motorLiftRight = motorLiftRight;
        this._gamepad = gamepad;
        this._telemetry = telemetry;
        this._robotVersion = robotVersion;
        this._showInfo = showInfo;
    }
    //endregion

    public void initialize()
    {
        if (_robotVersion == 1) //--- CRAB-IER
        {
            LIFT_TOP_BASKET_POSITION = 2000;
            LIFT_LOW_BASKET_POSITION = 650; //--- was 1200 -- for putting on the specimen
            LIFT_BOTTOM_POSITION = 0;

            LIFT_DEL1_POSITION = 300;
            LIFT_DEL2_POSITION = 900;
        }
        else //--- ARIEL
        {
            LIFT_TOP_BASKET_POSITION = 2000;
            LIFT_LOW_BASKET_POSITION = 650; //--- was 1200 -- for putting on the specimen
            LIFT_BOTTOM_POSITION = 0;

            LIFT_DEL1_POSITION = 300;
            LIFT_DEL2_POSITION = 900;
        }
    }

    //--- Handles lifting using power
    public void liftByPower()
    {
        //--- Configure motors for no encoder mode
        MotorUtils.configureForPower(_motorLiftLeft);
        MotorUtils.configureForPower(_motorLiftRight);

        if (_gamepad.left_trigger > 0.1) //--- Extend the lift
        {
            MotorUtils.setPower(_motorLiftLeft, _gamepad.left_trigger);
            MotorUtils.setPower(_motorLiftRight, _gamepad.left_trigger);
        }
        else if (_gamepad.left_bumper) //--- Retract the lift
        {
            MotorUtils.setPower(_motorLiftLeft, -1.0); // Full reverse power
            MotorUtils.setPower(_motorLiftRight, -1.0);
        }
        else
        {
            MotorUtils.stopMotor(_motorLiftLeft); // Stop motors
            MotorUtils.stopMotor(_motorLiftRight);
        }

        //--- Show telemetry if enabled
        if (_showInfo)
        {
            _telemetry.addData("Lift -> Power Left", "%4.2f", _motorLiftLeft.getPower());
            _telemetry.addData("Lift -> Power Right", "%4.2f", _motorLiftRight.getPower());
        }
    }

    //--- Handles lifting using encoder positions
    public void liftByEncoder()
    {
        if (_gamepad.y)
        {
            moveToHighBasket();
        }
        else if (_gamepad.b)
        {
            moveToLowBasket();
        }
        else if (_gamepad.a)
        {
            moveToBottom();
        }

        //--- Show telemetry if enabled
        if (_showInfo)
        {
            _telemetry.addData("Lift -> Target Position Left", _motorLiftLeft.getTargetPosition());
            _telemetry.addData("Lift -> Target Position Right", _motorLiftRight.getTargetPosition());
            _telemetry.addData("Lift -> Current Position Left", MotorUtils.getCurrentPosition(_motorLiftLeft));
            _telemetry.addData("Lift -> Current Position Right", MotorUtils.getCurrentPosition(_motorLiftRight));
        }
    }

    //--- Keeps track of the last control mode
    private enum LiftControlMode {
        MANUAL, //--- Controlled by liftByPowerUp or liftByPowerDown
        ENCODER, //--- Controlled by encoder position methods
        NONE //--- No active control (e.g., stopped)
    }

    private LiftControlMode _lastControlMode = LiftControlMode.NONE;

    //--- Moves the lift up by power
    public void liftByPowerUp(double power)
    {
        //--- Ensure power is positive for upward movement
        power = Math.abs(power);

        //--- Call the centralized liftByPower method
        liftByPower(power);
    }

    //--- Moves the lift down by power and resets the encoder
    public void liftByPowerDown(double power)
    {
        //--- Ensure power is positive, then make it negative for downward movement
        power = -Math.abs(power);

        //--- Call the centralized liftByPower method
        liftByPower(power);
    }

    //--- Reset the encoder
    public void liftResetEncoder()
    {
        //--- Reset encoder values for both motors
        _motorLiftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _motorLiftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //--- Reconfigure for power mode after resetting
        MotorUtils.configureForPower(_motorLiftLeft);
        MotorUtils.configureForPower(_motorLiftRight);

        //--- Show telemetry if enabled
        if (_showInfo)
        {
            _telemetry.addData("Lift -> Encoder Reset", "Lift encoders reset to zero.");
        }
    }

    //--- Moves the lift by specified power
    private void liftByPower(double power)
    {
        //--- Set the control mode to manual
        _lastControlMode = LiftControlMode.MANUAL;

        //--- Configure motors for no encoder mode
        MotorUtils.configureForPower(_motorLiftLeft);
        MotorUtils.configureForPower(_motorLiftRight);

        //--- Set the power to both motors
        MotorUtils.setPower(_motorLiftLeft, power);
        MotorUtils.setPower(_motorLiftRight, power);

        //--- Show telemetry if enabled
        if (_showInfo)
        {
            _telemetry.addData("Lift -> Power Left", "%4.2f", _motorLiftLeft.getPower());
            _telemetry.addData("Lift -> Power Right", "%4.2f", _motorLiftRight.getPower());
        }
    }

    //--- Stops the lift only if the last control was manual
    public void stopLiftIfManual()
    {
        if (_lastControlMode == LiftControlMode.MANUAL)
        {
            MotorUtils.stopMotor(_motorLiftLeft);
            MotorUtils.stopMotor(_motorLiftRight);

            //--- Update the control mode to NONE
            _lastControlMode = LiftControlMode.NONE;
        }
    }

    public void moveToHighBasket()
    {
        _lastControlMode = LiftControlMode.ENCODER;
        MotorUtils.moveToTargetPosition(_motorLiftLeft, LIFT_TOP_BASKET_POSITION, -1.0);
        MotorUtils.moveToTargetPosition(_motorLiftRight, LIFT_TOP_BASKET_POSITION, -1.0);
    }

    public void moveToLowBasket()
    {
        _lastControlMode = LiftControlMode.ENCODER;
        MotorUtils.moveToTargetPosition(_motorLiftLeft, LIFT_LOW_BASKET_POSITION, -1.0);
        MotorUtils.moveToTargetPosition(_motorLiftRight, LIFT_LOW_BASKET_POSITION, -1.0);
    }

    public void moveToDel1()
    {
        _lastControlMode = LiftControlMode.ENCODER;
        MotorUtils.moveToTargetPosition(_motorLiftLeft, LIFT_DEL1_POSITION, -1.0);
        MotorUtils.moveToTargetPosition(_motorLiftRight, LIFT_DEL1_POSITION, -1.0);
    }

    public void moveToDel2()
    {
        _lastControlMode = LiftControlMode.ENCODER;
        MotorUtils.moveToTargetPosition(_motorLiftLeft, LIFT_DEL2_POSITION, -1.0);
        MotorUtils.moveToTargetPosition(_motorLiftRight, LIFT_DEL2_POSITION, -1.0);
    }

    public void getTelemetry(){
        _telemetry.addData("Lift -> Target Position Left", _motorLiftLeft.getTargetPosition());
        _telemetry.addData("Lift -> Target Position Right", _motorLiftRight.getTargetPosition());
        _telemetry.addData("Lift -> Current Position Left", MotorUtils.getCurrentPosition(_motorLiftLeft));
        _telemetry.addData("Lift -> Current Position Right", MotorUtils.getCurrentPosition(_motorLiftRight));
    }

    public void moveToBottom()
    {
        _lastControlMode = LiftControlMode.ENCODER;
        MotorUtils.moveToTargetPosition(_motorLiftLeft, LIFT_BOTTOM_POSITION, -1.0);
        MotorUtils.moveToTargetPosition(_motorLiftRight, LIFT_BOTTOM_POSITION, -1.0);
    }

    //--- Get the current position of the lift motors
    public int getCurrentLiftPosition()
    {
        return MotorUtils.getCurrentPosition(_motorLiftLeft);
    }
}
