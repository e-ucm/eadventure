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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ead.common.resources.assets.AssetDescriptor;

public class BasicAssetBundle implements EAdAssetBundle {

	/**
	 * The collection of the descriptors of the elements in the bundle
	 */
	protected Collection<EAdAssetDescriptor> descriptors;

	/**
	 * The map of id and their corresponding assets
	 */
	protected Map<String, AssetDescriptor> assets;

	public BasicAssetBundle( ) {
		descriptors = new ArrayList<EAdAssetDescriptor>( );
		assets = new HashMap<String, AssetDescriptor>( );
	}

	public BasicAssetBundle( EAdAssetDescriptor... descriptors ) {
		for ( EAdAssetDescriptor descriptor : descriptors )
			this.descriptors.add( descriptor );
	}

	public AssetDescriptor getAsset( String id ) {
// This code striclty checks for the validty of the asset, but should be used only on the editor
//		for ( EAdAssetDescriptor d : descriptors ) {
//			if ( d.getAssetId( ).equals( id ) ) {
				AssetDescriptor assetPath = assets.get( id );
				if ( assetPath == null ) {
					return null;
				}
				return assetPath;
//			}
//		}
//		logger.warn( "No such asset", id );
//		return null;
	}

	public boolean addAsset( String id, AssetDescriptor asset ) {
/*		This code striclty checks for the validty of the asset, but should be used only on the editor
		EAdAssetDescriptor descriptor = getDescriptor( id );
		if ( descriptor == null )
			return false;

		for ( Class<?> c : descriptor.getValidAssets( ) ) {
			if ( c.isAssignableFrom( asset.getClass( ) ) ) {
				assets.put( id, asset );
				return true;
			}
		}

		return false;
		*/ 
		assets.put(id, asset);
		return true;
	}

	public EAdAssetDescriptor getDescriptor( String id ) {
		EAdAssetDescriptor descriptor = null;
		for ( EAdAssetDescriptor d : descriptors )
			if ( d.getAssetId( ).equals( id ) )
				descriptor = d;
		return descriptor;
	}

	@Override
	public void addDescriptor( EAdAssetDescriptor eAdAssetDescriptor ) {
		descriptors.add( eAdAssetDescriptor );
	}

	@Override
	public EAdAssetBundle duplicate() {
		EAdAssetBundle duplicate = new BasicAssetBundle();
		for (EAdAssetDescriptor descriptor : descriptors) {
			duplicate.addDescriptor(descriptor);
		}
		return duplicate;
	}

	public Set<String> getIds() {
		return assets.keySet();
	}

	@Override
	public boolean isEmpty() {
		return assets.isEmpty();
	}

	@Override
	public Collection<AssetDescriptor> getAllAssets() {
		return assets.values();
	}
}
