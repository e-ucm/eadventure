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

package ead.editor.control.change;

import java.util.ArrayList;

/**
 * Default change notifier implementation. Notice that this is in no way
 * thread-safe.
 */
public class ChangeNotifierImpl<E> implements ChangeNotifier<E> {

	/**
	 * List of change listeners
	 */
	private ArrayList<ChangeListener<E>> changeListenerList;

	/**
	 * Default constructor
	 */
	public ChangeNotifierImpl() {
		changeListenerList = new ArrayList<ChangeListener<E>>();
	}

	@Override
	public void addChangeListener(ChangeListener<E> changeListener) {
		changeListenerList.add(changeListener);
	}

	@Override
	public void notifyListeners(E event) {
		for (ChangeListener<E> cl : changeListenerList) {
			cl.processChange(event);
		}
	}

	@Override
	public void removeChangeListener(ChangeListener<E> changeListener) {
		changeListenerList.remove(changeListener);
	}
}
