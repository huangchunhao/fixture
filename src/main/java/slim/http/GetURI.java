package slim.http;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class GetURI implements IURI{

	@Override
	public URI createURI(String host,String path,Map<String,String> paramsMap) {
		
		URIBuilder uriB = null;
		URI uri=null;
		try {
			uriB = new URIBuilder()
				        .setScheme("http")
				        .setHost(host)//"192.168.0.65:8082"
				        .setPath(path);
			if(paramsMap.isEmpty()){
				
			}else{
				Iterator iterator = paramsMap.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String, String> entry = (Entry<String, String>) iterator.next();
					String paramKey = entry.getKey();
					String paramValue = entry.getValue();
					uriB.setParameter(paramKey, paramValue);			
				}				
			}
			uri=uriB.build();
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
