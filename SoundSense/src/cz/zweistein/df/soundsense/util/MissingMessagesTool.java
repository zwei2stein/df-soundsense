package cz.zweistein.df.soundsense.util;

import java.io.IOException;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.sounds.SoundsXML;
import cz.zweistein.df.soundsense.glue.Glue;
import cz.zweistein.df.soundsense.input.file.LogReader;
import cz.zweistein.df.soundsense.output.analysis.MissingMessagesProcessor;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class MissingMessagesTool {

	private static Logger logger = LoggerSource.LOGGER;

	public static void main(String[] args) {

		try {

			ConfigurationXML configuration = new ConfigurationXML("configuration.xml");

			logger.info("MissingMessagesTool for SoundSense is starting...");
			logger.info("Loading theme packs from " + configuration.getSoundpacksPath());
			SoundsXML soundsXML = new SoundsXML(configuration.getSoundpacksPath(), false);
			logger.info("Done loading " + configuration.getSoundpacksPath() + ", loaded " + soundsXML.getSounds().size() + " items.");

			logger.info("Attempting to open " + configuration.getGamelogPath());
			LogReader logReader = new LogReader(configuration.getGamelogPath(), configuration.getGamelogEncoding(), false, false);
			logger.info("Parsing " + configuration.getGamelogPath());

			MissingMessagesProcessor mmp = new MissingMessagesProcessor(soundsXML);

			Glue.glue(logReader, mmp);

		} catch (IOException e) {
			logger.severe("Exception :" + e + ": " + e.toString());
		} catch (SAXException e) {
			logger.severe("Exception :" + e + ": " + e.toString());
		}

	}

}
