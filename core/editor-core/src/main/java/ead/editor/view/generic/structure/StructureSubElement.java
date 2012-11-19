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

package ead.editor.view.generic.structure;

import javax.swing.Icon;

/**
 * Represent a sub-element of StructurePanel {@link StructurePanel}
 *
 * It's shown in the table of each element.
 */
public class StructureSubElement {

	private String name;

	/**
	 * The icon would be represented at the left of the element. Can be null.
	 */
	private Icon icon;

	private boolean isCreated;

	/**
	 * If element can be moved
	 */
	private boolean movable = true;

	/**
	 * If element can be duplicated
	 */
	private boolean duplicable = true;

	/**
	 * If element can be removed
	 */
	private boolean removible = true;

	/**
	 * If element can be renamed
	 */
	private boolean rename = true;

	/**
	 * Create a sub-element with a unique name. Can have an icon.
	 *
	 * @param name
	 *            the unique id
	 * @param icon
	 *            the url to an imageIcon
	 */
	public StructureSubElement(String name, Icon icon) {
		this.isCreated = false;
		this.name = name;
		this.icon = icon;
	}

	/**
	 * Return the id of the sub-element
	 *
	 * @return
	 * 		the unique id
	 */
	public String getName() {

		return name;
	}

	/**
	 * Returns the icon of the sub-element
	 * @return
	 */
	public Icon getIcon() {

		return icon;
	}

	public void setName(String name) {

		this.name = name;
	}

	public void setIcon(Icon icon) {

		this.icon = icon;
	}

	/**
	 * Returns if element is movable
	 *
	 * @return if element is movable
	 */
	public boolean isMovable() {
		return movable;
	}

	/**
	 * Sets if element is movable
	 *
	 * @param movable
	 *            if element is movable
	 */
	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public boolean canBeDuplicated() {
		return duplicable;
	}

	public boolean isCanRename() {
		return rename;
	}

	public boolean canBeRemoved() {
		return removible;
	}

	public void setJustCreated(boolean justCreated) {

		this.isCreated = justCreated;
	}

	public boolean isJustCreated() {

		return isCreated;
	}

}
