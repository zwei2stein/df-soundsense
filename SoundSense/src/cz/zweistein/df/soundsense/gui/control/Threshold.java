package cz.zweistein.df.soundsense.gui.control;

public enum Threshold {

	EVERYTHING(4L, "Everything"),
	FLUFF(3L, "Messages and enviromental sounds"),
	IMPORTANT(2L, "Important messages"),
	CRITICAL(1L, "Only critical messages"),
	NOTHING(0L, "Nothing");

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

		for (Threshold threshold : Threshold.values()) {
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
