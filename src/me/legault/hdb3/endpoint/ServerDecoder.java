package me.legault.hdb3.endpoint;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerDecoder extends Thread {

	private int portNumber;

	public ServerDecoder(int portNumber) {
		this.portNumber = portNumber;
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(this.portNumber);
			Socket socket = serverSocket.accept();
			DataInputStream input = new DataInputStream(socket.getInputStream());
			// Waiting for the request=to-send
			String tmp;
			while ((tmp = input.readLine()) != null) {
				if (!tmp.equals("request-to-send")) {
					System.err
							.println("[ServerDecoder]: The KeyboardEncoder is not ready to send");
				} else {
					System.out
							.println("[ServerDecoder]: Request-to-send request received");
					System.out
							.println("[ServerDecoder]: Sending clear-to-send confirmation");
					PrintStream output = new PrintStream(
							socket.getOutputStream());
					output.println("clear-to-send");
				}
				break;
			}
			StringBuffer inputLine = new StringBuffer();
			while ((tmp = input.readLine()) != null) {
				inputLine.append(tmp);
				System.out.println(tmp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
