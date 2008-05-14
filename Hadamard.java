package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;

// W³aœciwie nie widze potrzeby robienia oddzielnych klas pod warunkiem 
// ¿e zachowamy 'niezmiennoœæ' QGate; mo¿na by zrobiæ sta³e statyczne.
public class Hadamard extends AbstractQGate {
	public Hadamard() {
		ComplexMatrix m = h2;
		
		/* hmm
		 * for i in xrange(size - 1):
            m = kron(m, h2)
		 */		
	}	
	
	private static final ComplexMatrix h2 = ComplexMatrix.valueOf(new Complex[][] {
			new Complex[] {Complex.valueOf(1, 0), Complex.valueOf(1, 0)},
			new Complex[] {Complex.valueOf(1, 0), Complex.valueOf(-1, 0)}
	}).times(Complex.valueOf(Math.sqrt(2) / 2, 0));	
}
