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
 * <p>
 * Interface for the renderers of {@link GameObject}s into the graphic context
 * </p>
 * 
 * @param <S>
 *            The class of the graphic context
 * @param <T>
 *            The actual class of the {@link GameObject}
 */
public interface GameObjectRenderer<S, T extends GameObject<?>> extends
		GraphicRenderer<S, T> {

	/**
	 * Render the game object into the graphic context, at a given position and
	 * with a given scale
	 * 
	 * @param graphicContext
	 *            The graphic context (platform-dependent) into which to draw
	 * @param object
	 *            The {@link GameObject}
	 * @param transformation
	 *            The transformation accumulated by the given element
	 */
	void render(S graphicContext, T object, EAdTransformation transformation);

	/**
	 * Returns true if the {@link GameObject} contains the point in the virtual
	 * coordinates and it is not transparent
	 * 
	 * @param object
	 *            The {@link GameObject}
	 * @param virtualX
	 *            The virtual coordinate along the x axis
	 * @param virtualY
	 *            The virtual coordinate along the y axis
	 * @param transformation
	 *            the transformation accumulated by the given element
	 * @return True if the point is contained and not transparent
	 */
	boolean contains(T object, int virtualX, int virtualY,
			EAdTransformation transformation);

}
