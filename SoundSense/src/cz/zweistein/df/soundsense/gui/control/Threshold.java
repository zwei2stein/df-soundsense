package cz.zweistein.df.soundsense.gui.control;

public enum Threshold {

	EVERYTHING(4l, "Everything"),
	FLUFF(3l, "Messages and enviromental sounds"),
	IMPORTANT(2l, "Important messages"),
	CRITICAL(1l, "Only critical messages"),
	NOTHING(0l, "Nothing");

	private long value;
	private String description;

	Threshold(long value, String descritionn) {
		this.value = value;
		this.description = descritionn;
	}

	public long getValue() {
		return this.value;
	}

	public String getDescription() {
		return this.description;
	}
	
	public static Threshold forValue(long value) {
		
		for(Threshold threshold: Threshold.values()) {
			if (threshold.getValue() == value) {
				return threshold;
			}
		}
		return null;
		
	}

	@Override
	public String toString() {
		return this.description + " (" + this.value + ")";
	}

}
