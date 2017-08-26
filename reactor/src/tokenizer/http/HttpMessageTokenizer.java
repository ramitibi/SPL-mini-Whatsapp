package tokenizer.http;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Vector;

import tokenizer.MessageTokenizer;

public class HttpMessageTokenizer implements MessageTokenizer<HttpMessage> {

	private final String _messageSeparator;
	
	private final StringBuffer _stringBuf = new StringBuffer();
	
	private final Vector<ByteBuffer> _buffers = new Vector<ByteBuffer>();
	
	private final CharsetDecoder _decoder;
	private final CharsetEncoder _encoder;
		
	
	public HttpMessageTokenizer(String separator, Charset charset) {
	      this._messageSeparator = separator;
	 
	      this._decoder = charset.newDecoder();
	      this._encoder = charset.newEncoder();
	   }
	
	@Override
	public void addBytes(ByteBuffer bytes) {
		_buffers.add(bytes);
		
	}

	@Override
	public synchronized boolean hasMessage() {
		while(_buffers.size() > 0) {
	           ByteBuffer bytes = _buffers.remove(0);
	           CharBuffer chars = CharBuffer.allocate(bytes.remaining());
	 	      this._decoder.decode(bytes, chars, false); // false: more bytes may follow. Any unused bytes are kept in the decoder.
	 	      chars.flip();
	 	      this._stringBuf.append(chars);
		   }
		   return this._stringBuf.indexOf(this._messageSeparator) > -1;
	}

	@Override
	public synchronized HttpMessage nextMessage() {
		System.out.println("hasssssssss");
		String message = null;
	      int messageEnd = this._stringBuf.indexOf(this._messageSeparator);
	      if (messageEnd > -1) {
	         message = this._stringBuf.substring(0, messageEnd);
	         this._stringBuf.delete(0, messageEnd+this._messageSeparator.length());
	      }
	      return new HttpMessage(message);
	}

	@Override
	public ByteBuffer getBytesForMessage(HttpMessage msg) throws CharacterCodingException {
		StringBuilder sb = new StringBuilder(msg.getMessage());
      sb.append(this._messageSeparator);
      ByteBuffer bb = this._encoder.encode(CharBuffer.wrap(sb));
      return bb;
	}

}
