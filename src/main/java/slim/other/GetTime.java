package slim.other;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maiya.XDFixture;

public class GetTime {
	static Logger log = LogManager.getLogger("com.maiya");
	static String s_clog = "######Get_Time######: ";

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
		log.debug(s_clog+time);
		return time;
	}
	
	/**
	 * @param Format ��ʽ
	 * @param s_Months ����
	 * @return ���ص�ǰ��ǰ������µ�ʱ��
	 */
	public String getCurrentTimeOfAnyMonthWithFormat(String Format, String s_Months) {
		int i_days = Integer.parseInt(s_Months);
		SimpleDateFormat format = new SimpleDateFormat(Format);
		Calendar calendarNew = Calendar.getInstance();
		calendarNew.add(Calendar.MONTH, i_days);
		String time = format.format(calendarNew.getTime());
		log.debug(s_clog+time);
		return time;
	}

	/**
	 * @param Format ��ʽ
	 * @param s_days ����
	 * @return ���ص�ǰ��ǰ��������ʱ��
	 */
	public String getCurrentTimeOfAnyDayWithFormat(String Format, String s_days) {
		int i_days = Integer.parseInt(s_days);
		SimpleDateFormat format = new SimpleDateFormat(Format);
		Calendar calendarNew = Calendar.getInstance();
		calendarNew.add(Calendar.DAY_OF_MONTH, i_days);
		String time = format.format(calendarNew.getTime());
		log.debug(s_clog+time);
		return time;
	}

	/**
	 * @param Format ��ʽ
	 * @param s_days ����
	 * @return ���ص��쵱ǰ��ǰ�����Сʱ��ʱ��
	 */
	public String getAnyHourOfCurrentDayWithFormat(String Format, String s_hour) {
		int i_hour = Integer.parseInt(s_hour);
		SimpleDateFormat format = new SimpleDateFormat(Format);
		Calendar calendarNew = Calendar.getInstance();
		calendarNew.add(Calendar.HOUR, i_hour);
		String time = format.format(calendarNew.getTime());
		log.debug(s_clog+time);
		return time;
	}

	/**
	 * @param Format ��ʽ
	 * @param s_days ����
	 * @return ���ص�Сʱ��ǰ��ǰ����ٷ��ӵ�ʱ��
	 */
	public String getAnyMinuteOfCurrentHourWithFormat(String Format, String s_Minute) {
		int i_Minute = Integer.parseInt(s_Minute);
		SimpleDateFormat format = new SimpleDateFormat(Format);
		Calendar calendarNew = Calendar.getInstance();
		calendarNew.add(Calendar.MINUTE, i_Minute);
		String time = format.format(calendarNew.getTime());
		log.debug(s_clog+time);
		return time;
	}
	
	public boolean compareDate1IsGreaterThanDate2(String Format,String date1,String date2){
		boolean result = false;
		SimpleDateFormat df = new SimpleDateFormat(Format);
		try {
			Date date_1=df.parse(date1);
			Date date_2=df.parse(date2);
			if(date_1.getTime() > date_2.getTime()){
				result=true; 
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return result;		
	}
	
	public boolean plog(String s) {
		log.info(s);
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetTime gt = new GetTime();
		System.out.println(gt.getCurrentTimeOfAnyDayWithFormat("yyyy-MM-dd-HH-mm-ss-SSS", "10"));
		System.out.println(gt.getAnyHourOfCurrentDayWithFormat("yyyy-MM-dd-HH-mm-ss-SSS", "-10"));
		System.out.println(gt.getAnyMinuteOfCurrentHourWithFormat("yyyy-MM-dd-HH-mm-ss-SSS", "-10"));
	}

}
