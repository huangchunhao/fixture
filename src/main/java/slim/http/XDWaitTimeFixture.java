package slim.http;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XDWaitTimeFixture {
	static Logger log = LogManager.getLogger("com.maiya");
	
	/**
	 * �ȴ�����
	 */
	public XDWaitTimeFixture(){
		
	}
	
	/**
	 * �ȴ�ʱ������
	 * @param time �ȴ�ʱ�䣬��λms
	 * @return ִ�н��
	 */
	public boolean waitTime(int time) {
		try {
			log.debug("Wait time:"+time+"MS");
			log.debug("Wait begin");
			Thread.sleep(time);
			log.debug("Wait end");		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
