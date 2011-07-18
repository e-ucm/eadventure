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

package es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexSceneElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class ComplexSceneElementGO extends
		SceneElementGOImpl<EAdComplexSceneElement> {

	private static final Logger logger = Logger
			.getLogger("EAdComplexSceneElement");

	@Inject
	public ComplexSceneElementGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
		logger.info("New instance");
	}

	public GameObject<?> getDraggableElement(MouseState mouseState) {
		// TODO check if draggable
		return this;
	}

	public void setElement(EAdComplexSceneElement basicSceneElement) {
		super.setElement(basicSceneElement);
		for (EAdSceneElement sceneElement : element.getComponents()) {
 			SceneElementGO<?> go = (SceneElementGO<?>) gameObjectFactory.get(sceneElement);
 			go.getRenderAsset();
		}
	}

	@Override
	public boolean processAction(GUIAction action) {
		EAdList<EAdEffect> list = element.getEffects(action
				.getGUIEvent());
		if (list != null && list.size() > 0) {
			action.consume();
			for (EAdEffect e : element.getEffects(action.getGUIEvent())) {
				gameState.addEffect(e);
			}
			return true;
		}
		return false;
	}

	@Override
	public void update(GameState state) {
		super.update(state);
		for (EAdSceneElement sceneElement : element.getComponents())
			gameObjectFactory.get(sceneElement).update(state);
	}

	@Override
	public void doLayout(int offsetX, int offsetY) {
		updateDimensions();
		int newOffsetX = offsetX + this.getPosition().getJavaX(getWidth());
		int newOffsetY = offsetY + this.getPosition().getJavaY(getHeight());
		for (EAdSceneElement sceneElement : element.getComponents()) {
 			SceneElementGO<?> go = (SceneElementGO<?>) gameObjectFactory.get(sceneElement);
 			if (go.isVisible())
 				gui.addElement(go, newOffsetX, newOffsetY);
		}
	}

	private void updateDimensions() {
		int minX = Integer.MAX_VALUE;
		int minY = minX;
		int maxX = Integer.MIN_VALUE;
		int maxY = maxX;
		for (EAdSceneElement sceneElement : element.getComponents()) {
 			SceneElementGO<?> go = (SceneElementGO<?>) gameObjectFactory.get(sceneElement);
 			int xLeft = go.getPosition().getJavaX(go.getWidth());
 			int xRight = xLeft + go.getWidth();
 			int yTop = go.getPosition().getJavaY(go.getHeight());
 			int yBottom = yTop + go.getHeight();
 			
 			minX = xLeft < minX ? xLeft : minX;
 			maxX = xRight > maxX ? xRight : maxX;
 			minY = yTop < minY ? yTop : minY;
 			maxY = yBottom >  maxY ? yBottom : maxY;
		}
		
		this.setWidth(maxX - minX);
		this.setHeight(maxY - minY);
		
	}

	@Override
	public List<EAdAction> getValidActions() {
		// TODO?
		return null;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		if (!isVisible() && !allAssets) {
			return assetList;
		}

		for (EAdSceneElement sceneElement : element.getComponents())
			assetList = gameObjectFactory.get(sceneElement).getAssets(
					assetList, allAssets);
		return assetList;
	}

}