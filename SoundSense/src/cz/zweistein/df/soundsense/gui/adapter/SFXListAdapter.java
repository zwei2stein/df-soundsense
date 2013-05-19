package cz.zweistein.df.soundsense.gui.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import cz.zweistein.df.soundsense.output.sound.player.PlayerManager;
import cz.zweistein.df.soundsense.output.sound.player.SFXPlaybackCallback;
import cz.zweistein.df.soundsense.output.sound.player.SFXThread;

public class SFXListAdapter implements ListModel<SFXThread>, SFXPlaybackCallback {

	private PlayerManager playerManager;

	private List<ListDataListener> listeners;

	public SFXListAdapter(PlayerManager playerManager) {
		this.playerManager = playerManager;

		this.listeners = new ArrayList<ListDataListener>();

		this.playerManager.addSfxPlaybackCallback(this);
	}

	@Override
	public SFXThread getElementAt(int index) {
		synchronized (playerManager) {
			try {
				return (SFXThread) Arrays.asList(playerManager.getSfxThreads().toArray()).get(index);
			} catch (ArrayIndexOutOfBoundsException e) {
				return null;
			}
		}
	}

	@Override
	public int getSize() {
		return playerManager.getSfxThreads().size();
	}

	@Override
	public void addListDataListener(ListDataListener arg0) {
		this.listeners.add(arg0);
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		this.listeners.remove(arg0);
	}

	@Override
	public void ended() {
		for (ListDataListener listener : this.listeners) {
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this.playerManager.getSfxThreads().size() + 1));
		}
	}

	@Override
	public void started() {
		for (ListDataListener listener : this.listeners) {
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this.playerManager.getSfxThreads().size()));
		}
	}

}
