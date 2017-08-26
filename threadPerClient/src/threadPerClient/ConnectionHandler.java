package threadPerClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import protocol_http.HttpProtocol;
import tokenizer_http.HttpMessage;
import tokenizer_http.HttpTokenizer;

public class ConnectionHandler<T> implements Runnable {

	private BufferedReader in;
	private PrintWriter out;
	Socket clientSocket;
	HttpProtocol protocol;
	HttpTokenizer tokenizer;

	public ConnectionHandler(Socket acceptedSocket, HttpProtocol p, HttpTokenizer t)
	{
		in = null;
		out = null;
		clientSocket = acceptedSocket;
		protocol = p;
		tokenizer = t;
		System.out.println("Accepted connection from client!");
		System.out.println("The client is from: " + acceptedSocket.getInetAddress() + ":" + acceptedSocket.getPort());
	}

	public void run()
	{


		try {
			initialize();
		}
		catch (IOException e) {
			System.out.println("Error in initializing I/O");
		}

		try {
			process();
		} 
		catch (IOException e) {
			System.out.println("Error in I/O");
		} 

		System.out.println("Connection closed - bye bye...");
		close();

	}

	public void process() throws IOException
	{
		Object ob=new Object();
		StringBuilder sb = new StringBuilder();
		String s= new String();
		HttpMessage msg = new HttpMessage();
		//System.out.println("11111");
		while ((msg = tokenizer.nextMessage()) != null)
		{
			System.out.println("Received \"" + msg.getMessage() + "\" from client");
			HttpMessage response = protocol.processMessage(msg);
			System.out.println("*************************************************");
			sb.append(response.getMessage());
			sb.append("\n");
			sb.append("$");
			s=sb.toString();
			sb.setLength(0);
			System.out.println(s);
			
			if (response != null)
			{
				synchronized (ob) {
					out.println(s);
				}
				
			}

			if (protocol.isEnd(msg))
			{
				break;
			}

		}
	}

	// Starts listening
	public void initialize() throws IOException
	{
		// Initialize I/O
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
		out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),"UTF-8"), true);
		//Replaced
		//out = new PrintWriter(clientSocket.getOutputStream());
		tokenizer.addInputStream(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
		System.out.println("I/O initialized");
	}

	// Closes the connection
	public void close()
	{
		try {
			if (tokenizer.isAlive())//Handle this in tokenizer
			{
				in.close();
			}
			if (out != null)
			{
				out.close();
			}

			clientSocket.close();
		}
		catch (IOException e)
		{
			System.out.println("Exception in closing I/O");
		}
	}

}