package cz.zweistein.df.soundsense.input.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import cz.zweistein.df.soundsense.glue.Glue;
import cz.zweistein.df.soundsense.output.Procesor;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class NetworkManager {
	private static Logger logger = LoggerSource.logger;	
	
	private Procesor processor;
	
	public NetworkManager(Procesor processor) {
		this.processor = processor;
	}

	public void acceptIcommingConnections(int port) throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		logger.info("Listening for events at network port "+port+".");
		while (true) {
			Socket clientSocket = serverSocket.accept();
			logger.info("New client '"+clientSocket.getInetAddress()+"' connected.");
			Glue.glue(new PacketListener(clientSocket), this.processor);
		}
	}

}
