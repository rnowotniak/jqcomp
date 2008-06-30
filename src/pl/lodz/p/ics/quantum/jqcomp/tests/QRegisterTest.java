package pl.lodz.p.ics.quantum.jqcomp.tests;

import junit.framework.TestCase;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.junit.Before;
import org.junit.Test;

import pl.lodz.p.ics.quantum.jqcomp.MoreMath;
import pl.lodz.p.ics.quantum.jqcomp.QRegister;

public class QRegisterTest extends TestCase {

	double s2;
	
	QRegister epr;
	
	@Before
	public void setUp() throws Exception {
		s2 = Math.sqrt(2) / 2;
		epr = new QRegister(MoreMath
				.asComplexMatrix(new double[][] { { s2, 0, 0, s2 } }));
	}

	@Test
	public void testQRegisterInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testQRegisterComplexArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}

	@Test
	public void testSub() {
		fail("Not yet implemented");
	}

	@Test
	public void testInner() {
		fail("Not yet implemented");
	}

	@Test
	public void testNorm() {
		assertTrue("Not normalized state vector", epr.norm() == 1);
	}

	@Test
	public void testNormalize() {
		fail("Not yet implemented");
	}

	@Test
	public void testOuter() {
		fail("Not yet implemented");
	}

	@Test
	public void testReset() {
		QRegister qreg = new QRegister(MoreMath
				.asComplexMatrix(new double[][] { { s2, 0, 0.3, s2 } }));
		qreg.normalize();
		qreg.reset();
		ComplexMatrix matrix = qreg.getMatrix();
		assertEquals(matrix.get(0, 0), Complex.valueOf(1, 0));
		assertEquals(matrix.get(1, 0), Complex.valueOf(0, 0));
		assertEquals(matrix.get(2, 0), Complex.valueOf(0, 0));
		assertEquals(matrix.get(3, 0), Complex.valueOf(0, 0));
	}

	@Test
	public void testTensor() {
		fail("Not yet implemented");
	}

	@Test
	public void testMeasure() {
		fail("Not yet implemented");
	}

	@Test
	public void testDirac() {
		QRegister dirac01 = new QRegister(
				MoreMath.asComplexMatrix(new double[][] {{ 0, 1, 0, 0 }} ));
		QRegister dirac000 = new QRegister(
				MoreMath.asComplexMatrix(new double[][] {{ 1, 0, 0, 0, 0, 0, 0, 0 }} ));
		/*double s2i = 1 / Math.sqrt(2);
		QRegister reg = new QRegister(Complex.valueOf(s2i,0), Complex.valueOf(0,-s2i),
					Complex.valueOf(2*s2i,0), Complex.valueOf(0,0) ); */ 
		assertEquals(dirac01.dirac(),"|01>");
		assertEquals(dirac000.dirac(),"|000>");
		
	}

}
