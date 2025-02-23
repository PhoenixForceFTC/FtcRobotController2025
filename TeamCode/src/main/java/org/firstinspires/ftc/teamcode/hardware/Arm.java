package org.firstinspires.ftc.teamcode.hardware;

//region --- Imports ---
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.ServoUtils;
import org.firstinspires.ftc.teamcode.utils.StateMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//endregion

public class Arm {

    //region --- Constants ---
    private static double CLAW_WIDE, CLAW_OPEN, CLAW_CLOSED, WRIST_INTAKE, WRIST_DELIVERY;
    //endregion

    public void initialize()
    {
        if (_robotVersion == 1) //--- CRAB-IER
        {
            CLAW_WIDE = 0.5;
            CLAW_OPEN = 0.61;
            CLAW_CLOSED = 0.76;

            WRIST_INTAKE = 0.03;
            WRIST_DELIVERY = 0.69;
        }
        else //--- ARIEL
        {
            CLAW_WIDE = 0.5;
            CLAW_OPEN = 0.61;
            CLAW_CLOSED = 0.76;

            WRIST_INTAKE = 0.03;
            WRIST_DELIVERY = 0.69;
        }
    }

    //region --- State Machine Steps ---
    private void stepsForSpecimens() { //--- WORKING

        if (_robotVersion == 1) //--- CRAB-IER
        {
            _currentStates = Arrays.asList(
                //--- Pick up specimen from side
                new ArmState(CLAW_WIDE, WRIST_INTAKE, 0.34, 0.45, LiftAction.BOTTOM),
                //--- Grab specimen
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.34, 0.45, LiftAction.BOTTOM),
                //--- Lift specimen off the wall
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.34, 0.34, LiftAction.BOTTOM),
                //--- Drive to Submersible
                //TODO: Auto Drive
                //--- Arm ready to place specimen
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.79, 0.39, LiftAction.DEL1),
                //--- Arm shoots up to clip specimen
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.79, 0.39, LiftAction.DEL2),
                //--- Open claw
                new ArmState(CLAW_OPEN, WRIST_INTAKE, 0.79, 0.39, LiftAction.DEL2)
            );
        }
        else //--- ARIEL
        {
            _currentStates = Arrays.asList(
                //--- Pick up specimen from side
                new ArmState(CLAW_WIDE, WRIST_INTAKE, 0.34, 0.45, LiftAction.BOTTOM),
                //--- Grab specimen
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.34, 0.45, LiftAction.BOTTOM),
                //--- Lift specimen off the wall
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.34, 0.34, LiftAction.BOTTOM),
                //--- Drive to Submersible
                //TODO: Auto Drive
                //--- Arm ready to place specimen
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.79, 0.39, LiftAction.DEL1),
                //--- Arm shoots up to clip specimen
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.79, 0.39, LiftAction.DEL2),
                //--- Open claw
                new ArmState(CLAW_OPEN, WRIST_INTAKE, 0.79, 0.39, LiftAction.DEL2)
            );
        }

        updateStateMachines(_currentStates);
    }

    private void stepsForHighBasket()
    {
        if (_robotVersion == 1) //--- CRAB-IER
        {
            _currentStates = Arrays.asList(
                //--- Arm is positioned above for the intake
                new ArmState(CLAW_OPEN, WRIST_INTAKE, 0.31, 0.44, LiftAction.BOTTOM),
                //--- Arm is positioned for the intake
                new ArmState(CLAW_OPEN, WRIST_INTAKE, 0.30, 0.52, LiftAction.BOTTOM),
                //--- Grab the block from intake
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.30, 0.52, LiftAction.BOTTOM),
                //--- Bend arm up to extract block
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.19, 0.44, LiftAction.BOTTOM),
                // ---Intermediate position for driving
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.19, 0.27, LiftAction.BOTTOM),
                //--- Position straight up to not hit basket
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.51, 0.25, LiftAction.HIGH_BASKET),
                //--- Position to drop in basket
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.75, 0.27, LiftAction.HIGH_BASKET),
                //--- Drop in basket
                new ArmState(CLAW_OPEN, WRIST_INTAKE, 0.75, 0.27, LiftAction.HIGH_BASKET)
            );
        }
        else //--- ARIEL
        {
            _currentStates = Arrays.asList(
                //--- Arm is positioned above for the intake
                new ArmState(CLAW_OPEN, WRIST_INTAKE, 0.31, 0.44, LiftAction.BOTTOM),
                //--- Arm is positioned for the intake
                new ArmState(CLAW_OPEN, WRIST_INTAKE, 0.30, 0.52, LiftAction.BOTTOM),
                //--- Grab the block from intake
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.30, 0.52, LiftAction.BOTTOM),
                //--- Bend arm up to extract block
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.19, 0.44, LiftAction.BOTTOM),
                // ---Intermediate position for driving
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.19, 0.27, LiftAction.BOTTOM),
                //--- Position straight up to not hit basket
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.51, 0.25, LiftAction.HIGH_BASKET),
                //--- Position to drop in basket
                new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.75, 0.27, LiftAction.HIGH_BASKET),
                //--- Drop in basket
                new ArmState(CLAW_OPEN, WRIST_INTAKE, 0.75, 0.27, LiftAction.HIGH_BASKET)
            );
        }

        updateStateMachines(_currentStates);
    }

    private void stepsForLowBasket() {

        if (_robotVersion == 1) //--- CRAB-IER
        {
            _currentStates = Arrays.asList(
                    //--- Arm is positioned for the intake
                    new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.30, 0.52, LiftAction.BOTTOM),
                    //--- Position straight up to not hit basket
                    new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.51, 0.25, LiftAction.LOW_BASKET)
                    //TODO - add more
            );
        }
        else //--- ARIEL
        {
            _currentStates = Arrays.asList(
                    //--- Arm is positioned for the intake
                    new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.30, 0.52, LiftAction.BOTTOM),
                    //--- Position straight up to not hit basket
                    new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.51, 0.25, LiftAction.LOW_BASKET)
                    //TODO - add more
            );
        }

        updateStateMachines(_currentStates);
    }

    private void stepsForClimbing() {

        //TODO: Move the intake arm into robot

        if (_robotVersion == 1) //--- CRAB-IER
        {
            _currentStates = Arrays.asList(
                    //TODO - haven't started
                    new ArmState(CLAW_CLOSED, WRIST_DELIVERY, 0.51, 0.25, LiftAction.BOTTOM),
                    new ArmState(CLAW_CLOSED, WRIST_DELIVERY, 0.51, 0.25, LiftAction.BOTTOM)
            );
        }
        else //--- ARIEL
        {
            _currentStates = Arrays.asList(
                    //--- Arm is positioned for the intake
                    new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.30, 0.52, LiftAction.BOTTOM),
                    //--- Position straight up to not hit basket
                    new ArmState(CLAW_CLOSED, WRIST_INTAKE, 0.51, 0.25, LiftAction.LOW_BASKET)
                    //TODO - add more
            );
        }

        updateStateMachines(_currentStates);
    }
    //endregion

    //region --- Enums ---
    public enum Mode {
        HIGH_BASKET,
        SPECIMENS,
        CLIMBING,
        LOW_BASKET,
        DEL1,
        DEL2
    }
    //endregion


    //region --- Variables ---
    private final Servo _servoClaw;
    private final Servo _servoWrist;
    private final Servo _servoElbow;
    private final Servo _servoShoulderRight;
    private final Servo _servoShoulderLeft;
    private final Gamepad _gamepad1;
    private final Gamepad _gamepad2;
    private final Telemetry _telemetry;
    private final boolean _showInfo;
    private final Lift _robotLift;

    private StateMachine<Double> _clawStateMachine;
    private StateMachine<Double> _wristStateMachine;
    private StateMachine<Double> _elbowStateMachine;
    private StateMachine<Double> _shoulderRightStateMachine;
    private StateMachine<Double> _shoulderLeftStateMachine;
    private StateMachine<Integer> _liftStateMachine;

    private double _servoClawPos = 0.0;
    private double _servoWristPos = 0.0;
    private double _servoElbowPos = 0.0;
    private double _servoShoulderRightPos = 0.0;
    private double _servoShoulderLeftPos = 0.0;

    private Mode _currentMode = Mode.SPECIMENS; //--- Default mode
    private List<ArmState> _currentStates;

    private int _robotVersion;
    //endregion

    //region --- Constructor ---
    public Arm(Servo servoClaw, Servo servoWrist, Servo servoElbow, Servo servoShoulderRight, Servo servoShoulderLeft,
               Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry, int robotVersion, boolean showInfo, Lift robotLift)
    {
        this._servoClaw = servoClaw;
        this._servoWrist = servoWrist;
        this._servoElbow = servoElbow;
        this._servoShoulderRight = servoShoulderRight;
        this._servoShoulderLeft = servoShoulderLeft;
        this._gamepad1 = gamepad1;
        this._gamepad2 = gamepad2;
        this._telemetry = telemetry;
        this._robotVersion = robotVersion;
        this._showInfo = showInfo;
        this._robotLift = robotLift; // Initialize the Lift reference

        initializeStatesForMode(_currentMode);
    }
    //endregion

    //region --- Arm Control ---
    public void controlArm()
    {
        if (!areStateMachinesInitialized())
        {
            _telemetry.addData("Error", "State machines are not initialized");
            return;
        }

        //--- Manual overrides for lift
        if (_gamepad2.dpad_up)
        {
            _robotLift.liftByPowerUp(1.0); //--- Moves lift up
        }
        else if (_gamepad2.dpad_down)
        {
            _robotLift.liftByPowerDown(1.0); //--- Moves lift down
        }
        else
        {
            _robotLift.stopLiftIfManual(); //--- Stop if running manually
        }

        //--- Reset the lift encoder
        if (_gamepad2.left_stick_button)
        {
            _robotLift.liftResetEncoder();
        }

        //--- Switch states
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

        //--- Navigate States
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
            _telemetry.addData("Arm -> Shoulder R Pos", "%4.2f", _servoShoulderRightPos);
            _telemetry.addData("Arm -> Shoulder L Pos", "%4.2f", _servoShoulderLeftPos);
            //_telemetry.addData("Lift -> Lift Pos", _robotLift.getCurrentLiftPosition());
            _robotLift.getTelemetry();
        }
    }

    private void updateStateMachines(List<ArmState> states) {
        _clawStateMachine = new StateMachine<>(extractValues(states, ArmState::get_claw));
        _wristStateMachine = new StateMachine<>(extractValues(states, ArmState::get_wrist));
        _elbowStateMachine = new StateMachine<>(extractValues(states, ArmState::get_elbow));
        _shoulderRightStateMachine = new StateMachine<>(extractValues(states, ArmState::get_shoulderRight));
        _shoulderLeftStateMachine = new StateMachine<>(extractValues(states, ArmState::get_shoulderLeft));
    }

    private boolean areStateMachinesInitialized()
    {
        return _clawStateMachine != null &&
                _wristStateMachine != null &&
                _elbowStateMachine != null &&
                _shoulderRightStateMachine != null &&
                _shoulderLeftStateMachine != null &&
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
        if (_shoulderRightStateMachine != null) _shoulderRightStateMachine.reset();
        if (_shoulderLeftStateMachine != null) _shoulderLeftStateMachine.reset();
    }

    private void moveToNextState() {
        //--- Move servos to the next positions
        _servoClawPos = ServoUtils.moveToPosition(_servoClaw, _clawStateMachine.next());
        _servoWristPos = ServoUtils.moveToPosition(_servoWrist, _wristStateMachine.next());
        _servoElbowPos = ServoUtils.moveToPosition(_servoElbow, _elbowStateMachine.next());
        _servoShoulderRightPos = ServoUtils.moveToPosition(_servoShoulderRight, _shoulderRightStateMachine.next());
        _servoShoulderLeftPos = ServoUtils.moveToPosition(_servoShoulderLeft, _shoulderLeftStateMachine.next());

        //--- Execute the lift action for the current state
        _currentStates.get(_clawStateMachine.getCurrentStepIndex()).executeLiftAction(_robotLift);
    }

    private void moveToPreviousState() {
        //--- Move servos to the previous positions
        _servoClawPos = ServoUtils.moveToPosition(_servoClaw, _clawStateMachine.previous());
        _servoWristPos = ServoUtils.moveToPosition(_servoWrist, _wristStateMachine.previous());
        _servoElbowPos = ServoUtils.moveToPosition(_servoElbow, _elbowStateMachine.previous());
        _servoShoulderRightPos = ServoUtils.moveToPosition(_servoShoulderRight, _shoulderRightStateMachine.previous());
        _servoShoulderLeftPos = ServoUtils.moveToPosition(_servoShoulderLeft, _shoulderLeftStateMachine.previous());

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
        DEL1 {
            @Override
            public void execute(Lift lift) {
                lift.moveToDel1();
            }
        },
        DEL2 {
            @Override
            public void execute(Lift lift) {
                lift.moveToDel2();
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
        private final double _claw, _wrist, _elbow, _shoulderRight, _shoulderLeft;
        private final LiftAction _liftAction;

        public ArmState(double claw, double wrist, double elbow, double shoulder, LiftAction liftAction) {
            _claw = claw;
            _wrist = wrist;
            _elbow = elbow;
            _shoulderRight = shoulder;
            _shoulderLeft = shoulder;
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

        public double get_shoulderRight() {
            return _shoulderRight;
        }

        public double get_shoulderLeft() {
            return _shoulderLeft;
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
        //fineTuneClaw();
        fineTuneClawOpenClose();
        //fineTuneWrist();
        fineTuneWristUpDown();
        fineTuneElbow();
        fineTuneShoulder();
    }

    private void fineTuneClawOpenClose()
    {
        //--- wide open = 0.61
        //--- closed = 0.76

        double incrementPos = 0.01; // Amount to increment/decrement
        double minPos = 0.0;       // Minimum servo position
        double maxPos = 1.0;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (_gamepad2.x)
        {
            _servoClawPos = 0.5; //--- Closed
            ServoUtils.moveToPosition(_servoClaw, _servoClawPos);
        }
        if (_gamepad2.b)
        {
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
            _servoClawPos = Math.min(_servoClawPos + incrementPos, maxPos);
            ServoUtils.moveToPosition(_servoClaw, _servoClawPos);
        }
        if (_gamepad2.b)
        {
            _servoClawPos = Math.min(_servoClawPos - incrementPos, minPos);
            ServoUtils.moveToPosition(_servoClaw, _servoClawPos);
        }
        sleep(50);

        //--- Show messages
        if (_showInfo)
        {
            _telemetry.addData("Arm -> Claw", "%4.2f", _servoClawPos);
        }
    }

    private void fineTuneWristUpDown()
    {
        //--- intake (pronated) = 0.03
        //--- delivery (supinated) = 0.69

        double incrementPos = 0.01; // Amount to increment/decrement
        double minPos = 0.0;       // Minimum servo position
        double maxPos = 1.0;       // Maximum servo position

        //--- Adjust servo position based on button presses
        if (_gamepad2.y)
        {
            _servoWristPos = 0.03; //--- Intake (pronated)
            ServoUtils.moveToPosition(_servoWrist, _servoWristPos);
        }
        if (_gamepad2.a)
        {
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
            _servoWristPos = Math.min(_servoWristPos + incrementPos, maxPos);
            ServoUtils.moveToPosition(_servoWrist, _servoWristPos);
        }
        if (_gamepad2.a)
        {
            _servoWristPos = Math.min(_servoWristPos - incrementPos, minPos);
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
        if (_gamepad1.x)
        {
            _servoElbowPos = _servoElbowPos + incrementPos;
            if (_servoElbowPos > maxPos) _servoElbowPos = maxPos;
            ServoUtils.moveToPosition(_servoElbow, _servoElbowPos);
        }
        if (_gamepad1.b)
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
        if (_gamepad1.a)
        {
            _servoShoulderRightPos = _servoShoulderRightPos + incrementPos;
            if (_servoShoulderRightPos > maxPos) _servoShoulderRightPos = maxPos;
            ServoUtils.moveToPosition(_servoShoulderRight, _servoShoulderRightPos);

            _servoShoulderLeftPos = _servoShoulderLeftPos + incrementPos;
            if (_servoShoulderLeftPos > maxPos) _servoShoulderLeftPos = maxPos;
            ServoUtils.moveToPosition(_servoShoulderLeft, _servoShoulderLeftPos);
        }
        if (_gamepad1.y)
        {
            _servoShoulderRightPos = _servoShoulderRightPos - incrementPos;
            if (_servoShoulderRightPos < minPos) _servoShoulderRightPos = minPos;
            ServoUtils.moveToPosition(_servoShoulderRight, _servoShoulderRightPos);

            _servoShoulderLeftPos = _servoShoulderLeftPos - incrementPos;
            if (_servoShoulderLeftPos < minPos) _servoShoulderLeftPos = minPos;
            ServoUtils.moveToPosition(_servoShoulderLeft, _servoShoulderLeftPos);
        }
        sleep(25);

        //--- Show messages
        if (_showInfo)
        {
            _telemetry.addData("Arm -> R Shoulder", "%4.2f", _servoShoulderRightPos);
            _telemetry.addData("Arm -> L Shoulder", "%4.2f", _servoShoulderLeftPos);
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

