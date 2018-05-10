package events;

import java.util.Arrays;
import java.util.List;

import interfaces.Event;

public class WorkingDataReceived implements Event {

	private float temperature;
	private int ledIntensity;
	
	public WorkingDataReceived(String msg) {
		super();
		List<String> tmp = Arrays.asList(msg.replace("[", "").replace("]", "").split(" "));
		this.temperature = Float.valueOf(tmp.get(0));
		this.ledIntensity = Integer.valueOf(tmp.get(1));
	}

	public float getTemperature() {
		return this.temperature;
	}

	public int getLedIntensity() {
		return this.ledIntensity;
	}
}
