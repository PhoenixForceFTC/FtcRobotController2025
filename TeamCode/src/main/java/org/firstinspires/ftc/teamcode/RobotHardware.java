package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/*
 * --- Control Hub
 *
 * ------------------------------------------------------------
 *
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

 * ------------------------------------------------------------
 *
--- Expansion Hub
Motor
0 - rl (Read Left)
1 - rr (Read Right)
2 - in-arm (intake extension arm)
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

 * ------------------------------------------------------------
 */

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


    //------------------------------------------------------------------------------------------
    //--- Servos
    //------------------------------------------------------------------------------------------
    //--- Intake Servo
    public Servo servoIntakeUpDownLeft = null;
    public Servo servoIntakeUpDownRight = null;

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
    public double servoIntakeUpDownLeftPos = 0.0;
    public double servoIntakeUpDownRightPos = 0.0;

    public double servoArmWristPos = 0.0;
    public double servoArmElbowPos = 0.0;
    public double servoArmShoulderPos = 0.0;
    public double servoArmGrabPos = 0.0;



    // Define Drive constants.  Make them public so they CAN be used by the calling OpMode
//    public static final double MID_SERVO       =  0.5 ;
//    public static final double HAND_SPEED      =  0.02 ;  // sets rate to move servo
//    public static final double ARM_UP_POWER    =  0.45 ;
//    public static final double ARM_DOWN_POWER  = -0.45 ;

    // Define a constructor that allows the OpMode to pass a reference to itself.
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
        //--- Motors
        motorDriveFrontLeft = myOpMode.hardwareMap.get(DcMotor.class, "fl");
        motorDriveRearLeft = myOpMode.hardwareMap.get(DcMotor.class, "rl");
        motorDriveFrontRight = myOpMode.hardwareMap.get(DcMotor.class, "fr");
        motorDriveRearRight = myOpMode.hardwareMap.get(DcMotor.class, "rr");

        //--- Motor Directions
        motorDriveFrontLeft.setDirection(DcMotor.Direction.FORWARD);
        motorDriveRearLeft.setDirection(DcMotor.Direction.FORWARD);
        motorDriveFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorDriveRearRight.setDirection(DcMotor.Direction.REVERSE);

        //------------------------------------------------------------------------------------------
        //--- Servo Config
        //------------------------------------------------------------------------------------------
        //--- Servos (Continuous)
        servoIntakeSpinRight = myOpMode.hardwareMap.get(CRServo.class, "in-rspin"); //--- Intake Right Spinner (Continuous)
        servoIntakeSpinLeft = myOpMode.hardwareMap.get(CRServo.class, "in-lspin"); //--- Intake Left Spinner (Continuous)

        //--- Servos (Positional)
        servoIntakeUpDownLeft = myOpMode.hardwareMap.get(Servo.class, "in-lup"); //--- Intake Left Up/Down
        servoIntakeUpDownRight = myOpMode.hardwareMap.get(Servo.class, "in-rup"); //--- Intake Right Up/Down

        servoArmWrist = myOpMode.hardwareMap.get(Servo.class, "arm1"); //--- Arm Wrist
        servoArmElbow = myOpMode.hardwareMap.get(Servo.class, "arm2"); //--- Arm Elbow
        servoArmShoulder = myOpMode.hardwareMap.get(Servo.class, "arm3"); //--- Arm Shoulder
        servoArmClaw = myOpMode.hardwareMap.get(Servo.class, "arm4"); //--- Arm Grabber / Claw




        //--- Servo Default Position
//        _intakeUpDownLeft.setPosition(0.5);




        // If there are encoders connected, switch to RUN_USING_ENCODER mode for greater accuracy
        // leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        // Define and initialize ALL installed servos.
//        leftHand = myOpMode.hardwareMap.get(Servo.class, "left_hand");
//        rightHand = myOpMode.hardwareMap.get(Servo.class, "right_hand");
//        leftHand.setPosition(MID_SERVO);
//        rightHand.setPosition(MID_SERVO);

        //inputArm = null;



        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }
}
