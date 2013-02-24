package cz.zweistein.df.soundsense.config.sounds;

import java.util.LinkedList;
import java.util.List;


public class SoundFile {

	private String fileName;
	private int weight; // default weight is 100
	private float volumeAdjustment; // in decibels
	
	private Float balanceAdjustment;
	private boolean randomBalance;
	
	private List<Attribution> attributions;
	
	public SoundFile(String fileName, int weight, float volumeAdjustment, Float balanceAdjustment, boolean randomBalance) {
		this.fileName = fileName;
		this.weight = weight;
		this.volumeAdjustment = volumeAdjustment;
		this.balanceAdjustment = balanceAdjustment;
		this.randomBalance = randomBalance;
		
		this.attributions = new LinkedList<Attribution>();
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

	public List<Attribution> getAttributions() {
		return attributions;
	}

	@Override
	public String toString() {
		return this.getFileName();
	}

	
}
