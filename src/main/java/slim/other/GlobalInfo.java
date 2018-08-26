package slim.other;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GlobalInfo {
	static public Properties properties = new Properties();
	static public boolean getConfigFlag = true;// false Ϊ��ȡ�ڲ���Ϣ��trueΪ�ⲿ�����ļ�
	static public String screenShotSavePath = "D:\\B_fitnesse\\fitnesse-master\\FitNesseRoot\\files\\screenshot\\";
	static public String mysqlUrl = "jdbc:mysql://192.168.0.67:3306/maiyaph?"
			+ "user=maiyaph&password=maiyaph&useUnicode=true&characterEncoding=UTF8";

	static public String oracleUrl = "jdbc:oracle:thin:@192.168.0.67:1521:orcl";
	static public String oracleUser = "maiyaxd_performance";
	static public String oraclePassword = "maiyaxd_performance";

	static public String getScreenShotSavePath(String key) {
		String path = null;
		if (getConfigFlag) {
			path = getInfoFromOutProperties(key);
			if (path == null) {
				path = screenShotSavePath;
				System.out.println("the MysqlUrl is " + path + ". the path is from local static info");
			} else {
				System.out.println("the MysqlUrl is " + path + ". the path is from init.properties");
			}
		} else {
			path = screenShotSavePath;
		}
		return path;
	}

	static public String getMysqlUrl(String key) {
		String value = null;
		if (getConfigFlag) {
			value = getInfoFromOutProperties(key);
			if (value == null) {
				value = mysqlUrl;
				System.out.println("the MysqlUrl is " + value + ". the value is from local static info");
			} else {
				System.out.println("the MysqlUrl is " + value + ". the value is from init.properties");
			}
		} else {
			value = mysqlUrl;
		}
		return value;
	}

	static public String getOracleUrl(String key) {
		String url = null;
		if (getConfigFlag) {
			url = getInfoFromOutProperties(key);
			if (url == null) {
				url = oracleUrl;
				System.out.println("the OracleUser is " + url + ". the value is from local static info");
			} else {
				System.out.println("the OracleUser is " + url + ". the value is from init.properties");
			}
		} else {
			url = oracleUrl;
		}
		return url;
	}

	static public String getOracleUser(String key) {
		String user = null;
		if (getConfigFlag) {
			user = getInfoFromOutProperties(key);
			if (user == null) {
				user = oracleUser;
				System.out.println("the OracleUrl is " + user + ". the value is from local static info");
			} else {
				System.out.println("the OracleUrl is " + user + ". the value is from init.properties");
			}
		} else {
			user = oracleUser;
		}
		return user;
	}

	static public String getOraclePassword(String key) {
		String password = null;
		if (getConfigFlag) {
			password = getInfoFromOutProperties(key);
			if (password == null) {
				password = oraclePassword;
				System.out.println("the OraclePassword is " + password + ". the value is from local static info");
			} else {
				System.out.println("the OraclePassword is " + password + ". the value is from init.properties");
			}
		} else {
			password = oracleUser;
		}
		return password;

	}

	static private String getInfoFromOutProperties(String key) {
		String sep = System.getProperty("file.separator");
		String filePath = System.getProperty("user.dir") + sep + "config" + sep + "init.properties";
		System.out.println("the path of config file is " + filePath);
		String value = null;

		try (InputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
			if (in != null) {
				properties.load(in);
				in.close();
				if (properties.containsKey(key)) {
					value = properties.getProperty(key);
				} else {
					System.out.println("The " + key + " does not exist in /config/init.properties");
				}
			} else {
				System.out.println("InputStream of " + filePath + " is null");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

}
