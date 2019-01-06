package pl.jpranica.multipainterfx;

import javafx.collections.transformation.SortedList;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Comparator;

//
public class RemoteCanvasThread extends Thread{
	private ServerConnection connection;
	private PaintController pc;
	private ChronologicalCanvas canvas;

	public RemoteCanvasThread(ServerConnection connection, PaintController pc) {
		this.connection = connection;
		this.pc = pc;
		this.canvas = new ChronologicalCanvas(pc.getCanvas());
	}

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
