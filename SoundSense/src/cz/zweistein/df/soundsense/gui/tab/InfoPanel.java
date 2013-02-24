package cz.zweistein.df.soundsense.gui.tab;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cz.zweistein.df.soundsense.SoundSense;
import cz.zweistein.df.soundsense.gui.Browser;
import cz.zweistein.df.soundsense.gui.Icons;

public class InfoPanel extends JPanel {
	private static final long serialVersionUID = 5231590941209603954L;

	public InfoPanel() {
		super();
		
	    GridBagLayout gridbag = new GridBagLayout();
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.CENTER;
	    gridbag.setConstraints(this, constraints);
	    setLayout(gridbag);
	    setBorder(BorderFactory.createLineBorder(Color.white, 10));
	    setBackground(Color.white);
	    
	    JPanel centered = new JPanel();
	    
	    this.add(centered);
	    
	    centered.setLayout(new GridBagLayout());
	    centered.setBackground(Color.white);
		
	    centered.add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LEAF_1))), position(0));
		
		Properties properties = new Properties();
		try {
			properties.load(SoundSense.class.getClassLoader()
					.getResource("version.properties").openStream());
			centered.add(new JLabel(
					"Version: r" + properties.getProperty("release"),
					JLabel.CENTER), position(1));
			centered.add(new JLabel("Build Number: #"
					+ properties.getProperty("buildNum"), JLabel.CENTER), position(2));
			centered.add(new JLabel("Build Date: "
					+ properties.getProperty("buildDate"), JLabel.CENTER), position(3));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		centered.add(new JLabel("Links:", JLabel.CENTER), position(4));

		centered.add(createLink("Wiki", "http://df.magmawiki.com/index.php/Utility:SoundSense"), position(5));
		centered.add(createLink("Forum", "http://www.bay12forums.com/smf/index.php?topic=60287.0"), position(6));
		try {
			centered.add(createLink("Site", "http://df.zweistein.cz/soundsense/?appversion="+URLEncoder.encode(SoundSense.getVersionString(), "UTF-8")), position(7));
		} catch (UnsupportedEncodingException e) {
			centered.add(createLink("Site", "http://df.zweistein.cz/soundsense/?appversion=unknown"), position(7));
		}
		centered.add(createLink("Source Code", "https://code.google.com/p/df-soundsense/"), position(8));
		
		centered.add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LEAF_2))), position(9));

	}
	
	private Component createLink(String label, final String url) {
		JLabel component = new JLabel(label, JLabel.CENTER);
		
		Map<TextAttribute,Object> underlineFont = new Hashtable<TextAttribute,Object>();
		underlineFont.put(TextAttribute.UNDERLINE,TextAttribute.UNDERLINE_ON);
		
		component.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Browser.openURL(url);
			}
		});
		
		component.setFont(component.getFont().deriveFont(underlineFont));
		component.setCursor(new Cursor(Cursor.HAND_CURSOR));
		component.setForeground(Color.BLUE);
		
		return component;
	}

	private GridBagConstraints position(int row) {
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = row;
		
		return c;
	}


}
