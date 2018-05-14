package serialMonitor;

import java.net.*;
import java.io.*;

public class ClientTCP extends Thread {
	final static String LOG_REQUEST = "LOG";
	final static String UPDATE_REQUEST = "UPDATE";
	final int servPort = 6789;
	private Socket sock;
	private OutputStream sendStream;
	private InputStream recvStream;
	private Monitor monitor;
	private String serverAddress;

	public ClientTCP(final Monitor monitor, final String serverAddress) {
		this.serverAddress = serverAddress;
		this.monitor = monitor;
	}

	void connect() throws UnknownHostException, IOException {
		sock = new Socket(serverAddress, servPort);
		sendStream = sock.getOutputStream();
		recvStream = sock.getInputStream();
	}

	void close() throws IOException {
		sendStream.close();
		recvStream.close();
		sock.close();
	}

	void sendRequest(String request) throws IOException {
		byte[] sendBuff = new byte[request.length()];
		sendBuff = request.getBytes();
		sendStream.write(sendBuff, 0, sendBuff.length);
		sendStream.write("\n".getBytes());
		sendStream.flush();
	}

	String getResponse() throws IOException {
		int dataSize = 1;
		byte[] recvBuff = new byte[1024];
		String response = new String();
		while (dataSize > 0) {
			dataSize = recvStream.read(recvBuff, 0, 1024);
			System.out.println(dataSize);
			String buff_read = new String(recvBuff, 0, dataSize);
			response = response + buff_read;
			if (recvBuff[dataSize - 1] == '\n')
				dataSize = -1;
		}
		return response;
	}

	public void run() {
		try {
			String request;
			String response;
			while (true) {
				request = LOG_REQUEST;
				connect();
				sendRequest(request);
				response = getResponse();
				close();
				this.monitor.updateLog(response);
				request = UPDATE_REQUEST;
				connect();
				sendRequest(request);
				response = getResponse();
				close();
				String[] parts = response.split("/");
				this.monitor.updateData(parts[0], parts[1]);
				Thread.sleep(5000);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}