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

package ead.editor.view.generic;

import ead.common.model.elements.extra.EAdList;
import ead.editor.control.CommandManager;
import ead.editor.control.commands.AddElementCommand;
import ead.editor.control.commands.DuplicateElementCommand;
import ead.editor.control.commands.MoveElementCommand;
import ead.editor.control.commands.RemoveElementCommand;
import ead.editor.view.generic.EAdListFieldDescriptor;
import ead.gui.listpanel.ColumnDescriptor;
import ead.gui.listpanel.ListPanel;
import ead.gui.listpanel.ListPanelListener;
import ead.gui.listpanel.columntypes.StringCellRendererEditor;

public class EAdListOption<S> extends AbstractOption<EAdList<S>> {

	public EAdListOption(String label, String toolTipText,
			EAdListFieldDescriptor<S> fieldDescriptor) {
		super(label, toolTipText, fieldDescriptor);
	}

	public EAdListFieldDescriptor<S> getListFieldDescriptor() {
		return (EAdListFieldDescriptor<S>) fieldDescriptor;
	}

    @Override
	public ListPanel getComponent(CommandManager manager) {
		EAdListPanelListener<S> listPanelListener = new EAdListPanelListener<S>(
				manager, getListFieldDescriptor());
		ListPanel listPanel = new ListPanel(listPanelListener);
		listPanelListener.setListPanel(listPanel);
		listPanel.setToolTipText(getToolTipText());
		listPanel.addColumn(new ColumnDescriptor(getTitle(), "prueba.html",
				new StringCellRendererEditor()));
		listPanel.createElements();
		return listPanel;
	}

	private class EAdListPanelListener<S> implements ListPanelListener {

		@SuppressWarnings("unused")
		private ListPanel listPanel;

        private CommandManager manager;

		private EAdListFieldDescriptor<S> eAdListFieldDescriptor;

		public EAdListPanelListener(CommandManager manager,
				EAdListFieldDescriptor<S> eAdListFieldDescriptor) {
            this.manager = manager;
			this.eAdListFieldDescriptor = eAdListFieldDescriptor;
		}

		@Override
		public void selectionChanged() {

		}

		public void setListPanel(ListPanel listPanel) {
			this.listPanel = listPanel;
		}

		@Override
		public boolean addElement() {
			S newElement = null; // TODO show panel to add new? or what?
			throw new UnsupportedOperationException("Not yet implemented");
//			AddElementCommand<S> addElement = new AddElementCommand<S>(
//					eAdListFieldDescriptor.getList(), newElement);
//			manager.performCommand(addElement);
//			return true;
		}

		@Override
		public boolean duplicateElement(int i) {
			DuplicateElementCommand<S> duplicateElement = new DuplicateElementCommand<S>(
					eAdListFieldDescriptor.getList(),
					eAdListFieldDescriptor.getElementAt(i));
			manager.performCommand(duplicateElement);
			return true;
		}

		@Override
		public boolean deleteElement(int i) {
			RemoveElementCommand<S> removeElement = new RemoveElementCommand<S>(
					eAdListFieldDescriptor.getList(),
					eAdListFieldDescriptor.getElementAt(i));
			manager.performCommand(removeElement);
			return true;
		}

		@Override
		public void moveUp(int i) {
			MoveElementCommand<S> moveElementCommand = new MoveElementCommand<S>(
					eAdListFieldDescriptor.getList(),
					eAdListFieldDescriptor.getElementAt(i), i - 1);
			manager.performCommand(moveElementCommand);
		}

		@Override
		public void moveDown(int i) {
			MoveElementCommand<S> moveElementCommand = new MoveElementCommand<S>(
					eAdListFieldDescriptor.getList(),
					eAdListFieldDescriptor.getElementAt(i), i + 1);
			manager.performCommand(moveElementCommand);
		}

		@Override
		public boolean setValue(int rowIndex, int columnIndex, Object value) {
			return false;
		}

		@Override
		public Object getValue(int rowIndex, int columnIndex) {
			return null;
		}

		@Override
		public int getCount() {
			return eAdListFieldDescriptor.getCount();
		}
	}
}
