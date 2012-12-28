package cz.zweistein.df.soundsense.output.sound;

import java.util.logging.Logger;

import cz.zweistein.df.soundsense.config.Sound;
import cz.zweistein.df.soundsense.config.SoundsXML;
import cz.zweistein.df.soundsense.output.Procesor;
import cz.zweistein.df.soundsense.output.sound.player.PlayerManager;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class SoundProcesor extends Procesor {
	private static Logger logger = LoggerSource.logger;
	
	private SoundsXML soundsXML;
	private PlayerManager player;
	
	public SoundProcesor(SoundsXML soundsXML, long playbackTheshhold) {
		this.soundsXML = soundsXML;
		this.player = new PlayerManager();
		this.player.setPlaybackTheshhold(playbackTheshhold);
	}
	
	public SoundsXML getSoundsXML() {
		return this.soundsXML;
	}
	
	public void setGlobalVolume(float globalVolume) {
		this.player.setGlobalVolume(globalVolume);
	}

	@Override
	public void processLine(String nextLine) {
		
		int matches = 0;
		Sound matchedSound = null;
		for (Sound sound: soundsXML.getSounds()) {
			
			if (sound.matches(nextLine)) {
				matches++;
				matchedSound = sound;
				
				logger.fine("Message '"+nextLine+"' matched event '"+sound.toString()+"' from '"+sound.getParentFile()+"'.");
				
				player.playSound(sound);

				if (sound.getHaltOnMatch() == true) {
					logger.finest("Ending matching prematurely as expected.");
					break;
				} else {
					logger.fine("Continuing for next rule match.");
				}
			}
		}
		if (matchedSound == null) {
			logger.info(nextLine);
			logger.fine("Message '"+nextLine+"' did not match any rule.");
		} else {
			logger.info((matchedSound.getAnsiFormat()!= null ? matchedSound.getAnsiFormat() : "") + nextLine);
			logger.finest("Message '"+nextLine+"' matched " + matches + " rules.");
		}
	}

	public PlayerManager getPlayerManager() {
		return this.player;
	}

	public void setPlaybackTheshhold(long playbackTheshhold) {
		this.player.setPlaybackTheshhold(playbackTheshhold);
	}

}
