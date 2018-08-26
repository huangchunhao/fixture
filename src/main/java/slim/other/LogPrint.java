package slim.other;

public class LogPrint {
	
	 public static void print(String message){
		 StackTraceElement ste = new Throwable().getStackTrace()[1];
		 String fileName = ste.getFileName();
		 int lineNumber = ste.getLineNumber();	
		 System.out.println(fileName+"("+lineNumber+")"+": "+message);
	}
	


}
