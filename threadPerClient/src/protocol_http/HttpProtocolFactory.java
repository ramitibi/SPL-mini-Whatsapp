package protocol_http;

import protocol.ServerProtocolFactory;
import protocol_whatsapp.WhatsappProtocol;
import tokenizer_http.HttpMessage;

public class HttpProtocolFactory implements ServerProtocolFactory<HttpMessage> {
	
	@Override
	public HttpProtocol create(WhatsappProtocol manager) {
		
		return HttpProtocol.Create(manager);
	}

}
