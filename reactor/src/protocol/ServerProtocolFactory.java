package protocol;

import protocol_whatsapp.WhatsappProtocol;

public interface ServerProtocolFactory<T> {

AsyncServerProtocol<T> create(WhatsappProtocol manager);
}
