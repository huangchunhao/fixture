package slim.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RedisDO {

	public Jedis jedis;
	public String ip;
	public int port = 6397; // Ĭ�϶˿�
	public int dbIndex = 1;// ������֤��Ĭ�ϴ���db1��
	public String realKey;
	public String s_value = null;
	public String s_modifyContent = null;

	public RedisDO() {
		// this.ip = "192.168.0.65";
	}

	public RedisDO(String ip, int port, String key) {
		this.ip = ip;
		this.realKey = key;
		this.port = port;
	}

	public RedisDO(String ip, int port, int dbIndex, String key) {
		this.ip = ip;
		this.realKey = key;
		this.dbIndex = dbIndex;
		this.port = port;
		this.jedis = this.open();
	}

	public void close() {
		jedis.disconnect();
		jedis = null;
	}

	public Jedis open() {
		//log.info("begin to open redis, the ip is " + this.ip + " the port is " + this.port);
		JedisPoolConfig myConfig = new JedisPoolConfig();
		myConfig.setMaxTotal(100);
		myConfig.setMaxIdle(20);
		myConfig.setMaxWaitMillis(1000);

		JedisPool pool;
		pool = new JedisPool(myConfig, this.ip, this.port);

		boolean borrowOrOprSuccess = true;
		try {
			jedis = pool.getResource();
			// do redis opt by instance
		} catch (JedisConnectionException e) {
			borrowOrOprSuccess = false;
			if (jedis != null)
				pool.returnBrokenResource(jedis);

		} finally {
			if (borrowOrOprSuccess)
				pool.returnResource(jedis);
		}
		jedis = pool.getResource();
		return jedis;
	}

	public String getValue() {
		// RedisDO rd = new RedisDO();
		// rd.open();

		//log.info("begin to get value, the key is " + this.realKey + " the dbindex is " + this.dbIndex);
		this.jedis.select(this.dbIndex);
		Set s = this.jedis.keys("*");
		Iterator it = s.iterator();

		while (it.hasNext()) {
			String internalKey = (String) it.next();
			List<String> value = this.jedis.mget(internalKey);

			if (this.realKey.equals(internalKey)) {
				// System.out.println(internalKey + "-----" + value);
				this.s_value = value.get(0);
				break;
			}
		}

		this.close();
		return this.s_value;
	}
	
	public Set<String> getkeys(String KeyPattern) {
		// RedisDO rd = new RedisDO();
		// rd.open();
		Set<String> KeySet = null;
		//log.info("begin to GET keyset, the key pattern is " + KeyPattern + " the dbindex is " + this.dbIndex);
		try {
			this.jedis.select(this.dbIndex);
			KeySet = this.jedis.keys(KeyPattern + "*");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return KeySet;
	}

	public boolean delKey() {
		// RedisDO rd = new RedisDO();
		// rd.open();

		boolean flag = false;
		//log.info("begin to delete key, the key is " + this.realKey + " the dbindex is " + this.dbIndex);
		try {
			this.jedis.select(this.dbIndex);
			this.jedis.del(this.realKey);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}

		/*
		 * Set s = this.jedis.keys("*"); Iterator it = s.iterator();
		 * 
		 * while (it.hasNext()) { String internalKey = (String) it.next();
		 * List<String> value = this.jedis.mget(internalKey);
		 * 
		 * if (this.realKey.equals(internalKey)) {
		 * //System.out.println(internalKey + "-----" + value); this.s_value =
		 * value.get(0); break; } }
		 */

		this.close();
		return flag;
	}
	
	public boolean delKey(String s_key) {
		// RedisDO rd = new RedisDO();
		// rd.open();
		boolean flag = false;
		//log.info("begin to delete key, the key is " + s_key + " the dbindex is " + this.dbIndex);
		try {
			this.jedis.select(this.dbIndex);
			this.jedis.del(s_key);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public boolean modifyValue(String s_modifyContent) {
		this.s_modifyContent = s_modifyContent;
		boolean flag = false;
		//log.info("begin to modify value, the key is " + this.realKey + " the content is " + this.s_modifyContent);
		try {
			this.jedis.select(this.dbIndex);
			String code=this.jedis.set(this.realKey, this.s_modifyContent);
			//log.debug("the code status is " + code);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		this.close();
		return flag;
	}

	public boolean addKey(String s_Content) {
		this.s_value=s_Content;
		boolean flag = false;
		//log.debug("begin to add key, the key is " + this.realKey + " the content is " + this.s_value);
		try {
			this.jedis.select(this.dbIndex);
			String code = this.jedis.set(this.realKey, this.s_value);
			//log.debug("the code status is " + code);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		this.close();
		return flag;
	}
	
	public Long getTTL(String key){
		Long time = null;
		try{
			this.jedis.select(this.dbIndex);
			time = this.jedis.ttl(key);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	
	public long setTTL(String key,int seconds){
		Long result = null;
		try{
			this.jedis.select(this.dbIndex);
			result=this.jedis.expire(key, seconds);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isExists() {
		boolean flag = false;
		//log.debug("begin to judge key, the key is " + this.realKey);
		try {
			this.jedis.select(this.dbIndex);
			flag = this.jedis.exists(this.realKey);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		this.close();
		return flag;
	}
}
