package cz.zweistein.df.soundsense.gui.control;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import cz.zweistein.df.soundsense.config.achievement.Achievement;
import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;

public class AchievementCellRenderer implements TableCellRenderer {

	private AchievementsXML achievementsXML;
	
	public AchievementCellRenderer(AchievementsXML achievementsXML) {
		super();
		this.achievementsXML = achievementsXML;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if (value != null) {
			long progress;
			Color backgroundColor;
			if (column == 0) {
				progress = ((Achievement)value).getTriggerAmount();
				backgroundColor = Color.BLACK;
			} else {
				progress = achievementsXML.getAchievementPatterns().get(row).getHits();
				backgroundColor = Color.LIGHT_GRAY;
			}
			AchievementPanel ap = new AchievementPanel((Achievement) value, progress, null, backgroundColor);
			return ap;
		} else {
			return new JPanel();
		}
		
	}
}
