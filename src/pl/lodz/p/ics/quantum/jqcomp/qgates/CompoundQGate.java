package pl.lodz.p.ics.quantum.jqcomp.qgates;

import java.util.ArrayList;
import java.util.List;
import java.lang.Iterable;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;
import pl.lodz.p.ics.quantum.jqcomp.QGate;
import pl.lodz.p.ics.quantum.jqcomp.qgates.ElementaryQGate;

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
		initialize(gates);
	}
    
    public CompoundQGate(List<QGate> gates) {
        QGate[] g = new QGate[gates.size()];
        gates.toArray(g);
        initialize(g);
	}
    
    private void initialize(QGate[] gates) {
        this.gates = new ArrayList<ElementaryQGate>();
		for (QGate gate : gates) {
			if (gate instanceof CompoundQGate) {
				this.gates.addAll(((CompoundQGate)gate).gates);
			} else if (gate instanceof ElementaryQGate) {
				this.gates.add((ElementaryQGate)gate);
			} else {
				/* NOT REACHABLE */
				assert (false);
			}
		}
		this.size = 0;
		for (QGate gate : this.gates) {
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
        if(gates.size() == 0) {
            //return ComplexMatrix.valueOf(new Complex[0][0]);
            return null;
        }

		ComplexMatrix result = gates.get(0).getMatrix().copy();
		for (int i = 1; i < gates.size(); i++) {
			result = result.tensor(((ElementaryQGate)gates.get(i)).matrix);
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
