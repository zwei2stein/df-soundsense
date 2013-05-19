package cz.zweistein.df.soundsense.gui.tab;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.zweistein.df.soundsense.config.ConfigurationXML;
import cz.zweistein.df.soundsense.config.achievement.AchievementsXML;
import cz.zweistein.df.soundsense.gui.adapter.AchievementListAdapterModel;
import cz.zweistein.df.soundsense.gui.control.AchievementTable;
import cz.zweistein.df.soundsense.gui.control.ResetAchievementsButon;
import cz.zweistein.df.soundsense.output.achievements.AchievementsProcesor;

public class AchievementsPanel extends JPanel implements ChangeListener {
	private static final long serialVersionUID = -1223717359315086803L;

	private ConfigurationXML configurationXML;
	private JCheckBox achievementsEnabled;

	public AchievementsPanel(AchievementsXML achievementsXML, ConfigurationXML configurationXML, AchievementsProcesor ap) {
		super();

		this.configurationXML = configurationXML;

		this.setLayout(new BorderLayout());

		this.achievementsEnabled = new JCheckBox("Enable achievements", this.configurationXML.getAchievements());
		achievementsEnabled.addChangeListener(this);

		this.add(achievementsEnabled, BorderLayout.NORTH);

		AchievementListAdapterModel achievementTableModel = new AchievementListAdapterModel(achievementsXML, ap);
		final JTable achievementTable = new AchievementTable(achievementTableModel);

		JScrollPane achievementTableScroller = new JScrollPane(achievementTable);

		this.add(achievementTableScroller, BorderLayout.CENTER);
		
		this.add(new ResetAchievementsButon(achievementsXML), BorderLayout.SOUTH);

	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		this.configurationXML.setAchievements(achievementsEnabled.isSelected());
	}

}
