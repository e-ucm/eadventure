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

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

/**
 * <p>
 * Handler of the assets in the eAdventure engine
 * </p>
 * <p>
 * The class that implements this interfaces is in charge of loading the
 * different assets into the system, and possibly performing some platform
 * specific optimizations as necessary.
 * </p>
 */
public interface AssetHandler {

	/**
	 * Initialize the asset handler, so assets can be loaded into the system
	 */
	void initilize();

	/**
	 * Terminate the asset handler, so resources are freed accordingly
	 */
	void terminate();

	/**
	 * Returns the runtime asset asset represented by the given id in the
	 * element for the selected bundle
	 * 
	 * @param element
	 *            The element with the asset
	 * @param bundleId
	 *            The selected bundle
	 * @param id
	 *            The id of the asset
	 * @return The platform-independent runtime asset
	 * @see RuntimeAsset
	 */
	RuntimeAsset<?> getRuntimeAsset(EAdElement element, EAdBundleId bundleId,
			String id);

	/**
	 * Returns the runtime asset asset represented by the given id in the
	 * element, with no asset bundle
	 * 
	 * @param element
	 *            The element with the asset
	 * @param id
	 *            The id of the asset
	 * @return The platform-independent runtime asset
	 * @see RuntimeAsset
	 */
	RuntimeAsset<?> getRuntimeAsset(EAdElement element, String id);

	/**
	 * Returns the runtime asset for a given asset descriptor
	 * 
	 * @param <T>
	 *            The type of the asset descriptor
	 * @param descriptor
	 *            The descriptor of the asset
	 * @return The runtime asset
	 * @see RuntimeAsset
	 * @see AssetDescriptor
	 */
	<T extends AssetDescriptor> RuntimeAsset<T> getRuntimeAsset(T descriptor);
	
	/**
	 * Returns true if the adventure assets have been correctly loaded
	 * 
	 * @return true if assets loaded
	 */
	boolean isLoaded();

}