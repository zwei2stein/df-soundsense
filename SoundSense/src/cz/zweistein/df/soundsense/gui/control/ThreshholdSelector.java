package cz.zweistein.df.soundsense.gui.control;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.output.sound.SoundProcesor;

public class ThreshholdSelector extends JPanel implements ActionListener {
	private static final long serialVersionUID = -1807076983444937745L;
	
	private SoundProcesor sp;
	private ConfigurationXML config;

	public ThreshholdSelector(final ConfigurationXML config, final SoundProcesor sp) {
		super();
		
		this.sp = sp;
		this.config = config;

		setLayout(new BorderLayout());
		
        add(new JLabel("Play:"), BorderLayout.LINE_START);

        JComboBox<Threshold> threshholdSelector = new JComboBox<Threshold>(new DefaultComboBoxModel<Threshold>(Threshold.values()));
        threshholdSelector.addActionListener(this);
        threshholdSelector.setSelectedItem(Threshold.forValue(config.getPlaybackTheshhold()));
        
        add(threshholdSelector, BorderLayout.CENTER);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<Threshold> cb = (JComboBox<Threshold>)e.getSource();
		
		this.config.setPlaybackTheshhold(((Threshold) cb.getSelectedItem()).getValue());
		this.sp.setPlaybackTheshhold(((Threshold) cb.getSelectedItem()).getValue());
		
	}

}
