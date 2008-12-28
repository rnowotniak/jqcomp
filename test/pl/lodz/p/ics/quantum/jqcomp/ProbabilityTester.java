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
    }

    private void resetCounter() {
        for (String s: expected.keySet()) {
            measured.put(s, 0);
        }
    }

    public boolean test(QRegister arg){
        resetCounter();
        for (int i=0;i<iterations;i++){
            QRegister qr = new QRegister(arg);
            String result = qr.measure().dirac();
            addMeasured(result);
        }
        for (String s : expected.keySet()) {
            double prob = measured.get(s)*1.0 / iterations;
            if (Math.abs(expected.get(s)-prob) > tolerance) 
                fail("Measurement result "+s+" probability "+prob+" (expected: "+expected.get(s)+")");
        }
        return true;
    }
    
}
