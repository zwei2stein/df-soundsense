package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.GridLayout;
import java.awt.Label;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cz.zweistein.df.soundsense.config.sounds.Sound;
import cz.zweistein.df.soundsense.config.sounds.SoundFile;
import cz.zweistein.df.soundsense.gui.control.Threshold;
import cz.zweistein.df.soundsense.output.sound.player.PlayerManager;

public class SoundFileInfoPanel extends JPanel {
	private static final long serialVersionUID = 3819628170252365080L;
	
	final JLabel soundInfoFilenameLabel;
	final JLabel soundInfoWeightLabel;
	final JLabel soundInfoVolumeLabel;
	final JLabel soundInfoBalanceLabel;
	final StartSoundPlaybackButton soundInfoPlayButton;

	public SoundFileInfoPanel(PlayerManager manager) {
		super();
		
        this.setLayout(new GridLayout(0,2));

        this.add(new Label("Filename:"));
        soundInfoFilenameLabel = new JLabel();
        this.add(soundInfoFilenameLabel);
        
        this.add(new Label("Weight:"));
        soundInfoWeightLabel = new JLabel();
        this.add(soundInfoWeightLabel);
        
        this.add(new Label("Volume adjustment:"));
        soundInfoVolumeLabel = new JLabel();
        this.add(soundInfoVolumeLabel);
        
        this.add(new Label("Balance adjustment:"));
        soundInfoBalanceLabel = new JLabel();
        this.add(soundInfoBalanceLabel);
        
        this.add(new Label("Playback:"));
        soundInfoPlayButton = new StartSoundPlaybackButton(manager);
        this.add(soundInfoPlayButton);
	}

	public void updateSoundFile(SoundFile soundFile) {
		soundInfoFilenameLabel.setText(soundFile.getFileName());
		soundInfoWeightLabel.setText(Integer.toString(soundFile.getWeight()));
		soundInfoVolumeLabel.setText(Float.toString(soundFile.getVolumeAdjustment()));
		
		if (soundFile.getRandomBalance()) {
			soundInfoBalanceLabel.setText("Random");
		} else {
			if (soundFile.getBalanceAdjustment() != null) {
				soundInfoBalanceLabel.setText(Float.toString(soundFile.getBalanceAdjustment()));
			} else {
				soundInfoBalanceLabel.setText("");
			}
		}
		
		Sound dummySound = new Sound(null, Collections.singletonList(soundFile), null, null, null, "manual playback", Long.MAX_VALUE, false, 0L, 0L, null, Threshold.EVERYTHING.getValue());
		
		soundInfoPlayButton.setSound(dummySound);
	}

}
