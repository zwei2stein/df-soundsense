package cz.zweistein.df.soundsense.output.sound.player;

import java.util.logging.Logger;

import cz.zweistein.df.soundsense.config.sounds.Sound;
import cz.zweistein.df.soundsense.config.sounds.SoundFile;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class SFXThread implements Runnable {
	private static Logger logger = LoggerSource.LOGGER;

	private PlayerManager managerCallback;
	private float volume;
	private SoundFile soundFile;
	private SPIPlayer player;
	private Long delay;

	private Long xPos;
	private Long yPos;
	private Long zPos;

	public SFXThread(PlayerManager manager, Sound sound, float volume, Long xPos, Long yPos, Long zPos) {
		this.managerCallback = manager;
		this.soundFile = sound.getRandomSoundFile();
		this.volume = volume;
		this.delay = sound.getDelay();
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		if (this.player != null) {
			this.player.setMasterGain(volume + soundFile.getVolumeAdjustment());
		}
	}

	@Override
	public void run() {
		managerCallback.mp3ThreadPlayingStartCallback(this);
		try {

			if (delay != null) {
				logger.finest("Delaying playback by " + delay + " ms.");
				Thread.sleep(delay);
			}

			logger.finest("Concurent sounds: " + managerCallback.getConcurentSounds());
			this.player = new SPIPlayer();

			float distanceVolume = 1f;
			if (soundFile.getPositionalBallance() && xPos != null && yPos != null && zPos != null) {
				double dist = Math.sqrt(xPos * xPos + yPos * yPos + zPos * zPos);
				if (dist > 80) {
					distanceVolume = 0.1f;
				} else if (dist > 0) {
					distanceVolume = 1 * (0.9f * (float) dist / 80f);
				}
			}

			player.setMasterGain(distanceVolume * (volume + soundFile.getVolumeAdjustment()));

			if (soundFile.getPositionalBallance() && xPos != null) {
				float balance = 0;
				if (xPos <= -80) {
					balance = -1;
				} else if (xPos >= 80) {
					balance = 1;
				} else {
					balance = xPos.floatValue() / 80f;
				}
				player.setBalance(balance);
			} else if (soundFile.getRandomBalance()) {
				player.setBalance(((float) Math.random()) * 2 - 1);
			} else {
				if (soundFile.getBalanceAdjustment() != null) {
					player.setBalance(soundFile.getBalanceAdjustment());
				}
			}

			logger.fine("Playing " + soundFile.getFileName());
			player.play(soundFile.getFileName());
			logger.fine("Finished " + soundFile.getFileName());

		} catch (Exception e) {
			logger.severe("Exception :" + e + ": " + e.toString());
		} finally {
			managerCallback.mp3ThreadPlayingEndCallback(this);
		}
	}

	@Override
	public String toString() {
		return this.soundFile.getFileName();
	}

	public String getThreadName() {
		return "Sfx" + soundFile.getFileName().substring(soundFile.getFileName().length() - 14, soundFile.getFileName().length() - 4) + "_"
				+ soundFile.getFileName().hashCode() + "Thread";
	}

}
