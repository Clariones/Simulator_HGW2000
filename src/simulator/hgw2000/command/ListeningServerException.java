package simulator.hgw2000.command;

public class ListeningServerException extends Exception {

	/**
	 * 
	 */
	protected static final long serialVersionUID = 6304804051110696831L;
	public ListeningServerException() {
	}

	public ListeningServerException(String message) {
		super(message);
	}

	public ListeningServerException(Throwable cause) {
		super(cause);
	}

	public ListeningServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ListeningServerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


}
