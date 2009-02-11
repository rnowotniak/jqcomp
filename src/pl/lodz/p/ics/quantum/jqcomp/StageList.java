/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.lodz.p.ics.quantum.jqcomp;

import pl.lodz.p.ics.quantum.jqcomp.qgates.*;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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
            Stage stage = get(i);
            if (stage instanceof QGate) {
                QGate gate = (QGate)stage;
                CompoundQGate newGate = new CompoundQGate(gate, new Identity());
                setUnchecked(i, newGate);
            }
            else if (stage instanceof Measurement) {
                Measurement measure = (Measurement)stage;
                measure.expand(); // increase size by 1
                setUnchecked(i, measure);
            }
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
            if (get(i) instanceof QGate) {
                QGate gate = (QGate)get(i);
                CompoundQGate compound = (CompoundQGate)gate;
                List<ElementaryQGate> gates = compound.getGates();

                List<QGate> gates2 = new ArrayList<QGate>(gates.size());
                for(ElementaryQGate g : gates) {
                    gates2.add(g);
                }

                gates2.remove(gates.size() - 1);
                setUnchecked(i, new CompoundQGate(gates2));
            } else if (get(i) instanceof Measurement) {
                Measurement m = (Measurement) get(i);
                m.shrink();
                setUnchecked(i, m);
            }
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
            if (get(i) instanceof QGate) {
                QGate gate = (QGate)get(i);

                CompoundQGate compound = (CompoundQGate)gate;
                java.util.List<ElementaryQGate> gates = compound.getGates();
                if(!(gates.get(gates.size() - 1) instanceof Identity)) {
                    throw new Exception("Please remove stages which act on this qubit first");
                }
            } else if (get(i) instanceof Measurement) {
                Measurement m = (Measurement) get(i);
                int[] qubits = m.getMeasuredQubits();
                Arrays.sort(qubits);
                if (qubits[0]==0)
                    throw new Exception("Please remove measurements performed on |q0>.");
            }

        }
    }

    public boolean moveStage(Stage stage, int where) {
        int index = indexOfReference(stage);
        return moveStage(index, where);
    }

    public boolean moveStage(int stageToMove, int where) {
        if(stageToMove > -1) {
            Stage newStage = get(where);
            Stage stage = get(stageToMove);
            if(newStage != stage){
                remove(stageToMove);
                add(where, stage);
                return true;
            }
        }

        return false;
    }
}
