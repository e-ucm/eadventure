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

package ead.engine.core.gameobjects.effects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.EAdElement;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.scene.EAdScene;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.transitions.TransitionGO;
import ead.engine.core.gameobjects.go.transitions.TransitionGO.TransitionListener;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.TransitionFactory;
import ead.engine.core.platform.assets.AssetHandler;

public class ChangeSceneGO extends AbstractEffectGO<ChangeSceneEf> implements
		TransitionListener {

	private static final Logger logger = LoggerFactory
			.getLogger("ChangeSceneGO");

	private TransitionFactory transitionFactory;

	private TransitionGO<?> transition;

	private boolean end;

	private boolean firstFinish;

	@Inject
	public ChangeSceneGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, TransitionFactory transitionFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState);
		this.transitionFactory = transitionFactory;
	}

	@Override
	public void initialize() {
		firstFinish = true;
		super.initialize();
		end = false;
		// If the effect is to a different scene
		if (element.getNextScene() == null
				|| element.getNextScene() != gameState.getScene().getElement()) {
			transition = transitionFactory.getTransition(element
					.getTransition());
			transition.getTransitionListeners().add(this);
			EAdElement e = element.getNextScene();
			if (e != null) {
				EAdElement finalElement = gameState.getValueMap()
						.getFinalElement(e);
				if (finalElement instanceof EAdScene) {
					transition.setNext((EAdScene) finalElement);
				} else {
					logger.warn("Element in change scene is not an EAdScene. Returning to previous scene.");
					transition.setNext(gameState.getPreviousScene());
				}
			} else {
				transition.setNext(gameState.getPreviousScene());
			}
			transition.setPrevious(gameState.getScene());
			gameState.setScene(transition);
		}
		else {
			// Execute post effects
			end = true;
		}
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return end;
	}

	@Override
	public void transitionBegins() {

	}

	@Override
	public void transitionEnds() {
		end = true;
		finish();
	}

	public void finish() {
		if (firstFinish) {
			firstFinish = false;
			super.finish();
		}
	}

}
