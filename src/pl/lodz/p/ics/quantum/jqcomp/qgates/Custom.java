package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import pl.lodz.p.ics.quantum.jqcomp.MoreMath;

public class Custom extends ElementaryQGate {

	public Custom(ComplexMatrix matrix) {
		this.matrix = matrix;
        this.size = (int) MoreMath.log2(matrix.getNumberOfColumns());
	}
	
	public Custom(Complex[][] matrix) {
		this(ComplexMatrix.valueOf(matrix));
	}

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (!matrix.isSquare()) return false;;
        return this.isUnitary();
    }
	
}
