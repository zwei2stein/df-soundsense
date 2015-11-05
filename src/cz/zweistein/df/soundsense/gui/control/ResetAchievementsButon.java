package cz.zweistein.df.soundsense.gui.control;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import cz.zweistein.df.soundsense.config.achievement.AchievementPattern;
import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;
import cz.zweistein.df.soundsense.gui.Icons;

/**
 * 
 * Resets achievement progress.
 * 
 * @author Petr Prokop
 * 
 */
public class ResetAchievementsButon extends JButton implements ActionListener {
	private static final long serialVersionUID = 2649465718493256210L;

	private AchievementsXML achievementsXML;

	public ResetAchievementsButon(AchievementsXML achievementsXML) {
		super();
		this.achievementsXML = achievementsXML;
		addActionListener(this);
		setText("Reset");
		setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.BOMB)));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		int confirmation = JOptionPane.showConfirmDialog(this, "All achievement prgoress will be reset. Continue?", "SoundSense", JOptionPane.OK_CANCEL_OPTION);

		if (confirmation == JOptionPane.OK_OPTION) {
			for (AchievementPattern ap : achievementsXML.getAchievementPatterns()) {
				ap.setHits(0);
			}
		}
	}

}
