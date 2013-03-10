package cz.zweistein.df.soundsense.gui.control;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import cz.zweistein.df.soundsense.gui.adapter.AchievementListAdapterModel;

public class AchievementTable extends JTable {
	private static final long serialVersionUID = 7878780961926946651L;
	
	public AchievementTable(AchievementListAdapterModel dm) {
		super(dm);
		setRowHeight(64);
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return new AchievementCellRenderer(((AchievementListAdapterModel)this.getModel()).getAchievementsXML());
    }
	
}