package pl.lodz.p.ics.quantum.jqcomp;

public class WrongSizeException extends RuntimeException {

	private static final long serialVersionUID = 6909447735705557452L;

	public WrongSizeException() {
		
	}

	public WrongSizeException(String message) {
		super(message);
	}

	public WrongSizeException(Throwable cause) {
		super(cause);
	}

	public WrongSizeException(String message, Throwable cause) {
		super(message, cause);
	}

}
