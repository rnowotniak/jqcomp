/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.lodz.p.ics.quantum.jqcomp;
import java.util.HashMap;
import static org.junit.Assert.*;

/**
 * 
 * @author Rafal
 */
public class ProbabilityTester {

 
    // counters
    private HashMap<QRegister, Double> expected = new HashMap<QRegister,Double>();
    private HashMap<QRegister, Integer> measured = new HashMap<QRegister,Integer>();

    
    private int iterations = 5000;
    private double tolerance = 0.07;

    public ProbabilityTester() {
        
    }

    public void addExpected(QRegister str, Double probability) {
        expected.put(str, probability);
    }
    
    private void addMeasured(QRegister res) {
        Integer hits = measured.get(res);
        if (hits!=null)
             measured.put(res, hits+1);
        else
            measured.put(res,1);
    }

    private void resetCounter() {
        for (QRegister s: expected.keySet()) {
            measured.put(s, 0);
        }
    }

    public void test(QRegister arg){
        resetCounter();
        for (int i=0;i<iterations;i++){
            QRegister qr = new QRegister(arg);
            QCircuit qc = new QCircuit();
            qc.addStage(new Measurement(qr.size));
            addMeasured(qc.compute(qr));
        }
    }

    public void test(QRegister init, QCircuit circuit) {

        resetCounter();
        for (int i=0;i<iterations;i++){
            QRegister qr = new QRegister(init);
            QRegister output = circuit.compute(qr);
      //      QCircuit measurement = new QCircuit();
      //      measurement.addStage(new Measurement(qr.getSize()));
     //       output = measurement.compute(output);
            String result = output.dirac();
            addMeasured(output);
        }
    }

    public void checkExpected() {
        System.out.println(this.measured);
        for (QRegister s : expected.keySet()) {
            double prob = measured.get(s)*1.0 / iterations;
            if (Math.abs(expected.get(s)-prob) > tolerance) 
                fail("Measurement result "+s+" probability "+prob+" (expected: "+expected.get(s)+")");
        }
    }

    public void compareMode(QRegister expectedMode) {
        int max = -1;
        QRegister mode = null;
        for (QRegister s: measured.keySet()) {
            if (measured.get(s) > max) {
                max = measured.get(s);
                mode = s;
            }
        }
        if (!expectedMode.equals(mode)) fail("Most frequent result is "+mode+" (expected: "+expectedMode+")");
    }
    
}
