package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

	private PrintWriter writer;

	public Logger() throws FileNotFoundException, UnsupportedEncodingException {
		String homeFolder = System.getProperty("user.home");
		File log = new File(homeFolder + File.separator + "SmartDoor_Logs" + File.separator
				+ new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()) + ".txt");
		this.writer = new PrintWriter(new FileOutputStream(log, true));
	}

	public void writeLog(final String log) {
		this.writer.println(log);
	}

	public void closeLog() {
		this.writer.close();
	}
}
