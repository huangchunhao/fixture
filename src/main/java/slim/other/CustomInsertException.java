package slim.other;

public class CustomInsertException extends Exception {
	public CustomInsertException(){
		super();
	}

	public CustomInsertException(String msg) {
		super(msg);
	}

	public CustomInsertException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public CustomInsertException(Throwable cause) {
		super(cause);
	}

}
