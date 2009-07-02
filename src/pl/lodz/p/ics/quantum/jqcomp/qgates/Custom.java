package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;
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
     * @return true iff gate is unitary.
     */
    public boolean isValid() {
        if (!matrix.isSquare()) return false;
        return this.isUnitary();
    }
	
    public void orthonormalize(){
        int rows = matrix.getNumberOfRows();
        ComplexVector[] r = new ComplexVector[rows];
        for (int i=0;i<rows;i++) {
            r[i] = matrix.getRow(i);
        }
        matrix = ComplexMatrix.valueOf(MoreMath.GramSchmidtOrthonormalization(r));
    }

}
