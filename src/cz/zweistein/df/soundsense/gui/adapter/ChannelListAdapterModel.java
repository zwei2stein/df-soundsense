package cz.zweistein.df.soundsense.gui.adapter;

import java.util.Arrays;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import cz.zweistein.df.soundsense.output.sound.player.ChangeCallback;
import cz.zweistein.df.soundsense.output.sound.player.ChannelThread;
import cz.zweistein.df.soundsense.output.sound.player.PlayerManager;

public class ChannelListAdapterModel extends AbstractTableModel implements ChangeCallback {
	private static final long serialVersionUID = -6044410132230837932L;

	private transient PlayerManager playerManager;

	public ChannelListAdapterModel(PlayerManager playerManager) {
		this.playerManager = playerManager;
		this.playerManager.addChannelPlaybackCallback(this);
	}

	@Override
	public void changed() {
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public int getRowCount() {
		return this.playerManager.getChannels().size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValueAt(int row, int col) {
		
		ChannelThread thread = ((Entry<String, ChannelThread>)Arrays.asList(this.playerManager.getChannels().entrySet().toArray()).get(row)).getValue();
		
		switch (col) {
			case 0:
				return thread.getChannelName();
			case 1:
				return thread.getCurrentMusic();
			case 2:
				return thread.getLoopMusic();
			case 3:
				if (thread.getLoopMusic() == null) {
					return null;
				} else {
					return thread.getLoopMusic().getSoundFiles();
				}
			default:
				return thread;
		}
		
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return "Channel";
			case 1:
				return "Currently playing";
			case 2:
				return "Pattern";
			case 3:
				return "Playlist";
			default:
				return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 4 || columnIndex == 5;
	}

}
