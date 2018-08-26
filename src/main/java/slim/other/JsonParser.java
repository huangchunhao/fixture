package slim.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.maiya.XDFixture;

public class JsonParser {
	static Logger log = LogManager.getLogger("com.maiya");
	static String s_clog = " Json_Parser: ";
	private String Content = null;

	public JsonParser(String jsonContent) {
		log.info("######"+s_clog+"######"+" Create a Json Parser");
		this.Content = jsonContent;
		log.debug(s_clog+" the string content is "+jsonContent);
	}

	public JsonParser() {
		log.info("######"+s_clog+"######"+" Create a Json Parser");
	}

	/**
	 * ����Ӧ��json��Ϣ�л�ȡ�ؼ��ֵ�ֵ
	 * 
	 * @param key
	 *            �ؼ���
	 * @return ֵ
	 */
	public String getValueFromJsonResult(String key) {
		log.debug(s_clog+" the key is " + key);
		String value = null;
		if (this.Content.isEmpty()) {
			log.debug(s_clog+"the json content is null!");
		} else {
			JSONObject json = new JSONObject(this.Content);
			value = getValueFromJsonResult(json, key);
		}
		log.debug(s_clog+" the result is " + value);
		return value;
	}

	/**
	 * ��getValueFromJsonResult(key)����
	 * 
	 * @param json
	 *            ��Ӧ����
	 * @param key
	 *            �ؼ���
	 * @return ֵ
	 */
	private String getValueFromJsonResult(JSONObject json, String key) {
		String value = null;
		// JSONObject json = new JSONObject(this.jsonResult);
		HashSet<String> hSet = new HashSet<String>();
		if (json.has(key)) {
			try {
				value = json.get(key).toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			log.debug("Function: getKeyString" + " | "
					+ "Content: key does not exist in current json, need do further seeking");
			// get keys in json
			hSet = getKeySetOnJSON(json);
			log.debug("Function: getKeyString" + " | " + "Content: keys in current json is " + hSet.toString());
			Iterator<String> iterator = hSet.iterator();
			while (iterator.hasNext()) {
				String i = iterator.next();
				log.debug("Function: getKeyString" + " | " + "Content: current key is " + i + ", target key is " + key);

				if (isJSONObject(json, i)) {
					try {
						JSONObject tp = (JSONObject) json.get(i);
						log.debug("Function: getKeyString" + " | " + "Content: the value of " + i + " is "
								+ tp.toString());

						HashSet<String> hSet2 = new HashSet<String>();
						hSet2 = getKeySetOnJSON(tp);
						log.debug("Function: getKeyString" + " | " + "Content: Keys are " + hSet2.toString()
								+ " in the value of " + i);

						if (tp.has(key)) {
							value = tp.get(key).toString();
							break;
						} else {
							log.debug("Function: getKeyString" + " | " + "Content: " + key + "does exist on "
									+ tp.toString() + ", then need recall this function to do recursion");
							getValueFromJsonResult(tp, key);
						}

					} catch (JSONException e) {

						e.printStackTrace();
					}
				}
			}
		}
		return value;
	}

	/**
	 * ��getValueFromJsonResult(json,key)���ã����ڻ�ȡjson��������ϵ�����key
	 * 
	 * @param json
	 *            json����
	 * @return sets
	 */
	private HashSet<String> getKeySetOnJSON(JSONObject json) {
		HashSet<String> hSet = new HashSet<String>();
		Iterator<String> iterator = json.keys();
		while (iterator.hasNext()) {
			String i = iterator.next();
			hSet.add(i);
		}
		return hSet;
	}

	/**
	 * �ж�key��json�������Ƿ��Ƕ���
	 * 
	 * @param json
	 *            json����
	 * @param key
	 * @return �ǻ��߷�
	 */
	private boolean isJSONObject(JSONObject json, String key) {
		boolean flag = false;
		try {
			try {
				JSONObject tp = (JSONObject) json.get(key);
				flag = true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (ClassCastException e) {
			log.debug("Function: isJSONObject" + " | " + "Content: is not JSON Object");
		}
		return flag;
	}

	/**
	 * �ж�content���Ƿ����key
	 * 
	 * @param key
	 *            �ؼ���
	 * @param content
	 *            ����
	 * @return �ǻ��߷�
	 */
	public boolean isContainOn(String key, String content) {
		log.debug(s_clog+" the content is " + content);
		log.debug(s_clog+" the key is " + key);
		boolean result = false;
		if (!content.isEmpty()) {
			if (content.contains(key)) {
				result = true;
			} else {
				result = false;
			}
		} else {
			log.debug(s_clog+"the content is empty");
		}
		log.debug(s_clog+"the result is "+result);
		return result;
	}
	
	public boolean isContainOn(String key) {
		log.debug(s_clog+" the content is " + this.Content);
		log.debug(s_clog+" the key is " + key);
		boolean result = false;
		if (!this.Content.isEmpty()) {
			if (this.Content.contains(key)) {
				result = true;
			} else {
				result = false;
			}
		} else {
			log.debug(s_clog+"the content is empty");
		}
		log.debug(s_clog+"the result is "+result);
		return result;
	}

	/**
	 * ������ʽƥ�䷽��
	 * 
	 * @param regular
	 *            ������ʽ
	 * @param content
	 *            ����
	 * @return ƥ����
	 */
	public boolean isMatched(String regular, String content) {
		log.debug(s_clog+" the content is " + content);
		log.debug(s_clog+" the regular is " + regular);
		boolean result = false;
		Pattern p1 = Pattern.compile(regular);
		Matcher m = p1.matcher(content);
		result = m.find();
		log.debug(s_clog+" the match result is " + result);
		return result;
	}

	/**
	 * ������ͨ��������ʽ��content1 ��content2 �й��˳���Ӧ���ݣ����Ƚϡ�
	 * 
	 * @param content1
	 *            �ı�1
	 * @param content2
	 *            �ı�2
	 * @param regular
	 *            ������ʽ
	 * @return �ȶԽ��
	 */
	public boolean compareContentAfterFilter(String content1, String content2, String regular) {
		log.debug(s_clog+"content1 is " + content1 + " content2 is " + content2 + " regular is " + regular);
		boolean result = false;
		Pattern p1 = Pattern.compile(regular);
		Pattern p2 = Pattern.compile(regular);
		Matcher m1 = p1.matcher(content1);
		Matcher m2 = p2.matcher(content2);
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		while (m1.find()) {
			String c1 = m1.group();
			log.debug(c1 + " " + "start:" + m1.start() + ",end: " + m1.end());
			sb1.append(c1);
		}
		log.debug("content1AfterFilter is " + sb1.toString());
		while (m2.find()) {
			String c2 = m2.group();
			log.debug(c2 + " " + "start: " + m2.start() + ",end:" + m2.end());
			sb2.append(c2);
		}
		log.debug("content2AfterFilter is " + sb2.toString());
		result = sb2.toString().contains(sb1.toString());
		log.debug("the compare result is " + result);
		return result;
	}

	/**
	 * ��ƥ�䵽��������list��ʽ���أ���Ҫ����json list������ȡֵ��Ϊ��
	 * 
	 * @param content
	 *            ��ʼ����
	 * @param regular
	 *            ������ʽ
	 * @return ƥ������
	 */
	public String getValueFromList(String content, String regular) {
		log.debug(s_clog+" the content is " + content + " the regular is " + regular);
		List<String> s_lo = new ArrayList<String>(); 
		List<String> s_lr = new ArrayList<String>();
		Pattern p1 = Pattern.compile(regular);
		Matcher m1 = p1.matcher(content);
		while (m1.find()) {
			String c1 = m1.group();
			log.debug(c1 + " " + "start:" + m1.start() + ",end: " + m1.end());
			s_lo.add(c1);
		}
		Iterator<String> iterator = s_lo.iterator();
		while (iterator.hasNext()) {
			String s = iterator.next();
			s_lr.add(getValueFromString(s));
		}
		log.debug(s_clog+" the result is " + s_lr.toString());
		return s_lr.toString();
	}

	/**
	 * �ԣ��ָȡ�ڶ������ݣ�������ɾ��
	 * 
	 * @param s
	 *            �ı�����
	 * @return ������
	 */
	private String getValueFromString(String s) {
		String ss = s.split(":")[1];
		String sss = ss.replace("\"", "");
		return sss;
	}

	/**
	 * ͨ��������ʽ���ڹ�������ʵ��ʱ�ṩ��content�л�ȡ����
	 * 
	 * @param regular
	 *            ������ʽ
	 * @return ��ȡ������
	 */
	public String getStringByReg(String regular) {
		//log.debug("the content is " + this.Content);
		log.debug(s_clog+"the regular is " + regular);
		String result = null;
		Pattern p1 = Pattern.compile(regular);
		Matcher m1 = p1.matcher(this.Content);
		if (m1.find()) {
			result = m1.group();
			log.debug(s_clog +result + " " + "start:" + m1.start() + ",end: " + m1.end());
		}
		log.debug(s_clog + "the result is null ");
		return result;

	}

	/**
	 * ͨ��������ʽ�����ṩ��content�л�ȡ����
	 * @param dircetContent �ı�����
	 * @param regular ������ʽ
	 * @return ��ȡ��������
	 */
	public String getStringByReg(String dircetContent, String regular) {
		log.debug(s_clog+ " the content is " + dircetContent);
		log.debug(s_clog+ " the regular is " + regular);
		String result = null;
		Pattern p1 = Pattern.compile(regular);
		Matcher m1 = p1.matcher(dircetContent);
		if (m1.find()) {
			result = m1.group();
			log.debug(s_clog +result + " " + "start:" + m1.start() + ",end: " + m1.end());
		}
		log.debug(s_clog + "the result is null ");
		return result;

	}

	/**
	 * ��map�У�����key��ȡֵ
	 * @param map map
	 * @param key �ؼ���
	 * @return ��ȡ��������
	 */
	public String getValueFromMap(Map<String, String> map, String key) {
		log.debug(s_clog + "the size of map is " + map.size()+ " "+map.toString());
		log.debug(s_clog + "the key is " + key);
		String value = map.get(key);
		log.debug(s_clog + "the value is " + value);
		return value;
	}
	
	public boolean plog(String s) {
		log.info(s);
		return true;
	}
}
