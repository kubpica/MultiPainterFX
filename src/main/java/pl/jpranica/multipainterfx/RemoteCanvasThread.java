package pl.jpranica.multipainterfx;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

//
public class RemoteCanvasThread implements Runnable{
	private ServerConnection connection;
	private PaintController pc;
	private ChronologicalCanvas canvas;

	public RemoteCanvasThread(ServerConnection connection, PaintController pc) {
		this.connection = connection;
		this.pc = pc;
		this.canvas = new ChronologicalCanvas(pc.getCanvas());
	}

	@Override
	public void run() {
		try {
			Socket socket = connection.getSocket();
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            while (true){
                try {
                    Brushstroke bs = (Brushstroke)ois.readObject();
                    canvas.paint(bs);
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found.");
                }
            }
		} catch (IOException e) {
			System.out.println("Connection lost.");
		}
	}
}
