/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MeasurementOptionsDialog.java
 *
 * Created on 2009-02-11, 11:57:55
 */

package jqcompgui;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import pl.lodz.p.ics.quantum.jqcomp.Measurement;
import pl.lodz.p.ics.quantum.jqcomp.Stage;

/**
 *
 * @author Rafal
 */
public class MeasurementOptionsDialog extends javax.swing.JDialog {
    private JCheckBox[] checkBox;
    private final int size;
    public final static int DIALOG_ACCEPTED = 0;
    public final static int DIALOG_CANCELLED = 1;
    private int dialogResult = DIALOG_CANCELLED;
    
    /** Creates new form MeasurementOptionsDialog */
    public MeasurementOptionsDialog(java.awt.Frame parent, boolean modal, int qubits) {

        super(parent, modal);
        initComponents();
        checkBox = new JCheckBox[qubits];
        panel.setLayout(new GridLayout(0,4));
   //     FlowLayout fl = new FlowLayout(FlowLayout.TRAILING, 70,10);
   //     panel.setLayout(fl);
        for (int i=0;i<qubits;i++)
        {
            // create initially selected checkbox for each qubit
            String boxString = "|q"+i+">     ";
            checkBox[i] = new JCheckBox(boxString,true);
            panel.add(checkBox[i]);
        }
        this.pack();
        this.size = qubits;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectButton = new javax.swing.JButton();
        deselectButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        panel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        selectButton.setText("Select all");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        deselectButton.setText("Deselect all");
        deselectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deselectButtonActionPerformed(evt);
            }
        });

        addButton.setText("Add to circuit");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 311, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(selectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deselectButton)
                        .addGap(48, 48, 48)
                        .addComponent(addButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectButton)
                    .addComponent(deselectButton)
                    .addComponent(addButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (this.getSelected()==null) {
            // nothing selected
            JOptionPane.showMessageDialog(this, "Select at least one qubit.", "Error", 0, null);
            return;
        } else {
            dialogResult = DIALOG_ACCEPTED;
            setVisible(false);
            dispose();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
      for (JCheckBox box: checkBox) {
           box.setSelected(true);
       }
    }//GEN-LAST:event_selectButtonActionPerformed

    private void deselectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deselectButtonActionPerformed
        for (JCheckBox box: checkBox) {
           box.setSelected(false);
       }
    }//GEN-LAST:event_deselectButtonActionPerformed

    public int getResult() {
        return dialogResult;
    }

    public Stage getStage() {
        return new Measurement(size, getSelected());
    }

    public int[] getSelected() {
        ArrayList<Integer> sel = new ArrayList<Integer>();
        for (int i=0;i<size;i++) {
            if (checkBox[i].isSelected()) sel.add(i);
        }
        if (sel.size()==0) return null;
        int[] ret = new int[sel.size()];
        for (int i=0;i<ret.length;i++) {
            ret[i]=sel.get(i);
        }
        return ret;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MeasurementOptionsDialog dialog = new MeasurementOptionsDialog(new javax.swing.JFrame(), true, 8);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton deselectButton;
    private javax.swing.JPanel panel;
    private javax.swing.JButton selectButton;
    // End of variables declaration//GEN-END:variables

}
