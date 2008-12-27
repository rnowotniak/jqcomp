package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.ics.quantum.jqcomp.qgates.*;
import static org.junit.Assert.*;

/**
 *
 * @author rob
 */
public class QGateTest {
    static private double s2 = 1/Math.sqrt(2);
    public QGateTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
	public void testCNOT() {
		QGate cnot = new CNot();
		QRegister start = QRegister.ket(1,1).tensor(QRegister.ket(1,1)); // control: 1, target: 1
        QRegister expected = QRegister.ket(1, 1).tensor(QRegister.ket(0, 1)); // control flips the target
        assertEquals(expected,cnot.compute(start) ); // expected: |10>

        cnot = new CNot(0,1);
        expected = QRegister.ket(1,2); // |01>

        assertEquals(expected,cnot.compute(start));

        start = QRegister.ket(2, 2); //|10>: control: 0, target 1
        expected = QRegister.ket(2,2); //|10>
        assertEquals(expected,cnot.compute(start));
	}



    @Test
	public void testHadamard() {
        QGate had = new Hadamard();
        QRegister a = QRegister.ket(1,1); // |1>
        QRegister ha = new QRegister(cx(s2), cx(-s2));
        assertEquals(ha, had.compute(a) );

        // Identity: H*H = I
        QRegister b = new QRegister(cx(-0.4,0.8), cx(0.1,0.1));
        b.normalize();
        QGate hadhad = had.mul(had);
        assertEquals(hadhad.compute(b), b);
        
	}


    @Test
    public void testNOT(){
        QGate not = new Not();
        QRegister a = QRegister.ket(0, 1); // |0>
        assertEquals(QRegister.ket(1, 1), not.compute(a)); // expected |1>

        QRegister b = new QRegister(cx(-0.5,0), cx(0,0.5));
        QRegister bResult = new QRegister(cx(0,0.5), cx(-0.5,0));
        assertEquals(bResult, not.compute(b));
    }

    @Test
    public void testPhaseShift() {
        QGate ps = new PhaseShift(-Math.PI/2);
        QRegister a = new QRegister(Complex.ZERO, Complex.I);
        QRegister aResult = new QRegister(Complex.ZERO, Complex.ONE);
        assertEquals(aResult, ps.compute(a));

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