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

    /** Creates new form ComplexJPanel
     *  Complex numbers editor
     */
    public ComplexJPanel() {
        initComponents();
        setupControls();
    }

    @Override
    public void paintComponent(Graphics g) {
        if(showImaginary) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            JTextField t1 = getTxtRe();
            JTextField t2 = getTxtIm();

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

        getTxtRe().addComponentListener(cl);
        getTxtRe().setBorder(BorderFactory.createEmptyBorder());

        getTxtIm().addComponentListener(cl);
        getTxtIm().setBorder(BorderFactory.createEmptyBorder());

        add(getTxtRe());
        add(getTxtIm());
    }

    private void alignControls() {
        // offset index
        int oi = showImaginary ? 1 : 0;

        JTextField re = getTxtRe();
        JTextField im = getTxtIm();

        re.setLocation(leftOffset[oi], topOffset[oi]);
        Dimension pref1 = re.getPreferredSize();
        re.setSize(pref1.width + 1, pref1.height);

        im.setVisible(showImaginary);
        im.setLocation(re.getX() + re.getWidth()
                + betweenOffset[oi], topOffset[oi]);
        Dimension pref2 = im.getPreferredSize();
        im.setSize(pref2.width + 1, pref2.height);
       
        Dimension d;
        if(showImaginary) {
            d = new Dimension(
                im.getX() + im.getWidth() + rightOffset[oi],
                im.getY() + im.getHeight() + bottomOffset[oi]);
        } else {
            d = new Dimension(
                re.getX() + re.getWidth() + rightOffset[oi],
                im.getY() + re.getHeight() + bottomOffset[oi]);
        }

        this.setPreferredSize(d);
        this.setSize(d);
    }

    private void updateColors() {
        getTxtRe().setBackground(this.getBackground());
        getTxtRe().setForeground(this.getForeground());

        getTxtIm().setBackground(this.getBackground());
        getTxtIm().setForeground(this.getForeground());
    }

    public double getReal() {
        return getTxtRe().getValue(0.0d);
    }

    public void setReal(double real) {
        getTxtRe().setValue(real);
        alignControls();
    }

    public double getImaginary() {
        return getTxtIm().getValue(0.0d);
    }

    public void setImaginary(double imaginary) {
        getTxtIm().setValue(imaginary);
        alignControls();
    }

    public Complex getValue() {
        return Complex.valueOf(getReal(), getImaginary());
    }

    public void setValue(Complex complex) {
        getTxtRe().setValue(complex.getReal());
        getTxtIm().setValue(complex.getImaginary());
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

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        getTxtRe().setFont(font);
        getTxtIm().setFont(font);
    }

     /**
     * @return the txtRe
     */
    protected NumericJTextField getTxtRe() {
        if(txtRe == null) {
            txtRe = new NumericJTextField();
        }

        return txtRe;
    }

    /**
     * @return the txtIm
     */
    protected NumericJTextField getTxtIm() {
        if(txtIm == null) {
            txtIm = new NumericJTextField();
        }

        return txtIm;
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
   
   
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getTxtRe().setEnabled(enabled);
        getTxtIm().setEnabled(enabled);
    }

    private NumericJTextField txtRe = null;
    private NumericJTextField txtIm = null;
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
