package tokenizer_http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import tokenizer.Tokenizer;

public class HttpTokenizer implements Tokenizer<String> {
	
	private InputStreamReader _isr;
	@SuppressWarnings("unused")
	private final char _delimiter = '$';
	private boolean _isClosed;
	private BufferedReader br;
	
	
	public HttpTokenizer() {
		// TODO Auto-generated constructor stub
		_isClosed = false;
	}
	
	public HttpTokenizer(InputStreamReader inputS){
		addInputStream(inputS);
		_isClosed = false;
	}
	

	@Override
	public HttpMessage nextMessage() throws IOException {
		
		if (!isAlive())
			throw new IOException("tokenizer is closed");
		String ans = "";
		try {
			int c;
			StringBuilder sb = new StringBuilder();
			//			// read char by char, until encountering the framing character, or
			//			// the connection is closed.
			while ((c = br.read())!= 36) {
//				if (((char)c) == char$){

//					break;
//					}
//				else
					sb.append((char) c); //shrshor char
			}
			br.read();
			ans = sb.toString();

		} catch (IOException e) {
			this._isClosed = true;
			throw new IOException("Connection is dead");
		}
		return new HttpMessage(ans);
		
		//return null;
	}
	
	
	
	public static HttpTokenizer create(){
		
		HttpTokenizer returnedHttpTokenizer = new HttpTokenizer();
		
		try
		{
			returnedHttpTokenizer = new HttpTokenizer();
			return returnedHttpTokenizer;
		}
		catch(Exception e)
		{
			System.out.println("Error occured while creating instance of HttpTokenizer");
			return returnedHttpTokenizer;
		}
		
	}
	
	

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return !(_isClosed);
	}

	@Override
	public void addInputStream(InputStreamReader inputStreamReader) {
		// TODO Auto-generated method stub
		_isr=inputStreamReader;
		br = new BufferedReader(_isr);
	}

}
