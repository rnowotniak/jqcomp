package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;

public class Tester {
	 public static void main(String []args){
		 //test1();
		 test2();
		 test3();
	 }
	 
	 public static void test1() {
		 Complex[] test = {Complex.valueOf(0,1) ,
		 	Complex.valueOf(1,0),
		 	Complex.valueOf(0,0),
		 	Complex.valueOf(0,-1)
		 };
		 
		 Complex[] test2 = { Complex.valueOf(1,1),
		 	Complex.valueOf(1,1)
		 };
		 
		 QRegister reg = new QRegister(test);
		 QRegister reg2 = new QRegister(test2);
		 
		 System.out.println(reg2);
		 System.out.println(reg2.norm());
	 }
	 
	 public static void test2() {		
		 println("*** test2");
		 
		 QGate gate1 = new QGate(new Complex[][]{
				 {cx(1), cx(0)},
				 {cx(0), cx(1)}
		 });		 
		 
		 QGate gate2 = new QGate(new Complex[][]{
				 {cx(1), cx(1)},
				 {cx(0), cx(-1)}
		 });
		 
		 QRegister register1 = new QRegister(new Complex[]{
				 cx(1), cx(1)
		 });
		 
		 QRegister register2 = new QRegister(new Complex[]{
				 cx(-1), cx(0)
		 });		 
		 
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
		 
		 QGate someGate1 = new QGate(new Complex[][]{
				 {cx(1), cx(0)},
				 {cx(0), cx(1)}
		 });	
		 
		 QGate someGate2 = new QGate(new Complex[][]{
				 {cx(-1), cx(1)},
				 {cx(-1), cx(0)}
		 });
		 
		 QCircuit circuit1 = new QCircuit(new Stage[] {
			new Stage(new QGate[] {someGate1, someGate2}),
			new Stage(new QGate[] {someGate2, someGate2}),
			new Stage(new QGate[] {someGate1}),
		 });
		 
		 println("circuit1:\n" + circuit1);
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
