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

import ead.common.model.elements.operations.SystemFields;
import ead.common.model.elements.transitions.DisplaceTransition;
import ead.common.model.elements.transitions.enums.DisplaceTransitionType;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneGO;

public class DisplaceTransitionGO extends TransitionGO<DisplaceTransition> {

	private int width;

	private int height;

	private int x1, x2, y1, y2;

	private int currentTime;

	@Inject
	public DisplaceTransitionGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory);
		width = gameState.getValue(SystemFields.GAME_WIDTH);
		height = gameState.getValue(SystemFields.GAME_HEIGHT);
		currentTime = 0;
	}

	public void act(float delta) {
		if (nextScene != null) {
			currentTime += gui.getSkippedMilliseconds();

			float dispX = getDisp(true, currentTime);
			float dispY = getDisp(false, currentTime);

			if (dispX != 0.0f) {
				x1 = ((int) (dispX * -width));
				x2 = ((int) ((1 - dispX) * width));
				if (transition.getForward()) {
					x1 = -x1;
					x2 = (int) (dispX * width) - width;
				}
			}

			if (dispY != 0.0f) {
				y1 = ((int) (dispY * -height));
				y2 = ((int) ((1 - dispY) * height));

				if (transition.getForward()) {
					y1 = -y1;
					y2 = (int) (dispY * height) - height;
				}
			}

			nextScene.setX(x2);
			nextScene.setY(y2);
			previousScene.setX(x1);
			previousScene.setY(y1);
		}

		if (currentTime >= transition.getTime()) {
			nextScene.setX(0);
			nextScene.setY(0);
			super.finish();
		} else {
			super.act(delta);
		}
	}

	private float getDisp(boolean horizontal, int currentTime) {
		if ((horizontal && transition.getType() == DisplaceTransitionType.HORIZONTAL)
				|| (!horizontal && transition.getType() == DisplaceTransitionType.VERTICAL)) {
			float value = (float) currentTime / (float) transition.getTime();
			return value;

		} else
			return 0;

	}

	@Override
	public void transition(SceneGO previousScene, SceneGO nextScene,
			TransitionListener l) {
		super.transition(previousScene, nextScene, l);
		setPreviousScene(previousScene);
		addSceneElement(previousScene);
		addSceneElement(nextScene);
	}

	public void setPreviousScene(SceneGO scene) {
		currentTime = 0;
		x1 = x2 = y1 = y2 = 0;
	}
}
