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

package ead.editor.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.google.inject.Inject;
import ead.editor.control.Controller;
import ead.editor.control.change.ChangeListener;

/**
 * Default implementation of the editor menu.
 */
public class EditMenu extends AbstractEditorMenu {

	@Inject
	public EditMenu(Controller controller) {
		super(controller, Messages.edit_menu);
	}

	/**
	 * Initialize the editor menu
	 */
	@Override
	public void initialize() {
        AbstractEditorAction[] as = new AbstractEditorAction[]{
			new UndoAction(Messages.edit_menu_undo,
				KeyEvent.VK_Z,
				0),
            new RedoAction(Messages.edit_menu_redo,
				KeyEvent.VK_U, 
				0),

        };

        for (AbstractEditorAction a : as) {
			registerAction(a);
			// edit actions listen to command changes
			controller.getCommandManager().addChangeListener(a);			
			a.processChange(null);
        }		
	}

	public class UndoAction extends AbstractEditorAction {

		public UndoAction(String name, int gkey, int gmask) {
			super(name, gkey, gmask);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getCommandManager().undoCommand();
		}
		
		@Override
		public void processChange(Object o) {
			setEnabled(controller.getCommandManager().canUndo());
		}		
	}
	
	public class RedoAction extends AbstractEditorAction {

		public RedoAction(String name, int gkey, int gmask) {
			super(name, gkey, gmask);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getCommandManager().redoCommand();
		}
		
		@Override
		public void processChange(Object o) {
			setEnabled(controller.getCommandManager().canRedo());
		}		
	}
}
