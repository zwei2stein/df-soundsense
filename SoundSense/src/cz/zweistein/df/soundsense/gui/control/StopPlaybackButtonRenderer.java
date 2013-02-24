package cz.zweistein.df.soundsense.gui.control;

import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import cz.zweistein.df.soundsense.gui.Icons;

public class StopPlaybackButtonRenderer extends JButton implements TableCellRenderer {
	private static final long serialVersionUID = -4246131109408214743L;

	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object renderedObject, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.STOP)));
		setBorderPainted(false);
		
		return this;
	}

}
