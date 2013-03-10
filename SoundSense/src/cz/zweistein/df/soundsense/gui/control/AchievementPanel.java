package cz.zweistein.df.soundsense.gui.control;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cz.zweistein.df.soundsense.config.achievement.Achievement;
import cz.zweistein.df.soundsense.gui.Icons;
import cz.zweistein.df.soundsense.util.log.LoggerSource;

public class AchievementPanel extends JPanel {
	private static final long serialVersionUID = 4106068569413756079L;

	private static Logger logger = LoggerSource.logger;
	
	private int borderWidth = 1;

	private Color borderColor = Color.WHITE;
	private Color backgroundColor = Color.BLACK;

	private Color titleColor = Color.YELLOW;
	private Color descriptionColor = Color.WHITE;
	private Color progressColor = Color.GREEN;

	private Font font = new Font("Fixedsys Excelsior 3.01", Font.PLAIN, 16);

	public AchievementPanel(Achievement achievement, long progress, final JDialog parent, Color bgColor) {
		super(new GridBagLayout());
		
		this.backgroundColor = bgColor;

		try {
			InputStream myStream = new BufferedInputStream(new FileInputStream("./fonts/FSEX300.ttf"));
			Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
			this.font = ttfBase.deriveFont(Font.PLAIN, 16);
		} catch (FileNotFoundException e) {
			logger.severe("Loading font: " + e.toString());
		} catch (FontFormatException e) {
			logger.severe("Loading font: " + e.toString());
		} catch (IOException e) {
			logger.severe("Loading font: " + e.toString());
		}

		if (parent != null) {
			this.setPreferredSize(parent.getPreferredSize());
		}
		this.setBackground(backgroundColor);
		this.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));

		Component image;
		if (achievement.getImage() == null) {
			image = new JPanel();
			image.setPreferredSize(new Dimension(64, 64));
			image.setMinimumSize(image.getPreferredSize());
			image.setBackground(backgroundColor);
		} else {
			image = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(achievement.getImage())));
		}
		this.add(image, new GridBagConstraints(0, 0, 1, 3, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		JLabel achievementTile = new JLabel(achievement.getTitle());
		achievementTile.setToolTipText(achievement.getTitle());
		achievementTile.setForeground(titleColor);
		achievementTile.setFont(font);
		this.add(achievementTile, new GridBagConstraints(1, 0, 1, 1, 1f, 1f, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0));

		if (parent != null) {
			JButton close = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.CLOSE_WINDOW)));
			close.setFocusable(false);
			close.setOpaque(true);
			close.setBackground(backgroundColor);
			close.setBorder(BorderFactory.createEmptyBorder());
			close.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					parent.dispose();
				}
			});
			this.add(close, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}

		JLabel achievementDescription = new JLabel(achievement.getDescription());
		achievementDescription.setToolTipText(achievement.getDescription());
		achievementDescription.setForeground(descriptionColor);
		achievementDescription.setFont(font);
		this.add(achievementDescription, new GridBagConstraints(1, 1, 2, 1, 1f, 1f, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		JLabel achievementProgress = new JLabel(progress + "/" + achievement.getTriggerAmount());
		achievementProgress.setForeground(progressColor);
		achievementProgress.setFont(font);
		this.add(achievementProgress, new GridBagConstraints(1, 2, 2, 1, 1f, 1f, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

	}

}
