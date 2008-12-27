package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;
import org.jscience.mathematics.number.Complex;
import pl.lodz.p.ics.quantum.jqcomp.MoreMath;

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
            if (control==target) throw new RuntimeException("CNOT Gate: control == target");
            size = Math.max(target, control) + 1 ;
            int dim = MoreMath.pow2(size);
            ComplexVector[] rows = new ComplexVector[dim];
            Complex[] row = new Complex[dim];
            for (int i=0; i<dim ;i++) {
                if ((i>>control & 1) == 1) {
                    int t = i;
                    t ^= (1 << target); // flip target bit
                    for (int j=0;j<dim;j++) {
                        row[j] = (j==t ? Complex.ONE : Complex.ZERO);
                    }
                } else {
                    for (int j=0;j<dim;j++)
                        row [j] = (i==j ? Complex.ONE : Complex.ZERO);
                }
                rows[i] = ComplexVector.valueOf(row);
            }
            this.matrix = ComplexMatrix.valueOf(rows);
		}
	}


    static private void replaceChar(StringBuilder sb, int index, String replacement ) {
        sb.replace(index, index+1, replacement);
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
