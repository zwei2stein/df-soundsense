package cz.zweistein.df.soundsense.gui.control;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import cz.zweistein.df.soundsense.output.sound.SoundProcesor;

public class ChannelTable extends JTable {
	private static final long serialVersionUID = 7878780961926946651L;

	private SoundProcesor sp;

	public ChannelTable(TableModel dm, SoundProcesor sp) {
		super(dm);
		this.sp = sp;

		this.getColumnModel().getColumn(4).setMinWidth(16);
		this.getColumnModel().getColumn(4).setMaxWidth(16);
		this.getColumnModel().getColumn(5).setMinWidth(16);
		this.getColumnModel().getColumn(5).setMaxWidth(16);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		if (column == 4) {
			return new MutePlaybackButtonRenderer();
		} else if (column == 5) {
			return new StopPlaybackButtonRenderer();
		}
		return super.getCellRenderer(row, column);
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		if (column == 4) {
			return new MutePlaybackButtonEditor();
		} else if (column == 5) {
			return new StopPlaybackButtonEditor(sp.getPlayerManager());
		}
		return super.getCellEditor(row, column);
	}

}
