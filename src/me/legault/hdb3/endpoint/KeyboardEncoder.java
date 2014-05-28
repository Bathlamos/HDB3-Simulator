package me.legault.hdb3.endpoint;

import java.util.Scanner;

import me.legault.hdb3.encoding.HDB3Encoder;

public class KeyboardEncoder extends Endpoint {

	public static final String ID = "Client";
	
	private String hostname;
	private int portNumber;

	public KeyboardEncoder(String hostname, int portNumber) {
		super(ID);
		this.hostname = hostname;
		this.portNumber = portNumber;
	}

	public void run() {
		
		openSocket(this.hostname, this.portNumber, new Runnable(){

			@Override
			public void run() {
				println("Sending request-to-send");
				send("request-to-send");
				
				// Handshaking: waiting for clear-to-send
				listen(new Function<String>() {

					public boolean apply(String message) {
						if (!message.equals("clear-to-send")) {
							println("The system is not ready to send");
							return false;
						}
						println("Clear-to-send confirmation received");
						return true;
					}

				});
				
				//Listen for data
				Scanner keyboard = new Scanner(System.in);
				String line;
				println("Now listening for keyboard input");
				do {
					line = keyboard.nextLine();

					if (!validateBinaryInput(line)) {
						println("Please input a binary string");
						continue;
					}

					println("Text input " + line);
					String encoded = HDB3Encoder.encode(line);
					println("Encoded to " + encoded);
					send(encoded);
				}while(line != null);
				
				keyboard.close();
			}
			
		});
	}

	private static boolean validateBinaryInput(String input) {
		int n = input.length();
		for (int i = 0; i < n; i++)
			if (input.charAt(i) != '0' && input.charAt(i) != '1')
				return false;
		return true;
	}

}
