package slim.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class XdOracleQueryTable extends XdDBBase{
	static Logger log = LogManager.getLogger("com.maiya");
	static String s_clog = " Oracle_QueryTable_Fixture: ";
	private Connection conn = null;
	private Statement stmt =null;
	//private String tableName;
	//private String selectionCondition;
	private String address;
	private String table;
	private String user;
	private String password;
	private String driverClass="oracle.jdbc.driver.OracleDriver";
	private String sql;
	private String url;
	private List<String> columnLabelListInHTML;

	/**
	 * ������com.maiya.xd.sql===
	 * ��ʼ��һ�� XdOracleQueryTable
	 * @param address ��ַ
	 * @param table ����
	 * @param user �û���
	 * @param password ����
	 * @param sql sql���
	 */
	public XdOracleQueryTable(String address,String table,String user,String password,String sql) {
		log.info("######"+s_clog+"######" + " Create a Oracle fixture.");
		log.debug(s_clog + " Connect info: " + address + "|" + table + "|" + user + "|" + password);
		log.debug(s_clog + "the sql is " + sql);
		this.address = address;
		this.table = table;
		this.user=user;
		this.password=password;
		this.sql=sql;
		this.url = "jdbc:oracle:thin:@"+this.address+":"+this.table;
	}
	
	private Connection getMyConnection() throws SQLException {
		Connection icon = super.getBaseConnection(this.driverClass, this.url, this.user, this.password);
		return icon;
	}

	public void table(List<List<String>> table) {
		Iterator listIterator = table.iterator();
		if (listIterator.hasNext()) {
			this.columnLabelListInHTML = (List<String>) listIterator.next();
			log.debug(s_clog + " the number of columns is: " + columnLabelListInHTML.size());
			// System.out.println(columnLabelListInHTML.size());
			// System.out.println(columnLabelListInHTML.get(0));
			// System.out.println(columnLabelListInHTML.get(1));
		} else {
			try {
				throw new Exception("can not get table info from html");
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
			}
		}

	}

	public List<List<List<String>>> query() {

		List<List<List<String>>> resultAfterQuery = assemblyResult();
		log.debug(s_clog + " the result queried is: " + resultAfterQuery.toString());
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		return resultAfterQuery;
	}

	private ResultSet querySql() {

		ResultSet rs = null;
		//String sql = "SELECT * FROM " + tableName + " WHERE " + selectionCondition;

		try {
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//log.debug(s_clog + "load Oracle driver successfully");
			//String oracleUrl = "jdbc:oracle:thin:@"+this.address+":"+this.table;;
			//String oracleUser = this.user;
			//String oraclePassword = this.password;
			//log.debug(s_clog + "the oracle url is " + oracleUrl);
			//log.debug(s_clog + "the oracle url is " + oracleUser);
			//log.debug(s_clog + "the oracle url is " + oraclePassword);
			//conn = DriverManager.getConnection(oracleUrl, oracleUser, oraclePassword);
			conn = this.getMyConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} /*
			 * finally { try { conn.close(); } catch (SQLException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } }
			 */
		return rs;
	}

	private List<List<List<String>>> assemblyResult() {
	ResultSet rs = querySql();
	List<List<List<String>>> ResultList = new ArrayList<List<List<String>>>();
	

	// System.out.println(this.columnLabelListInHTML.size());
	try {
		while (rs.next()) {
			Iterator columnLabelList = this.columnLabelListInHTML.iterator();
			List<List<String>> rowList = new ArrayList<List<String>>();
			while (columnLabelList.hasNext()) {
				String columnLabel = (String) columnLabelList.next();
				if (!columnLabel.isEmpty()) {
					String columnLabelValue = rs.getString(columnLabel);
					log.trace(columnLabel);
					log.trace(columnLabelValue);						
					rowList.add(Arrays.asList(columnLabel, columnLabelValue));
					log.trace("the sizes of rowList is "+rowList.toString());						
				} else {
					log.trace("this is null");
				}				
			}
			ResultList.add(rowList);
			log.trace("the sizes of resultList is "+ResultList.size());
			log.trace("the content of resultList is "+ResultList.toString());
			
		}
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return ResultList;
}

}
