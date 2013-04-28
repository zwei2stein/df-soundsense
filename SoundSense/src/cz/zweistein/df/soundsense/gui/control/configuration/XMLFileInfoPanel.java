package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.sounds.SoundsXML;

public class XMLFileInfoPanel extends JPanel implements IxmlFileInfoPanel{
	private static final long serialVersionUID = -6987634849512435047L;
	
	private final JLabel pathLabel;
	private final ToggleXMLFileButton toggleXMLFileButton;
	private final ToggleXMLFileGroupButton toggleXMLFileGroupButton;

	public XMLFileInfoPanel(ConfigurationXML configuration, SoundsXML soundsConfiguration, JTree soundsTree) {
		super();
		
        this.setLayout(new GridLayout(0,2));

        this.add(new Label("Path:"));
        pathLabel = new JLabel();
        this.add(pathLabel);
        
        this.add(new Label("Control:"));
        toggleXMLFileButton = new ToggleXMLFileButton(configuration, soundsTree, this);
        this.add(toggleXMLFileButton);
        
        this.add(new JLabel());
        toggleXMLFileGroupButton = new ToggleXMLFileGroupButton(configuration, soundsConfiguration, soundsTree, this);
        this.add(toggleXMLFileGroupButton);
	}

	public void setPath(String path) {
		pathLabel.setText(path);
		
		toggleXMLFileButton.setPath(path);
		toggleXMLFileGroupButton.setPath(path);
	}

}
