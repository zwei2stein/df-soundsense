package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTree;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.sounds.SoundsXML;
import cz.zweistein.df.soundsense.gui.Icons;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class ToggleXMLFileGroupButton extends JButton implements ActionListener {
	private static final long serialVersionUID = -7991286287466831023L;
	
	private static Logger logger = LoggerSource.LOGGER;
	
	private ConfigurationXML configuration;
	private SoundsXML soundsConfiguration;
	private String path;
	private JTree soundsTree;
	
	private IxmlFileInfoPanel xmlFileInfoPanel;

	public ToggleXMLFileGroupButton(ConfigurationXML configuration, SoundsXML soundsConfiguration, JTree soundsTree, IxmlFileInfoPanel xmlFileInfoPanel) {
		super();
		this.configuration = configuration;
		this.soundsConfiguration = soundsConfiguration;
		this.soundsTree = soundsTree;
		this.xmlFileInfoPanel = xmlFileInfoPanel;
		addActionListener(this);
	}

	public void setPath(String path) {
		this.path = path;
		if (configuration.getDisabledSounds().contains(path)) {
			setText("Enable group");
			setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.PLAY_ALL)));
		} else {
			setText("Disable group");
			setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.STOP)));
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String pathRoot = path.substring(0, path.lastIndexOf("/"));

		if (configuration.getDisabledSounds().contains(path)) {
			logger.info("Enabling " + path);

			for (String xmlFile : soundsConfiguration.getXMLFiles()) {
				if (xmlFile.startsWith(pathRoot)) {
					logger.info("Enabling " + xmlFile);
					configuration.getDisabledSounds().remove(xmlFile);
				}
			}
		} else {
			logger.info("Disabling " + path);

			for (String xmlFile : soundsConfiguration.getXMLFiles()) {
				if (xmlFile.startsWith(pathRoot)) {
					logger.info("Disabling " + xmlFile);
					configuration.getDisabledSounds().add(xmlFile);
				}
			}
		}
		
		soundsTree.repaint();
		xmlFileInfoPanel.setPath(path);
	}

}
