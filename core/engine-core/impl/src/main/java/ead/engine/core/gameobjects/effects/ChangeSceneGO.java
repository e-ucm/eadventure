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

import com.google.inject.Inject;

import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.util.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.transitions.TransitionGO;
import ead.engine.core.gameobjects.go.transitions.TransitionGO.TransitionListener;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.TransitionFactory;

public class ChangeSceneGO extends AbstractEffectGO<ChangeSceneEf> implements TransitionListener {

	private TransitionFactory transitionFactory;
	
	private TransitionGO<?> transition;
	
	private boolean end;

	@Inject
	public ChangeSceneGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, TransitionFactory transitionFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState);
		this.transitionFactory = transitionFactory;
	}

	@Override
	public void initialize() {
		super.initialize();
		end = false;
		if (element.getNextScene() == null || element.getNextScene() != gameState.getScene().getElement()) {
			transition = transitionFactory
					.getTransition(element.getTransition());
			transition.getTransitionListeners().add(this);
			if (element.getNextScene() != null)
				transition.setNext(element.getNextScene());
			else
				transition.setNext(gameState.getPreviousScene());
			transition.setPrevious(gameState.getScene());
			

			gameState.setScene(transition);
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
	}

}
