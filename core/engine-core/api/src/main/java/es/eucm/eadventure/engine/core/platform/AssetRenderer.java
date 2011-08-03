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

package es.eucm.eadventure.engine.core.platform;

import es.eucm.eadventure.common.params.geom.EAdPosition;

/**
 * <p>
 * Graphic (i.e. screen) renderer for a runtime asset
 * </p>
 * <p>
 * Classes that implement this interface will be platform-dependent and allow
 * the graphic representation of {@link RuntimeAsset} in the screen.
 * </p>
 * 
 * @param <S>
 *            The graphic context of the platform
 * @param <T>
 *            The {@link RuntimeAsset} that is rendered
 */
public interface AssetRenderer<S, T extends RuntimeAsset<?>> extends
		GraphicRenderer<S, T> {

	/**
	 * Render the {@link RuntimeAsset} into the graphic context in the given
	 * position with the given scale
	 * 
	 * @param graphicContext
	 *            The graphic context where to draw
	 * @param asset
	 *            The {@link RuntimeAsset}
	 * @param position
	 *            The position of the {@link RuntimeAsset}
	 * @param scale
	 *            The scale of the asset in the graphic context
	 */
	void render(S graphicContext, T asset, EAdPosition position, float scale, int offsetX, int offsetY);

	/**
	 * <p>
	 * Returns true if the asset contains the coordinates (x,y)
	 * </p>
	 * <p>
	 * This method should return false if the point (x, y) is transparent in the
	 * asset
	 * </p>
	 * 
	 * @param x
	 *            The x coordinates
	 * @param y
	 *            The y coordinates
	 * @param asset
	 *            The asset to check for the point (x, y)
	 * @return true if the asset contains (x, y) and it is not transparent
	 */
	boolean contains(int x, int y, T asset);

}
