package smartdoor;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import devices.InputMsgReceiver;
import events.LoginDataReceived;
import events.LoginGranted;
import events.MotionNotDetected;
import interfaces.Event;
import interfaces.Light;
import utils.Logger;
import utils.Resource;

public class SmartDoor extends BasicEventLoopController {

	private enum State {
		IDLE, WAITING, LOGGED
	};

	private static final String LOGIN_OK = "O";
	private static final String LOGIN_FAIL = "K";
	private Logger logger;
	private State state;
	private InputMsgReceiver serialInput;
	private Light ledInside;
	private Light ledFailed;
	private final Resource res = new Resource();

	public SmartDoor(InputMsgReceiver serialInput, Light ledInside, Light ledFailed)
			throws FileNotFoundException, UnsupportedEncodingException {
		this.ledInside = ledInside;
		this.ledFailed = ledFailed;
		this.serialInput = serialInput;
		this.serialInput.addObserver(this);
		this.state = State.IDLE;
		this.logger = new Logger();
	}

	@Override
	protected void processEvent(Event ev) {
		try {
			switch (this.state) {
			case IDLE:
				if (ev instanceof LoginDataReceived) {
					LoginDataReceived event = ((LoginDataReceived) ev);
					if (res.checkUserExist(event.getUsername())
							&& res.getPassword(event.getUsername()).equals(event.getPassword())) {
						this.state = State.WAITING;
						this.serialInput.sendMsg(LOGIN_OK);
					} else {
						this.serialInput.sendMsg(LOGIN_FAIL);
						//FARE LOG
					}
				}
				break;

			case WAITING:
				if (ev instanceof LoginGranted) {
					this.state = State.LOGGED;
					this.ledInside.switchOn();
					//FARE LOG
				} else if (ev instanceof MotionNotDetected) {
					this.state = State.IDLE;
					//FARE LOG
					this.ledFailed.switchOn();
					Thread.sleep(300);
					this.ledFailed.switchOff();
				}
				break;

			/*case LOGGED:
				if (ev instanceof ButtonOffPressed) {
					this.state = State.IDLE;
					this.ledOn.switchOff();
					this.serialInput.sendMsg(IDLE);
					this.logger.closeLog();
				} else if (ev instanceof LoginDataReceived && ((LoginDataReceived) ev).getDistance() > DIST_MAX) {
					this.state = State.NOT_DETECTED;
				} else if (ev instanceof LoginDataReceived && ((LoginDataReceived) ev).getDistance() < DIST_MIN) {
					this.ledTracking.switchOn();
					this.state = State.TRACKING;
					final String msg = "Time: "
							+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
							+ " - Object tracked at angle " + ((LoginDataReceived) ev).getAngle() + " distance "
							+ ((LoginDataReceived) ev).getDistance();
					System.out.println(msg);
					this.serialInput.sendMsg(TRACK);
				} else if (ev instanceof LoginGranted) {
					System.out.println("Scan: " + this.counter.getScanCount() + " -- detected: "
							+ this.counter.getCount() + " objects");
					this.counter.incScanCount();
					this.counter.resetCount();
				}
				break;*/

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
