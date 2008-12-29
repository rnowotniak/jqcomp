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
     * Resets the execution to the initial state
     */
    public synchronized void reset() {
        if(getState() != INITIAL_STATE) {
            // don't reset inputRegister
            currentInputRegister = null;
            currentRegister = null;
            resultRegister = null;
            setCurrentStep(0);
            setState(INITIAL_STATE);
        }
    }

    /**
     * Computes specified quantum register instantly
     * @param reg the register to be computed
     * @return the result register
     * @throws ExecutionMonitorException
     */
    public synchronized QRegister compute(QRegister reg) {
        check(reg);
        reset();
        // don't assume the QCircuit.compute copies passed register
        currentInputRegister = new QRegister(reg);
        inputRegister = currentInputRegister;
        currentRegister = null; // not used
        resultRegister = circuit.compute(reg);
        setState(EXECUTED_STATE);
        return getResultRegister();
    }

    /**
     * Computes inputRegister instantly
     * @return the result register
     * @throws ExecutionMonitorException
     */
    public synchronized QRegister compute() {
        return compute(inputRegister);
    }

    /**
     * Starts step execution using the specified quantum register.
     * To advance the execution call nextStep method.
     * @param reg the register to be computed
     * @throws ExecutionMonitorException
     */
    public synchronized void startStepExecution(QRegister reg) {
        if(circuit.getStages().size() == 0) {
            resultRegister = currentInputRegister;
            return;
        }

        check(reg);
        reset();

        currentInputRegister = new QRegister(reg);
        currentRegister = reg; // will be equal to currentInputRegister all the time
        resultRegister = null;
        setCurrentStep(0);
        setState(STEP_EXECUTION_STATE);
    }

    /**
     * Starts step execution using the inputRegister.
     * To advance the execution call nextStep method.
     * @throws ExecutionMonitorException
     */
    public synchronized void startStepExecution() {
        startStepExecution(inputRegister);
    }

    /**
     * Advances the pending execution to the next step, usable only if the
     * execution monitor is in STEP_EXECUTION_STATE
     * @return true if execution is not finished; false otherwise
     * @throws ExecutionMonitorException
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

    /**
     * Same as nextStep, but calls startStepExecution when necessary.
     * Works only if inputRegister is set.
     * @throws ExecutionMonitorException
     */
    public synchronized void cyclicNextStep() {
        if(isStepExecuting()) {
            nextStep();
        } else {
            startStepExecution();
        }
    }

    private void check(QRegister reg) {
        if(reg == null) {
            throw new ExecutionMonitorException(
                    "There is no input register to compute");
        }

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
     * The register that has been used to compute outputRegister and/or currentRegister
     * Note: currentInputRegister may differ from inputRegister
     * @return the currentInputRegister
     */
    public synchronized QRegister getCurrentInputRegister() {
        return currentInputRegister;
    }

    /**
     * The register that will be used as currentInputRegister in next
     * call to compute() or startStepExecution().
     * This register, unlike currentInputRegister, is preserved during call to reset.
     * Note: currentInputRegister may differ from inputRegister
     * @return the inputRegister
     */
    public synchronized QRegister getInputRegister() {
        return inputRegister;
    }

    /**
     * The register that will be used as currentInputRegister in next
     * call to compute() or startStepExecution().
     * This register, unlike currentInputRegister, is preserved during call to reset.
     * Note: currentInputRegister may differ from inputRegister
     * @param inputRegister the register to set
     */
    public synchronized void setInputRegister(QRegister inputRegister) {
        this.inputRegister = inputRegister;
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
     * 
     * @return current quantum circuit name
     */
    public synchronized String getQCircuitName() {
        return name;
    }

    /**
     * method may call reset
     * @param circuit the circuit to set
     * @param name the circuit name
     */
    public synchronized void setQCircuit(QCircuit circuit, String name) {
        if(this.circuit != circuit) {
            reset();
            //QCircuit prev = this.circuit;
            this.circuit = circuit;
            if(name == null) {
                this.name = "";
            } else {
                this.name = name;
            }

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
    private String name = "";

    private int state;
    private int currentStep;    
    private QRegister currentRegister;
    private QRegister currentInputRegister;
    private QRegister resultRegister;
    private QRegister inputRegister;

    public static final int INITIAL_STATE = 0;
    public static final int EXECUTED_STATE = 1;
    public static final int STEP_EXECUTION_STATE = 2;
}
