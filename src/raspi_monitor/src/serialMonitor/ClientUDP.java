package serialMonitor;

import java.net.*;
import java.io.*;

public class ClientUDP extends Thread {
	final static String LOG_REQUEST = "LOG";
	final static String UPDATE_REQUEST = "UPDATE";
	final int buffSize = 4096;
	final int servPort = 6789;
	DatagramSocket sock;
	InetAddress servAddr;
	Monitor monitor;

	public ClientUDP(final Monitor monitor, final String serverAddress) throws SocketException, UnknownHostException {
		this.sock = new DatagramSocket();
		this.servAddr = InetAddress.getByName(serverAddress);
	}

	void sendRequest(String request) throws IOException {
		byte[] sendBuff = new byte[buffSize];
		sendBuff = request.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendBuff, sendBuff.length, this.servAddr, this.servPort);
		sock.send(sendPacket);
	}

	String getResponse() throws IOException {
		byte[] recvBuff = new byte[buffSize];
		DatagramPacket recvPacket = new DatagramPacket(recvBuff, buffSize);
		sock.receive(recvPacket);
		recvBuff = recvPacket.getData();
		return new String(recvBuff, 0, recvPacket.getLength());
	}

	public void run() {
		try {
			String request = new String();
			String response = new String();
			while (true) {
				request = LOG_REQUEST;
				sendRequest(request);
				response = getResponse();
				this.monitor.updateLog(response);
				request = UPDATE_REQUEST;
				sendRequest(request);
				response = getResponse();
				String[] parts = response.split("/");
				this.monitor.updateData(parts[0], parts[1]);
				Thread.sleep(5000);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}