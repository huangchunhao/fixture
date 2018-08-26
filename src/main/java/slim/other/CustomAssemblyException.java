package slim.other;

public class CustomAssemblyException extends Exception {
	public CustomAssemblyException(){
		super();
	}

	public CustomAssemblyException(String msg) {
		super(msg);
	}

	public CustomAssemblyException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public CustomAssemblyException(Throwable cause) {
		super(cause);
	}

}
