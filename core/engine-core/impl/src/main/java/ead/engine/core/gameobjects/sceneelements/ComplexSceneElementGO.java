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

package ead.engine.core.gameobjects.sceneelements;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scene.EAdComplexSceneElement;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.util.StringHandler;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdTransformation;

public class ComplexSceneElementGO extends
		SceneElementGOImpl<EAdComplexSceneElement> {

	private static final Logger logger = LoggerFactory
			.getLogger("EAdComplexSceneElement");

	private boolean first = true;

	@Inject
	public ComplexSceneElementGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EvaluatorFactory evaluatorFactory,
			EventGOFactory eventFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				eventFactory);
		logger.info("New instance");
	}

	public void setElement(EAdComplexSceneElement element) {
		super.setElement(element);
		for (EAdSceneElement sceneElement : element.getSceneElements()) {
			SceneElementGO<?> go = sceneElementFactory.get(sceneElement);
			go.getRenderAsset();
		}
	}

	@Override
	public boolean processAction(InputAction<?> action) {
		EAdList<EAdEffect> list = element.getEffects(action.getGUIEvent());
		boolean processed = addEffects(list, action);
		if (element.getDefinition() != element) {
			list = element.getDefinition().getEffects(action.getGUIEvent());
			processed |= addEffects(list, action);
		}
		return processed;

	}

	private boolean addEffects(EAdList<EAdEffect> list, InputAction<?> action) {
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
		for (EAdSceneElement sceneElement : element.getSceneElements()) {
			sceneElementFactory.get(sceneElement).update();
		}
	}

	protected void updateVars() {
		super.updateVars();
		// ValueMap valueMap = gameState.getValueMap();
		// TODO we will use it, some day
		// setWidth(valueMap.getValue(element, EAdBasicSceneElement.VAR_WIDTH));
		// setHeight(valueMap.getValue(element,
		// EAdBasicSceneElement.VAR_HEIGHT));
		// boolean updateWidth = valueMap.getValue(element,
		// EAdComplexElementImpl.VAR_AUTO_SIZE_HORIZONTAL);
		// boolean updateHeight = valueMap.getValue(element,
		// EAdComplexElementImpl.VAR_AUTO_SIZE_VERTICAL);

		if (first) {
			updateDimensions(true, true);
			first = false;
		}
	}

	private void updateDimensions(boolean updateWidth, boolean updateHeight) {
		int minX = Integer.MAX_VALUE;
		int minY = minX;
		int maxX = Integer.MIN_VALUE;
		int maxY = maxX;
		for (EAdSceneElement sceneElement : element.getSceneElements()) {
			SceneElementGO<?> go = sceneElementFactory.get(sceneElement);
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
		for (EAdSceneElement sceneElement : element.getSceneElements()) {
			SceneElementGO<?> go = sceneElementFactory.get(sceneElement);
			gui.addElement(go, transformation);

		}
	}

	@Override
	public List<AssetDescriptor> getAssets(List<AssetDescriptor> assetList,
			boolean allAssets) {
		for (EAdSceneElement sceneElement : element.getSceneElements())
			assetList = sceneElementFactory.get(sceneElement).getAssets(
					assetList, allAssets);
		return assetList;
	}

	@Override
	public void collectSceneElements(List<EAdSceneElement> elements) {
		super.collectSceneElements(elements);
		for ( EAdSceneElement e: element.getSceneElements()){
			elements.add(e);
		}
		
	}

	// @Override
	// public boolean contains(int x, int y) {
	// for (EAdSceneElement sceneElement : element.getElements()) {
	// SceneElementGO<?> go = sceneElementFactory.get(sceneElement);
	// float[] mouse =
	// go.getTransformation().getMatrix().multiplyPointInverse(x, y, true);
	// if ( go.contains((int) mouse[0], (int) mouse[1]))
	// return true;
	// }
	// return super.contains(x, y);
	// }

}
