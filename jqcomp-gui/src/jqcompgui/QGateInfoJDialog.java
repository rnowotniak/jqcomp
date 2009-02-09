/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * QGateInfoJDialog.java
 *
 * Created on 2008-12-24, 15:00:07
 */

package jqcompgui;

import java.awt.*;
import java.awt.event.*;
import pl.lodz.p.ics.quantum.jqcomp.*;
import javax.swing.*;
import org.jscience.mathematics.vector.ComplexMatrix;
import pl.lodz.p.ics.quantum.jqcomp.qgates.*;
import java.util.ArrayList;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Andrzej
 */
public class QGateInfoJDialog extends javax.swing.JDialog {
    
    public static interface QGateDisplayModel {
        boolean initializeDisplay(JScrollPane display, QGate gate);
        QGate update(QGate gate);
        String getName();
        String getDescription();
    }

    private class MatrixDisplayModel implements QGateDisplayModel {
        public boolean initializeDisplay(JScrollPane display, QGate gate) {
            this.gate = gate;
            pnlMatrix = new ComplexMatrixJPanel();
            pnlMatrix.setShowImaginary(isShowImaginary());
            display.setViewportView(pnlMatrix);
            pnlMatrix.setMatrix(gate.getMatrix());

            pnlMatrix.revalidate();
            return true; // always true
        }

        public QGate update(QGate gate) {
            ComplexMatrix newOne = pnlMatrix.getMatrix();
            if(!newOne.equals(gate.getMatrix())) {
                return new Custom(newOne);
            }

            return gate;
        }

        public String getName() {
            return gate.getClass().getSimpleName();
        }

        public String getDescription() {
            return gate.getClass().getSimpleName() + " quantum gate.";
        }

        protected QGate gate;
        protected ComplexMatrixJPanel pnlMatrix;
    }

    private static class PhaseShiftDisplayModel implements QGateDisplayModel {
        public boolean initializeDisplay(JScrollPane display, QGate gate) {
            if(gate instanceof PhaseShift) {
                this.gate = (PhaseShift)gate;
                pnlOptions = new PhaseShiftOptionsJPanel();
                display.setViewportView(pnlOptions);
                pnlOptions.setAngle(this.gate.getAngle());
                pnlOptions.revalidate();
                return true;
            }

            return false;
        }

        public QGate update(QGate gate) {
            double newAngle = pnlOptions.getAngle();
            if(newAngle != ((PhaseShift)gate).getAngle()) {
                return new PhaseShift(newAngle);
            }

            return gate;
        }

        public String getName() {
            return "Phase Shift";
        }

        public String getDescription() {
            return "Phase Shift quantum gate.";
        }

        protected PhaseShift gate;
        protected PhaseShiftOptionsJPanel pnlOptions;
    }

    private class CNotDisplayModel implements QGateDisplayModel {
        public boolean initializeDisplay(JScrollPane display, QGate gate) {
            if(gate instanceof CNot) {
                this.gate = (CNot)gate;
                pnlOptions = new CNotOptionsJPanel();
                pnlOptions.setMaxRow(maxRow);

                pnlOptions.setTargetRow(this.gate.getTarget());
                pnlOptions.setControlRow(this.gate.getControl());
                display.setViewportView(pnlOptions);
                pnlOptions.revalidate();
                return true;
            }

            return false;
        }

        public QGate update(QGate gate) {
            int target = pnlOptions.getTargetRow();
            if(target < 0) {
                return null;
            }

            int control = pnlOptions.getControlRow();
            if(control < 0) {
                return null;
            }

            if(control == target) {                
                return null;
            }

            if((control + getRow()) > maxRow || (target + getRow()) > maxRow) {
                return null;
            }

            return new CNot(control, target);
        }

        public String getName() {
            return "Controlled Not";
        }

        public String getDescription() {
            return "Controlled Not quantum gate.";
        }

        protected CNot gate;
        protected CNotOptionsJPanel pnlOptions;
    }

     private class HadamardDisplayModel implements QGateDisplayModel {

        public boolean initializeDisplay(JScrollPane display, QGate gate) {
            if(gate instanceof Hadamard) {
                this.gate = (Hadamard)gate;
                pnlOptions = new HadamardOptionPanel();
                pnlOptions.setGateSize(1);
                pnlOptions.setMaxSize(maxRow);
                display.setViewportView(pnlOptions);
                pnlOptions.revalidate();
                return true;
            }

            return false;
        }

        public QGate update(QGate gate) {
            int size = pnlOptions.getGateSize();
            if(size < 0) {
                return null;
            }

  //          if((control + getRow()) > maxRow || (target + getRow()) > maxRow) {
  //              return null;
   //         }
            return new Hadamard(size);
        }

        public String getName() {
            return "Hadamard";
        }

        public String getDescription() {
            return "Hadamard gate";
        }

        protected Hadamard gate;
        protected HadamardOptionPanel pnlOptions;
    }

    private class CustomDisplayModel implements QGateDisplayModel {
        public boolean initializeDisplay(JScrollPane display, QGate gate) {
            if(gate instanceof Custom) {
                this.gate = (Custom)gate;
                pnlOptions = new CustomOptionsJPanel();
                pnlOptions.setShowImaginary(isShowImaginary());
                pnlOptions.setMax(maxRow);
                pnlOptions.setMatrix(this.gate.getMatrix());
                display.setViewportView(pnlOptions);
                pnlOptions.revalidate();
                return true;
            }

            return false;
        }

        public QGate update(QGate gate) {
            if(gate.getMatrix().equals(pnlOptions.getMatrix())) {
                return gate;
            } else {
                return new Custom(pnlOptions.getMatrix());
            }
        }

        public String getName() {
            return "Custom";
        }

        public String getDescription() {
            return "Custom quantum gate.";
        }

        protected Custom gate;
        protected CustomOptionsJPanel pnlOptions;
    }

    public QGateInfoJDialog(Frame owner, boolean modal) {
        super(owner, modal);
        initComponents();

        // to chyba nie działa tak jak myślałem :)
        this.qubitJSpinner.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JSpinner i = (JSpinner)input;
                int value = (Integer)i.getValue();
                return value >= 0 && value <= maxRow;
            }
        });

        this.addDisplayModel(new PhaseShiftDisplayModel());
        this.addDisplayModel(new CNotDisplayModel());
        this.addDisplayModel(new CustomDisplayModel());
        this.addDisplayModel(new HadamardDisplayModel());
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                System.out.println("I'm visible!");                
            }
            
            @Override
            public void componentHidden(ComponentEvent e) {
                System.out.println("I'm hidden!");
            }
        });
    }

    public void display(QGate gate) {
        setGate(gate);
        setVisible(true);
    }

    private void updateGate() {
        QGate newOne = currentModel.update(gate);
        if(newOne != null && !gate.equals(newOne)) {
            changed = true;
            setGate(newOne);
            updateDisplay();
        }
    }

    private void setGateName(String name) {
        nameJTextField.setText(name);
    }

    private void setGateDescription(String description) {
        descriptionJTextArea.setText(description);
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public QGate getGate() {
        updateGate();
        return gate;
    }

    public void setGate(QGate gate) {
        if(gate == null)
            throw new NullPointerException("gate");
        
        if(this.gate != gate) {
            this.gate = gate;
            dialogResult = DIALOG_CANCELLED;
            setMaxRow(maxRow);
            updateDisplay();
        }
    }

    public int getRow() {
        int value = ((Integer)qubitJSpinner.getValue());
        if(value > maxRow) {
            value = maxRow;
            qubitJSpinner.setValue(value);
            // tu warto wyświetlić jakiś komunikat
        }

        return value;
    }

    /**
     * @return the maxRow
     */
    public int getMaxRow() {
        return maxRow;
    }

    /**
     * @param maxRow the maxRow to set
     */
    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
        if(gate != null) {
            ((SpinnerNumberModel)qubitJSpinner.getModel()).setMaximum(maxRow - gate.getSize());
        }
    }

    public boolean changed() {
        return changed;
    }

    public boolean isDisplaying() {
        return isVisible();
    }

    private JScrollPane getDisplay() {
        return jScrollPane2;
    }

     private void updateDisplay() {
        currentModel = null;
        for(QGateDisplayModel model : displayModels) {
            if(model.initializeDisplay(getDisplay(), gate)) {
                currentModel = model;
                break;
            }
        }

        if(currentModel == null) {
            defaultModel.initializeDisplay(getDisplay(), gate);
            currentModel = defaultModel;
        }

        setGateName(currentModel.getName());
        setGateDescription(currentModel.getDescription());
        setTitle("Quantum gate editor - " + currentModel.getName());
    }

    public void addDisplayModel(QGateDisplayModel model) {
        displayModels.add(model);
    }

    public void removeDisplayModel(QGateDisplayModel model) {
        displayModels.remove(model);
    }

    /**
     * @return the showImaginary
     */
    public boolean isShowImaginary() {
        return showImaginary;
    }

    /**
     * @param showImaginary the showImaginary to set
     */
    public void setShowImaginary(boolean showImaginary) {
        this.showImaginary = showImaginary;
    }

    public void setIcon(ImageIcon icon) {
        lblIcon.setIcon(icon);
    }

    private ArrayList<QGateDisplayModel> displayModels
            = new ArrayList<QGateDisplayModel>();

    private final QGateDisplayModel defaultModel = new MatrixDisplayModel();
    private QGateDisplayModel currentModel = defaultModel;

    public static final int DIALOG_CANCELLED = 0;
    public static final int DIALOG_ACCEPTED = 1;

    private QGate gate;
    private int dialogResult = DIALOG_CANCELLED;
    private boolean changed = false;
    private int maxRow = 0;
    private boolean showImaginary = true;
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameJTextField = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionJTextArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        displayJPanel = new jqcompgui.ComplexMatrixJPanel();
        addJButton = new javax.swing.JButton();
        qubitJSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        lblIcon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        nameJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameJTextFieldActionPerformed(evt);
            }
        });

        descriptionJTextArea.setColumns(20);
        descriptionJTextArea.setEditable(false);
        descriptionJTextArea.setRows(5);
        jScrollPane1.setViewportView(descriptionJTextArea);

        jLabel1.setText("Description:");

        javax.swing.GroupLayout displayJPanelLayout = new javax.swing.GroupLayout(displayJPanel);
        displayJPanel.setLayout(displayJPanelLayout);
        displayJPanelLayout.setHorizontalGroup(
            displayJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 551, Short.MAX_VALUE)
        );
        displayJPanelLayout.setVerticalGroup(
            displayJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 301, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(displayJPanel);

        addJButton.setText("Add to circuit");
        addJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJButtonActionPerformed(evt);
            }
        });

        qubitJSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));

        jLabel2.setText("Insertion qubit:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                        .addComponent(addJButton))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 265, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(qubitJSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addJButton)
                            .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(qubitJSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addComponent(lblIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJButtonActionPerformed
        dialogResult = DIALOG_ACCEPTED;
        setVisible(false);
        dispose();
}//GEN-LAST:event_addJButtonActionPerformed

    private void nameJTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameJTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameJTextFieldActionPerformed

    private boolean showYesNoDialog(String msg) {
        // ?
        return JOptionPane.showConfirmDialog(this, msg) == JOptionPane.YES_OPTION;
    }
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJButton;
    private javax.swing.JTextArea descriptionJTextArea;
    private jqcompgui.ComplexMatrixJPanel displayJPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JSpinner qubitJSpinner;
    // End of variables declaration//GEN-END:variables

}
