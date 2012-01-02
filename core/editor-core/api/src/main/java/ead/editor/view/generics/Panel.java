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

package ead.editor.view.generics;

import java.util.List;

/**
 * A panel interface element.
 * <p>
 * This element type allows for the display of several elements grouped in the
 * interface
 */
public interface Panel extends InterfaceElement {

	/**
	 * Available layout policies for the panel
	 */
	static enum LayoutPolicy { 
		/**
		 * A policy where each element is placed following the next, minimizing the size of the panel
		 */
		FLOW, 
		/**
		 * A policy where elements are placed next to each other, even if of different sizes
		 */
		HORIZONTAL, 
		/**
		 * A policy where elements are placed on top of each other, even if of different sizes
		 */
		VERTICAL, 
		/**
		 * A policy where elements are stacked on top of each other, each with the same height
		 */
		STRICT_VERTICAL }
	
	/**
	 * @return The list of interface elements in the panel
	 */
	List<InterfaceElement> getElements();

	/**
	 * @return The title of the panel (can be null)
	 */
	String getTitle();
	
	/**
	 * @param element The element to be added to the panel
	 */
	void addElement(InterfaceElement element);
	
	/**
	 * @return the layout policy for this panel
	 */
	LayoutPolicy getLayoutPolicy();

}
