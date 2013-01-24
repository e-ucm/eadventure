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

package ead.common.model.assets.drawable.compounds;

import ead.common.model.assets.drawable.basics.EAdBasicDrawable;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.params.util.EAdPosition;

/**
 * <p>
 * Composed drawable assets represent a set of drawable assets that can each be
 * drawn on-top of one another.
 * </p>
 * 
 */
public interface EAdComposedDrawable extends EAdBasicDrawable {

	/**
	 * A list with the {@link EAdBasicDrawable} compounding this element
	 * 
	 * @return
	 */
	EAdList<EAdBasicDrawable> getAssetList();

	/**
	 * A list with the positions associated to every {@link EAdBasicDrawable}
	 * returned by the {@link EAdComposedDrawable#getAssetList()}
	 * 
	 * @return
	 */
	EAdList<EAdPosition> getPositions();

	/**
	 * Adds a drawable to this composed drawable with the given offset
	 * 
	 * @param drawable
	 *            the drawable
	 * @param xOffset
	 *            the offset in the x coordinate
	 * @param yOffset
	 *            the offset in the y coordinate
	 */
	void addDrawable(EAdBasicDrawable drawable, int xOffset, int yOffset);

	/**
	 * Adds a drawable to this composed drawable with the zero offset
	 */
	void addDrawable(EAdBasicDrawable drawable);

}
