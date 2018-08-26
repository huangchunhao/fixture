package slim.db;


import java.util.HashMap;
import java.util.Map;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBPoolContext {
	
	public static Map<String,ComboPooledDataSource> DBConnectionMap = new HashMap<String,ComboPooledDataSource>();
		
	private DBPoolContext() {
	}

	private static volatile DBPoolContext opc = null;
	
	public static DBPoolContext getInstance() {
		synchronized (DBPoolContext.class) {
			if (opc == null) {
				opc = new DBPoolContext();
			}
		}
		return opc;
	}
}
