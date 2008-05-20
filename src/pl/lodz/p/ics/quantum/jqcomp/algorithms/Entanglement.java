package pl.lodz.p.ics.quantum.jqcomp.algorithms;

import pl.lodz.p.ics.quantum.jqcomp.CNot;
import pl.lodz.p.ics.quantum.jqcomp.ElementaryQGate;
import pl.lodz.p.ics.quantum.jqcomp.Hadamard;
import pl.lodz.p.ics.quantum.jqcomp.Identity;
import pl.lodz.p.ics.quantum.jqcomp.QCircuit;
import pl.lodz.p.ics.quantum.jqcomp.QRegister;
import pl.lodz.p.ics.quantum.jqcomp.Stage;

/* Circuit diagram:
 *
 *      --------+--   3rd qubit
 *              |
 *      --H--.--.--   2nd qubit
 *           |
 *      -----+-----   1st qubit
 */

public class Entanglement {

	public static void main(String[] args) {

		// input = |000>
		QRegister input = new QRegister(3);
		System.out.println("Initial state:");
		System.out.println(input);

		Stage s1 = new Stage(new ElementaryQGate[] { new Identity(),
				new Hadamard(), new Identity() });
		Stage s2 = new Stage(
				new ElementaryQGate[] { new Identity(), new CNot() });
		Stage s3 = new Stage(new ElementaryQGate[] { new CNot(0, 1),
				new Identity() });

		QCircuit qcirc = new QCircuit(new Stage[] { s1, s2, s3 });
		System.out.println("Final state:");
		System.out.println(qcirc.execute(input));
	}

}
