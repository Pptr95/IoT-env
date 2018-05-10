package devices;

import java.io.IOException;
import com.pi4j.io.gpio.*;

import interfaces.Light;
import utils.Config;

public class Led implements Light {
	private GpioPinDigitalOutput pin;
	
	public Led(int pinNum){
		try {
		    GpioController gpio = GpioFactory.getInstance();
		    this.pin = gpio.provisionDigitalOutputPin(Config.pinMap[pinNum]);	
		    this.switchOff();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public synchronized void switchOn() throws IOException {
		this.pin.high();		
	}

	@Override
	public synchronized void switchOff() throws IOException {
		this.pin.low();
	}
}
