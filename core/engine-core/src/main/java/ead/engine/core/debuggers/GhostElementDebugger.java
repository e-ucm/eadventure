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

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.scenes.EAdGhostElement;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.util.EAdPosition;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;

public class GhostElementDebugger implements Debugger {

	private GameState gameState;

	private EAdScene currentScene;

	private List<SceneElementGO<?>> drawables;

	private SceneElementGOFactory factory;

	@Inject
	public GhostElementDebugger(GameState gameState,
			SceneElementGOFactory factory) {
		this.gameState = gameState;
		this.factory = factory;
		drawables = new ArrayList<SceneElementGO<?>>();
	}

	@Override
	public List<SceneElementGO<?>> getGameObjects() {
		//		EAdScene newScene = gameState.getScene().getElement();
		EAdScene newScene = null;
		if (currentScene != newScene) {
			currentScene = newScene;
			drawables.clear();
			for (EAdSceneElement e : currentScene.getSceneElements()) {
				if (e instanceof EAdGhostElement) {
					SceneElement area = new SceneElement(((EAdGhostElement) e)
							.getInteractionArea());
					area.setInitialEnable(false);
					SceneElementGO<?> go = factory.get(e);
					area.setPosition(new EAdPosition(go.getPosition()));
					drawables.add(factory.get(area));
				}
			}
		}
		return drawables;
	}

	@Override
	public void setUp(EAdAdventureModel model) {

	}

}
