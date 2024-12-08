package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utils.DriveUtils;
import org.firstinspires.ftc.teamcode.utils.ServoUtils;

@TeleOp(name="Mecanum Drive", group="TeleOp")
public class TeleOp_Mecanum extends LinearOpMode
{
    //------------------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------------------
    RobotHardware _robot = new RobotHardware(this);
    public ElapsedTime _runtime = new ElapsedTime();

    double _servoPos = 0.3;    // Initial servo position

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
            //--- Start Telemetry Display
            //------------------------------------------------------------------------------------------
            telemetry.addData("Status", "Run Time: " + _runtime.toString());

            //------------------------------------------------------------------------------------------
            //--- Control Methods
            //------------------------------------------------------------------------------------------
            DriveUtils.mecanumDrive(
                    _robot.motorDriveFrontLeft, _robot.motorDriveFrontRight, _robot.motorDriveRearLeft, _robot.motorDriveRearRight,
                    gamepad1, telemetry);

            //ArmClawControl(true);
            //ArmWristFineTune(true);
            //ArmElbowFineTune(true);
            ArmShoulderFineTune(true);

            //IntakeLiftControl(true);
            //IntakeControl(true);

            //------------------------------------------------------------------------------------------
            //--- Update Telemetry Display
            //------------------------------------------------------------------------------------------
            telemetry.update();
        }
    }

    private void ArmClawFineTune(boolean showInfo)
    {
        //--- wide open = 0.61
        //--- closed = 0.76

        double incrementPos = 0.01; // Amount to increment/decrement
        double minPos = 0.0;       // Minimum servo position
        double maxPos = 1.0;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (gamepad1.y) {
            _servoPos = Math.min(_servoPos + incrementPos, maxPos);
            _robot.servoArmClawPos = ServoUtils.moveToPosition(_robot.servoArmClaw, _servoPos);
        }
        if (gamepad1.a) {
            _servoPos = Math.max(_servoPos - incrementPos, minPos);
            _robot.servoArmClawPos = ServoUtils.moveToPosition(_robot.servoArmClaw, _servoPos);
        }
        sleep(100);

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Arm Claw", "%4.2f", _robot.servoArmClawPos);
        }
    }

    private void ArmWristFineTune(boolean showInfo)
    {
        //--- pronated = 0.03
        //--- supinated = 0.69

        double incrementPos = 0.01; // Amount to increment/decrement
        double minPos = 0.0;       // Minimum servo position
        double maxPos = 1.0;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (gamepad1.y) {
            _servoPos = Math.min(_servoPos + incrementPos, maxPos);
            _robot.servoArmWristPos = ServoUtils.moveToPosition(_robot.servoArmWrist, _servoPos);
        }
        if (gamepad1.a) {
            _servoPos = Math.max(_servoPos - incrementPos, minPos);
            _robot.servoArmWristPos = ServoUtils.moveToPosition(_robot.servoArmWrist, _servoPos);
        }
        sleep(100);

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Arm Wrist", "%4.2f", _robot.servoArmWristPos);
        }
    }

    private void ArmElbowFineTune(boolean showInfo)
    {
        //--- intake = 0.31
        //---

        double incrementPos = 0.01; // Amount to increment/decrement
        double minPos = 0.0;       // Minimum servo position
        double maxPos = 1.0;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (gamepad1.y) {
            _servoPos = Math.min(_servoPos + incrementPos, maxPos);
            _robot.servoArmElbowPos = ServoUtils.moveToPosition(_robot.servoArmElbow, _servoPos);
        }
        if (gamepad1.a) {
            _servoPos = Math.max(_servoPos - incrementPos, minPos);
            _robot.servoArmElbowPos = ServoUtils.moveToPosition(_robot.servoArmElbow, _servoPos);
        }
        sleep(100);

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Arm Elbow", "%4.2f", _robot.servoArmElbowPos);
        }
    }

    private void ArmShoulderFineTune(boolean showInfo)
    {
        //--- intake = 0.75
        //--- full back = 0.14

        double incrementPos = 0.01; // Amount to increment/decrement
        double minPos = 0.0;       // Minimum servo position
        double maxPos = 1.0;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (gamepad1.y) {
            _servoPos = Math.min(_servoPos + incrementPos, maxPos);
            _robot.servoArmShoulderPos = ServoUtils.moveToPosition(_robot.servoArmShoulder, _servoPos);
        }
        if (gamepad1.a) {
            _servoPos = Math.max(_servoPos - incrementPos, minPos);
            _robot.servoArmShoulderPos = ServoUtils.moveToPosition(_robot.servoArmShoulder, _servoPos);
        }
        sleep(100);

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Arm Elbow", "%4.2f", _robot.servoArmShoulderPos);
        }
    }




    private void IntakeLiftControl(boolean showInfo)
    {
        if (gamepad1.y) {
            _robot.servoIntakeLiftLeftPos = ServoUtils.moveToPosition(_robot.servoIntakeLiftLeft, _robot.SERVO_INTAKE_LIFT_IN);
            _robot.servoIntakeLiftRightPos = ServoUtils.moveToPosition(_robot.servoIntakeLiftRight, _robot.SERVO_INTAKE_LIFT_IN);
        }
        if (gamepad1.b) {
            _robot.servoIntakeLiftLeftPos = ServoUtils.moveToPosition(_robot.servoIntakeLiftLeft, _robot.SERVO_INTAKE_LIFT_OUT);
            _robot.servoIntakeLiftRightPos = ServoUtils.moveToPosition(_robot.servoIntakeLiftRight, _robot.SERVO_INTAKE_LIFT_OUT);
        }
        if (gamepad1.x) {
            _robot.servoIntakeLiftLeftPos = ServoUtils.moveToPositionAndDisable(_robot.servoIntakeLiftLeft, _robot.SERVO_INTAKE_LIFT_OUT, 750);
            _robot.servoIntakeLiftRightPos = ServoUtils.moveToPositionAndDisable(_robot.servoIntakeLiftRight, _robot.SERVO_INTAKE_LIFT_OUT, 750);
        }

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Intake Lift Left", "%4.2f", _robot.servoIntakeLiftLeftPos);
            telemetry.addData("Intake Lift Right", "%4.2f", _robot.servoIntakeLiftRightPos);
        }
    }

    private void IntakeControl(boolean showInfo)
    {
        if (gamepad1.y)
        {
            _robot.servoIntakeSpinLeft.setPower(-1);
            _robot.servoIntakeSpinRight.setPower(1);
        }
        if (gamepad1.a)
        {
            _robot.servoIntakeSpinLeft.setPower(1);
            _robot.servoIntakeSpinRight.setPower(-1);
        }
        if (gamepad1.b)
        {
            _robot.servoIntakeSpinLeft.setPower(0);
            _robot.servoIntakeSpinRight.setPower(0);
        }

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Intake Lift Left", "%4.2f", _robot.servoIntakeLiftLeftPos);
            telemetry.addData("Intake Lift Right", "%4.2f", _robot.servoIntakeLiftRightPos);
        }
    }


}
