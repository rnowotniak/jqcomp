package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;



public class Hadamard extends ElementaryQGate {	

    public Hadamard() {
        this(1);
    }
    
	public Hadamard(int size) {
         if (size<1) {
             throw new RuntimeException("Hadamard gate size "+size+" < 1.");
         }
		 ComplexMatrix m = h1;
         for (int i=1;i<size;i++) {
             m = m.tensor(h1);
         }
		 this.matrix = m;
		 this.size = size;
    }


	
	private static final ComplexMatrix h1 = ComplexMatrix.valueOf(new Complex[][] {
			{Complex.valueOf(1, 0), Complex.valueOf(1, 0)},
			{Complex.valueOf(1, 0), Complex.valueOf(-1, 0)}
	}).times(Complex.valueOf(Math.sqrt(2) / 2, 0));		
}
