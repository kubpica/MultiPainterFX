package pl.jpranica.multipainterfx;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server extends Thread {
	private int serverPort;
	private ArrayList<ServerConnection> connections = new ArrayList<>();
	private LinkedList<Brushstroke> history = new LinkedList<>();

    public Server() {
        this(4545);
    }
	public Server(int port) {
		this.serverPort = port;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void sendBrushstroke(Brushstroke bs) throws IOException{
		for(ServerConnection sc : connections){
			sc.sendBrushstroke(bs);
		}
	}

	public void sendPoint(CanvasHistoricalPoint point) throws IOException{
        for(ServerConnection sc : connections){
            sc.sendPoint(point);
        }
    }

	public void addToHistory(Brushstroke point){
	    history.add(point);
    }

    public void resendHistory(ServerConnection connection) throws IOException{
        for(Brushstroke point : history){
            connection.sendBrushstroke(point);
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
			new Thread(new ServerThread(this, sc)).start();
			connections.add(sc);
		}
	}
}
