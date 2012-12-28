package cz.zweistein.df.soundsense.gui.control;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cz.zweistein.df.soundsense.SoundSense;

public class InfoPanel extends JPanel {
	private static final long serialVersionUID = 5231590941209603954L;

	public InfoPanel() {
		super();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Properties properties = new Properties();
		try {
			properties.load(SoundSense.class.getClassLoader()
					.getResource("version.properties").openStream());
			this.add(new JLabel(
					"Version: r" + properties.getProperty("release"),
					JLabel.CENTER));
			this.add(new JLabel("Build Number: #"
					+ properties.getProperty("buildNum"), JLabel.CENTER));
			this.add(new JLabel("Build Date: "
					+ properties.getProperty("buildDate"), JLabel.CENTER));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.add(new JLabel("Links:", JLabel.CENTER));

		this.add(createLink("Wiki", "http://df.magmawiki.com/index.php/Utility:SoundSense"));
		this.add(createLink("Forum", "http://www.bay12forums.com/smf/index.php?topic=60287.0"));
		try {
			this.add(createLink("Site", "http://df.zweistein.cz/soundsense/?appversion="+URLEncoder.encode(SoundSense.getVersionString(), "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			this.add(createLink("Site", "http://df.zweistein.cz/soundsense/?appversion=unknown"));
		}
		
		Dimension minSize = new Dimension(0, 0);
		Dimension prefSize = new Dimension(0, 500);
		Dimension maxSize = new Dimension(0, Short.MAX_VALUE);
		this.add(new Box.Filler(minSize, prefSize, maxSize));

	}
	
	private Component createLink(String label, final String url) {
		JLabel component = new JLabel(label, JLabel.CENTER);
		
		Map<TextAttribute,Object> underlineFont = new Hashtable<TextAttribute,Object>();
		underlineFont.put(TextAttribute.UNDERLINE,TextAttribute.UNDERLINE_ON);
		
		component.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				openURL(url);
			}
		});
		
		component.setFont(component.getFont().deriveFont(underlineFont));
		component.setCursor(new Cursor(Cursor.HAND_CURSOR));
		component.setForeground(Color.BLUE);
		
		return component;
	}

	private void openURL(String url) {

		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

		if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {

			throw new UnsupportedOperationException("Desktop doesn't support the browse action");
		}
		
		try {

			java.net.URI uri = new java.net.URI(url);
			desktop.browse(uri);
			
		} catch (Exception e) {

			throw new UnsupportedOperationException(e.getMessage());
		}

	}

}
