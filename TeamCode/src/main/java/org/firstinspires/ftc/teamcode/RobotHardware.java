package org.firstinspires.ftc.teamcode;

//region --- Imports ---
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.Arm;
import org.firstinspires.ftc.teamcode.hardware.Drive;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Lift;
//endregion

//region --- Control Hub Config ---
/*
Motor
0 - upl (Lifting Arm Left) + Encoder
1 - upr (Lifting Arm Righ) + Encoder
2 - fr (Front Right)
3 - fl (Front Left)

Servo
0 - in-lspin (Intake Left Spinner)
1 - ????? (not working)
2 - in-rspin (Intake Right Spinner)
3 - in-lup (Intake Left Up/Down)
4 - in-rup (Intake Right Up/Down)
5 - claw
*/
//endregion

//region --- Expansion Hub Config ---
/*
Motor
0 - rl (Rear Left)
1 - rr (Rear Right)
2 - in-arm (Intake Extension Arm)
3 -

Servo
0 - shoulder-r
1 - elbow
2 - wrist
3 - ????? (not working)
4 - ????? (not working)
5 - shoulder-l

I2C
0 - color
1 -
2 - camera
3 - odom (odometry)
*/
//endregion

public class RobotHardware {

    //------------------------------------------------------------------------------------------
    //--- Settings
    //------------------------------------------------------------------------------------------
    boolean _showInfo = true;

    //------------------------------------------------------------------------------------------
    //--- OpMode
    //------------------------------------------------------------------------------------------
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    //------------------------------------------------------------------------------------------
    //--- Drive Motors
    //------------------------------------------------------------------------------------------
    public DcMotor motorDriveFrontRight = null;
    public DcMotor motorDriveFrontLeft = null;
    public DcMotor motorDriveRearRight = null;
    public DcMotor motorDriveRearLeft = null;

    //------------------------------------------------------------------------------------------
    //--- Utility Motors
    //------------------------------------------------------------------------------------------
    public DcMotor motorIntake = null;
    public DcMotor motorLiftLeft = null;
    public DcMotor motorLiftRight = null;

    //------------------------------------------------------------------------------------------
    //--- Servos
    //------------------------------------------------------------------------------------------
    //--- Intake Servo
    public Servo servoIntakeLiftLeft = null;
    public Servo servoIntakeLiftRight = null;

    public CRServo servoIntakeSpinLeft = null;
    public CRServo servoIntakeSpinRight = null;

    //--- Arm Servos
    public Servo servoArmWrist = null;
    public Servo servoArmElbow = null;
    public Servo servoArmShoulderRight = null;
    public Servo servoArmShoulderLeft = null;
    public Servo servoArmClaw = null;

    //------------------------------------------------------------------------------------------
    //--- Custom Hardware Classes
    //------------------------------------------------------------------------------------------
    public Intake intake;
    public Drive drive;
    public Lift lift;
    public Arm arm;

    //------------------------------------------------------------------------------------------
    //--- Define a constructor that allows the OpMode to pass a reference to itself
    //------------------------------------------------------------------------------------------
    public RobotHardware(LinearOpMode opmode)
    {
        myOpMode = opmode;
    }

    /**
     * Initialize all the robot's hardware.
     * This method must be called ONCE when the OpMode is initialized.
     */
    public void init()
    {
        //------------------------------------------------------------------------------------------
        //--- Motor Config
        //------------------------------------------------------------------------------------------
        //--- Drive Motors
        motorDriveFrontLeft = myOpMode.hardwareMap.get(DcMotor.class, "fl");
        motorDriveRearLeft = myOpMode.hardwareMap.get(DcMotor.class, "rl");
        motorDriveFrontRight = myOpMode.hardwareMap.get(DcMotor.class, "fr");
        motorDriveRearRight = myOpMode.hardwareMap.get(DcMotor.class, "rr");

        //--- Drive Motor Directions
        motorDriveFrontLeft.setDirection(DcMotor.Direction.FORWARD);
        motorDriveRearLeft.setDirection(DcMotor.Direction.FORWARD);
        motorDriveFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorDriveRearRight.setDirection(DcMotor.Direction.REVERSE);

        //--- Utility Motors
        motorIntake = myOpMode.hardwareMap.get(DcMotor.class, "in-arm"); //--- Intake Extension Arm
        motorLiftLeft = myOpMode.hardwareMap.get(DcMotor.class, "upl"); //--- Lifting Arm Left
        motorLiftRight = myOpMode.hardwareMap.get(DcMotor.class, "upr"); //--- Lifting Arm Right

        motorLiftLeft.setDirection(DcMotor.Direction.REVERSE);
        motorLiftRight.setDirection(DcMotor.Direction.REVERSE);

        motorIntake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorIntake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorLiftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLiftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorLiftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLiftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //------------------------------------------------------------------------------------------
        //--- Servo Config
        //------------------------------------------------------------------------------------------
        //--- Servos (Continuous)
        servoIntakeSpinRight = myOpMode.hardwareMap.get(CRServo.class, "in-rspin"); //--- Intake Right Spinner
        servoIntakeSpinLeft = myOpMode.hardwareMap.get(CRServo.class, "in-lspin"); //--- Intake Left Spinner

        //--- Servos (Positional)
        servoIntakeLiftLeft = myOpMode.hardwareMap.get(Servo.class, "in-lup"); //--- Intake Left Up/Down
        servoIntakeLiftRight = myOpMode.hardwareMap.get(Servo.class, "in-rup"); //--- Intake Right Up/Down

        servoArmClaw = myOpMode.hardwareMap.get(Servo.class, "claw"); //--- Arm Grabber / Claw
        servoArmWrist = myOpMode.hardwareMap.get(Servo.class, "wrist"); //--- Arm Wrist
        servoArmElbow = myOpMode.hardwareMap.get(Servo.class, "elbow"); //--- Arm Elbow
        servoArmShoulderRight = myOpMode.hardwareMap.get(Servo.class, "shoulder-r"); //--- Arm Shoulder
        servoArmShoulderLeft = myOpMode.hardwareMap.get(Servo.class, "shoulder-l"); //--- Arm Shoulder

        //--- Configure Servos
        //servoArmWrist.setDirection(Servo.Direction.REVERSE);

        //------------------------------------------------------------------------------------------
        //--- Hardware Constructors
        //------------------------------------------------------------------------------------------
        intake = new Intake(
                motorIntake,
                servoIntakeSpinLeft,
                servoIntakeSpinRight,
                servoIntakeLiftLeft,
                servoIntakeLiftRight,
                myOpMode.gamepad1,
                myOpMode.telemetry,
                _showInfo
        );

        drive = new Drive(
                motorDriveFrontLeft,
                motorDriveFrontRight,
                motorDriveRearLeft,
                motorDriveRearRight,
                myOpMode.gamepad1,
                myOpMode.telemetry,
                _showInfo
        );

        lift = new Lift(
                motorLiftLeft,
                motorLiftRight,
                myOpMode.gamepad1,
                myOpMode.telemetry,
                _showInfo
        );

        arm = new Arm(
                servoArmClaw,
                servoArmWrist,
                servoArmElbow,
                servoArmShoulderRight,
                servoArmShoulderLeft,
                myOpMode.gamepad1,
                myOpMode.gamepad2,
                myOpMode.telemetry,
                _showInfo,
                lift
        );

        //------------------------------------------------------------------------------------------
        //--- Messages
        //------------------------------------------------------------------------------------------
        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }
}
