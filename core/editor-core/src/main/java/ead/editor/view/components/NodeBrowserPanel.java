/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.view.components;

import ead.editor.control.Controller;
import ead.editor.model.nodes.EditorNode;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
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
	protected EditorNode lastSelected = null;

	public final static String selectedPropertyName = "node_selected";

	protected Controller controller;

	public abstract void setNodes(Collection<EditorNode> nodes);

	public abstract void addNode(EditorNode node);

	protected ArrayList<EditorNode> nodes = new ArrayList<EditorNode>();

	public NodeBrowserPanel() {
		setLayout(new BorderLayout());
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public TreeSet<EditorNode> getSelected() {
		return selected;
	}

	public EditorNode getLastSelected() {
		return lastSelected;
	}

	private int indexOf(EditorNode node) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i) == node) {
				return i;
			}
		}
		return -1;
	}

	public EditorNode getPrevious() {
		int count = nodes.size();
		if (count == 0)
			return null;
		int pos = (lastSelected == null) ? count - 1
				: (indexOf(lastSelected) - 1 + count) % count;
		lastSelected = nodes.get(pos);
		return lastSelected;
	}

	public EditorNode getNext() {
		int count = nodes.size();
		if (count == 0)
			return null;
		int pos = (lastSelected == null) ? 0 : (indexOf(lastSelected) + 1)
				% count;
		lastSelected = nodes.get(pos);
		return lastSelected;
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
