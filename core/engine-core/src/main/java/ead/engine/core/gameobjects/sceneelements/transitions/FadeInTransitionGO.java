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

package ead.engine.core.gameobjects.sceneelements.transitions;

import com.google.inject.Inject;

import ead.common.model.elements.transitions.FadeInTransition;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class FadeInTransitionGO extends TransitionGO<FadeInTransition> {

	private float sceneAlpha;

	private int currentTime;

	private SceneGO nextScene;

	private boolean first;

	@Inject
	public FadeInTransitionGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory);
		currentTime = 0;
	}

	@Override
	public void setPreviousScene(SceneGO scene) {
		super.setPreviousScene(scene);
		currentTime = 0;
		sceneAlpha = 0;
		nextScene = null;
		first = true;
	}

	public void act(float delta) {
		if (nextScene != null) {

			if (first) {
				nextScene.setAlpha(0);
				nextScene.setX(0);
				nextScene.setY(0);
				addActor(nextScene);
				first = false;
			}

			currentTime += gui.getSkippedMilliseconds();
			sceneAlpha = currentTime / transition.getTime();
			nextScene.setAlpha(sceneAlpha);
		}

		if (currentTime >= transition.getTime()) {
			gui.setScene(nextScene);
			nextScene.act(delta);
		} else {
			super.act(delta);
		}
	}

	@Override
	public void transition(SceneGO nextScene) {
		this.nextScene = nextScene;
	}

}
