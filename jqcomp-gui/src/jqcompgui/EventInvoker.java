/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui;

/**
 *
 * @author Andrzej
 */
public class EventInvoker<T extends java.util.EventObject> {
    public EventInvoker() {
        this.listeners = new java.util.LinkedList<Listener<T>>();
        this.event = new Event<T>(this.listeners);
    }

    public void invoke(T e) {
        synchronized(listeners) {
            for(Listener<T> l : listeners) {
                l.invoked(e);
            }
        }
    }

    public Event<T> getEvent() {
        return event;
    }

    private final java.util.List<Listener<T>> listeners;
    private final Event<T> event;
}
