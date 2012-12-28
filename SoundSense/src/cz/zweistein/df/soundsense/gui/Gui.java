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
import cz.zweistein.df.soundsense.gui.control.Icons;
import cz.zweistein.df.soundsense.gui.control.InfoPanel;
import cz.zweistein.df.soundsense.gui.control.SoundPackUpdatePanel;
import cz.zweistein.df.soundsense.gui.control.ThreshholdSelector;
import cz.zweistein.df.soundsense.gui.control.VolumeSlider;
import cz.zweistein.df.soundsense.gui.control.configuration.ConfigurationPanel;
import cz.zweistein.df.soundsense.gui.control.configuration.CurrentlyPlayingPanel;
import cz.zweistein.df.soundsense.output.sound.SoundProcesor;

public class Gui extends JPanel {
	private static final long serialVersionUID = 7326794456268877831L;
	
	public Gui(final ConfigurationXML config, final SoundProcesor sp) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
        add(new VolumeSlider(config, sp));
        
        add(new ThreshholdSelector(config, sp));
        
        JTabbedPane tabPane = new JTabbedPane();
        
        add(tabPane);

        JPanel currentlyPlayingPanel = new CurrentlyPlayingPanel(sp);
        tabPane.addTab("Currently Playing", new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.NOT_MUTE)), currentlyPlayingPanel);
        
        JPanel configurationPanel = new ConfigurationPanel(sp); 
        tabPane.addTab("Sounds Configuration", new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.PLAY_ALL)), configurationPanel);
        
        JPanel soundPackUpdatePanel = new SoundPackUpdatePanel(config);
		tabPane.addTab("Soundpack update", new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TRANSFER)), soundPackUpdatePanel);
		
		JPanel infoPanel = new InfoPanel();
		tabPane.addTab("Info", new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.INFO)), infoPanel);

	}
	
    private static void createAndShowGUI(final ConfigurationXML config, SoundProcesor sp) {
        //Create and set up the window.
        JFrame frame = new JFrame("SoundSense "+SoundSense.getVersionString()+" "+config.getGamelogPath());
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
				config.saveConfiguration();
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
        
        Gui gui = new Gui(config, sp);
                
        //Add content to the window.
        frame.add(gui, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true); 
    }

    public static void newGui(final ConfigurationXML config, final SoundProcesor sp) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            createAndShowGUI(config, sp);
	        }
	    });
    }

}
