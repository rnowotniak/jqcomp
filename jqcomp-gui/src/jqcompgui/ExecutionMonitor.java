/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui;

import pl.lodz.p.ics.quantum.jqcomp.*;
import pl.lodz.p.ics.quantum.jqcomp.qgates.*;
import java.util.EventObject;

/**
 * 
 * @author Andrzej
 */
public class ExecutionMonitor {
    public ExecutionMonitor() {
        reset();
    }

    /**
     * resets the execution to the initial state
     */
    public synchronized void reset() {
        if(getState() != INITIAL_STATE) {
            inputRegister = null;
            currentRegister = null;
            resultRegister = null;
            setCurrentStep(0);
            setState(INITIAL_STATE);
        }
    }

    /**
     * computes specified quantum register instantly
     * @param reg the register to be computed
     * @return the result register
     */
    public synchronized QRegister compute(QRegister reg) {
        check(reg);
        // don't assume the QCircuit.compute copies passed register
        inputRegister = new QRegister(reg);
        currentRegister = reg; // will be equal to inputRegister all the time
        resultRegister = circuit.compute(reg);
        setState(EXECUTED_STATE);
        return getResultRegister();
    }

    /**
     * starts step execution using the specified quantum register,
     * to advance the execution call nextStep method
     * @param reg the register to be computed
     * @throws ExecutionMonitorException
     */
    public synchronized void startStepExecution(QRegister reg) {
        if(getState() != INITIAL_STATE) {
            throw new ExecutionMonitorException("ExcecutionMonitor needs to be in the INITLAL_STATE");
        }

        if(circuit.getStages().size() == 0) {
            resultRegister = inputRegister;
            return;
        }

        check(reg);

        inputRegister = new QRegister(reg);
        currentRegister = reg; // will be equal to inputRegister all the time
        resultRegister = null;
        setCurrentStep(0);
        setState(STEP_EXECUTION_STATE);
    }

    /**
     * advances the pending execution to the next step, usable only if the
     * execution monitor is in STEP_EXECUTION_STATE
     * @return true if execution is not finished; false otherwise
     */
    public synchronized boolean nextStep() {
        if(getState() != STEP_EXECUTION_STATE) {
            throw new ExecutionMonitorException("ExcecutionMonitor needs to be in the STEP_EXECUTION_STATE");
        }

        if(getCurrentStep() == circuit.getStages().size()) {
            return false;
        }

        Stage stage = circuit.getStages().get(getCurrentStep());
        currentRegister = stage.compute(currentRegister);

        if((getCurrentStep() + 1) == circuit.getStages().size()) {
            resultRegister = currentRegister;
            setCurrentStep(getCurrentStep() + 1);
            setState(EXECUTED_STATE);
            return false;
        }

        setCurrentStep(getCurrentStep() + 1);
        return true;
    }

    private void check(QRegister reg) {
        if(circuit.getStages().get(0).getSize() != reg.getSize()) {
            throw new ExecutionMonitorException(
                    "The provided quantum register and current circuit differ in size");
        }
    }

    /**
     * @return the state
     */
    public synchronized int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    private void setState(int state) {
        if(this.state != state) {            
            this.state = state;
            stateChangedInvoker.invoke(new EventObject(this));
        }
    }

    /**
     * @return the currentRegister
     */
    public synchronized QRegister getCurrentRegister() {
        return currentRegister;
    }

    /**
     * @return the inputRegister
     */
    public synchronized QRegister getInputRegister() {
        return inputRegister;
    }

    /**
     * @return the resultRegister
     */
    public synchronized QRegister getResultRegister() {
        return resultRegister;
    }

    /**
     * @return the currentStep
     */
    public synchronized int getCurrentStep() {
        return currentStep;
    }

    /**
     * @param currentStep the currentStep to set
     */
    private void setCurrentStep(int currentStep) {
        if(this.currentStep != currentStep) {
            this.currentStep = currentStep;
            stepChangedInvoker.invoke(new EventObject(this));
        }
    }    

    /**
     * @return the circuit
     */
    public synchronized QCircuit getQCircuit() {
        return circuit;
    }

    /**
     * method may call reset
     * @param circuit the circuit to set
     */
    public synchronized void setQCircuit(QCircuit circuit) {
        if(this.circuit != circuit) {
            reset();
            //QCircuit prev = this.circuit;
            this.circuit = circuit;
            circuitChangedInvoker.invoke(new EventObject(this));
        }
    }

    public synchronized Stage getCurrentStage() {
        if(state == STEP_EXECUTION_STATE) {
            return circuit.getStages().get(currentStep);
        } else if (state == EXECUTED_STATE) {
            return circuit.getStages().get(circuit.getStages().size() - 1);
        }

        return null;
    }

    public boolean isStepExecuting() {
        return getState() == STEP_EXECUTION_STATE;
    }

    public boolean isInInitialState() {
        return getState() == INITIAL_STATE;
    }

    public boolean isInExecutedState() {
        return getState() == EXECUTED_STATE;
    }

    private EventInvoker<EventObject> stepChangedInvoker
        = new EventInvoker<EventObject>();
    public Event<EventObject> stepChangedEvent() {
        return stepChangedInvoker.getEvent();
    }

    private EventInvoker<EventObject> circuitChangedInvoker
        = new EventInvoker<EventObject>();
    public Event<EventObject> circuitChangedEvent() {
        return circuitChangedInvoker.getEvent();
    }

    private EventInvoker<EventObject> stateChangedInvoker
        = new EventInvoker<EventObject>();
    public Event<EventObject> stateChangedEvent() {
        return stateChangedInvoker.getEvent();
    }

    private QCircuit circuit = null;

    private int state;
    private int currentStep;    
    private QRegister currentRegister;
    private QRegister inputRegister;
    private QRegister resultRegister;

    public static final int INITIAL_STATE = 0;
    public static final int EXECUTED_STATE = 1;
    public static final int STEP_EXECUTION_STATE = 2;
}
