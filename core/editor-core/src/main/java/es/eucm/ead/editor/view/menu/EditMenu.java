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

package es.eucm.ead.editor.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.google.inject.Inject;
import es.eucm.ead.editor.R;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.model.EditorModelImpl;
import es.eucm.ead.editor.model.nodes.LogNode;
import es.eucm.ead.editor.util.Log4jConfig;
import es.eucm.ead.tools.java.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the editor menu.
 */
public class EditMenu extends AbstractEditorMenu {

	private static Logger logger = LoggerFactory.getLogger(EditMenu.class);

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
						R.Drawable.sidePanel__assessmentProfiles_png),
				null, // this is a separator
				new TestAction(Messages.edit_menu_test1, KeyEvent.VK_1,
						KeyEvent.CTRL_DOWN_MASK,
						R.Drawable.sidePanel__test1_png),
				new TestAction(Messages.edit_menu_test2, KeyEvent.VK_2,
						KeyEvent.CTRL_DOWN_MASK,
						R.Drawable.sidePanel__test2_png),
				new TestAction(Messages.edit_menu_test3, KeyEvent.VK_3,
						KeyEvent.CTRL_DOWN_MASK,
						R.Drawable.sidePanel__test3_png),
				null, // this is a separator
				new ConsoleAction(Messages.edit_menu_console, KeyEvent.VK_O,
						KeyEvent.ALT_DOWN_MASK, R.Drawable.assets__log_png) };
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

	public class TestAction extends EditorAction {

		public TestAction(String name, int gkey, int gmask, String iconUrl) {
			super(name, gkey, gmask, iconUrl);
		}

		public void actionPerformed(ActionEvent e) {
			logger.info(e.getActionCommand());
			switch (e.getActionCommand().substring(
					e.getActionCommand().length() - 1).charAt(0)) {
			case '1': {
				logger.info("testing import...");
				File dest = new File(
						"/home/mfreire/code/e-ucm/e-adventure-1.x/games/x");
				if (dest.exists()) {
					try {
						FileUtils.deleteRecursive(dest);
					} catch (IOException ex) {
						logger.info("Could not delete previous version", ex);
					}
				}
				File source = new File(
						"/home/mfreire/code/e-ucm/e-adventure-1.x/games/simple");
				controller.getProjectController().doImport(
						source.getAbsolutePath(), dest.getAbsolutePath());
				logger.info("... import finished");
				break;
			}
			case '2': {
				break;
			}
			case '3': {
				break;
			}
			default:
				throw new IllegalArgumentException(e.getActionCommand());
			}
		}

		@Override
		public void processChange(String o) {
			setEnabled(true);
		}
	}

	public class ConsoleAction extends EditorAction {

		public ConsoleAction(String name, int gkey, int gmask, String iconUrl) {
			super(name, gkey, gmask, iconUrl);
		}

		public void processChange(String o) {
			setEnabled(true);
		}

		public void actionPerformed(ActionEvent e) {
			logger.info(e.getActionCommand());
			LogNode ln = new LogNode(controller.getModel().generateId(null));
			((EditorModelImpl) controller.getModel())
					.registerNode(ln, "addLog");
			controller.getViewController()
					.addView("log", "" + ln.getId(), true);
		}
	}

	public class EditorAction extends AbstractEditorAction<String> {
		public EditorAction(String name, int gkey, int gmask, String iconUrl) {
			super(name, gkey, gmask, iconUrl);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// FIXME: implement all those pesky views
			System.err.println("Invoked " + getValue(NAME));
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
