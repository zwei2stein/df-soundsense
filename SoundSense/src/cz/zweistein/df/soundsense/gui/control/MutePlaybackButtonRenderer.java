package cz.zweistein.df.soundsense.gui.control;

import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import cz.zweistein.df.soundsense.output.sound.player.ChannelThread;

public class MutePlaybackButtonRenderer extends JButton implements TableCellRenderer {
	private static final long serialVersionUID = -8024030814258874902L;

	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object renderedObject, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		if (((ChannelThread)renderedObject).isMute()) {
			setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.MUTE)));
		} else {
			setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.NOT_MUTE)));
		}
		
		setBorderPainted(false);
		
		return this;
	}

}
