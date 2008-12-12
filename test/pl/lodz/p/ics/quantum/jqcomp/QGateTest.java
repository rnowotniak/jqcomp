package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.ics.quantum.jqcomp.qgates.CNot;
import pl.lodz.p.ics.quantum.jqcomp.qgates.Hadamard;
import static org.junit.Assert.*;

/**
 *
 * @author rob
 */
public class QGateTest {

    public QGateTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
	public void testGates() {
		QGate cnot = new CNot();
		QRegister reg = new QRegister(cx(0.5, 0), cx(0, 0.5), cx(-0.5, 0), cx(
				0, 0));
		println("Przed CNOT:");
		println(reg);
		println("Po CNOT: ");
		println(cnot.mul(reg));
		println("Po RevCNOT");
        
//		QRegister reg1q = new QRegister(cx(1, 0), cx(0, 0));
//		println(cnot.mul(reg1q));
	}

    @Test
	public void testHadamard() {
		println("Hadamard");
		QGate had = new Hadamard();
		println(had);
	}

    public void println(Object str) {
		System.out.println(str);
	}

	public void print(Object str) {
		System.out.print(str);
	}

    public Complex cx(double real, double imaginary) {
		return Complex.valueOf(real, imaginary);
	}

	public Complex cx(double real) {
		return cx(real, 0);
	}

}