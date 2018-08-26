package slim.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.maiya.util.LogPrint;

public class HttpFixture {
	static Logger log = LogManager.getLogger("com.maiya");
	private CloseableHttpClient httpclient;
	private RequestConfig requestConfig;
	private String method;
	private IURI uri = null;
	private HttpPost hp = null;
	private List<NameValuePair> formparams;
	private StringBuffer stringBuffer;
	private UrlEncodedFormEntity uefEntity;
	private HttpEntity hpEntity;
	private StringEntity stringEntity;
	private FileEntity fileEntity;
	private HttpEntity he;
	private MultipartEntityBuilder meb;
	private String multipartBoundary = "----=_Part_0_";
	private HttpGet hg = null;

	private CloseableHttpResponse response;

	private String host;
	private String path;
	private Map<String, String> paramsMap;

	public HttpFixture() {

	}

	public HttpFixture(String method) {
		this.method = method;
		//httpclient = HttpClients.createDefault();
		httpclient = HttpClients.createDefault();
		requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();	
		switch (this.method.toLowerCase()) {
		case "post":
			IURI postUri = new PostURI();
			this.setURIType(postUri);
			break;
		case "get":
			IURI getUri = new GetURI();
			this.setURIType(getUri);
			break;
		case "postg":
			IURI postgUri = new PostgURI();
			this.setURIType(postgUri);
			break;
		default:
			try {
				throw new Exception("Please input \"post\" or \"get\" or \"postg\"");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setURIType(IURI uri) {
		this.uri = uri;
	}

	private void judgeURITypeAndSetRequest() {
		if (uri != null) {
			if (this.uri instanceof PostURI) {
				hp = new HttpPost(uri.createURI(host, path));
				hp.setConfig(requestConfig);
			}

			if (this.uri instanceof GetURI) {
				hg = new HttpGet(uri.createURI(host, path, paramsMap));
				hg.setConfig(requestConfig);
			}

			if (this.uri instanceof PostgURI) {
				hp = new HttpPost(uri.createURI(host, path, paramsMap));
				hp.setConfig(requestConfig);
			}
		} else {
			try {
				throw new Exception("Please set URI first");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public boolean setUriForPostRequest(String host, String path) {
		this.host = host;
		this.path = path;

		boolean result = false;

		this.judgeURITypeAndSetRequest();
		if (this.hp != null) {
			result = true;
		} else {
			try {
				throw new Exception("Failed to set URI for post request");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;

	}

	public boolean setUriForGetRequest(String host, String path, Map<String, String> paramsMap) {
		this.host = host;
		this.path = path;
		this.paramsMap = paramsMap;

		boolean result = false;

		this.judgeURITypeAndSetRequest();
		if (this.hg != null) {
			result = true;
		} else {
			try {
				throw new Exception("Failed to set URI for post request");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;

	}

	public boolean setUriForPostgRequest(String host, String path, Map<String, String> paramsMap) {
		this.host = host;
		this.path = path;
		this.paramsMap = paramsMap;

		boolean result = false;

		this.judgeURITypeAndSetRequest();
		if (this.hp != null) {
			result = true;
		} else {
			try {
				throw new Exception("Failed to set URI for post request");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;

	}

	public boolean setHeaderForPostRequest(Map<String, String> headerInfo) {
		boolean result = false;
		hp.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		Iterator iterator = headerInfo.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator.next();
			String headerInfoKey = entry.getKey();
			String headerInfoValue = entry.getValue();
			hp.addHeader(new BasicHeader(headerInfoKey, headerInfoValue));
			//log.debug(headerInfoKey + ":" + headerInfoValue);
			result = true;
		}
		return result;
	}

	public boolean setHeaderForPostJsonRequest(Map<String, String> headerInfo) {
		boolean result = false;
		hp.setHeader("Content-Type", "application/json;charset=UTF-8");
		Iterator iterator = headerInfo.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator.next();
			String headerInfoKey = entry.getKey();
			String headerInfoValue = entry.getValue();
			hp.addHeader(new BasicHeader(headerInfoKey, headerInfoValue));
			//LogPrint.print(headerInfoKey + ":" + headerInfoValue);
			result = true;
		}
		return result;
	}

	public boolean setMultipartHeaderForPostRequest(Map<String, String> headerInfo) {
		boolean result = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = format.format(Calendar.getInstance().getTime());
		this.multipartBoundary += time;
		hp.setHeader("Content-Type", "multipart/form-data" + "; boundary=" + "\"" + multipartBoundary + "\"");
		Iterator iterator = headerInfo.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator.next();
			String headerInfoKey = entry.getKey();
			String headerInfoValue = entry.getValue();
			hp.addHeader(new BasicHeader(headerInfoKey, headerInfoValue));
			//LogPrint.print(headerInfoKey + ":" + headerInfoValue);
			result = true;
		}
		return result;
	}

	public boolean setHeaderForGetRequest(Map<String, String> headerInfo) {
		boolean result = false;
		hg.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		Iterator iterator = headerInfo.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator.next();
			String headerInfoKey = entry.getKey();
			String headerInfoValue = entry.getValue();
			hg.addHeader(new BasicHeader(headerInfoKey, headerInfoValue));
			//LogPrint.print(headerInfoKey + ":" + headerInfoValue);
			result = true;
		}
		return result;
	}

	public boolean setHeaderForGetJsonRequest(Map<String, String> headerInfo) {
		boolean result = false;
		hg.setHeader("Content-Type", "application/json;charset=UTF-8");
		Iterator iterator = headerInfo.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator.next();
			String headerInfoKey = entry.getKey();
			String headerInfoValue = entry.getValue();
			hg.addHeader(new BasicHeader(headerInfoKey, headerInfoValue));
			//LogPrint.print(headerInfoKey + ":" + headerInfoValue);
			result = true;
		}
		return result;
	}

	public boolean setMultipartHeaderForGetRequest(Map<String, String> headerInfo) {
		boolean result = false;
		hg.setHeader("Content-Type", "multipart/form-data");
		Iterator iterator = headerInfo.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator.next();
			String headerInfoKey = entry.getKey();
			String headerInfoValue = entry.getValue();
			hg.addHeader(new BasicHeader(headerInfoKey, headerInfoValue));
			//LogPrint.print(headerInfoKey + ":" + headerInfoValue);
			result = true;
		}
		return result;
	}

	public boolean setParamsForPostRequest(Map<String, String> ParamsMap) {
		boolean result = false;
		formparams = new ArrayList<NameValuePair>();
		Iterator iterator = ParamsMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator.next();
			String ParamKey = entry.getKey();
			String ParamValue = entry.getValue();
			/*
			 * String ParamValue = null; ParamValue = new
			 * String(entry.getValue().getBytes(),Charset.forName("UTF-8"));
			 */
			//LogPrint.print(ParamKey + ":" + ParamValue);
			result = formparams.add(new BasicNameValuePair(ParamKey, ParamValue));
		}
		return result;
	}

	public boolean setEntityForPostRequest() {
		boolean result = false;
		// uefEntity = new UrlEncodedFormEntity(formparams,
		// HTTP.DEF_CONTENT_CHARSET);
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hp.setEntity(uefEntity);
		result = true;
		return result;
	}

	public boolean setParamsForPostRequestWithNoTrans(Map<String, String> ParamsMap) {
		boolean result = false;
		stringBuffer = new StringBuffer();
		Iterator iterator = ParamsMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator.next();
			String ParamKey = entry.getKey();
			String ParamValue = entry.getValue();
			if (this.stringBuffer.length() == 0) {
				this.stringBuffer.append(ParamKey + "=" + ParamValue);
			} else {
				this.stringBuffer.append("&" + ParamKey + "=" + ParamValue);
			}

			if (this.stringBuffer.length() > 0) {
				result = true;
			}
		}
		return result;
	}

	public boolean setEntityForPostRequestWithNoTrans() {
		boolean result = false;
		hp.setEntity(new StringEntity(this.stringBuffer.toString(), HTTP.DEF_CONTENT_CHARSET));
		result = true;
		return result;
	}

	public boolean setJsonEntityForPostRequest(String json) {
		boolean result = false;
		// uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		if (!json.isEmpty()) {
			stringEntity = new StringEntity(json, ContentType.create("application/json", "UTF-8"));
			hp.setEntity(stringEntity);
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/*
	 * public boolean setFileEntityForPostRequest(File file,String contentType){
	 * boolean result=false; if(!(null==file)){ FileEntity fileEntity = new
	 * FileEntity(file, ContentType.create(contentType, "UTF-8"));
	 * hp.setEntity(fileEntity); result=true; }else{ result=false; } return
	 * result; }
	 */

	public boolean createMultipartEntityBuilder() {
		boolean result = true;
		meb = MultipartEntityBuilder.create();
		return result;
	}

	public boolean createFilebodyAndAddToMultipart(String fieldname, File file, String contentType) {
		boolean result = false;
		if (!(null == file)) {
			FileBody fb = new FileBody(file);
			//LogPrint.print("Create Filebody-- Filename:" + file.getName() + ", and add it to Multipart");
			meb.addBinaryBody(fieldname, file, ContentType.create(contentType, Charset.forName("UTF-8")),
					file.getName());
			meb.setBoundary(multipartBoundary);
			// meb.setContentType(ContentType.create(contentType));
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public boolean createStringbodyAndAddToMultipart(String fieldname, String string, String contentType) {
		boolean result = false;
		if (!(null == string)) {
			try {
				ContentType ct = ContentType.create(contentType, "UTF-8");
				StringBody sb = new StringBody(string, ct);
				//LogPrint.print("Create Stringbody-- string:" + string + ", and add it to Multipart");
				// meb.addPart("StringBody",sb);
				meb.addTextBody(fieldname, string, ContentType.create(contentType, Charset.forName("UTF-8")));
				meb.setBoundary(multipartBoundary);
				result = true;
			} catch (UnsupportedCharsetException e) {
				e.printStackTrace();
			}

		} else {
			result = false;
		}
		return result;
	}

	public boolean setHttpEntityForPostRequest() {
		boolean result = false;
		this.he = meb.build();
		if (!(null == he)) {
			hp.setEntity(he);
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public void getResponseForPostRequest(Map<String, String> headMap, List<String> resultList) {
		try {			
			response = httpclient.execute(hp);
			try {

				Header[] allHeaders = response.getAllHeaders();
				int length = allHeaders.length;
				for (int i=0;i<length;i++){
					if(headMap.containsKey(allHeaders[i].getName())){
						headMap.put(allHeaders[i].getName()+i, allHeaders[i].getValue());
					}else{
						headMap.put(allHeaders[i].getName(), allHeaders[i].getValue());
					}
				}
				/*for (Header h : allHeaders) {
					headMap.put(h.getName(), h.getValue());	
				}*/
				HttpEntity entity = response.getEntity();
				String statusLine = response.getStatusLine().toString();
				resultList.add(statusLine);
				String ResponseContent = EntityUtils.toString(entity, "UTF-8");
				if (entity != null) {
					resultList.add(ResponseContent);				
				}
				log.debug("Response list content: " + resultList);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ��ʱû��ʹ�õ�
	/*
	 * public boolean getResponseForPostRequestWithMatch(List<String>
	 * resultList,String RegexString) { boolean result = false; Matcher m;
	 * this.getResponseForPostRequest(resultList); if(resultList.size()<2){
	 * String statusLine = resultList.get(0); Pattern p1 =
	 * Pattern.compile(RegexString); m = p1.matcher(statusLine); }else{ String
	 * statusLine = resultList.get(0); String ResponseContent =
	 * resultList.get(1); Pattern p1 = Pattern.compile(RegexString); m =
	 * p1.matcher(statusLine+" "+ResponseContent); } if (m.find()) { result =
	 * true; } return result; }
	 */

	public void getResponseForGetRequest(Map<String, String> headMap, List<String> resultList) {
		try {
			response = httpclient.execute(hg);
			try {
				Header[] allHeaders = response.getAllHeaders();
				for (Header h : allHeaders) {
					headMap.put(h.getName(), h.getValue());
				}
				HttpEntity entity = response.getEntity();
				String StatusLine = response.getStatusLine().toString();
				resultList.add(StatusLine);
				String ResponseContent = EntityUtils.toString(entity, "UTF-8");
				if (entity != null) {
					resultList.add(ResponseContent);
					//log.debug("Response content: " + ResponseContent);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// ��ʱû��ʹ�õ�
	/*
	 * public boolean getResponseForGetRequestWithMatch(List<String>
	 * resultList,String RegexString) { boolean result = false; Matcher m;
	 * this.getResponseForGetRequest(resultList); if(resultList.size()<2){
	 * String statusLine = resultList.get(0); Pattern p1 =
	 * Pattern.compile(RegexString); m = p1.matcher(statusLine); }else{ String
	 * statusLine = resultList.get(0); String ResponseContent =
	 * resultList.get(1); Pattern p1 = Pattern.compile(RegexString); m =
	 * p1.matcher(statusLine+" "+ResponseContent); } if (m.find()) { result =
	 * true; } return result; }
	 */

	public String getCookieOnResponseByPostRequest() {
		String token = null;

		try {
			response = httpclient.execute(hp);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Set-Cookie"));
					while (it.hasNext()) {
						HeaderElement elem = it.nextElement();
						// System.out.println(elem.getName() + "=" +
						// elem.getValue());
						if (elem.getName().equals("token")) {
							// sid = elem.getName() + "=" + elem.getValue();
							token = elem.getValue();
							// System.out.println(sid + "--" + sid.length());
						}
						NameValuePair[] params = elem.getParameters();
						for (int i = 0; i < params.length; i++) {
							//log.debug(" " + params[i]);
						}
					}
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// �ر�����,�ͷ���Դ
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return token;
	}

}
