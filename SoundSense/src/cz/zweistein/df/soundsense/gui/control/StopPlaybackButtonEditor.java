package cz.zweistein.df.soundsense.gui.control;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import cz.zweistein.df.soundsense.config.sounds.Loop;
import cz.zweistein.df.soundsense.config.sounds.Sound;
import cz.zweistein.df.soundsense.gui.Icons;
import cz.zweistein.df.soundsense.output.sound.player.ChannelThread;
import cz.zweistein.df.soundsense.output.sound.player.PlayerManager;

public class StopPlaybackButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	private static final long serialVersionUID = -114871533700969832L;

	private transient ChannelThread currentThread;
	private transient PlayerManager manager;
	private JButton button;

	public StopPlaybackButtonEditor(PlayerManager manager) {
		button = new JButton();
		button.addActionListener(this);
		button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.STOP)));
		button.setBorderPainted(false);

		this.manager = manager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		manager.playSound(
				new Sound(null, null, null, null, Loop.STOP_LOOPING, currentThread.getChannelName(), 0L, false, 0L, 0L, null, Threshold.NOTHING.getValue()),
				null, null, null);
	}

	@Override
	public Object getCellEditorValue() {
		return currentThread;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		currentThread = (ChannelThread) value;
		return button;
	}
}
