package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.gui.adapter.SupplementalLogsListAdapterModel;
import cz.zweistein.df.soundsense.gui.control.SupplementalLogTable;

public class PathsConfigurationPanel extends JPanel {
	private static final long serialVersionUID = -1223717359315086803L;

	public PathsConfigurationPanel(ConfigurationXML configuration) {
		super();
		
		this.setLayout(new BorderLayout());
		
		JPanel gamelogPathPanel = new JPanel(new BorderLayout());

        JLabel gamelogPathLabel = new JLabel("Game log:", JLabel.LEFT);
        JTextField gamelogPathValue = new JTextField(configuration.getGamelogPath());
        gamelogPathValue.setEditable(false);
        
        gamelogPathPanel.add(gamelogPathLabel, BorderLayout.PAGE_START);
        gamelogPathPanel.add(gamelogPathValue, BorderLayout.CENTER);

        JPanel supplementalLogsPanel = new JPanel(new BorderLayout());
        
        JLabel supplementalLogsLabel = new JLabel("Additional logs:", JLabel.LEFT);
        
        TableModel supplementalLogsTableModel = new SupplementalLogsListAdapterModel(configuration);
		final JTable supplementalLogsTable = new SupplementalLogTable(supplementalLogsTableModel);
	
        JScrollPane supplementalLogsScroller = new JScrollPane(supplementalLogsTable);
        
        supplementalLogsPanel.add(supplementalLogsLabel, BorderLayout.PAGE_START);
        supplementalLogsPanel.add(supplementalLogsScroller, BorderLayout.CENTER);
        
        this.add(gamelogPathPanel, BorderLayout.PAGE_START);
        this.add(supplementalLogsPanel, BorderLayout.CENTER);
        
	}

}