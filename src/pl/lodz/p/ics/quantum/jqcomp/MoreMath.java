package pl.lodz.p.ics.quantum.jqcomp;

import java.util.HashMap;
import java.util.Set;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

public class MoreMath {
	
	static public final double epsilon = 1e-5;
	
	/** 
	 * Compute the binary logarithm of a positive integer x.
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

    static public boolean isNearNumber(double x, double y) {
        return Math.abs(x-y) < epsilon;
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
}
