package events;

import interfaces.Event;

public class DataScanningReceived implements Event {

	private float distance;
	private int angle;

	public DataScanningReceived(float distance, int angle) {
		super();
		this.distance = distance;
		this.angle = angle;
	}

	public float getDistance() {
		return this.distance;
	}
	
	public int getAngle() {
		return this.angle;
	}	
}
