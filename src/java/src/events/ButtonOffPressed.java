package events;

import devices.Button;
import interfaces.Event;

public class ButtonOffPressed implements Event {
	private Button source;
	
	public ButtonOffPressed(Button source){
		this.source = source;
	}
	
	public Button getSourceButton(){
		return source;
	}
}
