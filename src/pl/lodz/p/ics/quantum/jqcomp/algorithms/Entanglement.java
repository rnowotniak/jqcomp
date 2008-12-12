package pl.lodz.p.ics.quantum.jqcomp.algorithms;

import pl.lodz.p.ics.quantum.jqcomp.qgates.CNot;
import pl.lodz.p.ics.quantum.jqcomp.qgates.ElementaryQGate;
import pl.lodz.p.ics.quantum.jqcomp.qgates.Hadamard;
import pl.lodz.p.ics.quantum.jqcomp.qgates.Identity;
import pl.lodz.p.ics.quantum.jqcomp.QCircuit;
import pl.lodz.p.ics.quantum.jqcomp.QRegister;
import pl.lodz.p.ics.quantum.jqcomp.qgates.CompoundQGate;

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

		CompoundQGate s1 = new CompoundQGate(new ElementaryQGate[] { new Identity(),
				new Hadamard(), new Identity() });
		CompoundQGate s2 = new CompoundQGate(
				new ElementaryQGate[] { new Identity(), new CNot() });
		CompoundQGate s3 = new CompoundQGate(new ElementaryQGate[] { new CNot(0, 1),
				new Identity() });

		QCircuit qcirc = new QCircuit(new CompoundQGate[] { s1, s2, s3 });
		System.out.println("Final state:");
		System.out.println(qcirc.compute(input));
        System.out.println(qcirc.compute(input).dirac());
	}

}
