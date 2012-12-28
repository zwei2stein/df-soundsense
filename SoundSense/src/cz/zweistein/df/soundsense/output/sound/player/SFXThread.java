package cz.zweistein.df.soundsense.output.sound.player;

import java.util.logging.Logger;

import cz.zweistein.df.soundsense.config.Sound;
import cz.zweistein.df.soundsense.config.SoundFile;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class SFXThread implements Runnable {
	private static Logger logger = LoggerSource.logger;
	
	private PlayerManager managerCallback;
	private float volume;
	private SoundFile soundFile;
	private SPIPlayer player;
	private Long delay;
	
	public SFXThread(PlayerManager manager, Sound sound, float volume) {
		this.managerCallback = manager;
		this.soundFile = sound.getRandomSoundFile();
		this.volume = volume;
		this.delay = sound.getDelay();
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
		if (this.player != null) {
			this.player.setMasterGain(volume+soundFile.getVolumeAdjustment());
		}
	}

	@Override
	public void run() {
		managerCallback.mp3ThreadPlayingStartCallback(this);
		try {
			
			if (delay != null ) {
				logger.finest("Delaying playback by " + delay + " ms.");
				Thread.sleep(delay);
			}
			
			logger.finest("Concurent sounds: "+managerCallback.concurentSounds);
			this.player = new SPIPlayer();
			player.setMasterGain(volume+soundFile.getVolumeAdjustment());
			if (soundFile.getRandomBalance() == true) {
				player.setBalance(((float)Math.random())*2-1);
			} else {
				if (soundFile.getBalanceAdjustment() != null) {
					player.setBalance(soundFile.getBalanceAdjustment());
				}
			}
			
			logger.fine("Playing "+soundFile.getFileName());
			player.play(soundFile.getFileName());
			logger.fine("Finished "+soundFile.getFileName());
			
		} catch (Exception e) {
			logger.severe("Exception :"+e+": "+e.toString());
		} finally {
			managerCallback.mp3ThreadPlayingEndCallback(this);
		}
	}
	
	@Override
	public String toString() {
		return this.soundFile.getFileName();
	}
	
	public String getThreadName() {
		return "Sfx"+ soundFile.getFileName().substring(soundFile.getFileName().length()-14, soundFile.getFileName().length()-4)+"_"+soundFile.getFileName().hashCode()+"Thread";
	}

}
