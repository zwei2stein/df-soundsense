package cz.zweistein.df.soundsense.input.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import cz.zweistein.df.soundsense.input.Listener;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class LogReader extends Listener {
	private static Logger logger = LoggerSource.LOGGER;

	public LogReader(String logFile, String encoding) throws IOException {
		this(logFile, encoding, true, true);
	}

	public LogReader(String logFile, String encoding, boolean skip, boolean streaming) throws IOException {

		FileInputStream in = new FileInputStream(logFile);
		// reading as MS-DOS Latin-1 alphabet "Cp850"
		InputStreamReader isr = new InputStreamReader(in, encoding);
		this.br = new BufferedReader(isr);
		this.streaming = streaming;

		if (skip) {
			long length = new File(logFile).length();
			logger.info("Skipping " + length + " bytes.");
			br.skip(length);
		}

	}

}
