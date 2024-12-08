package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utils.DriveUtils;
import org.firstinspires.ftc.teamcode.utils.ServoUtils;
import org.firstinspires.ftc.teamcode.utils.StateMachine;

import java.util.ArrayList;
import java.util.Arrays;

@TeleOp(name="Mecanum Drive", group="TeleOp")
public class TeleOp_Mecanum extends LinearOpMode
{
    //------------------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------------------
    RobotHardware _robot = new RobotHardware(this);
    public ElapsedTime _runtime = new ElapsedTime();

    double _servoPos = 0.3;    // Initial servo position

    //--- Need to know initialized states
    boolean _initializedArmControl = false;

    StateMachine<Double> clawStateMachine = null;
    StateMachine<Double> wristStateMachine = null;
    StateMachine<Double> elbowStateMachine = null;
    StateMachine<Double> shoulderStateMachine = null;

    //------------------------------------------------------------------------------------------
    //--- OpMode
    //------------------------------------------------------------------------------------------
    @Override
    public void runOpMode()
    {
        //------------------------------------------------------------------------------------------
        //--- Initialize
        //------------------------------------------------------------------------------------------
        _robot.init();
        ArmControlInitialize();

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

            ArmControl(true);

            //ArmClawControl(true);
            //ArmWristFineTune(true);
            //ArmElbowFineTune(true);
            //ArmShoulderFineTune(true);

            //IntakeLiftControl(true);
            //IntakeControl(true);

            //------------------------------------------------------------------------------------------
            //--- Update Telemetry Display
            //------------------------------------------------------------------------------------------
            telemetry.update();
        }
    }

    //region --- Arm Control ---

    private void ArmControlInitialize()
    {
        //TODO: Refactor into it's own class -- Arm

        double clawOpenPos = 0.61;
        double clawClosedPos = 0.76;

        double wristIntake = 0.03;
        double wristDelivery = 0.69;

        double elbowIntake = 0.31;
        double elbowMiddle = 0.5;
        double elbowUp = 0.7; //???

        double shoulderIntake = 0.75;
        double shoulderMiddle = 0.14;
        double shoulderFullBack = 0.14;

        //--- Position 1 -- arm is up out of the way
        double claw1 = clawClosedPos;
        double wrist1 = wristIntake;
        double elbow1 = elbowMiddle;
        double shoulder1 = shoulderMiddle;

        //--- Position 2 -- arm is positioned for the intake
        double claw2 = clawOpenPos;
        double wrist2 = wristIntake;
        double elbow2 = elbowIntake;
        double shoulder2 = shoulderIntake;

        //--- Position 3 -- arm is ready to deliver
        double claw3 = clawClosedPos;
        double wrist3 = wristDelivery;
        double elbow3 = elbowUp;
        double shoulder3 = shoulderMiddle;

        //--- State machine for each servo
        clawStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(claw1, claw2, claw3)));
        wristStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(wrist1, wrist2, wrist3)));
        elbowStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(elbow1, elbow2, elbow3)));
        shoulderStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(shoulder1, shoulder2, shoulder3)));
    }

    private void ArmControl(boolean showInfo)
    {
        //--- Navigate through the steps based on button presses
        if (gamepad1.y) {
            _robot.servoArmClawPos = ServoUtils.moveToPosition(_robot.servoArmClaw, clawStateMachine.next());
            _robot.servoArmWristPos = ServoUtils.moveToPosition(_robot.servoArmWrist, wristStateMachine.next());
            _robot.servoArmElbowPos = ServoUtils.moveToPosition(_robot.servoArmElbow, elbowStateMachine.next());
            _robot.servoArmShoulderPos = ServoUtils.moveToPosition(_robot.servoArmShoulder, shoulderStateMachine.next());
            sleep(250);
        }
        if (gamepad1.a) {
            _robot.servoArmClawPos = ServoUtils.moveToPosition(_robot.servoArmClaw, clawStateMachine.previous());
            _robot.servoArmWristPos = ServoUtils.moveToPosition(_robot.servoArmWrist, wristStateMachine.previous());
            _robot.servoArmElbowPos = ServoUtils.moveToPosition(_robot.servoArmElbow, elbowStateMachine.previous());
            _robot.servoArmShoulderPos = ServoUtils.moveToPosition(_robot.servoArmShoulder, shoulderStateMachine.previous());
            sleep(250);
        }

        //--- Show messages
        if (showInfo) {
            telemetry.addData("Claw Step Index", clawStateMachine.getCurrentStepIndex());
            telemetry.addData("Wrist Step Index", wristStateMachine.getCurrentStepIndex());
            telemetry.addData("Elbow Step Index", elbowStateMachine.getCurrentStepIndex());
            telemetry.addData("Shoulder Step Index", shoulderStateMachine.getCurrentStepIndex());

            telemetry.addData("Claw Position", "%4.2f", _robot.servoArmClawPos);
            telemetry.addData("Wrist Position", "%4.2f", _robot.servoArmWristPos);
            telemetry.addData("Elbow Position", "%4.2f", _robot.servoArmElbowPos);
            telemetry.addData("Shoulder Position", "%4.2f", _robot.servoArmShoulderPos);
        }
    }

    //endregion

    //region --- Arm Servo Fine Tuning ---

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
        sleep(250);

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Arm Claw", "%4.2f", _robot.servoArmClawPos);
        }
    }

    private void ArmWristFineTune(boolean showInfo)
    {
        //--- intake (pronated) = 0.03
        //--- delivery (supinated) = 0.69

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
        sleep(250);

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
        sleep(250);

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
        sleep(250);

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Arm Elbow", "%4.2f", _robot.servoArmShoulderPos);
        }
    }

    //endregion

    //region --- Intake ---

    private void IntakeLiftControl(boolean showInfo)
    {
        //TODO: Refactor into it's own class -- Intake

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

    //endregion
}
