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

        start = QRegister.ket(5, 3); // |101>
        cnot = new CNot(2,1);
        expected = QRegister.ket(7, 3); // |111>
        assertEquals(expected,cnot.compute(start));

        start = QRegister.ket(3, 3); // |011>
        expected = QRegister.ket(3, 3); // cnot does nothing (control qubit is 0)
        assertEquals(expected,cnot.compute(start));
	}

    @Test
    public void testFredkin(){
        QGate fr =  new Fredkin();
        QRegister a = QRegister.ket(2, 3); // |010>
        assertEquals(QRegister.ket(2, 3), fr.compute(a)); // expected |010>

        a = QRegister.ket(6,3); // |110>
        assertEquals(QRegister.ket(5, 3), fr.compute(a)); // expected |101>
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

        QGate not4 = new CompoundQGate(new Not(), new Identity(3));
        b = QRegister.ket(15, 4); // |1111>
        bResult = QRegister.ket(7, 4); // |0111>
        assertEquals(bResult, not4.compute(b));
    }

    @Test
    public void testPhaseShift() {
        QGate ps = new PhaseShift(-Math.PI/2);
        QRegister a = new QRegister(Complex.ZERO, Complex.I);
        QRegister aResult = new QRegister(Complex.ZERO, Complex.ONE);
        assertEquals(aResult, ps.compute(a));

    }

   @Test
   public void testSwap() {
       QGate swap = new Swap();
       QRegister a = QRegister.ket(2, 2); // |10>
       QRegister expected = QRegister.ket(1, 2); // |01>
       assertEquals(expected, swap.compute(a));
   }

   @Test
   public void testToffoli() {
       QGate toffoli = new Toffoli();
       for (int i=0;i<4;i++) {
           QRegister a = QRegister.ket(i*2, 3); // |i>|0>
           QRegister expected = QRegister.ket(i*2 + (i==3?1:0), 3);
           assertEquals(expected, toffoli.compute(a));
       }
   }

   @Test

   public void testIfUnitary() {
       for (int i=0;i<6;i++) {
           for (int j=0;i<6;i++) {
                if (i!=j) {
                    QGate cnot = new CNot(i,j);
                    assertTrue( cnot.isUnitary());
                }
           }
       }
       assertTrue( new Toffoli().isUnitary());
       assertTrue( new Not().isUnitary());
       assertTrue( new Fredkin().isUnitary());
       assertTrue( new Identity().isUnitary());
       assertTrue( new PhaseShift(Math.PI*0.25).isUnitary());
       assertTrue( new Swap().isUnitary());
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