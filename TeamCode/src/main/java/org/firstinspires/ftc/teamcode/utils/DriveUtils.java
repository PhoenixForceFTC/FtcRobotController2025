package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveUtils {

    //--- Drives a robot with mecanum wheels
    public static void arcadeDrive(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight, Gamepad gamepad, Telemetry telemetry, boolean showInfo)
    {
        double max;

        //--- POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double axial = -gamepad.left_stick_y;  //--- Note, pushing stick forward gives negative value
        double lateral = gamepad.left_stick_x;
        double yaw = gamepad.right_stick_x;

        //--- Combine the joystick requests for each axis-motion to determine each wheel's power.
        double leftFrontPower = axial + lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower = axial - lateral + yaw;
        double rightBackPower = axial + lateral - yaw;

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
