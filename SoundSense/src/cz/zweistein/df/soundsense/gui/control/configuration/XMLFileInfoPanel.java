package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import cz.zweistein.df.soundsense.config.ConfigurationXML;

public class XMLFileInfoPanel extends JPanel {
	private static final long serialVersionUID = -6987634849512435047L;
	
	final JLabel pathLabel;
	final ToggleXMLFileButton toggleXMLFileButton;

	public XMLFileInfoPanel(ConfigurationXML configuration, JTree soundsTree) {
		super();
		
        this.setLayout(new GridLayout(0,2));

        this.add(new Label("Path:"));
        pathLabel = new JLabel();
        this.add(pathLabel);
        
        this.add(new Label("Control:"));
        toggleXMLFileButton = new ToggleXMLFileButton(configuration, soundsTree);
        this.add(toggleXMLFileButton);
	}

	public void updatePath(String path) {
		pathLabel.setText(path);
		
		toggleXMLFileButton.setPath(path);
	}

}
