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

package ead.engine.core.gameobjects.go.transitions;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.transitions.EAdTransition;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.ComplexSceneElementGOImpl;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.go.transitions.sceneloaders.SceneLoader;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.util.EAdTransformation;

public abstract class AbstractTransitionGO<T extends EAdTransition> extends
		ComplexSceneElementGOImpl<T> implements TransitionGO<T> {

	protected InputHandler inputHandler;

	protected T transition;

	protected SceneGO<?> previousScene;

	protected EAdScene nextScene;

	protected SceneGO<?> nextSceneGO;

	protected SceneLoader sceneLoader;

	private boolean loading;

	private boolean loaded;

	private ArrayList<TransitionListener> listeners;

	private boolean firstUpdateNext;

	private boolean firstUpdatePrevious;

	public AbstractTransitionGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			SceneLoader sceneLoader, InputHandler inputHandler) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory);
		this.sceneLoader = sceneLoader;
		this.inputHandler = inputHandler;
		listeners = new ArrayList<TransitionListener>();

	}

	public void setPrevious(SceneGO<?> scene) {
		this.previousScene = scene;
		firstUpdatePrevious = true;
		for (TransitionListener l : this.getTransitionListeners()) {
			l.transitionBegins();
		}
	}

	@Override
	public void sceneLoaded(SceneGO<?> sceneGO) {
		nextSceneGO = sceneGO;
		loaded = true;
		loading = false;
	}

	@Override
	public boolean isLoadedNextScene() {
		return loaded;
	}

	@Override
	public boolean isFinished() {
		return loaded;
	}

	@Override
	public void setNext(EAdScene scene) {
		nextScene = scene;
		loading = false;
		firstUpdateNext = true;
	}

	public void update() {
		super.update();
		if (!loaded && loading) {
			sceneLoader.step();
		}

		if (!loaded && !loading) {
			loading = true;
			sceneLoader.loadScene(nextScene, this);
		}

		if (firstUpdatePrevious) {
			// Necessary to maintain consistency
			firstUpdatePrevious = false;
			previousScene.update();
			gameState.clearEffects(false);
		}

		if (loaded && firstUpdateNext) {
			inputHandler.clearAllInputs();

			firstUpdateNext = false;
			nextSceneGO.update();
		}

		if (isFinished()) {
			sceneLoader.freeUnusedAssets(nextSceneGO, previousScene);
			gameState.setScene(nextSceneGO);
			for (TransitionListener l : this.getTransitionListeners()) {
				l.transitionEnds();
			}
		}
	}

	@Override
	public void doLayout(EAdTransformation transformation) {
		if (!isFinished())
			gui.addElement(previousScene, transformation);
	}

	public List<TransitionListener> getTransitionListeners() {
		return listeners;
	}

	public SceneGO<?> getNextSceneGO() {
		return nextSceneGO;
	}

	@Override
	public DrawableGO<?> processAction(InputAction<?> action) {
		return this;
	}

}
