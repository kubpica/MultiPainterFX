package pl.jpranica.multipainterfx;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//
public class Server {
	private int serverPort;

	public Server(int port) {
		serverPort=port;
	}
	public Server() {
		serverPort= 4545;
	}

	public void runServer() throws IOException{
		ServerSocket serverSocket = new ServerSocket(serverPort);
		System.out.println("Server initialized on port "+serverPort+". Awaiting connections.");
		while(true) {
			Socket socket = serverSocket.accept();
			System.out.println("New connection");
			new ServerThread(socket).start();
		}
	}
}
