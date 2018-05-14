package events;

import java.util.Arrays;
import java.util.List;

import interfaces.Event;

public class LoginDataReceived implements Event {

	private String username;
	private String password;
	
	public LoginDataReceived(String msg) {
		super();
		List<String> tmp = Arrays.asList(msg.replace("{", "").replace("}", "").split(" "));
		if(tmp.size() == 2) {
			this.username = tmp.get(0);
			this.password = tmp.get(1);
		} else {
			this.username = "-";
			this.password = "-";
		}
	}
	
	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}
}
