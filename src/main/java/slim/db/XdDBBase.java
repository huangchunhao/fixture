package slim.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class XdDBBase {
	static Logger log = LogManager.getLogger("com.maiya");
	static String s_clog = " XdDBBase_Fixture: ";
	
	public Connection getBaseConnection(String driverClass,String url,String user, String password) throws SQLException {
		Connection icon = null;
		if (DBPoolContext.getInstance().DBConnectionMap.containsKey(user)) {
			log.debug(s_clog + " Connection pool " + user + " exists");
			log.debug(s_clog + " the connection info is " + url + "|" + user + "|"+ password);
			icon = DBPoolContext.getInstance().DBConnectionMap.get(user).getConnection();
		} else {
			log.debug(s_clog + " Connection pool " + user + " does not exist, should be created");
			ComboPooledDataSource cpds = new DBPoolFactory(driverClass, url, user,password).getDBPool();
			DBPoolContext.getInstance().DBConnectionMap.put(user, cpds);
			log.debug(s_clog + " Connection pool " + user + " have been put into DBConnectionMap.");
			icon = cpds.getConnection();
		}
		return icon;
	}

}
