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

package es.eucm.eadventure.gui.listpanel;

/**
 * Implements the listeners to update the elements.
 */
public interface ListPanelListener {
	
	/**
	 * When a row is selected, it allows us to update a panel, trigger an event...
	 */
	public void selectionChanged();
	
	/**
	 * Add a new element.
	 * @return
	 * 		True if no problems.
	 */
	public boolean addElement();

	/**
	 * Duplicate the selected element.
	 * @param i
	 * 		Index of selected element.
	 * @return
	 * 		True if no problems.
	 */
	public boolean duplicateElement(int i);
	
	/**
	 * Delete the selected element.
	 * @param i
	 * 		Index of selected element.
	 * @return
	 * 		True if no problems.
	 */
	public boolean deleteElement(int i);

	/**
	 * Move up a position the selected element.
	 * @param i
	 * 		Index of selected element.
	 */
	public void moveUp(int i);

	/**
	 * Move down a position the selected element.
	 * @param i
	 * 		Index of selected element.
	 */
	public void moveDown(int i);
	
	/**
	 * Set a value in a column of an element.
	 * @param rowIndex
	 * 		Index of the element.
	 * @param columnIndex
	 * 		Index of the column.
	 * @param value
	 * 		Value to set.
	 * @return
	 * 		True if no problem.
	 */
	public boolean setValue(int rowIndex, int columnIndex, Object value);

	/**
	 * Return the value of a column of an element.
	 * @param rowIndex
	 * 		Index of the element.
	 * @param columnIndex
	 * 		Index of the column.
	 * @return
	 * 		The order value.
	 */
	public Object getValue(int rowIndex, int columnIndex);
	
	/**
	 * Get the elements number, each one will be represented like a row.
	 * @return
	 * 		Elements number.
	 */
	public int getCount();

}
