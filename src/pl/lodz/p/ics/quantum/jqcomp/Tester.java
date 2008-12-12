package pl.lodz.p.ics.quantum.jqcomp;

import pl.lodz.p.ics.quantum.jqcomp.qgates.Custom;
import pl.lodz.p.ics.quantum.jqcomp.qgates.CompoundQGate;
import pl.lodz.p.ics.quantum.jqcomp.qgates.ElementaryQGate;
import pl.lodz.p.ics.quantum.jqcomp.qgates.CNot;
import pl.lodz.p.ics.quantum.jqcomp.qgates.Hadamard;
import org.jscience.mathematics.number.Complex;

public class Tester {
	
	
	
	public static void main(String[] args) {
		
	
	
		
	
	//	testMeasurement();
	//	testKets();
	//	test1();
	//	test2();
	//	test3();
	//	testGates();
	//	testHadamard();
//		testMeasure2();
	}

	public static void testKets(){
		for (int d=1;d<3;d++) {
			System.out.println("Qubits: "+d);
			for (int ket=0;ket<MoreMath.pow2(d);ket++) {
				System.out.println("|"+ket+">:\n"+QRegister.ket(ket, d));
			}
		}
	}
	
	public static void testMeasurement(){
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
	}
	
	public static void testGates() {
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

	public static void testHadamard() {
		println("Hadamard");
		QGate had = new Hadamard();
		println(had);
	}

	public static void test1() {
		Complex[] test = { Complex.valueOf(0, 1), Complex.valueOf(1, 0),
				Complex.valueOf(0, 0), Complex.valueOf(0, -1) };

		Complex[] test2 = { Complex.valueOf(1, 1), Complex.valueOf(1, 1) };

		QRegister reg = new QRegister(test);
		QRegister reg2 = new QRegister(test2);

		System.out.println(reg2);
		System.out.println(reg2.norm());
	}

	public static void test2() {
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

	public static void test3() {
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
	
	public static void testMeasure2() {
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

	public static Complex cx(double real, double imaginary) {
		return Complex.valueOf(real, imaginary);
	}

	public static Complex cx(double real) {
		return cx(real, 0);
	}

	public static void println(Object str) {
		System.out.println(str);
	}

	public static void print(Object str) {
		System.out.print(str);
	}
}
