package pl.jpranica.multipainterfx;

import javax.sql.rowset.CachedRowSet;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnection {
	private Socket socket = null;
	private String address = null;
	private int port;
	private boolean newRequest = false;

	public ServerConnection(String address, int port) throws UnknownHostException, IOException {
		this.address=address;
		this.port=port;
		//this.socket=new Socket(address,port);
	}

	public void sendBrushstroke(Brushstroke bs) throws IOException{
		OutputStream os = socket.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(bs);
		oos.close();
		os.close();
	}

	public void sendServerRequest(String request) throws IOException {
		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
		printWriter.println(request);
	}

	public boolean getNewRequest() {
		return newRequest;
	}

	public CachedRowSet getTable(String tableName) throws IOException, ClassNotFoundException {
		socketOpen();
		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
		String request = "get-table;"+tableName;
		printWriter.println(request);
		
		InputStream is = socket.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		CachedRowSet crs = (CachedRowSet)ois.readObject();
		if(crs!=null)System.out.println("Successfully downloaded table "+tableName);
		
		socket.close();
		return crs;
	}

	public void socketClose() {
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
	}
}
