/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui;

import javax.swing.text.*;
import javax.swing.event.*;

/**
 *
 * @author Andrzej
 */
public final class EventHelper {
    private EventHelper() { }

    public static void addTextChanged(final JTextComponent c, final Listener<EventArgs> l) {
        c.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                l.invoked(new EventArgs(c, e));
            }

            public void removeUpdate(DocumentEvent e) {
                l.invoked(new EventArgs(c, e));
            }

            public void changedUpdate(DocumentEvent e) {
                l.invoked(new EventArgs(c, e));
            }
        });
    }

}
