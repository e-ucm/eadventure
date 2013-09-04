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

package es.eucm.ead.engine.gameobjects.sceneelements.transitions;

import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.transitions.DisplaceTransition;
import es.eucm.ead.model.elements.transitions.enums.DisplaceTransitionType;

public class DisplaceTransitionGO extends TransitionGO<DisplaceTransition> {

	private int width;

	private int height;

	private int x1, x2, y1, y2;

	private int currentTime;

	@Inject
	public DisplaceTransitionGO(AssetHandler assetHandler,
			SceneElementFactory gameObjectFactory, Game game,
			EventFactory eventFactory) {
		super(assetHandler, gameObjectFactory, game, eventFactory);
		width = gameState.getValue(SystemFields.GAME_WIDTH);
		height = gameState.getValue(SystemFields.GAME_HEIGHT);
		currentTime = 0;
	}

	public void act(float delta) {
		if (nextScene != null) {
			currentTime += game.getSkippedMilliseconds();

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
			if (nextScene != null) {
				nextScene.setX(0);
				nextScene.setY(0);
			}
			previousScene.setX(0);
			previousScene.setY(0);
			super.finish();
		} else {
			super.act(delta);
		}
	}

	private float getDisp(boolean horizontal, int currentTime) {
		if ((horizontal && transition.getType() == DisplaceTransitionType.HORIZONTAL)
				|| (!horizontal && transition.getType() == DisplaceTransitionType.VERTICAL)) {
			return (float) currentTime / (float) transition.getTime();

		} else
			return 0;

	}

	@Override
	public void transition(SceneGO previousScene, SceneGO nextScene,
			TransitionListener l) {
		currentTime = 0;
		x1 = x2 = y1 = y2 = 0;
		super.transition(previousScene, nextScene, l);
		addSceneElement(previousScene);
		addSceneElement(nextScene);
	}
}
