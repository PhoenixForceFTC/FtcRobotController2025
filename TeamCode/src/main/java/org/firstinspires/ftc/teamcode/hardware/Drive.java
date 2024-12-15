package org.firstinspires.ftc.teamcode.hardware;

//region --- Imports ---
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.DriveUtils;
import org.firstinspires.ftc.teamcode.utils.MotorUtils;
//endregion

public class Drive
{
    //--- Constants for Speed Multipliers
    private static final double SPEED_FAST = 1.0;
    private static final double SPEED_SLOW = 0.5;
    private static final double SPEED_ROTATE_FAST = 0.8;
    private static final double SPEED_ROTATE_SLOW = 0.4;

    //--- State Variables
    private boolean isSpeedFast = true; //--- Default movement speed mode
    private boolean isRotateFast = false; //--- Default rotation speed mode
    private boolean wasLeftStickButtonPressed = false;
    private boolean wasRightStickButtonPressed = false;

    //--- Robot Components
    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor rearLeft;
    private final DcMotor rearRight;
    private final Gamepad gamepad;
    private final Telemetry telemetry;
    private final boolean showInfo;

    //region --- Constructor ---
    public Drive(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight,
                 Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.rearLeft = rearLeft;
        this.rearRight = rearRight;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
        this.showInfo = showInfo;
    }
    //endregion

    //--- Arcade Drive with Speed Control
    public void arcadeDriveSpeedControl()
    {
        if (gamepad.left_stick_button && !wasLeftStickButtonPressed)
        {
            isSpeedFast = !isSpeedFast; //--- Toggle movement speed
        }
        wasLeftStickButtonPressed = gamepad.left_stick_button;

        if (gamepad.right_stick_button && !wasRightStickButtonPressed)
        {
            isRotateFast = !isRotateFast; //--- Toggle rotation speed
        }
        wasRightStickButtonPressed = gamepad.right_stick_button;

        double speedMultiplier = isSpeedFast ? SPEED_FAST : SPEED_SLOW;
        double speedMultiplierRotate = isRotateFast ? SPEED_ROTATE_FAST : SPEED_ROTATE_SLOW;

        DriveUtils.arcadeDrive(frontLeft, frontRight, rearLeft, rearRight, gamepad, telemetry, showInfo, speedMultiplier, speedMultiplierRotate);

        if (showInfo)
        {
            telemetry.addData("Drive -> Speed Mode", isSpeedFast ? "FAST" : "SLOW");
            telemetry.addData("Drive -> Rotate Mode", isRotateFast ? "FAST" : "SLOW");
        }
    }

    //--- Directional Driving with D-Pad
    public void directionDrive(double speed)
    {
        if (gamepad.dpad_up)
        {
            moveForward(speed);
            if (showInfo) telemetry.addData("Drive -> Direction", "FORWARD (%4.2f)", speed);
        }
        else if (gamepad.dpad_down)
        {
            moveBackward(speed);
            if (showInfo) telemetry.addData("Drive -> Direction", "BACKWARD (%4.2f)", speed);
        }
        else if (gamepad.dpad_left)
        {
            moveLeft(speed);
            if (showInfo) telemetry.addData("Drive -> Direction", "LEFT (%4.2f)", speed);
        }
        else if (gamepad.dpad_right)
        {
            moveRight(speed);
            if (showInfo) telemetry.addData("Drive -> Direction", "RIGHT (%4.2f)", speed);
        }
    }

    //region --- Move Directions ---

    //--- Moves the robot forward
    private void moveForward(double speed)
    {
        MotorUtils.setPower(frontLeft, speed);
        MotorUtils.setPower(frontRight, speed);
        MotorUtils.setPower(rearLeft, speed);
        MotorUtils.setPower(rearRight, speed);
    }

    //--- Moves the robot backward
    private void moveBackward(double speed)
    {
        MotorUtils.setPower(frontLeft, -speed);
        MotorUtils.setPower(frontRight, -speed);
        MotorUtils.setPower(rearLeft, -speed);
        MotorUtils.setPower(rearRight, -speed);
    }

    //--- Moves the robot to the left
    private void moveLeft(double speed)
    {
        MotorUtils.setPower(frontLeft, -speed);
        MotorUtils.setPower(frontRight, speed);
        MotorUtils.setPower(rearLeft, speed);
        MotorUtils.setPower(rearRight, -speed);
    }

    //--- Moves the robot to the right
    private void moveRight(double speed)
    {
        MotorUtils.setPower(frontLeft, speed);
        MotorUtils.setPower(frontRight, -speed);
        MotorUtils.setPower(rearLeft, -speed);
        MotorUtils.setPower(rearRight, speed);
    }

    //endregion
}
