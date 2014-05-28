package me.legault.hdb3.endpoint;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class KeyboardEncoder extends Thread {

	private String hostname;
	private int portNumber;

	public KeyboardEncoder(String hostname, int portNumber) {
		this.hostname = hostname;
		this.portNumber = portNumber;
	}

	public void run() {
		Socket socket = null;
		Scanner keyboard = null;
		try {
			socket = new Socket(this.hostname, this.portNumber);

			PrintStream output = new PrintStream(socket.getOutputStream());
			keyboard = new Scanner(System.in);

			System.out.println("[KeyboardEncoder]: Sending request-to-send");
			output.println("request-to-send");

			// Handshaking: waiting for clear-to-send
			DataInputStream input = new DataInputStream(socket.getInputStream());
			String tmp;
			while ((tmp = input.readLine()) != null) {
				if (!tmp.equals("clear-to-send")) {
					System.err
							.println("[KeyboardEncoder]: The system is not ready to send");
				} else {
					System.out
							.println("[KeyboardEncoder]: Clear-to-send confirmation received");
				}
				break;
			}

			while (true) {
				String line = keyboard.nextLine();

				if (!validateBinaryInput(line)) {
					System.err
							.println("[KeyboardEncoder]: Please input a binary string");
					continue;
				}

				output.println(encodeBinaryStringIntoHDB3(line));
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (keyboard != null) {
				keyboard.close();
			}
		}

	}

	static boolean validateBinaryInput(String input) {
		int n = input.length();
		for (int i = 0; i < n; i++)
			if (input.charAt(i) != '0' && input.charAt(i) != '1')
				return false;
		return true;
	}

	static String encodeBinaryStringIntoHDB3(String input) {
		boolean lastOneNegative = true;
		boolean parityEven = true;
		boolean previousPulseNegative = false;
		int n = input.length();
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			if (input.charAt(i) == '1') {
				sb.append(lastOneNegative ? "+" : "-");
				lastOneNegative = !lastOneNegative;
				previousPulseNegative = lastOneNegative;
			} else {
				// Check for 4 zeros
				if (hasFourZeros(input, i)) {
					if (!parityEven) {
						sb.append(previousPulseNegative ? "-00-" : "+00+");
					} else {
						sb.append(previousPulseNegative ? "000+" : "000-");
						previousPulseNegative = !previousPulseNegative;
						parityEven = !parityEven;
					}
					i += 3;
				} else {
					sb.append("0");
				}
			}
		}
		return sb.toString();
	}

	static boolean hasFourZeros(String input, int index) {
		return input.length() > index + 3 && input.charAt(index) == '0'
				&& input.charAt(index + 1) == '0'
				&& input.charAt(index + 2) == '0'
				&& input.charAt(index + 3) == '0';
	}

}
