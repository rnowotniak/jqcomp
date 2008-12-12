package pl.lodz.p.ics.quantum.jqcomp;

import pl.lodz.p.ics.quantum.jqcomp.qgates.CompoundQGate;
import java.util.Arrays;
import java.util.List;

public class QCircuit {

	private List<CompoundQGate> stages;

	public QCircuit() {
		this(new CompoundQGate[] {});
	}

	public QCircuit(CompoundQGate[] stages) {
		this.stages = Arrays.asList(stages);
	}

	public QRegister compute(QRegister register) {
		/*
		 * XXX:
		 * Efficient algorithm could be implemented here Wissam A. Samad, Roy
		 * Ghandour, and Mohamad. Memory efficient quantum circuit simulator
		 * based on linked list architecture'
		 */

		QRegister result = new QRegister(register);
		for (CompoundQGate stage : stages) {
			result = stage.compute(result);
		}

		return result;
	}

	public void addStage(CompoundQGate s) {
		stages.add(s);
	}

	public void addStage(int index, CompoundQGate s) {
		stages.add(index, s);
	}

    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < stages.size(); i++) {
			CompoundQGate stage = stages.get(i);

			sb.append("* Stage ");
			sb.append(i);
			sb.append(", size: ");
			sb.append(stage.size);
			sb.append(", matrix:\n");
			sb.append(stage.getMatrix());
			sb.append("\n");
		}

		return sb.toString();
	}

}
