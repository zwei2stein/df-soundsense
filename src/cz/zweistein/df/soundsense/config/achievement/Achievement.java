package cz.zweistein.df.soundsense.config.achievement;

public class Achievement implements Comparable<Achievement> {

	private long triggerAmount;
	private String title;
	private String description;
	private String image;

	public Achievement(long triggerAmount, String title, String description, String image) {
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

	@Override
	public int compareTo(Achievement o) {
		long p1 = this.getTriggerAmount();
		long p2 = o.getTriggerAmount();

		if (p1 > p2) {
			return 1;
		} else if (p1 < p2) {
			return -1;
		} else {
			return 0;
		}
	}

}
