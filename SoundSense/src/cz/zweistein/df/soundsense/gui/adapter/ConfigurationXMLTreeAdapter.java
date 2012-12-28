package cz.zweistein.df.soundsense.gui.adapter;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import cz.zweistein.df.soundsense.config.IReloadProgressCallback;
import cz.zweistein.df.soundsense.config.Sound;
import cz.zweistein.df.soundsense.config.SoundFile;
import cz.zweistein.df.soundsense.config.SoundsXML;

public class ConfigurationXMLTreeAdapter implements TreeModel, IReloadProgressCallback {

	private SoundsXML config;
	private List<TreeModelListener> treeModelListeners;

	public ConfigurationXMLTreeAdapter(SoundsXML config) {
		this.config = config;
		this.treeModelListeners = new LinkedList<TreeModelListener>();
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		this.treeModelListeners.add(l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof SoundsXML) {
			return ((SoundsXML)parent).getXMLFiles().get(index);
		} else if (parent instanceof Sound) {
			return ((Sound)parent).getSoundFiles().get(index);
		} else if (parent instanceof String) {
			return config.getSoundsByXMLFile((String)parent).get(index);
		} else {
			return null;
		}
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof SoundsXML) {
			return ((SoundsXML)parent).getXMLFiles().size();
		} else if (parent instanceof Sound) {
			return ((Sound)parent).getSoundFiles().size();
		} else if (parent instanceof String) {
			return config.getSoundsByXMLFile((String)parent).size();
		} else {
			return 0;
		}
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof SoundsXML) {
			return ((SoundsXML)parent).getSounds().indexOf(child);
		} else if (parent instanceof Sound) {
			return ((Sound)parent).getSoundFiles().indexOf(child);
		} else {
			return 0;
		}
	}

	@Override
	public Object getRoot() {
		return config;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node instanceof SoundFile) {
			return true;
		} else if (node instanceof Sound) {
			return ((Sound)node).getSoundFiles().isEmpty();
		} else if (node instanceof String) {
			return config.getSoundsByXMLFile((String)node).isEmpty();
		} else {
			return false;
		}
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		this.treeModelListeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void done() {
		for(TreeModelListener listener: this.treeModelListeners) {
			TreeModelEvent event = new TreeModelEvent(this, new TreePath(getRoot()));
			listener.treeStructureChanged(event);
		}
	}

	@Override
	public void tick() {
	}

}
