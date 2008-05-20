package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;


public class Not extends ElementaryQGate {
	public Not() {
		this.matrix = ComplexMatrix.valueOf(new Complex[][] {
				{cx(0), cx(1)},
				{cx(1), cx(0)}
		});
		
		this.size = 1;
	}
}
