package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;
import org.jscience.mathematics.vector.DimensionException;

public class QRegister {

	/**
	 * Create qregister in |00...0> initial state
	 * 
	 * @param size
	 *            number of qubits
	 */
	public QRegister(int size) {
		int dim = MoreMath.pow2(size);

		Complex[] initialValues = new Complex[dim];
		initialValues[0] = Complex.valueOf(1, 0);
		for (int i = 1; i < dim; i++) {
			initialValues[i] = Complex.valueOf(0, 0);
		}
		ComplexVector vector = ComplexVector.valueOf(initialValues);

		this.matrix = ComplexMatrix.valueOf(vector).transpose();
		this.size = size;
	}

	public QRegister(Complex... initialValues) {
		this(ComplexMatrix.valueOf(new Complex[][] { initialValues }));
	}

	public QRegister(ComplexMatrix initialValues) {
		ComplexMatrix matrix = initialValues.copy();
		if (matrix.getNumberOfColumns() > 1) {
			matrix = matrix.transpose();
		}
		if (matrix.getNumberOfColumns() != 1) {
			throw new DimensionException("Input matrix must have 1 column.");
		}
		int size = (int) MoreMath.log2(matrix.getNumberOfRows());
		if (size != MoreMath.log2(matrix.getNumberOfRows())) {
			throw new WrongSizeException(
					"Dimension of QRegister state space have to be the power of 2");
		}
		this.matrix = matrix;
		this.size = size;
	}

	public QRegister(QRegister other) {
		this.size = other.size;
		this.matrix = other.matrix.copy();
	}

	public final boolean equals(QRegister other) {
		return matrix.equals(other.matrix);
	}

	/** Addition */
	public final QRegister add(QRegister that) {
		return new QRegister(this.matrix.plus(that.matrix));
	}

	public final QRegister sub(QRegister other) {
		return new QRegister(this.matrix.minus(other.matrix));
	}

	/** Inner product */
	public final Complex inner(QRegister that) {
		// may raise DimensionException
		return MoreMath.ConjugateTranspose(this.matrix).times(that.matrix).get(
				0, 0);
	}

	/**
	 * Compute the norm of the QRegister state vector
	 * 
	 * @return norm of the vector
	 */
	public double norm() {
		// Please note that this.inner(this) doesn't have any imaginary part
		double result = this.inner(this).getReal();
		return Math.sqrt(result);
	}

	/**
	 * Normalize QRegister state vector
	 * 
	 * This method modifies current object and returns the modified state
	 * 
	 * @return normalized QRegister state
	 */
	public final QRegister normalize() {
		double norm0 = this.norm();
		Complex coordinates[] = new Complex[MoreMath.pow2(size)];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = matrix.get(i, 0).divide(norm0);
		}
		matrix = ComplexMatrix.valueOf(ComplexVector.valueOf(coordinates))
				.transpose();
		return this;
	}

	/** Outer product */
	public final ComplexMatrix outer(QRegister that) {
		// may raise DimensionException
		return this.matrix.times(that.matrix.transpose());
	}

	/** Set [1, 0, 0, ..., 0] */
	public void reset() {
		Complex coordinates[] = new Complex[MoreMath.pow2(size)];
		coordinates[0] = Complex.valueOf(1, 0);
		for (int i = 1; i < coordinates.length; i++) {
			coordinates[i] = Complex.valueOf(0, 0);
		}
		matrix = ComplexMatrix.valueOf(ComplexVector.valueOf(coordinates))
				.transpose();
	}

	/** Tensor (Kronecker) product */
	public final QRegister tensor(QRegister that) {
		return new QRegister(this.matrix.tensor(that.matrix));
	}

	/**
	 * Perform projective measurement operation
	 * 
	 * @param qubits
	 *            indices of measured qubits
	 * @return one of the measurement subspace base vectors
	 */
	public QRegister measure(int... qubits) {
		throw new RuntimeException("Not implemented yet");
	}

	/**
	 * Return state in Dirac (bra-ket) notation
	 * 
	 * @return string representing current state in Dirac notation
	 */
	public String dirac() {
		int zeros = 0;
		int onePos = -1;
		String ret = "";
		ComplexVector vector = matrix.getColumn(0);
		int vectorSize = MoreMath.pow2(size);
		for (int i=0;i<vectorSize;i++) {
			if (vector.get(i).getReal()==1.0&&vector.get(i).getImaginary()==0.0) {
				if (onePos==-1) onePos = i; // 1st occurence of 1+0j found
				else {
					onePos=-1;
					break;
				}
			}
			else if (vector.get(i).getReal()==0.0&&vector.get(i).getImaginary()==0.0) {
				zeros++;
			}
		}
		if (onePos>=0&&vectorSize-1==zeros) { 
			// mamy jedno "1" (1+0j) a reszta to zera: to wektor bazowy
			ret = Integer.toBinaryString(onePos) + ">";
			while (ret.length()-1 < size) 
				ret = "0" + ret;
			return "|" + ret;
		}
		int displayedItems = 0;
		StringBuffer output = new StringBuffer();
		for (int i=0;i<vectorSize;i++){
			Complex elem = vector.get(i);
			// don't display (0+0j)*|ket>
			if ( !MoreMath.isNearZero(elem.getReal()) || !MoreMath.isNearZero(elem.getImaginary())) {
				if (displayedItems>0) output.append(" + ");				   
				output.append("(").append(elem).append(")|");
				String tmpStr = Integer.toBinaryString(i) + ">";
				while (tmpStr.length()-1 < size) 
					tmpStr= "0" + tmpStr;
				output.append(tmpStr);
				displayedItems++;
			}
		}
		return output.toString();
	}

	public String toString() {
		return matrix.toString();
	}

	protected int size;
	protected ComplexMatrix matrix;
	
	public ComplexMatrix getMatrix() {
		return matrix;
	}
}
