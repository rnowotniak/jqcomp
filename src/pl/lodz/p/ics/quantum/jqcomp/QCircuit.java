package pl.lodz.p.ics.quantum.jqcomp;

import java.util.Arrays;
import java.util.List;

public class QCircuit {

	public QCircuit() {
		this(new Stage[] {});
	}

	public QCircuit(Stage[] stages) {
		this.stages = Arrays.asList(stages);
	}

	public QRegister execute(QRegister register) {
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

	public void addStage(Stage s) {
		stages.add(s);
	}

	public void addStage(int index, Stage s) {
		stages.add(index, s);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < stages.size(); i++) {
			Stage stage = stages.get(i);

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

	private List<Stage> stages;
}
