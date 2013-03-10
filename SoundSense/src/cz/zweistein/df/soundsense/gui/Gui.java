package cz.zweistein.df.soundsense.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import cz.zweistein.df.soundsense.SoundSense;
import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;
import cz.zweistein.df.soundsense.gui.control.ThreshholdSelector;
import cz.zweistein.df.soundsense.gui.control.VolumeSlider;
import cz.zweistein.df.soundsense.gui.tab.AchievementsPanel;
import cz.zweistein.df.soundsense.gui.tab.ConfigurationPanel;
import cz.zweistein.df.soundsense.gui.tab.CurrentlyPlayingPanel;
import cz.zweistein.df.soundsense.gui.tab.InfoPanel;
import cz.zweistein.df.soundsense.gui.tab.PathsConfigurationPanel;
import cz.zweistein.df.soundsense.gui.tab.SoundPackUpdatePanel;
import cz.zweistein.df.soundsense.output.achievements.AchievementsProcesor;
import cz.zweistein.df.soundsense.output.sound.SoundProcesor;

public class Gui extends JPanel {
	private static final long serialVersionUID = 7326794456268877831L;
	
	public Gui(final ConfigurationXML configurationXML, final SoundProcesor sp, AchievementsXML achievementsXML, AchievementsProcesor ap) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
        add(new VolumeSlider(configurationXML, sp));
        
        add(new ThreshholdSelector(configurationXML, sp));
        
        JTabbedPane tabPane = new JTabbedPane();
        
        add(tabPane);

        JPanel currentlyPlayingPanel = new CurrentlyPlayingPanel(sp);
        tabPane.addTab("Currently Playing", new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.NOT_MUTE)), currentlyPlayingPanel);
        
        JPanel configurationPanel = new ConfigurationPanel(sp); 
        tabPane.addTab("Sounds Configuration", new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.PLAY_ALL)), configurationPanel);
        
        JPanel achievementsPanel = new AchievementsPanel(achievementsXML, configurationXML,  ap);
        tabPane.addTab("Achievements", new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.STAR)), achievementsPanel);

        
        JPanel pathsConfigurationPanel = new PathsConfigurationPanel(configurationXML);
        tabPane.addTab("Logs", new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LOGS)), pathsConfigurationPanel);
        
        JPanel soundPackUpdatePanel = new SoundPackUpdatePanel(configurationXML);
		tabPane.addTab("Pack update", new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TRANSFER)), soundPackUpdatePanel);
		
		JPanel infoPanel = new InfoPanel();
		tabPane.addTab("Info", new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.INFO)), infoPanel);

	}
	
    private static void createAndShowGUI(final ConfigurationXML configurationXML, SoundProcesor sp, final AchievementsXML achievementsXML, AchievementsProcesor ap) {
        //Create and set up the window.
        JFrame frame = new JFrame("SoundSense "+SoundSense.getVersionString()+" "+configurationXML.getGamelogPath());
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Icons.APP_ICON));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				configurationXML.saveConfiguration();
				achievementsXML.save();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}
			

		});
        
        Gui gui = new Gui(configurationXML, sp, achievementsXML, ap);
                
        //Add content to the window.
        frame.add(gui, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true); 
    }

    public static void newGui(final ConfigurationXML config, final SoundProcesor sp, final AchievementsXML achievementsXML, final AchievementsProcesor ap) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            createAndShowGUI(config, sp, achievementsXML, ap);
	        }
	    });
    }

}
