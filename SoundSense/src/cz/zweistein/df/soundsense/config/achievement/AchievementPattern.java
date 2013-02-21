package cz.zweistein.df.soundsense.config.achievement;

import java.util.List;

public class AchievementPattern {
	
	private String logPattern;
	private List<Achievement> achievements;
	private int hits;
	
	public AchievementPattern(String logPattern, List<Achievement> achievements) {
		this.logPattern = logPattern;
		this.achievements = achievements;
		this.hits = 0;
	}

	public String getLogPattern() {
		return this.logPattern;
	}

	public List<Achievement> getAchievements() {
		return this.achievements;
	}

	public int getHits() {
		return this.hits;
	}
	
	public void hit() {
		this.hits++;
	}
	
	public Achievement justAchieved() {
		
		for (Achievement achievement: this.achievements) {
			if (achievement.getTriggerAmount() == this.getHits()) {
				return achievement;
			}
		}
		
		return null;
	}

}
