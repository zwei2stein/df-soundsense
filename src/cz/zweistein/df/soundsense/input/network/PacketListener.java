package cz.zweistein.df.soundsense.input.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import cz.zweistein.df.soundsense.input.Listener;

public class PacketListener extends Listener {

	public PacketListener(Socket clientSocket) throws IOException {
		this.br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

}
