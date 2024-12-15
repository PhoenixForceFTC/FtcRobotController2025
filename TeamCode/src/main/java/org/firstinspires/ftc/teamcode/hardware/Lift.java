package org.firstinspires.ftc.teamcode.hardware;

//region --- Imports ---
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.MotorUtils;
//endregion

public class Lift
{
    //--- Robot Components
    private final DcMotor motorLiftLeft;
    private final DcMotor motorLiftRight;
    private final Gamepad gamepad;
    private final Telemetry telemetry;
    private final boolean showInfo;

    //--- Constants for Lift Positions
    private static final int LIFT_MAX_POSITION = 2000; // Example maximum encoder position
    private static final int LIFT_MIN_POSITION = 0;   // Example minimum encoder position

    //region --- Constructor
    public Lift(DcMotor motorLiftLeft, DcMotor motorLiftRight, Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        this.motorLiftLeft = motorLiftLeft;
        this.motorLiftRight = motorLiftRight;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
        this.showInfo = showInfo;
    }
    //endregion

    //--- Handles lifting using power
    public void liftByPower()
    {
        //--- Configure motors for no encoder mode
        MotorUtils.configureForPower(motorLiftLeft);
        MotorUtils.configureForPower(motorLiftRight);

        if (gamepad.left_trigger > 0.1) //--- Extend the lift
        {
            MotorUtils.setPower(motorLiftLeft, gamepad.left_trigger);
            MotorUtils.setPower(motorLiftRight, gamepad.left_trigger);
        }
        else if (gamepad.left_bumper) //--- Retract the lift
        {
            MotorUtils.setPower(motorLiftLeft, -1.0); // Full reverse power
            MotorUtils.setPower(motorLiftRight, -1.0);
        }
        else
        {
            MotorUtils.stopMotor(motorLiftLeft); // Stop motors
            MotorUtils.stopMotor(motorLiftRight);
        }

        //--- Show telemetry if enabled
        if (showInfo)
        {
            telemetry.addData("Lift -> Power Left", "%4.2f", motorLiftLeft.getPower());
            telemetry.addData("Lift -> Power Right", "%4.2f", motorLiftRight.getPower());
        }
    }

    //--- Handles lifting using encoder positions
    public void liftByEncoder()
    {
        if (gamepad.y) //--- Extend to maximum position
        {
            MotorUtils.moveToTargetPosition(motorLiftLeft, LIFT_MAX_POSITION, -1.0);
            MotorUtils.moveToTargetPosition(motorLiftRight, LIFT_MAX_POSITION, -1.0);
        }
        else if (gamepad.a) //--- Retract to minimum position
        {
            MotorUtils.moveToTargetPosition(motorLiftLeft, LIFT_MIN_POSITION, -1.0);
            MotorUtils.moveToTargetPosition(motorLiftRight, LIFT_MIN_POSITION, -1.0);
        }

        //--- Show telemetry if enabled
        if (showInfo)
        {
            telemetry.addData("Lift -> Target Position Left", motorLiftLeft.getTargetPosition());
            telemetry.addData("Lift -> Target Position Right", motorLiftRight.getTargetPosition());
            telemetry.addData("Lift -> Current Position Left", MotorUtils.getCurrentPosition(motorLiftLeft));
            telemetry.addData("Lift -> Current Position Right", MotorUtils.getCurrentPosition(motorLiftRight));
        }
    }
}
