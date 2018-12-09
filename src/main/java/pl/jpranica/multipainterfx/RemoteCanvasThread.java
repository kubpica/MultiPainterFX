package pl.jpranica.multipainterfx;

import javax.sql.rowset.CachedRowSet;
import java.io.*;
import java.net.Socket;

//
public class RemoteCanvasThread extends Thread{
	private Socket socket;
	private PaintController pc;

	public RemoteCanvasThread(Socket socket, PaintController pc) {
		this.socket = socket;
		this.pc = pc;
	}

	public void run() {
		try {
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            while (ois.available()>0){
                try {
                    Brushstroke bs = (Brushstroke)ois.readObject();
                    bs.recreate(pc.getGraphicsContext());
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found.");
                }
            }

            /*if(crs!=null)System.out.println("Successfully downloaded table " + tableName);

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
			}*/
		} catch (IOException e) {
			System.out.println("Connection lost.");
		}
	}
}
