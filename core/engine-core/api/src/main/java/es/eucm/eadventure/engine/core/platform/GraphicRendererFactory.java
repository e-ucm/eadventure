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

import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

/**
 * 
 * 
 * @param <S>
 *            The class of the graphic context
 */
public interface GraphicRendererFactory<S> {

	/**
	 * Renders an asset at the (0, 0) of the given context
	 * 
	 * @param graphicContext
	 *            graphic context
	 * @param asset
	 *            the asset to be rendered
	 */
	<T extends RuntimeAsset<?>> void render(S graphicContext, T asset);

	/**
	 * If the asset contains the given coordinates
	 * 
	 * @param x
	 *            the transformed x coordinate
	 * @param y
	 *            the transformed y coordinate
	 * @param asset
	 *            the asset
	 * @return if the asset contains the given coordinates
	 */
	<T extends RuntimeAsset<?>> boolean contains(int x, int y, T asset);

	/**
	 * Renders a game object in the given graphic context, with the given
	 * transformation
	 * 
	 * @param graphicContext
	 *            the graphic context
	 * @param gameObject
	 *            the game object
	 * @param transformation
	 *            the transformation accumulated by the game object
	 */
	<T extends GameObject<?>> void render(S graphicContext, T gameObject,
			EAdTransformation transformation);

	/**
	 * Returns if the game object contains the given coordinates
	 * 
	 * @param gameObject
	 *            the game object
	 * @param virtualX
	 *            the x coordinate of the mouse
	 * @param virtualY
	 *            the y coordinate of the mouse
	 * @param transformation
	 *            the transformation accumulated by the game object
	 * @return it the game object contains the given coordinates
	 */
	<T extends GameObject<?>> boolean contains(T gameObject, int virtualX,
			int virtualY, EAdTransformation transformation);

}
