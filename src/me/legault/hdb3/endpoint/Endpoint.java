package me.legault.hdb3.endpoint;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Endpoint extends Thread {

	private String id;
	protected Socket socket;

	Endpoint(String id) {
		this.id = id;
	}

	void openServerSocket(int portNumber, Runnable process) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			socket = serverSocket.accept();
			process.run();
		} catch (IOException e) {
			println("ServerSocket could not be open");
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			closeSocket();
		}
	}

	void openSocket(String hostname, int portNumber, Runnable process) {
		try {
			socket = new Socket(hostname, portNumber);
			process.run();
		} catch (IOException e) {
			println("Socket could not be open");
		} finally {
			closeSocket();
		}
	}
	
	private void closeSocket(){
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	void listen(Function<String> listener) {
		if (socket == null) {
			println("Cannot operate with a null socket");
			return;
		} else if (listener == null) {
			println("The listener needs to be set");
			return;
		}

		DataInputStream input;
		String tmp;
		try {
			input = new DataInputStream(socket.getInputStream());
			while ((tmp = input.readLine()) != null) {
				if (listener.apply(tmp))
					break;

			}
		} catch (IOException e) {
			println("An exception has occured while reading from the socket");
		}
	}

	void send(String data) {
		if (socket == null) {
			println("Cannot operate with a null socket");
			return;
		}

		PrintStream output;
		try {
			output = new PrintStream(socket.getOutputStream());
			output.println(data);
		} catch (IOException e) {
			println("An exception has occured while sending to the socket");
		}
	}

	void println(String message) {
		System.out.println("[" + id + "] " + message);
	}

}
