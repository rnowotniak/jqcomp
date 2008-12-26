/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ComplexTextBox.java
 *
 * Created on 2008-12-25, 11:51:40
 */

package jqcompgui;

import java.awt.*;
import java.awt.event.*;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

/**
 *
 * @author Andrzej
 */
public class ComplexTextBox extends javax.swing.JPanel {

    /** Creates new form ComplexTextBox */
    public ComplexTextBox() {
        initComponents();
        setupControls();
    }

    private void setupControls() {
        removeAll();
      
        updateColors();
        setValue(Complex.valueOf(0, 0));
        
        ComponentListener cl = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                alignControls();
            }
        };

        getTxt1().addComponentListener(cl);
        getTxt2().addComponentListener(cl);

        add(getTxt1());
        add(getTxt2());
    }

    private void alignControls() {
        System.out.println("alignControls");
        final int offset = 2;

        getTxt1().setLocation(0, 0);
        Dimension pref1 = getTxt1().getPreferredSize();
        getTxt1().setSize(pref1.width + 1, pref1.height);

        getTxt2().setVisible(showImaginary);
        getTxt2().setLocation(getTxt1().getX() + getTxt1().getWidth() + offset, 0);
        Dimension pref2 = getTxt2().getPreferredSize();
        getTxt2().setSize(pref2.width + 1, pref2.height);
       
        Dimension d = new Dimension(
                getTxt2().getX() + getTxt2().getWidth(),
                getTxt2().getHeight());

        this.setPreferredSize(d);
        this.setSize(d);
    }

    private void updateColors() {
        getTxt1().setBackground(this.getBackground());
        getTxt1().setForeground(this.getForeground());

        getTxt2().setBackground(this.getBackground());
        getTxt2().setForeground(this.getForeground());
    }

    public double getReal() {
        return getTxt1().getValue(0.0d);
    }

    public void setReal(double real) {
        getTxt1().setValue(real);
        alignControls();
    }

    public double getImaginary() {
        return getTxt2().getValue(0.0d);
    }

    public void setImaginary(double imaginary) {
        getTxt2().setValue(imaginary);
        alignControls();
    }

    public Complex getValue() {
        return Complex.valueOf(getReal(), getImaginary());
    }

    public void setValue(Complex complex) {
        getTxt1().setValue(complex.getReal());
        getTxt2().setValue(complex.getImaginary());
        alignControls();
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        updateColors();
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        updateColors();
    }

     /**
     * @return the txt1
     */
    protected NumericTextBox getTxt1() {
        if(txt1 == null) {
            txt1 = new NumericTextBox();
        }

        return txt1;
    }

    /**
     * @return the txt2
     */
    protected NumericTextBox getTxt2() {
        if(txt2 == null) {
            txt2 = new NumericTextBox();
        }

        return txt2;
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
            alignControls();
        }
    }

    private NumericTextBox txt1 = null;
    private NumericTextBox txt2 = null;
    private boolean showImaginary = true;

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
            .addGap(0, 183, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    
 


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
