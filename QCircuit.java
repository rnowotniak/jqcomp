package pl.lodz.p.ics.quantum.jqcomp;

public class QCircuit {
	public QCircuit(Stage[] stages) {
		this.stages = stages;
	}
	
	public QRegister execute(QRegister register){		
		/* '# Efficient algorithm could be implemented here
        # Wissam A. Samad, Roy Ghandour, and Mohamad.
        # Memory efficient quantum circuit simulator based on linked list architecture'
        */
		
		QRegister result = new QRegister(register);
		for(Stage stage : stages) {
			result = stage.compute(result);
		}
		
		return result;
	}
	
	private Stage[] stages;
}
