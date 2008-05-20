package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;


abstract public class ElementaryQGate extends QGate {

	protected ComplexMatrix matrix;

	public ComplexMatrix getMatrix() {
		return matrix;
	}
		

}
