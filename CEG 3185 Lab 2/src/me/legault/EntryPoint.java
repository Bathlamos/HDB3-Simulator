package me.legault;

public class EntryPoint {

	static String HOSTNAME = "localhost";
	static int PORT_NUMBER = 65234;

	public static void main(String[] args) {
		// Launch the server
		ServerDecoder decoder = new ServerDecoder(PORT_NUMBER);
		decoder.start();

		// Launch the client
		KeyboardEncoder encoder = new KeyboardEncoder(HOSTNAME,
				PORT_NUMBER);
		encoder.start();
	}

}
