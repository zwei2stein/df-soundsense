package cz.zweistein.df.soundsense.gui.adapter;

import javax.swing.table.AbstractTableModel;

import cz.zweistein.df.soundsense.config.achievement.Achievement;
import cz.zweistein.df.soundsense.config.achievement.AchievementPattern;
import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;
import cz.zweistein.df.soundsense.output.achievements.AchievementsProcesor;
import cz.zweistein.df.soundsense.output.sound.player.ChangeCallback;

public class AchievementListAdapterModel extends AbstractTableModel implements ChangeCallback {
	private static final long serialVersionUID = -6044410132230837932L;

	private transient AchievementsXML achievementsXML;

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
		return 4;
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
				if (current == null) {
					return "";
				} else {
					return current.getTitle();
				}
			case 1:
				if (current == null) {
					return "";
				} else {
					return current.getDescription();
				}
			case 2:
				if (current != null) {
					return achievement.getHits()+"/"+current.getTriggerAmount();
				} else if (next != null) {
					return achievement.getHits()+"/"+next.getTriggerAmount();
				} else {
					return achievement.getHits();
				}
			case 3:
				if (next == null) {
					return "";
				} else {
					return next.getTitle();
				}
			default:
				return null;
		}
		
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return "Title";
			case 1:
				return "Description";
			case 2:
				return "Progress";
			case 3:
				return "Next";
			default:
				return null;
		}
	}

}
