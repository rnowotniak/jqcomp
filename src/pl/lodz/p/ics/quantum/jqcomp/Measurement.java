
package pl.lodz.p.ics.quantum.jqcomp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.jscience.mathematics.number.Complex;
/**
 *
 * @author rob
 */
public class Measurement implements Stage {

    private int gateSize;
    private int[] qubits;
    private QRegister returnRegister;

    /**
     * Create `gate' which measures all qubits
     * @param gateSize
     */
    public Measurement(int size)
    {
        this.qubits = new int[size];
        for (int i=0;i<size;i++) {
            qubits[i]=i;
        }
        this.gateSize = size;
    }


    public Measurement(int size, int ...qubits) {
        this.qubits = qubits.clone();
        // TODO: exception if qubits out of range
        this.gateSize = size;
    }

    

    public QRegister compute(QRegister reg) {

		Arrays.sort(qubits);
		int probLen = MoreMath.pow2(qubits.length);
        Complex[] amp = reg.toComplexArray();
        int dim = MoreMath.pow2(reg.getSize());
        int size = reg.getSize();
		HashMap<String, Double> prob = new HashMap<String, Double>(probLen);
		HashMap<String, String> match = new HashMap<String, String>(dim);
		for (int i=0;i<probLen;i++) {
			String binary = Integer.toBinaryString(i);
			while (binary.length()<qubits.length) binary = "0"+binary;
			prob.put(binary, 0.0);
		}

		for (int i=0;i<dim;i++) {
			String binary = Integer.toBinaryString(i);
			while (binary.length()<size) binary = "0"+binary; // add trailing zeros
			StringBuffer sb = new StringBuffer();
			for (int q: qubits) sb.append(binary.charAt(size-q-1));
			String key = sb.toString();
	//		System.out.println(binary+" matches "+ key);
			match.put(binary,key);
			
			double probability = amp[i].magnitude() * amp[i].magnitude();
			prob.put(key, prob.get(key)+probability);
		}

        for (String z: prob.keySet()) {
    //        System.out.println("for "+z+" probability "+prob.get(z));
        }
		String rand = MoreMath.randomize(prob);
		this.returnRegister = QRegister.ket(Integer.parseInt(rand,2), qubits.length) ;

		for (int i=0;i<dim;i++) {
			String binary = Integer.toBinaryString(i);
			while (binary.length()<size) binary = "0"+binary; // add trailing zeros
			boolean matches = true;
			int p=0;
			for (int q:qubits) {
				if (binary.charAt(size-1-q) != rand.charAt(p++)) { matches=false; break;}
			}
			if (!matches) {
				//zeroAmp.add(i);
                amp[i]=Complex.ZERO;
			}
		}

        reg = new QRegister(amp);
        reg.normalize();

		return reg;
    }

    public int getSize() {
        return gateSize;
    }


    /** Increase the size of the 'gate' by 1 */
    public void expand() {
        for (int i=0;i<qubits.length;i++) qubits[i]++;
        this.gateSize++;
    }

    /** Decrease the size of the 'gate' by 1 by removing |0>| */
    public void shrink() {
        if (gateSize<=1) throw new RuntimeException("Cannot shrink: size<=1");
        //if (qubits.length==1 && )

        for (int i=0;i<qubits.length;i++) qubits[i]--;
        this.gateSize--;
        if (qubits[0]==-1) {
            qubits = Arrays.copyOfRange(qubits, 1, qubits.length);
        }
    }

    public int[] getMeasuredQubits() {
        return this.qubits;
    }

}
