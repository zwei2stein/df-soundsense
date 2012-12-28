package cz.zweistein.df.soundsense.input.network;

import java.io.IOException;
import java.net.Socket;

public class ConnectionThread implements Runnable {
	
	private PacketListener packetListener;

	public ConnectionThread(Socket clientSocket) throws IOException {
		this.packetListener = new PacketListener(clientSocket);
	}

	@Override
	public void run() {
		
		while (true) {
			try {
				System.out.print(this.packetListener.getNextLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
