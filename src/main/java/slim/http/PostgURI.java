package slim.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.maiya.util.StringUtil;

public class PostgURI implements IURI{
	static Logger log = LogManager.getLogger("com.maiya");

	@Override
	public URI createURI(String host,String path,Map<String,String> paramsMap) {
		
		URIBuilder uriB = null;
		URI uri=null;
		try {
			uriB = new URIBuilder()
				        .setScheme("http")
				        .setHost(host)//"192.168.0.65:8082"
				        .setPath(path);
			Iterator iterator = paramsMap.entrySet().iterator();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			while(iterator.hasNext()){
				Entry<String, String> entry = (Entry<String, String>) iterator.next();
				String paramKey = entry.getKey();
				//String paramValue = URLEncoder.encode(entry.getValue(),"UTF-8");
				String paramValue = entry.getValue();
				  
				params.add(new BasicNameValuePair(paramKey, paramValue));  				
				//if(StringUtil.chinesseMap.containsKey(paramValue)){
					//paramValue=java.net.URLDecoder.decode(StringUtil.chinesseMap.get(paramValue),"utf-8");
					//paramValue=new String(StringUtil.chinesseMap.get(paramValue).getBytes("GB2312"), "GB2312");
					//paramValue=StringUtil.chinesseMap.get(paramValue);
				//}
				//uriB.setParameter(paramKey, paramValue);					
			}
			uri= new URI(uriB.toString()+"?"+URLEncodedUtils.format(params, Charset.forName("UTF-8")));
			//uriB.build();
			log.debug("the uri is " +uri.toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return uri;

	}

	@Override
	public URI createURI(String host, String path) {
		URI uri = null;
		try {
			uri = new URIBuilder()
				        .setScheme("http")
				        .setHost(host)//"192.168.0.65:8082"
				        .setPath(path)//"/manage-server/organization/delOrganization.htm"
				        .build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
	}


}
