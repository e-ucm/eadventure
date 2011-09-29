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

package es.eucm.eadventure.engine.core.platform.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.eadventure.common.interfaces.features.Resourced;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

/**
 * <p>
 * Abstract, platform-independent, implementation of the basic methods in the
 * asset handler.
 * </p>
 * <p>
 * This abstract implementation has a map that works as a cache of runtime
 * assets (this do not have to be loaded, so they can use very little memory.<br>
 * A class map, use to know how to load an asset of different types based on the
 * descriptor is inyected to this class.
 * </p>
 */
public abstract class AbstractAssetHandler implements AssetHandler {

	/**
	 * The class logger
	 */
	private static final Logger logger = Logger
			.getLogger("AbstractAssetHandler");


	/**
	 * A cache of the runtime assets for each asset descriptor
	 */
	protected Map<AssetDescriptor, RuntimeAsset<?>> cache;

	/**
	 * A class map of the asset descriptor classes and their corresponding
	 * runtime assets
	 */
	private Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> classMap;

	private boolean loaded = false;

	/**
	 * Default constructor, values are supplied by injection
	 * 
	 * @param injector
	 *            The guice injector
	 * @param classMap
	 *            The class map of asset descriptors to runtime assets
	 * @see Injector
	 * @see AssetDescriptor
	 * @see RuntimeAsset
	 */
	@Inject
	public AbstractAssetHandler(
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> classMap) {
		this.classMap = classMap;
		cache = new HashMap<AssetDescriptor, RuntimeAsset<?>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.AssetHandler#getRuntimeAsset(
	 * es.eucm.eadventure.common.resources.assets.AssetDescriptor)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AssetDescriptor> RuntimeAsset<T> getRuntimeAsset(
			T descriptor) {
		if (descriptor == null) {
			return null;
		}
		try {
			RuntimeAsset<T> temp = (RuntimeAsset<T>) cache.get(descriptor);

			if (temp == null) {
				Class<?> clazz = descriptor.getClass();
				Class<? extends RuntimeAsset<?>> tempClass = null;
				while (clazz != null && tempClass == null) {
					tempClass = classMap.get(clazz);
					clazz = clazz.getSuperclass();
				}
				temp = (RuntimeAsset<T>) getInstance(tempClass);
				temp.setDescriptor(descriptor);
				cache.put(descriptor, temp);
			}
			return temp;
		} catch (NullPointerException e) {
			logger.log(Level.SEVERE, "Null pointer, descriptor:" + descriptor,
					e);
			throw e;
		}
	}

	public abstract RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.AssetHandler#getRuntimeAsset(
	 * es.eucm.eadventure.common.model.EAdElement,
	 * es.eucm.eadventure.common.resources.EAdBundleId, java.lang.String)
	 */
	@Override
	public RuntimeAsset<?> getRuntimeAsset(Resourced element,
			EAdBundleId bundleId, String id) {
		AssetDescriptor descriptor = element.getAsset(bundleId, id);
		if (descriptor == null)
			descriptor = element.getAsset(id);

		if (descriptor == null) {
			logger.log(Level.SEVERE,
					"No such asset. element: " + element.getClass()
							+ "; bundleId: "
							+ (bundleId != null ? bundleId : "null") + "; id: "
							+ id);
			return null;
		}

		RuntimeAsset<?> finalAsset = cache.get(descriptor);
		if (finalAsset != null) {
			return finalAsset;
		}

		finalAsset = getRuntimeAsset(descriptor);
		cache.put(descriptor, finalAsset);
		return finalAsset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.AssetHandler#getRuntimeAsset(
	 * es.eucm.eadventure.common.model.EAdElement, java.lang.String)
	 */
	@Override
	public RuntimeAsset<?> getRuntimeAsset(Resourced element, String id) {
		return getRuntimeAsset(element, null, id);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.AssetHandler#isLoaded()
	 */
	@Override
	public boolean isLoaded() {
		return loaded;
	}

	protected void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

}
