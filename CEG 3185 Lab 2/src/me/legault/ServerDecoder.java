package me.legault;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerDecoder extends Thread {

	private int portNumber;

	ServerDecoder(int portNumber) {
		this.portNumber = portNumber;
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(this.portNumber);
			Socket socket = serverSocket.accept();
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
			StringBuffer inputLine = new StringBuffer();
			String tmp;
			while ((tmp = input.readLine()) != null) {
				inputLine.append(tmp);
				System.out.println(tmp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null){
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
