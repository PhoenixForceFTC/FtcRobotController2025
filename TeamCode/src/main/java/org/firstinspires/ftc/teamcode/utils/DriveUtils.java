package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveUtils {

    //--- Constants for Speed Multipliers
    private static final double SPEED_HIGH = 1.0;
    private static final double SPEED_SLOW = 0.5;

    private static final double SPEED_ROTATE_HIGH = 0.8;
    private static final double SPEED_ROTATE_SLOW = 0.4;

    //--- State Variables for Toggles
    private static boolean isHighSpeed = true; //--- High by default
    private static boolean isHighRotateSpeed = false; //--- Slow by default
    private static boolean wasLeftStickButtonPressed = false;
    private static boolean wasRightStickButtonPressed = false;

    //--- Arcade Drive Method with High/Low speed controls
    public static void arcadeDriveSpeedControl(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight,
                                               Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        //--- Handle toggling movement speed with the left stick button
        if (gamepad.left_stick_button && !wasLeftStickButtonPressed)
        {
            isHighSpeed = !isHighSpeed; // Toggle the movement speed mode
        }
        wasLeftStickButtonPressed = gamepad.left_stick_button; // Update button state

        //--- Handle toggling rotation speed with the right stick button
        if (gamepad.right_stick_button && !wasRightStickButtonPressed)
        {
            isHighRotateSpeed = !isHighRotateSpeed; // Toggle the rotation speed mode
        }
        wasRightStickButtonPressed = gamepad.right_stick_button; // Update button state

        //--- Determine speed multipliers
        double speedMultiplier = isHighSpeed ? SPEED_HIGH : SPEED_SLOW;
        double speedMultiplierRotate = isHighRotateSpeed ? SPEED_ROTATE_HIGH : SPEED_ROTATE_SLOW;

        //--- Drive Logic
        arcadeDrive(frontLeft, frontRight, rearLeft, rearRight, gamepad, telemetry, showInfo, speedMultiplier, speedMultiplierRotate);

        //--- Show telemetry for the speed modes
        if (showInfo)
        {
            telemetry.addData("Speed Mode", isHighSpeed ? "High" : "Slow");
            telemetry.addData("Rotate Speed Mode", isHighRotateSpeed ? "High" : "Slow");
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
            telemetry.addData("Joystick axial/lateral/yaw", "%4.2f, %4.2f, %4.2f", axial, lateral, yaw);
            telemetry.addData("Front left/right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back left/right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
        }
    }

    //--- Handles D-pad-based directional driving
    public static void directionDrive(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight, Gamepad gamepad, double speed, Telemetry telemetry, boolean showInfo)
    {
        if (gamepad.dpad_up)
        {
            moveForward(frontLeft, frontRight, rearLeft, rearRight, speed);
            if (showInfo) telemetry.addData("Moving Forward at speed: %4.2f", speed);
        }
        else if (gamepad.dpad_down)
        {
            moveBackward(frontLeft, frontRight, rearLeft, rearRight, speed);
            if (showInfo) telemetry.addData("Moving Backward at speed: %4.2f", speed);
        }
        else if (gamepad.dpad_left)
        {
            moveLeft(frontLeft, frontRight, rearLeft, rearRight, speed);
            if (showInfo) telemetry.addData("Moving Left at speed: %4.2f", speed);
        }
        else if (gamepad.dpad_right)
        {
            moveRight(frontLeft, frontRight, rearLeft, rearRight, speed);
            if (showInfo) telemetry.addData("Moving Right at speed: %4.2f", speed);
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
