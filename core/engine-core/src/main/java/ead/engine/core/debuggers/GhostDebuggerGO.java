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

package ead.engine.core.debuggers;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.GhostElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGOImpl;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class GhostDebuggerGO extends SceneElementGOImpl {

	private SceneGO currentScene;

	private List<SceneElementGO<?>> currentSceneElements;

	@Inject
	public GhostDebuggerGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, sceneElementFactory, gui, gameState, eventFactory);
		currentSceneElements = new ArrayList<SceneElementGO<?>>();
	}

	public void update() {
		if (this.currentScene != gui.getScene()) {
			currentScene = gui.getScene();
			for (SceneElementGO<?> go : currentSceneElements) {
				go.remove();
			}
			if (currentScene != null) {
				for (SceneElementGO<?> go : currentScene.getChildren()) {
					if (go instanceof GhostElementGO) {
						GhostElementGO ghost = (GhostElementGO) go;
						GhostElement ghostElement = (GhostElement) ghost
								.getElement();
						EAdDrawable interactionArea = ghostElement
								.getInteractionArea();
						if (interactionArea != null) {
							SceneElement element = new SceneElement(
									interactionArea);
							element.setInitialEnable(false);
							SceneElementGO<?> elementGO = sceneElementFactory
									.get(element);
							ghost.addSceneElement(elementGO);
							currentSceneElements.add(elementGO);
						}
					}
				}
			}
		}
	}

}