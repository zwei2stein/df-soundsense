package cz.zweistein.df.soundsense.gui.tab;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;
import cz.zweistein.df.soundsense.gui.adapter.AchievementListAdapterModel;
import cz.zweistein.df.soundsense.gui.control.AchievementTable;
import cz.zweistein.df.soundsense.output.achievements.AchievementsProcesor;

public class AchievementsPanel extends JPanel {
	private static final long serialVersionUID = -1223717359315086803L;

	public AchievementsPanel(AchievementsXML achievementsXML, AchievementsProcesor ap) {
		super();
		
		this.setLayout(new BorderLayout());
		
		TableModel achievementTableModel = new AchievementListAdapterModel(achievementsXML, ap);
		final JTable achievementTable = new AchievementTable(achievementTableModel);
		
		JScrollPane achievementTableScroller = new JScrollPane(achievementTable);

        this.add(achievementTableScroller, BorderLayout.CENTER);
        
	}

}