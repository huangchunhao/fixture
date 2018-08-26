package slim.other;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maiya.exceptions.CustomInsertException;

public class StringUtil {
	static Logger log = LogManager.getLogger("com.maiya");
	static String s_clog = " String_Util: ";
	public static Map<String,String> chinesseMap=new HashMap<String,String>();
	
	public StringUtil() {
		log.info("######"+s_clog+"######"+" Create a String Util");
	}
	
	public String replaceContent(String oldContent,String replaceContent,int beginIndex) throws CustomInsertException{
		String newContent=null;
		
		int length = replaceContent.length();
		if((beginIndex+length)>oldContent.length()){
			throw new CustomInsertException(
					"the content needed to replace is no enough length.");
		}else{
			String subString = oldContent.substring(beginIndex, beginIndex+length);
			newContent=oldContent.replaceFirst(subString, replaceContent);
			log.info(s_clog+"the oldContent is: "+ oldContent);
			log.info(s_clog+"the replaceContent is: "+replaceContent);
			log.info(s_clog+"the newContent is: "+newContent);
		}
		
		return newContent;		
	}
	
	public String getRandom(int length) throws Exception{
		String randomString=null;
		StringBuffer randomStringb=new StringBuffer();
		if(length>17 || length<1){
			throw new Exception("the length should not greater than 17 or less than 1!!!");
		}else{
			String Format="yyyyMMddHHmmssSSS";		
			SimpleDateFormat format = new SimpleDateFormat(Format);
			String time = format.format(Calendar.getInstance().getTime());			
			randomStringb.append(time);
			log.info(s_clog+"the currentTime is: "+randomStringb.toString());
			log.info(s_clog+"the length needed is: "+length);
			randomString=randomStringb.reverse().subSequence(0, length).toString();			
		}
		log.info(s_clog+"the randomString is: "+randomString);
		return randomString;		
	}
	
	static{
		chinesseMap.put("nanjing", "�Ͼ�");
		chinesseMap.put("jianyequ", "������");		
	}
	
	/*public static void main(String[] args) {
		try {
			try {
				System.out.println(new StringUtil().getRandom(1));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	*/

}
