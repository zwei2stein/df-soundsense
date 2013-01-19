package cz.zweistein.df.soundsense.gui;

import java.io.File;

import javax.swing.JFileChooser;

import cz.zweistein.df.soundsense.config.ConfigurationXML;

public class GameLogValidator {

	private ConfigurationXML configuration;

	public GameLogValidator(ConfigurationXML configuration) {
		this.configuration = configuration;
	}
	
	public void gamelogValidate() {
		
		boolean hadInvalidLogfile = false;
		
		while (!(new File(configuration.getGamelogPath()).exists())) {
			hadInvalidLogfile = true;
			
			final JFileChooser fc = new JFileChooser(configuration.getGamelogPath());
			
			fc.setDialogTitle("Find gamelog.txt in Dwarf Fortress directory.");

			int returnVal = fc.showOpenDialog(null);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            configuration.setGamelogPath(file.getAbsolutePath());
			}
			
		}
		
		if (hadInvalidLogfile) {
			configuration.saveConfiguration();
		}
		
	}

}
