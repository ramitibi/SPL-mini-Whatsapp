package protocol_http;

import protocol.AsyncServerProtocol;
import protocol.ServerProtocol;
import protocol.ServerProtocolFactory;
import protocol_whatsapp.WhatsappProtocol;
import tokenizer.http.HttpMessage;

public class HttpProtocolFactory implements ServerProtocolFactory {

	@Override
	public AsyncServerProtocol create(WhatsappProtocol manager) {
		
		return HttpProtocol.Create(manager);
		
	}

}
