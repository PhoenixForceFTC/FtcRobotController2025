package org.firstinspires.ftc.teamcode;

//region --- Imports ---
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
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
1 -
2 - in-rspin (Intake Right Spinner)
3 - in-lup (Intake Left Up/Down)
4 - in-rup (Intake Right Up/Down)
5 -
*/
//endregion

//region --- Expansion Hub Config ---
/*
Motor
0 - rl (Read Left)
1 - rr (Read Right)
2 - in-arm (Intake Extension Arm)
3 -

Servo
0 -
1 -
2 - Arm Servo: arm1
3 - Arm Servo: arm2
4 - Arm Servo: arm3
5 - Arm Servo: arm4

I2C
0 - color
1 -
2 - camera
3 - odom (odometry)
*/
//endregion

public class RobotHardware {

    //------------------------------------------------------------------------------------------
    //--- OpMode
    //------------------------------------------------------------------------------------------
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    //------------------------------------------------------------------------------------------
    //--- Utility
    //------------------------------------------------------------------------------------------

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
    public Servo servoArmShoulder = null;
    public Servo servoArmClaw = null;

    //--- Servo Power
    public double servoIntakeSpinRightPower = 0.0;
    public double servoIntakeSpinLeftPower = 0.0;

    //--- Servo Position
    public double servoIntakeLiftLeftPos = 0.0;
    public double servoIntakeLiftRightPos = 0.0;

    public double servoArmWristPos = 0.0;
    public double servoArmElbowPos = 0.0;
    public double servoArmShoulderPos = 0.0;
    public double servoArmClawPos = 0.0;

    //------------------------------------------------------------------------------------------
    //--- Constants
    //------------------------------------------------------------------------------------------

    public static final double SERVO_INTAKE_LIFT_IN = 0.85;
    public static final double SERVO_INTAKE_LIFT_OUT = 0.43;    //--- Out straight in front of robot

    //--- Define a constructor that allows the OpMode to pass a reference to itself.
    public RobotHardware(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    /**
     * Initialize all the robot's hardware.
     * This method must be called ONCE when the OpMode is initialized.
     */
    public void init()    {
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

        motorIntake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorIntake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorLiftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLiftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorLiftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLiftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //------------------------------------------------------------------------------------------
        //--- Servo Config
        //------------------------------------------------------------------------------------------
        //--- Servos (Continuous)
        servoIntakeSpinRight = myOpMode.hardwareMap.get(CRServo.class, "in-rspin"); //--- Intake Right Spinner
        servoIntakeSpinLeft = myOpMode.hardwareMap.get(CRServo.class, "in-lspin"); //--- Intake Left Spinner

        //--- Servos (Positional)
        servoIntakeLiftLeft = myOpMode.hardwareMap.get(Servo.class, "in-lup"); //--- Intake Left Up/Down
        servoIntakeLiftRight = myOpMode.hardwareMap.get(Servo.class, "in-rup"); //--- Intake Right Up/Down

        servoArmClaw = myOpMode.hardwareMap.get(Servo.class, "arm1"); //--- Arm Grabber / Claw
        servoArmWrist = myOpMode.hardwareMap.get(Servo.class, "arm2"); //--- Arm Wrist
        servoArmElbow = myOpMode.hardwareMap.get(Servo.class, "arm3"); //--- Arm Elbow
        servoArmShoulder = myOpMode.hardwareMap.get(Servo.class, "arm4"); //--- Arm Shoulder

        //--- Configure Servos
        //servoArmWrist.setDirection(Servo.Direction.REVERSE);

        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }
}
