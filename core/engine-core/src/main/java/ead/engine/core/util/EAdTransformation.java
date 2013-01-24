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

package ead.engine.core.util;

import ead.common.model.params.util.EAdMatrix;
import ead.common.model.params.util.EAdRectangle;

/**
 * Transformation applicable to the graphic context.
 * <p>
 * Transformation includes a transformation matrix (translation, rotation and
 * scale) as well as values for visibility and alpha.
 */
public interface EAdTransformation extends Cloneable {

	/**
	 * @return the transformation matrix
	 */
	EAdMatrix getMatrix();

	void setMatrix(EAdMatrix matrix);

	/**
	 * @return true if the transformed elements must be visible
	 */
	boolean isVisible();

	/**
	 * @return the transparency of the transformation (1.0 opaque, 0.0
	 *         transparent)
	 */
	float getAlpha();

	/**
	 * @return clone of the transformation
	 */
	Object clone();

	void setAlpha(float sceneAlpha);

	void setVisible(boolean visible);

	EAdRectangle getClip();

	void setClip(int x, int y, int width, int height);

	boolean isValidated();

	void setValidated(boolean validated);

}
