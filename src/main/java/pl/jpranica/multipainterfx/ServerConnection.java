package pl.jpranica.multipainterfx;

import javax.sql.rowset.CachedRowSet;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnection {
	private Socket socket = null;
	private OutputStream os;
	private ObjectOutputStream oos;

	public ServerConnection(Socket socket) throws IOException{
		this.socket = socket;
		this.os = socket.getOutputStream();
		this.oos = new ObjectOutputStream(os);
	}

	public void sendBrushstroke(Brushstroke bs) throws IOException{
		oos.writeObject(bs);
	}

	public void sendPoint(CanvasHistoricalPoint point) throws IOException{
		oos.writeObject(point);
	}

	public Socket getSocket() {
		return socket;
	}

	/*public void socketClose() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void socketOpen() {
		try {
			socket=new Socket(address,port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
