package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveUtils {

    //--- Constants for Speed Multipliers
    private static final double SPEED_FAST = 1.0;
    private static final double SPEED_SLOW = 0.5;

    private static final double SPEED_ROTATE_FAST = 0.8;
    private static final double SPEED_ROTATE_SLOW = 0.4;

    //--- State Variables for Toggles
    private static boolean isSpeedFast = true; //--- High by default
    private static boolean isRotateFast = false; //--- Slow by default
    private static boolean wasLeftStickButtonPressed = false;
    private static boolean wasRightStickButtonPressed = false;

    //--- Arcade Drive Method with High/Low speed controls
    public static void arcadeDriveSpeedControl(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight,
                                               Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        //--- Handle toggling movement speed with the left stick button
        if (gamepad.left_stick_button && !wasLeftStickButtonPressed)
        {
            isSpeedFast = !isSpeedFast; // Toggle the movement speed mode
        }
        wasLeftStickButtonPressed = gamepad.left_stick_button; // Update button state

        //--- Handle toggling rotation speed with the right stick button
        if (gamepad.right_stick_button && !wasRightStickButtonPressed)
        {
            isRotateFast = !isRotateFast; // Toggle the rotation speed mode
        }
        wasRightStickButtonPressed = gamepad.right_stick_button; // Update button state

        //--- Determine speed multipliers
        double speedMultiplier = isSpeedFast ? SPEED_FAST : SPEED_SLOW;
        double speedMultiplierRotate = isRotateFast ? SPEED_ROTATE_FAST : SPEED_ROTATE_SLOW;

        //--- Drive Logic
        arcadeDrive(frontLeft, frontRight, rearLeft, rearRight, gamepad, telemetry, showInfo, speedMultiplier, speedMultiplierRotate);

        //--- Show telemetry for the speed modes
        if (showInfo)
        {
            telemetry.addData("Drive -> Speed Mode", isSpeedFast ? "FAST" : "SLOW");
            telemetry.addData("Drive -> Rotate Mode", isRotateFast ? "FAST" : "SLOW");
        }
    }

    //--- Arcade Drive Method
    public static void arcadeDrive(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight,
                                   Gamepad gamepad, Telemetry telemetry, boolean showInfo,
                                   double speedMultiplier, double speedMultiplierRotate)
    {
        double max;

        //--- POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double axial = -gamepad.left_stick_y;  //--- Note, pushing stick forward gives negative value
        double lateral = gamepad.left_stick_x;
        double yaw = gamepad.right_stick_x * speedMultiplierRotate; // Scale yaw separately

        //--- Combine the joystick requests for each axis-motion to determine each wheel's power.
        double leftFrontPower = (axial + lateral + yaw) * speedMultiplier;
        double rightFrontPower = (axial - lateral - yaw) * speedMultiplier;
        double leftBackPower = (axial - lateral + yaw) * speedMultiplier;
        double rightBackPower = (axial + lateral - yaw) * speedMultiplier;

        //--- Normalize the values so no wheel power exceeds 100%
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0)
        {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        //--- Send calculated power to wheels
        frontLeft.setPower(leftFrontPower);
        frontRight.setPower(rightFrontPower);
        rearLeft.setPower(leftBackPower);
        rearRight.setPower(rightBackPower);

        //--- Show telemetry if enabled
        if (showInfo)
        {
            telemetry.addData("Control -> Axial/Lateral/Yaw", "%4.2f, %4.2f, %4.2f", axial, lateral, yaw);
            telemetry.addData("Motor -> Front Left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Motor -> Back Left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
        }
    }

    //--- Handles D-pad-based directional driving
    public static void directionDrive(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight,
                                      Gamepad gamepad, Telemetry telemetry, boolean showInfo,
                                      double speed)
    {
        if (gamepad.dpad_up)
        {
            moveForward(frontLeft, frontRight, rearLeft, rearRight, speed);
            if (showInfo) telemetry.addData("Drive -> Forward (%4.2f)", speed);
        }
        else if (gamepad.dpad_down)
        {
            moveBackward(frontLeft, frontRight, rearLeft, rearRight, speed);
            if (showInfo) telemetry.addData("Drive -> Back (%4.2f)", speed);
        }
        else if (gamepad.dpad_left)
        {
            moveLeft(frontLeft, frontRight, rearLeft, rearRight, speed);
            if (showInfo) telemetry.addData("Drive -> Left (%4.2f)", speed);
        }
        else if (gamepad.dpad_right)
        {
            moveRight(frontLeft, frontRight, rearLeft, rearRight, speed);
            if (showInfo) telemetry.addData("Drive -> Right (%4.2f)", speed);
        }

        //--- No stop logic added here to avoid interfering with mecanumDrive
    }

    //--- Moves the robot forward at a specified speed
    private static void moveForward(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight, double speed)
    {
        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        rearLeft.setPower(speed);
        rearRight.setPower(speed);
    }

    //--- Moves the robot backward at a specified speed
    private static void moveBackward(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight, double speed)
    {
        frontLeft.setPower(-speed);
        frontRight.setPower(-speed);
        rearLeft.setPower(-speed);
        rearRight.setPower(-speed);
    }

    //--- Moves the robot to the left (strafe) at a specified speed
    private static void moveLeft(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight, double speed)
    {
        frontLeft.setPower(-speed);
        frontRight.setPower(speed);
        rearLeft.setPower(speed);
        rearRight.setPower(-speed);
    }

    //--- Moves the robot to the right (strafe) at a specified speed
    private static void moveRight(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight, double speed)
    {
        frontLeft.setPower(speed);
        frontRight.setPower(-speed);
        rearLeft.setPower(-speed);
        rearRight.setPower(speed);
    }
}
