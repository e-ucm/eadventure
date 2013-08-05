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

import es.eucm.ead.engine.game.interfaces.GameState;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.transitions.ScaleTransition;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;

public class ScaleTransitionGO extends TransitionGO<ScaleTransition> {

	private boolean growing;

	private float scale;

	private float oldX, oldY, oldDispX, oldDispY;

	@Inject
	public ScaleTransitionGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory);
	}

	public void setElement(EAdSceneElement e) {
		super.setElement(e);
		growing = transition.isGrow();
		scale = growing ? 0 : 1;
	}

	public void transition(SceneGO previousScene, SceneGO nextScene,
			TransitionListener transitionListener) {
		super.transition(previousScene, nextScene, transitionListener);
		addActor(nextScene);
		nextScene.setZ(transition.isTargetNext() ? 100 : 50);
		previousScene.setZ(transition.isTargetNext() ? 50 : 100);
		SceneElementGO scaling = (transition.isTargetNext() ? nextScene
				: previousScene);
		oldX = scaling.getRelativeX();
		oldY = scaling.getRelativeY();
		oldDispX = scaling.getDispX();
		oldDispY = scaling.getDispY();

		float centerX = scaling.getCenterX();
		float centerY = scaling.getCenterY();
		scaling.setDispX(0.5f);
		scaling.setDispY(0.5f);
		scaling.setX(centerX);
		scaling.setY(centerY);
	}

	public void act(float delta) {
		if (nextScene != null) {
			float step = 1.0f * (delta / (float) transition.getTime());
			scale += (growing ? 1 : -1) * step;
			if (transition.isTargetNext()) {
				nextScene.setScale(scale);
			} else {
				previousScene.setScale(scale);
			}
			if ((growing && scale >= 1) || (!growing && scale <= 0)) {
				previousScene.setScale(1);
				nextScene.setScale(1);
				SceneElementGO scaling = (transition.isTargetNext() ? nextScene
						: previousScene);
				scaling.setDispX(oldDispX);
				scaling.setDispY(oldDispY);
				scaling.setX(oldX);
				scaling.setY(oldY);
				super.finish();
			}
			super.act(delta);
		}
	}

}
