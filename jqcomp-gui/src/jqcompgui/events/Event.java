/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui.events;

/**
 *
 * @author Andrzej
 */
public class Event<T extends java.util.EventObject> {
    public Event(java.util.List<Listener<T>> listeners) {
        if(listeners == null) {
            throw new NullPointerException("listeners");
        }
      
        this.listeners = listeners;
    }

    public void add(Listener<T> l) {
        if(l == null) {
            throw new NullPointerException("l");
        }

        synchronized(listeners) {
            listeners.add(l);
        }
    }

    public void remove(Listener<T> l) {
        if(l == null) {
            throw new NullPointerException("l");
        }

        synchronized(listeners) {
            listeners.remove(l);
        }
    }

    private final java.util.List<Listener<T>> listeners;
}