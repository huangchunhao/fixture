package slim.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maiya.exceptions.CustomInsertException;
import com.maiya.util.LogPrint;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class XdOracleFixture extends XdDBBase {
	static Logger log = LogManager.getLogger("com.maiya");
	static String s_clog = " Oracle_Fixture: ";
	// private Connection con = null;
	// private PreparedStatement pre = null;
	// static public String oracleUrl =
	// "jdbc:oracle:thin:@192.168.0.67:1521:orcl";
	private String address;
	private String url;
	private String table;
	private String user;
	private String password;
	private String driverClass = "oracle.jdbc.driver.OracleDriver";
	private String s_tableName;
	private String s_fields;
	private StringBuffer sb_fields = new StringBuffer();
	private String s_values;
	private StringBuffer sb_values = new StringBuffer();
	private String s_insertStatement;
	private StringBuffer sb_insertStatement = new StringBuffer();

	/**
	 * ������com.maiya.xd.sql=== ��ʼ��һ�� XdOracleFixture
	 * 
	 * @param address
	 *            ��ַ
	 * @param table
	 *            ����
	 * @param user
	 *            �û���
	 * @param password
	 *            ����
	 */
	public XdOracleFixture(String address, String table, String user, String password) {
		log.info("######" + s_clog + "######" + " Create a oracle fixture.");
		log.debug(s_clog + " Connect info: " + address + "|" + table + "|" + user + "|" + password);
		this.address = address;
		this.table = table;
		this.user = user;
		this.password = password;
		this.url = "jdbc:oracle:thin:@" + this.address + ":" + this.table;
		// try {
		// Class.forName("oracle.jdbc.driver.OracleDriver");
		// log.trace("begin to connect oracle successfully");
		// this.url = "jdbc:oracle:thin:@" + this.address + ":" + this.table;
		// log.trace("the oracle url is: " + this.url);
		// } catch (ClassNotFoundException e) {
		// log.debug(s_clog+" can not get oracle ");
		// log.error(e);
		// e.printStackTrace();
		// }
	}

	private Connection getMyConnection() throws SQLException {
		Connection icon = super.getBaseConnection(this.driverClass, this.url, this.user, this.password);
		return icon;
	}

	/**
	 * insert������ñ���
	 * 
	 * @param tableName
	 *            ����
	 * @return ���ý��
	 */
	public boolean setTableName(String tableName) {
		log.debug(s_clog + " Table name: " + tableName);
		this.s_tableName = tableName;
		boolean result = true;
		return result;
	}

	/**
	 * insert��������ֶ�
	 * 
	 * @param fields
	 *            �ֶ�
	 * @return ���ý��
	 */
	public boolean setFields(String... fields) {
		boolean result = true;
		for (String field : fields) {
			this.sb_fields.append(field + ",");
		}
		this.sb_fields.deleteCharAt(this.sb_fields.lastIndexOf(","));
		this.s_fields = "(" + this.sb_fields.toString() + ")";
		log.debug(s_clog + " The fields of insert sql statement is " + this.s_fields);
		return result;
	}

	/**
	 * insert�������ֵ
	 * 
	 * @param values
	 *            ֵ
	 * @return ���ý��
	 */
	public boolean setValues(String... values) {
		boolean result = true;
		for (String value : values) {
			this.sb_values.append(value + ",");
		}
		this.sb_values.deleteCharAt(this.sb_values.lastIndexOf(","));
		this.s_values = "(" + this.sb_values.toString() + ")";
		log.debug(s_clog + " The values of insert sql statement is " + this.s_values);
		return result;
	}

	/**
	 * ��setTableName��setFields�Լ�setValues��ִ�����
	 * 
	 * @return ִ�н��
	 * @throws SQLException
	 */
	public boolean executeInsert() throws SQLException {
		boolean result = false;
		if (this.s_tableName.isEmpty() && this.s_fields.isEmpty() && this.s_values.isEmpty()) {
			try {
				throw new CustomInsertException("tableName|fields|values shold not empty, "
						+ "please refer to method: setTableName|setFields|setValues");
			} catch (CustomInsertException e) {
				log.error(e);
				e.printStackTrace();
			}
		} else {
			this.sb_insertStatement.append("insert into");
			this.sb_insertStatement.append(" ");
			this.sb_insertStatement.append(this.s_tableName);
			this.sb_insertStatement.append(" ");
			this.sb_insertStatement.append(this.s_fields);
			this.sb_insertStatement.append(" ");
			this.sb_insertStatement.append("values");
			this.sb_insertStatement.append(this.s_values);
			this.s_insertStatement = this.sb_insertStatement.toString();
			log.debug(s_clog + this.s_insertStatement);
			result = this.insertData(this.s_insertStatement);
		}
		return result;
	}

	/**
	 * �������
	 * 
	 * @param sql
	 *            insert���
	 * @return ������
	 * @throws SQLException
	 */
	public boolean insertData(String sql) throws SQLException {
		int i = 0;
		boolean result = false;
		Connection icon = this.getMyConnection();
		PreparedStatement ipre = null;
		try {
			// icon = DriverManager.getConnection(this.url, this.user,
			// this.password);
			log.debug(s_clog + " connect oracle successfully ");
			log.debug(s_clog + sql);
			ipre = (PreparedStatement) icon.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			i = ipre.executeUpdate();
			log.debug(s_clog + " The number of rows affected is " + i);
			if (0 != i) {
				result = true;
			}
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			try {
				ipre.close();
				icon.close();
			} catch (SQLException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		// System.out.println(result);
		return result;
	}

	/**
	 * ɾ������
	 * 
	 * @param sql
	 *            delete���
	 * @return ɾ�����
	 * @throws SQLException
	 */
	public boolean deleteData(String sql) throws SQLException {
		int i = 0;
		boolean result = false;
		Connection dcon = this.getMyConnection();
		PreparedStatement dpre = null;
		try {
			// dcon = DriverManager.getConnection(this.url, this.user,
			// this.password);
			log.debug(s_clog + " connect oracle successfully ");
			log.debug(s_clog + sql);
			dpre = (PreparedStatement) dcon.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			i = dpre.executeUpdate();
			log.debug(s_clog + " The number of rows affected is " + i);
			// System.out.println(i);
			if (0 != i) {
				result = true;
			}
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			try {
				dpre.close();
				dcon.close();
			} catch (SQLException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		// System.out.println(result);
		return result;
	}

	/**
	 * ��������
	 * 
	 * @param sql
	 *            update���
	 * @return ���½��
	 * @throws SQLException
	 */
	public boolean updateData(String sql) throws SQLException {
		int i = 0;
		boolean result = false;
		Connection ucon = this.getMyConnection();
		PreparedStatement upre = null;
		try {
			// ucon = DriverManager.getConnection(this.url, this.user,
			// this.password);
			log.debug(s_clog + " connect oracle successfully ");
			log.debug(s_clog + sql);
			upre = (PreparedStatement) ucon.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			i = upre.executeUpdate();
			log.debug(s_clog + " The number of rows affected is " + i);
			if (0 != i) {
				result = true;
			}
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			try {
				upre.close();
				ucon.close();
			} catch (SQLException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		// System.out.println(result);
		return result;
	}

	// ����ֻ֧�ֲ�ѯһ��ֵ
	/**
	 * ��ѯ����
	 * 
	 * @param sql
	 *            select���
	 * @return ��ѯ�����ֻ�ܷ���һ�����
	 * @throws CustomInsertException
	 * @throws SQLException
	 */
	public String queryData(String sql) throws CustomInsertException, SQLException {
		// XdOracleFixture qd = new XdOracleFixture(this.address, this.table,
		// this.user, this.password);
		Connection qcon = this.getMyConnection();
		PreparedStatement qpre = null;

		ResultSet rs = null;
		String result = null;
		int count = 0;
		try {
			// qcon = DriverManager.getConnection(this.url, this.user,
			// this.password);
			log.debug(s_clog + " connect oracle successfully ");
			log.debug(s_clog + sql);
			qpre = (PreparedStatement) qcon.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = qpre.executeQuery(sql);
			while (rs.next()) {
				count++;
			}
			if (count == 0) {
				// result = "please look at Exception info";
				throw new CustomInsertException(
						"Please check the sql :" + sql + "(the count of result is " + count + " )");
				/*
				 * try { throw new CustomInsertException(
				 * "Please check the sql :" + sql + "(the count of result is " +
				 * count + " )"); } catch (CustomInsertException e) {
				 * log.error(e); e.printStackTrace(); }
				 */
			} else if (count == 1) {
				rs.first();
				result = rs.getString(1);
				log.debug(s_clog + "the result is: " + result);
			} else if (count > 1) {
				//List<String> list = new ArrayList<String>();
				while (rs.next()) {
					//list.add(rs.getString(1));
					result+="/";
					result+=rs.getString(1);
				}
				//result = list.toString();
				throw new CustomInsertException("get too many results, Please check the result :" + sql
						+ "(the count of result is " + count + " )" + "the result is " + result);
			}
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			try {
				qpre.close();
				qcon.close();
			} catch (SQLException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		// System.out.println(result);
		return result;
	}

	/**
	 * ��ѯ����
	 * 
	 * @param sql
	 *            select���
	 * @return ��ѯ�����ֻ�ܷ���һ�����(�����ָ�����ʱʹ��)
	 * @throws SQLException
	 */
	public String queryData(String sql, String ignore) throws SQLException {
		String result = null;
		try {
			result = queryData(sql);
		} catch (CustomInsertException e) {
			result = ignore;
			log.error(e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ��ѯ����(�����еȴ�ʱ��)
	 * 
	 * @param sql
	 *            select���
	 * @param value
	 *            Ԥ��ֵ
	 * @param timeStep
	 *            ʱ����
	 * @param times
	 *            ���Դ���
	 * @return ��ѯ�����ֻ�ܼ��һ�����(���ݱ��г������ݲ���ʱ�����)
	 * @throws SQLException
	 */
	public boolean queryData(String sql, String value, int timeStep, int times) throws SQLException {
		boolean flag = false;
		String result = null;
		if(timeStep>2000){
			timeStep=2000;
		}
		if(times>30){
			times=30;
		}
		for (int i = 0; i < times; i++) {
			try {
				result = queryData(sql);
				log.debug("the result is " + result);
				if (value.equals(result)) {
					flag = true;
					break;
				} else {
					log.debug("continue");
					this.waitTime(timeStep);
				}
			} catch (CustomInsertException e) {
				log.debug("keep waitting");
				this.waitTime(timeStep);
			}
		}
		return flag;
	}

	/**
	 * @param sql
	 *            select���
	 * @param timeStep
	 *            ʱ����
	 * @param times
	 *            ���Դ���
	 * @return ��ѯ���(������Ϊ�գ�һֱ�ȵ��������Դ���)
	 * @throws SQLException
	 */
	public String queryData(String sql, int timeStep, int times) throws SQLException {
		String result = null;
		if(timeStep>2000){
			timeStep=2000;
		}
		if(times>30){
			times=30;
		}
		for (int i = 0; i < times; i++) {
			try {
				result = queryData(sql);
				log.debug("the result is " + result);
				if (null != result) {
					break;
				} else {
					log.debug("continue");
					this.waitTime(timeStep);
				}
			} catch (CustomInsertException e) {
				log.debug("keep waitting");
				this.waitTime(timeStep);
			}
		}
		return result;
	}
	
	/**
	 * ��ѯ��Ϊָ������(�����еȴ�ʱ��)
	 * 
	 * @param sql
	 *            select���
	 * @param value
	 *            Ԥ��ֵ
	 * @param timeStep
	 *            ʱ����
	 * @param times
	 *            ���Դ���
	 * @return ��ѯ�����ֻ�ܼ��һ�����(���ݱ���У��Ԥ��ֵ���ֱ仯)
	 * @throws SQLException
	 */
	public boolean queryDataNotValue(String sql, String value, int timeStep, int times) throws SQLException {
		boolean flag = false;
		String result = null;
		if(timeStep>2000){
			timeStep=2000;
		}
		if(times>30){
			times=30;
		}
		for (int i = 0; i < times; i++) {
			try {
				result = queryData(sql);
				log.debug("the result is " + result);
				if (!value.equals(result)) {
					flag = true;
					break;
				} else {
					log.debug("continue");
					this.waitTime(timeStep);
				}
			} catch (CustomInsertException e) {
				log.debug("keep waitting");
				this.waitTime(timeStep);
			}
		}
		return flag;
	}

	/**
	 * ����
	 * 
	 * @param sql
	 *            select���
	 * @return ��ѯ���������ֵΪ��
	 * @throws CustomInsertException
	 * @throws SQLException
	 */
	public String queryDataCheckNull(String sql) throws CustomInsertException, SQLException {
		// XdOracleFixture qd = new XdOracleFixture(this.address, this.table,
		// this.user, this.password);
		Connection qcon = this.getMyConnection();
		PreparedStatement qpre = null;

		ResultSet rs = null;
		String result = null;
		int count = 0;
		try {
			// qcon = DriverManager.getConnection(this.url, this.user,
			// this.password);
			log.debug(s_clog + " connect oracle successfully ");
			log.debug(s_clog + sql);
			qpre = (PreparedStatement) qcon.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = qpre.executeQuery(sql);
			while (rs.next()) {
				count++;
			}
			if (count == 0) {
				result = "null";

				/*
				 * try { throw new CustomInsertException(
				 * "Please check the sql :" + sql + "(the count of result is " +
				 * count + " )"); } catch (CustomInsertException e) {
				 * log.error(e); e.printStackTrace(); }
				 */
			} else if (count == 1) {
				rs.first();
				result = rs.getString(1);
				log.debug(s_clog + "the result is: " + result);
			} else if (count > 1) {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString(1));
				}
				result = list.toString();
				throw new CustomInsertException("get too many results, Please check the result :" + sql
						+ "(the count of result is " + count + " )" + "the result is " + result);
			}
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			try {
				qpre.close();
				qcon.close();
			} catch (SQLException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		// System.out.println(result);
		return result;
	}

	/**
	 * ��queryData��ͬ�����������Է���һ�еĶ��ֵ�����ؽ����list��ʽ
	 * 
	 * @param sql
	 *            sql���
	 * @return list���
	 * @throws SQLException
	 */
	public String queryDataBackListByColumn(String sql) throws SQLException {

		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		String result = null;
		Connection conn = this.getMyConnection();
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			// System.out.println("match");
			// conn = DriverManager.getConnection(this.mysqlUrl);
			// log.debug(s_clog+" connect mysql successfully ");
			// log.debug(s_clog+sql);
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			result = list.toString();
			log.debug(s_clog + "the result is: " + result);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		return result;
	}

	// ����֧�ֲ�ѯ���ֵ
	/**
	 * ��ѯ����
	 * 
	 * @param sql
	 *            select���
	 * @return ��ѯ�������string������ʽ���ض�����
	 * @throws CustomInsertException
	 * @throws SQLException
	 */
	public String queryDataBackListByRow(String sql) throws CustomInsertException, SQLException {
		// XdOracleFixture qd = new XdOracleFixture(this.address, this.table,
		// this.user, this.password);
		Connection qcon = this.getMyConnection();
		PreparedStatement qpre = null;

		ResultSet rs = null;
		String result = null;
		StringBuffer sb = new StringBuffer();
		int count = 0;
		try {
			// qcon = DriverManager.getConnection(this.url, this.user,
			// this.password);
			log.debug(s_clog + " connect oracle successfully ");
			log.debug(s_clog + sql);
			qpre = (PreparedStatement) qcon.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = qpre.executeQuery(sql);
			while (rs.next()) {
				count++;
			}
			if (count == 0) {
				// result = "please look at Exception info";
				throw new CustomInsertException(
						"Please check the sql :" + sql + "(the count of result is " + count + " )");
				/*
				 * try { throw new CustomInsertException(
				 * "Please check the sql :" + sql + "(the count of result is " +
				 * count + " )"); } catch (CustomInsertException e) {
				 * log.error(e); e.printStackTrace(); }
				 */
			} else if (count == 1) {
				rs.first();
				ResultSetMetaData md = rs.getMetaData();
				int columnCount = md.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					sb.append(rs.getObject(i).toString());
					if (i != columnCount) {
						sb.append(",");
					}
				}
				result = sb.toString();
				log.debug(s_clog + "the result is: " + result);
			} else if (count > 1) {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString(1));
				}
				result = list.toString();
				throw new CustomInsertException("get too many results, Please check the result :" + sql
						+ "(the count of result is " + count + " )" + "the result is " + result);
			}
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			try {
				qpre.close();
				qcon.close();
			} catch (SQLException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		// System.out.println(result);
		return result;
	}

	/**
	 * ��ȡ��ǰʱ�������
	 * 
	 * @param Format
	 *            ��ʽ
	 * @return ��ǰʱ�������
	 */
	public String getCurrentTimeWithFormat(String Format) {
		// "yyyy-MM-dd-HH-mm-ss-SSS"
		SimpleDateFormat format = new SimpleDateFormat(Format);
		String time = format.format(Calendar.getInstance().getTime());
		return time;
	}

	public boolean plog(String s) {
		log.info(s);
		return true;
	}

	public boolean waitTime(int time) {
		try {
			log.debug("Wait time:" + time + "MS");
			log.debug("Wait begin");
			Thread.sleep(time);
			log.debug("Wait end");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * public static void main(String[] args) { XdOracleFixture query= new
	 * XdOracleFixture("192.168.0.67:1521","orcl","maiyaxd_trunk",
	 * "maiyaxd_trunk"); String result = query.queryData(
	 * "SELECT sBillNo from hk_Bill WHERE sMemberId ='80168fb8d0c7453e9d831cbc3c981c8f' order by dBillBeginDate desc"
	 * ); System.out.println(result); }
	 */

}
