package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;



public class PhaseShift extends AbstractQGate {
	public PhaseShift(double angle) {
		this.angle = angle;
		this.matrix = ComplexMatrix.valueOf(new Complex[][] {
				{cx(1), cx(0)},
				{cx(0), cx(angle, 1).exp()} // exp(angle * 1j)
		});
		
		this.size = matrix.getNumberOfRows();		
	}
	
	double angle;
}
