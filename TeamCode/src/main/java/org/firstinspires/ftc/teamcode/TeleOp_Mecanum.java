package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Mecanum Drive", group="TeleOp")
public class TeleOp_Mecanum extends LinearOpMode
{
    //------------------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------------------
    RobotHardware _robot = new RobotHardware(this);
    public ElapsedTime _runtime = new ElapsedTime();

    //------------------------------------------------------------------------------------------
    //--- OpMode
    //------------------------------------------------------------------------------------------
    @Override
    public void runOpMode()
    {
        //------------------------------------------------------------------------------------------
        //--- Initialize Hardware
        //------------------------------------------------------------------------------------------
        _robot.init();

        //------------------------------------------------------------------------------------------
        //--- Display and wait for the game to start (driver presses START)
        //------------------------------------------------------------------------------------------
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        _runtime.reset();

        //------------------------------------------------------------------------------------------
        //--- Run until the end of the match (driver presses STOP)
        //------------------------------------------------------------------------------------------
        while (opModeIsActive()) {

            //------------------------------------------------------------------------------------------
            //--- Display Info
            //------------------------------------------------------------------------------------------
            telemetry.addData("Status", "Run Time: " + _runtime.toString());

            //------------------------------------------------------------------------------------------
            //--- Control Methods
            //------------------------------------------------------------------------------------------
            MecanumDrive(true); //--- gamepad1.left_stick_y, gamepad1.left_stick_x
            ArmControl(true);
            IntakeControl(true);

            //------------------------------------------------------------------------------------------
            //--- Display Info
            //------------------------------------------------------------------------------------------
            telemetry.addData("Intake Left Up/Down", "%4.2f", _robot.servoIntakeUpDownLeftPos);
            telemetry.addData("Intake Right Up/Down", "%4.2f", _robot.servoIntakeUpDownRightPos);



            telemetry.update();
        }
    }

    private void IntakeControl(boolean showInfo)
    {
//        if (gamepad1.y)
//        {
//            _robot.servoIntakeSpinLeft.setPower(-1);
//            _robot.servoIntakeSpinRight.setPower(1);
//        }
//        if (gamepad1.a)
//        {
//            _robot.servoIntakeSpinLeft.setPower(1);
//            _robot.servoIntakeSpinRight.setPower(-1);
//        }
//        if (gamepad1.b)
//        {
//            _robot.servoIntakeSpinLeft.setPower(0);
//            _robot.servoIntakeSpinRight.setPower(0);
//        }

        if (gamepad1.y)
        {
            _robot.servoIntakeUpDownLeft.setPosition(0.6);
            _robot.servoIntakeUpDownRight.setPosition(0.6);
        }
        if (gamepad1.a)
        {
            _robot.servoIntakeUpDownLeft.setPosition(0.5);
            _robot.servoIntakeUpDownRight.setPosition(0.5);
        }
        if (gamepad1.b)
        {
            _robot.servoIntakeUpDownLeft.setPosition(0.4);
            _robot.servoIntakeUpDownRight.setPosition(0.4);
        }
        if (gamepad1.x)
        {
            //--- Turn off?
            _robot.servoIntakeUpDownLeft.getController().pwmDisable();
            _robot.servoIntakeUpDownRight.getController().pwmDisable();
        }

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Arm Wrist", "%4.2f", _robot.servoArmWristPos);
            telemetry.addData("Arm Elbow", "%4.2f", _robot.servoArmElbowPos);
            telemetry.addData("Arm Shoulder", "%4.2f", _robot.servoArmShoulderPos);
            telemetry.addData("Arm Claw", "%4.2f", _robot.servoArmGrabPos);
        }
    }

    private void ArmControl(boolean showInfo)
    {
//        if (gamepad1.a)
//        {
//
//        }

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Arm Wrist", "%4.2f", _robot.servoArmWristPos);
            telemetry.addData("Arm Elbow", "%4.2f", _robot.servoArmElbowPos);
            telemetry.addData("Arm Shoulder", "%4.2f", _robot.servoArmShoulderPos);
            telemetry.addData("Arm Claw", "%4.2f", _robot.servoArmGrabPos);
        }
    }

    private void MecanumDrive(boolean showInfo)
    {
        double max;

        //--- POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
        double lateral =  gamepad1.left_stick_x;
        double yaw     =  gamepad1.right_stick_x;

        //--- Combine the joystick requests for each axis-motion to determine each wheel's power.
        //--- Set up a variable for each drive wheel to save the power level for telemetry.
        double leftFrontPower  = axial + lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower   = axial - lateral + yaw;
        double rightBackPower  = axial + lateral - yaw;

        //--- Normalize the values so no wheel power exceeds 100%
        //--- This ensures that the robot maintains the desired motion.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower  /= max;
            rightFrontPower /= max;
            leftBackPower   /= max;
            rightBackPower  /= max;
        }

        //--- Send calculated power to wheels
        _robot.motorDriveFrontLeft.setPower(leftFrontPower);
        _robot.motorDriveFrontRight.setPower(rightFrontPower);
        _robot.motorDriveRearLeft.setPower(leftBackPower);
        _robot.motorDriveRearRight.setPower(rightBackPower);

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
        }
    }
}
