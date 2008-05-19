package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;



public class Toffoli extends AbstractQGate {
	public Toffoli() {
		this.matrix = tmatrix;
		this.size = tmatrix.getNumberOfRows();
		// dlaczego tam jest size = 3 ???
	}
	
	private static final ComplexMatrix tmatrix = ComplexMatrix.valueOf(
		new Complex[][]{
				{ cx(1),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0)},
	            { cx(0),  cx(1),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0)},
	            { cx(0),  cx(0),  cx(1),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0)},
	            { cx(0),  cx(0),  cx(0),  cx(1),  cx(0),  cx(0),  cx(0),  cx(0)},
	            { cx(0),  cx(0),  cx(0),  cx(0),  cx(1),  cx(0),  cx(0),  cx(0)},
	            { cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(1),  cx(0),  cx(0)},
	            { cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(1)},
	            { cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(1),  cx(0)}
	});
}