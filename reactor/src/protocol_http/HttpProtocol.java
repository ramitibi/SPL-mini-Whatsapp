package protocol_http;

import protocol.AsyncServerProtocol;
import protocol_whatsapp.WhatsappProtocol;
import struct.User;
import tokenizer.http.HttpMessage;

public class HttpProtocol implements AsyncServerProtocol<HttpMessage> {

	private WhatsappProtocol manager = new WhatsappProtocol();
	
	
	
	public HttpProtocol(WhatsappProtocol manager) {
		
		this.manager=manager;
	}
	

	@Override
	public HttpMessage processMessage(HttpMessage msg) {
		//System.out.println("prossecing");
		msg.sperate();
		
		if(msg.getType().compareTo("POST")==0){
			if(checkPost(msg))
				return this.manager.processMessage(msg);
		}
		if(msg.getType().compareTo("GET")==0){
			if(checkGet(msg))
				return this.manager.processMessage(msg); 
		}
		
		return new HttpMessage("ERROR 404 , Type not find" + "\n");
	}
	
	
	
	public boolean checkGet(HttpMessage msg){
		
		if(msg.getUri().compareTo("/logout.jsp") == 0)
			return true;
		if(msg.getUri().compareTo("/queue.jsp") == 0)
			return true;
		if(msg.getUri().compareTo("/close.jsp") == 0)
			return true;
		return false;
	}
	
	
	public boolean checkPost(HttpMessage msg){
		if(msg.getUri().compareTo("/list.jsp") == 0)
			return true;
		if(msg.getUri().compareTo("/login.jsp") == 0)
			return true;
		if(msg.getUri().compareTo("/create_group.jsp") == 0)
			return true;
		if(msg.getUri().compareTo("/send.jsp") == 0)
			return true;
		if(msg.getUri().compareTo("/add_user.jsp") == 0)
			return true;
		if(msg.getUri().compareTo("/remove_user.jsp") == 0)
			return true;
		return false;
	}
	
	

	@Override
	public boolean isEnd(HttpMessage msg) {
		if(msg.getMessage().compareTo("bye" + "\n")==0)
			return true;
		return false;
	}
	
	public static HttpProtocol Create(WhatsappProtocol manager)
	{
		try 
		{
			return new HttpProtocol(manager);
		}
		
		catch (Exception e) 
		{
			System.out.println("Error occured while creating instance of ProtocolHttp");
			return null;
		}
	}


	@Override
	public boolean shouldClose() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void connectionTerminated() {
		// TODO Auto-generated method stub
		
	}
}


