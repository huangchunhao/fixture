package slim.http;

public enum ERequestMethod {
	POST("post"),GET("get"),POSTG("postg");
	
	private String name;
	
	private ERequestMethod(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
}
