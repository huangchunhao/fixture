package slim.http;

import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import slim.db.DataContent;
import slim.db.ReqTemplete;
import slim.other.GsonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XDFixture {
	static Logger log = LogManager.getLogger("com.maiya");
	static String s_clog = " http_interface: ";
	private HttpFixture hf;
	private int methodFlag = 0;// 0=post;1=get;2=post-g
	private String jsonResult = null;
	private String s_StatusLine = null;
	private String s_ResponseContent = null;
	private List<String> resultList = new ArrayList<String>();
	private Map<String, String> headMap = new HashMap<String, String>();
	private List<HttpFixture> hfl = new ArrayList<HttpFixture>();

	private List<String> billList = new ArrayList<String>();
	private int billListNum = 0;

	private LoopLogicContainer llc;
	private int stepType = 0;// 0:HttpPostgStep 1:OracleQueryStep
	private HttpPostgStep hpgs;
	private OracleQueryStep oqs;
	
	//���ڲ������ݿⷽ��
	private ReqTemplete rtp = new ReqTemplete();
	private List<DataContent> data=new ArrayList<DataContent>();

	/**
	 * Constructor Fixture for maiyadai
	 */
	public XDFixture() {
		log.info("============"+s_clog+"============ " + "Create a Http Request.");
	}

	/**
	 * ������com.maiya.xd=== ��ȡ��ǰxdFixtureʵ��
	 * 
	 * @return current xdFixture instance;
	 * 
	 */
	public XDFixture getCurrentObject() {
		log.info("============"+s_clog+"============ " + "Get current http request instance.");
		return this;
	}

	/**
	 * ��ȡ��ǰhttpFixtureʵ��
	 * 
	 * @return current HttpFixture instance
	 */
	public HttpFixture getCurrentHttpFixture() {
		return this.hf;
	}

	/**
	 * Ϊhttp�������÷�����post get ���� postg
	 * 
	 * @param method
	 *            the method should be post|get|postg
	 * @return result the result for the setting
	 */
	public boolean setHttpMethod(String method) {
		boolean result = false;
		log.debug(s_clog + "Set the method of current http as " + method);

		this.hf = new HttpFixture(method);

		if ("post".equals(method.toLowerCase().trim())) {
			this.methodFlag = 0;
		}

		if ("get".equals(method.toLowerCase().trim())) {
			this.methodFlag = 1;
		}

		if ("postg".equals(method.toLowerCase().trim())) {
			this.methodFlag = 2;
		}

		if (this.hf != null) {
			result = true;
		}
		return result;
	}

	/**
	 * Ϊpost��������uri
	 * 
	 * @param host
	 *            Example��192.168.0.65:8081
	 * @param path
	 *            Example��/myLoan-mobile/action/api/AdBanner
	 * @return Setting result
	 */
	public boolean setUriForPostRequest(String host, String path) {
		log.debug(s_clog + "add URI" + " host: " + host + "path: " + path);
		boolean result = this.hf.setUriForPostRequest(host, path);
		return result;
	}

	/**
	 * Ϊget��������uri
	 * 
	 * @param host
	 *            Example��192.168.0.65:8081
	 * @param path
	 *            Example��/myLoan-mobile/action/api/AdBanner
	 * @param paramsMap
	 *            a map about params.
	 *            Example��!{version:2.1.0,build:6,deviceNo:863837023689236,
	 *            client:1,channel:!-AppStore-!}
	 * @return Setting result
	 */
	public boolean setUriForGetRequest(String host, String path, Map<String, String> paramsMap) {
		log.debug(s_clog + " add URI and paramsMap " + " host: " + host + " path: " + path + " paramsMap: "
				+ paramsMap.toString());
		boolean result = this.hf.setUriForGetRequest(host, path, paramsMap);
		return result;
	}

	/**
	 * Ϊpost��������uri
	 * 
	 * @param host
	 *            Example��192.168.0.65:8081
	 * @param path
	 *            Example��/myLoan-mobile/action/api/AdBanner
	 * @param paramsMap
	 *            a map about params.
	 *            Example��!{version:2.1.0,build:6,deviceNo:863837023689236,
	 *            client:1,channel:!-AppStore-!}
	 * @return Setting result
	 */
	public boolean setUriForPostgRequest(String host, String path, Map<String, String> paramsMap) {
		log.debug(s_clog + " add URI and paramsMap " + " host: " + host + " path: " + path + " paramsMap: "
				+ paramsMap.toString());
		boolean result = this.hf.setUriForPostgRequest(host, path, paramsMap);
		return result;
	}

	/**
	 * Ϊ��������header��Ϣ��Content-Type �� application/x-www-form-urlencoded��
	 * 
	 * @param headerInfo
	 *            a map about headerinfo.
	 *            Example��!{version:2.1.0,build:6,deviceNo:863837023689236,
	 *            client:1,channel:!-AppStore-!}
	 * @return Setting result
	 */
	public boolean setHeaderForRequest(Map<String, String> headerInfo) {
		log.debug(s_clog + " add header " + " headerInfo: " + headerInfo.toString());
		boolean result = false;
		if (this.methodFlag == 0) {
			result = this.hf.setHeaderForPostRequest(headerInfo);
		} else if (this.methodFlag == 1) {
			result = this.hf.setHeaderForGetRequest(headerInfo);
		} else if (this.methodFlag == 2) {
			result = this.hf.setHeaderForPostRequest(headerInfo);
		}
		return result;
	}

	// for Json body
	/**
	 * Ϊ��������header��Ϣ��Content-Type ��application/json��
	 * 
	 * @param headerInfo
	 *            a map about headerinfo.
	 *            Example��!{version:2.1.0,build:6,deviceNo:863837023689236,
	 *            client:1,channel:!-AppStore-!}
	 * @return Setting result
	 */
	public boolean setHeaderForJsonRequest(Map<String, String> headerInfo) {
		log.debug(s_clog + " add header for JsonRequest " + " headerInfo: " + headerInfo.toString());
		boolean result = false;
		if (this.methodFlag == 0) {
			result = this.hf.setHeaderForPostJsonRequest(headerInfo);
		} else if (this.methodFlag == 1) {
			result = this.hf.setHeaderForGetJsonRequest(headerInfo);
		} else if (this.methodFlag == 2) {
			result = this.hf.setHeaderForPostJsonRequest(headerInfo);
		}
		return result;
	}

	// for Multipart
	/**
	 * Ϊ��������header��Ϣ��Content-Type ��multipart/form-data"+";
	 * boundary="+"\""+multipartBoundary+"\"��
	 * 
	 * @param headerInfo
	 *            a map about headerinfo.
	 *            Example��!{version:2.1.0,build:6,deviceNo:863837023689236,
	 *            client:1,channel:!-AppStore-!}
	 * @param type
	 *            Multipart
	 * @return Setting result
	 */
	public boolean setHeaderForMultipartRequest(Map<String, String> headerInfo) {
		log.debug(s_clog + " Add header for MultipartRequest " + " headerInfo: " + headerInfo.toString());
		boolean result = false;
		if (this.methodFlag == 0) {
			result = this.hf.setMultipartHeaderForPostRequest(headerInfo);
		} else if (this.methodFlag == 1) {
			result = this.hf.setMultipartHeaderForGetRequest(headerInfo);
		} else if (this.methodFlag == 2) {
			result = this.hf.setMultipartHeaderForPostRequest(headerInfo);
		}
		return result;
	}

	// set
	// entity---begin-----------------------------------------------------------

	// For post
	/**
	 * ���post���󣬲�������key=value�ĸ�ʽ
	 * 
	 * @param ParamsMap
	 *            a map about headerinfo.
	 *            Example��!{version:2.1.0,build:6,deviceNo:863837023689236,
	 *            client:1,channel:!-AppStore-!}
	 * @return Setting result
	 */
	public boolean setMapParamsForRequest(Map<String, String> ParamsMap) {
		log.debug(s_clog + " Add Key-Value ParamsMap " + " ParamsMap: " + ParamsMap.toString());
		//String companyName = ParamsMap.get("companyName");
		//LogPrint.print("----------------------" + companyName + "-------------------------");
		boolean result = false;
		result = this.hf.setParamsForPostRequest(ParamsMap);
		result = this.hf.setEntityForPostRequest();
		return result;
	}

	/**
	 * ���post���󣬲�������key=value�ĸ�ʽ����setMapParamsForRequest��������û�н�����ֵ����UrlEncode���롣
	 * ������ֵ�г���%����ʹ�����������
	 * 
	 * @param ParamsMap
	 *            a map about headerinfo.
	 *            Example��!{version:2.1.0,build:6,deviceNo:863837023689236,
	 *            client:1,channel:!-AppStore-!}
	 * @return Setting result
	 */
	public boolean setMapParamsForRequestWithNoTrans(Map<String, String> ParamsMap) {
		log.debug(s_clog + " Add Key-Value ParamsMap(not encode with UrlEncode) " + " ParamsMap: " + ParamsMap.toString());
		boolean result = false;
		result = this.hf.setParamsForPostRequestWithNoTrans(ParamsMap);
		result = this.hf.setEntityForPostRequestWithNoTrans();
		return result;
	}

	// For post
	/**
	 * ���post���󣬲�������json����
	 * 
	 * @param json
	 *            a map about headerinfo.
	 *            Example��!{version:2.1.0,build:6,deviceNo:863837023689236,
	 *            client:1,channel:!-AppStore-!}
	 * @return Setting result
	 */
	public boolean setJsonParamsForRequest(String json) {
		log.debug(s_clog + " Add json params " + " json: " + json);
		boolean result = false;
		result = this.hf.setJsonEntityForPostRequest(json);
		return result;
	}
	
	public boolean addReplaceContentToReqTemplete(String tableName,Map<String, String> content){
		Map<String,Object> datamap = new HashMap<String,Object>();
		datamap.put("tableName", tableName);
		datamap.put("content", content);
		Gson g= new Gson();
		String json = g.toJson(datamap);
		DataContent dc = GsonUtil.parseJsonWithGson(json, DataContent.class);
		data.add(dc);
		return true;	
	}
	
	public boolean addBasicInfoToReqTemplete(Map<String, String> ParamsMap){
		//DataContent dc=new DataContent();
		rtp.setDbtype(ParamsMap.get("dbtype"));
		rtp.setIp(ParamsMap.get("ip"));
		rtp.setSid(ParamsMap.get("sid"));
		rtp.setUser(ParamsMap.get("user"));
		rtp.setPw(ParamsMap.get("pw"));
		rtp.setBusinessType(ParamsMap.get("businessType"));
		rtp.setData(this.data);
		return true;
	}
	

	
	//�����������������ݳ�ʼ��
	public boolean setJsonParamsForRequestWithMap() {		
		log.debug(s_clog + " Add json params " + " json: " + this.rtp.toString());
		boolean result = false;
		result = this.hf.setJsonEntityForPostRequest(this.rtp.toString());
		return result;
	}
	
	//����������xjbk
	public boolean setJsonParamsForRequestWithMap(Map<String, String> ParamsMap) {		
		log.debug(s_clog + " Add json params " + " jsonmap: " + ParamsMap.toString());
		Gson g = new Gson();
		String jsonContent = g.toJson(ParamsMap);
		boolean result = false;
		result = this.hf.setJsonEntityForPostRequest(jsonContent);
		return result;
	}
	//����������xjbk
	public boolean setJsonParamsForRequestWithMaps(Map<String, String> user_info,Map<String, String> order_info) {
		Map<String, Map<String, String>> ParamsMap = new HashMap<String, Map<String, String>>();
		ParamsMap.put("user_info", user_info);
		ParamsMap.put("order_info", order_info);
		log.debug(s_clog + " Add json params " + " jsonmap: " + ParamsMap.toString());		
		Gson g = new Gson();
		String jsonContent = g.toJson(ParamsMap);
		boolean result = false;
		result = this.hf.setJsonEntityForPostRequest(jsonContent);
		return result;
	}

	/*
	 * public boolean setFileForRequest(String filePath,String fileName,String
	 * contentType){ boolean result=false; String sep =
	 * System.getProperty("file.separator"); String path = filePath + sep +
	 * fileName; File file = new File(path); result=
	 * this.hf.setFileEntityForPostRequest(file, contentType); return result; }
	 */

	// For Multipart--step 1
	/**
	 * ���post�ϴ��฽��������Ҫ��setHeaderForRequest(headerInfo,type)���ã�
	 * 
	 * @return �������
	 */
	public boolean createMultipartForRequest() {
		log.debug(s_clog + " Create a Multipart container " );
		boolean result = false;
		result = this.hf.createMultipartEntityBuilder();
		return result;
	}

	// For Multipart--step 2--for file
	/**
	 * ���post�ϴ��฽������������entity���ļ�
	 * 
	 * @param filePath
	 *            �ļ�·�� ���������ļ����� ���磺/home/maiya/fitnesse/info
	 * @param fileName
	 *            �ļ����� ���磺***��jpg
	 * @param contentType
	 *            �ļ����� ���磺image/jpeg
	 *            ���ο�http://www.w3school.com.cn/media/media_mimeref.asp��
	 * @return ��ӽ��
	 */
	public boolean addFileEntityToMultipartEntity(String fieldname, String filePath, String fileName,
			String contentType) {
		log.debug(s_clog + " Add a file attachment  " + " fieldname: " +fieldname+" filePath: "+filePath+" fileName: "+fileName+" contentType: "+contentType);
		boolean result = false;
		String sep = System.getProperty("file.separator");
		String path = filePath + sep + fileName;				
		File file = new File(path);
		if (file.exists()) {
			log.debug("File path is:  " + path);
			result = this.hf.createFilebodyAndAddToMultipart(fieldname, file, contentType);
		} else {
			log.debug(filePath + "/" + fileName + " does not exist!");
			try {
				throw new FileNotFoundException(filePath + "/" + fileName + " does not exist!");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// For Multipart--step 2--for string
	/**
	 * ���post�ϴ��฽������������entity��String
	 * 
	 * @param string
	 *            ����
	 * @param contentType
	 *            ����
	 * @return ��ӽ��
	 */
	public boolean addStringEntityToMultipartEntity(String fieldname, String string, String contentType) {
		boolean result = false;
		log.debug(s_clog + " Add a string attachment  " + " fieldname: " +fieldname+" string: "+string+" contentType: "+contentType);
		result = this.hf.createStringbodyAndAddToMultipart(fieldname, string, contentType);
		return result;
	}

	// For Multipart--step 3
	/**
	 * ��Multipart���͵����ݼ���http������
	 * 
	 * @return ���ý��
	 */
	public boolean setMultipartForRequest() {
		boolean result = false;
		log.debug(s_clog + " put the Multipart container into http request  ");
		result = this.hf.setHttpEntityForPostRequest();
		return result;
	}

	// set
	// entity--end------------------------------------------------------------

	/*
	 * private String getTokenOnResponse(){ String token; token =
	 * this.hf.getCookieOnResponseByPostRequest(); return token; }
	 */

	/**
	 * ������Ӧ��������а���StatusLine��responseContent
	 * 
	 * @return ��Ӧ���
	 */
	public String getJsonResponse() {
		log.info(s_clog + " Get response  ");
		this.resultList.clear();
		String result = null;

		if (this.methodFlag == 0) {
			this.hf.getResponseForPostRequest(this.headMap, this.resultList);
		} else if (this.methodFlag == 1) {
			this.hf.getResponseForGetRequest(this.headMap, this.resultList);
		} else if (this.methodFlag == 2) {
			this.hf.getResponseForPostRequest(this.headMap, this.resultList);
		}
		if (this.resultList.size() > 0) {
			if (this.resultList.size() < 2) {
				result = this.resultList.get(0);
			} else {
				result = this.resultList.get(0) + " " + this.resultList.get(1);
			}
		} else {
			log.debug("Can not get response!");
		}
		this.jsonResult = result;
		log.debug(s_clog + " response content:" + this.jsonResult);
		return result;
	}

	/**
	 * ������Ӧ��������а���StatusLine
	 * 
	 * @return StatusLine
	 */
	public String getStatusLineFromJsonResponse() {
		log.info(s_clog + " get StatusLine from response  ");
		this.resultList.clear();
		String result = null;

		if (this.methodFlag == 0) {
			this.hf.getResponseForPostRequest(this.headMap, this.resultList);
		} else if (this.methodFlag == 1) {
			this.hf.getResponseForGetRequest(this.headMap, this.resultList);
		} else if (this.methodFlag == 2) {
			this.hf.getResponseForPostRequest(this.headMap, this.resultList);
		}
		if (this.resultList.size() > 0) {
			result = this.resultList.get(0);
		} else {
			log.debug("Can not get response!");
		}
		this.s_StatusLine = result;
		log.debug(s_clog + " StatusLine:" + this.s_StatusLine);
		return result;
	}

	/**
	 * ������Ӧ��������а���responseContent
	 * 
	 * @return responseContent
	 */
	public String getResponseContentFromJsonResponse() {
		log.info(s_clog + " get content from response  ");
		this.resultList.clear();
		String result = null;

		if (this.methodFlag == 0) {
			this.hf.getResponseForPostRequest(this.headMap, this.resultList);
		} else if (this.methodFlag == 1) {
			this.hf.getResponseForGetRequest(this.headMap, this.resultList);
		} else if (this.methodFlag == 2) {
			this.hf.getResponseForPostRequest(this.headMap, this.resultList);
		}
		if (this.resultList.size() > 0) {
			if (this.resultList.size() < 2) {
				result = "null";
				log.debug("The Response Content is null!");
			} else {
				result = this.resultList.get(1);
			}
		} else {
			log.debug("Can not get response!");
		}
		this.s_ResponseContent = result;
		log.debug(s_clog + " content: " + this.s_ResponseContent);
		return result;
	}

	/**
	 * @param key
	 *            ͷ���ֶ�����
	 * @return ͷ���ֶ�ֵ
	 */
	public String getHeaderFromResponse(String key) {
		log.info(s_clog + " Get value of "+key+" in header" );
		this.headMap.clear();
		String result = null;

		if (this.methodFlag == 0) {
			this.hf.getResponseForPostRequest(this.headMap, this.resultList);
		} else if (this.methodFlag == 1) {
			this.hf.getResponseForGetRequest(this.headMap, this.resultList);
		} else if (this.methodFlag == 2) {
			this.hf.getResponseForPostRequest(this.headMap, this.resultList);
		}
		if (this.headMap.size() > 0) {
			result = this.headMap.get(key);
		} else {
			log.debug("Can not get response!");
		}
		log.debug(s_clog+" the value of "+key+" is: "+ result);
		return result;
	}

	/**
	 * @return headerMap
	 */
	public Map<String, String> getHeaderFromResponse() {
		log.info(s_clog + " get all key-value of header" );
		this.headMap.clear();
		String result = null;

		if (this.methodFlag == 0) {
			this.hf.getResponseForPostRequest(this.headMap, this.resultList);
		} else if (this.methodFlag == 1) {
			this.hf.getResponseForGetRequest(this.headMap, this.resultList);
		} else if (this.methodFlag == 2) {
			this.hf.getResponseForPostRequest(this.headMap, this.resultList);
		}
		log.debug(s_clog + " header:" +this.headMap.toString());
		return this.headMap;
	}

	/**
	 * ����Ӧ���ݵ���string���������л�ȡ����Ӧkey��ֵ
	 * 
	 * @param key
	 *            �ؼ���
	 * @return ֵ
	 */
	public String getValueFromStringResult(String key) {
		String value = null;
		String targetString = null;
		String[] allKeyAndValue = this.jsonResult.split(",");
		for (String string : allKeyAndValue) {
			if (string.contains(key)) {
				targetString = string;
				break;
			}
		}

		if (!targetString.isEmpty()) {
			String[] keyAndValue = targetString.split(":");
			value = keyAndValue[1].replace("\"", " ").replace("}", " ").trim();
			log.debug("key : " + key + " Value : " + value);
		} else {
			log.debug("the targetString is empty");
		}
		log.debug(s_clog+"getValueFromStringResult, the key is " + key+" the value is "+value);
		return value;
	}

	/**
	 * ����Ӧ��json��Ϣ�л�ȡ�ؼ��ֵ�ֵ
	 * 
	 * @param key
	 *            �ؼ���
	 * @return ֵ
	 */
	public String getValueFromJsonResult(String key) {
		
		String value = null;
		if (this.resultList.size() > 1) {
			JSONObject json = new JSONObject(this.resultList.get(1));
			value = getValueFromJsonResult(json, key);
		} else {
			log.debug("the json content is null!");
		}
		log.debug(s_clog+"getValueFromJsonResult, the key is " + key+" the value is "+value);
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
		boolean result = false;
		if (!content.isEmpty()) {
			if (content.contains(key)) {
				result = true;
			} else {
				result = false;
			}
		} else {
			log.debug("the content is empty");
		}
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
		boolean result = false;
		Pattern p1 = Pattern.compile(regular);
		Matcher m = p1.matcher(content);
		result = m.find();
		return result;
	}

	/**
	 * ���ݸ�ʽ����ʱ��������Ϣ
	 * 
	 * @param Format
	 *            ��ʽ��yyyy-MM-dd-HH-mm-ss-SSS
	 * @return ʱ�������
	 */
	public String getCurrentTimeWithFormat(String Format) {
		// "yyyy-MM-dd-HH-mm-ss-SSS"
		SimpleDateFormat format = new SimpleDateFormat(Format);
		String time = format.format(Calendar.getInstance().getTime());
		log.debug(time);
		return time;
	}

	/*
	 * private void getBillListFromDatabase(String sql) { Connection con = null;
	 * PreparedStatement pre = null; try {
	 * Class.forName("oracle.jdbc.driver.OracleDriver");
	 * System.out.println("connect oracle successfully!"); String url =
	 * GlobalInfo.getOracleUrl("oracleUrl"); String user =
	 * GlobalInfo.getOracleUrl("oracleUser"); String password =
	 * GlobalInfo.getOracleUrl("oraclePassword"); con =
	 * DriverManager.getConnection(url, user, password);
	 * System.out.println("successfully!"); ResultSet rs = null; String result =
	 * null; pre = (PreparedStatement) con.prepareStatement(sql); rs =
	 * pre.executeQuery(sql); int count = 1; while (rs.next()) {
	 * this.billList.add(rs.getString(count)); count++; }
	 * 
	 * } catch (ClassNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (SQLException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }finally { try { pre.close();
	 * con.close(); } catch (SQLException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } }
	 * 
	 * }
	 * 
	 * 
	 * public int getNumberFromBillList(String sql){
	 * getBillListFromDatabase(sql); if(this.billList!=null){
	 * this.billListNum=this.billList.size(); }else{
	 * System.out.println("Fail to query BillList from DataBase!"); } return
	 * this.billListNum; }
	 * 
	 * 
	 * public boolean addHttpFixtureToList(int clearFlag){ boolean result=false;
	 * if(!hfl.isEmpty() && clearFlag==1){//0:not clear list hfl.clear(); }
	 * result=hfl.add(getCurrentHttpFixture()); return result; }
	 * 
	 * 
	 * public boolean execForLoopWithHttpFixtureList(Map<String,String>
	 * interfaceAndJsonEntity){ boolean result=false; int size =
	 * interfaceAndJsonEntity.size(); for(int i=0;i<size;i++){
	 * hfl.get(i).setJsonEntityForPostRequest(interfaceAndJsonEntity.get(Integer
	 * .toString(i+1))); }
	 * 
	 * return result; }
	 */

	// LoopLogic(begin)-------------------------------------------------------
	/**
	 * LoopLogic������һ������ѭ������
	 * 
	 * @return �������
	 */
	public boolean createLoopLogic() {
		log.debug("createLoopLogic");
		boolean result = false;
		this.llc = new LoopLogicContainer();
		result = (!(llc == null)) ? true : false;
		if (result) {
			log.debug("Create LoopLogic successfully");
		} else {
			log.debug("Fail to create LoopLogic");
		}
		return result;
	}

	/**
	 * LoopLogic������postg������
	 * 
	 * @param host
	 *            Example��192.168.0.65:8081
	 * @param path
	 *            Example��/myLoan-mobile/action/api/AdBanner
	 * @param headerInfo
	 *            a map about headerinfo.
	 *            Example��!{version:2.1.0,build:6,deviceNo:863837023689236,
	 *            client:1,channel:!-AppStore-!}
	 * @param paramsMap
	 *            a map about params.
	 *            Example��!{version:2.1.0,build:6,deviceNo:863837023689236,
	 *            client:1,channel:!-AppStore-!}
	 * @return �������
	 */
	public boolean creatHttpPostgStep(String host, String path, Map<String, String> headerInfo,
			Map<String, String> paramsMap) {
		log.debug("begin to create HttpPostgStep");
		boolean result = false;
		this.stepType = 0;
		this.hpgs = llc.creatHttpPostgStep(host, path, headerInfo, paramsMap);
		result = (!(this.hpgs == null)) ? true : false;
		if (result) {
			log.debug("create HttpPostgStep succefully");
		} else {
			log.debug("Fail to create HttpPostgStep");
		}
		return result;
	}

	/**
	 * LoopLogic��������ѯoracle��������
	 * 
	 * @param sql
	 *            select���
	 * @return �������
	 */
	public boolean creatOracleQueryStep(String sql) {
		log.debug("begin to create OracleQueryStep");
		boolean result = false;
		this.stepType = 1;
		this.oqs = llc.creatOracleQueryStep(sql);
		result = (!(this.oqs == null)) ? true : false;
		if (result) {
			log.debug("create OracleQueryStep succefully");
		} else {
			log.debug("Fail to create OracleQueryStep");
		}
		return result;
	}

	/**
	 * LoopLogic����Բ��裬������Ҫ�޸ĵ�list
	 * 
	 * @param fieldList
	 *            list
	 * @return ���ý��
	 */
	public boolean setNeedModifyField(List<String> fieldList) {
		boolean result = false;
		switch (this.stepType) {
		case 0:
			this.hpgs = (HttpPostgStep) llc.setNeedModifyField(this.hpgs, fieldList);
			log.debug("setNeedModifyField for HttpPostgStep");
			result = true;
			break;
		case 1:
			this.oqs = (OracleQueryStep) llc.setNeedModifyField(this.oqs, fieldList);
			log.debug("setNeedModifyField for OracleQueryStep");
			result = true;
			break;
		default:
			log.debug("Failed to setNeedModifyField");
		}
		return result;
	}

	/**
	 * LoopLogic����Բ��裬������Ҫ�����list
	 * 
	 * @param fieldList
	 *            list
	 * @return ���ý��
	 */
	public boolean setNeedSaveField(List<String> fieldList) {
		boolean result = false;
		switch (this.stepType) {
		case 0:
			this.hpgs = (HttpPostgStep) llc.setNeedSaveField(this.hpgs, fieldList);
			log.debug("setNeedSaveField for HttpPostgStep");
			result = true;
			break;
		case 1:
			this.oqs = (OracleQueryStep) llc.setNeedSaveField(this.oqs, fieldList);
			log.debug("setNeedSaveField for OracleQueryStep");
			result = true;
			break;
		default:
			log.debug("Failed to setNeedSaveField");
		}
		return result;
	}

	/**
	 * LoopLogic����������뵽ѭ��������
	 * 
	 * @return ��ӽ��
	 */
	public boolean addStepToLoopContainer() {
		boolean result = false;
		switch (this.stepType) {
		case 0:
			llc.addStepToContainer(this.hpgs);
			log.debug("put HttpPostgStep to loop Container");
			result = true;
			break;
		case 1:
			llc.addStepToContainer(this.oqs);
			log.debug("put OracleQueryStep to loop Containerp");
			result = true;
			break;
		default:
			log.debug("Failed to addStepToContainer");
		}
		return result;
	}
	

	/**
	 * LoopLogic��ִ��ѭ������
	 * 
	 * @return ִ�н��
	 */
	public boolean execLoop() {
		log.debug("begin to execLoop");
		boolean result = llc.execSteps();
		log.debug("end to execLoop");
		return result;
	}
	
	
	public boolean healthCheck(String host, String path, String saccount) {
		log.debug("begin to healthCheck");
		boolean result = true;
		int iaccount = Integer.parseInt(saccount);
		while (true) {
			String status = healthCheck(host, path);
			log.debug("status is " + status);
			if (null == status) {			
					iaccount -= 1;
					this.waitTime(10000);			
			} else if (iaccount == 0) {
				result = false;
				break;
			} else if (status.contains("200")) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	private String healthCheck(String host,String path){
		Map<String,String> paramsMap = new HashMap<String,String>();
		this.setHttpMethod("get");
		this.setUriForGetRequest(host, path, paramsMap);
		String result=this.getStatusLineFromJsonResponse();
		return result;		
	}
	
	
	public boolean plog(String s) {
		log.info(s);
		return true;
	}
	
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

	// LoopLogic(end)-------------------------------------------------------

}
