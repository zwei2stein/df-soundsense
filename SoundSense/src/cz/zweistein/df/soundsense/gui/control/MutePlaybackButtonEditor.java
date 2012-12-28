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

import cz.zweistein.df.soundsense.output.sound.player.ChannelThread;

public class MutePlaybackButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	private static final long serialVersionUID = 2820023210532265955L;

	private ChannelThread currentThread;
	private JButton button;

	public MutePlaybackButtonEditor() {
		button = new JButton();
		button.addActionListener(this);
		button.setBorderPainted(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		currentThread.setMute(!currentThread.isMute());
		
		if (currentThread.isMute()) {
			button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.MUTE)));
		} else {
			button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.NOT_MUTE)));
		}
		
	}

	@Override
	public Object getCellEditorValue() {
		return currentThread;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		currentThread = (ChannelThread) value;
		
		if (currentThread.isMute()) {
			button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.MUTE)));
		} else {
			button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.NOT_MUTE)));
		}
		
		return button;
	}
}
