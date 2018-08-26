package slim.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mock.http.HttpUtil;

public class MaiYaAssert {
	static Logger log = LogManager.getLogger("com.maiya");
	
	public String url;
	
	
	public MaiYaAssert(String platform,String env,String mobile){
		switch(platform.trim().toLowerCase()){
		case "maiyadai":
			this.url="http://192.168.161.10:19694/myddb/Data/queryDataAndSaveInCache/"+env+"/"+mobile;
			break;		
		default: 
			this.url="http://192.168.161.10:19694/myddb/Data/queryDataAndSaveInCache/"+env+"/"+mobile;
		}
	}
	
	public String getContentId(){
		log.debug(this.url);
		String contentId = HttpUtil.doGet(this.url, "UTF-8");
		log.debug(contentId);
		return contentId;
	}
	

}
