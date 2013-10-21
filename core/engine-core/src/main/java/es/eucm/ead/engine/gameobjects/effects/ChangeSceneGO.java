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

package es.eucm.ead.engine.gameobjects.effects;

import com.google.inject.Inject;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.GameLoader;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;
import es.eucm.ead.engine.gameobjects.sceneelements.transitions.TransitionGO;
import es.eucm.ead.engine.gameobjects.sceneelements.transitions.sceneloaders.SceneLoader;
import es.eucm.ead.engine.gameobjects.sceneelements.transitions.sceneloaders.SceneLoaderListener;
import es.eucm.ead.engine.tracking.GameTracker;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.params.variables.VarDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeSceneGO extends AbstractEffectGO<ChangeSceneEf> implements
		SceneLoaderListener, TransitionGO.TransitionListener {

	static private Logger logger = LoggerFactory.getLogger(ChangeSceneGO.class);

	public static ElementField<Boolean> IN_TRANSITION = new ElementField<Boolean>(
			null, new VarDef<Boolean>("in_transition", Boolean.class, false));
	private final GameLoader gameLoader;

	private GUI gui;

	private SceneLoader sceneLoader;

	private SceneElementFactory sceneElementFactory;

	private TransitionGO<?> transition;

	private SceneGO previousScene;

	private SceneGO nextScene;

	private boolean finished;

	private GameTracker gameTracker;

	@Inject
	public ChangeSceneGO(SceneElementFactory sceneElementFactory,
			SceneLoader sceneLoader, GameLoader gameLoader, Game game,
			GameTracker gameTracker) {
		super(game);
		this.sceneLoader = sceneLoader;
		this.gui = this.game.getGUI();
		this.sceneElementFactory = sceneElementFactory;
		this.gameLoader = gameLoader;
		this.gameTracker = gameTracker;
	}

	@Override
	public void initialize() {
		super.initialize();
		game.getGameState().setValue(IN_TRANSITION, true);
		finished = false;
		String nextSceneId = effect.getNextSceneId();
		Scene nextScene;

		// if null, return to previous scene
		if (nextSceneId == null) {
			nextScene = gui.getPreviousScene();
		} else {
			nextScene = gameLoader.loadScene(nextSceneId);
		}

		// If next scene is different from current one
		if (nextScene != gui.getScene().getElement()) {
			previousScene = gui.getScene();
			transition = (TransitionGO<?>) sceneElementFactory.get(effect
					.getTransition());

			logger.debug("Transition {} -> {}", new Object[] { previousScene,
					nextScene });
			gameTracker.phaseEnd(previousScene.getName());
			gameTracker.phaseStart(nextScene.getId());

			sceneLoader.loadScene(nextScene, this);
			gui.setScene(transition);
		} else {
			finished = true;
		}
		// We remove any remaining non-persistent effect in the scene
		game.clearEffects(false);
	}

	@Override
	public void sceneLoaded(SceneGO sceneGO) {
		transition.transition(previousScene, sceneGO, this);
		this.nextScene = sceneGO;
	}

	public void act(float delta) {
		sceneLoader.step();
	}

	public boolean isQueueable() {
		return true;
	}

	public boolean isBlocking() {
		return true;
	}

	public boolean isFinished() {
		return finished;
	}

	@Override
	public void transitionEnded() {
		game.getGameState().setValue(IN_TRANSITION, false);
		gui.setScene(nextScene);
		nextScene.setPosition(0, 0);
		nextScene.setAlpha(1);
		nextScene.setZ(0);
		finished = true;
		transition.removeActor(nextScene);
		transition.removeActor(previousScene);
		previousScene.free();
		transition.free();
	}

}
