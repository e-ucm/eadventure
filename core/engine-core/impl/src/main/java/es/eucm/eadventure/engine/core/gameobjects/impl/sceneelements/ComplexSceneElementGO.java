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

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdComplexElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public class ComplexSceneElementGO extends
		SceneElementGOImpl<EAdComplexElement> {

	private static final Logger logger = Logger
			.getLogger("EAdComplexSceneElement");

	private EvaluatorFactory evaluatorFactory;

	@Inject
	public ComplexSceneElementGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState,
			EvaluatorFactory evaluatorFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState);
		logger.info("New instance");
		this.evaluatorFactory = evaluatorFactory;
	}

	public GameObject<?> getDraggableElement(MouseState mouseState) {
		if (evaluatorFactory.evaluate(((EAdBasicSceneElement) element)
				.getDraggableCondition()))
			return this;
		return null;
	}

	public void setElement(EAdComplexElement element) {
		super.setElement(element);
		for (EAdSceneElement sceneElement : element.getElements()) {
			SceneElementGO<?> go = (SceneElementGO<?>) gameObjectFactory
					.get(sceneElement);
			go.getRenderAsset();
		}
	}

	@Override
	public boolean processAction(GUIAction action) {
		EAdList<EAdEffect> list = element.getEffects(action.getGUIEvent());
		boolean processed = addEffects( list, action );
		if ( element.getDefinition() != element ){
			list = element.getDefinition().getEffects(action.getGUIEvent());
			processed |= addEffects( list, action );
		}
		return processed;

	}
	
	private boolean addEffects( EAdList<EAdEffect> list, GUIAction action ){
		if (list != null && list.size() > 0) {
			action.consume();
			for (EAdEffect e : list) {
				gameState.addEffect(e, action, getElement());
			}
			return true;
		}
		return false;
	}

	@Override
	public void update() {
		super.update();
		for (EAdSceneElement sceneElement : element.getElements()) {
			gameObjectFactory.get(sceneElement).update();
		}
	}

	protected void updateVars() {
		super.updateVars();
		ValueMap valueMap = gameState.getValueMap();
		setWidth(valueMap.getValue(element, EAdBasicSceneElement.VAR_WIDTH));
		setHeight(valueMap.getValue(element, EAdBasicSceneElement.VAR_HEIGHT));
		boolean updateWidth = valueMap.getValue(element,
				EAdComplexElementImpl.VAR_AUTO_SIZE_HORIZONTAL);
		boolean updateHeight = valueMap.getValue(element,
				EAdComplexElementImpl.VAR_AUTO_SIZE_VERTICAL);

		if (updateWidth || updateHeight) {
			updateDimensions(updateWidth, updateHeight);
		}
	}

	private void updateDimensions(boolean updateWidth, boolean updateHeight) {
		int minX = Integer.MAX_VALUE;
		int minY = minX;
		int maxX = Integer.MIN_VALUE;
		int maxY = maxX;
		for (EAdSceneElement sceneElement : element.getElements()) {
			SceneElementGO<?> go = (SceneElementGO<?>) gameObjectFactory
					.get(sceneElement);
			int xLeft = go.getPosition().getJavaX(go.getWidth());
			int xRight = xLeft + go.getWidth();
			int yTop = go.getPosition().getJavaY(go.getHeight());
			int yBottom = yTop + go.getHeight();

			minX = xLeft < minX ? xLeft : minX;
			maxX = xRight > maxX ? xRight : maxX;
			minY = yTop < minY ? yTop : minY;
			maxY = yBottom > maxY ? yBottom : maxY;
		}

		if (updateWidth)
			this.setWidth(maxX - minX);

		if (updateHeight)
			this.setHeight(maxY - minY);

	}

	@Override
	public void doLayout(EAdTransformation transformation) {
		for (EAdSceneElement sceneElement : element.getElements()) {
			SceneElementGO<?> go = (SceneElementGO<?>) gameObjectFactory
					.get(sceneElement);
			gui.addElement(go, transformation);

		}
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		if (visible && !allAssets) {
			return assetList;
		}

		for (EAdSceneElement sceneElement : element.getElements())
			assetList = gameObjectFactory.get(sceneElement).getAssets(
					assetList, allAssets);
		return assetList;
	}

}
