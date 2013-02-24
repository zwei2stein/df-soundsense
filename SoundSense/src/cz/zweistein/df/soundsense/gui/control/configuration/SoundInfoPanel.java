package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cz.zweistein.df.soundsense.config.sounds.Sound;
import cz.zweistein.df.soundsense.gui.control.Threshold;
import cz.zweistein.df.soundsense.output.sound.player.PlayerManager;

public class SoundInfoPanel extends JPanel {
	private static final long serialVersionUID = -6987634849512435047L;
	
	final JLabel soundInfoPatternLabel;
	final JLabel soundInfoChannelLabel;
	final JLabel soundInfoLoopLabel;
	final JLabel soundInfoTimeoutLabel;
	final JLabel soundInfoConcurencyLabel;
	final JLabel soundInfoDelayLabel;
	final StartSoundPlaybackButton soundInfoPlayButton;

	private JLabel soundInfoPropabilityLabel;

	private JLabel soundInfoplaybackTheshholdLabel;

	public SoundInfoPanel(PlayerManager manager) {
		super();
		
        this.setLayout(new GridLayout(0,2));

        this.add(new Label("Pattern:"));
        soundInfoPatternLabel = new JLabel();
        this.add(soundInfoPatternLabel);
        
        this.add(new Label("Channel:"));
        soundInfoChannelLabel = new JLabel();
        this.add(soundInfoChannelLabel);
        
        this.add(new Label("Loop:"));
        soundInfoLoopLabel = new JLabel();
        this.add(soundInfoLoopLabel);
        
        this.add(new Label("Timeout:"));
        soundInfoTimeoutLabel = new JLabel();
        this.add(soundInfoTimeoutLabel);
        
        this.add(new Label("Concurency:"));
        soundInfoConcurencyLabel = new JLabel();
        this.add(soundInfoConcurencyLabel);
        
        this.add(new Label("Delay:"));
        soundInfoDelayLabel = new JLabel();
        this.add(soundInfoDelayLabel);
        
        this.add(new Label("Propability:"));
        soundInfoPropabilityLabel = new JLabel();
        this.add(soundInfoPropabilityLabel);
        
        this.add(new Label("Playback threshhold:"));
        soundInfoplaybackTheshholdLabel = new JLabel();
        this.add(soundInfoplaybackTheshholdLabel);
        
        this.add(new Label("Playback:"));
        soundInfoPlayButton = new StartSoundPlaybackButton(manager);
        this.add(soundInfoPlayButton);
	}

	public void updateSound(Sound sound) {
		soundInfoPatternLabel.setText(sound.getLogPattern());
		soundInfoChannelLabel.setText(sound.getChannel());
		soundInfoLoopLabel.setText(sound.getLoop()==null?"":sound.getLoop().toString());
		soundInfoTimeoutLabel.setText(sound.getTimeout()==null?"no timeout":sound.getTimeout()+"ms");
		soundInfoConcurencyLabel.setText(sound.getConcurency()==null?"disregards concurency":sound.getConcurency().toString());
		soundInfoDelayLabel.setText(sound.getDelay()==null?"no delay":sound.getDelay()+"ms");
		soundInfoPropabilityLabel.setText(sound.getPropability()==null?"always plays":sound.getPropability()+"%");
		soundInfoplaybackTheshholdLabel.setText(Threshold.forValue(sound.getPlaybackTheshhold()).toString());
		soundInfoPlayButton.setSound(sound);
	}

}
