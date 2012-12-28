package cz.zweistein.df.soundsense.output.achievements;

public class Achievement {
	
	private int triggerAmount;
	private String title;
	private String description;
	private String image;
	
	public Achievement(int triggerAmount, String title, String description,	String image) {
		this.triggerAmount = triggerAmount;
		this.title = title;
		this.description = description;
		this.image = image;
	}
	public int getTriggerAmount() {
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
