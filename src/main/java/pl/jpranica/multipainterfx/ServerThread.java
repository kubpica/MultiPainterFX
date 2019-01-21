package pl.jpranica.multipainterfx;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.time.Instant;

public class ServerThread implements Runnable{
	private Server server;
	private ServerConnection connection;

	public ServerThread(Server server, ServerConnection connection) {
		this.server = server;
		this.connection = connection;
	}

	@Override
	public void run() {
		try {
			Socket socket = connection.getSocket();
			server.resendHistory(connection);

			InputStream is = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);

			while (true){
				try {
					Brushstroke bs = (Brushstroke)ois.readObject();
					System.out.println("Odebrano");
					bs.setDate(Instant.now());
                    server.addToHistory(bs);
					server.sendBrushstroke(bs);
				} catch (ClassNotFoundException e) {
					System.out.println("Class not found.");
				}
			}
		} catch (IOException e) {
			System.out.println("Connection lost.");
		}
	}
}
