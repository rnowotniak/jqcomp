package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;


public class Qubit extends QRegister {
	
	/**
	 * Create |0> qubit state
	 */
	public Qubit() {
		super(1);
	}
	
	public Qubit(Complex alpha, Complex beta) {
		super(alpha,beta);
	}	
	
	public final void flip() {
		matrix = ComplexMatrix.valueOf(new Complex[][]{
				new Complex[] {matrix.get(1, 0), matrix.get(0, 0)}});
	}
}
