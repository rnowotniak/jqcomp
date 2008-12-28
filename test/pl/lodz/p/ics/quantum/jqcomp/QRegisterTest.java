package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.lodz.p.ics.quantum.jqcomp.qgates.*;

/**
 *
 * @author rob
 */
public class QRegisterTest {

    private final static double s2 = 1.0 / Math.sqrt(2);

    public QRegisterTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testKets() {
        for (int d = 1; d < 16; d++) {
            QRegister k = QRegister.ket(d, 4);
            assertTrue(MoreMath.isNearNumber(1.0,k.norm()));
        }
    }

    @Test
    public void testComparator() {
        QRegister a = QRegister.ket(1, 1); // |1>
        QRegister b = new QRegister(cx(0), cx(1));
        assertEquals(a,b);
        b = new QRegister(cx(0), cx(0,1));
        assertFalse(a.equals(b));
        b = new QRegister(cx(0+MoreMath.epsilon*2), cx(1));
        assertFalse(a.equals(b));
    }




    @Test
    public void testGateOperations() {

        ElementaryQGate gate1 = new Hadamard();

        ElementaryQGate gate2 = new Custom(new Complex[][]{
                    {cx(0,1), cx(0)},
                    {cx(0), cx(-1,0)}});

        ElementaryQGate gate3 = new Custom(new Complex[][]{
                    {cx(-1,0), cx(0)},
                    {cx(0), cx(0,-1)}});

        
        ElementaryQGate zero = new Custom(new Complex[][]{ //zero doesn't have inverse
            {cx(0), cx(0)},
            {cx(0), cx(0)}
        });
        
        QGate ident = new Identity(2);

        assertTrue( MoreMath.isNearNumber(gate2.determinant(),  cx(0,-1))); // -i
        assertTrue( MoreMath.isNearNumber(gate1.determinant(),  cx(-1,0)));

        QGate product = new Custom(new Complex[][]{
                    {cx(0,-1), cx(0)},
                    {cx(0), cx(0,1)}});

        assertEquals(product, gate2.mul(gate3));
        
        // checking identity: G*I = I*G = G
        assertEquals(gate1, ident.mul(gate1));
        assertEquals(gate1, gate1.mul(ident));
 
    }

    @Test
    public void test3() {


        ElementaryQGate someGate1 = new Custom(new Complex[][]{
                    {cx(1), cx(0)}, {cx(0), cx(1)}});

        ElementaryQGate someGate2 = new Custom(new Complex[][]{
                    {cx(-1), cx(0)}, {cx(0), cx(1)}});

        QCircuit circuit1 = null;
        try {
            circuit1 = new QCircuit(new CompoundQGate[]{
                        new CompoundQGate(new ElementaryQGate[]{someGate1, someGate2}),
                        new CompoundQGate(new ElementaryQGate[]{someGate2, someGate2}),
                        new CompoundQGate(new ElementaryQGate[]{someGate1}),});
            fail("Creation of quantum circuit composing different size stages shouldn't be possible");
        } catch (WrongSizeException ex) {
            /* DO NOTHING, exception is correct */
        }
    }

    @Test
    public void testDirac() {
        QRegister a = QRegister.ket(0, 2);
        assertEquals("|00>", a.dirac());
        QRegister b = new QRegister(cx(0), cx(1.0 + 1.0e-8));
        assertEquals("|1>" , b.dirac());
        QRegister c = new QRegister(cx(1.0 - 1.0e-8), cx(0), cx(1.0e-8), cx(0));
        assertEquals("|00>", c.dirac());
        QRegister d = new QRegister(cx(1.0), cx(0, -1e-8));
        assertEquals("|0>", d.dirac());
        QRegister e = new QRegister(cx(0), cx(0.5), cx(0), cx(0.75), cx(0), cx(0), cx(0), cx(0));

        ComplexMatrix cm = ComplexMatrix.valueOf(new Complex[][]{{
            cx(0), cx(0), cx(0), cx(0),
            cx(0), cx(0), cx(0), cx(0),
            cx(0), cx(0), cx(0), cx(0),
            cx(0), cx(0), cx(0), cx(-1.0000000002, 0)}});
        cm = cm.transpose();
        QRegister f = new QRegister(cm);
        assertEquals("-1.0|1111>", f.dirac()); // or -1 * |1111> ?
    }

    @Test
    public void testMeasure2() {
        QRegister a = QRegister.ket(0, 3);
        QGate had4 = new Hadamard(3);
        a = had4.compute(a);
        ProbabilityTester pt = new ProbabilityTester();
        for (int i=0;i<8;i++)
            pt.addExpected(QRegister.ket(i, 3).dirac(),1.0/8);
         pt.test(a);
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