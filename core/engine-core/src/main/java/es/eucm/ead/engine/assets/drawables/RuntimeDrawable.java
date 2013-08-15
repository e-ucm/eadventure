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

package es.eucm.ead.engine.assets.drawables;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;

import es.eucm.ead.engine.assets.RuntimeAsset;
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.engine.canvas.GdxCanvas;

/**
 * <p>
 * Represents a {@link es.eucm.ead.engine.assets.RuntimeAsset} that can be directly drawn into the screen
 * </p>
 * 
 * @param <T>
 *            The class of the {@link EAdDrawable} object
 */
public interface RuntimeDrawable<T extends EAdDrawable> extends RuntimeAsset<T> {

	/**
	 * Returns the width of the asset
	 * 
	 * @return the width of the asset
	 */
	int getWidth();

	/**
	 * Returns the height of the asset
	 * 
	 * @return the height of the asset
	 */
	int getHeight();

	/**
	 * Render the asset at (0, 0) position (transformations are applied in the
	 * canvas)
	 * 
	 * @param c
	 *            The canvas where to render the asset
	 */
	void render(GdxCanvas c);

	/**
	 * Returns if this asset contains the given coordinates
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return
	 */
	boolean contains(int x, int y);

	/**
	 * Some drawables need more data to decide their state
	 * 
	 */
	RuntimeDrawable<?> getDrawable(int time, List<String> states, int level);

	/**
	 * Returns the OpenGL texture handle
	 * @return
	 */
	Texture getTextureHandle();

}
