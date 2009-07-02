/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.lodz.p.ics.quantum.jqcomp.qgates.Custom;
import static org.junit.Assert.*;
import static pl.lodz.p.ics.quantum.jqcomp.MoreMath.*;

/**
 *
 * @author Rafal
 */
public class MoreMathTest {

    private static Complex c1 = Complex.ONE;
    private static Complex c0 = Complex.ZERO;

    public MoreMathTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void orthonormalization(){
        ComplexVector[] v = new ComplexVector[] {
            ComplexVector.valueOf(Complex.valueOf(-2,-2),c0,c0,c0),
            ComplexVector.valueOf(Complex.valueOf(-2,-2),c0,c0,c1),
            ComplexVector.valueOf(c0,c1,c0,c1),
            ComplexVector.valueOf(c1,c1,c1,c1)
        };
        ComplexVector[] u = MoreMath.GramSchmidtOrthonormalization(v);
        Custom gate = new Custom(ComplexMatrix.valueOf(u));
        assertTrue(gate.isUnitary());
    }

      @Test
        public void orthonormalization2(){
        ComplexVector[] v = new ComplexVector[] {
            ComplexVector.valueOf(Complex.I,c0),
            ComplexVector.valueOf(Complex.I,Complex.I.inverse()),
        };
        ComplexVector[] u = MoreMath.GramSchmidtOrthonormalization(v);
        Custom gate = new Custom(ComplexMatrix.valueOf(u));
        assertTrue(gate.isUnitary());
    }

      @Test
      public void projection(){
         ComplexVector[] v = new ComplexVector[] {
            ComplexVector.valueOf(Complex.valueOf(2.5, 1),c1),
            ComplexVector.valueOf(c1,c0)
        };
        assertEquals(ComplexVector.valueOf(Complex.valueOf(2.5, 1),c0), project(v[0], v[1]));
      }

}