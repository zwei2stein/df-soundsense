package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.sounds.Attribution;
import cz.zweistein.df.soundsense.config.sounds.Sound;
import cz.zweistein.df.soundsense.config.sounds.SoundFile;
import cz.zweistein.df.soundsense.gui.Icons;

public class IconTreeCellRenderer implements TreeCellRenderer {
	
	private ConfigurationXML configuration;

	public IconTreeCellRenderer(ConfigurationXML configuration) {
		this.configuration = configuration;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		DefaultTreeCellRenderer defaultTreeCellRenderer = new DefaultTreeCellRenderer();
		
		defaultTreeCellRenderer.setText(value.toString());
		
		if (value instanceof Sound) {
			
			if (expanded) {
				defaultTreeCellRenderer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TREE_SOUND_EVENT_OPEN)));
			} else {
				defaultTreeCellRenderer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TREE_SOUND_EVENT_CLOSED)));
			}
			
		} else if (value instanceof SoundFile) {
			
			defaultTreeCellRenderer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TREE_SOUND_FILE)));
			
		} else if (value instanceof Attribution) {
			
			defaultTreeCellRenderer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TREE_ATTRIBUTION)));
			
		} else if (value instanceof String) {
			
			if (configuration.getDisabledSounds().contains(value)) {
				if (expanded) {
					defaultTreeCellRenderer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TREE_SOUND_XML_DISABLED_OPEN)));
				} else {
					defaultTreeCellRenderer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TREE_SOUND_XML_DISABLED_CLOSED)));
				}
				defaultTreeCellRenderer.setForeground(Color.RED);
			} else {
				if (expanded) {
					defaultTreeCellRenderer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TREE_SOUND_XML_OPEN)));
				} else {
					defaultTreeCellRenderer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TREE_SOUND_XML_CLOSED)));
				}				
			}
			
		}
		
		if (hasFocus) {
			defaultTreeCellRenderer.setForeground(Color.WHITE);
			defaultTreeCellRenderer.setBackground(Color.DARK_GRAY);
			defaultTreeCellRenderer.setOpaque(true);
		}
		
		return defaultTreeCellRenderer;
	}

}
