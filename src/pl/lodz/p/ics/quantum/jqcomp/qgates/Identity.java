package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;
import pl.lodz.p.ics.quantum.jqcomp.MoreMath;


public class Identity extends ElementaryQGate {
	public Identity() { 
		this(1); 	
	}	
	
	public Identity(int size) {
		int dim = MoreMath.pow2(size);
		Complex[][] values = new Complex[dim][dim];
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				values[i][j] = Complex.valueOf((i == j) ? 1 : 0, 0);
			}			
		}
		
		this.matrix = ComplexMatrix.valueOf(values);
		this.size = size;
	}
}
