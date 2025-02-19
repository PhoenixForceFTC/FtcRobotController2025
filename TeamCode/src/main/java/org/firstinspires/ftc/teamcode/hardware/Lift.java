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
    private static final int LIFT_TOP_BASKET_POSITION = 2000;
    private static final int LIFT_LOW_BASKET_POSITION = 650; //--- was 1200 -- for putting on the specimen
    private static final int LIFT_BOTTOM_POSITION = 0;
    //endregion

    //region --- Hardware ---
    private final DcMotor _motorLiftLeft;
    private final DcMotor _motorLiftRight;
    private final Gamepad _gamepad;
    private final Telemetry _telemetry;
    private final boolean _showInfo;
    //endregion

    //region --- Constructor
    public Lift(DcMotor motorLiftLeft, DcMotor motorLiftRight, Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        this._motorLiftLeft = motorLiftLeft;
        this._motorLiftRight = motorLiftRight;
        this._gamepad = gamepad;
        this._telemetry = telemetry;
        this._showInfo = showInfo;
    }
    //endregion

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

    public void moveToHighBasket()
    {
        MotorUtils.moveToTargetPosition(_motorLiftLeft, LIFT_TOP_BASKET_POSITION, -1.0);
        MotorUtils.moveToTargetPosition(_motorLiftRight, LIFT_TOP_BASKET_POSITION, -1.0);
    }

    public void moveToLowBasket()
    {
        MotorUtils.moveToTargetPosition(_motorLiftLeft, LIFT_LOW_BASKET_POSITION, -1.0);
        MotorUtils.moveToTargetPosition(_motorLiftRight, LIFT_LOW_BASKET_POSITION, -1.0);

    }

    public void getTelemetry(){
        _telemetry.addData("Lift -> Target Position Left", _motorLiftLeft.getTargetPosition());
        _telemetry.addData("Lift -> Target Position Right", _motorLiftRight.getTargetPosition());
        _telemetry.addData("Lift -> Current Position Left", MotorUtils.getCurrentPosition(_motorLiftLeft));
        _telemetry.addData("Lift -> Current Position Right", MotorUtils.getCurrentPosition(_motorLiftRight));
    }

    public void moveToBottom()
    {
        MotorUtils.moveToTargetPosition(_motorLiftLeft, LIFT_BOTTOM_POSITION, -1.0);
        MotorUtils.moveToTargetPosition(_motorLiftRight, LIFT_BOTTOM_POSITION, -1.0);
    }

    //--- Get the current position of the lift motors
    public int getCurrentLiftPosition()
    {
        return MotorUtils.getCurrentPosition(_motorLiftLeft);
    }
}
