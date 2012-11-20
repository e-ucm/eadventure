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

import ead.common.resources.assets.AssetDescriptor;

/**
 * The asset descriptor defines the name and the expected types of an asset
 * in a resource.
 */
public class EAdAssetDescriptor {

	/**
	 * Id of the asset
	 */
	private String assetId;

	/**
	 * A list of the valid asset interfaces this asset might implement
	 */
	private Class<? extends AssetDescriptor>[] validAssets;

	/**
	 * Constructor for an asset descriptor. The asset descriptor requires an id
	 * and an undetermined number of classes that extend {@link AssetDescriptor}.
	 * 
	 * @param assetId The id of the descriptor
	 * @param validAssets The valid classes for the asset
	 */
	public EAdAssetDescriptor(String assetId,
			Class<? extends AssetDescriptor>... validAssets) {
		this.assetId = assetId;
		this.validAssets = validAssets;
	}

	/**
	 * Special constructor for descriptors that accept any type of asset.
	 * 
	 * @param assetId The id of the descriptor
	 */
	@SuppressWarnings("unchecked")
	public EAdAssetDescriptor(String assetId) {
		this(assetId, AssetDescriptor.class);
	}

	/**
	 * Gets the id of the asset.
	 * 
	 * @return the assetId
	 */
	public String getAssetId() {
		return assetId;
	}

	/**
	 * Gets the valid asset classes.
	 * 
	 * @return the validAssets
	 */
	public Class<? extends AssetDescriptor>[] getValidAssets() {
		return validAssets;
	}

}
