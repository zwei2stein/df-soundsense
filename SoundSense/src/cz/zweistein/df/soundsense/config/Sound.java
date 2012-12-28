package cz.zweistein.df.soundsense.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sound {

	private List<SoundFile> soundFiles;

	private int soundFilesWeightsSum;
	
	private String logPattern;
	private Pattern parsedLogPattern;
	
	private String parentFile;
	
	// loop repeats indefinitely, only music can loop
	private Loop loop;
	// there can be only one sound played on channel.
	private String channel;
	
	// concurency is cap on how many sounds can be played concurenctly to this one.
	// if more sounds are playing than this number and this sound is triggered, it would be ignored.
	private Long concurency; 
	
	private boolean haltOnMatch;
	
	private long hits;
	
	// timeout in milliseconds, sound would not be repeated within this time. 
	private Long timeout;
	private long lastPlayed;
	
	private Long delay;
	
	// in percents
	private Long propability;
	
	private long playbackTheshhold;
	
	private String ansiFormat;

	public Sound(String parentFile, List<SoundFile> soundFiles, String logPattern, String ansiFormat, Loop loop, String channel, Long concurency, boolean haltOnMatch, Long timeout, Long delay, Long propability, long playbackTheshhold) {
		this.parentFile = parentFile;
		
		if (soundFiles != null) {
			this.soundFiles = soundFiles;
		} else {
			this.soundFiles = new ArrayList<SoundFile>(0);
		}
		this.logPattern = logPattern;
		this.loop = loop;
		this.channel = channel;
		this.concurency = concurency;
		this.haltOnMatch = haltOnMatch;
		this.timeout = timeout;
		this.delay = delay;
		this.propability = propability;
		this.playbackTheshhold = playbackTheshhold;
		
		this.soundFilesWeightsSum = 0;
		
		for (SoundFile soundFile: this.soundFiles) {
			this.soundFilesWeightsSum += soundFile.getWeight();
		}
		
		this.hits = 0;
		
		if (this.logPattern != null) {
			this.parsedLogPattern = Pattern.compile(this.logPattern);
		}
		
		this.ansiFormat = ansiFormat;
		
	}
	
	public List<SoundFile> getSoundFiles() {
		return this.soundFiles;
	}

	public Long getConcurency() {
		return this.concurency;
	}

	public String getLogPattern() {
		return this.logPattern;
	}
	
	public Loop getLoop() {
		return this.loop;
	}
	
	public boolean hasNoSoundFiles() {
		return this.soundFiles.isEmpty();
	}

	public SoundFile getRandomSoundFile() {
		
		if (this.soundFiles.size() > 0) {
			int weightedRandom = new Random().nextInt(this.soundFilesWeightsSum);
			for (SoundFile soundFile: this.soundFiles) {
				if (soundFile.getWeight() > weightedRandom) {
					return soundFile;
				}
				weightedRandom -= soundFile.getWeight();
			}
		}
	
		return null;
		
	}

	public String getParentFile() {
		return this.parentFile;
	}

	public String getChannel() {
		return channel;
	}

	public boolean getHaltOnMatch() {
		return this.haltOnMatch;
	}
	
	public long getHits() {
		return this.hits;
	}

	public void hit() {
		this.hits++;
	}

	public long getLastPlayed() {
		return this.lastPlayed;
	}

	public void setLastPlayed(long lastPlayed) {
		this.lastPlayed = lastPlayed;
	}

	public Long getTimeout() {
		return this.timeout;
	}
	
	public Long getDelay() {
		return delay;
	}
	
	public void setPropability(Long propability) {
		this.propability = propability;
	}
	
	public Long getPropability() {
		return propability;
	}

	@Override
	public String toString() {
		return this.getLogPattern();
	}

	public long getPlaybackTheshhold() {
		return playbackTheshhold;
	}
	
	public boolean matches(String logEvent) {
		Matcher matcher = this.parsedLogPattern.matcher(logEvent);
		return matcher.matches();
	}

	public String getAnsiFormat() {
		return ansiFormat;
	}

}
