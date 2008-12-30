/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.lodz.p.ics.quantum.jqcomp.algorithms;

import pl.lodz.p.ics.quantum.jqcomp.*;
import pl.lodz.p.ics.quantum.jqcomp.qgates.*;
import org.jscience.mathematics.number.Complex;
/**
 * Grover's algorithm
 * @author Rafal
 */
public class Grover {
    final public QCircuit circuit;
    final public QRegister init;
    
    /**
     * Prepare gates
     *
     * @param n
     * @param search the search element 0<=search<n
     */
    public Grover(final int n, int search) {
        int dim = (int) MoreMath.log2(n);
        Complex[] initArray = new Complex[n];
        int steps = (int)(Math.floor(Math.PI/Math.asin(Math.sqrt(1.0/n))/4.0));
        for (int i=0;i<initArray.length; i++) {
            initArray[i] = Complex.valueOf( 1.0 / Math.sqrt(n), 0.0);
        }
        init = new QRegister(initArray);
        QRegister w0 = QRegister.ket(search, dim);
        QGate A = w0.outer(w0).mul(Complex.valueOf(2.0,0));
     
        A = new Identity(dim).sub(A);
        QGate B = init.outer(init).mul(Complex.valueOf(2.0,0)) ;
  
        B = B.sub(new Identity(dim));
    
        circuit = new QCircuit();
        for (int i=0;i<steps;i++) {
            circuit.addStage(A);
            circuit.addStage(B);
        }
        Stage measure = new Measurement(dim);
        circuit.addStage(measure);
    }

    static public void main(String[] args) {
        Grover g = new Grover(8,1);
        QRegister result = g.circuit.compute(g.init);


    }
}
