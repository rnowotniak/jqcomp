package pl.lodz.p.ics.quantum.jqcomp.algorithms;

import pl.lodz.p.ics.quantum.jqcomp.Arbitrary;
import pl.lodz.p.ics.quantum.jqcomp.Identity;
import pl.lodz.p.ics.quantum.jqcomp.MoreMath;
import pl.lodz.p.ics.quantum.jqcomp.Not;
import pl.lodz.p.ics.quantum.jqcomp.PhaseShift;
import pl.lodz.p.ics.quantum.jqcomp.QGate;
import pl.lodz.p.ics.quantum.jqcomp.QRegister;

/**
 * Superdense coding example
 * 
 * Superdense coding allows to encode one of four classical states
 * (00, 01, 10, 11) in only one quantum bit of information.
 * What unique phenomenon exists in this operation? 
 * The measurement performed on the two-level quantum system (qubit) can yield
 * one of only two values: for e.g. horizontal or vertical polarization of photon.
 * Despite that, one of four states can be encoded and extracted from such
 * two-level quantum system, by the use of quantum channel (entangled EPR pair).
 * 
 * @author rob
 * 
 * TODO: Use Dirac notation, as soon as provided by QRegister
 * 
 */
public class Superdense {

	public static void main(String[] args) {
		
		// Two classical bits
		int b0 = 1;
		int b1 = 1;

		double s2 = Math.sqrt(2) / 2;

		// EPR pair:  sqrt(2) / 2  * ( |00> + |11> )
		QRegister qreg = new QRegister(MoreMath
				.asComplexMatrix(new double[][] { { s2, 0, 0, s2 } }));
		
		System.out.println("Initial state (should be the EPR pair");
		System.out.println(qreg);
		
		//
		// << One qubits from the pair goes to Alice, the second to Bob >>
		//
		
		// Conditional operations on the ONE qubit from the entangled pair
		if (b0 != 0) {
			qreg = (new PhaseShift(Math.PI)).next(new Identity()).compute(qreg);
		}
		if (b1 != 0) {
			qreg = (new Not()).next(new Identity()).compute(qreg);
		}

		//
		// << Alice sends her qubit to Bob >>
		//
		
		// Decoding matrix
		QGate B = new Arbitrary(MoreMath.asComplexMatrix(new double[][] {
				{ s2, 0, 0, s2 },
				{ 0, s2, s2, 0 },
				{ s2, 0, 0, -s2 },
				{ 0, -s2, s2, 0 }
		}));

		// Perform operation on both qubits
		QRegister result = B.compute(qreg);
		
		System.out.println("Final state:");
		System.out.println("(should be one of the four base vectors, according to b0, b1 bits:");
		System.out.println(result);
		
	}

}
