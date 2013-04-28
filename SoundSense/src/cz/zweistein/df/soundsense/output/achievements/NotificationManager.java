package cz.zweistein.df.soundsense.output.achievements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.awt.AWTUtilities;

import cz.zweistein.df.soundsense.config.achievement.Achievement;
import cz.zweistein.df.soundsense.gui.control.AchievementPanel;

public class NotificationManager {

	private List<JDialog> activeNotifications;

	// settings:
	private int duration = 5000;

	private int borderWidth = 1;

	public NotificationManager() {
		this.activeNotifications = Collections.synchronizedList(new LinkedList<JDialog>());
	}

	private void repositionNotifications() {
		int count = 0;
		synchronized (activeNotifications) {
			for (JDialog dialog : activeNotifications) {
				count++;
				dialog.setLocation(dialog.getX(), Toolkit.getDefaultToolkit().getScreenSize().height - dialog.getHeight() * count);
			}
		}

	}

	public void showNotification(Achievement achievement, long progress) {

		final JDialog dialog = new JDialog();
		dialog.setUndecorated(true);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setBackground(new Color(1f, 1f, 1f, 1f));
		dialog.setAlwaysOnTop(true);
		dialog.setPreferredSize(new Dimension(300, 64 + 2 * borderWidth));

		JPanel content = new AchievementPanel(achievement, progress, dialog, Color.BLACK);
		dialog.add(content);

		dialog.pack();
		dialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - dialog.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().height - dialog.getHeight() * (activeNotifications.size() + 1));
		dialog.setVisible(true);
		activeNotifications.add(dialog);

		new Thread(new Runnable() {

			@Override
			public void run() {

				int tickms = 40; // 25 fps
				double tickDecreement = ((1f / (duration / tickms)));

				float opacity = 1f;

				while (opacity > 0f) {

					AWTUtilities.setWindowOpacity(dialog, opacity);
					opacity -= tickDecreement;
					if (!dialog.isVisible()) {
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
