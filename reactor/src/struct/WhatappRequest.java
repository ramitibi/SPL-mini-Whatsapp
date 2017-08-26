package struct;

public class WhatappRequest {
	
	private String msg;
	private String uri;
	
	public WhatappRequest() {
		// TODO Auto-generated constructor stub
	}
	
	public WhatappRequest(String msg , String uri){
		this.msg=msg;
		this.uri=uri;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public String getUri(){
		return this.uri;
	}
	
	
	
}
