package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

	private PrintWriter writer;

	public Logger() throws FileNotFoundException, UnsupportedEncodingException {
		final boolean  logsFolderCreation = new File(Paths.get(".").toAbsolutePath().normalize()+File.separator+"logs").mkdir();
		File log = new File(Paths.get(".").toAbsolutePath().normalize().toString()+File.separator+ "logs" + File.separator
				+ new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()) + ".txt");
		this.writer = new PrintWriter(new FileOutputStream(log, true));
	}

	public void writeLog(final String log) {
		this.writer.println(log);
		this.writer.flush();
	}

	public void closeLog() {
		this.writer.close();
	}
}
