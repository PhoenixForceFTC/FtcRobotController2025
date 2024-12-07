package org.firstinspires.ftc.teamcode.NotUsed;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Mecanum Drive - Old", group="TeleOp")
@Disabled
public class NotUsed_TeleOpMecanum extends LinearOpMode
{
    //------------------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------------------
    //--- Utility
    private ElapsedTime _runtime = new ElapsedTime();

    //--- Motors
    private DcMotor _leftFrontDrive = null;
    private DcMotor _leftBackDrive = null;
    private DcMotor _rightFrontDrive = null;
    private DcMotor _rightBackDrive = null;

    //--- Servos
    private Servo _intakeUpDownLeft = null;
    private Servo _intakeUpDownRight = null;

    private CRServo _intakeSpinLeft = null;
    private CRServo _intakeSpinRight = null;

    private Servo _armWrist = null;
    private Servo _armElbow = null;
    private Servo _armShoulder = null;
    private Servo _armClaw = null;

    double _intakeSpinRightPower = 0.0;
    double _intakeSpinLeftPower = 0.0;

    double _intakeUpDownLeftPos = 0.0;
    double _intakeUpDownRightPos = 0.0;

    double _armWristPos = 0.0;
    double _armElbowPos = 0.0;
    double _armShoulderPos = 0.0;
    double _armGrabPos = 0.0;


    @Override
    public void runOpMode()
    {
        //------------------------------------------------------------------------------------------
        //--- Motor Config
        //------------------------------------------------------------------------------------------
        //--- Motors
        _leftFrontDrive = hardwareMap.get(DcMotor.class, "fl");
        _leftBackDrive = hardwareMap.get(DcMotor.class, "rl");
        _rightFrontDrive = hardwareMap.get(DcMotor.class, "fr");
        _rightBackDrive = hardwareMap.get(DcMotor.class, "rr");

        //--- Motor Directions
        _leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        _leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        _rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        _rightBackDrive.setDirection(DcMotor.Direction.REVERSE);

        //------------------------------------------------------------------------------------------
        //--- Servo Config
        //------------------------------------------------------------------------------------------
        //--- Servos (Continuous)
        _intakeSpinRight = hardwareMap.get(CRServo.class, "in-rspin"); //--- Intake Right Spinner (Continuous)
        _intakeSpinLeft = hardwareMap.get(CRServo.class, "in-lspin"); //--- Intake Left Spinner (Continuous)

        //--- Servos (Positional)
        _intakeUpDownLeft = hardwareMap.get(Servo.class, "in-lup"); //--- Intake Left Up/Down
        _intakeUpDownRight = hardwareMap.get(Servo.class, "in-rup"); //--- Intake Right Up/Down

        _armWrist = hardwareMap.get(Servo.class, "arm1"); //--- Arm Wrist
        _armElbow = hardwareMap.get(Servo.class, "arm2"); //--- Arm Elbow
        _armShoulder = hardwareMap.get(Servo.class, "arm3"); //--- Arm Shoulder
        _armClaw = hardwareMap.get(Servo.class, "arm4"); //--- Arm Grabber / Claw

        //--- Servo Default Position
//        _intakeUpDownLeft.setPosition(0.5);

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
            MecanumDrive(true);


            telemetry.addData("Intake Left Up/Down", "%4.2f", _intakeUpDownLeftPos);
            telemetry.addData("Intake Right Up/Down", "%4.2f", _intakeUpDownRightPos);

            telemetry.addData("Arm Wrist", "%4.2f", _armWristPos);
            telemetry.addData("Arm Elbow", "%4.2f", _armElbowPos);
            telemetry.addData("Arm Shoulder", "%4.2f", _armShoulderPos);
            telemetry.addData("Arm Claw", "%4.2f", _armGrabPos);

            telemetry.update();
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
        _leftFrontDrive.setPower(leftFrontPower);
        _rightFrontDrive.setPower(rightFrontPower);
        _leftBackDrive.setPower(leftBackPower);
        _rightBackDrive.setPower(rightBackPower);

        //--- Show messages
        if (showInfo)
        {
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
        }
    }
}
