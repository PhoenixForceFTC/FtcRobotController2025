package org.firstinspires.ftc.teamcode;

//region -- Imports ---
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
//endregion

@Disabled
@TeleOp(name="Test", group="1")
public class TeleOp_MecanumTester extends LinearOpMode
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
        //--- Robot Initialize
        //------------------------------------------------------------------------------------------
        int robotVersion = 1; //--- 1 for CRAB-IER and 2 for ARIEL
        _robot.init(robotVersion);

        //------------------------------------------------------------------------------------------
        //--- Display and wait for the game to start (driver presses START)
        //------------------------------------------------------------------------------------------
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        _runtime.reset();

        //------------------------------------------------------------------------------------------
        //--- Hardware Initialize
        //------------------------------------------------------------------------------------------
        _robot.arm.initialize();
        _robot.intake.initialize();
        _robot.lift.initialize();

        //------------------------------------------------------------------------------------------
        //--- Run until the end of the match (driver presses STOP)
        //------------------------------------------------------------------------------------------
        while (opModeIsActive()) {

            //------------------------------------------------------------------------------------------
            //--- Start Telemetry Display
            //------------------------------------------------------------------------------------------
            telemetry.addData("Status", "Run Time: " + _runtime.toString());

            //------------------------------------------------------------------------------------------
            //--- Drive
            //------------------------------------------------------------------------------------------
            //_robot.drive.directionDrive(0.5);  //--- D-pad for directional movement
            //_robot.drive.arcadeDriveSpeedControl();  //--- Joysticks for mecanum movement

            //------------------------------------------------------------------------------------------
            //--- Intake
            //------------------------------------------------------------------------------------------

            //_robot.intake.intakeByEncoder();
            //_robot.intake.setSpinnerControls();
            //_robot.intake.setLiftArmControls();

            //------------------------------------------------------------------------------------------
            //--- Arm
            //------------------------------------------------------------------------------------------
            //_robot.arm.controlArm();

            _robot.lift.liftByEncoder();

            //------------------------------------------------------------------------------------------
            //--- Update Telemetry Display
            //------------------------------------------------------------------------------------------
            telemetry.update();
        }
    }
}
