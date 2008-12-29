/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.lodz.p.ics.quantum.jqcomp;

import pl.lodz.p.ics.quantum.jqcomp.qgates.*;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

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

    public void addQubit() {
        for(int i = 0; i < size(); i++) {
            QGate gate = (QGate)get(i);
            CompoundQGate newGate = new CompoundQGate(gate, new Identity());
            setUnchecked(i, newGate);
        }
    }

    public boolean tryRemoveLastQubit() {
        try {
            removeLastQubit();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void removeLastQubit() throws Exception {
        checkIfCanRemoveLastQubit();

        for(int i = 0; i < size(); i++) {
            QGate gate = (QGate)get(i);
            CompoundQGate compound = (CompoundQGate)gate;
            List<ElementaryQGate> gates = compound.getGates();

            List<QGate> gates2 = new ArrayList<QGate>(gates.size());
            for(ElementaryQGate g : gates) {
                gates2.add(g);
            }

            gates2.remove(gates.size() - 1);
            setUnchecked(i, new CompoundQGate(gates2));
        }
    }

    private void checkIfCanRemoveLastQubit() throws Exception {
        if(size() == 0) {
            throw new Exception("Quantum circuit has no stages");
        }

        if(get(0).getSize() < 2) {
            throw new Exception("Too few qubits to remove.");
        }

        for(int i = 0; i < size(); i++) {
            QGate gate = (QGate)get(i);

            CompoundQGate compound = (CompoundQGate)gate;
            java.util.List<ElementaryQGate> gates = compound.getGates();
            if(!(gates.get(gates.size() - 1) instanceof Identity)) {
                throw new Exception("Please remove stages which act on this qubit first");
            }
        }
    }
}
