package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTree;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.gui.Icons;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class ToggleXMLFileButton extends JButton implements ActionListener {
	private static final long serialVersionUID = -7991286287466831023L;
	
	private static Logger logger = LoggerSource.logger;
	
	private ConfigurationXML configuration;
	private String path;
	private JTree soundsTree;
	
	private IxmlFileInfoPanel xmlFileInfoPanel;

	public ToggleXMLFileButton(ConfigurationXML configuration, JTree soundsTree, IxmlFileInfoPanel xmlFileInfoPanel) {
		super();
		this.configuration = configuration;
		this.soundsTree = soundsTree;
		this.xmlFileInfoPanel = xmlFileInfoPanel;
		addActionListener(this);
	}

	public void setPath(String path) {
		this.path = path;
		if (configuration.getDisabledSounds().contains(path)) {
			setText("Enable");
			setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.PLAY)));
		} else {
			setText("Disable");
			setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.STOP)));
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (configuration.getDisabledSounds().contains(path)) {
			logger.info("Enabling " + path);
			configuration.getDisabledSounds().remove(path);
		} else {
			logger.info("Disabling " + path);
			configuration.getDisabledSounds().add(path);
		}
		soundsTree.repaint();
		xmlFileInfoPanel.setPath(path);
	}

}
