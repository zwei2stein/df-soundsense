package cz.zweistein.df.soundsense.gui.control.configuration;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import cz.zweistein.df.soundsense.gui.adapter.ChannelListAdapterModel;
import cz.zweistein.df.soundsense.gui.adapter.SFXListAdapter;
import cz.zweistein.df.soundsense.gui.control.ChannelTable;
import cz.zweistein.df.soundsense.output.sound.SoundProcesor;
import cz.zweistein.df.soundsense.output.sound.player.SFXThread;

public class CurrentlyPlayingPanel extends JPanel {
	private static final long serialVersionUID = -1223717359315086803L;

	public CurrentlyPlayingPanel(SoundProcesor sp) {
		super();
		
		this.setLayout(new BorderLayout());
		
		JPanel sfxPanel = new JPanel(new BorderLayout());

        JLabel sfxListLabel = new JLabel("SFX:", JLabel.LEFT);
        
        JList<SFXThread> sfxList = new JList<SFXThread>(new SFXListAdapter(sp.getPlayerManager()));
        JScrollPane sfxListScroller = new JScrollPane(sfxList);
        
        sfxPanel.add(sfxListLabel, BorderLayout.PAGE_START);
        sfxPanel.add(sfxListScroller, BorderLayout.CENTER);

        JPanel channelPanel = new JPanel(new BorderLayout());
        
        JLabel chanelsListLabel = new JLabel("Channels:", JLabel.LEFT);
        
        TableModel channelTableModel = new ChannelListAdapterModel(sp.getPlayerManager());
		final JTable channelTable = new ChannelTable(channelTableModel, sp);
	
        JScrollPane channelListScroller = new JScrollPane(channelTable);
        
        channelPanel.add(chanelsListLabel, BorderLayout.PAGE_START);
        channelPanel.add(channelListScroller, BorderLayout.CENTER);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sfxPanel, channelPanel);
        splitPane.setBorder(null);
        
        this.add(splitPane, BorderLayout.CENTER);
        
	}

}
