/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui;

import jqcompgui.events.Event;
import jqcompgui.events.Listener;
import jqcompgui.events.EventArgs;
import jqcompgui.events.EventHelper;
import javax.swing.*;
import java.awt.event.*;
import java.util.EventObject;
import jqcompgui.events.EventInvoker;

/**
 *
 * @author Andrzej
 */
public class NumericJTextField extends JTextField
{
    public NumericJTextField()
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
                valueChangedInvoker.invoke(new EventObject(this));
            }
        });

        EventHelper.addTextChanged(this, new Listener<EventArgs>() {
            public void invoked(EventArgs e) {
                textChanged();
            }
        });

        textChanged();
    }

    public boolean passChar(char c)
    {       
        if(Character.isDigit(c)) {
            String text = getText();
            if (text.length() > getCaretPosition())
                if (text.charAt(getCaretPosition()) == '-') return false;
            return true;
        }

        if(c == '.' && floatingEnabled) {
            String text = getText();
            if(text.length() != 0 && !text.contains(".")) {
                return true;
            }
        }

        if((c == '-' && negativeExpEnabled) || c == '+') {
            String text = getText();
            if(text.length() == 0
               || Character.toLowerCase(text.charAt(text.length() - 1)) == 'e') {
                return true;
            }
        }

        if (c == '-' && negativeEnabled) {
            if (getCaretPosition()==0) return true;
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
            setSize(getPreferredSize());
        }
    }

    public double getValue()
    {
        return getValue(def);
    }

    public double getValue(double def)
    {
        try
        {
            double val = Double.parseDouble(getText().trim());
            if(!(!negativeEnabled && val < 0)
                && !(!floatingEnabled && val != ((double)(int)val)))
            {
                return val;
            }
        }
        catch(Exception e) {
      
        }

        setText(Double.toString(def));
        return def;
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

    /**
     * @return the floatingEnabled
     */
    public boolean isFloatingEnabled() {
        return floatingEnabled;
    }

    /**
     * @param floatingEnabled the floatingEnabled to set
     */
    public void setFloatingEnabled(boolean floatingEnabled) {
        this.floatingEnabled = floatingEnabled;
    }

    /**
     * @return the negativeEnabled
     */
    public boolean isNegativeExpEnabled() {
        return negativeExpEnabled;
    }

    /**
     * @param negativeEnabled the negativeEnabled to set
     */
    public void setNegativeExpEnabled(boolean negativeEnabled) {
        this.negativeExpEnabled = negativeEnabled;
    }

    public boolean isNegativeEnabled() {
        return negativeEnabled;
    }

    public void setNegativeEnabled(boolean negativeEnabled) {
        this.negativeEnabled = negativeEnabled;
    }

    private EventInvoker<EventObject> valueChangedInvoker =
            new EventInvoker<EventObject>();

    public Event<EventObject> valueChangedEvent(){
        return valueChangedInvoker.getEvent();
    }

    private double def = 0;
    private boolean sizeAsText = true;

    private boolean floatingEnabled = true;
    private boolean negativeExpEnabled = true;
    private boolean negativeEnabled = false;



}
