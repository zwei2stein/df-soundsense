package cz.zweistein.df.soundsense.output.achievements;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.achievement.Achievement;
import cz.zweistein.df.soundsense.config.achievement.AchievementPattern;
import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;
import cz.zweistein.df.soundsense.output.Procesor;
import cz.zweistein.df.soundsense.output.sound.player.ChangeCallback;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class AchievementsProcesor extends Procesor {
	private static Logger logger = LoggerSource.LOGGER;

	private AchievementsXML achievementPatterns;
	private NotificationManager nm;
	private ConfigurationXML configurationXML;

	private List<ChangeCallback> changeCallbacks;

	public AchievementsProcesor(AchievementsXML achievementPatterns, ConfigurationXML configurationXML) {
		this.configurationXML = configurationXML;
		this.changeCallbacks = new LinkedList<ChangeCallback>();
		this.achievementPatterns = achievementPatterns;
		this.nm = new NotificationManager();
	}

	@Override
	public void processLine(String line) {

		if (configurationXML.getAchievements()) {

			for (AchievementPattern ap : achievementPatterns.getAchievementPatterns()) {

				if (Pattern.matches(ap.getLogPattern(), line)) {
					ap.hit();

					Achievement achievement = ap.justAchieved();

					if (achievement != null) {
						logger.info("Achieved: " + achievement.getTitle() + " - " + achievement.getDescription() + " - " + ap.getHits() + "/" + achievement.getTriggerAmount());
						this.nm.showNotification(achievement, ap.getHits());

						for (ChangeCallback changeCallback : this.changeCallbacks) {
							changeCallback.changed();
						}
					}
				}
			}

		}

	}

	public void addChangeCallback(ChangeCallback changeCallback) {
		this.changeCallbacks.add(changeCallback);
	}

}
