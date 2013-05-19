package cz.zweistein.df.soundsense.output.sound.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.sounds.Loop;
import cz.zweistein.df.soundsense.config.sounds.Sound;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public final class PlayerManager {
	private static final Logger logger = LoggerSource.logger;

	private int concurentSounds = 0;
	private float globalChanngelGain = 0;
	private float globalVolume;

	private Map<String, ChannelThread> channels;
	private List<ChangeCallback> channelPlaybackCallback;

	private Set<SFXThread> sfxThreads;
	private List<SFXPlaybackCallback> sfxPlaybackCallback;

	private VolumeAdjuster volumeAdjuster;

	private long playbackTheshhold;

	private ConfigurationXML configurationXML;

	public PlayerManager(ConfigurationXML configurationXML) {
		this.channels = new HashMap<String, ChannelThread>();
		this.channelPlaybackCallback = new ArrayList<ChangeCallback>();

		this.sfxThreads = new HashSet<SFXThread>();
		this.sfxPlaybackCallback = new ArrayList<SFXPlaybackCallback>(1);

		this.volumeAdjuster = new VolumeAdjuster();
		this.volumeAdjuster.setManager(this);
		
		this.configurationXML = configurationXML;
		
		new Thread(this.volumeAdjuster, "VolumeAdjustment").start();
	}

	public Map<String, ChannelThread> getChannels() {
		return this.channels;
	}

	public Set<SFXThread> getSfxThreads() {
		return this.sfxThreads;
	}

	public void playSound(Sound sound) {

		if (sound.hasNoSoundFiles() && sound.getChannel() == null) {
			logger.finest("Sound " + sound + " is ignored because it can not be played");
			return;
		}

		if (this.playbackTheshhold < sound.getPlaybackTheshhold()) {
			logger.fine("Sound " + sound.toString() + " filtered because it's threshold is " + sound.getPlaybackTheshhold() + " while global threshold is "
					+ this.playbackTheshhold + ".");
			return;
		}

		if (sound.getTimeout() != null) {
			if ((sound.getLastPlayed()) < System.currentTimeMillis() - sound.getTimeout()) {
				sound.setLastPlayed(System.currentTimeMillis());
			} else {
				logger.fine("Sound " + sound.toString() + " is still timeouted, will be ready in "
						+ ((sound.getLastPlayed() + sound.getTimeout()) - System.currentTimeMillis()) + ".");
				return;
			}
		}

		if (sound.getPropability() != null) {
			if (new Random().nextInt(100) > sound.getPropability()) {
				logger.fine("Sound " + sound.toString() + " dropped because its propability is " + sound.getPropability() + "% and failed dice roll.");
				return;
			}
		}

		if (sound.getChannel() == null) {
			if (sound.getConcurency() == null || (sound.getConcurency() != null && concurentSounds < sound.getConcurency())) {
				SFXThread mp3Thread = new SFXThread(this, sound, this.globalVolume);
				new Thread(mp3Thread, mp3Thread.getThreadName()).start();
			} else {
				logger.fine("Dropped sound for " + sound.toString() + " its concurency is " + sound.getConcurency() + " and there are " + concurentSounds
						+ " sounds playing.");
			}

		} else {

			ChannelThread channelThread = this.getChannel(sound.getChannel());

			if (sound.getLoop() == Loop.START_LOOPING) {
				channelThread.setLoopMusic(sound);
			} else if (sound.getLoop() == Loop.STOP_LOOPING) {
				this.channelStatusChanged(channelThread);
				channelThread.setLoopMusic(null);
				logger.fine("Stopped looping " + sound.getChannel() + ".");
			}

			if (sound.hasNoSoundFiles()) {
				channelThread.setCurrentMusic(null);
				logger.finest("Stopped playin " + sound.getChannel() + ".");
			} else {
				channelThread.setSingualMusic(sound.getRandomSoundFile(), sound.getDelay());
			}

			if (channelThread.getMusicPlayer() != null) {
				channelThread.getMusicPlayer().stopPlayback();
			}

		}

	}

	private ChannelThread getChannel(String channel) {
		ChannelThread channelThread = this.channels.get(channel);
		if (channelThread == null) {
			channelThread = new ChannelThread(channel, this);
			if (configurationXML.getMutedChannels().contains(channel)) {
				channelThread.setMute(true);
			}
			channelThread.setDefaultGain(this.globalChanngelGain + this.globalVolume);
			this.channels.put(channel, channelThread);
			new Thread(channelThread, "Channel" + channel + "Thread").start();
		}
		return channelThread;
	}

	public synchronized void setGainForAllChannels(float gain) {
		this.globalChanngelGain = gain;
		for (Map.Entry<String, ChannelThread> entry : this.channels.entrySet()) {
			entry.getValue().setDefaultGain(gain + this.globalVolume);
		}
	}

	public synchronized void setVolumeForAllSfx(float volume) {
		for (SFXThread sfx : this.sfxThreads) {
			sfx.setVolume(volume);
		}
	}

	public synchronized void mp3ThreadPlayingStartCallback(SFXThread mp3Thread) {
		this.sfxThreads.add(mp3Thread);
		concurentSounds++;
		this.volumeAdjuster.setTargetGain(-15);
		for (SFXPlaybackCallback callback : this.sfxPlaybackCallback) {
			callback.started();
		}
	}

	public synchronized void mp3ThreadPlayingEndCallback(SFXThread mp3Thread) {
		this.sfxThreads.remove(mp3Thread);
		concurentSounds--;
		if (this.concurentSounds == 0) {
			this.volumeAdjuster.setTargetGain(-5);
		}
		for (SFXPlaybackCallback callback : this.sfxPlaybackCallback) {
			callback.ended();
		}
	}

	public void addSfxPlaybackCallback(SFXPlaybackCallback callback) {
		this.sfxPlaybackCallback.add(callback);
	}

	public void removeSfxPlaybackCallback(SFXPlaybackCallback callback) {
		this.sfxPlaybackCallback.remove(callback);
	}

	public void setGlobalVolume(float globalVolume) {
		this.globalVolume = globalVolume;
		this.setGainForAllChannels(this.globalChanngelGain);
		this.setVolumeForAllSfx(globalVolume);
	}

	public void addChannelPlaybackCallback(ChangeCallback channelListAdapter) {
		this.channelPlaybackCallback.add(channelListAdapter);
	}

	public synchronized void removeChannelPlaybackCallback(ChangeCallback channelListAdapter) {
		this.channelPlaybackCallback.remove(channelListAdapter);
	}

	public void channelStatusChanged(ChannelThread channelThread) {
		for (ChangeCallback channelListAdapter : this.channelPlaybackCallback) {
			channelListAdapter.changed();
		}
	}

	public void setPlaybackTheshhold(long playbackTheshhold) {
		this.playbackTheshhold = playbackTheshhold;
	}

	public int getConcurentSounds() {
		return this.concurentSounds;
	}

}
