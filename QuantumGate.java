package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

public class QuantumGate {
	
	ComplexMatrix matrix;
	int size; // = columns ?
	
	public void compute() {
		
	}
	
	public QuantumGate(ComplexMatrix x) {
		// TODO
		matrix = x;
		size = x.getNumberOfRows();
	}
	
	public Complex determinant() {
		return matrix.determinant();
	}

	public QuantumGate inverse() {
		matrix = matrix.inverse();
		return this;
	}
	
}
