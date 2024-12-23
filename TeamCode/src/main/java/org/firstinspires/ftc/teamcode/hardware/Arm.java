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
    //endregion

    //region --- State Machine Steps ---
    private void stepsForSpecimens() {
        _currentStates = Arrays.asList(
                //--- Arm is grabbing specimens from wall
                new ArmState(CLAW_OPEN,   WRIST_INTAKE,   ELBOW_MIDDLE,  SHOULDER_MIDDLE,    LiftAction.BOTTOM),
                //--- Grab specimen
                new ArmState(CLAW_CLOSED, WRIST_INTAKE,   ELBOW_MIDDLE,  SHOULDER_MIDDLE,    LiftAction.BOTTOM),
                //--- Arm moves up
                new ArmState(CLAW_CLOSED, WRIST_INTAKE,   ELBOW_GRABBED, SHOULDER_MIDDLE,    LiftAction.BOTTOM),
                //--- Arm is ready to hook on the bar
                new ArmState(CLAW_CLOSED, WRIST_DELIVERY, ELBOW_UP,      SHOULDER_FULL_BACK, LiftAction.BOTTOM),
                //--- Specimen is hooked
                new ArmState(CLAW_CLOSED, WRIST_DELIVERY, ELBOW_HOOKED,  SHOULDER_HOOKED,    LiftAction.BOTTOM)
        );
        updateStateMachines(_currentStates);
    }

    private void stepsForHighBasket()
    {
        // Add more as needed
        _currentStates = Arrays.asList(
                //--- Arm is positioned for the intake
                new ArmState(CLAW_OPEN,   WRIST_INTAKE,   ELBOW_INTAKE, SHOULDER_INTAKE,   LiftAction.BOTTOM),
                //--- Grab the block from intake
                new ArmState(CLAW_CLOSED, WRIST_INTAKE,   ELBOW_INTAKE, SHOULDER_INTAKE,   LiftAction.BOTTOM),
                //--- (more steps)
                //--- Position to drop in basket
                new ArmState(CLAW_CLOSED, WRIST_DELIVERY, ELBOW_UP,    SHOULDER_FULL_BACK, LiftAction.HIGH_BASKET),
                //--- (more steps)
                new ArmState(CLAW_OPEN, WRIST_DELIVERY, ELBOW_UP, SHOULDER_FULL_BACK, LiftAction.HIGH_BASKET)
        );
        updateStateMachines(_currentStates);
    }

    private void stepsForLowBasket() {

        _currentStates = Arrays.asList(
                new ArmState(CLAW_CLOSED, WRIST_INTAKE,   ELBOW_INTAKE, SHOULDER_INTAKE,    LiftAction.BOTTOM),
                new ArmState(CLAW_CLOSED, WRIST_DELIVERY, ELBOW_UP,     SHOULDER_FULL_BACK, LiftAction.LOW_BASKET)
                //TODO - add more
        );
        updateStateMachines(_currentStates);
    }

    private void stepsForClimbing() {

        //TODO: Move the intake arm into robot

        _currentStates = Arrays.asList(
                //TODO - haven't started
                new ArmState(CLAW_CLOSED, WRIST_DELIVERY, ELBOW_GRABBED, SHOULDER_HOOKED, LiftAction.BOTTOM),
                new ArmState(CLAW_CLOSED, WRIST_DELIVERY, ELBOW_GRABBED, SHOULDER_HOOKED, LiftAction.BOTTOM)
        );
        updateStateMachines(_currentStates);
    }
    //endregion

    //region --- Enums ---
    public enum Mode {
        HIGH_BASKET,
        SPECIMENS,
        CLIMBING,
        LOW_BASKET
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
    private final Lift _robotLift;

    private StateMachine<Double> _clawStateMachine;
    private StateMachine<Double> _wristStateMachine;
    private StateMachine<Double> _elbowStateMachine;
    private StateMachine<Double> _shoulderStateMachine;
    private StateMachine<Integer> _liftStateMachine;

    private double _servoClawPos = 0.0;
    private double _servoWristPos = 0.0;
    private double _servoElbowPos = 0.0;
    private double _servoShoulderPos = 0.0;

    private Mode _currentMode = Mode.SPECIMENS; //--- Default mode
    private List<ArmState> _currentStates;
    //endregion

    //region --- Constructor ---
    public Arm(Servo servoClaw, Servo servoWrist, Servo servoElbow, Servo servoShoulder,
               Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, boolean showInfo, Lift robotLift)
    {
        this._servoClaw = servoClaw;
        this._servoWrist = servoWrist;
        this._servoElbow = servoElbow;
        this._servoShoulder = servoShoulder;
        this._gamepad1 = gamepad1;
        this._gamepad2 = gamepad2;
        this._telemetry = telemetry;
        this._showInfo = showInfo;
        this._robotLift = robotLift; // Initialize the Lift reference

        initializeStatesForMode(_currentMode);
    }
    //endregion

    //region --- Arm Control ---
    public void initialize()
    {
        _servoClawPos = ServoUtils.moveToPosition(_servoClaw, CLAW_OPEN);
        _servoWristPos = ServoUtils.moveToPosition(_servoWrist,  WRIST_INTAKE);
        _servoElbowPos = ServoUtils.moveToPosition(_servoElbow, ELBOW_INTAKE);
        _servoShoulderPos = ServoUtils.moveToPosition(_servoShoulder, SHOULDER_FULL_BACK);
    }

    public void controlArm()
    {
        if (!areStateMachinesInitialized())
        {
            _telemetry.addData("Error", "State machines are not initialized");
            return;
        }
        if(_gamepad2.dpad_up)
        {
            _servoClawPos = ServoUtils.moveToPosition(_servoClaw, CLAW_OPEN);
            _servoWristPos = ServoUtils.moveToPosition(_servoWrist,  WRIST_INTAKE);
            _servoElbowPos = ServoUtils.moveToPosition(_servoElbow, ELBOW_GRABBED);
            _servoShoulderPos = ServoUtils.moveToPosition(_servoShoulder, _shoulderStateMachine.next());
        }
        if (_gamepad2.y)
        {
            switchMode(Mode.HIGH_BASKET);
        }
        else if (_gamepad2.a)
        {
            switchMode(Mode.LOW_BASKET);
        }
        else if (_gamepad2.b)
        {
            switchMode(Mode.SPECIMENS);
        }
        else if (_gamepad2.x)
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
            _telemetry.addData("Lift -> Lift Pos", _robotLift.getCurrentLiftPosition());
        }
    }

    private void updateStateMachines(List<ArmState> states) {
        _clawStateMachine = new StateMachine<>(extractValues(states, ArmState::get_claw));
        _wristStateMachine = new StateMachine<>(extractValues(states, ArmState::get_wrist));
        _elbowStateMachine = new StateMachine<>(extractValues(states, ArmState::get_elbow));
        _shoulderStateMachine = new StateMachine<>(extractValues(states, ArmState::get_shoulder));
    }

    private boolean areStateMachinesInitialized()
    {
        return _clawStateMachine != null &&
                _wristStateMachine != null &&
                _elbowStateMachine != null &&
                _shoulderStateMachine != null &&
                _currentStates != null;
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
        else if (mode == Mode.LOW_BASKET)
        {
            stepsForLowBasket();
        }
        else
        {
            throw new IllegalArgumentException("Unknown Mode: " + mode);
        }

        //--- Reset all state machines to the first step
        if (_clawStateMachine != null) _clawStateMachine.reset();
        if (_wristStateMachine != null) _wristStateMachine.reset();
        if (_elbowStateMachine != null) _elbowStateMachine.reset();
        if (_shoulderStateMachine != null) _shoulderStateMachine.reset();
    }

    private void moveToNextState() {
        //--- Move servos to the next positions
        _servoClawPos = ServoUtils.moveToPosition(_servoClaw, _clawStateMachine.next());
        _servoWristPos = ServoUtils.moveToPosition(_servoWrist, _wristStateMachine.next());
        _servoElbowPos = ServoUtils.moveToPosition(_servoElbow, _elbowStateMachine.next());
        _servoShoulderPos = ServoUtils.moveToPosition(_servoShoulder, _shoulderStateMachine.next());

        //--- Execute the lift action for the current state
        _currentStates.get(_clawStateMachine.getCurrentStepIndex()).executeLiftAction(_robotLift);
    }

    private void moveToPreviousState() {
        //--- Move servos to the previous positions
        _servoClawPos = ServoUtils.moveToPosition(_servoClaw, _clawStateMachine.previous());
        _servoWristPos = ServoUtils.moveToPosition(_servoWrist, _wristStateMachine.previous());
        _servoElbowPos = ServoUtils.moveToPosition(_servoElbow, _elbowStateMachine.previous());
        _servoShoulderPos = ServoUtils.moveToPosition(_servoShoulder, _shoulderStateMachine.previous());

        //--- Execute the lift action for the current state
        _currentStates.get(_clawStateMachine.getCurrentStepIndex()).executeLiftAction(_robotLift);
    }

    public enum LiftAction {
        HIGH_BASKET {
            @Override
            public void execute(Lift lift) {
                lift.moveToHighBasket();
            }
        },
        LOW_BASKET {
            @Override
            public void execute(Lift lift) {
                lift.moveToLowBasket();
            }
        },
        BOTTOM {
            @Override
            public void execute(Lift lift) {
                lift.moveToBottom();
            }
        };

        public abstract void execute(Lift lift);
    }
    //endregion

    //region --- ArmState Class ---
    private static class ArmState {
        private final double _claw, _wrist, _elbow, _shoulder;
        private final LiftAction _liftAction;

        public ArmState(double claw, double wrist, double elbow, double shoulder, LiftAction liftAction) {
            _claw = claw;
            _wrist = wrist;
            _elbow = elbow;
            _shoulder = shoulder;
            _liftAction = liftAction;
        }

        public double get_claw() {
            return _claw;
        }

        public double get_wrist() {
            return _wrist;
        }

        public double get_elbow() {
            return _elbow;
        }

        public double get_shoulder() {
            return _shoulder;
        }

        public void executeLiftAction(Lift lift) {
            if (_liftAction != null) {
                _liftAction.execute(lift);
            }
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
        if (_gamepad2.y)
        {
//            servoWristPos = Math.min(servoWristPos + incrementPos, maxPos);
            _servoWristPos = 0.03; //--- Intake (pronated)
            ServoUtils.moveToPosition(_servoWrist, _servoWristPos);
        }
        if (_gamepad2.a)
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
        if (_gamepad1.a)
        {
            _servoElbowPos = _servoElbowPos + incrementPos;
            if (_servoElbowPos > maxPos) _servoElbowPos = maxPos;
            ServoUtils.moveToPosition(_servoElbow, _servoElbowPos);
        }
        if (_gamepad1.y)
        {
            _servoElbowPos = _servoElbowPos - incrementPos;
            if (_servoElbowPos < minPos) _servoElbowPos = minPos;
            ServoUtils.moveToPosition(_servoElbow, _servoElbowPos);
        }
        if (_gamepad1.right_stick_button)
        {
            ServoUtils.disable(_servoElbow);
        }
        sleep(25);

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
        double minPos = 0.16;       // Minimum servo position
        double maxPos = 0.74;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (_gamepad1.x)
        {
            _servoShoulderPos = _servoShoulderPos + incrementPos;
            if (_servoShoulderPos > maxPos) _servoShoulderPos = maxPos;
            ServoUtils.moveToPosition(_servoShoulder, _servoShoulderPos);
        }
        if (_gamepad1.b)
        {
            _servoShoulderPos = _servoShoulderPos - incrementPos;
            if (_servoShoulderPos < minPos) _servoShoulderPos = minPos;
            ServoUtils.moveToPosition(_servoShoulder, _servoShoulderPos);
        }
        sleep(25);

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

