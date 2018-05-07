package devices;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import events.ButtonOffPressed;
import events.ButtonOnPressed;
import interfaces.Event;
import utils.Config;

public class Button extends ObservableButton {

	private GpioPinDigitalInput pin;
	private int type;

	public Button(int pinNum) {
		super();
		this.type = pinNum;
		try {
			GpioController gpio = GpioFactory.getInstance();
			pin = gpio.provisionDigitalInputPin(Config.pinMap[pinNum], PinPullResistance.PULL_DOWN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pin.addListener(new ButtonListener(this));
	}

	@Override
	public synchronized boolean isPressed() {
		return pin.isHigh();
	}

	class ButtonListener implements GpioPinListenerDigital {
		Button button;
		private static final int ON = 0;

		public ButtonListener(Button button) {
			this.button = button;
		}

		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
			Event ev = null;
			if (event.getState().isHigh()) {
				if (Button.this.type == ON) {
					ev = new ButtonOnPressed(button);
				} else {
					ev = new ButtonOffPressed(button);
				}
				notifyEvent(ev);
			}
		}
	}
}
