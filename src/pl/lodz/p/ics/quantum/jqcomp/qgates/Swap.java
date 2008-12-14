package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

/**
 *
 * @author rob
 */
public class Swap extends ElementaryQGate {

    public Swap() {
        size = 2;
        matrix = ComplexMatrix.valueOf(new Complex[][] {
            {cx(1), cx(0), cx(0), cx(0)},
            {cx(0), cx(0), cx(1), cx(0)},
            {cx(0), cx(1), cx(0), cx(0)},
            {cx(0), cx(0), cx(0), cx(1)},
        });
    }

}
