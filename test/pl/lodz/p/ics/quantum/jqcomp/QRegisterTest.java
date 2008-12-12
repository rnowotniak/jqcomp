
package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.lodz.p.ics.quantum.jqcomp.qgates.CompoundQGate;
import pl.lodz.p.ics.quantum.jqcomp.qgates.Custom;
import pl.lodz.p.ics.quantum.jqcomp.qgates.ElementaryQGate;

/**
 *
 * @author rob
 */
public class QRegisterTest {

    public QRegisterTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }



    @Test
	public void testKets(){
		for (int d=1;d<3;d++) {
			System.out.println("Qubits: "+d);
			for (int ket=0;ket<MoreMath.pow2(d);ket++) {
				System.out.println("|"+ket+">:\n"+QRegister.ket(ket, d));
			}
		}
	}

    @Test
	public void testMeasurement(){
		int results[] = new int[4];
		QRegister[] b = {
				QRegister.ket(0, 2),
				QRegister.ket(1, 2),
				QRegister.ket(2, 2),
				QRegister.ket(3, 2)
		};
		for (int i=0;i<1000;i++) {
			QRegister reg = new QRegister(cx(0.5, 0), cx(0, 0.5), cx(-0.5, 0), cx(
				0, -0.5));

			reg = reg.normalize();
			reg = reg.measure();
			for (int j=0;j<4;j++) {
				if (b[j].equals(reg)) results[j]++;
			}
		}
		for (int i=0;i<results.length;i++)
			System.out.println(b[i].dirac()+": "+results[i]);
        assertEquals(1,1);
	}



    @Test
	public void test1() {
		Complex[] test = { Complex.valueOf(0, 1), Complex.valueOf(1, 0),
				Complex.valueOf(0, 0), Complex.valueOf(0, -1) };

		Complex[] test2 = { Complex.valueOf(1, 1), Complex.valueOf(1, 1) };

		QRegister reg = new QRegister(test);
		QRegister reg2 = new QRegister(test2);

		System.out.println(reg2);
		System.out.println(reg2.norm());
	}

    @Test
	public void test2() {
		println("*** test2");

		ElementaryQGate gate1 = new Custom(new Complex[][] {
				{ cx(1), cx(0) }, { cx(0), cx(1) } });

		ElementaryQGate gate2 = new Custom(new Complex[][] {
				{ cx(1), cx(1) }, { cx(0), cx(-1) } });

		QRegister register1 = new QRegister(new Complex[] { cx(1), cx(1) });

		QRegister register2 = new QRegister(new Complex[] { cx(-1), cx(0) });

		println("gate1:\n" + gate1);
		println("gate2:\n" + gate2);
		println("======================================");
		println("add:\n" + gate1.add(gate2));
		println("sub:\n" + gate1.sub(gate2));
		println("mul:\n" + gate1.mul(gate2));
		println("======================================");
		println("gate1.trace():\n" + gate1.trace());
		println("gate1.determinant():\n" + gate1.determinant());
		println("gate1.transpose():\n" + gate1.transpose());
		println("gate1.inverse():\n" + gate1.inverse());
		println("======================================");
		println("register1:\n" + register1);
		println("register2:\n" + register2);
		println("======================================");
		println("gate2.mul(register1):\n" + gate2.mul(register1));
		println("gate2.mul(register2):\n" + gate2.mul(register2));
		println("gate1.mul(register2):\n" + gate1.mul(register2));
		println("======================================");
		println("test2 done.");
	}

    @Test
	public void test3() {
		println("*** test3");

		ElementaryQGate someGate1 = new Custom(new Complex[][] {
				{ cx(1), cx(0) }, { cx(0), cx(1) } });

		ElementaryQGate someGate2 = new Custom(new Complex[][] {
				{ cx(-1), cx(1) }, { cx(-1), cx(0) } });

		QCircuit circuit1 = new QCircuit(new CompoundQGate[] {
				new CompoundQGate(new ElementaryQGate[] { someGate1, someGate2 }),
				new CompoundQGate(new ElementaryQGate[] { someGate2, someGate2 }),
				new CompoundQGate(new ElementaryQGate[] { someGate1 }), });

		println("circuit1:\n" + circuit1);
	}

    @Test
	public void testMeasure2() {
		int ones =0;
		int zeros = 0;
		for (int i=0;i<3;i++) {
			QRegister rr = new QRegister( cx(0,0.25),  cx(0.1,0),  cx(0.1,0),  cx(0.1,0), cx(0.1), cx(0.5,0),
				cx(0,-0.3), cx(0.4,0));
			rr.normalize();

			QRegister ret = rr.measure(0,1,2);
			if (ret.equals(QRegister.ket(0, 1))) zeros++;
			else if (ret.equals(QRegister.ket(1, 1))) ones++;
			System.out.println("Returned "+ret.dirac());
			System.out.println("this is "+rr);
		}



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









/*
 * Automatically generated stub methods
 */

//    /**
//     * Test of ket method, of class QRegister.
//     */
//    @Test
//    public void testKet() {
//        System.out.println("ket");
//        int n = 0;
//        int qubits = 0;
//        QRegister expResult = null;
//        QRegister result = QRegister.ket(n, qubits);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of equals method, of class QRegister.
//     */
//    @Test
//    public void testEquals() {
//        System.out.println("equals");
//        QRegister other = null;
//        QRegister instance = null;
//        boolean expResult = false;
//        boolean result = instance.equals(other);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of add method, of class QRegister.
//     */
//    @Test
//    public void testAdd() {
//        System.out.println("add");
//        QRegister that = null;
//        QRegister instance = null;
//        QRegister expResult = null;
//        QRegister result = instance.add(that);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sub method, of class QRegister.
//     */
//    @Test
//    public void testSub() {
//        System.out.println("sub");
//        QRegister other = null;
//        QRegister instance = null;
//        QRegister expResult = null;
//        QRegister result = instance.sub(other);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of inner method, of class QRegister.
//     */
//    @Test
//    public void testInner() {
//        System.out.println("inner");
//        QRegister that = null;
//        QRegister instance = null;
//        Complex expResult = null;
//        Complex result = instance.inner(that);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of norm method, of class QRegister.
//     */
//    @Test
//    public void testNorm() {
//        System.out.println("norm");
//        QRegister instance = null;
//        double expResult = 0.0;
//        double result = instance.norm();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of normalize method, of class QRegister.
//     */
//    @Test
//    public void testNormalize() {
//        System.out.println("normalize");
//        QRegister instance = null;
//        QRegister expResult = null;
//        QRegister result = instance.normalize();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of outer method, of class QRegister.
//     */
//    @Test
//    public void testOuter() {
//        System.out.println("outer");
//        QRegister that = null;
//        QRegister instance = null;
//        ComplexMatrix expResult = null;
//        ComplexMatrix result = instance.outer(that);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of reset method, of class QRegister.
//     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        QRegister instance = null;
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of tensor method, of class QRegister.
//     */
//    @Test
//    public void testTensor() {
//        System.out.println("tensor");
//        QRegister that = null;
//        QRegister instance = null;
//        QRegister expResult = null;
//        QRegister result = instance.tensor(that);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of measure method, of class QRegister.
//     */
//    @Test
//    public void testMeasure_0args() {
//        System.out.println("measure");
//        QRegister instance = null;
//        QRegister expResult = null;
//        QRegister result = instance.measure();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of measure method, of class QRegister.
//     */
//    @Test
//    public void testMeasure_intArr() {
//        System.out.println("measure");
//        int[] qubits = null;
//        QRegister instance = null;
//        QRegister expResult = null;
//        QRegister result = instance.measure(qubits);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of dirac method, of class QRegister.
//     */
//    @Test
//    public void testDirac() {
//        System.out.println("dirac");
//        QRegister instance = null;
//        String expResult = "";
//        String result = instance.dirac();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of toString method, of class QRegister.
//     */
//    @Test
//    public void testToString() {
//        System.out.println("toString");
//        QRegister instance = null;
//        String expResult = "";
//        String result = instance.toString();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getMatrix method, of class QRegister.
//     */
//    @Test
//    public void testGetMatrix() {
//        System.out.println("getMatrix");
//        QRegister instance = null;
//        ComplexMatrix expResult = null;
//        ComplexMatrix result = instance.getMatrix();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}