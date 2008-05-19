package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;


public class Not extends AbstractQGate {
	public Not() {
		this.matrix = ComplexMatrix.valueOf(new Complex[][] {
				{cx(0), cx(1)},
				{cx(1), cx(0)}
		});
		
		this.size = 1; 
		/* dochodzę do wniosku, że size powinien oznaczać
		 * ilosc kubitow, na ktorych operuje bramka, a nie wymiar macierzy
		 */
	}
}
