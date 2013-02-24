package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cz.zweistein.df.soundsense.config.sounds.Attribution;
import cz.zweistein.df.soundsense.gui.Browser;

public class AttributionInfoPanel extends JPanel {
	private static final long serialVersionUID = 3819628170252365080L;
	
	final JLabel urlLabel;
	final JLabel licenseLabel;
	final JLabel authorLabel;
	final JLabel descriptionLabel;
	final JLabel noteLabel;

	private Attribution attribution;

	public AttributionInfoPanel() {
		super();
		
        this.setLayout(new GridLayout(0,2));

        this.add(new Label("URL:"));
        urlLabel = new JLabel((String)null,  JLabel.CENTER);
        Map<TextAttribute,Object> underlineFont = new Hashtable<TextAttribute,Object>();
        underlineFont.put(TextAttribute.UNDERLINE,TextAttribute.UNDERLINE_ON);
        urlLabel.setFont(urlLabel.getFont().deriveFont(underlineFont));
        urlLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        urlLabel.setForeground(Color.BLUE);
        urlLabel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent me) {
        		Browser.openURL(attribution.getUrl());
        	}
        });
        this.add(urlLabel);
		
        
        this.add(new Label("License:"));
        licenseLabel = new JLabel();
        this.add(licenseLabel);
        
        this.add(new Label("Author"));
        authorLabel = new JLabel();
        this.add(authorLabel);
        
        this.add(new Label("Description:"));
        descriptionLabel = new JLabel();
        this.add(descriptionLabel);
        
        this.add(new Label("Note:"));
        noteLabel = new JLabel();
        this.add(noteLabel);
        
	}
	
	

	public void setAttribution(Attribution attribution) {
		this.attribution = attribution;
		
		urlLabel.setText(attribution.getUrl());
		licenseLabel.setText(attribution.getLicense());
		authorLabel.setText(attribution.getAuthor());
		descriptionLabel.setText(attribution.getDescription());
		noteLabel.setText(attribution.getNote());
	}

}
