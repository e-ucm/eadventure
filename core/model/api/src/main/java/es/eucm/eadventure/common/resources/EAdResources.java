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

package es.eucm.eadventure.common.resources;

import java.util.Collection;

import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

/**
 * Resources for eAdventure elements. Resources are AssetBundles that have,
 * besides different assets, a list of AssetBundles that can be chosen among.
 */
public interface EAdResources extends EAdAssetBundle {

	/**
	 * Gets the path or string of an asset with the given id in the bundle with
	 * the given {@link EAdBundleId}.
	 * 
	 * @param bundleId
	 * @param id
	 *            The id of the asset
	 * @return The path of the asset
	 */
	AssetDescriptor getAsset( EAdBundleId bundleId, String id );

	/**
	 * Adds a new path for an asset in the given {@link EAdBundleId} and for the
	 * given id.
	 * 
	 * @param bundleId
	 * @param id
	 *            The id of the asset
	 * @param asset
	 *            The asset
	 * @return Returns true if the path is successfully changed.
	 */
	boolean addAsset( EAdBundleId bundleId, String id, AssetDescriptor asset );

	/**
	 * Returns the initially selected or default {@link EAdBundleId} for the
	 * resources.
	 * 
	 * @return The {@link EAdBundleId} to be initially selected.
	 */
	EAdBundleId getInitialBundle( );

	/**
	 * Sets the initial or default {@link EAdBundleID}.
	 * 
	 * @param bundleId
	 */
	void setInitialBundle( EAdBundleId bundleId );

	/**
	 * Returns the valid classes for an asset with the given id.
	 * 
	 * @param id
	 *            The id of the asset.
	 * @return The valid classes of asset.
	 */
	Class<? extends AssetDescriptor>[] getValidAssets( String id );

	/**
	 * Add a new {@link EAdBundleId} to the list of bundles
	 * 
	 * @param bundleId The {@link EAdBundleId} to be added
	 */
	void addBundle(EAdBundleId bundleId);

	/**
	 * @return The list of bundles in this resource
	 */
	Collection<EAdBundleId> getBundles();
	
	boolean isEmpty();

}
