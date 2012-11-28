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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.inject.Inject;

import ead.common.model.elements.scenes.EAdComplexSceneElement;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.resources.assets.AssetDescriptor;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.util.EAdTransformation;

public class ComplexSceneElementGOImpl<T extends EAdComplexSceneElement>
		extends SceneElementGOImpl<T> implements
		ComplexSceneElementGO<T> {

	protected List<DrawableGO<?>> sceneElements;

	private boolean first = true;

	private ArrayList<DrawableGO<?>> goUnderMouse;

	/**
	 * Comparator to order the scene elements
	 */
	private Comparator<DrawableGO<?>> comparator;

	@Inject
	public ComplexSceneElementGOImpl(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState,
				eventFactory);
		sceneElements = new ArrayList<DrawableGO<?>>();
		goUnderMouse = new ArrayList<DrawableGO<?>>();
	}

	public void setElement(T element) {
		super.setElement(element);
		sceneElements.clear();
		for (EAdSceneElement sceneElement : element
				.getSceneElements()) {
			SceneElementGO<?> go = sceneElementFactory
					.get(sceneElement);
			sceneElements.add(go);
		}
	}

	/**
	 * Sets a comparator to automatically reorder the scene elements (modifies
	 * drawing order)
	 * 
	 * @param comparator
	 */
	public void setComparator(Comparator<DrawableGO<?>> comparator) {
		this.comparator = comparator;
	}

	@Override
	public List<DrawableGO<?>> getContainedDrawables() {
		return sceneElements;
	}

	@Override
	public boolean contains(int x, int y) {
		if (super.contains(x, y)) {
			return true;
		}

		for (DrawableGO<?> go : sceneElements) {
			if (go.contains(x, y))
				return true;
		}
		return false;
	}

	@Override
	public DrawableGO<?> processAction(InputAction<?> action) {
		if (action instanceof MouseInputAction) {
			int i = sceneElements.size() - 1;
			MouseInputAction m = (MouseInputAction) action;
			int x = m.getVirtualX();
			int y = m.getVirtualY();
			while (i >= 0) {
				DrawableGO<?> go = sceneElements.get(i);
				if (go.contains(x, y)) {
					return go.processAction(action);
				}
				i--;
			}
		}
		return super.processAction(action);
	}

	@Override
	public void update() {
		super.update();
		for (DrawableGO<?> go : this.sceneElements) {
			go.update();
		}

		// Reorder list
		if (comparator != null) {
			Collections.sort(sceneElements, comparator);
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

	private void updateDimensions(boolean updateWidth,
			boolean updateHeight) {
		int minX = Integer.MAX_VALUE;
		int minY = minX;
		int maxX = Integer.MIN_VALUE;
		int maxY = maxX;
		for (DrawableGO<?> go : sceneElements) {
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
		for (DrawableGO<?> sceneElement : sceneElements) {
			gui.addElement(sceneElement, transformation);
		}
	}

	@Override
	public List<AssetDescriptor> getAssets(
			List<AssetDescriptor> assetList, boolean allAssets) {
		for (DrawableGO<?> sceneElement : sceneElements) {
			assetList = sceneElement.getAssets(assetList, allAssets);
		}
		return assetList;
	}

	@Override
	public void collectSceneElements(List<EAdSceneElement> elements) {
		super.collectSceneElements(elements);
		for (EAdSceneElement e : element.getSceneElements()) {
			elements.add(e);
		}

	}

	@Override
	public DrawableGO<?> getFirstGOIn(int x, int y) {
		for (int i = sceneElements.size() - 1; i >= 0; i--) {
			DrawableGO<?> tempGameObject = sceneElements.get(i);
			if (tempGameObject.contains(x, y)) {
				return tempGameObject;
			}
		}
		return contains(x, y) ? this : null;
	}

	@Override
	public List<DrawableGO<?>> getAllGOIn(int x, int y) {
		goUnderMouse.clear();
		for (DrawableGO<?> go : sceneElements) {
			float[] mouse = go.getTransformation().getMatrix()
					.multiplyPointInverse(x, y, true);
			if (go.contains((int) mouse[0], (int) mouse[1]))
				goUnderMouse.add(go);
		}
		return goUnderMouse;
	}
}
