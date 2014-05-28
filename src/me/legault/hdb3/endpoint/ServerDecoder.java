package me.legault.hdb3.endpoint;

import me.legault.hdb3.encoding.HDB3Encoder;

public class ServerDecoder extends Endpoint {

	public static final String ID = "Server";
	private int portNumber;

	public ServerDecoder(int portNumber) {
		super(ID);
		this.portNumber = portNumber;
	}

	public void run() {
		
		openServerSocket(this.portNumber, new Runnable(){

			@Override
			public void run() {
				// Waiting for the request-to-send
				listen(new Function<String>() {

					public boolean apply(String message) {
						if (!message.equals("request-to-send")) {
							println("The KeyboardEncoder is not ready to send");
							return false;
						} else {
							println("Request-to-send request received");
							println("Sending clear-to-send confirmation");
							send("clear-to-send");
							return true;
						}
					}

				});

				// Waiting for the data to decode
				listen(new Function<String>() {

					public boolean apply(String message) {
						println(HDB3Encoder.decode(message));
						return false;
					}

				});
			}
			
		});
	}
}
