package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

public class MoreMath {
	static public double log2(double x){
		return Math.log10(x)/Math.log10(2);
	}
	
	/** Return 2**x, x is a nonnegative integer */
	static public int pow2(int exp) {
		if (exp < 0 || exp > 31 ) {
			throw new ArithmeticException();
		}
		return 1<<exp;
	}
	
	/** Transpose and complex conjugate */
	static public ComplexMatrix ConjugateTranspose(ComplexMatrix H) {
		Complex[][] C = new Complex[H.getNumberOfRows()][H.getNumberOfColumns()];
		for (int r=0;r<H.getNumberOfRows();r++) 
			for (int c=0;c<H.getNumberOfColumns();c++) {
				C[r][c]=H.get(r,c).conjugate();
		}
		ComplexMatrix ret = ComplexMatrix.valueOf(C);
		return ret.transpose();
	}
	
	/**
	 * Convert 2d real values matrix into ComplexMatrix object 
	 * @param matrix 2d matrix of real values 
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
}
