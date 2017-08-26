package threadPerClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import protocol_http.HttpProtocolFactory;
import protocol_whatsapp.WhatsappProtocol;
import tokenizer_http.HttpTokenizerFactory;

public class MultipleClientProtocolServer<T> implements Runnable {
	private ServerSocket serverSocket;
	private int listenPort;
	private HttpProtocolFactory _protocolFactory;
	private HttpTokenizerFactory _tokenizerFactory;
	private WhatsappProtocol manager = new WhatsappProtocol();


	public MultipleClientProtocolServer(int port, HttpProtocolFactory httpProtocolFactory, HttpTokenizerFactory tokenizerFactory)
	{
		serverSocket = null;
		listenPort = port;
		_protocolFactory = httpProtocolFactory;
		_tokenizerFactory =  tokenizerFactory;
	}

	public void run()
	{
		try {
			serverSocket = new ServerSocket(listenPort);
			System.out.println("Listening...");
		}
		catch (IOException e) {
			System.out.println("Cannot listen on port " + listenPort);
		}

		while (true)
		{
			try {
				ConnectionHandler newConnection = new ConnectionHandler(serverSocket.accept(), _protocolFactory.create(this.manager),_tokenizerFactory.create());
				//s = an.nextLine();
				new Thread(newConnection).start();
			}
			catch (IOException e)
			{
				System.out.println("Failed to accept on port " + listenPort);
			}
		}
	}


	// Closes the connection
	public void close() throws IOException
	{
		serverSocket.close();
	}

	public static void main(String[] args) throws IOException
	{
		// Get port
		//int port = Integer.decode(args[0]).intValue();
		int port = 6666;
		//MultipleClientProtocolServer server = new MultipleClientProtocolServer(port, new HttpProtocolFactory(), new HttpTokenizerFactory());
		MultipleClientProtocolServer server = new MultipleClientProtocolServer(port, new HttpProtocolFactory(), new HttpTokenizerFactory());
		Thread serverThread = new Thread(server);
		serverThread.start();
		try {
			serverThread.join();
		}
		catch (InterruptedException e)
		{
			System.out.println("Server stopped");
		}



	}
}

