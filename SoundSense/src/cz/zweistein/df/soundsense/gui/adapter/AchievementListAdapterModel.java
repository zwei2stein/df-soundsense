package cz.zweistein.df.soundsense.gui.adapter;

import javax.swing.table.AbstractTableModel;

import cz.zweistein.df.soundsense.config.achievement.Achievement;
import cz.zweistein.df.soundsense.config.achievement.AchievementPattern;
import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;
import cz.zweistein.df.soundsense.output.achievements.AchievementsProcesor;
import cz.zweistein.df.soundsense.output.sound.player.ChangeCallback;

public class AchievementListAdapterModel extends AbstractTableModel implements ChangeCallback {
	private static final long serialVersionUID = -6044410132230837932L;

	private AchievementsXML achievementsXML;

	public AchievementListAdapterModel(AchievementsXML achievementsXML, AchievementsProcesor ap) {
		this.achievementsXML = achievementsXML;
		ap.addChangeCallback(this);
	}

	@Override
	public void changed() {
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return this.achievementsXML.getAchievementPatterns().size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		
		AchievementPattern achievement = achievementsXML.getAchievementPatterns().get(row);
		
		Achievement current = achievement.currentAchievement();
		
		Achievement next = achievement.nextAchievement();
		
		switch (col) {
			case 0:
				return current;
			case 1:
				return next;
			default:
				return null;
		}
		
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return "Current";
			case 1:
				return "Next";
			default:
				return null;
		}
	}

	public AchievementsXML getAchievementsXML() {
		return this.achievementsXML;
	}
	
}
