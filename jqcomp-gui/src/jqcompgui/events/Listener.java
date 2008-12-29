/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui.events;

/**
 *
 * @author Andrzej
 */
public interface Listener<T extends java.util.EventObject> {
    void invoked(T e);
}
