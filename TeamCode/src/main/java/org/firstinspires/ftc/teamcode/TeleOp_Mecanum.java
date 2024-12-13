package org.firstinspires.ftc.teamcode;

//region -- Imports ---
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utils.DriveUtils;
import org.firstinspires.ftc.teamcode.utils.MotorUtils;
import org.firstinspires.ftc.teamcode.utils.ServoUtils;
import org.firstinspires.ftc.teamcode.utils.StateMachine;

import java.util.ArrayList;
import java.util.Arrays;
//endregion

//----------------------------------------------------------------------
// Gamepad 1
//  - Left Stick    - Mecanum Drive
//  - Right Stick   - Mecanum Turning
//  - Dpad Up       -    //Moving forward
//  - Dpad Down     -    //Moving backward
//  - Dpad Right    -    //Moving right
//  - Dpad Left     -    //Moving left
//  - Left Trigger  - Intake Arm Out
//  - Left Bumpers  - Intake Arm Back
//  - Right Trigger -
//  - Right Bumpers -
//  - X             -    //Speed slow
//  - Y             -    //Speed medium
//  - B             -    //Speed fast
//
// Gamepad 2
//  - Dpad Up       -    //Arm up
//  - Dpad Down     -    //Arm down
//  - Right Trigger -
//  - L/R Bumpers   -
//  - Y             -    //Intake
//  - A             -    //Output
//  - B             -    //Releas intake
//  - Right stick   -    //Eye movement
//----------------------------------------------------------------------

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
    private static final int MOTOR_INTAKE_MAX_POSITION = 100;    // Maximum position (fully extended)
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

            //------------------------------------------------------------------------------------------
            //--- Intake Motor - Drive by Power
            //------------------------------------------------------------------------------------------
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


            //------------------------------------------------------------------------------------------
            //--- Lift Motors - Drive by Power
            //------------------------------------------------------------------------------------------
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


            //------------------------------------------------------------------------------------------
            //--- Intake Motor - Drive by Encoder Position
            //--- Advantage of moving the motor with Encoder Position, is we can set the distance limits
            //------------------------------------------------------------------------------------------
            //--- Encoder-controlled motorIntake movement
            if (gamepad1.left_trigger > 0.1) //--- Extend when left trigger is pressed
            {
                MotorUtils.setTargetPosition(_robot.motorIntake, MOTOR_INTAKE_MAX_POSITION, 1.0);
            }
            else if (gamepad1.left_bumper) //--- Retract when left bumper is pressed
            {
                MotorUtils.setTargetPosition(_robot.motorIntake, MOTOR_INTAKE_MIN_POSITION, 1.0);
            }
            else
            {
                //--- Stop the motor when no input
                //--- NOTE this will stop the motor at whatever position it is at when the trigger or bumper is released
                MotorUtils.stopMotor(_robot.motorIntake);
            }

            if (gamepad1.x)
            {
                MotorUtils.resetEncoder(_robot.motorIntake);
            }

            telemetry.addData("motorIntake Position", _robot.motorIntake.getCurrentPosition());
            telemetry.addData("motorIntake Target", _robot.motorIntake.getTargetPosition());

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
