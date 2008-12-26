package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.number.Complex;



public class PhaseShift extends ElementaryQGate {
	public PhaseShift(double angle) {
		this.angle = angle;
        this.matrix = ComplexMatrix.valueOf(new Complex[][] {
				{cx(1), cx(0)},
				{cx(0), cx(0, angle * 1).exp()}
		});

		this.size = 1;
	}

    /**
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }    

	private final double angle;
}
