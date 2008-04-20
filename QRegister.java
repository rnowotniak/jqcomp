package pl.lodz.p.ics.quantum.jqcomp;


import org.jscience.mathematics.number.*;
import org.jscience.mathematics.vector.*;

class MoreMath {
	static public double log2(double x){
		return Math.log10(x)/Math.log10(2);
	}
}

public class QRegister {
	 private int size;
	 private ComplexMatrix matrix;
	 public QRegister(Complex[] initialValues) {
		 ComplexVector vector = ComplexVector.valueOf(initialValues);
		 this.size = vector.getDimension();
		 matrix = ComplexMatrix.valueOf(vector).transpose();
	 }
	 public QRegister(int dim) {
		 // TODO check dimensions
		 ComplexVector vector;
		 Complex[] initialValues = new Complex[dim];
		 initialValues[0] = Complex.valueOf(1,0);
		 for (int i=1;i<dim;i++)
			 initialValues[i] = Complex.valueOf(0, 0); 
		 vector = ComplexVector.valueOf(initialValues);
		 matrix = ComplexMatrix.valueOf(vector);
		 size = dim;
	 }
	 
	 public int measure() {return 0;}
	 
	 
	 // X + Y
	 //public QRegister add(QRegister that) {
		 // TODO wrong size exception
		 // TODO QRegister( ComplexMatrix )
		 // return this.matrix.plus(that.matrix);
	 //}
	 
	 
	 //public String dirac();
	 
	 /** Inner product  */ 
	 public Complex inner(QRegister that) {
		 // may raise DimensionException
		 return this.matrix.transpose().times(that.matrix).get(0, 0);
	 }
	 
	 /** Outer product */
	 public ComplexMatrix outer(QRegister that) {
		 // may raise DimensionException
		 return this.matrix.times(that.matrix.transpose());
	 }
	 
	 // 
	 
	 public String toString() {
		 return matrix.toString();
	 }
	
}
