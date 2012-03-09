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

package ead.common.interfaces.features;

import ead.common.model.EAdElement;
import ead.common.resources.EAdBundleId;
import ead.common.resources.EAdResources;
import ead.common.resources.assets.AssetDescriptor;

/**
 * Represents an {@link EAdElement} with resources associated
 * 
 */
public interface Resourced {
	/**
	 * Get the asset for the given id.
	 * 
	 * @param id
	 *            The id of the asset
	 * @return The asset with that id
	 */
	AssetDescriptor getAsset(String id);

	/**
	 * Get the asset for the given id in the given bundle
	 * 
	 * @param bundleId
	 *            the bundle in which look for the asset
	 * @param id
	 *            the asset's id
	 * @return the asset
	 */
	AssetDescriptor getAsset(EAdBundleId bundleId, String id);

	/**
	 * Get the bundle {@link EAdBunldeId} initially selected for the element.
	 * The initial or default bundle of the element is a parameter set during
	 * edition.
	 * 
	 * 
	 * @return The initial bundle of assets. Initial bundle could be null for
	 *         elements with no initial bundle.
	 */
	EAdBundleId getInitialBundle();

	/**
	 * Sets the initial bundle for the element
	 * 
	 * @param bundleId
	 *            the initial bundle
	 */
	void setInitialBundle(EAdBundleId bundleId);

	/**
	 * Get the resources {@link EAdResources} of this element.
	 * 
	 * @return The resources associated with the element. Resources should never
	 *         be null.
	 */
	EAdResources getResources();
}
