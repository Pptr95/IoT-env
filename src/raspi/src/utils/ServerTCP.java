package utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServerTCP extends Thread {
	static int buffSize = 4096;
	private float temperature;
	private int ledIntensity;
	private 	DataOutputStream outToClient;

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public void setLedIntensity(int ledIntensity) {
		this.ledIntensity = ledIntensity;
	}

	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket servSock = new ServerSocket(6789); 
			while (true) {
				Socket connectionSocket = servSock.accept();
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				String request = inFromClient.readLine();
				processRequest(request);
				connectionSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processRequest(String request) throws IOException {
		String response = new String();
		if (request.equals("LOG")) {
			byte[] encoded;
			try {
				String path = Paths.get(".").toAbsolutePath().normalize().toString()+File.separator+ "logs" + File.separator
						+ new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()) + ".txt";
				encoded = Files.readAllBytes(Paths.get(path));
				response = new String(encoded, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (request.equals("UPDATE")) {
			response = new String(Float.toString(temperature) + "/" + Integer.toString(ledIntensity));
		}
		outToClient.writeBytes(response);
	}
}
