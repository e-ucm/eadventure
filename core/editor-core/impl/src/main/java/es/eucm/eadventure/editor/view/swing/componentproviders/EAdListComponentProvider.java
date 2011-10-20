package es.eucm.eadventure.editor.view.swing.componentproviders;

import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.commands.impl.AddElementCommand;
import es.eucm.eadventure.editor.control.commands.impl.DuplicateElementCommand;
import es.eucm.eadventure.editor.control.commands.impl.MoveElementCommand;
import es.eucm.eadventure.editor.control.commands.impl.RemoveElementCommand;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.EAdListFieldDescriptor;
import es.eucm.eadventure.editor.view.generics.impl.EAdListOption;
import es.eucm.eadventure.gui.listpanel.ColumnDescriptor;
import es.eucm.eadventure.gui.listpanel.ListPanel;
import es.eucm.eadventure.gui.listpanel.ListPanelListener;
import es.eucm.eadventure.gui.listpanel.columntypes.StringCellRendererEditor;

public class EAdListComponentProvider<T> implements
		ComponentProvider<EAdListOption<T>, ListPanel> {

	private CommandManager commandManager;

	public EAdListComponentProvider(CommandManager commandManger) {
		this.commandManager = commandManger;
	}

	@Override
	public ListPanel getComponent(EAdListOption<T> element) {
		EAdListPanelListener<T> listPanelListener = new EAdListPanelListener<T>(
				element.getListFieldDescriptor());
		ListPanel listPanel = new ListPanel(listPanelListener);
		listPanelListener.setListPanel(listPanel);
		listPanel.setToolTipText(element.getToolTipText());
		listPanel.addColumn(new ColumnDescriptor(element.getTitle(), "prueba.html",
				new StringCellRendererEditor()));
		listPanel.createElements();
		return listPanel;
	}

	private class EAdListPanelListener<S> implements ListPanelListener {

		private ListPanel listPanel;

		private EAdListFieldDescriptor<S> eAdListFieldDescriptor;

		public EAdListPanelListener(
				EAdListFieldDescriptor<S> eAdListFieldDescriptor) {
			this.eAdListFieldDescriptor = eAdListFieldDescriptor;
		}

		@Override
		public void selectionChanged() {
			// TODO Auto-generated method stub

		}

		public void setListPanel(ListPanel listPanel) {
			this.listPanel = listPanel;
		}

		@Override
		public boolean addElement() {
			S newElement = null; // TODO show panel to add new? or what?
			AddElementCommand<S> addElement = new AddElementCommand<S>(
					eAdListFieldDescriptor.getList(), newElement);
			commandManager.performCommand(addElement);
			return true;
		}

		@Override
		public boolean duplicateElement(int i) {
			DuplicateElementCommand<S> duplicateElement = new DuplicateElementCommand<S>(
					eAdListFieldDescriptor.getList(),
					eAdListFieldDescriptor.getElementAt(i));
			commandManager.performCommand(duplicateElement);
			return true;
		}

		@Override
		public boolean deleteElement(int i) {
			RemoveElementCommand<S> removeElement = new RemoveElementCommand<S>(
					eAdListFieldDescriptor.getList(),
					eAdListFieldDescriptor.getElementAt(i));
			commandManager.performCommand(removeElement);
			return true;
		}

		@Override
		public void moveUp(int i) {
			MoveElementCommand<S> moveElementCommand = new MoveElementCommand<S>(
					eAdListFieldDescriptor.getList(),
					eAdListFieldDescriptor.getElementAt(i), i - 1);
			commandManager.performCommand(moveElementCommand);
		}

		@Override
		public void moveDown(int i) {
			MoveElementCommand<S> moveElementCommand = new MoveElementCommand<S>(
					eAdListFieldDescriptor.getList(),
					eAdListFieldDescriptor.getElementAt(i), i + 1);
			commandManager.performCommand(moveElementCommand);
		}

		@Override
		public boolean setValue(int rowIndex, int columnIndex, Object value) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object getValue(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() {
			return eAdListFieldDescriptor.getCount();
		}

	}

}
