package smartdoor;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import devices.InputMsgReceiver;
import events.LoginDataReceived;
import events.LoginGranted;
import events.MotionNotDetected;
import events.SessionEnded;
import events.WorkingDataReceived;
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
	private String currentUser;

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
						this.currentUser = event.getUsername();
						this.state = State.WAITING;
						this.serialInput.sendMsg(LOGIN_OK);
					} else {
						this.serialInput.sendMsg(LOGIN_FAIL);
						final String msg = "Time: "
								+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
								+ " - Login failed for user: " + event.getUsername();
						logger.writeLog(msg);
					}
				}
				break;

			case WAITING:
				if (ev instanceof LoginGranted) {
					this.state = State.LOGGED;
					this.ledInside.switchOn();
					final String msg = "Time: "
							+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
							+ " - Login granted for user: " + this.currentUser;
					logger.writeLog(msg);
				} else if (ev instanceof MotionNotDetected) {
					this.state = State.IDLE;
					final String msg = "Time: "
							+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
							+ " - Motion sensor didn't detect the user: " + this.currentUser;
					logger.writeLog(msg);
					this.ledFailed.switchOn();
					Thread.sleep(300);
					this.ledFailed.switchOff();
				}
				break;

			case LOGGED:
				if (ev instanceof WorkingDataReceived) {
					//CREARE SOCKET E AGG TEMP E L INTENSITY
				}else if (ev instanceof SessionEnded) {
					ledInside.switchOff();
					final String msg = "Time: "
							+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
							+ " - Motion sensor didn't detect the user: " + this.currentUser;
					logger.writeLog(msg);
				}
				break;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}



/*final String msg = "Time: "
		+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
		+ " - Object tracked at angle " + ((LoginDataReceived) ev).getAngle() + " distance "
		+ ((LoginDataReceived) ev).getDistance();
this.serialInput.sendMsg(TRACK);*/
