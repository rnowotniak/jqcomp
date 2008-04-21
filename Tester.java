package pl.lodz.p.ics.quantum.jqcomp;

import org.jscience.mathematics.number.Complex;

public class Tester {

	 public static void main(String []args){
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
		 System.out.println(reg2.inner(reg2));
		 System.out.println(reg2.norm());
		 
	 }
}
