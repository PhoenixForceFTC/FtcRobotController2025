package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    double _servoClawPos = 0.3;    // Initial servo position
    double _servoWristPos = 0.3;
    double _servoElbowPos = 0.3;
    double _servoShoulderPos = 0.3;


    StateMachine<Double> clawStateMachine = null;
    StateMachine<Double> wristStateMachine = null;
    StateMachine<Double> elbowStateMachine = null;
    StateMachine<Double> shoulderStateMachine = null;


    //--- Constants for motorIntake positions
    private static final int MOTOR_INTAKE_MAX_POSITION = 1000;    // Maximum position (fully extended)
    private static final int MOTOR_INTAKE_MIN_POSITION = 0;       // Minimum position (fully retracted)
    private static final int MOTOR_INTAKE_INCREMENT_EXTEND = 10;  // Increment value for extending
    private static final int MOTOR_INTAKE_INCREMENT_RETRACT = 5;  // Increment value for retracting

    private int _motorIntakeCurrentTarget = MOTOR_INTAKE_MIN_POSITION; // Start at the retracted position


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
//            DriveUtils.mecanumDrive(
//                    _robot.motorDriveFrontLeft, _robot.motorDriveFrontRight, _robot.motorDriveRearLeft, _robot.motorDriveRearRight,
//                    gamepad1, telemetry);

            //ArmControl(true);

//            ArmClawFineTune(true);
//            ArmWristFineTune(true);
//            ArmElbowFineTune(true);
//            ArmShoulderFineTune(true);

            //IntakeLiftControl(true);
            //IntakeControl(true);

            //--- Add code for motorIntake control
//            if (gamepad1.left_trigger > 0.1) //--- Extend motorIntake when left trigger is pressed
//            {
//                _robot.motorIntake.setPower(gamepad1.left_trigger); //--- Scale power by trigger pressure
//            }
//            else if (gamepad1.left_bumper) //--- Retract motorIntake when left bumper is pressed
//            {
//                _robot.motorIntake.setPower(-1); //--- Full power in reverse
//            }
//            else
//            {
//                _robot.motorIntake.setPower(0); // Stop motorIntake when no input
//            }


            //--- Add code for motorIntake control
//            if (gamepad1.left_trigger > 0.1) //--- Extend motorIntake when left trigger is pressed
//            {
//                _robot.motorLiftLeft.setPower(gamepad1.left_trigger);
//                _robot.motorLiftRight.setPower(gamepad1.left_trigger);
//            }
//            else if (gamepad1.left_bumper) //--- Retract motorIntake when left bumper is pressed
//            {
//                _robot.motorLiftLeft.setPower(-1);
//                _robot.motorLiftRight.setPower(-1);
//            }
//            else
//            {
//                _robot.motorLiftLeft.setPower(0);
//                _robot.motorLiftRight.setPower(0);
//            }

            telemetry.addData("left_trigger", gamepad1.left_trigger);
            telemetry.addData("left_bumper", gamepad1.left_bumper);

            //--- Encoder-controlled motorIntake movement
//            if (gamepad1.left_trigger > 0.1) { // Extend when left trigger is pressed
//                _robot.motorIntake.setTargetPosition(MOTOR_INTAKE_EXTEND_POSITION);
//                _robot.motorIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                _robot.motorIntake.setPower(1.0); // Set full power
//            } else if (gamepad1.left_bumper) { // Retract when left bumper is pressed
//                _robot.motorIntake.setTargetPosition(MOTOR_INTAKE_RETRACT_POSITION);
//                _robot.motorIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                _robot.motorIntake.setPower(1.0); // Set full power
//            } else {
//                _robot.motorIntake.setPower(0); // Stop the motor when no input
//            }

            //--- Dynamic motorIntake control
//            if (gamepad1.left_trigger > 0.1) { // Incrementally extend
//                _motorIntakeCurrentTarget += MOTOR_INTAKE_INCREMENT_EXTEND;
//                _motorIntakeCurrentTarget = Math.min(_motorIntakeCurrentTarget, MOTOR_INTAKE_MAX_POSITION); // Clamp to max
//                _robot.motorIntake.setTargetPosition(_motorIntakeCurrentTarget);
//                _robot.motorIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                _robot.motorIntake.setPower(1.0); // Full power
//            } else if (gamepad1.left_bumper) { // Incrementally retract
//                _motorIntakeCurrentTarget -= MOTOR_INTAKE_INCREMENT_RETRACT;
//                _motorIntakeCurrentTarget = Math.max(_motorIntakeCurrentTarget, MOTOR_INTAKE_MIN_POSITION); // Clamp to min
//                _robot.motorIntake.setTargetPosition(_motorIntakeCurrentTarget);
//                _robot.motorIntake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                _robot.motorIntake.setPower(1.0); // Full power
//            } else {
//                _robot.motorIntake.setPower(0); // Stop motor when no input
//            }

            //--- Display intake motor telemetry
//            telemetry.addData("motorIntake Target", _motorIntakeCurrentTarget);
//            telemetry.addData("motorIntake Position", _robot.motorIntake.getCurrentPosition());

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

        double wristIntake = 0.69;
        double wristDelivery = 0.03;

        double elbowIntake = 0.31;
        double elbowGrabbed = 0.38;
        double elbowMiddle = 0.43;
        double elbowUp = 0.13; //???
        double elbowHooked = 0.05;

        double shoulderIntake = 0.75;
        double shoulderMiddle = 0.59;
        double shoulderFullBack = 0.40;
        double shoulderHooked = 0.5;

        //--- Position 0 -- arm is grabbing specimens
        double claw1 = clawOpenPos;
        double wrist1 = wristIntake;
        double elbow1 = elbowMiddle;
        double shoulder1 = shoulderMiddle;

        //--- Position 1 --- arm grabs specimens
        double claw2 = clawClosedPos;
        double wrist2 = wristIntake;
        double elbow2 = elbowMiddle;
        double shoulder2 = shoulderMiddle;

        //--- Position 2 --- arm moves up
        double claw3 = clawClosedPos;
        double wrist3 = wristIntake;
        double elbow3 = elbowGrabbed;
        double shoulder3 = shoulderMiddle;

        /*
        //--- Position 3 -- arm is positioned for the intake
        double claw4 = clawOpenPos;
        double wrist4 = wristIntake;
        double elbow4 = elbowIntake;
        double shoulder4 = shoulderIntake;
         */

        //--- Position 3 -- arm is ready to deliver
        double claw4 = clawClosedPos;
        double wrist4 = wristDelivery;
        double elbow4 = elbowUp;
        double shoulder4 = shoulderFullBack;

        //--- Position 4 --- specimen is hooked
        double claw5 = clawClosedPos;
        double wrist5 = wristDelivery;
        double elbow5 = elbowHooked;
        double shoulder5 = shoulderHooked;

        //--- State machine for each servo
        clawStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(claw1, claw2, claw3, claw4, claw5)));
        wristStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(wrist1, wrist2, wrist3, wrist4, wrist5)));
        elbowStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(elbow1, elbow2, elbow3, elbow4, elbow5)));
        shoulderStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(shoulder1, shoulder2, shoulder3, shoulder4, shoulder5)));
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
            telemetry.addData("-- State Index", clawStateMachine.getCurrentStepIndex());

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
        if (gamepad2.x) {
            //_servoPos = Math.min(_servoPos + incrementPos, maxPos);
            //_robot.servoArmClawPos = ServoUtils.moveToPosition(_robot.servoArmClaw, _servoPos);
            _servoClawPos = 0.61;
            _robot.servoArmClawPos = ServoUtils.moveToPosition(_robot.servoArmClaw, _servoClawPos);
        }
        if (gamepad2.b) {
            //_servoPos = Math.max(_servoPos - incrementPos, minPos);
            //_robot.servoArmClawPos = ServoUtils.moveToPosition(_robot.servoArmClaw, _servoPos);
            _servoClawPos = 0.76;
            _robot.servoArmClawPos = ServoUtils.moveToPosition(_robot.servoArmClaw, _servoClawPos);

        }
        sleep(50);

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
        if (gamepad1.x) {
            //_servoPos = Math.min(_servoPos + incrementPos, maxPos);
            //_robot.servoArmWristPos = ServoUtils.moveToPosition(_robot.servoArmWrist, _servoPos);
            _servoWristPos = 0.03;
            _robot.servoArmWristPos = ServoUtils.moveToPosition(_robot.servoArmWrist, _servoWristPos);
        }
        if (gamepad1.b) {
            //_servoPos = Math.max(_servoPos - incrementPos, minPos);
            //_robot.servoArmWristPos = ServoUtils.moveToPosition(_robot.servoArmWrist, _servoPos);
            _servoWristPos = 0.69;
            _robot.servoArmWristPos = ServoUtils.moveToPosition(_robot.servoArmWrist, _servoWristPos);
        }
        sleep(50);

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
            _servoElbowPos = Math.min(_servoElbowPos + incrementPos, maxPos);
            _robot.servoArmElbowPos = ServoUtils.moveToPosition(_robot.servoArmElbow, _servoElbowPos);
        }
        if (gamepad1.a) {
            _servoElbowPos = Math.max(_servoElbowPos - incrementPos, minPos);
            _robot.servoArmElbowPos = ServoUtils.moveToPosition(_robot.servoArmElbow, _servoElbowPos);
        }
        sleep(50);

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
        if (gamepad2.y) {
            _servoShoulderPos = Math.min(_servoShoulderPos + incrementPos, maxPos);
            _robot.servoArmShoulderPos = ServoUtils.moveToPosition(_robot.servoArmShoulder, _servoShoulderPos);
        }
        if (gamepad2.a) {
            _servoShoulderPos = Math.max(_servoShoulderPos - incrementPos, minPos);
            _robot.servoArmShoulderPos = ServoUtils.moveToPosition(_robot.servoArmShoulder, _servoShoulderPos);
        }
        sleep(50);

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Arm Shoulder", "%4.2f", _robot.servoArmShoulderPos);
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
