package utils;

import java.io.Serializable;
import java.util.ResourceBundle;

public class Resource implements Serializable {

	private static final long serialVersionUID = -3325080430848316075L;
	private final ResourceBundle res;

	public Resource() {
		this.res = ResourceBundle.getBundle("Users");
	}

	public boolean checkUserExist(final String username) {
		return this.res.containsKey(username);
	}

	public String getPassword(final String username) {
		return this.res.getString(username);
	}
	
	public static void main(String[] args) {
		Resource res = new Resource();
		System.out.println(res.getPassword("luigi"));
	}
}