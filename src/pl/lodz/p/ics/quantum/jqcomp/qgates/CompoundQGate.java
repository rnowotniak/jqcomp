package pl.lodz.p.ics.quantum.jqcomp.qgates;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.vector.ComplexMatrix;
import pl.lodz.p.ics.quantum.jqcomp.QGate;

/**
 * A stage of computing in quantum circuit
 * 
 */
public class CompoundQGate extends QGate {

    private List<ElementaryQGate> gates;

	public CompoundQGate() {
		this(new Custom[] {});
	}

	public CompoundQGate(QGate... gates) {
		this.gates = new ArrayList<ElementaryQGate>();
		for (QGate gate : gates) {
			if (gate instanceof CompoundQGate) {
				this.gates.addAll(((CompoundQGate) gate).gates);
			} else if (gate instanceof ElementaryQGate) {
				this.gates.add((ElementaryQGate) gate);
			} else {
				/* NOT REACHABLE */
				assert (false);
			}
		}
		this.size = 0;
		for (ElementaryQGate gate : this.gates) {
			this.size += gate.getSize();
		}
	}

	public void addGate(ElementaryQGate gate) {
		gates.add(gate);
	}

	public void addGate(int index, ElementaryQGate gate) {
		gates.add(index, gate);
	}

	public ComplexMatrix getMatrix() {
		ComplexMatrix result = gates.get(0).getMatrix().copy();
		for (int i = 1; i < gates.size(); i++) {
			result = result.tensor(gates.get(i).matrix);
		}
		return result;
	}

    /**
     * @return the gates
     */
    public List<ElementaryQGate> getGates() {
        return gates;
    }

    /**
     * @param gates the gates to set
     */
    public void setGates(List<ElementaryQGate> gates) {
        this.gates = gates;
    }

}
