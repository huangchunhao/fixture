package slim.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;

public class PostURI implements IURI{

	@Override
	public URI createURI(String host,String path) {
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

	@Override
	public URI createURI(String host, String path, Map<String, String> paramsMap) {
		// TODO Auto-generated method stub
		return null;
	}



}
