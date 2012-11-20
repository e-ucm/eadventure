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

package ead.engine.core.gameobjects.go;

import ead.engine.core.input.InputAction;
import ead.engine.core.util.EAdTransformation;

public interface Renderable {

	/**
	 * Layout out the child game objects of this game objects
	 * 
	 * @param transformation
	 *            the transformation accumulated by this game object container
	 */
	void doLayout(EAdTransformation transformation);

	/**
	 * Updates the element
	 */
	void update();

	boolean contains(int x, int y);

	/**
	 * Process the action in the graphic interface (click, etc.)
	 * 
	 * @param action
	 *            the action to process
	 * @return {@code true} if he action has been processed by the object
	 */
	boolean processAction(InputAction<?> action);

	/**
	 * Returns the transformation (translation, rotation, scale, etc.) of this
	 * game object
	 * 
	 * @return the transformation
	 */
	EAdTransformation getTransformation();

}
