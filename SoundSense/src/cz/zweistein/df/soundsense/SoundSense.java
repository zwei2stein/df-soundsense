package cz.zweistein.df.soundsense;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.UIManager;

import org.xml.sax.SAXException;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.SoundsXML;
import cz.zweistein.df.soundsense.glue.Glue;
import cz.zweistein.df.soundsense.gui.GameLogValidator;
import cz.zweistein.df.soundsense.gui.Gui;
import cz.zweistein.df.soundsense.input.file.LogReader;
import cz.zweistein.df.soundsense.output.sound.SoundProcesor;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class SoundSense {
	private static Logger logger = LoggerSource.logger;
	
	public static void main(String[] args) {
		
		setLookAndFeel();
		
		try {

			logger.info("SoundSense for Dwarf Fortress is starting...");
			logger.info(getVersionString());
			
			ConfigurationXML configuration = new ConfigurationXML("configuration.xml");
			
			logger.info("Loading theme packs; default directory is "+configuration.getSoundpacksPath());
			SoundsXML soundsXML = new SoundsXML(configuration.getSoundpacksPath(), true, configuration.noWarnAbsolutePath());
			logger.info("Done loading "+soundsXML.toString()+", loaded "+soundsXML.getSounds().size()+" items.");
			
			new GameLogValidator(configuration).gamelogValidate();
			logger.info("Attempting to listen to "+configuration.getGamelogPath());
			LogReader logReader = new LogReader(configuration.getGamelogPath(), configuration.getGamelogEncoding());
			logger.info("Listening to "+configuration.getGamelogPath());
			
			SoundProcesor sp = new SoundProcesor(soundsXML, configuration);
			
			sp.setGlobalVolume(configuration.getVolume());
			
			Glue.glue(logReader, sp);
			
			String gamelogBasedir = new File(configuration.getGamelogPath()).getParentFile().getPath();
			for (String supplementalLogPath : configuration.getSupplementalLogs()) {
				supplementalLogPath = supplementalLogPath.replace("${gamelogBaseDir}", gamelogBasedir);
				if (new File(supplementalLogPath).exists()) {
					logger.info("Attempting to listen to supplemental "+supplementalLogPath);
					LogReader supplementalLogReader = new LogReader(supplementalLogPath, configuration.getGamelogEncoding());
					Glue.glue(supplementalLogReader, sp);
				} else {
					logger.info("Supplemental log "+supplementalLogPath+" not found.");
				}
			}

			if (configuration.getGui()) {
				Gui.newGui(configuration, sp);
			}
			
		} catch (IOException e) {
			logger.severe("Exception :"+e+": "+e.toString());
		} catch (SAXException e) {
			logger.severe("Exception :"+e+": "+e.toString());
		}
		
	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getVersionString() {
		Properties properties = new Properties();
		try {
			properties.load(SoundSense.class.getClassLoader().getResource("version.properties").openStream());
			return "release #" + properties.getProperty("release") + " build #" + properties.getProperty("buildNum") + " date " + properties.getProperty("buildDate");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}

}
