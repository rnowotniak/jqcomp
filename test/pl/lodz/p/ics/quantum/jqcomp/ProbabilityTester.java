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
    private HashMap<String, Double> expected = new HashMap<String,Double>();
    private HashMap<String, Integer> measured = new HashMap<String,Integer>();

    
    private int iterations = 5000;
    private double tolerance = 0.07;

    public ProbabilityTester() {
        
    }

    public void addExpected(String str, Double probability) {   
        expected.put(str, probability);
    }
    
    private void addMeasured(String res) {
        Integer hits = measured.get(res);
        if (hits!=null)
             measured.put(res, hits+1);
        else
            measured.put(res,1);
    }

    private void resetCounter() {
        for (String s: expected.keySet()) {
            measured.put(s, 0);
        }
    }

    public void test(QRegister arg){
        resetCounter();
        for (int i=0;i<iterations;i++){
            QRegister qr = new QRegister(arg);
            String result = qr.measure().dirac();
            addMeasured(result);
        }
    }

    public void test(QRegister init, QCircuit circuit) {

        resetCounter();
        for (int i=0;i<iterations;i++){
            QRegister qr = new QRegister(init);
            QRegister output = circuit.compute(qr);
            String result = output.measure().dirac();
            addMeasured(result);
        }
    }

    public void checkExpected() {
        for (String s : expected.keySet()) {
            double prob = measured.get(s)*1.0 / iterations;
            if (Math.abs(expected.get(s)-prob) > tolerance) 
                fail("Measurement result "+s+" probability "+prob+" (expected: "+expected.get(s)+")");
        }
    }

    public void compareMode(String expectedMode) {
        int max = -1;
        String mode = "";
        for (String s: measured.keySet()) {
            if (measured.get(s) > max) {
                max = measured.get(s);
                mode = s;
            }
        }
        if (!expectedMode.equals(mode)) fail("Most frequent result is "+mode+" (expected: "+expectedMode+")");
    }
    
}
