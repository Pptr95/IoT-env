package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;

public class Logger {

	private PrintWriter writer;
	
	//DA AGGIUSTARE
	public Logger() throws FileNotFoundException, UnsupportedEncodingException {
		this.writer = new PrintWriter(new FileOutputStream(new File("log.txt"),true));
		//this.writer = new PrintWriter(Paths.get(".").toAbsolutePath().normalize().toString()+File.separator+"src/smartdoor/log" +   ".txt", "UTF-8");
	}
	
	public void writeLog(final String log) {
		this.writer.println(log);
	}
	
	public void closeLog() {
		this.writer.close();
	}
}
