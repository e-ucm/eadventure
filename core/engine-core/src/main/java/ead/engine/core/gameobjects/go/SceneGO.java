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

import java.util.Comparator;
import java.util.List;

import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.resources.assets.AssetDescriptor;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.util.EAdTransformation;

public class SceneGO extends ComplexSceneElementGOImpl<EAdScene>
		implements Comparator<SceneElementGO<?>> {

	public SceneGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EvaluatorFactory evaluatorFactory,
			EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState,
				evaluatorFactory, eventFactory);
		this.setComparator(this);
	}

	public void doLayout(EAdTransformation transformation) {
		gui.addElement(
				sceneElementFactory.get(element.getBackground()),
				transformation);
		super.doLayout(transformation);
	}

	@Override
	public int compare(SceneElementGO<?> o1, SceneElementGO<?> o2) {
		int z1 = gameState.getValueMap().getValue(o1.getElement(),
				SceneElement.VAR_Z);
		int z2 = gameState.getValueMap().getValue(o2.getElement(),
				SceneElement.VAR_Z);
		return z1 - z2;
	}

	@Override
	public void update() {
		sceneElementFactory.get(element.getBackground()).update();
		super.update();
	}

	@Override
	public List<AssetDescriptor> getAssets(
			List<AssetDescriptor> assetList, boolean allAssets) {
		if (element != null) {
			EAdSceneElement background = element.getBackground();
			SceneElementGO<?> gameObject = sceneElementFactory
					.get(background);
			assetList = gameObject.getAssets(assetList, allAssets);
			for (EAdSceneElement sceneElement : element
					.getSceneElements())
				assetList = sceneElementFactory.get(sceneElement)
						.getAssets(assetList, allAssets);
			return assetList;
		}
		return null;
	}

	@Override
	public DrawableGO<?> processAction(InputAction<?> action) {
		DrawableGO<?> go = super.processAction(action);
		if (go == null) {
			return sceneElementFactory.get(element.getBackground())
					.processAction(action);
		} else {
			return go;
		}
	}

	@Override
	public void collectSceneElements(List<EAdSceneElement> elements) {
		super.collectSceneElements(elements);
		for (EAdSceneElement e : element.getSceneElements()) {
			elements.add(e);
		}

	}

}
