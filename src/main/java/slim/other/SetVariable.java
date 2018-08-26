package slim.other;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetVariable {
	
	static Logger log = LogManager.getLogger("com.maiya");
	private String oldValue;
	private String newValue;
	
	public SetVariable getInstance(){
		return this;
	}
	
	public String setValue(String newValue,String oldValue){
		this.oldValue=oldValue;
		this.newValue=newValue;
		log.debug("the old value is " + this.oldValue);
		log.debug("the new value is " + this.newValue);
		return this.newValue;
	}
	
	public String getOldValue(){
		return this.oldValue;
	}
	
	public String getNewValue(){
		return this.newValue;
	}

}
