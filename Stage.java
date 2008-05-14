package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;

public class Stage extends QGate {
	public Stage(QGate[] gates) {
		this.gates = gates;
		
		size = 0;
		matrix = gates[0].matrix.copy();
		for(int i = 1; i < gates.length; i++) {
			// kron == tensor ?
			matrix = matrix.tensor(gates[i].matrix);
			size += gates[i].size;
		}	
	}
	
	QGate[] gates;	
}
