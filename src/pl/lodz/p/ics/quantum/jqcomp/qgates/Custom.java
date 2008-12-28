package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import pl.lodz.p.ics.quantum.jqcomp.MoreMath;

public class Custom extends ElementaryQGate {

	public Custom(ComplexMatrix matrix) {
		this.matrix = matrix;
        this.size = (int) MoreMath.log2(matrix.getNumberOfColumns());
        if (!matrix.isSquare())
            throw new RuntimeException("Matrix must be square");
        if (!isUnitary()) {
            System.out.println("***WARNING*** Matrix must be unitary: \n"+matrix);
           // throw new RuntimeException("Nonunitary");
        }
	}
	
	public Custom(Complex[][] matrix) {
		this(ComplexMatrix.valueOf(matrix));
	}
	
}
