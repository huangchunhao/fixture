package slim.db;

import java.beans.PropertyVetoException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBPoolFactory {
	
	
   private ComboPooledDataSource ds;
   
   public DBPoolFactory(String driverClass,String jdbcUrl,String user,String password){
	   ds = new ComboPooledDataSource();
	   try {
			ds.setDriverClass(driverClass);
			ds.setJdbcUrl(jdbcUrl);
			ds.setUser(user);
			ds.setPassword(password);
			ds.setMaxPoolSize(10);
			ds.setMinPoolSize(2);
			ds.setInitialPoolSize(5);
			ds.setMaxStatements(180);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
   }
   
   public ComboPooledDataSource getDBPool(){
	   return this.ds;
   }
}
