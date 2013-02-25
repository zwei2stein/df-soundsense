package cz.zweistein.df.soundsense.util;

import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.UIManager;

import org.xml.sax.SAXException;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;
import cz.zweistein.df.soundsense.glue.Glue;
import cz.zweistein.df.soundsense.gui.GameLogValidator;
import cz.zweistein.df.soundsense.input.file.LogReader;
import cz.zweistein.df.soundsense.output.achievements.AchievementsProcesor;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class AchievementsTool {
	
	private static Logger logger = LoggerSource.logger;
	
	public static void main(String[] args) {
		
		setLookAndFeel();
		
		try {
			logger.info("Achievements for SoundSense is starting...");
			
			ConfigurationXML configuration = new ConfigurationXML("configuration.xml");
			new GameLogValidator(configuration).gamelogValidate();

			AchievementsXML achievementsConfugration = new AchievementsXML("achievements/achievements.xml");
			
			logger.info("Attempting to open "+configuration.getGamelogPath());
			LogReader logReader = new LogReader(configuration.getGamelogPath(), configuration.getGamelogEncoding(), false, true);
			logger.info("Parsing "+configuration.getGamelogPath());
			
			AchievementsProcesor ap = new AchievementsProcesor(achievementsConfugration);
			
			Glue.glue(logReader, ap);
			
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

}
