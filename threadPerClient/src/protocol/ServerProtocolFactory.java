package protocol;

import protocol_whatsapp.WhatsappProtocol;


public interface ServerProtocolFactory<T> {
	ServerProtocol<T> create(WhatsappProtocol manager);
}
