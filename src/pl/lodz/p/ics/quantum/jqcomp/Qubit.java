package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

public class Qubit extends QRegister {	
	public Qubit(Complex p, Complex q) {
		super(p,q);
	}	
	
	public final void flip() {
		matrix = ComplexMatrix.valueOf(new Complex[][]{
				new Complex[] {matrix.get(1, 0), matrix.get(0, 0)}});
	}
}
