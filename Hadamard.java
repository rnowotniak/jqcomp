package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;


public class Hadamard extends AbstractQGate {
	public Hadamard(int size) {
		 ComplexMatrix m = h2;		
		 for(int i = 0; i < size - 1; i++) {
			 m = m.tensor(h2);
		 }
		 
		 this.matrix = m;
		 this.size = m.getNumberOfRows();		 
		 
		/* nie wiem czy dobrze to zinterpretowa³em
		 * for i in xrange(size - 1):
            m = kron(m, h2)
		 */		
	}	
	
	private static final ComplexMatrix h2 = ComplexMatrix.valueOf(new Complex[][] {
			{Complex.valueOf(1, 0), Complex.valueOf(1, 0)},
			{Complex.valueOf(1, 0), Complex.valueOf(-1, 0)}
	}).times(Complex.valueOf(Math.sqrt(2) / 2, 0));		
}
