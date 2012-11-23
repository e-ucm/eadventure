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

package ead.engine.core.gameobjects.go.transitions.sceneloaders;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.scenes.EAdScene;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.platform.assets.AssetHandler;
import ead.tools.SceneGraph;

@Singleton
public class GraphSceneLoader implements SceneLoader {

	private static final Logger logger = LoggerFactory
			.getLogger("GraphSceneLoader");

	private AssetHandler assetHandler;

	private SceneLoaderListener listener;

	private SceneElementGOFactory factory;

	private EAdScene scene;

	private SceneGraph sceneGraph;

	private List<EAdScene> sceneRemainingToBeLoad;

	@Inject
	public GraphSceneLoader(AssetHandler assetHandler,
			SceneElementGOFactory factory, SceneGraph sceneGraph) {
		this.assetHandler = assetHandler;
		this.factory = factory;
		this.sceneGraph = sceneGraph;
		this.sceneRemainingToBeLoad = new ArrayList<EAdScene>();
	}

	@Override
	public void loadScene(EAdScene scene, SceneLoaderListener listener) {
		this.listener = listener;
		this.scene = scene;
		sceneRemainingToBeLoad.clear();
		// Removes the assets that are loading, since they are not longer needed
		assetHandler.clearAssetQueue();
		assetHandler.queueSceneToLoad(scene);
		// If there is a lot of scenes connected to this scene, we should load
		// some of them, because probably they'll be required soon. We used a
		// random decision
		List<EAdScene> connections = sceneGraph.getGraph().get(scene);
		if (connections == null) {
			logger.warn("Scene {} has no connections. That's weird.");
		} else {
			for (int i = 0; i < connections.size(); i++) {
				// These scenes will be loaded BEFORE the scene changes
				if (Math.random() > 0.5f) {
					assetHandler.queueSceneToLoad(connections.get(i));
				}
				// These scenes will be loaded AFTER the scene changes
				else {
					sceneRemainingToBeLoad.add(connections.get(i));
				}
			}
		}

	}

	@Override
	public void freeUnusedAssets(SceneGO currentScene, SceneGO oldScene) {
		// TODO Auto-generated method stub

	}

	@Override
	public void step() {
		if (!assetHandler.loadStep()) {
			SceneGO sceneGO = (SceneGO) factory.get(scene);
			sceneGO.update();
			// Add the next scenes, so they load in the background
			for (EAdScene s : sceneRemainingToBeLoad) {
				assetHandler.queueSceneToLoad(s);
			}
			listener.sceneLoaded(sceneGO);
		}
	}

}
