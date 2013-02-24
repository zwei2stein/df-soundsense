package cz.zweistein.df.soundsense.gui.control;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import cz.zweistein.df.soundsense.gui.Icons;

public class ProgressTicker extends JLabel {
	private static final long serialVersionUID = -1672969220162646775L;
	
	private List<ImageIcon> phases;
	private int phase;

	public ProgressTicker() {
		super();

		this.phases = new ArrayList<ImageIcon>();
		
		this.phases.add(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LOAD_01)));
		this.phases.add(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LOAD_02)));
		this.phases.add(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LOAD_03)));
		this.phases.add(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LOAD_04)));
		this.phases.add(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LOAD_05)));
		this.phases.add(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LOAD_06)));
		this.phases.add(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LOAD_07)));
		this.phases.add(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Icons.LOAD_08)));
		
		this.phase = -1;
		this.tick();
		
	}
	
	public void tick() {
		this.phase++;
		if (this.phase > (this.phases.size() - 1)) {
			this.phase = 0;
		}
		this.setIcon(this.phases.get(this.phase));
	}

}
