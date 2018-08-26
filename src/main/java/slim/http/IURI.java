package slim.http;

import java.net.URI;
import java.util.Map;

public interface IURI {
	
	public URI createURI(String host, String path);
	public URI createURI(String host, String path, Map<String, String> paramsMap);

}
