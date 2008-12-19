
package pl.lodz.p.ics.quantum.jqcomp;

/**
 *
 * @author rob
 */
public class Measurement implements Stage {

    private int size;
    private int[] qubits;


    /**
     * Create `gate' which measures all qubits
     * @param size
     */
    public Measurement(int size)
    {
        this.qubits = new int[3];
        for (int i=0;i<size;i++) {
            qubits[i]=i;
        }
        this.size = size;
    }
   
    public Measurement(int size, int ...qubits) {
        this.qubits = qubits.clone();
   
        this.size = size;
    }

    public QRegister compute(QRegister q) {
       return q.measure(this.qubits);
    }

    public int getSize() {
        return size;
    }

}
