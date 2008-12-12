
package pl.lodz.p.ics.quantum.jqcomp;

/**
 *
 * @author rob
 */
public class Measurement implements Stage {

    int size;

    public QRegister compute(QRegister q) {
        throw new UnsupportedOperationException("Not supported yet.");
        /* use q.measurement(...) here */
    }

    public int getSize() {
        return size;
    }

}
