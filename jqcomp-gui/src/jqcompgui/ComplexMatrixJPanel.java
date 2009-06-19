/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MatrixJPanel.java
 *
 * Created on 2008-12-24, 15:12:28
 */

package jqcompgui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

/**
 *
 * @author Andrzej
 */
public class ComplexMatrixJPanel extends javax.swing.JPanel {

    /** Creates new form MatrixJPanel */
    public ComplexMatrixJPanel() {
        initComponents();
    }

//    private void updateFields()
//    {
//        ComplexMatrix m = getMatrix();
//        if(m == null) {
//            return;
//        }
//
//        ComponentListener cl = new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                System.out.println("component resized");
//                alignFields();
//            }
//        };
//
//        final int rows = m.getNumberOfRows();
//        final int columns = m.getNumberOfColumns();
//
//        ntbs = new ComplexJPanel[columns][rows];
//
//        int x = getXOffset();
//        int y = getYOffset();
//
//        //Matrix m = context.getPolynomial();
//
//        JComponent pane = getPane();
//
//        Color bgColor = getBackground();
//        Color fgColor = getForeground();
//        Font font = getFont();
//
//        int maxWidth = 0;
//
//        pane.removeAll();
//        for(int i = 0; i < columns; i++)
//        {
//            y = getYOffset();
//
//            for(int j = 0; j < rows; j++)
//            {
//                ComplexJPanel ntb = new ComplexJPanel();
//                ntb.setFont(font);
//                ntb.setBackground(bgColor);
//                ntb.setForeground(fgColor);
//                ntb.setValue(m.get(i, j));
//                ntb.setShowImaginary(isShowImaginary());
//                ntb.addComponentListener(cl);
//                setNTB(ntb, i, j);
//                getPane().add(ntb);
//
//                if(ntb.getWidth() > maxWidth) {
//                    maxWidth = ntb.getWidth() + 2;
//                }
//
//                y += ntb.getHeight() + getYOffset();
//            }
//
//            x += maxWidth + getXOffset();
//        }
//
//        setPreferredSize(new Dimension(x, y));
//        revalidate();
//        repaint();
//    }

    private void updateFields() {
        ComplexMatrix m = getMatrix();
        if(m == null) {
            return;
        }

        ComponentListener cl = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("component resized");
                alignFields(true);
            }
        };

        final int rows = m.getNumberOfRows();
        final int columns = m.getNumberOfColumns();

        ntbs = new ComplexJPanel[columns][rows];

        int x = getXOffset();
        int y = getYOffset();

        Color bgColor = getBackground();
        Color fgColor = getForeground();
        Font font = getFont();
        
        for(int i = 0; i < columns; i++) {
            for(int j = 0; j < rows; j++) {
                ComplexJPanel ntb = new ComplexJPanel();
                ntb.setFont(font);
                ntb.setBackground(bgColor);
                ntb.setForeground(fgColor);
                ntb.setValue(m.get(i, j));
                ntb.setShowImaginary(isShowImaginary());
                ntb.addComponentListener(cl);
                setNTB(ntb, i, j);
            }
        }
        
        alignFields(false);

        JComponent pane = getPane();
        pane.removeAll();
        for(int i = 0; i < columns; i++) {
            for(int j = 0; j < rows; j++) {
                pane.add(getNTB(i, j));
            }            
        }
        
        setPreferredSize(new Dimension(x, y));
        revalidate();
        repaint();
    }

     private void alignFields(boolean invalidate) {
        ComplexMatrix m = getMatrix();
        final int rows = m.getNumberOfRows();
        final int columns = m.getNumberOfColumns();

        if(columnWidths.length != columns) {
            columnWidths = new int[columns];
            for(int i = 0; i < columnWidths.length; i++) {
                columnWidths[i] = 0;
            }

            computeColumnsWidths(columnWidths);
        }

        int x = xOffset;
        int y = yOffset;

        for(int i = 0; i < columns; i++) {
            y = yOffset;

            for(int j = 0; j < rows; j++) {
                ComplexJPanel ntb = getNTB(i, j);
                ntb.setLocation(x, y);
                y += ntb.getHeight() + getYOffset();
            }

            x += columnWidths[i] + xOffset;
        }

        if(invalidate) {
            setPreferredSize(new Dimension(x, y));
            revalidate();
            repaint();
        }
    }

    private int getColumnMaxWidth(int column) {
        final int size = getMatrix().getNumberOfRows();
        int max = 0;
        for(int i = 0; i < size; i++) {
            int cur = getNTB(column, i).getWidth();
            if(cur > max) {
                max = cur;
            }
        }

        return max;
    }

    private boolean computeColumnsWidths(int[] widths) {
        boolean modified = false;
        for(int i = 0; i < widths.length; i++) {
            int newWidth = getColumnMaxWidth(i);
            if(newWidth != widths[i]) {
                widths[i] = newWidth;
                modified = true;
            }
        }

        return modified;
    }


    private ComplexMatrix getMatrixFromFields() {
        Complex[][] marr = new Complex[ntbs.length][ntbs[0].length];
        
        for(int i = 0; i < marr.length; i++) {
            for(int j = 0; j < marr[0].length; j++) {
                marr[i][j] = getNTB(i, j).getValue();
            }
        }

        return ComplexMatrix.valueOf(marr);
    }

    private void setNTB(ComplexJPanel ntb, int i, int j) {
        ntbs[i][j] = ntb;        
    }

    private ComplexJPanel getNTB(int i, int j){
        return ntbs[i][j];
    }

    // nie wiem jak tutaj można jakoś wygodnie przekazywać zdażenia...
    private void inputMatrixChanged() {
        changedOne = true;
        changed = true;
    }

    private JComponent getPane() {
        return this;//.jScrollPane1;
    }
    
     /**
     * @return the matrix
     */
    public ComplexMatrix getMatrix() {
        if(changedOne) {
            matrix = getMatrixFromFields();
            changedOne = false;
        }

        // ugly but working:
        //if (ntbs.length>0) matrix = getMatrixFromFields();

        return matrix;
    }

    /**
     * @param matrix the matrix to set
     */
    public void setMatrix(ComplexMatrix matrix) {
        this.matrix = matrix;
        changed = false;
        changedOne = false;
        updateFields();
    }

    public boolean matrixChanged() {
        return changed;
    }

     /**
     * @return the editEnabled
     */
    public boolean isEditEnabled() {
        return editEnabled;
    }

    /**
     * @param editEnabled the editEnabled to set
     */
    public void setEditEnabled(boolean editEnabled) {
        this.editEnabled = editEnabled;
    }

     /**
     * @return the xOffset
     */
    public int getXOffset() {
        return xOffset;
    }

    /**
     * @param xOffset the xOffset to set
     */
    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * @return the yOffset
     */
    public int getYOffset() {
        return yOffset;
    }

    /**
     * @param yOffset the yOffset to set
     */
    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
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
        if(this.showImaginary != showImaginary) {
            this.showImaginary = showImaginary;
            this.updateFields();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for(int i = 0; i < ntbs.length; i++) {
            for(int j = 0; j < ntbs[0].length; j++) {
                ntbs[i][j].setEnabled(enabled);
            }
        }
    }

    private int[] columnWidths = new int[0];
    private ComplexJPanel[][] ntbs = new ComplexJPanel[0][0];

    private ComplexMatrix matrix = null;
    private boolean changedOne = false;
    private boolean changed = false;
    private boolean editEnabled = true;
    private boolean showImaginary = true;

    private int xOffset = 4;
    private int yOffset = 2;
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 109, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
