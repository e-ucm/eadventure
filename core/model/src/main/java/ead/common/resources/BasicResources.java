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
import java.util.logging.Level;
import java.util.logging.Logger;

import ead.common.model.EAdElement;
import ead.common.resources.assets.AssetDescriptor;

public class BasicResources extends BasicAssetBundle implements EAdResources {

	private Map<EAdBundleId, EAdAssetBundle> assetBundles;

	private EAdBundleId initialBundle;

	private static final Logger logger = Logger.getLogger( "EAdResourcesImpl" );

	/**
	 * Default constructor. The different asset descriptors and bundles are
	 * generated for the @Asset and @BundledAsset annotations in the definition.
	 */
	public BasicResources( Class<? extends EAdElement> c ) {
		super( );
		assetBundles = new HashMap<EAdBundleId, EAdAssetBundle>( );

		boolean hasBundle = false;
		EAdAssetBundle bundle = new BasicAssetBundle( );

		
		/* Should probably only be implemented for a "strict" resource implementation
		for ( java.lang.reflect.Field a : c.getFields( ) ) {
			Asset asset = a.getAnnotation( Asset.class );
			Bundled bundled = a.getAnnotation( Bundled.class );
			if ( asset != null && bundled != null ) {
				bundle.addDescriptor( new EAdAssetDescriptor( a.getName( ), asset.value( ) ) );
				hasBundle = true;
			}
			else if ( asset != null )
				descriptors.add( new EAdAssetDescriptor( a.getName( ), asset.value( ) ) );
		}
		*/
		hasBundle = true;

		if ( hasBundle ) {
			initialBundle = new EAdBundleId( "default" );
			assetBundles.put( initialBundle, bundle );
		}
	}

	@Override
	public AssetDescriptor getAsset( String id ) {
		AssetDescriptor asset = super.getAsset( id );
		if ( asset != null )
			return asset;
		logger.log(Level.SEVERE, "No such asset, id: " + id );
		return null;
	}

	@Override
	public AssetDescriptor getAsset( EAdBundleId bundleId, String id ) {
		if ( assetBundles.get( bundleId ) == null )
			return this.getAsset( id );
		return assetBundles.get( bundleId ).getAsset( id );
	}

	@Override
	public boolean addAsset( EAdBundleId bundleId, String id, AssetDescriptor asset ) {
		if (!assetBundles.containsKey( bundleId ))
			this.addBundle(bundleId);
		return assetBundles.get( bundleId ).addAsset( id, asset );
	}

	@Override
	public void addBundle( EAdBundleId bundleId) {
		if (!assetBundles.containsKey(bundleId)) {
			EAdAssetBundle initialBundle = assetBundles.get(this.initialBundle);
			EAdAssetBundle bundle = initialBundle.duplicate();
			assetBundles.put(bundleId, bundle);
		}
	}
	
	@Override
	public EAdBundleId getInitialBundle( ) {
		return this.initialBundle;
	}

	@Override
	public void setInitialBundle( EAdBundleId bundleId ) {
		this.initialBundle = bundleId;
	}

	@Override
	public Class<? extends AssetDescriptor>[] getValidAssets( String id ) {
		if ( this.getDescriptor( id ) != null )
			return this.getDescriptor( id ).getValidAssets( );
		else
			return assetBundles.get( initialBundle ).getDescriptor( id ).getValidAssets( );
	}

	public Set<EAdBundleId> getBundleIds() {
		return assetBundles.keySet();
	}

	public EAdAssetBundle getBundle(EAdBundleId bundleId) {
		return assetBundles.get(bundleId);
	}

	@Override
	public Collection<EAdBundleId> getBundles() {
		return assetBundles.keySet();
	}

	@Override
	public boolean isEmpty() {
		if ( super.isEmpty() ){
			for ( EAdAssetBundle assetBundle: assetBundles.values()){
				if ( !assetBundle.isEmpty()){
					return false;
				}
			}
			return true;
		}
		return false;
			
	}

	@Override
	public void removeBundle(EAdBundleId bundleId) {
		if (assetBundles.keySet().size() > 1)
			assetBundles.remove(bundleId);
		if (bundleId == initialBundle)
			setInitialBundle(assetBundles.keySet().toArray(new EAdBundleId[] {})[0]);
	}
	
	public Collection<AssetDescriptor> getAllAssets( ){
		ArrayList<AssetDescriptor> allAssets = new ArrayList<AssetDescriptor>();
		for ( EAdAssetBundle bundle: assetBundles.values() ){
			allAssets.addAll(bundle.getAllAssets());
		}
		allAssets.addAll(assets.values());
		return allAssets;
	}

}
