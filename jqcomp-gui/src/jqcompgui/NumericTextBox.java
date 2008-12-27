/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.event.*;

/**
 *
 * @author Andrzej
 */
public class NumericTextBox extends JTextField
{
    public NumericTextBox()
    {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(!passChar(e.getKeyChar())) {
                    e.consume();
                }               
            }
        });

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                getValue();
            }
        });

        this.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }
        });

        textChanged();
    }

    public boolean passChar(char c)
    {       
        if(Character.isDigit(c)) {
            return true;
        }

        if(c == '.') {
            String text = getText();
            if(text.length() != 0 && !text.contains(".")) {
                return true;
            }
        }

        if(c == '-' || c == '+') {
            String text = getText();
            if(text.length() == 0
               || Character.toLowerCase(text.charAt(text.length() - 1)) == 'e') {
                return true;
            }
        }

        if(c == 'e' || c == 'E') {
            String text = getText().toLowerCase();
            if(!text.contains("e")) {
                return true;
            }
        }

        return false;
    }

    private void textChanged() {
        if(isSizeAsText()) {
            Dimension d = getPreferredSize();
            if(d.width < 50) { // inaczej nie widać kursora
                d = new Dimension(50, d.height);
            }

            System.out.println("aligning to text");
            final Dimension df = d;
//            EventQueue.invokeLater(new Runnable() {
//                public void run() {
                    setSize(df);
//                }
//            });
        }
    }

    public double getValue()
    {
        try
        {
            return Double.parseDouble(getText().trim());
        }
        catch(Exception e)
        {
            setText(Double.toString(def));
            return def;
        }
    }

    public double getValue(double def)
    {
        try
        {
            return Double.parseDouble(getText().trim());
        }
        catch(Exception e)
        {
            setText(Double.toString(def));
            return def;
        }
    }

    public void setValue(double value)
    {
        setText(Double.toString(value));
    }

    /**
     * @return the sizeAsText
     */
    protected boolean isSizeAsText() {
        return sizeAsText;
    }

    /**
     * @param sizeAsText the sizeAsText to set
     */
    protected void setSizeAsText(boolean sizeAsText) {
        this.sizeAsText = sizeAsText;
    }
    
     /**
     * @return the def
     */
    protected double getDef() {
        return def;
    }

    /**
     * @param def the def to set
     */
    protected void setDef(double def) {
        this.def = def;
    }

    private double def = 0;
    private boolean sizeAsText = true;

    
}
