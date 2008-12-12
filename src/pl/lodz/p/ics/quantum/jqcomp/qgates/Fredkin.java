package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;

public class Fredkin extends ElementaryQGate {
	public Fredkin() {
		this.size = 3;
		this.matrix = fmatrix;
	}
	private static final ComplexMatrix fmatrix = ComplexMatrix.valueOf(
				new Complex[][]{
		  { cx(1),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0)},
          { cx(0),  cx(1),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0)},
          { cx(0),  cx(0),  cx(1),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0)},
          { cx(0),  cx(0),  cx(0),  cx(1),  cx(0),  cx(0),  cx(0),  cx(0)},
          { cx(0),  cx(0),  cx(0),  cx(0),  cx(1),  cx(0),  cx(0),  cx(0)},
          { cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(1),  cx(0)},
          { cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(1),  cx(0),  cx(0)},
          { cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(0),  cx(1)}
				}
	 );
}
