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

package es.eucm.ead.engine.gameobjects.sceneelements.transitions.sceneloaders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;
import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class DefaultSceneLoader implements SceneLoader {

	static private Logger logger = LoggerFactory
			.getLogger(DefaultSceneLoader.class);

	private SceneElementFactory sceneElementFactory;

	private List<AssetDescriptor> currentAssets;

	private List<SceneElement> goList;

	private List<SceneElement> currentGoList;

	protected SceneLoaderListener sceneLoaderListener;

	private Scene scene;

	protected SceneGO sceneGO;

	protected SceneGO currentSceneGO;

	protected SceneGO oldSceneGO;

	@Inject
	public DefaultSceneLoader(SceneElementFactory sceneElementFactory) {
		this.sceneElementFactory = sceneElementFactory;
		currentAssets = new ArrayList<AssetDescriptor>();
		goList = new ArrayList<SceneElement>();
		currentGoList = new ArrayList<SceneElement>();
	}

	public void loadScene(Scene scene, SceneLoaderListener listener) {
		this.scene = scene;
		this.sceneLoaderListener = listener;
		logger.info("Loading next scene: " + scene.getId());
		loadScene();
		sceneLoaderListener.sceneLoaded(sceneGO);
		logger.info(scene.getId() + " loaded.");
	}

	protected void loadScene() {
		sceneGO = (SceneGO) sceneElementFactory.get(scene);
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

		int i = 0;
		for (SceneElement e : goList) {
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
