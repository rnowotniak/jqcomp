package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

public class AbstractQGate extends QGate {
	public AbstractQGate(ComplexMatrix matrix) {
		super(matrix);
		// TODO Auto-generated constructor stub
	}

	public AbstractQGate(QGate other) {
		super(other);
		// TODO Auto-generated constructor stub
	}
	
	public AbstractQGate(Complex[][] matrix) {
		super(matrix);
	}

	protected AbstractQGate() {
		// TODO Auto-generated constructor stub
	}
}
