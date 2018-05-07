package events;

import devices.Button;
import interfaces.Event;

public class ButtonOnPressed implements Event {
	private Button source;
	
	public ButtonOnPressed(Button source){
		this.source = source;
	}
	
	public Button getSourceButton(){
		return source;
	}
}
