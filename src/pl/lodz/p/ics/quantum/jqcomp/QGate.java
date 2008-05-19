package pl.lodz.p.ics.quantum.jqcomp;


import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

public class QGate {
	public QGate(ComplexMatrix matrix) {
		// TODO 
		this.matrix = matrix;
		this.size = matrix.getNumberOfRows();
	}	
	
	public QGate(Complex[][] matrix) {
		this.matrix = ComplexMatrix.valueOf(matrix);
		this.size = this.matrix.getNumberOfRows();
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
		// mam nadzieje �e dot oznacza dot product
		// result.matrix = dot(self.matrix, arg2.matrix)
		// oraz nie wiem czy dobrze interpretuje dzia�anie metody 'times', wg doca:
		// 'Returns the product of this matrix with the one specified.'
		// ponadto co ze sprawdzaniem wymiarow? z dokumentacji nic nie wynika,
		// mo�e trzeba je sprawdzi� samemu, a mo�e times zwr�ci null
		
		/* RE:
		 * Matrix.times to zwykły iloczyn macierzy.
		 * Dla operatora (bramki) A i rejestru x wykonuje A*x, tzn. wykonuje
		 * "operację" A na x.  Wynikiem jest nowy Qregister po przekształceniu
		 * o wymiarach takich samych jak stary x.
		 * 
		 * Wymagania:
		 * wymiary A : size * size
		 * wymiary x : size * 1
		 * 
		 * Np. Blad pojawis się gdy bramkę działającą na 2 qubitach używamy na 
		 * rejestrze 1-bitowym (DimensionException);
		 */
		return new QRegister(this.matrix.times(arg.matrix));
	}
	
	public QGate mul(QGate arg) {
		// bramka * bramka to nowa bramka
		// odpowiednik złożenia (superpozycji) operacji (działania tych 2 bramek)

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
	
	public String toString() {
		return matrix.toString();
	}
	
	/*	 troche dziwnie to by�o tam napisane
	 *   je�eli mamy zamiar zmienia� stan danego obiektu
	 *   to mo�e by go nie zwraca�?
	public QGate inverse() {
		matrix = matrix.inverse();
		return this;
	}*/
	
	 // troche dziwne miejsce na te metody, ale przez to jest mniej pisania
	 protected static Complex cx(double real, double imaginary) {
		return Complex.valueOf(real, imaginary); 
	 }
	 
	 protected static Complex cx(double real) {
		return cx(real, 0); 
	 }	 
	 
	 /*	 
	 protected static toComplexArray(double[][] values) {
		Complex[][] cvalues = new Complex[values.length][values.length] 
	 }*/
	
	ComplexMatrix matrix;
	int size; // = columns ?	
}