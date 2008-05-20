package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

abstract public class QGate {

	abstract public ComplexMatrix getMatrix();

	/**
	 * Create a composed quantum gate 
	 * 
	 * @param gate
	 *            next parallel gate
	 * @return a stage of quantum computation
	 */
	public Stage next(QGate gate) {
		return new Stage(this, gate);
	}

	public QRegister mul(QRegister arg) {
		return new QRegister(getMatrix().times(arg.matrix));
	}

	public QGate mul(QGate arg) {
		return new Arbitrary(arg.getMatrix().times(this.getMatrix()));
	}

	public QGate add(QGate other) {
		return new Arbitrary(getMatrix().plus(other.getMatrix()));
	}

	public QGate sub(QGate other) {
		return new Arbitrary(getMatrix().minus(other.getMatrix()));
	}

	public QRegister compute(QRegister arg) {
		return mul(arg);
	}

	public Complex trace() {
		return getMatrix().trace();
	}

	public Complex determinant() {
		return getMatrix().determinant();
	}

	public QGate transpose() {
		return new Arbitrary(getMatrix().transpose());
	}

	public QGate inverse() {
		return new Arbitrary(getMatrix().inverse());
	}

	public String toString() {
		return getMatrix().toString();
	}

	/*
	 * troche dziwnie to by�o tam napisane je�eli mamy zamiar zmienia� stan
	 * danego obiektu to mo�e by go nie zwraca�? public QGate inverse() { matrix =
	 * matrix.inverse(); return this; }
	 */

	// troche dziwne miejsce na te metody, ale przez to jest mniej pisania
	protected static Complex cx(double real, double imaginary) {
		return Complex.valueOf(real, imaginary);
	}

	protected static Complex cx(double real) {
		return cx(real, 0);
	}

	/**
	 * number of qubits this gates operates on
	 */
	public int size;
}