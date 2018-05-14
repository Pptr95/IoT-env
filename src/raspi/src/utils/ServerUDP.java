package utils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServerUDP extends Thread {
	static int buffSize = 4096;
	private float temperature;
	private int ledIntensity;

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public void setLedIntensity(int ledIntensity) {
		this.ledIntensity = ledIntensity;
	}

	public void run() {
		try {
			final int port = 6789;
			@SuppressWarnings("resource")
			DatagramSocket servSock = new DatagramSocket(port);
			while (true) {
				byte[] recvBuff = new byte[buffSize];
				DatagramPacket newPacket = new DatagramPacket(recvBuff, buffSize);
				servSock.receive(newPacket);
				processRequest(newPacket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processRequest(DatagramPacket recvPacket) throws IOException {
		byte[] recvBuff = new byte[buffSize];
		recvBuff = recvPacket.getData();
		String request = new String(recvBuff, 0, recvPacket.getLength());
		InetAddress clientAddr = recvPacket.getAddress();
		int clientPort = recvPacket.getPort();
		String response = new String();
		
		System.out.println(request);
		
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
		System.out.println(response);
		DatagramSocket sock = new DatagramSocket();
		byte[] sendBuff = new byte[buffSize];
		sendBuff = response.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendBuff, sendBuff.length, clientAddr, clientPort);
		sock.send(sendPacket);
		System.out.println("INVIATAAAAAAA   " + clientAddr.toString() + "   "+ clientPort);
		sock.close();
	}
}
