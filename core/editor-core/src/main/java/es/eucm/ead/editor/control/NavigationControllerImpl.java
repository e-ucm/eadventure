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

package es.eucm.ead.editor.control;

import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.event.CFocusListener;
import bibliothek.gui.dock.common.intern.CDockable;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.editor.control.change.ChangeNotifierImpl;
import es.eucm.ead.editor.view.dock.ClassDockableFactory;

import java.util.ArrayList;

/**
 * Default implementation for the {@link NavigationController}.
 */
@Singleton
public class NavigationControllerImpl extends ChangeNotifierImpl<String>
		implements NavigationController, CFocusListener {

	private Controller controller;
	private ArrayList<String> list = new ArrayList<String>();
	private int current;
	private boolean updating = false;

	@Inject
	NavigationControllerImpl() {
		current = -1;
	}

	@Override
	public void goForward() {
		if (current < list.size() - 1) {
			String next = list.get(++current);
			updating = true;
			controller.getViewController().addView("", next, true);
			updating = false;
			notifyListeners(next);
		}
	}

	@Override
	public void goBackward() {
		if (current > 0) {
			String previous = list.get(--current);
			updating = true;
			controller.getViewController().addView("", previous, true);
			updating = false;
			notifyListeners(previous);
		}
	}

	@Override
	public boolean canGoForward() {
		return current < list.size() - 1;
	}

	@Override
	public boolean canGoBackward() {
		return current > 0;
	}

	@Override
	public void clearHistory() {
		list.clear();
		current = -1;
	}

	/**
	 * Set the actual super-controller.
	 * @param controller the main controller, providing access to model, views,
	 * and more
	 */
	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void focusGained(CDockable dockable) {
		if (updating) {
			// do not trigger for focus-gained due to goForward or goBackward
			return;
		}

		String id = ClassDockableFactory
				.getDockableId((DefaultMultipleCDockable) dockable);

		// move all "future" elements to start
		ArrayList<String> nextList = new ArrayList<String>();
		if (canGoForward()) {
			for (String s : list.subList(current + 1, list.size())) {
				if (!s.equals(id)) {
					nextList.add(s);
				}
			}
		}
		// copy "past" elements after the "future" ones
		for (String s : list.subList(0, current + 1)) {
			if (!s.equals(id)) {
				nextList.add(s);
			}
		}
		list = nextList;

		// add id as last element
		current = list.size();
		list.add(id);
		notifyListeners(id);
	}

	@Override
	public void focusLost(CDockable dockable) {
		// not interested
	}
}
