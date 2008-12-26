package pl.lodz.p.ics.quantum.jqcomp;

import java.util.ArrayList;
import pl.lodz.p.ics.quantum.jqcomp.qgates.CompoundQGate;
import java.util.Arrays;
import java.util.List;

public class QCircuit {

    private List<Stage> stages;

    public QCircuit() {
        this(new CompoundQGate[]{});
    }

    public QCircuit(Stage[] stages) throws WrongSizeException {
        Integer size = null;
        for (Stage s : stages) {
            if (size == null) {
                size = s.getSize();
                continue;
            }
            if (size != s.getSize()) {
                throw new WrongSizeException(
                        "All stages in circuit must have same size.");
            }
        }
        this.stages = new ArrayList<Stage>(Arrays.asList(stages));
    }

    public QRegister compute(QRegister register) {
        /*
         * XXX:
         * Efficient algorithm could be implemented here Wissam A. Samad, Roy
         * Ghandour, and Mohamad. Memory efficient quantum circuit simulator
         * based on linked list architecture'
         */

        QRegister result = new QRegister(register);
        for (Stage stage : stages) {
                result = stage.compute(result);
        }

        return result;
    }

    public void addStage(Stage s) throws WrongSizeException {
        if (stages.size() > 0 && stages.get(0).getSize() != s.getSize()) {
            throw new WrongSizeException(
                    "All stages in circuit must have same size.");
        }
        stages.add(s);
    }

    public void addStage(int index, Stage s) throws WrongSizeException {
        if (stages.size() > 0 && stages.get(0).getSize() != s.getSize()) {
            throw new WrongSizeException(
                    "All stages in circuit must have same size.");
        }
        stages.add(index, s);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stages.size(); i++) {
            Stage stage = stages.get(i);

            sb.append("* Stage ");
            sb.append(i);
            sb.append(", size: ");
            sb.append(stage.getSize());
            sb.append("\n");
        }

        return sb.toString();
    }



    /**
     * @return the stages
     */
    public List<Stage> getStages() {
        return stages;
    }

    /**
     * @param stages the stages to set
     */
    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }
}
