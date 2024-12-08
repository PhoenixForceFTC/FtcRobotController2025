package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveUtils {

    //--- Drives a robot with mecanum wheels
    public static void mecanumDrive(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight, Gamepad gamepad, Telemetry telemetry) {
        double max;

        //--- POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double axial = -gamepad.left_stick_y;  //--- Pushing stick forward gives negative value
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

        if (max > 1.0) {
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

        //--- Show messages
        telemetry.addData("Front left/right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
        telemetry.addData("Back left/right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
    }
}
