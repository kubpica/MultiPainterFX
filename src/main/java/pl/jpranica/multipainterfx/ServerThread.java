package pl.jpranica.multipainterfx;

import javax.sql.rowset.CachedRowSet;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

//
public class ServerThread extends Thread{
	private Socket socket;

	public ServerThread(Socket socket) {
		this.socket=socket;
	}

	public void run() {
		try {
			String message=null;
			BufferedReader bufferedReader = new BufferedReader ( new InputStreamReader(socket.getInputStream()));
			while((message=bufferedReader.readLine())!=null)
			{
				System.out.println("Acquired message: "+message);
				if(message.contains("get-table")){
					String[] request = message.split(";");
					String tableName = request[1];
					OutputStream os = socket.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
                    //CachedRowSet crs = dbH.getQueryBuilder().getTable(tableName);
                    //oos.writeObject(crs);
                    oos.close();
                    os.close();
					
				}
			}
		} catch (IOException e) {
			System.out.println("Connection lost.");
		}
	}
}
