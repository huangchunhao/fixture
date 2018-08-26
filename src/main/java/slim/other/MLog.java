package slim.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.LogManager;



public class MLog {
	static Logger log = LogManager.getLogger("com.maiya");
	
	public MLog(String s){
		log.info(s);
	}
	
	public static Logger getLogger(){  
	    ConfigurationSource source; 
	    Logger logger = null;
	    //method2 System.getProperty  
	    String config = System.getProperty("user.dir"); 
	    String sep = System.getProperty("file.separator");
	    String fullPath = config +sep +"config" +sep +"log4j2.xml";  
	    System.out.println("Log4j2 fullPath = " + fullPath);  
	    File file = new File(fullPath);  
	    //this ConfigurationSource() could load xml dynamicly, info->debug->info test OK!!!  
	    try {  
	        source = new ConfigurationSource(new FileInputStream(file), file);  
	        Configurator.initialize(null, source);  
	        //Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);  
	        logger = StatusLogger.getLogger();  
	    } catch (FileNotFoundException e) {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	    }  
	    return logger;  
	}  

}
