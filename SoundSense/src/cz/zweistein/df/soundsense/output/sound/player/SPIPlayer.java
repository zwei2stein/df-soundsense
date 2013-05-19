package cz.zweistein.df.soundsense.output.sound.player;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class SPIPlayer {
	private static final int BUFFER_SIZE = 4096;

	private static Logger logger = LoggerSource.LOGGER;

	private boolean stop = false;
	private float masterGain = 0;
	private boolean mute = false;
	private float balance = 0;

	private String fileName;

	/**
	 * @param masterGain
	 *            - decibell value of volume amplification.
	 */
	public void setMasterGain(float masterGain) {
		this.masterGain = masterGain;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public void stopPlayback() {
		this.stop = true;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}

	public void play(String fileName) {
		try {
			this.fileName = fileName;
			logger.finest("Opening '" + this.fileName + "'");
			File file = new File(this.fileName);
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			AudioInputStream din = null;
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			// Play now.
			rawPlay(decodedFormat, din);
			in.close();
		} catch (IOException e) {
			logger.severe("Failed to open " + this.fileName + " propably download error or configuration mistake. " + e.getMessage());
		} catch (Exception e) {
			logger.severe("Exception with " + this.fileName + ": " + e + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void rawPlay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
		byte[] data = new byte[BUFFER_SIZE];

		SourceDataLine line = getLine(targetFormat);

		if (line != null) {
			// Start
			line.start();

			if (this.balance != 0) {
				if (line.isControlSupported(FloatControl.Type.BALANCE)) {
					FloatControl masterSampleRate = (FloatControl) line.getControl(FloatControl.Type.BALANCE);
					masterSampleRate.setValue(this.balance);
				} else {
					logger.info("FloatControl.Type.BALANCE not supported for " + this.fileName + "!");
				}
			}

			@SuppressWarnings("unused")
			int nBytesRead = 0, nBytesWritten = 0;
			while (nBytesRead != -1) {
				if (this.stop) {
					logger.finest("Stopping playback.");
					this.stop = false;
					break;
				}
				if (line.isControlSupported(BooleanControl.Type.MUTE)) {
					BooleanControl muteControl = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
					muteControl.setValue(this.mute);
				} else {
					logger.info("BooleanControl.Type.MUTE is not supported by sound system.");
				}
				if (this.masterGain != 0) {
					if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
						FloatControl masterGainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);

						if (this.masterGain < masterGainControl.getMinimum()) {
							this.masterGain = masterGainControl.getMinimum();
						} else if (this.masterGain > masterGainControl.getMaximum()) {
							this.masterGain = masterGainControl.getMaximum();
						}

						masterGainControl.setValue(this.masterGain);
						this.masterGain = 0;
					} else {
						logger.info("FloatControl.Type.MASTER_GAIN is not supported by sound system.");
					}
				}
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1) {
					nBytesWritten = line.write(data, 0, nBytesRead);
				}
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			line = null;
			din.close();
			din = null;
		}
	}

	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

}
