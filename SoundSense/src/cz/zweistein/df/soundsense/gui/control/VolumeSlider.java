package cz.zweistein.df.soundsense.gui.control;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.output.sound.SoundProcesor;

public class VolumeSlider extends JPanel implements ChangeListener {
	private static final long serialVersionUID = -1807076983444937745L;
	
	private SoundProcesor sp;
	private JLabel volumeLabel;
	private ConfigurationXML config;

	public VolumeSlider(final ConfigurationXML config, final SoundProcesor sp) {
		super();
		
		this.sp = sp;
		this.config = config;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
        volumeLabel = new JLabel("Volume: " + (int) config.getVolume(), JLabel.CENTER);
        volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(volumeLabel);

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, -40, 6, (int) config.getVolume());
        volumeSlider.addChangeListener(this);
        
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(5);
        
        add(volumeSlider);
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		sp.setGlobalVolume(((JSlider) e.getSource()).getValue());
		volumeLabel.setText("Volume: "+((JSlider) e.getSource()).getValue());
		config.setVolume(((JSlider) e.getSource()).getValue());
	}

}
