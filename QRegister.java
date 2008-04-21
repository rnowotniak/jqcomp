package pl.lodz.p.ics.quantum.jqcomp;


import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;
import org.jscience.mathematics.vector.DimensionException;

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
	 
	 public QRegister(ComplexMatrix initialValues){
		 if (initialValues.getNumberOfColumns()!=1)
			 throw new DimensionException("Input matrix must have 1 column.");
		 this.matrix = initialValues;
		 size = matrix.getNumberOfRows();
	 }
	 	 
	 /** Addition */
	public QRegister add(QRegister that) {
		  return new QRegister(this.matrix.plus(that.matrix));
	 }
	 
	 /** Inner product  */ 
	 public Complex inner(QRegister that) {
		 // may raise DimensionException
		 return MoreMath.ConjugateTranspose(this.matrix).times(that.matrix).get(0, 0);
	 }
	 
	 /** Norm = sqrt( <this|this> ) */
	 public double norm(){
		 double ret = this.inner(this).getReal();
		 System.out.println(this.inner(this));
		 return Math.sqrt(ret);
	 }
	 
	 /** Outer product */
	 public ComplexMatrix outer(QRegister that) {
		 // may raise DimensionException
		 return this.matrix.times(that.matrix.transpose());
	 }
	 
	 /** Set [1, 0, 0, ..., 0] */
	 public void reset() {
		 //TODO
	 }
	 
	 /** Tensor (Kronecker) product */
	 public QRegister tensor(QRegister that) {
		 return new QRegister(this.matrix.tensor(that.matrix));
	 }
	 
	 public String toString() {
		 return matrix.toString();
	 }
}
