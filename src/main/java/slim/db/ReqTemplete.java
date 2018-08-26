package slim.db;

import java.util.List;

public class ReqTemplete {
	public String dbtype;
	public String ip;
	public String sid;
	public String user;
	public String pw;
	public String businessType;
	public List<DataContent> data;

	@Override
	public String toString() {
		String s = "{\"dbtype\":\"" + this.getDbtype() + "\"," + "\"ip\":\"" + this.getIp() + "\"," + "\"sid\":\""
				+ this.getSid() + "\"," + "\"user\":\"" + this.getUser() + "\"," + "\"pw\":\"" + this.getPw() + "\","
				+ "\"businessType\":\"" + this.getBusinessType() + "\"," + "\"data\":" + this.getData() + "}";
		return s;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public List<DataContent> getData() {
		return data;
	}

	public void setData(List<DataContent> data) {
		this.data = data;
	}

}
