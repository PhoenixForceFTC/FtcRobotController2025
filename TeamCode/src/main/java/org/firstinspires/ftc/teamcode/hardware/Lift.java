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
    private static final int LIFT_MAX_POSITION = 500; // Example maximum encoder position
    private static final int LIFT_MIN_POSITION = 0;   // Example minimum encoder position

    //--- Constructor
    public Lift(DcMotor motorLiftLeft, DcMotor motorLiftRight, Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        this.motorLiftLeft = motorLiftLeft;
        this.motorLiftRight = motorLiftRight;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
        this.showInfo = showInfo;
    }

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
        //--- Configure motors for encoder mode
        MotorUtils.configureForEncoder(motorLiftLeft);
        MotorUtils.configureForEncoder(motorLiftRight);

        if (gamepad.left_trigger > 0.1) //--- Extend to maximum position
        {
            MotorUtils.setTargetPosition(motorLiftLeft, LIFT_MAX_POSITION, gamepad.left_trigger);
            MotorUtils.setTargetPosition(motorLiftRight, LIFT_MAX_POSITION, gamepad.left_trigger);
        }
        else if (gamepad.left_bumper) //--- Retract to minimum position
        {
            MotorUtils.setTargetPosition(motorLiftLeft, LIFT_MIN_POSITION, -1.0); // Full reverse
            MotorUtils.setTargetPosition(motorLiftRight, LIFT_MIN_POSITION, -1.0);
        }
        else
        {
            //--- Stop motors when no input
            MotorUtils.stopMotor(motorLiftLeft);
            MotorUtils.stopMotor(motorLiftRight);
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
