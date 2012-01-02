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

package ead.engine.core.gameobjects.transitions;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.fills.EAdColor;
import ead.common.params.fills.EAdPaintImpl;
import ead.common.params.text.EAdString;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.CaptionImpl;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.SceneGOImpl;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.go.TransitionGO;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.RuntimeAsset;
import ead.engine.core.util.EAdTransformation;

public class SimpleTransitionGO extends SceneGOImpl implements TransitionGO {

	protected SceneGO<?> previousSceneGO;

	protected EAdScene nextEAdScene;

	protected SceneGO<?> nextSceneGO;

	private RuntimeAsset<ImageImpl> background;

	private Caption caption;

	protected RuntimeAsset<ImageImpl> previousSceneImage;

	protected SceneElementImpl loadingText;

	protected SceneElementImpl screenBlock;

	protected boolean loading = false;

	protected boolean loaded = false;

	@Inject
	public SimpleTransitionGO(AssetHandler assetHandler,
			StringHandler stringHandler, SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState, EngineConfiguration platformConfiguration, EventGOFactory eventFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, eventFactory);

		EAdString string = EAdString.newEAdString("Loading");
		string.parse("Loading");
		caption = new CaptionImpl(string);
		loadingText = new SceneElementImpl(caption);
		loadingText.setId("loadingText");
		loadingText.setPosition(EAdPosition.volatileEAdPosition(750, 550,
				1.0f, 1.0f));

		RectangleShape rs = new RectangleShape(
				gameState.getValueMap().getValue(SystemFields.GAME_WIDTH),
				gameState.getValueMap().getValue(SystemFields.GAME_HEIGHT));
		rs.setPaint(new EAdPaintImpl(new EAdColor(100, 100, 100, 30),
				EAdColor.BLACK));

		screenBlock = new SceneElementImpl(rs);
		screenBlock.setId("screenBlock");
	}

	@Override
	public boolean acceptsVisualEffects() {
		return false;
	}

	@Override
	public void setElement(EAdScene element) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl#doLayout(int,
	 * int)
	 * 
	 * The bloking element and the loading text should never be offset
	 */

	@Override
	public void doLayout(EAdTransformation transformation) {
		gui.addElement(sceneElementFactory.get(screenBlock), transformation);
		gui.addElement(sceneElementFactory.get(loadingText), transformation);
		if (loaded) {
			gui.addElement((DrawableGO<?>) nextSceneGO, transformation);
		}
	}

	@Override
	public void setPrevious(SceneGO<?> screen) {
		this.previousSceneGO = screen;
	}

	@Override
	public void setNext(EAdScene screen) {
		this.nextEAdScene = screen;
		if (nextEAdScene == null)
			nextEAdScene = this.gameState.getPreviousScene();
	}

	@Override
	public RuntimeAsset<ImageImpl> getBackground() {
		return background;
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
			loaded = true;
		}

		if (previousSceneImage == null) {
//			previousSceneImage = gui.commitToImage();
		}

		if (loaded) {
			gameState.setScene(nextSceneGO);
			if (previousSceneImage != null)
				previousSceneImage.freeMemory();
		}
	}

}
