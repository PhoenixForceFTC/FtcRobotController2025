package org.firstinspires.ftc.teamcode.hardware;

//region --- Imports ---
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.MotorUtils;
import org.firstinspires.ftc.teamcode.utils.ServoUtils;
import org.firstinspires.ftc.teamcode.utils.StateMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//endregion

public class Arm {

    //region --- Constants ---
    private static final double CLAW_OPEN = 0.61;
    private static final double CLAW_CLOSED = 0.76;

    private static final double WRIST_INTAKE = 0.69;
    private static final double WRIST_DELIVERY = 0.03;

    private static final double ELBOW_INTAKE = 0.31;
    private static final double ELBOW_GRABBED = 0.38;
    private static final double ELBOW_MIDDLE = 0.43;
    private static final double ELBOW_UP = 0.13;
    private static final double ELBOW_HOOKED = 0.05;

    private static final double SHOULDER_INTAKE = 0.75;
    private static final double SHOULDER_MIDDLE = 0.59;
    private static final double SHOULDER_FULL_BACK = 0.40;
    private static final double SHOULDER_HOOKED = 0.50;

    private static final int LIFT_LOW = 0;
    private static final int LIFT_HIGH = 2000;
    //endregion

    //region --- Enums ---
    public enum Mode {
        HIGH_BASKET, SPECIMENS, CLIMBING
    }
    //endregion

    //region --- Variables ---
    private final Servo _servoClaw;
    private final Servo _servoWrist;
    private final Servo _servoElbow;
    private final Servo _servoShoulder;
    private final Gamepad _gamepad1;
    private final Gamepad _gamepad2;
    private final Telemetry _telemetry;
    private final boolean _showInfo;

    private StateMachine<Double> _clawStateMachine;
    private StateMachine<Double> _wristStateMachine;
    private StateMachine<Double> _elbowStateMachine;
    private StateMachine<Double> _shoulderStateMachine;
    private StateMachine<Integer> _liftStateMachine;

    private double _servoClawPos = 0.0;
    private double _servoWristPos = 0.0;
    private double _servoElbowPos = 0.0;
    private double _servoShoulderPos = 0.0;
    private int _motorLiftPos = 0;

    private Mode _currentMode = Mode.SPECIMENS; //--- Default mode
    private List<ArmState> _currentStates;
    //endregion

    //region --- Constructor ---
    public Arm(Servo servoClaw, Servo servoWrist, Servo servoElbow, Servo servoShoulder,
               Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, boolean showInfo)
    {
        this._servoClaw = servoClaw;
        this._servoWrist = servoWrist;
        this._servoElbow = servoElbow;
        this._servoShoulder = servoShoulder;
        this._gamepad1 = gamepad1;
        this._gamepad2 = gamepad2;
        this._telemetry = telemetry;
        this._showInfo = showInfo;

        switchMode(Mode.SPECIMENS);
    }
    //endregion

    //region --- Initialize State Machines ---
    private void stepsForSpecimens()
    {
        List<ArmState> specimensStates = Arrays.asList(
                new ArmState(CLAW_OPEN,   WRIST_INTAKE,   ELBOW_MIDDLE,  SHOULDER_MIDDLE,    LIFT_LOW),
                new ArmState(CLAW_CLOSED, WRIST_INTAKE,   ELBOW_MIDDLE,  SHOULDER_MIDDLE,    LIFT_LOW),
                new ArmState(CLAW_CLOSED, WRIST_INTAKE,   ELBOW_GRABBED, SHOULDER_MIDDLE,    LIFT_HIGH),
                new ArmState(CLAW_CLOSED, WRIST_DELIVERY, ELBOW_UP,      SHOULDER_FULL_BACK, LIFT_LOW),
                new ArmState(CLAW_CLOSED, WRIST_DELIVERY, ELBOW_HOOKED,  SHOULDER_HOOKED,    LIFT_LOW)
        );
        _currentStates = specimensStates;
        updateStateMachines(_currentStates);
    }

    private void stepsForHighBasket()
    {
        List<ArmState> highBasketStates = Arrays.asList(
                new ArmState(CLAW_CLOSED, WRIST_DELIVERY, ELBOW_UP, SHOULDER_FULL_BACK, LIFT_LOW)
                // Add more as needed
        );
        _currentStates = highBasketStates;
        updateStateMachines(_currentStates);
    }

    private void stepsForClimbing() {
        List<ArmState> climbingStates = Arrays.asList(
                new ArmState(CLAW_CLOSED, WRIST_DELIVERY, ELBOW_GRABBED, SHOULDER_HOOKED, LIFT_LOW)
                // Add more as needed
        );
        _currentStates = climbingStates;
        updateStateMachines(_currentStates);
    }

    private void updateStateMachines(List<ArmState> states)
    {
        _clawStateMachine = new StateMachine<>(extractValues(states, ArmState::getClaw));
        _wristStateMachine = new StateMachine<>(extractValues(states, ArmState::getWrist));
        _elbowStateMachine = new StateMachine<>(extractValues(states, ArmState::getElbow));
        _shoulderStateMachine = new StateMachine<>(extractValues(states, ArmState::getShoulder));
        _liftStateMachine = new StateMachine<>(extractValues(states, ArmState::getLift));
    }
    //endregion

    //region --- Arm Control ---
    public void controlArm()
    {
        if (_gamepad2.y)
        {
            switchMode(Mode.HIGH_BASKET);
        }
        else if (_gamepad2.a)
        {
            switchMode(Mode.SPECIMENS);
        }
        else if (_gamepad2.b)
        {
            switchMode(Mode.CLIMBING);
        }

        if (_gamepad1.y)
        {
            moveToNextState();
            sleep(250);
        }
        if (_gamepad1.a)
        {
            moveToPreviousState();
            sleep(250);
        }

        if (_showInfo)
        {
            _telemetry.addData("Arm -> Mode", _currentMode);
            _telemetry.addData("Arm -> State Index", _clawStateMachine.getCurrentStepIndex());
            _telemetry.addData("Arm -> Claw Pos", "%4.2f", _servoClawPos);
            _telemetry.addData("Arm -> Wrist Pos", "%4.2f", _servoWristPos);
            _telemetry.addData("Arm -> Elbow Pos", "%4.2f", _servoElbowPos);
            _telemetry.addData("Arm -> Shoulder Pos", "%4.2f", _servoShoulderPos);
            _telemetry.addData("Lift -> Lift Pos", _motorLiftPos);
        }
    }

    private void switchMode(Mode newMode)
    {
        if (_currentMode != newMode)
        {
            _currentMode = newMode;
            initializeStatesForMode(newMode);
        }
    }

    private void initializeStatesForMode(Mode mode)
    {
        if (mode == Mode.SPECIMENS)
        {
            stepsForSpecimens();
        }
        else if (mode == Mode.HIGH_BASKET)
        {
            stepsForHighBasket();
        }
        else if (mode == Mode.CLIMBING)
        {
            stepsForClimbing();
        }
    }

    private void moveToNextState()
    {
        _servoClawPos = ServoUtils.moveToPosition(_servoClaw, _clawStateMachine.next());
        _servoWristPos = ServoUtils.moveToPosition(_servoWrist, _wristStateMachine.next());
        _servoElbowPos = ServoUtils.moveToPosition(_servoElbow, _elbowStateMachine.next());
        _servoShoulderPos = ServoUtils.moveToPosition(_servoShoulder, _shoulderStateMachine.next());
        //_motorLiftPos =  MotorUtils.moveToTargetPosition();
    }

    private void moveToPreviousState()
    {
        _servoClawPos = ServoUtils.moveToPosition(_servoClaw, _clawStateMachine.previous());
        _servoWristPos = ServoUtils.moveToPosition(_servoWrist, _wristStateMachine.previous());
        _servoElbowPos = ServoUtils.moveToPosition(_servoElbow, _elbowStateMachine.previous());
        _servoShoulderPos = ServoUtils.moveToPosition(_servoShoulder, _shoulderStateMachine.previous());
        //_motorLiftPos = MotorUtils.moveToTargetPosition();
    }
    //endregion

    //region --- Helper Class ---
    private static class ArmState {
        private final double claw, wrist, elbow, shoulder;
        private final int lift;

        public ArmState(double claw, double wrist, double elbow, double shoulder, int lift) {
            this.claw = claw;
            this.wrist = wrist;
            this.elbow = elbow;
            this.shoulder = shoulder;
            this.lift = lift;
        }

        public double getClaw() {
            return claw;
        }

        public double getWrist() {
            return wrist;
        }

        public double getElbow() {
            return elbow;
        }

        public double getShoulder() {
            return shoulder;
        }

        public int getLift() {
            return lift;
        }
    }
    //endregion

    //region --- Fine Tuning ---

    public void fineTuneArm()
    {
        fineTuneClaw();
        fineTuneWrist();
        fineTuneElbow();
        fineTuneShoulder();
    }

    private void fineTuneClaw()
    {
        //--- wide open = 0.61
        //--- closed = 0.76

        double incrementPos = 0.01; // Amount to increment/decrement
        double minPos = 0.0;       // Minimum servo position
        double maxPos = 1.0;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (_gamepad2.x)
        {
//            servoClawPos = Math.min(servoClawPos + incrementPos, maxPos);
            _servoClawPos = 0.61; //--- Closed
            ServoUtils.moveToPosition(_servoClaw, _servoClawPos);
        }
        if (_gamepad2.b)
        {
//            servoClawPos = Math.min(servoClawPos - incrementPos, minPos);
            _servoClawPos = 0.76; //--- Wide open
            ServoUtils.moveToPosition(_servoClaw, _servoClawPos);
        }
        sleep(50);

        //--- Show messages
        if (_showInfo)
        {
            _telemetry.addData("Arm -> Claw", "%4.2f", _servoClawPos);
        }
    }

    private void fineTuneWrist()
    {
        //--- intake (pronated) = 0.03
        //--- delivery (supinated) = 0.69

        double incrementPos = 0.01; // Amount to increment/decrement
        double minPos = 0.0;       // Minimum servo position
        double maxPos = 1.0;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (_gamepad2.x)
        {
//            servoWristPos = Math.min(servoWristPos + incrementPos, maxPos);
            _servoWristPos = 0.03; //--- Intake (pronated)
            ServoUtils.moveToPosition(_servoWrist, _servoWristPos);
        }
        if (_gamepad2.b)
        {
//            servoWristPos = Math.min(servoWristPos - incrementPos, minPos);
            _servoWristPos = 0.69; //--- Delivery (supinated)
            ServoUtils.moveToPosition(_servoWrist, _servoWristPos);
        }
        sleep(50);

        //--- Show messages
        if (_showInfo)
        {
            _telemetry.addData("Arm -> Wrist", "%4.2f", _servoWristPos);
        }
    }

    private void fineTuneElbow()
    {
        //--- intake = 0.31
        //---

        double incrementPos = 0.01; // Amount to increment/decrement
        double minPos = 0.0;       // Minimum servo position
        double maxPos = 1.0;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (_gamepad1.y)
        {
            _servoElbowPos = Math.min(_servoElbowPos + incrementPos, maxPos);
            ServoUtils.moveToPosition(_servoElbow, _servoElbowPos);
        }
        if (_gamepad1.a)
        {
            _servoElbowPos = Math.min(_servoElbowPos - incrementPos, minPos);
            ServoUtils.moveToPosition(_servoElbow, _servoElbowPos);
        }
        sleep(50);

        //--- Show messages
        if (_showInfo)
        {
            _telemetry.addData("Arm -> Elbow", "%4.2f", _servoElbowPos);
        }
    }

    private void fineTuneShoulder()
    {
        //--- intake = 0.75
        //--- full back = 0.14

        double incrementPos = 0.01; // Amount to increment/decrement
        double minPos = 0.0;       // Minimum servo position
        double maxPos = 1.0;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (_gamepad2.y)
        {
            _servoShoulderPos = Math.min(_servoShoulderPos + incrementPos, maxPos);
            ServoUtils.moveToPosition(_servoShoulder, _servoShoulderPos);
        }
        if (_gamepad2.a)
        {
            _servoShoulderPos = Math.min(_servoShoulderPos - incrementPos, minPos);
            ServoUtils.moveToPosition(_servoShoulder, _servoShoulderPos);
        }
        sleep(50);

        //--- Show messages
        if (_showInfo)
        {
            _telemetry.addData("Arm -> Shoulder", "%4.2f", _servoShoulderPos);
        }
    }

    //endregion

    //region --- Utility Methods ---
    private static <T> List<T> extractValues(List<ArmState> states, java.util.function.Function<ArmState, T> getter) {
        List<T> values = new ArrayList<>();
        for (ArmState state : states) {
            values.add(getter.apply(state));
        }
        return values;
    }

    //--- TODO: Move to util class
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    //endregion
}

//    public void initialize()
//    {
//        //--- Constants
//        double clawOpenPos = 0.61;
//        double clawClosedPos = 0.76;
//
//        double wristIntake = 0.69;
//        double wristDelivery = 0.03;
//
//        double elbowIntake = 0.31;
//        double elbowGrabbed = 0.38;
//        double elbowMiddle = 0.43;
//        double elbowUp = 0.13; //???
//        double elbowHooked = 0.05;
//
//        double shoulderIntake = 0.75;
//        double shoulderMiddle = 0.59;
//        double shoulderFullBack = 0.40;
//        double shoulderHooked = 0.5;
//
//
//        //--- Position 0 -- arm is grabbing specimens
//        double claw1 = clawOpenPos;
//        double wrist1 = wristIntake;
//        double elbow1 = elbowMiddle;
//        double shoulder1 = shoulderMiddle;
//        int lift1 = 0;
//
//        //--- Position 1 --- arm grabs specimens
//        double claw2 = clawClosedPos;
//        double wrist2 = wristIntake;
//        double elbow2 = elbowMiddle;
//        double shoulder2 = shoulderMiddle;
//        int lift2 = 0;
//
//        //--- Position 2 --- arm moves up
//        double claw3 = clawClosedPos;
//        double wrist3 = wristIntake;
//        double elbow3 = elbowGrabbed;
//        double shoulder3 = shoulderMiddle;
//        int lift3 = 2000;
//
//        /*
//        //--- Position 3 -- arm is positioned for the intake
//        double claw4 = clawOpenPos;
//        double wrist4 = wristIntake;
//        double elbow4 = elbowIntake;
//        double shoulder4 = shoulderIntake;
//         */
//
//        //--- Position 3 -- arm is ready to deliver
//        double claw4 = clawClosedPos;
//        double wrist4 = wristDelivery;
//        double elbow4 = elbowUp;
//        double shoulder4 = shoulderFullBack;
//        int lift4 = 0;
//
//        //--- Position 4 --- specimen is hooked
//        double claw5 = clawClosedPos;
//        double wrist5 = wristDelivery;
//        double elbow5 = elbowHooked;
//        double shoulder5 = shoulderHooked;
//        int lift5 = 0;
//
//        //--- State machine for each servo
//        _clawStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(claw1, claw2, claw3, claw4, claw5)));
//        _wristStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(wrist1, wrist2, wrist3, wrist4, wrist5)));
//        _elbowStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(elbow1, elbow2, elbow3, elbow4, elbow5)));
//        _shoulderStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(shoulder1, shoulder2, shoulder3, shoulder4, shoulder5)));
//        _liftStateMachine = new StateMachine<>(new ArrayList<>(Arrays.asList(lift1, lift2, lift3, lift4, lift5)));
//
//        //--- Goes to first state when initialized
//        moveToPreviousState();
//    }