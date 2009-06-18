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

import jqcompgui.events.Listener;
import jqcompgui.events.ExecutionMonitorException;
import jqcompgui.events.ExecutionMonitor;
import java.util.EventObject;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.jscience.mathematics.number.Complex;
import pl.lodz.p.ics.quantum.jqcomp.*;

/**
 *
 * @author Andrzej
 */
public class ExecutionInfoJDialog extends javax.swing.JDialog {

    private class ExecutionTableModel extends AbstractTableModel {
        public static final String COLUMN1_HEADER = "amplitude";

        private QRegister getTarget() {
            if (ExecutionInfoJDialog.this.monitor==null) return null;
            if (ExecutionInfoJDialog.this.monitor.isStepExecuting())
                return ExecutionInfoJDialog.this.monitor.getCurrentRegister();
            else if (ExecutionInfoJDialog.this.monitor.isInExecutedState()) {
                return ExecutionInfoJDialog.this.monitor.getResultRegister();
            }
            return null;
        }

        private ExecutionMonitor getMonitor() {
            return ExecutionInfoJDialog.this.monitor;
        }

        private int getRegisterSize(){
            QRegister target = getTarget();
            if (target!=null)
                return target.getSize();
            return 0;
        }

        public int getRowCount() {
            return MoreMath.pow2(getRegisterSize());

        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0)
                return QRegister.ket(rowIndex, getRegisterSize()).dirac();
            else {
                return (getTarget().toComplexArray())[rowIndex];
            }
                
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 1 && aValue instanceof Complex) {
                Complex[] array = getTarget().toComplexArray();
                array[rowIndex] = (Complex) aValue;
                getMonitor().modifyRegister(new QRegister(array), false);
                ExecutionInfoJDialog.this.modifiedTable = true;
            }
        }

        @Override
        public String getColumnName(int column) {
            if (column == 1) return COLUMN1_HEADER;
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if ( columnIndex==1) return true;
            return false;
        }
    }


    /** Creates new form ExecutionInfoJDialog */
    public ExecutionInfoJDialog(java.awt.Frame parent, ExecutionMonitor monitor) {
        super(parent, false);        
        initComponents();
        inputDecJTextField.setSizeAsText(false);
        inputDecJTextField.setNegativeEnabled(false);
        inputDecJTextField.setFloatingEnabled(false);

        setExecutionMonitor(monitor);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setExecutionMonitor(null);
            }
        });
        TableColumn col = this.amplitudesTable.getColumn(ExecutionTableModel.COLUMN1_HEADER);
        col.setCellEditor(new ComplexNumberCellEditor());
    }

    private void update() {
        stepChanged();
        updateInputRegister();
    }

    private void updateInputRegister() {
        QRegister reg = getInputRegister();
        if(reg != null) {
            monitor.setInputRegister(reg);
        }
    }

    private void stepChanged() {
        displayAll(monitor);
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
                this.monitor.circuitChangedEvent().remove(circuitChangedListener);
                this.monitor.stateChangedEvent().remove(stateChangedListener);
            }
            
            this.monitor = monitor;

            if(this.monitor != null) {
                setStateMessage(null);
   
                this.monitor.stepChangedEvent().add(stepChangedListener);
                this.monitor.circuitChangedEvent().add(circuitChangedListener);
                this.monitor.stateChangedEvent().add(stateChangedListener);
                update();
            }
        }
    }

    private Listener stepChangedListener = new Listener() {
        public void invoked(EventObject e) {
            stepChanged();
        }
    };

    private Listener circuitChangedListener = new Listener() {
        public void invoked(EventObject e) {
            updateInputRegister();
        }
    };

    private Listener stateChangedListener = new Listener() {
        public void invoked(EventObject e) {
            ExecutionMonitor m = (ExecutionMonitor)e.getSource();

            switch(m.getState()) {
                case Executed:
                    displayAll(m);
                    setStateMessage("executed.");
                    break;

                case StepExecution:
                    displayAll(m);
                    setStateMessage("step execution...");
                    break;

                case Initial:
                    displayAll(m);
                    setStateMessage("");
                    break;
            }
        }
    };

    private void setStateMessage(String state) {
        if(state != null && !state.equals("")) {
            setTitle(monitor.getQCircuitName() + " - " + state);
        } else {
            setTitle(monitor.getQCircuitName());
        }
    }

    private void displayAll(ExecutionMonitor m) {
        displayInput(m.getCurrentInputRegister());
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

    private void writeMsg(String msg) {
        MainJFrame.getInstance().writeMsg(msg, monitor.getQCircuitName());
    }

    private void redrawTable(){
         ((ExecutionTableModel)this.amplitudesTable.getModel()).fireTableDataChanged();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        amplitudesTable = new javax.swing.JTable();
        normalizeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        currentJTextField.setEditable(false);
        currentJTextField.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel1.setText("Current:");

        jLabel2.setText("Input:");

        inputJTextField.setEditable(false);
        inputJTextField.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Result:");

        resultJTextField.setEditable(false);

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

        amplitudesTable.setModel(new  ExecutionTableModel ());
        jScrollPane1.setViewportView(amplitudesTable);
        amplitudesTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        amplitudesTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        amplitudesTable.getColumnModel().getColumn(1).setWidth(80);

        normalizeButton.setText("Normalize");
        normalizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normalizeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
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
                        .addComponent(resetJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(normalizeButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(normalizeButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stepJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepJButtonActionPerformed
        try {
            if(monitor.isStepExecuting()) {
                monitor.nextStep();
            } else {
                updateInputRegister();
                monitor.startStepExecution();
            }
            redrawTable();
        } catch(ExecutionMonitorException e) {
            writeMsg(e.getMessage());
        }
    }//GEN-LAST:event_stepJButtonActionPerformed

    private void resetJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetJButtonActionPerformed
        monitor.reset();
    }//GEN-LAST:event_resetJButtonActionPerformed

    private void runJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runJButtonActionPerformed
        try {
            updateInputRegister();
            monitor.compute();
            redrawTable();
        } catch(ExecutionMonitorException e) {
            writeMsg(e.getMessage());
        }
    }//GEN-LAST:event_runJButtonActionPerformed

    private void normalize() {
        monitor.modifyRegister(null, true);
        redrawTable();
    }

    private void normalizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normalizeButtonActionPerformed
        normalize();
}//GEN-LAST:event_normalizeButtonActionPerformed

    /** 
     * Set if the user edits amplitudes 
     */
   private boolean modifiedTable = false;

    public boolean isModifiedByUser() {
        return modifiedTable;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable amplitudesTable;
    private javax.swing.JTextField currentJTextField;
    private jqcompgui.NumericJTextField inputDecJTextField;
    private javax.swing.JTextField inputJTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton normalizeButton;
    private javax.swing.JButton resetJButton;
    private javax.swing.JTextField resultJTextField;
    private javax.swing.JButton runJButton;
    private javax.swing.JButton stepJButton;
    // End of variables declaration//GEN-END:variables

}
