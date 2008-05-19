package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;



public class Hadamard extends AbstractQGate {	
	public Hadamard() {
		this(2);
	}
	
	public Hadamard(int size) {
		 if(size < 1) {
			 throw new WrongSizeException("size < 1");
		 }
		
		 ComplexMatrix m = h2;		
		 // w oryginale jest - 1, ale czy - 2 nie jest bardziej logiczne?
		 for(int i = 1; i < size ; i++) {
			 m = m.tensor(h2);
		 }
		 
		 this.matrix = m;
		 this.size = size;		 
		 
		/* nie wiem czy dobrze to zinterpretowa�em
		 * for i in xrange(size - 1):
            m = kron(m, h2)
		 */		
	}	
	
	private static final ComplexMatrix h2 = ComplexMatrix.valueOf(new Complex[][] {
			{Complex.valueOf(1, 0), Complex.valueOf(1, 0)},
			{Complex.valueOf(1, 0), Complex.valueOf(-1, 0)}
	}).times(Complex.valueOf(Math.sqrt(2) / 2, 0));		
}
