package smartradar;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import devices.InputMsgReceiver;
import devices.ObservableButton;
import events.ButtonOffPressed;
import events.ButtonOnPressed;
import events.DataScanningReceived;
import events.RadarSpinCompleted;
import interfaces.Event;
import interfaces.Light;
import utils.Counter;
import utils.Logger;

public class SmartRadar extends BasicEventLoopController {

	private enum State {
		IDLE, NOT_DETECTED, DETECTED, TRACKING
	};

	private static final float DIST_MAX = 0.50f;
	private static final float DIST_MIN = 0.20f;
	private static final String RUN = "R";
	private static final String IDLE = "I";
	private static final String TRACK = "T";
	private static final String START = "S";
	private Counter counter;
	private Logger logger;
	private State state;
	private InputMsgReceiver serialInput;
	private Light ledOn;
	private Light ledDetected;
	private Light ledTracking;

	public SmartRadar(ObservableButton onButton, ObservableButton offButton, InputMsgReceiver serialInput, Light ledOn,
			Light ledDetected, Light ledTracking) throws FileNotFoundException, UnsupportedEncodingException {
		this.ledOn = ledOn;
		this.ledDetected = ledDetected;
		this.ledTracking = ledTracking;
		this.serialInput = serialInput;
		this.serialInput.addObserver(this);
		onButton.addObserver(this);
		offButton.addObserver(this);
		this.state = State.IDLE;
		this.counter = new Counter();
		this.logger = new Logger();
	}

	@Override
	protected void processEvent(Event ev) {
		try {
			switch (this.state) {
			case IDLE:
				if (ev instanceof ButtonOnPressed) {
					this.state = State.NOT_DETECTED;
					this.ledOn.switchOn();
					this.serialInput.sendMsg(RUN);
					this.logger.initializeLog();
				}
				break;

			case NOT_DETECTED:
				if (ev instanceof ButtonOffPressed) {
					this.state = State.IDLE;
					this.ledOn.switchOff();
					this.serialInput.sendMsg(IDLE);
					this.logger.closeLog();
				} else if (ev instanceof DataScanningReceived && ((DataScanningReceived) ev).getDistance() < DIST_MAX) {
					this.state = State.DETECTED;
					this.ledDetected.switchOn();
					Thread.sleep(100);
					this.ledDetected.switchOff();
					final String msg = "Time: "
							+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
							+ " - Object detected at angle " + ((DataScanningReceived) ev).getAngle();
					this.logger.writeLog(msg);
					this.counter.incCount();
					System.out.println(msg);
				} else if (ev instanceof RadarSpinCompleted) {
					System.out.println("Scan: " + this.counter.getScanCount() + " -- detected: "
							+ this.counter.getCount() + " objects");
					this.counter.incScanCount();
					this.counter.resetCount();
				}
				break;

			case DETECTED:
				if (ev instanceof ButtonOffPressed) {
					this.state = State.IDLE;
					this.ledOn.switchOff();
					this.serialInput.sendMsg(IDLE);
					this.logger.closeLog();
				} else if (ev instanceof DataScanningReceived && ((DataScanningReceived) ev).getDistance() > DIST_MAX) {
					this.state = State.NOT_DETECTED;
				} else if (ev instanceof DataScanningReceived && ((DataScanningReceived) ev).getDistance() < DIST_MIN) {
					this.ledTracking.switchOn();
					this.state = State.TRACKING;
					final String msg = "Time: "
							+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
							+ " - Object tracked at angle " + ((DataScanningReceived) ev).getAngle() + " distance "
							+ ((DataScanningReceived) ev).getDistance();
					System.out.println(msg);
					this.serialInput.sendMsg(TRACK);
				} else if (ev instanceof RadarSpinCompleted) {
					System.out.println("Scan: " + this.counter.getScanCount() + " -- detected: "
							+ this.counter.getCount() + " objects");
					this.counter.incScanCount();
					this.counter.resetCount();
				}
				break;

			case TRACKING:
				if (ev instanceof ButtonOffPressed) {
					this.state = State.IDLE;
					this.ledOn.switchOff();
					this.ledTracking.switchOff();
					this.serialInput.sendMsg(IDLE);
					this.logger.closeLog();
				} else if (ev instanceof DataScanningReceived && ((DataScanningReceived) ev).getDistance() > DIST_MAX) {
					this.state = State.NOT_DETECTED;
					this.ledTracking.switchOff();
					this.serialInput.sendMsg(START);
				} else if (ev instanceof DataScanningReceived && ((DataScanningReceived) ev).getDistance() < DIST_MAX
						&& ((DataScanningReceived) ev).getDistance() > DIST_MIN) {
					this.state = State.DETECTED;
					this.ledTracking.switchOff();
					this.serialInput.sendMsg(START);
				}
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
