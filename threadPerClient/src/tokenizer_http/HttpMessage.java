package tokenizer_http;

import struct.Group;
import struct.User;
import tokenizer.Message;

public class HttpMessage implements Message<String> {
	
	private String message;
	private User sender;
	private Group group; // think its not a nessary
	private String uri;
	private String vertion;
	private String type;
	
	
	public HttpMessage(){}
	
	public HttpMessage(String message){
		this.message=message;
		this.group=null;
		this.sender=null;
		this.uri=new String();
		this.vertion=new String();
		this.type=new String();
	}
	
	
	public void setSender(User user){
		this.sender=user;
	}
	
	public User getSender(){
		return this.sender;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public Group getGroup(){
		return this.group;
	}
	
	public void serGroup(Group group){
		this.group=group;
	}
	
	public String getVertion(){
		return this.vertion;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getUri(){
		return this.uri;
	}
	
	public String toString(){
		return this.message;
	}
	
	public void sperate(){
		
		String u="";
		String t="";
		String v="";
		
		int i=0;
		StringBuilder str=new StringBuilder();
		
		while(this.message.charAt(i) != ' '){
			str.append(this.message.charAt(i));
			i++;
		}
		t=str.toString();
		str.setLength(0);
		i++;
		
		while(this.message.charAt(i) != ' '){
			str.append(this.message.charAt(i));
			i++;
		}
		u=str.toString();
		str.setLength(0);
		i++;
		
		while(this.message.charAt(i) != '\n'){
			str.append(this.message.charAt(i));
			i++;
		}
		v=str.toString();
		str.setLength(0);
		this.message.substring(i++, this.message.length());
		
		
		this.type=t;
		this.uri=u;
		this.vertion=v;
		//System.out.println("sperated");
		//System.out.println("type: " + type);
		//System.out.println("U: " + uri);
		//System.out.println("V :" + vertion);
	}

}
