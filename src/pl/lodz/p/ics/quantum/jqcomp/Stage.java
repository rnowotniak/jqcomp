package pl.lodz.p.ics.quantum.jqcomp;

import java.util.ArrayList;
import java.util.List;

import org.jscience.mathematics.vector.ComplexMatrix;

/**
 * A stage of computing in quantum circuit
 * 
 */
public class Stage extends QGate {

	public Stage() {
		this(new Arbitrary[] {});
	}

	public Stage(QGate... gates) {
		this.gates = new ArrayList<ElementaryQGate>();
		for (QGate gate : gates) {
			if (gate instanceof Stage) {
				this.gates.addAll(((Stage) gate).gates);
			} else if (gate instanceof ElementaryQGate) {
				this.gates.add((ElementaryQGate) gate);
			} else {
				/* NOT REACHABLE */
				assert (false);
			}
		}
		this.size = 0;
		for (ElementaryQGate gate : this.gates) {
			this.size += gate.size;
		}
	}

	public void addGate(ElementaryQGate gate) {
		gates.add(gate);
	}

	public void addGate(int index, ElementaryQGate gate) {
		gates.add(index, gate);
	}

	@Override
	public ComplexMatrix getMatrix() {
		ComplexMatrix result = gates.get(0).getMatrix().copy();
		for (int i = 1; i < gates.size(); i++) {
			result = result.tensor(gates.get(i).matrix);
		}
		return result;
	}

	private List<ElementaryQGate> gates;
}
