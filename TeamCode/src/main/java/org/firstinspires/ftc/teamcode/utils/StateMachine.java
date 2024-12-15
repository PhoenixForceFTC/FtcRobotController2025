package org.firstinspires.ftc.teamcode.utils;

//region --- Imports ---
import java.util.List;
//endegion

public class StateMachine<T>
{
    private List<T> _steps;
    private int _currentStepIndex;

    //--- Constructor to initialize steps and starting index
    public StateMachine(List<T> steps)
    {
        _steps = steps;
        _currentStepIndex = 0; // Start at the first step by default
    }

    //--- Move to the next step
    public T next()
    {
        if (_currentStepIndex < _steps.size() - 1)
        {
            _currentStepIndex++;
        }
        return getCurrentStep();
    }

    //--- Move to the previous step
    public T previous()
    {
        if (_currentStepIndex > 0)
        {
            _currentStepIndex--;
        }
        return getCurrentStep();
    }

    //--- Get the current step
    public T getCurrentStep()
    {
        return _steps.get(_currentStepIndex);
    }

    //--- Add a new step at a specific index
    public void addStep(int index, T step)
    {
        _steps.add(index, step);
    }

    //--- Append a step to the end
    public void addStep(T step)
    {
        _steps.add(step);
    }

    //--- Remove a step by index
    public void removeStep(int index)
    {
        _steps.remove(index);
        if (_currentStepIndex >= _steps.size()) {
            _currentStepIndex = _steps.size() - 1; //--- Adjust index if out of bounds
        }
    }

    //--- Reset to the first step
    public void reset()
    {
        _currentStepIndex = 0;
    }

    //--- Get the current step index
    public int getCurrentStepIndex()
    {
        return _currentStepIndex;
    }
}
