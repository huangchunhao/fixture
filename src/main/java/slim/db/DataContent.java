package slim.db;

import java.util.List;
import java.util.Map;

public class DataContent {
	public String tableName;
	public Map<String,String> content;

	public String getTableName() {
		return this.tableName;
	}

	public Map<String,String> getContent() {
		return this.content;
	}
	
	public void setTableName(String tableName) {
		this.tableName=tableName;
	}
	
	public void setContent(Map<String,String> content) {
		this.content=content;
	}

	@Override
	public String toString() {
		return "{\"tableName\":\"" + this.tableName + "\",\"content\":\"" + this.content + "\"}";
	}

}
