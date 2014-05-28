package me.legault.hdb3.endpoint;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Endpoint {
	
	private String id;
	protected Socket socket;
	
	Endpoint(String id){
		this.id = id;
	}
	
	@SuppressWarnings("deprecation")
	void listen(Function<String> listener){
		if(socket == null){
			println("Cannot operate with a null socket");
			return;
		}else if(listener == null){
			println("The listener needs to be set");
			return;
		}
		
		DataInputStream input;
		String tmp;
		try {
			input = new DataInputStream(socket.getInputStream());
			while ((tmp = input.readLine()) != null) {
				if(listener.apply(tmp))
					break;
				
			}
		} catch (IOException e) {
			println("An exception has occured while reading from the socket");
		}
	}

	void println(String message){
		System.out.println("[" + id + "] " + message);
	}
	
}
