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

/**
 * Default implementation of the editor menu.
 */
public class WindowMenu extends AbstractEditorMenu {

	@Inject
	public WindowMenu(Controller controller) {
		super(controller, Messages.window_menu);
	}

	/**
	 * Initialize the editor menu
	 */
	@Override
	public void initialize() {
		AbstractEditorAction[] as = new AbstractEditorAction[] {
				new PrevAction(Messages.window_menu_prev, KeyEvent.VK_P, 0,
						R.Drawable.toolbar__backward_png),
				new NextAction(Messages.window_menu_next, KeyEvent.VK_N, 0,
						R.Drawable.toolbar__forward_png),
				new ClearAction(Messages.window_menu_clear, KeyEvent.VK_C, 0), };

		for (AbstractEditorAction a : as) {
			if (a == null) {
				addSeparator();
				continue;
			}

			registerAction(a);
			// edit actions listen to navigation changes
			controller.getNavigationController().addChangeListener(a);
			a.processChange(null);
		}
	}

	public class PrevAction extends AbstractEditorAction {

		public PrevAction(String name, int gkey, int gmask, String iconUrl) {
			super(name, gkey, gmask, iconUrl);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getNavigationController().goForward();
		}

		@Override
		public void processChange(Object o) {
			setEnabled(controller.getNavigationController().canGoBackward());
		}
	}

	public class NextAction extends AbstractEditorAction {

		public NextAction(String name, int gkey, int gmask, String iconUrl) {
			super(name, gkey, gmask, iconUrl);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getNavigationController().goForward();
		}

		@Override
		public void processChange(Object o) {
			setEnabled(controller.getNavigationController().canGoForward());
		}
	}

	public class ClearAction extends AbstractEditorAction {

		public ClearAction(String name, int gkey, int gmask) {
			super(name, gkey, gmask);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getViewController().clearViews();
		}
	}
}
