package pl.jpranica.multipainterfx;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
	private int serverPort;
	private ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();

	public Server(int port) {
		serverPort = port;
	}
	public Server() {
		serverPort = 4545;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void sendBrushstroke(Brushstroke bs) throws IOException{
	    System.out.println("server send bs");
		for(ServerConnection sc : connections){
			sc.sendBrushstroke(bs);
		}
	}

	public void run(){
		try {
			runServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void runServer() throws IOException{
		ServerSocket serverSocket = new ServerSocket(serverPort);
		System.out.println("Server initialized on port "+serverPort+". Awaiting connections.");
		while(true) {
			Socket socket = serverSocket.accept();
			System.out.println("New connection");
			ServerConnection sc = new ServerConnection(socket);
			new ServerThread(this, sc).start();
			connections.add(sc);
		}
	}
}
