package cz.zweistein.df.soundsense.util.log;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.jansi.AnsiConsole;

public class LoggerSource {
	public static Logger logger = Logger.getAnonymousLogger();
	
	static {
		try {
			System.setOut(new PrintStream(System.out, true, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// nothing, really.
		} 
		AnsiConsole.systemInstall();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				AnsiConsole.systemUninstall();
			}
		}));
		
        logger.setUseParentHandlers(false);
        SingleLineFormatter formatter = new SingleLineFormatter();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        logger.addHandler(handler);
        handler.setLevel(Level.FINEST);
	}

}
