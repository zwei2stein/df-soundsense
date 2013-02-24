package cz.zweistein.df.soundsense.output.analysis;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import cz.zweistein.df.soundsense.config.sounds.Sound;
import cz.zweistein.df.soundsense.config.sounds.SoundsXML;
import cz.zweistein.df.soundsense.output.Procesor;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class MissingMessagesProcessor extends Procesor {
	private static Logger logger = LoggerSource.logger;
	
	private SoundsXML soundsXML;
	
	public MissingMessagesProcessor(SoundsXML soundsXML) {
		this.soundsXML = soundsXML;
	}

	@Override
	public void processLine(String nextLine) {
		
		if (Pattern.matches("x([0-9]+)", nextLine)) {
			// repeated anouncement "combo points", we ignore those
			return;
		}

		boolean matched = false;
		for (Sound sound: soundsXML.getSounds()) {
			
			if (sound.matches(nextLine)) {
				matched = true;
				sound.hit();
				break;
			}
		}
		if (matched == false) {
			logger.info(nextLine);
		}
	}

}
