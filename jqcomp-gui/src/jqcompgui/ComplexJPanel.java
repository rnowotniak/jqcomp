/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ComplexJPanel.java
 *
 * Created on 2008-12-25, 11:51:40
 */

package jqcompgui;

import java.awt.*;
import java.awt.event.*;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import javax.swing.*;

/**
 *
 * @author Andrzej
 */
public class ComplexJPanel extends javax.swing.JPanel {

    /** Creates new form ComplexJPanel */
    public ComplexJPanel() {
        initComponents();
        setupControls();
    }

    @Override
    public void paintComponent(Graphics g) {
        if(showImaginary) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            JTextField t1 = getTxt1();
            JTextField t2 = getTxt2();

            FontMetrics metrics = g.getFontMetrics();

            int width = metrics.charWidth('+');
            int offset = t1.getX() + t1.getWidth();
            int x = offset + betweenOffset[1] / 2 - width / 2;
            int y = (topOffset[1] + t1.getHeight() + bottomOffset[1]) / 2 + 4;
            g.setColor(getForeground());
            g.drawString("+", x, y);

            width = metrics.charWidth('i');
            offset = t2.getX() + t2.getWidth();
            x = offset;
            g.drawString("i", x, y);

        } else {
            // does nothing
        }
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
        getTxt1().setBorder(BorderFactory.createEmptyBorder());

        getTxt2().addComponentListener(cl);
        getTxt2().setBorder(BorderFactory.createEmptyBorder());

        add(getTxt1());
        add(getTxt2());
    }

    private void alignControls() {
        // offset index
        int oi = showImaginary ? 1 : 0;

        JTextField t1 = getTxt1();
        JTextField t2 = getTxt2();

        t1.setLocation(leftOffset[oi], topOffset[oi]);
        Dimension pref1 = t1.getPreferredSize();
        t1.setSize(pref1.width + 1, pref1.height);

        t2.setVisible(showImaginary);
        t2.setLocation(t1.getX() + t1.getWidth()
                + betweenOffset[oi], topOffset[oi]);
        Dimension pref2 = t2.getPreferredSize();
        t2.setSize(pref2.width + 1, pref2.height);
       
        Dimension d;
        if(showImaginary) {
            d = new Dimension(
                t2.getX() + t2.getWidth() + rightOffset[oi],
                t2.getY() + t2.getHeight() + bottomOffset[oi]);
        } else {
            d = new Dimension(
                t1.getX() + t1.getWidth() + rightOffset[oi],
                t2.getY() + t1.getHeight() + bottomOffset[oi]);
        }

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

//    @Override
//    public void setFont(Font font) {
//
//    }



     /**
     * @return the txt1
     */
    protected NumericJTextField getTxt1() {
        if(txt1 == null) {
            txt1 = new NumericJTextField();
        }

        return txt1;
    }

    /**
     * @return the txt2
     */
    protected NumericJTextField getTxt2() {
        if(txt2 == null) {
            txt2 = new NumericJTextField();
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

    private NumericJTextField txt1 = null;
    private NumericJTextField txt2 = null;
    private boolean showImaginary = true;

    // {hideImaginary, showImaginary}
    private static final int[] leftOffset = new int[] {0, 0};
    private static final int[] betweenOffset = new int[] {0, 10};
    private static final int[] rightOffset = new int[] {0, 5};
    private static final int[] topOffset = new int[] {0, 0};
    private static final int[] bottomOffset = new int[] {0, 0};
    

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
