package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;

public class QGate {
	public QGate(ComplexMatrix matrix) {
		// TODO 
		this.matrix = matrix;
		this.size = matrix.getNumberOfRows();
	}	
	
	public QGate(QGate other) {
		this.matrix = other.matrix.copy();
		this.size = matrix.getNumberOfRows();
	}	
	
	protected QGate() { 
		// nic nie robi 
	}
	
	public Stage pow(QGate gate) {
		// narazie nie rozumiem implementacji w pythonie
		/*
		 def __pow__(self, arg2):
		        # parallel gates
		        if not isinstance(arg2, QGate):
		            raise Exception(repr(arg2))
		        result = Stage(self, arg2)
		        return result
		*/
		// dlaczego Stage?
		
		throw new RuntimeException("Not yet implemented.");
	}
	
	public QRegister mul(QRegister arg) {		
		// mam nadzieje ¿e dot oznacza dot product
		// result.matrix = dot(self.matrix, arg2.matrix)
		// oraz nie wiem czy dobrze interpretuje dzia³anie metody 'times', wg doca:
		// 'Returns the product of this matrix with the one specified.'
		// ponadto co ze sprawdzaniem wymiarow? z dokumentacji nic nie wynika,
		// mo¿e trzeba je sprawdziæ samemu, a mo¿e times zwróci null
		return new QRegister(this.matrix.times(arg.matrix));
	}
	
	public QGate mul(QGate arg) {
		// '# order changed!'
		// jw
		return new QGate(arg.matrix.times(this.matrix));
	}	
	
	public QGate add(QGate other) {
		return new QGate(this.matrix.plus(other.matrix));		
	}
	
	public QGate sub(QGate other) {
		return new QGate(this.matrix.minus(other.matrix));		
	}	
	
	public QRegister compute(QRegister arg) {
		return mul(arg);
	}
	
	public Complex trace() {
		return matrix.trace();
	}
	
	public Complex determinant() {
		return matrix.determinant();
	}	
	
	public QGate transpose() {
		return new QGate(matrix.transpose());
	}

	public QGate inverse() {
		return new QGate(matrix.inverse());
	}	
	
	/*	 troche dziwnie to by³o tam napisane
	 *   je¿eli mamy zamiar zmieniaæ stan danego obiektu
	 *   to mo¿e by go nie zwracaæ?
	public QGate inverse() {
		matrix = matrix.inverse();
		return this;
	}*/
	
	ComplexMatrix matrix;
	int size; // = columns ?	
}