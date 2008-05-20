package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;



public class Hadamard extends ElementaryQGate {	

	public Hadamard() {
		 ComplexMatrix m = h;				 
		 this.matrix = m;
		 this.size = 1;		
	}	
	
	private static final ComplexMatrix h = ComplexMatrix.valueOf(new Complex[][] {
			{Complex.valueOf(1, 0), Complex.valueOf(1, 0)},
			{Complex.valueOf(1, 0), Complex.valueOf(-1, 0)}
	}).times(Complex.valueOf(Math.sqrt(2) / 2, 0));		
}
