
package pl.lodz.p.ics.quantum.jqcomp;

/**
 *
 * @author rob
 */
public interface Stage {

    public QRegister compute(QRegister q);

    public int getSize();
    
}
