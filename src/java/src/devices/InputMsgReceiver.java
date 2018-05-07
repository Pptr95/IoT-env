package devices;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

import events.DataScanningReceived;
import events.RadarSpinCompleted;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import utils.Observable;

public class InputMsgReceiver extends Observable implements SerialPortEventListener {

	private static final char SCANNED_SIGNAL = '[';
	private static final String SPIN_COMPLETED = "F";
	private SerialPort serialPort;
	private BufferedReader input;
	private OutputStream output;

	public InputMsgReceiver(String port, int rate) throws Exception {
		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
		// open serial port, and use class name for the appName.
		SerialPort serialPort = (SerialPort) portId.open(this.getClass().getName(), 2000);

		// set port parameters
		serialPort.setSerialPortParams(rate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

		// open the streams
		input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
		output = serialPort.getOutputStream();

		// add event listeners
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
	}

	public void sendMsg(String msg) {
		char[] array = (msg + "\n").toCharArray();
		byte[] bytes = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			bytes[i] = (byte) array[i];
		}
		try {
			output.write(bytes);
			output.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent port
	 * locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	private float getDistance(final String distance) {
		return Float.valueOf(Arrays.asList(distance.replace("[", "").replace("]", "").split(" ")).get(0));
	}

	private int getAngle(final String angle) {
		return Integer.valueOf(Arrays.asList(angle.replace("[", "").replace("]", "").split(" ")).get(1));
	}

	@Override
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String msg = input.readLine();
				if (msg.equals(SPIN_COMPLETED)) {
					notifyEvent(new RadarSpinCompleted());
				} else if (msg.charAt(0) == SCANNED_SIGNAL) {
					notifyEvent(new DataScanningReceived(this.getDistance(msg), this.getAngle(msg)));
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}
}
