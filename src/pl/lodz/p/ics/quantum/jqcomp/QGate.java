package pl.lodz.p.ics.quantum.jqcomp;

import pl.lodz.p.ics.quantum.jqcomp.qgates.CompoundQGate;
import pl.lodz.p.ics.quantum.jqcomp.qgates.Custom;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

abstract public class QGate implements Stage {

  	/**
	 * number of qubits this gates operates on
	 */
	protected int size;

	abstract public ComplexMatrix getMatrix();

    public int getSize() {
        return size;
    }

	/**
	 * Create a composed quantum gate 
	 * 
	 * @param gate
	 *            next parallel gate
	 * @return a stage of quantum computation
	 */
	public CompoundQGate next(QGate gate) {
		return new CompoundQGate(this, gate);
	}

	public QRegister mul(QRegister arg) {
		return new QRegister(getMatrix().times(arg.getMatrix()));
	}

	public QGate mul(QGate arg) {
		return new Custom(arg.getMatrix().times(this.getMatrix()));
	}

	public QGate add(QGate other) {
		return new Custom(getMatrix().plus(other.getMatrix()));
	}

	public QGate sub(QGate other) {
		return new Custom(getMatrix().minus(other.getMatrix()));
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
		return new Custom(getMatrix().transpose());
	}

	public QGate inverse() {
		return new Custom(getMatrix().inverse());
	}

    @Override
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


}