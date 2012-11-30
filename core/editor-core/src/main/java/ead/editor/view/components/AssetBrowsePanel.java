/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.view.components;

import ead.editor.model.nodes.EditorNode;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import javax.swing.JPanel;

/**
 *
 * @author mfreire
 */
public abstract class AssetBrowsePanel extends JPanel {

	protected TreeSet<EditorNode> selected = new TreeSet<EditorNode>(
			new IdComparator());

	public final static String selectedPropertyName = "node_selected";

	public abstract void setNodes(Collection<EditorNode> nodes);

	public AssetBrowsePanel() {
		setLayout(new BorderLayout());
	}

	public TreeSet<EditorNode> getSelected() {
		return selected;
	}

	private static class IdComparator implements Comparator<EditorNode> {
		@Override
		public int compare(EditorNode o1, EditorNode o2) {
			return o1.getId() - o2.getId();
		}
	}
}
