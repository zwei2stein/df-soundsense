package cz.zweistein.df.soundsense.gui.adapter;

import javax.swing.table.AbstractTableModel;

import cz.zweistein.df.soundsense.config.ConfigurationXML;

public class SupplementalLogsListAdapterModel extends AbstractTableModel {
	private static final long serialVersionUID = -6044410132230837932L;

	private transient ConfigurationXML configuration;

	public SupplementalLogsListAdapterModel(ConfigurationXML configuration) {
		this.configuration = configuration;
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return this.configuration.getSupplementalLogs().size();
	}

	@Override
	public Object getValueAt(int row, int col) {
				
		switch (col) {
			case 0:
				return this.configuration.getSupplementalLogs().get(row);
			default:
				return "";
		}
		
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return "Path";
			default:
				return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
