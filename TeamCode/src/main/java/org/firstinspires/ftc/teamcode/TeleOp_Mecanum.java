package org.firstinspires.ftc.teamcode;

//region -- Imports ---
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
//endregion

//region --- Controls ---
//----------------------------------------------------------------------
// Gamepad 1 -----------------------------------------------------------
//  - Left Stick        - Mecanum Drive
//  - Right Stick       - Mecanum Rotate
//  - Left Stick Click  - Drive Speed High/Low
//  - Right Stick Click - Rotate Speed High/Low
//
//  - Dpad Up           - Move Forward (Slow)
//  - Dpad Down         - Move Back (Slow)
//  - Dpad Right        - Move Right (Slow)
//  - Dpad Left         - Move Left (Slow)
//
//  - Right Trigger     - Intake Spin In
//  - Right Bumpers     - Intake Spin Out
//  - Left Trigger      - Intake Arm Out
//  - Left Bumpers      - Intake Arm Back
//
//  - Y (▲)             - Next Step in Current Mode
//  - A (✕)             - Previous Step in Current Mode
//  - X (■)             - Intake In
//  - B (○)             - Intake Out
//
//----------------------------------------------------------------------
// Gamepad 2 -----------------------------------------------------------
//  - Left Stick        -
//  - Right Stick       -
//  - Left Stick Click  -
//  - Right Stick Click -
//
//  - Dpad Up           - ??Manual Arm Up
//  - Dpad Down         - ??Manual Arm Down (Reset Encoder)
//  - Dpad Right        - ??Manual Intake Out
//  - Dpad Left         - ??Manual Intake In (Reset Encoder)
//
//  - Right Trigger     -
//  - Right Bumpers     -
//  - Left Trigger      -
//  - Left Bumpers      -

//  - Y (▲)             - ??Mode -> High Basket
//  - A (✕)             - ??Mode -> Specimens
//  - X (■)             -
//  - B (○)             - ??Mode -> Climbing
//----------------------------------------------------------------------
//endregion

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
        //--- Robot Initialize
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
        //--- Hardware Initialize
        //------------------------------------------------------------------------------------------
        _robot.intake.initialize();
        _robot.arm.initialize();

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
            _robot.drive.directionDrive(0.5);  //--- D-pad for directional movement
            _robot.drive.arcadeDriveSpeedControl();  //--- Joysticks for mecanum movement

            //------------------------------------------------------------------------------------------
            //--- Intake
            //------------------------------------------------------------------------------------------
//            _robot.intake.intakeByEncoder();
//            _robot.intake.setSpinnerControls();
//            _robot.intake.setLiftArmControls();

            //------------------------------------------------------------------------------------------
            //--- Arm
            //------------------------------------------------------------------------------------------
            _robot.arm.controlArm();
//            _robot.arm.fineTuneArm();

            //------------------------------------------------------------------------------------------
            //--- Lift
            //------------------------------------------------------------------------------------------
//            _robot.lift.liftByEncoder();

            //------------------------------------------------------------------------------------------
            //--- Update Telemetry Display
            //------------------------------------------------------------------------------------------
            telemetry.update();
        }
    }
}
