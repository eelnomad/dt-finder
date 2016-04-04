package bi.vision.exceptions;

public class ColNumException extends Exception {

	private static final long serialVersionUID = -2143206628123848348L;
	
	public ColNumException() {
		super();
	}

	public ColNumException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ColNumException(String message, Throwable cause) {
		super(message, cause);
	}

	public ColNumException(String message) {
		super(message);
	}

	public ColNumException(Throwable cause) {
		super(cause);
	}
	
}
