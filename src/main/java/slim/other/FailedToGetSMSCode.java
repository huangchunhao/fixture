package slim.other;

public class FailedToGetSMSCode extends Exception {
	public FailedToGetSMSCode(){
		super();
	}

	public FailedToGetSMSCode(String msg) {
		super(msg);
	}

	public FailedToGetSMSCode(String msg, Throwable cause) {
		super(msg, cause);
	}

	public FailedToGetSMSCode(Throwable cause) {
		super(cause);
	}

}
