package cz.zweistein.df.soundsense.glue;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Logger;

import cz.zweistein.df.soundsense.input.Listener;
import cz.zweistein.df.soundsense.output.Procesor;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class GlueThread implements Runnable {
	private static Logger logger = LoggerSource.LOGGER;

	private Listener listener;
	private List<Procesor> procesors;

	public GlueThread(Listener listener, List<Procesor> procesors) {
		this.listener = listener;
		this.procesors = procesors;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String line = listener.getNextLine();
				for (Procesor procesor : this.procesors) {
					try {
						procesor.processLine(line);
					} catch (ConcurrentModificationException e) {
						logger.fine("Exception :" + e + ": " + e.toString());
					}
				}
			}
		} catch (IOException e) {
			logger.severe("Exception :" + e + ": " + e.toString());
		}
	}

}
