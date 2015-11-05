package cz.zweistein.df.soundsense;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.xml.sax.SAXException;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;
import cz.zweistein.df.soundsense.config.executor.ExecutorXML;
import cz.zweistein.df.soundsense.config.sounds.SoundsXML;
import cz.zweistein.df.soundsense.glue.Glue;
import cz.zweistein.df.soundsense.gui.GameLogValidator;
import cz.zweistein.df.soundsense.gui.Gui;
import cz.zweistein.df.soundsense.input.file.LogReader;
import cz.zweistein.df.soundsense.output.Procesor;
import cz.zweistein.df.soundsense.output.achievements.AchievementsProcesor;
import cz.zweistein.df.soundsense.output.call.ExecutorProcesor;
import cz.zweistein.df.soundsense.output.sound.SoundProcesor;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class SoundSense {
	private static final Logger LOGGER = LoggerSource.LOGGER;

	public static void main(String[] args) {

		setLookAndFeel();

		try {

			LOGGER.info("SoundSense for Dwarf Fortress is starting...");
			LOGGER.info(getVersionString());

			ConfigurationXML configurationXML = new ConfigurationXML("configuration.xml");
			new GameLogValidator(configurationXML).gamelogValidate();
			String gameBaseDir = new File(configurationXML.getGamelogPath()).getParentFile().getPath();

			LOGGER.info("Loading theme packs; default directory is " + configurationXML.getSoundpacksPath());
			SoundsXML soundsXML = new SoundsXML(configurationXML.getSoundpacksPath(), true, configurationXML.noWarnAbsolutePath());
			LOGGER.info("Done loading " + soundsXML.toString() + ", loaded " + soundsXML.getSounds().size() + " items.");

			LOGGER.info("Loading executors.");
			ExecutorXML executorXML = new ExecutorXML("./executor/");
			LOGGER.info("Done loading executors, loaded " + executorXML.getExecutors().size() + " items.");

			LOGGER.info("Loading achievements.");
			AchievementsXML achievementsXML = new AchievementsXML("./achievements/");
			LOGGER.info("Done loading achievements, loaded " + achievementsXML.getAchievementPatterns().size() + " items.");

			LOGGER.info("Attempting to listen to " + configurationXML.getGamelogPath());
			LogReader logReader = new LogReader(configurationXML.getGamelogPath(), configurationXML.getGamelogEncoding());
			LOGGER.info("Listening to " + configurationXML.getGamelogPath());

			List<Procesor> procesors = new ArrayList<Procesor>(2);
			SoundProcesor sp = new SoundProcesor(soundsXML, configurationXML);
			procesors.add(sp);
			ExecutorProcesor ep = new ExecutorProcesor(executorXML, gameBaseDir);
			procesors.add(ep);
			AchievementsProcesor ap = new AchievementsProcesor(achievementsXML, configurationXML);
			procesors.add(ap);

			sp.setGlobalVolume(configurationXML.getVolume());

			Glue.glue(logReader, procesors);

			for (String supplementalLogPath : configurationXML.getSupplementalLogs()) {
				supplementalLogPath = supplementalLogPath.replace("${gameBaseDir}", gameBaseDir);
				if (new File(supplementalLogPath).exists()) {
					LOGGER.info("Attempting to listen to supplemental " + supplementalLogPath);
					LogReader supplementalLogReader = new LogReader(supplementalLogPath, configurationXML.getGamelogEncoding());
					Glue.glue(supplementalLogReader, procesors);
				} else {
					LOGGER.info("Supplemental log " + supplementalLogPath + " not found. That is okay if you are not using dfhack plugin.");
				}
			}

			if (configurationXML.getGui()) {
				Gui gui = Gui.newGui(configurationXML, sp, achievementsXML, ap);

				if (soundsXML.getSounds().size() < configurationXML.getExpectedMinimalSoundsCount()) {
					LOGGER.warning("It seems that there are too few sounds defined. Is soundpack installed?");
					int result = JOptionPane.showConfirmDialog(null,
							"It seems that there are too few sounds defined and thus soundpack might not be installed.\n"
									+ "Easiest way to solve this issue is to go to tab 'Pack update' and click 'Start update' button.\n"
									+ "Should I do that rightaway?", "SoundSense", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
					if (JOptionPane.YES_OPTION == result) {
						gui.triggerSoundPackUpdate();
					}
				}

			}

		} catch (IOException e) {
			LOGGER.severe("Exception :" + e + ": " + e.toString());
		} catch (SAXException e) {
			LOGGER.severe("Exception :" + e + ": " + e.toString());
		}

	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructs human readable information about build and version.
	 * 
	 * @return string with human readable version
	 */
	public static String getVersionString() {
		Properties properties = new Properties();
		try {
			properties.load(SoundSense.class.getClassLoader().getResource("version.properties").openStream());
			return "release #" + properties.getProperty("release") + " build #" + properties.getProperty("buildNum") + " date "
					+ properties.getProperty("buildDate");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

}
