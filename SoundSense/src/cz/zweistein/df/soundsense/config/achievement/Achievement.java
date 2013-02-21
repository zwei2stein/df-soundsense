package cz.zweistein.df.soundsense.config.achievement;

public class Achievement {
	
	private long triggerAmount;
	private String title;
	private String description;
	private String image;
	
	public Achievement(long triggerAmount, String title, String description,	String image) {
		this.triggerAmount = triggerAmount;
		this.title = title;
		this.description = description;
		this.image = image;
	}
	public long getTriggerAmount() {
		return this.triggerAmount;
	}
	public String getTitle() {
		return this.title;
	}
	public String getDescription() {
		return this.description;
	}
	public String getImage() {
		return this.image;
	}
	
	
	
	

}
