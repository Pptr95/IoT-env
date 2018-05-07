package application;

import devices.Button;
import devices.InputMsgReceiver;
import devices.Led;
import devices.ObservableButton;
import interfaces.Light;
import smartradar.SmartRadar;

public class Application {
	public static void main(String[] args) throws Exception {
		Light ledOn = new Led(1);
		Light ledDetected = new Led(4);
		Light ledTracking = new Led(7);
		ObservableButton onButton = new Button(0);
		ObservableButton offButton = new Button(2);
		InputMsgReceiver serial = new InputMsgReceiver(args[0], Integer.valueOf(args[1]));
		SmartRadar sr = new SmartRadar(onButton, offButton, serial, ledOn, ledDetected, ledTracking);
		sr.start();
	}
}