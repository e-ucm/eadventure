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

package ead.common.resources;

import java.util.Collection;

import ead.common.resources.assets.AssetDescriptor;

/**
 * EAdAssetBundles represent groups of {@link EAdAssetDescriptor}s in the eAdventure model.
 */
public interface EAdAssetBundle {

	/**
	 * Gets the corresponding asset for the id
	 * 
	 * @param id The identifier
	 * @return The corresponding asset
	 */
	AssetDescriptor getAsset(String id);

	/**
	 * Adds an asset for an id
	 * 
	 * @param id the id
	 * @param asset the asset
	 * @return false if the asset cannot be added
	 */
	boolean addAsset(String id, AssetDescriptor asset);

	/**
	 * Adds a new descriptor {@link EAdAssetDescriptor} to the bundle
	 * 
	 * @param eAdAssetDescriptor An asset descriptor
	 */
	void addDescriptor(EAdAssetDescriptor eAdAssetDescriptor);
	
	/**
	 * Gets the descriptor {@link EAdAssetDescriptor} associated with
	 * an id for the resources of in the bundle.
	 * 
	 * @param id The id associated with the descriptor.
	 * @return The descriptor of the assets with the id.
	 */
	EAdAssetDescriptor getDescriptor(String id);

	/**
	 * Duplicate this asset bundle identifier
	 * 
	 * @return The new asset bundle
	 */
	EAdAssetBundle duplicate();
	
	boolean isEmpty();
	
	Collection<AssetDescriptor> getAllAssets( );
	
}
