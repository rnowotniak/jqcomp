/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.lodz.p.ics.quantum.jqcomp;

import java.util.Collection;

/**
 *
 * @author Andrzej
 */
public class StageList extends MonitoredList<Stage> {
    public StageList() {
        super();
    }

    public StageList(int initialCapacity) {
        super(initialCapacity);
    }

    public StageList(Collection<? extends Stage> c) {
        super(c);
    }

    public StageList(Stage[] a) {
        super(a);
    }

    @Override
    protected void check(Stage element) {
        if(element == null) {
            throw new NullPointerException("StageList does not allow null values");
        }

        if(size() > 0 && get(0).getSize() != element.getSize()) {
            throw new WrongSizeException("All stages in circuit must have same size.");
        }
    }
}
