package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

public class Custom extends ElementaryQGate {

	public Custom(ComplexMatrix matrix) {
		this.matrix = matrix;
	}
	
	public Custom(Complex[][] matrix) {
		this(ComplexMatrix.valueOf(matrix));
	}
	
}
