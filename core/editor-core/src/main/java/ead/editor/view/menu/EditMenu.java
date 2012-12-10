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
import ead.editor.R;
import ead.editor.control.Controller;
import javax.swing.Action;

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
		@SuppressWarnings("unchecked")
		AbstractEditorAction<String>[] as = new AbstractEditorAction[] {
				new UndoAction(Messages.edit_menu_undo, KeyEvent.VK_Z, 0,
						R.Drawable.toolbar__undo_png),
				new RedoAction(Messages.edit_menu_redo, KeyEvent.VK_U, 0,
						R.Drawable.toolbar__redo_png),
				null, // this is a separator

				new EditorAction(Messages.edit_menu_scenes, KeyEvent.VK_S,
						KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__scenes_png),
				new EditorAction(Messages.edit_menu_player, KeyEvent.VK_P,
						KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__player_png),
				new EditorAction(Messages.edit_menu_npcs, KeyEvent.VK_N,
						KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__npcs_png),
				new EditorAction(Messages.edit_menu_conversations,
						KeyEvent.VK_C, KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__conversations_png),
				new EditorAction(Messages.edit_menu_items, KeyEvent.VK_I,
						KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__items_png),
				new EditorAction(Messages.edit_menu_atrezzo, KeyEvent.VK_A,
						KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__atrezzo_png),
				new EditorAction(Messages.edit_menu_books, KeyEvent.VK_B,
						KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__books_png),
				new EditorAction(Messages.edit_menu_advanced, KeyEvent.VK_V,
						KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__advanced_png),
				new EditorAction(Messages.edit_menu_cutscenes, KeyEvent.VK_T,
						KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__cutscenes_png),
				new EditorAction(Messages.edit_menu_adaptation_profiles,
						KeyEvent.VK_P, KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__adaptationProfiles_png),
				new EditorAction(Messages.edit_menu_assessment_profiles,
						KeyEvent.VK_R, KeyEvent.SHIFT_DOWN_MASK,
						R.Drawable.sidePanel__assessmentProfiles_png) };

		for (AbstractEditorAction<String> a : as) {
			if (a == null) {
				addSeparator();
				continue;
			}

			registerAction(a);
			if (a instanceof EditorAction) {
				// true edit-stuff actions listen to project changes
				controller.getProjectController().addChangeListener(a);
			} else {
				// but classic edit-menus are only worried with command stacks
				controller.getCommandManager().addChangeListener(a);
			}
			a.processChange(null);
		}
	}

	public class EditorAction extends AbstractEditorAction<String> {
		public EditorAction(String name, int gkey, int gmask, String iconUrl) {
			super(name, gkey, gmask, iconUrl);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// FIXME: implement all those pesky views
			System.err.println("Invoked " + getValue(Action.NAME));
		}

		@Override
		public void processChange(String o) {
			setEnabled(controller.getModel().getEngineModel() != null);
		}
	}

	public class UndoAction extends AbstractEditorAction<String> {

		public UndoAction(String name, int gkey, int gmask, String iconUrl) {
			super(name, gkey, gmask, iconUrl);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getCommandManager().undoCommand();
		}

		@Override
		public void processChange(String o) {
			setEnabled(controller.getCommandManager().canUndo());
		}
	}

	public class RedoAction extends AbstractEditorAction<String> {

		public RedoAction(String name, int gkey, int gmask, String iconUrl) {
			super(name, gkey, gmask, iconUrl);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getCommandManager().redoCommand();
		}

		@Override
		public void processChange(String o) {
			setEnabled(controller.getCommandManager().canRedo());
		}
	}
}
