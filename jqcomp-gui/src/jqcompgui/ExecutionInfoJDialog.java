/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ExecutionInfoJDialog.java
 *
 * Created on 2008-12-28, 16:02:23
 */

package jqcompgui;

import java.util.EventObject;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;

import pl.lodz.p.ics.quantum.jqcomp.*;
import pl.lodz.p.ics.quantum.jqcomp.qgates.*;

/**
 *
 * @author Andrzej
 */
public class ExecutionInfoJDialog extends javax.swing.JDialog {

    /** Creates new form ExecutionInfoJDialog */
    public ExecutionInfoJDialog(java.awt.Frame parent, ExecutionMonitor monitor) {
        super(parent, false);        
        initComponents();

        inputDecJTextField.setSizeAsText(false);

        setExecutionMonitor(monitor);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                setExecutionMonitor(null);
            }
        });
    }

    /**
     * @return the monitor
     */
    public ExecutionMonitor getExecutionMonitor() {
        return monitor;
    }

    /**
     * @param monitor the monitor to set
     */
    public void setExecutionMonitor(ExecutionMonitor monitor) {
        if(this.monitor != monitor) {
            if(this.monitor != null) {
                this.monitor.stepChangedEvent().remove(stepChangedListener);
            }
            
            this.monitor = monitor;

            if(monitor != null) {
                monitor.stepChangedEvent().add(stepChangedListener);
                update();
            }
        }
    }

    private Listener<EventObject> stepChangedListener
            = new Listener<EventObject>() {
        public void invoked(EventObject e) {
            stepChanged();
        }
    };

    private Listener<EventObject> stateChangedListener
            = new Listener<EventObject>() {
        public void invoked(EventObject e) {
            ExecutionMonitor m = (ExecutionMonitor)e.getSource();
            switch(m.getState()) {
                case ExecutionMonitor.EXECUTED_STATE:
                    displayAll(m);
                    setStateMessage("");
                    break;

                case ExecutionMonitor.STEP_EXECUTION_STATE:
                    displayAll(m);
                    break;

                case ExecutionMonitor.INITIAL_STATE:
                    displayAll(m);
                    break;
            }
        }
    };

    private void setStateMessage(String state) {

    }

    private void update() {
        stepChanged();
    }

    private void stepChanged() {
        displayAll(monitor);
    }

    private void displayAll(ExecutionMonitor m) {
        displayInput(m.getInputRegister());
        displayCurrent(m.getCurrentRegister());
        displayResult(m.getResultRegister());
    }

    private void displayInput(QRegister r) {
        displayRegister(inputJTextField, r);
    }

    private void displayCurrent(QRegister r) {
        displayRegister(currentJTextField, r);
    }

    private void displayResult(QRegister r) {
        displayRegister(resultJTextField, r);
    }

    private void displayRegister(JTextField f, QRegister r) {
        String text = "unavailable";
        if(r != null) {
            text = r.dirac();
        } 

        f.setText(text);
    }

    private QRegister getInputRegister() {
        Object val = inputDecJTextField.getValue();
        if(val == null) {
            return null;
        }

        if(monitor.getQCircuit().getStages().size() < 1) {
            return null;
        }

        int qubits = monitor.getQCircuit().getStages().get(0).getSize();
        double dval = (Double)val;

        return QRegister.ket((int)dval, qubits);
    }

    private ExecutionMonitor monitor;

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        currentJTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        inputJTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        resultJTextField = new javax.swing.JTextField();
        stepJButton = new javax.swing.JButton();
        resetJButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        runJButton = new javax.swing.JButton();
        inputDecJTextField = new jqcompgui.NumericJTextField();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Current:");

        jLabel2.setText("Input:");

        jLabel3.setText("Result:");

        stepJButton.setText("Next Step");
        stepJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepJButtonActionPerformed(evt);
            }
        });

        resetJButton.setText("Reset");
        resetJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetJButtonActionPerformed(evt);
            }
        });

        runJButton.setText("Run");
        runJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runJButtonActionPerformed(evt);
            }
        });

        inputDecJTextField.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Input (decimal): ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(currentJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(resultJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                    .addComponent(inputJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                    .addComponent(inputDecJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(runJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stepJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputDecJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resetJButton)
                    .addComponent(stepJButton)
                    .addComponent(runJButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stepJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepJButtonActionPerformed
        if(monitor.isInExecutedState()) {
            monitor.reset();
        }

        if(monitor.isInInitialState()) {
            QRegister reg = getInputRegister();
            if(reg == null) {
                return;
            }

            monitor.startStepExecution(reg);
        }

        monitor.nextStep();
    }//GEN-LAST:event_stepJButtonActionPerformed

    private void resetJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetJButtonActionPerformed
        monitor.reset();
    }//GEN-LAST:event_resetJButtonActionPerformed

    private void runJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runJButtonActionPerformed
        QRegister reg = getInputRegister();
        if(reg != null) {
            monitor.compute(reg);
        }
    }//GEN-LAST:event_runJButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField currentJTextField;
    private jqcompgui.NumericJTextField inputDecJTextField;
    private javax.swing.JTextField inputJTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton resetJButton;
    private javax.swing.JTextField resultJTextField;
    private javax.swing.JButton runJButton;
    private javax.swing.JButton stepJButton;
    // End of variables declaration//GEN-END:variables

}
