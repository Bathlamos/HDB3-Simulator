package me.legault.hdb3;

import me.legault.hdb3.endpoint.KeyboardEncoder;
import me.legault.hdb3.endpoint.ServerDecoder;

public class EntryPoint {

	static final String HOSTNAME = "localhost";

	static final int PORT_NUMBER = 65234;

	public static void main(String[] args) {

		// Launch the server
		ServerDecoder decoder = new ServerDecoder(PORT_NUMBER);
		decoder.start();

		// Launch the client
		KeyboardEncoder encoder = new KeyboardEncoder(HOSTNAME, PORT_NUMBER);
		encoder.start();

	}

}
