package cz.zweistein.df.soundsense.output.achievements;

import java.awt.Color;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.awt.AWTUtilities;

import cz.zweistein.df.soundsense.gui.Icons;


public class NotificationManager {
	
	private List<JDialog> activeNotifications;
	
	//settings:
	private int duration = 5000;

	private int borderWidth = 1;
	private Color borderColor = Color.WHITE;
	private Color backgroundColor = Color.BLACK;
	
	private Color titleColor = Color.YELLOW;
	private Color descriptionColor = Color.WHITE;
	private Color progressColor = Color.GREEN;
	
	private Font font = new Font("Fixedsys Excelsior 3.01", Font.PLAIN, 16);
	
	
	public NotificationManager() {
		this.activeNotifications = Collections.synchronizedList(new LinkedList<JDialog>());
		

		try {
			InputStream myStream = new BufferedInputStream(new FileInputStream("./fonts/FSEX300.ttf"));
			Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
			this.font = ttfBase.deriveFont(Font.PLAIN, 16);
		} catch (FileNotFoundException e) {
		} catch (FontFormatException e) {
		} catch (IOException e) {
		}

	}
	
	private void repositionNotifications() {
		int count = 0;
		synchronized(activeNotifications) {
			for (JDialog dialog : activeNotifications) {
				count++;
				dialog.setLocation(dialog.getX(), Toolkit.getDefaultToolkit().getScreenSize().height - dialog.getHeight() * count);
			}
		}
		
	}
	
	public void showNotification(String imageFilename, String title, String description, String progress) {
		
        final JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setBackground(new Color(1f, 1f, 1f, 1f));
        dialog.setAlwaysOnTop(true);
        dialog.setPreferredSize(new Dimension(300, 64 + 2*borderWidth));
        
        JPanel content = new JPanel(new GridBagLayout());
        content.setPreferredSize(dialog.getPreferredSize());
        content.setBackground(backgroundColor);
        content.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
        
        JLabel image;
        if (imageFilename == null) {
        	image = new JLabel();
        	image.setPreferredSize(new Dimension(64, 64));
        } else {
        	image = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageFilename)));
        }
		content.add(image, new GridBagConstraints(0, 0, 1, 3, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        
        JLabel achievementTile = new JLabel(title);
        achievementTile.setForeground(titleColor);
        achievementTile.setFont(font);
        content.add(achievementTile, new GridBagConstraints(1, 0, 1, 1, 1f, 1f, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0));
        
        JButton close = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.CLOSE_WINDOW)));
        close.setFocusable(false);
        close.setOpaque(true);
        close.setBackground(backgroundColor);
        close.setBorder(BorderFactory.createEmptyBorder());
        close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
        content.add(close, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        
        JLabel achievementDescription = new JLabel(description);
        achievementDescription.setForeground(descriptionColor);
        achievementDescription.setFont(font);
        content.add(achievementDescription, new GridBagConstraints(1, 1, 2, 1, 1f, 1f, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        JLabel achievementProgress = new JLabel(progress);
        achievementProgress.setForeground(progressColor);
        achievementProgress.setFont(font);
        content.add(achievementProgress, new GridBagConstraints(1, 2, 2, 1, 1f, 1f, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
        
		dialog.add(content);

		dialog.pack();
		dialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - dialog.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().height - dialog.getHeight() * (activeNotifications.size()+1));
        dialog.setVisible(true);
        activeNotifications.add(dialog);
     
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				int tickms = 40; // 25 fps
				double tickDecreement = ((1f/(duration/tickms)));
				
				float opacity = 1f;
				
				while(opacity > 0f) {
			
					AWTUtilities.setWindowOpacity(dialog, opacity);
					opacity -= tickDecreement;
					if (dialog.isVisible() == false) {
						opacity = 0f;
					}
			        
			        try {
						Thread.sleep(tickms);
					} catch (InterruptedException e) {
						break;
					}
					
				}
				
				dialog.dispose();
				activeNotifications.remove(dialog);
				repositionNotifications();
				
			}
		}).start();
		
	}

}
