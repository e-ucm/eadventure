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

package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;

/**
 * Represents an scene element that is compound with other scene elements. This
 * element acts as container. Its state (position, scale, rotation...) affects
 * its contained elements
 * 
 * 
 */
public class EAdComplexSceneElement extends EAdBasicSceneElement implements
		EAdElement, EAdSceneElement {

	/**
	 * A variable defining if this container must update its width to the
	 * minimum width to contain all its components
	 */
	public static final EAdVarDef<Boolean> VAR_AUTO_SIZE_HORIZONTAL = new EAdVarDefImpl<Boolean>(
			"autoSizeH", Boolean.class, Boolean.TRUE);

	/**
	 * A variable defining if this container must update its height to the
	 * minimum height to contain all its components
	 */
	public static final EAdVarDef<Boolean> VAR_AUTO_SIZE_VERTICAL = new EAdVarDefImpl<Boolean>(
			"autoSizeV", Boolean.class, Boolean.TRUE);

	/**
	 * A variable defining the x offset for the container
	 */
	public static final EAdVarDef<Integer> VAR_OFFSET_X = new EAdVarDefImpl<Integer>(
			"offsetX", Integer.class, 0);

	/**
	 * A variable defining the y offset for the container
	 */
	public static final EAdVarDef<Integer> VAR_OFFSET_Y = new EAdVarDefImpl<Integer>(
			"offsetY", Integer.class, 0);

	protected EAdList<EAdSceneElement> components;

	/**
	 * Creates an empty complex scene element
	 * 
	 * @param id
	 *            the id for the element
	 */
	public EAdComplexSceneElement(String id) {
		super(id);
		components = new EAdListImpl<EAdSceneElement>(EAdSceneElement.class);
		getVars().add(
				new EAdVarDef<?>[] { VAR_AUTO_SIZE_VERTICAL,
						VAR_AUTO_SIZE_HORIZONTAL, VAR_OFFSET_X, VAR_OFFSET_Y });
	}

	/**
	 * Returns the scene elements contained by this element
	 * 
	 * @return
	 */
	public EAdList<EAdSceneElement> getComponents() {
		return components;
	}

	/**
	 * Sets the bounds for this container. Values equal or less than zero are
	 * interpreted as infinitum
	 * 
	 * @param width
	 *            the width
	 */
	public void setBounds(int width, int height) {
		getVars().getVar(VAR_AUTO_SIZE_HORIZONTAL).setInitialValue(width <= 0);
		getVars().getVar(EAdSceneElementVars.VAR_WIDTH).setInitialValue(width);
		getVars().getVar(VAR_AUTO_SIZE_VERTICAL).setInitialValue(height <= 0);
		getVars().getVar(EAdSceneElementVars.VAR_HEIGHT)
				.setInitialValue(height);

	}

	/**
	 * Sets the initial offset for this container
	 * 
	 * @param xOffset
	 *            x offset
	 * @param yOffset
	 *            y offset
	 */
	public void setInitialOffset(int xOffset, int yOffset) {
		getVars().getVar(VAR_OFFSET_X).setInitialValue(xOffset);
		getVars().getVar(VAR_OFFSET_Y).setInitialValue(yOffset);
	}
}
