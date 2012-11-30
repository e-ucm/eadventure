/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.view.components;

import ead.editor.model.nodes.EditorNode;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

/**
 * An abstract panel that shows collections of nodes. Supports
 * drag & drop (or at least drag), and selections.
 * @author mfreire
 */
public abstract class NodeBrowserPanel extends JPanel {

	protected TreeSet<EditorNode> selected = new TreeSet<EditorNode>(
			new IdComparator());

	public final static String selectedPropertyName = "node_selected";

	public abstract void setNodes(Collection<EditorNode> nodes);

	public NodeBrowserPanel() {
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

	/**
	 * Handles drag&drop. Actually, only drag; drop is handled in places that
	 * expect these references.
	 */
	protected class NodeTransferHandler extends TransferHandler {
		@Override
		protected Transferable createTransferable(JComponent c) {
			StringBuilder sb = new StringBuilder();
			for (EditorNode node : selected) {
				sb.append("," + node.getId());
			}
			return (sb.length() == 0) ? null : new DataHandler(sb.toString()
					.substring(1), DataFlavor.stringFlavor.getMimeType());
		}

		@Override
		public boolean canImport(TransferHandler.TransferSupport info) {
			return false;
		}

		@Override
		public int getSourceActions(JComponent c) {
			return TransferHandler.COPY;
		}

		@Override
		public boolean importData(TransferHandler.TransferSupport info) {
			throw new UnsupportedOperationException();
		}
	}
}
