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

package ead.common.model.elements.scene;

import ead.common.interfaces.WithActions;
import ead.common.interfaces.WithBehavior;
import ead.common.interfaces.features.Documented;
import ead.common.interfaces.features.Named;
import ead.common.interfaces.features.ResourcedEvented;
import ead.common.model.EAdElement;
import ead.common.resources.EAdBundleId;
import ead.common.resources.assets.drawable.EAdDrawable;

/**
 * Scene element definition in the eAdventure model. Definition for scene
 * elements, placed in eAdventure scene.
 */
public interface EAdSceneElementDef extends EAdElement, ResourcedEvented,
		WithBehavior, Documented, Named, WithActions {

	/**
	 * Sets the appearance in the given bundle
	 *
	 * @param bundle
	 *            the bundle id
	 * @param appearance
	 *            the appearance
	 */
	public void setAppearance(EAdBundleId bundle, EAdDrawable appearance);

	/**
	 * Sets the initial appearance for the scene element
	 *
	 * @param appearance
	 *            the initial appearance
	 */
	public void setAppearance(EAdDrawable appearance);

	/**
	 * Returns the default appearance for this definition
	 *
	 * @return
	 */
	EAdDrawable getAppearance();

	/**
	 * Returns the appearance for the given bundle
	 *
	 * @param bundle
	 * @return
	 */
	EAdDrawable getAppearance(EAdBundleId bundle);

}
