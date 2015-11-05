package cz.zweistein.df.soundsense.util.log;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.jansi.AnsiConsole;

public final class LoggerSource {
	public static final Logger LOGGER = Logger.getAnonymousLogger();

	private LoggerSource() {
	}

	static {
		try {
			System.setOut(new PrintStream(System.out, true, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		AnsiConsole.systemInstall();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				AnsiConsole.systemUninstall();
			}
		}));

		LOGGER.setUseParentHandlers(false);
		SingleLineFormatter formatter = new SingleLineFormatter();
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(formatter);
		LOGGER.addHandler(handler);
		handler.setLevel(Level.FINEST);
	}

}
