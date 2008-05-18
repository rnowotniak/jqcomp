package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;

public class CNot extends AbstractQGate {
	public CNot() {
		this(1, 0);
	}
	
	public CNot(int control, int target){
		if(control == 1 && target == 0) {
			this.matrix = matrix10;
			this.size = matrix10.getNumberOfRows();
		}
		else {
			throw new RuntimeException("Not yet implemented");
		}
	}
	
	private static final ComplexMatrix matrix10 = ComplexMatrix.valueOf(
		new Complex[][] {
			{cx(1), cx(0), cx(0), cx(0)},
			{cx(0), cx(1), cx(0), cx(0)},
			{cx(0), cx(0), cx(0), cx(1)},
			{cx(0), cx(0), cx(1), cx(0)},
	});
}
