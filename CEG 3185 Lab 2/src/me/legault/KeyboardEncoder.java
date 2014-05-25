package me.legault;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class KeyboardEncoder extends Thread {
	
	private String hostname;
	private int portNumber;
	
	KeyboardEncoder(String hostname, int portNumber) {
		this.hostname = hostname;
		this.portNumber = portNumber;
	}
	
	public void run(){
		Socket socket = null;
		Scanner keyboard = null;
		try {
			socket = new Socket(this.hostname, this.portNumber);
			
			PrintStream output = new PrintStream(socket.getOutputStream());
			keyboard = new Scanner(System.in);
			
			while(true){
				String line = keyboard.nextLine();
				output.println(line);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(keyboard != null){
				keyboard.close();
			}
		}
		
	}

}
