package cz.zweistein.df.soundsense.output.achievements;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import cz.zweistein.df.soundsense.config.achievement.Achievement;
import cz.zweistein.df.soundsense.config.achievement.AchievementPattern;
import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;
import cz.zweistein.df.soundsense.output.Procesor;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class AchievementsProcesor extends Procesor {
	private static Logger logger = LoggerSource.logger;
	
	private AchievementsXML achievementPatterns;
	
	public AchievementsProcesor(AchievementsXML achievementPatterns) {
		this.achievementPatterns = achievementPatterns;
	}

	@Override
	public void processLine(String line) {
		
		for (AchievementPattern ap: achievementPatterns.getAchievementPatterns()) {
			
			if (Pattern.matches(ap.getLogPattern(), line)) {
				ap.hit();
				
				Achievement achievement = ap.justAchieved();
				
				if (achievement != null) {
					logger.info("Achieved: " + achievement.getTitle()+" - "+achievement.getDescription()+" - "+ap.getHits()+"/"+achievement.getTriggerAmount());
				}
			}
		}
	}

}
