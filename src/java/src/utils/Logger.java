package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;

public class Logger {

	private PrintWriter writer;
	private int num;
	
	public Logger() {
		this.num = 0;
	}
	
	public void initializeLog() throws FileNotFoundException, UnsupportedEncodingException {
		this.num++;
		this.writer = new PrintWriter(Paths.get(".").toAbsolutePath().normalize().toString()+File.separator+"src/smartradar/log" + this.num + ".txt", "UTF-8");
	}
	
	public void writeLog(final String log) {
		this.writer.println(log);
	}
	
	public void closeLog() {
		this.writer.close();
	}
}
