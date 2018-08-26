package slim.redis;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maiya.util.MLog;

public class RedisOperation {
	static Logger log = LogManager.getLogger("com.maiya");
	static String s_clog = " Redis_Operation: ";

	// static Logger log = MLog.getLogger();
	public String redisIp;
	public int i_dbindex;
	public int i_port = 6397;
	public String s_Value;
	public String s_key;

	public RedisOperation(String redisIp) {
		log.info("######" + s_clog + "######" + "Create a RedisOperation");
		log.debug(s_clog + " redisIp is " + redisIp);
		this.redisIp = redisIp;
	}

	/**
	 * <p>
	 * ��ȡ��ȡredis����ʵ��
	 * </p>
	 * <p>
	 * ���ӣ�
	 * </p>
	 * <p>
	 * |script|!-RedisOperation-! |192.168.0.65 |6379 |
	 * </p>
	 * <p>
	 * |$smscode= |get value by key from
	 * index;|myph:sms:login:13770000001|1|\d{6}|
	 * </p>
	 * 
	 * @param redisIp
	 *            redis��ip��ַ
	 * @param s_port
	 *            �˿�
	 */
	public RedisOperation(String redisIp, String s_port) {
		log.info("######" + s_clog + "######" + "Create a RedisOperation");
		log.debug(s_clog + " the redisIp is " + redisIp + " the port is " + s_port);
		this.redisIp = redisIp;
		try {
			this.i_port = Integer.parseInt(s_port);
		} catch (NumberFormatException e) {
			log.debug("the s_port can not be translated to integer");
			log.debug(e);
			e.printStackTrace();
		}
	}

	/**
	 * ���ݹؼ��ֻ�ȡֵ
	 * 
	 * @param key
	 *            �ؼ���
	 * @param s_dbindex
	 *            dbindex
	 * @return content
	 */
	public String getValueByKeyFromIndex(String key, String s_dbindex) {
		log.debug(s_clog + " get the value of " + key + " at db index  " + s_dbindex);
		this.s_key = key;
		try {
			this.i_dbindex = Integer.parseInt(s_dbindex);
		} catch (NumberFormatException e) {
			log.debug(e);
			log.info("the dbindex can not be translated to integer");
			e.printStackTrace();
		}

		RedisDO rd = new RedisDO(this.redisIp, this.i_port, this.i_dbindex, this.s_key);
		this.s_Value = rd.getValue();
		log.debug(s_clog + " the value of " + key + " is  " + this.s_Value);
		return this.s_Value;
	}

	/**
	 * ���ݹؼ��ֻ�ȡֵ
	 * 
	 * @param key
	 *            �ؼ���
	 * @param s_dbindex
	 *            dbindex
	 * @param s_regx
	 *            ������ʽ
	 * @return content
	 */
	public String getValueByKeyFromIndex(String key, String s_dbindex, String s_regx) {
		log.debug(s_clog + " get the value of " + key + " at db index  " + s_dbindex);
		try {
			this.i_dbindex = Integer.parseInt(s_dbindex);
		} catch (NumberFormatException e) {
			log.debug(e);
			log.info("the dbindex can not be translated to integer");
			e.printStackTrace();
		}

		this.s_Value = this.getValueByKeyFromIndex(key, s_dbindex);
		log.debug(s_clog + " the value of " + key + " is  " + this.s_Value);
		if (this.s_Value != null) {
			Pattern pn = Pattern.compile(s_regx);
			Matcher m = pn.matcher(this.s_Value);
			while (m.find()) {
				this.s_Value = m.group();
			}
		} else {
			this.s_Value = "notExist";
		}
		log.debug(s_clog + " the value of " + key + " is  " + this.s_Value + " after hold with regx " + s_regx);
		return this.s_Value;
	}

	public String getTTLByKeyFromIndex(String key, String s_dbindex) {
		log.debug(s_clog + " get the ttl of " + key + " at db index  " + s_dbindex);
		try {
			this.i_dbindex = Integer.parseInt(s_dbindex);
		} catch (NumberFormatException e) {
			log.debug(e);
			log.info("the dbindex can not be translated to integer");
			e.printStackTrace();
		}

		Long time = null;
		RedisDO rd = new RedisDO(this.redisIp, this.i_port, this.i_dbindex, this.s_key);
		time = rd.getTTL(key);

		return Long.toString(time);
	}

	public boolean setTTLByKeyFromIndex(String key, String s_dbindex, int seconds) {
		log.debug(s_clog + " set the ttl of " + key + " at db index  " + s_dbindex + " and the expire time is "
				+ seconds);
		try {
			this.i_dbindex = Integer.parseInt(s_dbindex);
		} catch (NumberFormatException e) {
			log.debug(e);
			log.info("the dbindex can not be translated to integer");
			e.printStackTrace();
		}

		Long result = null;
		RedisDO rd = new RedisDO(this.redisIp, this.i_port, this.i_dbindex, this.s_key);
		result = rd.setTTL(key, seconds);

		if (result.toString().equals("1")) {
			log.debug(s_clog + " set successfully");
			return true;
		} else {
			log.debug(s_clog + " Failed to set");
			return false;
		}

	}

	/**
	 * ɾ������
	 * 
	 * @param key
	 *            �ؼ���
	 * @param s_dbindex
	 *            dbindex
	 * @return ɾ�����
	 */
	public boolean deleteValueByKeyFromIndex(String key, String s_dbindex) {
		log.debug(s_clog + " delete the key named " + key + " at db index  " + s_dbindex);
		boolean flag = false;

		this.s_key = key;
		try {
			this.i_dbindex = Integer.parseInt(s_dbindex);
		} catch (NumberFormatException e) {
			log.debug(e);
			log.info("the dbindex can not be translated to integer");
			e.printStackTrace();
		}

		try {
			RedisDO rd = new RedisDO(this.redisIp, this.i_port, this.i_dbindex, this.s_key);
			flag = rd.delKey();
			log.debug(s_clog + " delete the key named " + key + " successfully");
			flag = true;
		} catch (Exception e) {
			log.debug(s_clog + " Fail to delete the key named " + key);
			log.debug(e);
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * ����ɾ������
	 * 
	 * @param key
	 *            �ؼ���
	 * @param s_dbindex
	 *            dbindex
	 * @return ɾ�����
	 */
	public boolean deleteBatchByKeyFromIndex(String pre_str, String s_dbindex) {
		log.debug(s_clog + " delete the keys contained the content " + pre_str + " at db index  " + s_dbindex);
		boolean flag = false;
		try {
			this.i_dbindex = Integer.parseInt(s_dbindex);
		} catch (NumberFormatException e) {
			log.debug(e);
			log.info("the dbindex can not be translated to integer");
			e.printStackTrace();
		}

		String s_key = null;
		try {
			RedisDO rd = new RedisDO(this.redisIp, this.i_port, this.i_dbindex, null);
			Set<String> set = rd.getkeys(pre_str);
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				s_key = it.next();
				flag = rd.delKey(s_key);
				log.debug(s_clog + " delete the key named " + s_key + " successfully");
			}
			rd.close();
		} catch (Exception e) {
			log.debug(e);
			log.debug(s_clog + " Fail to delete the key named " + s_key);
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * �޸Ļ�������
	 * 
	 * @param key
	 *            �ؼ���
	 * @param s_dbindex
	 *            dbindex
	 * @param s_modifyContent
	 *            ������
	 * @return �޸Ľ��
	 */
	public boolean modifyValueByKeyFromIndex(String key, String s_dbindex, String s_modifyContent) {
		log.debug(s_clog + " modify the key named " + key + " at db index  " + s_dbindex + " with value "
				+ s_modifyContent);
		boolean flag = false;

		this.s_key = key;
		try {
			this.i_dbindex = Integer.parseInt(s_dbindex);
		} catch (NumberFormatException e) {
			log.debug(e);
			log.info("the dbindex can not be translated to integer");
			e.printStackTrace();
		}

		try {
			RedisDO rd = new RedisDO(this.redisIp, this.i_port, this.i_dbindex, this.s_key);
			flag = rd.modifyValue(s_modifyContent);
			log.debug(s_clog + " modify the key named " + key + " successfully");
			flag = true;
		} catch (Exception e) {
			log.debug(s_clog + " Fail to modify the key named " + key);
			log.debug(e);
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @param key
	 *            �ؼ���
	 * @param s_dbindex
	 *            dbindex
	 * @param s_partoldContent
	 *            ���ִ��޸�����
	 * @param s_partModifyContent
	 *            ����������
	 * @return �޸Ľ��
	 */
	public boolean modifyValueByKeyFromIndex(String key, String s_dbindex, String s_partoldContent,
			String s_partModifyContent) {
		log.debug(s_clog + " modify the key named " + key + " at db index  " + s_dbindex + " with value "
				+ s_partModifyContent);
		boolean flag = false;

		this.s_key = key;
		try {
			this.i_dbindex = Integer.parseInt(s_dbindex);
		} catch (NumberFormatException e) {
			log.debug(e);
			log.info("the dbindex can not be translated to integer");
			e.printStackTrace();
		}

		try {
			RedisDO rd = new RedisDO(this.redisIp, this.i_port, this.i_dbindex, this.s_key);
			String oldValue = getValueByKeyFromIndex(key, s_dbindex);
			StringBuffer sb_holder = new StringBuffer(oldValue);
			int begin = sb_holder.indexOf(s_partoldContent);
			int old_length = s_partoldContent.length();
			sb_holder.delete(begin, begin + old_length);
			// int new_length = s_partModifyContent.length();
			sb_holder.insert(begin, s_partModifyContent);
			flag = rd.modifyValue(sb_holder.toString());
			log.debug(s_clog + " modify the key named " + key + " successfully");
			flag = true;
		} catch (Exception e) {
			log.debug(s_clog + " Fail to modify the key named " + key);
			log.debug(e);
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * ��ӻ���
	 * 
	 * @param s_dbindex
	 *            dbindex
	 * @param key
	 *            �ؼ���
	 * @param s_Content
	 *            ����
	 * @return ��ӽ��
	 */
	public boolean addValueByKeyFromIndex(String s_dbindex, String key, String s_Content) {
		log.debug(s_clog + " Add the key named " + key + " at db index  " + s_dbindex + " with value " + s_dbindex);
		boolean flag = false;
		this.s_key = key;
		try {
			this.i_dbindex = Integer.parseInt(s_dbindex);
		} catch (NumberFormatException e) {
			log.debug(e);
			log.info("the dbindex can not be translated to integer");
			e.printStackTrace();
		}

		try {
			RedisDO rd = new RedisDO(this.redisIp, this.i_port, this.i_dbindex, this.s_key);
			flag = rd.addKey(s_Content);
			log.debug(s_clog + " add the key named " + key + " successfully");
		} catch (Exception e) {
			log.debug(s_clog + " Fail to add the key named " + key);
			log.debug(e);
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * �жϹؼ����Ƿ����
	 * 
	 * @param s_dbindex
	 *            dbindex
	 * @param key
	 *            �ؼ���
	 * @return �������
	 */
	public boolean judgeKeyIsExists(String s_dbindex, String key) {
		log.debug(s_clog + " judge the key named " + key + " at db index  " + s_dbindex + " whether exist ");
		boolean flag = false;
		this.s_key = key;
		try {
			this.i_dbindex = Integer.parseInt(s_dbindex);
		} catch (NumberFormatException e) {
			log.info("the dbindex can not be translated to integer");
			e.printStackTrace();
		}

		try {
			RedisDO rd = new RedisDO(this.redisIp, this.i_port, this.i_dbindex, this.s_key);
			flag = rd.isExists();
			log.debug(s_clog + " the reslut is " + flag);
		} catch (Exception e) {
			log.debug(e);
			e.printStackTrace();
		}

		return flag;
	}

	public boolean waitUntilKeyExists(String s_dbindex, String key, int timestep, int times) {
		boolean flag = false;
		while (true) {
			flag = judgeKeyIsExists(s_dbindex, key);
			if (times == 0) {
				break;
			}
			if (flag) {
				break;
			} else {
				waitTime(timestep);
				times--;
			}
		}
		return flag;
	}
	
	public boolean waitUntilKeyNotExists(String s_dbindex, String key, int timestep, int times) {
		boolean flag = false;
		while (true) {
			flag = judgeKeyIsExists(s_dbindex, key);
			if (times == 0) {
				break;
			}
			if (!flag) {
				break;
			} else {
				waitTime(timestep);
				times--;
			}
		}
		return !flag;
	}

	public boolean waitUntilValueExists(String s_dbindex, String key, String value, int timestep, int times) {
		boolean flag = false;
		while (true) {
			flag = judgeKeyIsExists(s_dbindex, key) && getValueByKeyFromIndex(key, s_dbindex).equals(value);
			if (times == 0) {
				break;
			}
			if (flag) {
				break;
			} else {
				waitTime(timestep);
				times--;
			}
		}
		return flag;
	}

	public boolean waitUntilValueExistsByReg(String s_dbindex, String key, String value, String s_regx, int timestep,
			int times) {		
		boolean flag = false;
		while (true) {
			flag = judgeKeyIsExists(s_dbindex, key) && getValueByKeyFromIndex(key, s_dbindex, s_regx).equals(value);
			if (times == 0) {
				break;
			}
			if (flag) {
				break;
			} else {
				waitTime(timestep);
				times--;
			}
		}
		return flag;
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

	public boolean plog(String s) {
		log.info(s);
		return true;
	}
}
