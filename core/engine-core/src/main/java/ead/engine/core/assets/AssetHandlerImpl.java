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

package ead.engine.core.assets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.interfaces.features.Resourced;
import ead.common.model.assets.AssetDescriptor;
import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.elements.scenes.EAdScene;
import ead.engine.core.assets.drawables.RuntimeDrawable;
import ead.engine.core.factories.mapproviders.AssetHandlerMap;
import ead.tools.GenericInjector;
import ead.tools.SceneGraph;

/**
 * <p>
 * Implementation of the basic methods in the asset handler.
 * </p>
 * <p>
 * This abstract implementation has a map that works as a cache of runtime
 * assets (this do not have to be loaded, so they can use very little memory.<br>
 * A class map, use to know how to load an asset of different types based on the
 * descriptor is inyected to this class.
 * </p>
 */
@Singleton
public abstract class AssetHandlerImpl implements AssetHandler {

	public static final String ENGINE_RESOURCES_PATH = "ead/engine/resources/";

	public static final String PROJECT_INTERNAL_PATH = "";

	protected GenericInjector injector;

	/**
	 * The class logger
	 */
	protected static final Logger logger = LoggerFactory
			.getLogger("AbstractAssetHandler");
	/**
	 * A cache of the runtime assets for each asset descriptor
	 */
	final private Map<AssetDescriptor, RuntimeAsset<?>> cache;
	/**
	 * A class map of the asset descriptor classes and their corresponding
	 * runtime assets
	 */
	private Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> classMap;
	private boolean loaded = false;
	protected String resourcesUri;
	private boolean cacheEnabled;
	private ArrayList<AssetDescriptor> assetsQueue;
	private SceneGraph sceneGraph;
	protected String currentLanguage;

	@Inject
	public AssetHandlerImpl(GenericInjector injector, SceneGraph sceneGraph) {
		this.injector = injector;
		this.classMap = new AssetHandlerMap().getMap();
		cache = new HashMap<AssetDescriptor, RuntimeAsset<?>>();
		cacheEnabled = true;
		assetsQueue = new ArrayList<AssetDescriptor>();
		this.sceneGraph = sceneGraph;
	}

	public void queueSceneToLoad(EAdScene scene) {
		List<AssetDescriptor> list = sceneGraph.getSceneAssets().get(scene);
		if (list == null) {
			logger.warn("Assets for scene {} were empty in the scene graph",
					scene.getId());
		} else {
			int i = 0;
			for (AssetDescriptor a : list) {
				if (!cache.containsKey(a)) {
					assetsQueue.add(a);
					i++;
				}
			}

			if (i > 0) {
				logger.info("{} assets will be loaded", i);
			}
		}

	}

	public void clearAssetQueue() {
		assetsQueue.clear();
	}

	public boolean loadStep() {
		if (assetsQueue.size() > 0) {
			AssetDescriptor asset = assetsQueue.remove(0);
			getRuntimeAsset(asset, true);
		}
		return assetsQueue.size() > 0;
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

		RuntimeAsset<T> temp = null;
		if (cacheEnabled) {
			synchronized (cache) {
				temp = (RuntimeAsset<T>) cache.get(descriptor);
			}
		}

		if (temp == null) {
			Class<?> clazz = descriptor.getClass();
			Class<? extends RuntimeAsset<?>> tempClass = null;
			while (clazz != null && tempClass == null) {
				tempClass = classMap.get(clazz);
				clazz = clazz.getSuperclass();
			}
			temp = (RuntimeAsset<T>) getInstance(tempClass);
			temp.setDescriptor(descriptor);
			if (cacheEnabled) {
				synchronized (cache) {
					cache.put(descriptor, temp);
				}
			}
		}
		return temp;

	}

	@Override
	public <T extends AssetDescriptor> RuntimeAsset<T> getRuntimeAsset(
			T descriptor, boolean load) {
		RuntimeAsset<T> runtimeAsset = getRuntimeAsset(descriptor);
		if (runtimeAsset == null) {
			logger.warn("No runtime asset for {}", descriptor);
		}
		if (load && !runtimeAsset.isLoaded()) {
			runtimeAsset.loadAsset();
		}
		return runtimeAsset;
	}

	@Override
	public <T extends EAdDrawable, GraphicContext> RuntimeDrawable<T> getDrawableAsset(
			T descriptor) {
		return (RuntimeDrawable<T>) getRuntimeAsset(descriptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.AssetHandler#getRuntimeAsset(
	 * es.eucm.eadventure.common.model.EAdElement,
	 * es.eucm.eadventure.common.resources.EAdBundleId, java.lang.String)
	 */
	@Override
	public RuntimeAsset<?> getRuntimeAsset(Resourced element, String bundleId,
			String id) {
		AssetDescriptor descriptor = element.getAsset(bundleId, id);
		if (descriptor == null) {
			descriptor = element.getAsset(id);
		}

		if (descriptor == null) {
			logger.error("No such asset. element: " + element.getClass()
					+ "; bundleId: " + (bundleId != null ? bundleId : "null")
					+ "; id: " + id);
			return null;
		}
		return getRuntimeAsset(descriptor);
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

	private ArrayList<AssetDescriptor> descriptorsToRemove = new ArrayList<AssetDescriptor>();

	@Override
	public void clean(List<AssetDescriptor> exceptions) {
		descriptorsToRemove.clear();
		synchronized (cache) {
			if (exceptions != null) {
				for (AssetDescriptor asset : cache.keySet()) {
					if (!exceptions.contains(asset)) {
						descriptorsToRemove.add(asset);
					}
				}
			} else {
				descriptorsToRemove.addAll(cache.keySet());
			}

			for (AssetDescriptor a : descriptorsToRemove) {
				RuntimeAsset<?> asset = cache.remove(a);
				asset.freeMemory();
			}
		}

		logger.info(descriptorsToRemove.size()
				+ " unused assets were remove from the cache");
	}

	protected void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	@Override
	public void setResourcesLocation(String uri) {
		this.resourcesUri = uri;
	}

	@Override
	public void setCacheEnabled(boolean enable) {
		this.cacheEnabled = enable;
	}

	public void setLanguage(String currentLanguage) {
		if (this.currentLanguage == null
				|| !this.currentLanguage.equals(currentLanguage)) {
			this.currentLanguage = currentLanguage;
			refresh();
		}
	}

	@Override
	public void refresh() {
		synchronized (cache) {
			for (RuntimeAsset<?> r : cache.values()) {
				r.refresh();
			}
		}
	}

	public void remove(AssetDescriptor assetDescriptor) {
		RuntimeAsset<?> asset = null;
		synchronized (cache) {
			asset = cache.remove(assetDescriptor);
		}
		if (asset != null && asset.isLoaded()) {
			asset.freeMemory();
		}
	}

	@Override
	public void initialize() {

	}

	@Override
	public void terminate() {

	}

	@Override
	public String getTextFile(String path) {
		FileHandle fh = getFileHandleLocalized(path.substring(1));

		if (fh != null) {
			StringBuilder text = new StringBuilder();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(fh.reader());
				String line = null;
				while ((line = reader.readLine()) != null) {
					text.append(line + "\n");
				}
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} catch (GdxRuntimeException e) {

			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {

				}
			}
			return text.toString();
		}
		return null;
	}

	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {
		return injector.getInstance(clazz);
	}

	public FileHandle getFileHandle(String path) {
		String uri = path.substring(1);
		FileHandle fh = null;
		if (currentLanguage != null && !"".equals(currentLanguage)) {
			fh = getFileHandleLocalized(currentLanguage + "/" + uri);
		}
		if (fh == null || !fh.exists()) {
			fh = getFileHandleLocalized(uri);
		}
		return fh;
	}

	/**
	 * retrieves a file handle for the path
	 * 
	 * @param path
	 * @return
	 */
	public FileHandle getFileHandleLocalized(String uri) {
		if (resourcesUri != null) {
			FileHandle absolute = getProjectFileHandle(uri);
			if (absolute.exists()) {
				return absolute;
			}
		}
		FileHandle internal = getProjectInternal(uri);
		if (internal.exists()) {
			return internal;
		}

		return getEngineFileHandle(uri);
	}

	public FileHandle getProjectFileHandle(String uri) {
		return Gdx.files.absolute(resourcesUri + "/" + uri);
	}

	public FileHandle getProjectInternal(String uri) {
		return Gdx.files.internal(PROJECT_INTERNAL_PATH + uri);
	}

	public FileHandle getEngineFileHandle(String uri) {
		return Gdx.files.internal(ENGINE_RESOURCES_PATH + uri);
	}

	public int getCacheSize() {
		return cache != null ? cache.size() : 0;
	}
}
