/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CustomOptionsJPanel.java
 *
 * Created on 2008-12-26, 16:54:42
 */

package jqcompgui;

import java.awt.*;
import javax.swing.*;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import pl.lodz.p.ics.quantum.jqcomp.WrongSizeException;

/**
 *
 * @author Andrzej
 */
public class CustomOptionsJPanel extends javax.swing.JPanel {

    /** Creates new form CustomOptionsJPanel */
    public CustomOptionsJPanel() {
        initComponents();
    }

    public void setMax(int i) {
        ((SpinnerNumberModel)numberSpinner.getModel()).setMaximum(i);
    }

    public int getMax() {
        return (Integer)((SpinnerNumberModel)numberSpinner.getModel()).getMaximum();
    }

    public ComplexMatrix getMatrix() {
        return pnlMatrix.getMatrix();
    }

    public void setMatrix(ComplexMatrix matrix) {
        //check(matrix);
        pnlMatrix.setMatrix(matrix);
    }

//    private void check(ComplexMatrix matrix) {
//        int columns = matrix.getNumberOfColumns();
//        int rows = matrix.getNumberOfRows();
//        if(columns != rows) {
//            throw new WrongSizeException("columns != rows");
//        }
//
//        if(columns > (getMax() * 2)) {
//            throw new WrongSizeException("columns > (getMax() * 2)");
//        }
//
//        if(columns % 2 != 0) {
//            throw new WrongSizeException("columns % 2 != 0");
//        }
//    }

    private void resizeMatrix(int size) {
        ComplexMatrix matrix = getMatrix();
        if(matrix == null) {
            return;
        }

        int curSize = matrix.getNumberOfColumns();
        if(curSize == size) {
            return;
        }

        Complex[][] marr = new Complex[size][size];
        for(int i = 0; i < size && i < curSize; i++) {
            for(int j = 0; j < size && j < curSize; j++) {
                marr[i][j] = matrix.get(i, j);
            }
        }

        if(size > curSize) {
            for(int i = curSize; i < size; i++) {
                for(int j = 0; j < size; j++) {
                    marr[i][j] = Complex.ZERO;
                }
            }

            for(int i = 0; i < size; i++) {
                for(int j = curSize; j < size; j++) {
                    marr[i][j] = Complex.ZERO;
                }
            }
        }
        
        setMatrix(ComplexMatrix.valueOf(marr));
    }

    public void setShowImaginary(boolean value) {
        pnlMatrix.setShowImaginary(value);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMatrix = new jqcompgui.ComplexMatrixJPanel();
        jLabel1 = new javax.swing.JLabel();
        numberSpinner = new javax.swing.JSpinner();

        javax.swing.GroupLayout pnlMatrixLayout = new javax.swing.GroupLayout(pnlMatrix);
        pnlMatrix.setLayout(pnlMatrixLayout);
        pnlMatrixLayout.setHorizontalGroup(
            pnlMatrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 219, Short.MAX_VALUE)
        );
        pnlMatrixLayout.setVerticalGroup(
            pnlMatrixLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );

        jLabel1.setText("Number ");

        numberSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));
        numberSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numberSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numberSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
            .addComponent(pnlMatrix, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(numberSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlMatrix, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void numberSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numberSpinnerStateChanged
        resizeMatrix((Integer)numberSpinner.getValue() * 2);
    }//GEN-LAST:event_numberSpinnerStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSpinner numberSpinner;
    private jqcompgui.ComplexMatrixJPanel pnlMatrix;
    // End of variables declaration//GEN-END:variables

}