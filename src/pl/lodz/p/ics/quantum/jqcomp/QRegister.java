package pl.lodz.p.ics.quantum.jqcomp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import pl.lodz.p.ics.quantum.jqcomp.qgates.Custom;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;
import org.jscience.mathematics.vector.DimensionException;

public class QRegister {
	
	protected int size;
	protected ComplexMatrix matrix;
	
	/** Create Ket-n from H^{2^qubits} */
	public static QRegister ket(int n, int qubits){
		
		QRegister ret = new QRegister(qubits);
		if (n==0) return ret;
		Complex[] values = ret.toComplexArray();
		values[0]=Complex.valueOf(0,0);
		try {
			values[n]=Complex.valueOf(1,0);
		} catch (IndexOutOfBoundsException e) {
			//throw new RuntimeException();
            return null;
		}
		return new QRegister(values);
	}
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
		matrix = initialValues.copy();
		if (matrix.getNumberOfColumns() > 1) {
			matrix = matrix.transpose();
		}
		if (matrix.getNumberOfColumns() != 1) {
			throw new DimensionException("Input matrix must have 1 column.");
		}
		size = (int) MoreMath.log2(matrix.getNumberOfRows());
		if (size != MoreMath.log2(matrix.getNumberOfRows())) {
			throw new WrongSizeException(
					"Dimension of QRegister state space have to be the power of 2");
		}
	}

	public QRegister(QRegister other) {
		this.size = other.size;
		this.matrix = other.matrix.copy();
	}

    @Override
	public final boolean equals(Object other) {
        if (! (other instanceof QRegister )) return false;
        ComplexMatrix otherMatrix = ((QRegister)other).matrix;
        return MoreMath.isNearMatrix(this.matrix, otherMatrix);
	}

/**
 * Add two registers.
 * @param that
 * @return this+that
 */
	public final QRegister add(QRegister that) {
		return new QRegister(this.matrix.plus(that.matrix));
	}

	public final QRegister sub(QRegister other) {
		return new QRegister(this.matrix.minus(other.matrix));
	}

	/** Compute the inner product of two registers. */
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

	/** Computer the outer product.
     @param that
     */
	public final QGate outer(QRegister that) {
		
		return new Custom(this.matrix.times(that.matrix.transpose()));
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

	/** Compute the tensor (Kronecker) product*/
	public final QRegister tensor(QRegister that) {
		return new QRegister(this.matrix.tensor(that.matrix));
	}
	
	
	/** Set selected amplitudes to zero
     */
	private final void zeroAmplitudes(ArrayList<Integer> amp){
		Complex[] array = this.toComplexArray();

		for (Integer i: amp) {
			array[i] = Complex.ZERO;		
		}
	
		matrix = ComplexMatrix.valueOf(ComplexVector.valueOf(array))
			.transpose();
	}

	/** Measure all qubits of this quantum register. */
	public final QRegister measure() {
		Complex[] elements = this.toComplexArray();
		double[] probabilities = new double[elements.length];
		for (int i=0;i<elements.length;i++) {
			double x = elements[i].magnitude();
			probabilities[i] = x * x;
		}

		double sum = 0.0;
		double rand = Math.random();
		for (int i=0;i<probabilities.length;i++) {
			sum += probabilities[i];
			if (rand<sum) {
				return ket(i,
						(int)(Math.round(MoreMath.log2(elements.length))));
			}
		}
		
		QRegister newRegister = ket(probabilities.length-1,
				(int)(Math.round(MoreMath.log2(elements.length))));
		
		matrix = newRegister.getMatrix(); //niszczymy stare dane
		return newRegister;
		
	}

	/**
	 * Perform projective measurement operation.
	 * 
	 * @param qubits
	 *            indices of measured qubits
	 * @return one of the measurement subspace base vectors
	 */
	public QRegister measure(int... qubits) {
		for (int i=0; i<qubits.length; i++) {
			qubits[i] = this.size - qubits[i] - 1;
		}
		Arrays.sort(qubits);
		int probLen = MoreMath.pow2(qubits.length);
		HashMap<String, Double> prob = new HashMap<String, Double>(probLen);
		HashMap<String, String> match = new HashMap<String, String>(MoreMath.pow2(this.size));
		for (int i=0;i<probLen;i++) {
			String binary = Integer.toBinaryString(i);
			while (binary.length()<qubits.length) binary = "0"+binary;
			prob.put(binary, 0.0);
		}

		// przebiegnij wszystkie możliwe stany bazowe
		for (int i=0;i<MoreMath.pow2(this.size);i++) {
			String binary = Integer.toBinaryString(i);
			while (binary.length()<this.size) binary = "0"+binary; // add trailing zeros
			StringBuffer sb = new StringBuffer();
			for (int q: qubits) sb.append(binary.charAt(q));
			String key = sb.toString();
		//	System.out.println(binary+" matches "+ key);
			match.put(binary,key);
			Complex val = this.toComplexArray()[i];
			double probability = val.magnitude() * val.magnitude();
			prob.put(key, prob.get(key)+probability);
		}
		
		String rand = MoreMath.randomize(prob);
		QRegister returnRegister = QRegister.ket(Integer.parseInt(rand,2), qubits.length) ;
		
		/* zerowanie amplitud */
		
		// przebiegnij przez wszystkie stany bazowe i ustal, które amplitudy trzeba wyzerować
		ArrayList<Integer> zeroAmp = new ArrayList<Integer>(); // lista amplitud do wyzerowania
		for (int i=0;i<MoreMath.pow2(this.size);i++) {
			String binary = Integer.toBinaryString(i);
			while (binary.length()<this.size) binary = "0"+binary; // add trailing zeros
			boolean matches = true;
			int p=0;
			for (int q:qubits) {
				if (binary.charAt(q) != rand.charAt(p++)) { matches=false; break;}
			}
			if (!matches) {
				zeroAmp.add(i);
			}
		}
		zeroAmplitudes(zeroAmp);
		normalize();
		
		//System.out.println(returnRegister);
		return returnRegister;
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
		//ComplexVector vector = matrix.getColumn(0);
        Complex[] array = this.toComplexArray();
		int vectorSize = MoreMath.pow2(size);
		for (int i=0;i<vectorSize;i++) {
            if (MoreMath.isNearZero(array[i].getImaginary()))
                array[i] = Complex.valueOf(array[i].getReal(), 0.0);

            if (MoreMath.isNearZero(array[i].getReal()))
                array[i] = Complex.valueOf(0.0, array[i].getImaginary());

            if (MoreMath.isNearNumber(array[i].getImaginary(), 1.0))
                array[i] = Complex.valueOf(array[i].getReal(), 1.0);

            if (MoreMath.isNearNumber(array[i].getReal(), 1.0)) {
                array[i] = Complex.valueOf(1.0, array[i].getImaginary());
            }

            if (MoreMath.isNearNumber(array[i].getImaginary(), -1.0))
                array[i] = Complex.valueOf(array[i].getReal(), -1.0);

            if (MoreMath.isNearNumber(array[i].getReal(), -1.0)) {
                array[i] = Complex.valueOf(-1.0, array[i].getImaginary());
            }

			if (array[i].getReal()==1.0&&array[i].getImaginary()==0.0) {
				if (onePos==-1) onePos = i; // 1st occurrence of 1+0j found
				else {
					onePos=-1;
					break;
				}
			}
			else if (array[i].getReal()==0.0&&array[i].getImaginary()==0.0) {
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
			Complex elem = array[i];
			// don't display (0+0j)*|ket>
			if ( !MoreMath.isNearZero(elem.getReal()) || !MoreMath.isNearZero(elem.getImaginary())) {
                if (elem.getImaginary()==0.0) {
                    if (displayedItems>0) {
                        if (elem.getReal()>=0) output.append(" +");
                        else output.append(" -");
                        output.append(Math.abs(elem.getReal()));
                    }
                    else output.append(elem.getReal());
                }
                else {
                    if (displayedItems>0) output.append(" +");
                    output.append("(").append(elem).append(")");
                }
              
                output.append("|");
				String tmpStr = Integer.toBinaryString(i) + ">";
				while (tmpStr.length()-1 < size) 
					tmpStr= "0" + tmpStr;
				output.append(tmpStr);
				displayedItems++;
			}
		}
		return output.toString();
	}
	
	public final Complex[] toComplexArray(){
		Complex[] array = new Complex[matrix.getNumberOfRows()];
		for (int i=0;i<array.length;i++) {
			array[i] = matrix.get(i, 0);
		}
		return array;
	}

    @Override
	public String toString() {
		return matrix.toString();
	}

	
	public ComplexMatrix getMatrix() {
		return matrix;
	}

    public int getSize() {
        return size;
    }
}
