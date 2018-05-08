package application;

import devices.InputMsgReceiver;
import devices.Led;
import interfaces.Light;
import smartdoor.SmartDoor;
import utils.ServerUDP;

public class Application {
	public static void main(String[] args) throws Exception {
		Light ledInside = new Led(1);
		Light ledFailed = new Led(23);
		InputMsgReceiver serial = new InputMsgReceiver(args[0], Integer.valueOf(args[1]));
		ServerUDP server = new ServerUDP();
		SmartDoor sr = new SmartDoor(serial, server, ledInside, ledFailed);
		sr.start();
	}
}