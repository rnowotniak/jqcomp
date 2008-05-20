package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

public class Arbitrary extends ElementaryQGate {

	public Arbitrary(ComplexMatrix matrix) {
		this.matrix = matrix;
	}
	
	public Arbitrary(Complex[][] matrix) {
		this(ComplexMatrix.valueOf(matrix));
	}
	
}
