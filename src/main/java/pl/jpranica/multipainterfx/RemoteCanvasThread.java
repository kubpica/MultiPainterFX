package pl.jpranica.multipainterfx;

import java.io.*;
import java.net.Socket;

//
public class RemoteCanvasThread extends Thread{
	private ServerConnection connection;
	private PaintController pc;

	public RemoteCanvasThread(ServerConnection connection, PaintController pc) {
		this.connection = connection;
		this.pc = pc;
	}

	public void run() {
		try {
			Socket socket = connection.getSocket();
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            while (true){
                try {
                    Brushstroke bs = (Brushstroke)ois.readObject();
                    bs.recreate(pc.getGraphicsContext());
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found.");
                }
            }
		} catch (IOException e) {
			System.out.println("Connection lost.");
		}
	}
}
