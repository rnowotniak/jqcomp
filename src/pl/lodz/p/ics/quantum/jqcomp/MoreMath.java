package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

public class MoreMath {
	static public double log2(double x){
		return Math.log10(x)/Math.log10(2);
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
}
