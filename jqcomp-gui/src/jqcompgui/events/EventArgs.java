/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui.events;

/**
 *
 * @author Andrzej
 */
public class EventArgs extends java.util.EventObject {
    public EventArgs(Object source, Object eventData) {
        super(source);
        this.eventData = eventData;
    }

    public EventArgs(Object source) {
        this(source, NO_DATA);
    }

    /**
     * @return the eventData
     */
    public Object getEventData() {
        return eventData;
    }

    private final Object eventData;

    public static final Object NO_DATA = new Object();
}
