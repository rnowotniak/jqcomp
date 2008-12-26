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
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import pl.lodz.p.ics.quantum.jqcomp.qgates.*;
import java.util.ArrayList;

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

    private static class MatrixDisplayModel implements QGateDisplayModel {
        public boolean initializeDisplay(JScrollPane display, QGate gate) {
            this.gate = gate;
            pnlMatrix = new ComplexMatrixJPanel();
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

    public QGateInfoJDialog(Frame owner, boolean modal) {
        super(owner, modal);
        initComponents();

        // to chyba nie działa tak jak myślałem :)
        this.rowSpinner.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JSpinner i = (JSpinner)input;
                int value = (Integer)i.getValue();
                return value >= 0 && value <= maxRow;
            }
        });

        this.addDisplayModel(new PhaseShiftDisplayModel());
        
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
        if(!gate.equals(newOne)) {
            changed = true;
            setGate(newOne);
            updateDisplay();
        }
    }

    private void setGateName(String name) {
        txtName.setText(name);
    }

    private void setGateDescription(String description) {
        txtDescription.setText(description);
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
            updateDisplay();
        }
    }

    public int getRow() {
        int value = ((Integer)rowSpinner.getValue());
        if(value > maxRow) {
            value = maxRow;
            rowSpinner.setValue(value);
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
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlImage = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnlMatrix = new jqcompgui.ComplexMatrixJPanel();
        btnAdd = new javax.swing.JButton();
        rowSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout pnlImageLayout = new javax.swing.GroupLayout(pnlImage);
        pnlImage.setLayout(pnlImageLayout);
        pnlImageLayout.setHorizontalGroup(
            pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );
        pnlImageLayout.setVerticalGroup(
            pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        txtDescription.setColumns(20);
        txtDescription.setEditable(false);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        jLabel1.setText("Description:");

        javax.swing.GroupLayout pnlMatrixLayout = new javax.swing.GroupLayout(pnlMatrix);
        pnlMatrix.setLayout(pnlMatrixLayout);
        pnlMatrixLayout.setHorizontalGroup(
            pnlMatrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 551, Short.MAX_VALUE)
        );
        pnlMatrixLayout.setVerticalGroup(
            pnlMatrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 301, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(pnlMatrix);

        btnAdd.setText("Add to circuit");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jLabel2.setText("Row:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(pnlImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                                .addComponent(btnAdd))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdd))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addComponent(pnlImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        dialogResult = DIALOG_ACCEPTED;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnAddActionPerformed

    private boolean showYesNoDialog(String msg) {
        // ?
        return JOptionPane.showConfirmDialog(this, msg) == JOptionPane.YES_OPTION;
    }
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel pnlImage;
    private jqcompgui.ComplexMatrixJPanel pnlMatrix;
    private javax.swing.JSpinner rowSpinner;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables

}
