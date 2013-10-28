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

package es.eucm.ead.editor.view.generic.table;

/**
 * Common interface for table-like controls.
 * 
 * @author mfreire
 * @param <T> value-type
 * @param <K> key-type (Integer for lists)
 */
public interface TableLikeControl<T, K> {

	/**
	 * Removes an object from the list. Triggered either externally or via
	 * button-click.
	 *
	 * @param index
	 */
	void remove(K index);

	/**
	 * Launches UI prompt to add an element to the list
	 */
	T chooseElementToAdd();

	/**
	 * Launches UI prompt to add a key to a list element
	 */
	K chooseKeyToAdd();

	/**
	 * Adds an object to the list. Triggered either externally or via
	 * button-click.
	 * 
	 * @param added element 
	 * @param index 
	 */
	public void add(T added, K index);

	/**
	 * Moves an object one position up. Triggered either externally or via
	 * button-click. Optional operation.
	 *
	 * @param index
	 */
	void moveUp(K index);

	/**
	 * Removes an object from the list. Triggered either externally or via
	 * button-click. Optional operation.
	 *
	 * @param index
	 */
	void moveDown(K index);

	/**
	 * Returns the key for a given row
	 * @param row
	 * @return corresponding key (probably the row itself...)
	 */
	K keyForRow(int row);
}
