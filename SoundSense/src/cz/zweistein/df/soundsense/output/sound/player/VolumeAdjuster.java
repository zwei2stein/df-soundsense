package cz.zweistein.df.soundsense.output.sound.player;

public class VolumeAdjuster implements Runnable {

	private float currentGain; // decibels
	private float targetGain; // decibels

	private int adjustmentInterval = 100; // miliseconds

	private PlayerManager manager;

	public void setTargetGain(float targetGain) {
		this.targetGain = targetGain;
	}

	public void setManager(PlayerManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {

		while (true) {

			if (currentGain < targetGain) {
				currentGain++;
				manager.setGainForAllChannels(currentGain);
			} else if (currentGain > targetGain) {
				currentGain--;
				manager.setGainForAllChannels(currentGain);
			}

			if ((Math.abs(targetGain - currentGain) > 0) && (Math.abs(targetGain - currentGain) < 1)) {
				targetGain = currentGain;
				manager.setGainForAllChannels(currentGain);
			}

			try {
				Thread.sleep(this.adjustmentInterval);
			} catch (InterruptedException e) {
			}
		}

	}

}
