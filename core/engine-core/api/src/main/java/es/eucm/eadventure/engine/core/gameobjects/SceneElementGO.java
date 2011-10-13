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

package es.eucm.eadventure.engine.core.gameobjects;

import java.util.List;

import es.eucm.eadventure.common.interfaces.features.Oriented;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.Renderable;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;

/**
 * 
 * @author Eugenio Marchiori
 * 
 */
public interface SceneElementGO<T extends EAdSceneElement> extends
		GameObject<T>, Oriented, Renderable {

	void setPosition(EAdPosition position);

	void setScale(float scale);

	/**
	 * Returns the final drawable asset to be rendered for this scene element
	 * 
	 * @return
	 */
	DrawableAsset<?> getRenderAsset();

	/**
	 * Returns the runtime asset representing the scene element
	 * 
	 * @return
	 */
	DrawableAsset<?> getAsset();

	/**
	 * Returns the current asset descriptor, used for rendering
	 * 
	 * @return
	 */
	AssetDescriptor getCurrentAssetDescriptor();

	int getWidth();

	int getHeight();

	List<EAdAction> getValidActions();

	/**
	 * Returns the x coordinate of scene element center, using the scale
	 * 
	 * @return
	 */
	int getCenterX();

	/**
	 * Returns the x coordinate of scene element center, using the scale
	 * 
	 * @return
	 */
	int getCenterY();

	/**
	 * Returns the current scale of the element
	 * @return
	 */
	float getScale();

}
