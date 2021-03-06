package pl.lodz.p.ics.quantum.jqcomp;

import java.util.HashMap;
import java.util.Set;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;

public class MoreMath {

    /** A very small positive number */
	static public final double epsilon = 1e-7;

    /**
     * Compares two matrices.
     * @param a complex matrix
     * @param b complex matrix
     * @return true if both matrices have equal number of rows and columns and
     * difference between the corresponding entries is lesser than epsilon;
     * otherwise returns false
     */
    static public boolean isNearMatrix(ComplexMatrix a, ComplexMatrix b) {
        if (a.getNumberOfColumns()!= b.getNumberOfColumns())
            return false;
        if (a.getNumberOfRows() != b.getNumberOfRows())
            return false;
        for (int i=0;i<a.getNumberOfRows();i++)
            for (int j=0;j<b.getNumberOfColumns();j++) {
                // MoreMath.epsilon - tolerance
                if (!a.get(i, j).equals(b.get(i, j), MoreMath.epsilon))
                    return false;
            }
        return true;
    }

	/** 
	 * Compute the binary logarithm of a positive real number.
	 * @param x
	 * @return the base 2 logarithm of x
	 */
	static public double log2(double x) {
		return Math.log10(x) / Math.log10(2);
	}

	/**
	 * Compute the n-th power of 2
	 * 
	 * @param n
	 *            exponent (nonnegative integer)
	 * @return 2**n
	 */
	static public int pow2(int n) {
		if (n < 0 || n > 31) {
			throw new ArithmeticException("Exponent out of range");
		}
		return 1 << n;
	}

	/** Transpose and complex conjugate */
	static public ComplexMatrix ConjugateTranspose(ComplexMatrix H) {
		Complex[][] C = new Complex[H.getNumberOfRows()][H.getNumberOfColumns()];
		for (int r = 0; r < H.getNumberOfRows(); r++)
			for (int c = 0; c < H.getNumberOfColumns(); c++) {
				C[r][c] = H.get(r, c).conjugate();
			}
		ComplexMatrix ret = ComplexMatrix.valueOf(C);
		return ret.transpose();
	}

	/**
	 * Convert 2d real values matrix into ComplexMatrix object
	 * 
	 * @param matrix
	 *            2d matrix of real values
	 * @return
	 */
	static public ComplexMatrix asComplexMatrix(double[][] matrix) {
		Complex[][] celements = new Complex[matrix.length][];
		for (int i = 0; i < matrix.length; i++) {
			celements[i] = new Complex[matrix[i].length];
			for (int j = 0; j < matrix[i].length; j++) {
				celements[i][j] = Complex.valueOf(matrix[i][j], 0);
			}
		}
		return ComplexMatrix.valueOf(celements);
	}
	
	
	/** 
	 * Check if |x| is lesser than epsilon. 
	 * @param x 	real number
	 * */
	static public boolean isNearZero(double x){
		return Math.abs(x)<epsilon;
	}

    /**
     * @param x
     * @param y
     * @return true if the distance between x and y is lesser than epsilon
     */
    static public boolean isNearNumber(double x, double y) {
        return Math.abs(x-y) < epsilon;
    }

    static public boolean isNearNumber(Complex z1, Complex z2) {
        return z1.equals(z2, epsilon);
    }
	
	public  static String randomize(HashMap<String, Double> probabilities) {
		Set<String> keys = probabilities.keySet();
		double r = Math.random();
		double sum = 0.0;
		for (String s: keys) {
			sum += probabilities.get(s);
			if (r<sum) return s;
		}
		return "" ;// todo;
	}

    /**
     * Project v onto u.
     * @param v
     * @param u
     * @return the projection
     */
    public static ComplexVector project(ComplexVector v, ComplexVector u){
        Complex uu = u.times(u); // dot product
        Complex uv = u.times(v);
        return u.times(uv.divide(uu));
    }

    /**
     * Return a set of orthonormal vectors obtained by the Gram-Schmidt process.
     * @param inputSet linearly independent set of vectors
     * @return orthonormal set of vectors provided inputSet is linearly independent
     */
    public static ComplexVector[] GramSchmidtOrthonormalization(ComplexVector[] inputSet) {

        for (int i=0;i<inputSet.length;i++) {
            if (inputSet[i].getDimension() != inputSet.length) {
                throw new WrongSizeException("Invalid input vector length");
            }
        }
        int vectors = inputSet.length;
        Complex[] zero = new Complex[vectors];
        for (int i=0;i<vectors;i++) {
            zero[i] = Complex.ZERO;
        }

        ComplexVector u[] = new ComplexVector[vectors];
        ComplexVector n[] = new ComplexVector[vectors]; // normalized u
        for (int i=0;i<vectors;i++){
            n[i] = ComplexVector.valueOf(zero);
        }
        for (int i=0;i<vectors;i++){
            u[i] = inputSet[i];
            for (int j=0;j<i;j++) {
                u[i] = u[i].minus(project(inputSet[i],u[j]));
            }
            // store normalized value in n
            Complex norm = u[i].norm();
            if (!norm.equals(Complex.ZERO) ) n[i] = u[i].times(Complex.ONE.divide(norm));
            else {
                n[i] = u[i];
                break;
            }
        }
        return n;
    }

    public static ComplexMatrix GramSchmidtOrthonormalization(ComplexMatrix matrix) {
        int rows = matrix.getNumberOfRows();
        ComplexVector[] r = new ComplexVector[rows];
        for (int i=0;i<rows;i++) {
            r[i] = matrix.getRow(i);
        }
        ComplexMatrix ret = ComplexMatrix.valueOf(MoreMath.GramSchmidtOrthonormalization(r));
        return ret;
    }

    private MoreMath() {}

}
