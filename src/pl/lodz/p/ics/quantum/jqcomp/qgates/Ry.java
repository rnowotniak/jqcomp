/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.lodz.p.ics.quantum.jqcomp.qgates;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

/**
 *
 * @author Rafal
 */
public class Ry extends ElementaryQGate {
   private final double angle;
    public Ry(double angle) {
        this.angle = angle;
        this.size = 1;
        double t = angle / 2;
        this.matrix = ComplexMatrix.valueOf(new Complex[][] {
				{cx(Math.cos(t)), cx(-Math.sin(t))},
				{cx(-Math.sin(t)), cx(Math.cos(t))}
		});
    }
}

