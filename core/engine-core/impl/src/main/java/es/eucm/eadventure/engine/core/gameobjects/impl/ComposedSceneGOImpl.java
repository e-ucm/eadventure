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

package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdComposedScene;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class ComposedSceneGOImpl extends AbstractGameObject<EAdComposedScene> implements SceneGO<EAdComposedScene> {

	private static final Logger logger = Logger.getLogger("ScreenGOImpl");
	
	private EAdScene currentScene;
	
	@Inject
	public ComposedSceneGOImpl(AssetHandler assetHandler,
			StringHandler stringsReader, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
		logger.info( "New instance" );
	}
	
	public void setElement(EAdComposedScene scene) {
		super.setElement(scene);
	}
	
	public void doLayout(int offsetX, int offsetY) {
		if (currentScene == null)
			updateScene();
		gameObjectFactory.get(currentScene).doLayout(offsetX, offsetY);
	}
	
	@Override
	public void update(GameState state) {
		super.update(state);
		updateScene();
		gameObjectFactory.get(currentScene).update(state);
	}

	@Override
	public boolean acceptsVisualEffects() {
		if (currentScene == null)
			updateScene();
		return ((SceneGO<?>) gameObjectFactory.get(currentScene)).acceptsVisualEffects();
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(
			List<RuntimeAsset<?>> assetList, boolean allAssets) {
		if (currentScene == null)
			updateScene();
		if (!allAssets) {
			assetList = gameObjectFactory.get(currentScene).getAssets(assetList, allAssets);
		} else {
			for (EAdScene scene : element.getScenes())
				assetList = gameObjectFactory.get(scene).getAssets(assetList, allAssets);
		}
		return assetList;
	}

	private void updateScene() {
		int currentSceneIndex = valueMap.getValue(element.currentSceneVar());
		currentScene = element.getScenes().get(currentSceneIndex);
	}

}
