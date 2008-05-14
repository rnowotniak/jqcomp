package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;

public class Identity extends AbstractQGate {
	public Identity() { 
		this(2); 	
	}	
	
	public Identity(int size) {
		Complex[][] values = new Complex[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				values[i][j] = Complex.valueOf((i == j) ? 1 : 0, 0);
			}			
		}
		
		this.matrix = ComplexMatrix.valueOf(values);
		this.size = this.matrix.getNumberOfRows();
	}
}
