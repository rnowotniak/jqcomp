package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;


public class CNot extends ElementaryQGate {

    private int control;
    private int target;

	public CNot() {
		this(1, 0);
	}
	
	public CNot(int control, int target){
        this.control = control;
        this.target = target;
		if(control == 1 && target == 0) {
			this.matrix = matrix10;
			this.size = 2;
		}
		else if (control == 0 && target == 1) {
			this.matrix = matrix01;
			this.size = 2;
		}
		else {
			throw new RuntimeException("Not yet implemented");
		}
	}

    /**
     * @return the control
     */
    public int getControl() {
        return control;
    }

    /**
     * @return the target
     */
    public int getTarget() {
        return target;
    }
	
	private static final ComplexMatrix matrix10 = ComplexMatrix.valueOf(
		new Complex[][] {
			{cx(1), cx(0), cx(0), cx(0)},
			{cx(0), cx(1), cx(0), cx(0)},
			{cx(0), cx(0), cx(0), cx(1)},
			{cx(0), cx(0), cx(1), cx(0)},
	});
	
	private static final ComplexMatrix matrix01 = ComplexMatrix.valueOf(
			new Complex[][] {
				{cx(1), cx(0), cx(0), cx(0)},
				{cx(0), cx(0), cx(0), cx(1)},
				{cx(0), cx(0), cx(1), cx(0)},
				{cx(0), cx(1), cx(0), cx(0)},
		});
}
