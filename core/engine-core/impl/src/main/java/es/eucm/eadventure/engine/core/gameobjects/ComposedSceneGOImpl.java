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

package es.eucm.eadventure.engine.core.gameobjects;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.elements.scene.EAdScene;
import es.eucm.eadventure.common.model.elements.scenes.ComposedScene;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.game.GameState;
import es.eucm.eadventure.engine.core.gameobjects.factories.EventGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.go.SceneGO;
import es.eucm.eadventure.engine.core.gameobjects.sceneelements.SceneElementGOImpl;
import es.eucm.eadventure.engine.core.input.InputAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public class ComposedSceneGOImpl extends SceneElementGOImpl<ComposedScene>
		implements SceneGO<ComposedScene> {

	private static final Logger logger = Logger.getLogger("ScreenGOImpl");

	private EAdScene currentScene;

	@Inject
	public ComposedSceneGOImpl(AssetHandler assetHandler,
			StringHandler stringsReader,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState,
				eventFactory);
		logger.info("New instance");
	}

	public void doLayout(EAdTransformation transformation) {
		if (currentScene == null)
			updateScene();
		sceneElementFactory.get(currentScene).doLayout(transformation);
	}

	@Override
	public void update() {
		updateScene();
		sceneElementFactory.get(currentScene).update();
	}

	@Override
	public boolean acceptsVisualEffects() {
		if (currentScene == null)
			updateScene();
		return ((SceneGO<?>) sceneElementFactory.get(currentScene))
				.acceptsVisualEffects();
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		if (currentScene == null)
			updateScene();
		if (!allAssets) {
			assetList = sceneElementFactory.get(currentScene).getAssets(
					assetList, allAssets);
		} else {
			for (EAdScene scene : element.getScenes())
				assetList = sceneElementFactory.get(scene).getAssets(assetList,
						allAssets);
		}
		return assetList;
	}

	private void updateScene() {
		int currentSceneIndex = gameState.getValueMap().getValue(element,
				ComposedScene.VAR_CURRENT_SCENE);
		if (currentSceneIndex < element.getScenes().size())
			currentScene = element.getScenes().get(currentSceneIndex);
	}

	@Override
	public boolean processAction(InputAction<?> action) {
		return false;
	}

}
