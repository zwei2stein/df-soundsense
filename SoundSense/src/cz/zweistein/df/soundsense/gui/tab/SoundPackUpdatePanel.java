package cz.zweistein.df.soundsense.gui.tab;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.zweistein.autoupdater.AutoUpdater;
import cz.zweistein.autoupdater.callback.IControllCallback;
import cz.zweistein.autoupdater.callback.IProgressCallback;
import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.gui.Icons;
import cz.zweistein.df.soundsense.gui.control.ProgressTicker;

public class SoundPackUpdatePanel extends JPanel implements ActionListener, IProgressCallback, IControllCallback, ChangeListener {
	private static final long serialVersionUID = 5231590941209603954L;
	
	private ConfigurationXML config;
	private DefaultListModel<String> logModel;
	private JButton startButton;
	private JList<String> log;
	private JLabel downloadLabel;
	private JProgressBar downloadProgressBar;
	private JLabel totalProgressLabel;
	private JProgressBar totalProgressBar;
	private JLabel etaLabel;
	private ProgressTicker progressTicker;
	
	private JCheckBox deleteCheckbox;
	private JCheckBox replaceCheckbox;
	
	private long bytesPerSeccond;
	private long bytesLeft;

	public SoundPackUpdatePanel(ConfigurationXML config) {
		super();
		
		setLayout(new BorderLayout());
		
		this.config = config;
		
		this.progressTicker = new ProgressTicker();
		
		JButton startButton = new JButton("Start automatic update");
		startButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.TRANSFER)));
		startButton.addActionListener(this);
		this.startButton = startButton;
		
		JPanel headerPanel = new JPanel();
		headerPanel.add(this.progressTicker);
		headerPanel.add(this.startButton);
		
		this.deleteCheckbox = new JCheckBox("Delete existing files", this.config.getDeleteFiles());
		this.deleteCheckbox.addChangeListener(this);
		headerPanel.add(deleteCheckbox);
		
		this.replaceCheckbox = new JCheckBox("Replace existing files", this.config.getReplaceFiles());
		this.replaceCheckbox.addChangeListener(this);
		headerPanel.add(replaceCheckbox);
		
		this.downloadLabel = new JLabel("0 / 0 kb - file ?");
		this.downloadLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		this.downloadProgressBar = new JProgressBar(0, 100);
		this.downloadProgressBar.setStringPainted(true);
		
		this.totalProgressLabel = new JLabel("0 kb / 0 kb ok - total to update is ? kb");
		this.totalProgressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		this.etaLabel = new JLabel("0 kb/s - 00:00:00 left");
		
		this.totalProgressBar = new JProgressBar(0, 100);
		this.totalProgressBar.setStringPainted(true);

		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.PAGE_AXIS));
		
		footerPanel.add(downloadLabel);
		footerPanel.add(downloadProgressBar);
		footerPanel.add(totalProgressLabel);
		footerPanel.add(etaLabel);
		footerPanel.add(totalProgressBar);
		
		this.log = new JList<String>();
		this.logModel = new DefaultListModel<String>();
		log.setModel(this.logModel);
		this.add(new JScrollPane(log));
		
		this.add(headerPanel, BorderLayout.PAGE_START);
		this.add(new JScrollPane(log), BorderLayout.CENTER);
		this.add(footerPanel, BorderLayout.PAGE_END);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		startButton.setEnabled(false);
		logModel.clear();
		final AutoUpdater updater = new AutoUpdater(config.getSoundpacksPath(), config.getAutoUpdateURLs());
		updater.registerProgressCallback(this);
		updater.registerControllCallback(this);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				updater.start();
			}
			
		}).start();

	}
	
	private void logMessage(final String message) {
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				logModel.addElement(message);
				log.setSelectedIndex(logModel.getSize()-1);
				log.ensureIndexIsVisible(logModel.getSize()-1);
			}
		});
		
	}

	@Override
	public void changeFound(String arg0) {
		logMessage("Changed: "+arg0);
	}

	@Override
	public void deleted(String arg0) {
		logMessage("Deleted: "+arg0);
	}

	@Override
	public void done() {
		logMessage("Done, restart SoundSense.");
		startButton.setEnabled(true);
	}

	@Override
	public void error(String arg0) {
		logMessage("Error: "+arg0);
		startButton.setEnabled(true);
	}

	@Override
	public void localParseDone() {
		logMessage("Done scanning "+config.getSoundpacksPath());
	}

	@Override
	public void localParseStart() {
		logMessage("Scanning "+config.getSoundpacksPath());
	}

	@Override
	public void newFound(String arg0) {
		logMessage("New: "+arg0);
	}

	@Override
	public void downloadProgress(Long progress, Long total, String url) {
		
		String text; 
		if (total == null) {
			text = progress/1024+" kb - file "+url;
			
			this.downloadProgressBar.setValue(0);
			this.downloadProgressBar.setString("0%");
			
		} else {
			text = progress/1024+" / "+total/1024+" kb - file "+url;
			
			this.downloadProgressBar.setValue((int)((progress*100)/total));
			this.downloadProgressBar.setString(((progress*100)/total)+"%");
			
		}
		
		this.downloadLabel.setText(text);
	}

	@Override
	public void totalProgress(Long localSize, Long remoteSize) {
		this.totalProgressLabel.setText(localSize/1204+" kb / "+remoteSize/1024 + " kb ok - total to update is "+((remoteSize-localSize)/1204)+" kb");
		this.totalProgressBar.setValue((int)((localSize*100)/remoteSize));
		this.totalProgressBar.setString(((localSize*100)/remoteSize)+"%");
		
		this.bytesLeft = remoteSize - localSize; 
	}

	@Override
	public void tick() {
		this.progressTicker.tick();
	}

	@Override
	public void speed(Long bytesPerSec) {
		this.bytesPerSeccond = (this.bytesPerSeccond*99 + bytesPerSec)/100;
		
		String eta = "0";
		if (this.bytesPerSeccond != 0) {
			eta = new SimpleDateFormat("HH:mm:ss").format(new Date((1000*this.bytesLeft)/this.bytesPerSeccond));
		}
		
		this.etaLabel.setText((this.bytesPerSeccond/1024)+" kb/s - "+eta+" left");
		
	}

	@Override
	public boolean deleteExistingFile(String filename) {
		return this.deleteCheckbox.isSelected();
	}

	@Override
	public boolean replaceExistingFile(String filename) {
		return this.replaceCheckbox.isSelected();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		this.config.setDeleteFiles(this.deleteCheckbox.isSelected());
		this.config.setReplaceFiles(this.replaceCheckbox.isSelected());
	}

}
