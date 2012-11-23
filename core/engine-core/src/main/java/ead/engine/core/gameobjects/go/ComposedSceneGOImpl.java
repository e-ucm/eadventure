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

package ead.engine.core.gameobjects.go;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.elements.scenes.ComposedScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.resources.assets.AssetDescriptor;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.util.EAdTransformation;

public class ComposedSceneGOImpl extends SceneElementGOImpl<ComposedScene>
		implements ComplexSceneElementGO<ComposedScene> {

	private static final Logger logger = LoggerFactory
			.getLogger("ScreenGOImpl");

	private EAdScene currentScene;

	@Inject
	public ComposedSceneGOImpl(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory);
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
	public List<AssetDescriptor> getAssets(List<AssetDescriptor> assetList,
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
	public DrawableGO<?> processAction(InputAction<?> action) {
		return null;
	}
}
