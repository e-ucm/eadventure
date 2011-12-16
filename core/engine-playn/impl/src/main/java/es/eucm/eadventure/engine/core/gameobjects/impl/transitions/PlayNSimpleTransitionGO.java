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

package es.eucm.eadventure.engine.core.gameobjects.impl.transitions;

import static playn.core.PlayN.assetManager;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.game.GameState;
import es.eucm.eadventure.engine.core.gameobjects.factories.EventGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.go.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.go.SceneGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.EngineConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class PlayNSimpleTransitionGO extends SimpleTransitionGO {

	@Inject
	public PlayNSimpleTransitionGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EngineConfiguration platformConfiguration,
			EventGOFactory eventFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				platformConfiguration, eventFactory);
	}
	
	@Override
	public void update() {
		if (!loading) {
			loading = true;
			nextSceneGO = (SceneGO<?>) sceneElementFactory.get(nextEAdScene);

			List<RuntimeAsset<?>> newAssetList = ((DrawableGO<?>) nextSceneGO).getAssets(
					new ArrayList<RuntimeAsset<?>>(), false);

			List<RuntimeAsset<?>> oldAssetList = new ArrayList<RuntimeAsset<?>>();
			oldAssetList = ((DrawableGO<?>) previousSceneGO).getAssets(oldAssetList, true);
			// unload unnecessary assets
			if (oldAssetList != null) {
				for (RuntimeAsset<?> asset : oldAssetList) {
					if (asset != null && newAssetList != null && !newAssetList.contains(asset)) {
						asset.freeMemory();
					}
				}
			}
			System.gc();
			// pre-load minimum required assets
			for (RuntimeAsset<?> asset : newAssetList) {
				if (asset != null && !asset.isLoaded())
					asset.loadAsset();
			}

			nextSceneGO.update();
		}
		loaded = assetManager().isDone();

		if (previousSceneImage == null) {
			previousSceneImage = gui.commitToImage();
		}

		if (loaded) {
			gameState.setScene(nextSceneGO);
			if (previousSceneImage != null)
				previousSceneImage.freeMemory();
		}
	}

}
