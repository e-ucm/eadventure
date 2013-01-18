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

package ead.engine.core.gameobjects.sceneelements.transitions.sceneloaders;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.resources.assets.AssetDescriptor;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.platform.assets.AssetHandler;

@Singleton
public class DefaultSceneLoader implements SceneLoader {

	private static final Logger logger = LoggerFactory.getLogger("SceneLoader");

	private SceneElementGOFactory sceneElementFactory;

	private AssetHandler assetHandler;

	private List<AssetDescriptor> assetsList;

	private List<AssetDescriptor> currentAssets;

	private List<EAdSceneElement> goList;

	private List<EAdSceneElement> currentGoList;

	protected SceneLoaderListener sceneLoaderListener;

	private EAdScene scene;

	protected SceneGO sceneGO;

	protected SceneGO currentSceneGO;

	protected SceneGO oldSceneGO;

	@Inject
	public DefaultSceneLoader(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory) {
		this.sceneElementFactory = sceneElementFactory;
		this.assetHandler = assetHandler;
		assetsList = new ArrayList<AssetDescriptor>();
		currentAssets = new ArrayList<AssetDescriptor>();
		goList = new ArrayList<EAdSceneElement>();
		currentGoList = new ArrayList<EAdSceneElement>();
	}

	public void loadScene(EAdScene scene, SceneLoaderListener listener) {
		this.scene = scene;
		this.sceneLoaderListener = listener;
		logger.info("Loading next scene: " + scene.getId());
		loadScene();
		sceneLoaderListener.sceneLoaded(sceneGO);
		logger.info(scene.getId() + " loaded.");
	}

	protected void loadScene() {
		sceneGO = (SceneGO) sceneElementFactory.get(scene);
		assetsList.clear();

		assetsList = sceneGO.getAssets(assetsList, false);

		for (AssetDescriptor asset : assetsList) {
			if (asset != null) {
				assetHandler.getRuntimeAsset(asset, true);
			}
		}
	}

	public void freeUnusedAssets(SceneGO currentScene, SceneGO oldScene) {
		this.currentSceneGO = currentScene;
		this.oldSceneGO = oldScene;
		logger.info("Freeing unused assets...");
		freeScene();
		logger.info("Unused assets freed.");
	}

	protected void freeScene() {
		currentAssets.clear();
		// FIXME remove assets in other way
		//		assetHandler.clean(currentSceneGO.getAssets(currentAssets, true));

		goList.clear();
		currentGoList.clear();

		sceneGO.collectSceneElements(currentGoList);
		oldSceneGO.collectSceneElements(goList);

		int i = 0;
		for (EAdSceneElement e : goList) {
			if (!currentGoList.contains(e)) {
				sceneElementFactory.remove(e);
				i++;
			}
		}

		logger.info("{} unused game objects removed", i);
	}

	@Override
	public void step() {

	}

}
