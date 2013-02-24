package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import cz.zweistein.df.soundsense.config.sounds.Sound;
import cz.zweistein.df.soundsense.gui.Icons;
import cz.zweistein.df.soundsense.output.sound.player.PlayerManager;

public class StartSoundPlaybackButton extends JButton implements ActionListener {
	private static final long serialVersionUID = 6661022698071626453L;
	
	private Sound sound;
	private transient PlayerManager manager;

	public StartSoundPlaybackButton(PlayerManager manager) {
		super();
		this.manager = manager;
		addActionListener(this);
		setText("Start");
		setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.PLAY)));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.manager.playSound(this.sound);
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

}
