/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui;

import pl.lodz.p.ics.quantum.jqcomp.*;
import pl.lodz.p.ics.quantum.jqcomp.qgates.*;

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

    public void removeByOwner(Object owner) {
        if(owner == null) {
            throw new NullPointerException("owner");
        }

        
    }

    private final java.util.List<Listener<T>> listeners;
}