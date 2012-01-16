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

package ead.engine.core.gameobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.inject.Inject;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.AssetDescriptor;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGOImpl;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdTransformation;

public class SceneGOImpl extends SceneElementGOImpl<EAdScene> implements
		SceneGO<EAdScene> {
	
	private final ElementComparator comparator = new ElementComparator();
	
	private ArrayList<EAdSceneElement> orderedElements;

	@Inject
	public SceneGOImpl(AssetHandler assetHandler, StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui, GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, eventFactory);
		orderedElements = new ArrayList<EAdSceneElement>();
	}

	public void doLayout(EAdTransformation transformation) {
		super.doLayout(transformation);
		gui.addElement(sceneElementFactory.get(element.getBackground()),
				transformation);
		sortElements();
		for ( EAdSceneElement e: orderedElements ){
			gui.addElement(sceneElementFactory.get(e), transformation);
		}

	}

	private void sortElements() {
		orderedElements.clear();
		for ( EAdSceneElement e: element.getComponents() ){
			orderedElements.add(e);
		}
		Collections.sort(orderedElements, comparator);
	}
	
	private class ElementComparator implements Comparator<EAdSceneElement> {

		@Override
		public int compare(EAdSceneElement o1, EAdSceneElement o2) {
			int z1 = gameState.getValueMap().getValue(o1, SceneElementImpl.VAR_Z);
			int z2 = gameState.getValueMap().getValue(o2, SceneElementImpl.VAR_Z);
			return z1 - z2;
		}

		
		
	}

	@Override
	public void update() {
		super.update();
		sceneElementFactory.get(element.getBackground()).update();
		for (EAdSceneElement sceneElement : element.getComponents())
			sceneElementFactory.get(sceneElement).update();
	}

	@Override
	public List<AssetDescriptor> getAssets(List<AssetDescriptor> assetList,
			boolean allAssets) {
		if (element != null) {
			EAdSceneElement background = element.getBackground();
			SceneElementGO<?> gameObject = sceneElementFactory.get(background);
			assetList = gameObject.getAssets(assetList, allAssets);
			for (EAdSceneElement sceneElement : element.getComponents())
				assetList = sceneElementFactory.get(sceneElement).getAssets(
						assetList, allAssets);
			return assetList;
		}
		return null;
	}

	@Override
	public boolean processAction(InputAction<?> action) {
		return false;
	}

}
