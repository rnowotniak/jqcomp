/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.lodz.p.ics.quantum.jqcomp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
/**
 *
 * @author Andrzej
 */
public class MonitoredList<E> extends ArrayList<E> {
    public static interface ChangeListener<E> {
        /**
         * Note: the method may be called before the element is added to the list
         * @param element
         * @param index
         */
        void elementAdded(E element, int index);

         /**
         * Note: the method may be called before the element is removed from the list
         * @param element
         * @param index
         */
        void elementRemoved(E element, int index);
    }

    public static abstract class ChangeAdapter<E> implements ChangeListener<E> {
        public void elementAdded(E element, int index) { }
        public void elementRemoved(E element, int index) { }
    }
    
    public MonitoredList() {
        super();
    }

    public MonitoredList(int initialCapacity) {
        super(initialCapacity);
    }

    public MonitoredList(Collection<? extends E> c) {
        super(c);
    }

    public MonitoredList(E[] a) {
        ensureCapacity(a.length);
        for(int i = 0; i < a.length; i++) {
            add(a[i]);
        }
    }

    /**
     * method throws an exception if provided element does not fit into collection
     * @param element the element to be checked
     */
    protected void check(E element) {
        // does nothing
    }

    protected void onAdd(E element, int index) {
        if(changeListeners != null) {
            for(ChangeListener l : changeListeners) {
                l.elementAdded(element, index);
            }
        }
    }

    protected void onRemove(E element, int index) {
        if(changeListeners != null) {
            for(ChangeListener l : changeListeners) {
                l.elementRemoved(element, index);
            }
        }
    }

    public final void addChangeListener(ChangeListener l) {
        if(l == null) {
            throw new NullPointerException("l");
        }

        if(changeListeners == null) {
            changeListeners = new LinkedList<ChangeListener>();
        }

        changeListeners.add(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        if(l == null) {
            throw new NullPointerException("l");
        }

        if(changeListeners == null) {
            changeListeners = new LinkedList<ChangeListener>();
        }

        changeListeners.remove(l);
    }

    private LinkedList<ChangeListener> changeListeners = null;

    @Override
    public E set(int index, E element) {
        check(element);
        
        E oldValue = super.set(index, element);
        if(oldValue != null) {
            onRemove(oldValue, index);
        }

        onAdd(element, index);
        return oldValue;
    }

    protected E setUnchecked(int index, E element) {
        return super.set(index, element);
    }

    @Override
    public boolean add(E e) {
        check(e);
        if(super.add(e)) { // always true 
            onAdd(e, size() - 1);
            return true;
        }

        return false;
    }

    @Override
    public void add(int index, E element) {
        check(element);
        super.add(index, element);
        onAdd(element, index);
    }

    @Override
    public E remove(int index) {
        E oldValue = super.remove(index);
        onRemove(oldValue, index);
        return oldValue;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if(i > 0) {
            remove(i);
            return true;
        }

        return false;
    }

    @Override
    public void clear() {
        for(int i = 0; i < size(); i++) {
            onRemove(get(i), i);
        }

        super.clear();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        ensureCapacity(c.size());
        for(E s : c) {
            add(s);
        }

        return true; // throws exception on failure anyway
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for(E s : c) {
            check(s);
        }

        return super.addAll(index, c);
    }
    
    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        for(int i = fromIndex; i <= toIndex; i++) {
            onRemove(get(i), i);
        }

        super.removeRange(fromIndex, toIndex);
    }
}
