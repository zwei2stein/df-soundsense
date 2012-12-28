package cz.zweistein.df.soundsense.config;

public class SoundFile {

	private String fileName;
	private int weight; // default weight is 100
	private float volumeAdjustment; // in decibels
	
	private Float balanceAdjustment;
	private boolean randomBalance;
	
	public SoundFile(String fileName, int weight, float volumeAdjustment, Float balanceAdjustment, boolean randomBalance) {
		this.fileName = fileName;
		this.weight = weight;
		this.volumeAdjustment = volumeAdjustment;
		this.balanceAdjustment = balanceAdjustment;
		this.randomBalance = randomBalance;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public float getVolumeAdjustment() {
		return this.volumeAdjustment;
	}

	public int getWeight() {
		return this.weight;
	}

	public Float getBalanceAdjustment() {
		return this.balanceAdjustment;
	}

	public boolean getRandomBalance() {
		return this.randomBalance;
	}

	@Override
	public String toString() {
		return this.getFileName();
	}
	
}
