/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.lodz.p.ics.quantum.jqcomp.algorithms;
import org.jscience.mathematics.number.Complex;
import pl.lodz.p.ics.quantum.jqcomp.qgates.*;
import pl.lodz.p.ics.quantum.jqcomp.*;
import pl.lodz.p.ics.quantum.jqcomp.qgates.CompoundQGate;
/**
 *
 * @author Rafal
 */
public class Teleportation {
    public static class L extends Custom {
        private static double s2 = 1/Math.sqrt(2);
        private static Complex s2c = Complex.valueOf(s2,0);
        private static Complex s2ci = Complex.valueOf(-s2,0);

        public L() {
            super( new Complex[][] { {s2c, s2ci}, {s2c,s2c} });
        }       
    }

    public static class S extends Custom {
        public S() {
            super( new Complex[][] { { Complex.I, Complex.ZERO,},{Complex.ZERO, Complex.ONE}});
        }
    }

    public static class R extends Custom {
        private static double s2 = 1/Math.sqrt(2);
        private static Complex s2c = Complex.valueOf(s2,0);
        private static Complex s2ci = Complex.valueOf(-s2,0);

        public R() {
             super( new Complex[][] { {s2c, s2c}, {s2ci,s2c} });
        }
    }

    public static class T extends Custom {
        private static Complex min1 = Complex.valueOf(-1,0);
        private static Complex minImag = Complex.valueOf(0,-1);
        public T() {
             super( new Complex[][] { {min1, Complex.ZERO}, {Complex.ZERO, minImag} });
        }
    }

    public static void main(String []args) {
        QCircuit c = new QCircuit();

        QRegister init = new QRegister(Complex.valueOf(0.22, 0.44), Complex.valueOf(0.7, 0));
        init.normalize();
      //  init = QRegister.ket(0, 2).tensor(init);
        init = init.tensor(QRegister.ket(0, 2));
        System.out.println("init = "+init);
        System.out.println("");
        c.addStage(new CompoundQGate(new Identity(1), new L(), new Identity(1)));
        c.addStage(new CompoundQGate(new Identity(1), new CNot()));
        c.addStage(new CompoundQGate(new CNot(), new Identity(1)));
        c.addStage(new CompoundQGate(new R(), new Identity(1), new Identity(1)));
        c.addStage(new Measurement(3,1,2)); // <---
        c.addStage(new CompoundQGate(new S(), new CNot()));
        c.addStage(new CompoundQGate(new Identity(1), new Swap()));
        c.addStage(new CompoundQGate(new CNot(0,1), new Identity(1)));
        c.addStage(new CompoundQGate(new Identity(1), new Swap()));
        c.addStage(new CompoundQGate(new S(), new Identity(1), new T()));
        c.addStage(new CompoundQGate(new Identity(1), new Swap()));
        c.addStage(new CompoundQGate(new CNot(0,1), new Identity()));
        c.addStage(new CompoundQGate(new Identity(1), new Swap()));
        QRegister fin = c.compute(init);
        System.out.println("fin = "+fin); //sth is wrong
    }
}
