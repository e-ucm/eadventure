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

package ead.engine.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.inject.Inject;
import ead.common.model.elements.huds.BottomHud;
import ead.common.model.elements.huds.MouseHud;
import ead.common.model.elements.operations.SystemFields;
import ead.common.model.elements.predef.LoadingScreen;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public abstract class GUIImpl implements GUI {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger("AbstractGUI");

	protected Game game;

	protected GameState gameState;

	protected SceneElementGO root;

	protected SceneElementGO hudRoot;

	protected SceneElementGO sceneRoot;

	/**
	 * Stack with all visited scenes
	 */
	private Stack<EAdScene> previousSceneStack;

	/**
	 * Current scene
	 */
	private SceneGO scene;

	private EAdScene loadingScreen;

	private SceneElementGOFactory sceneElementFactory;

	private float scaleX;

	private float scaleY;

	@Inject
	public GUIImpl() {
		super();
		logger.info("Created GUI");
		previousSceneStack = new Stack<EAdScene>();
	}

	public void setScale(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public void initialize(final Game game, GameState gameState,
			SceneElementGOFactory sceneElementFactory) {
		this.loadingScreen = new LoadingScreen();
		this.game = game;
		this.gameState = gameState;
		this.sceneElementFactory = sceneElementFactory;
		reset();
	}

	public void addHierarchy() {
		root = sceneElementFactory.get(new GroupElement());
		root.getElement().setId("#engine.root");
		root.setScale(scaleX, scaleY);
		hudRoot = sceneElementFactory.get(new GroupElement());
		hudRoot.getElement().setId("#engine.huds");
		sceneRoot = sceneElementFactory.get(new GroupElement());
		sceneRoot.getElement().setId("#engine.sceneContainer");
		root.addSceneElement(sceneRoot);
		root.addSceneElement(hudRoot);
		// Bottom hud
		hudRoot.addSceneElement(sceneElementFactory.get(new BottomHud()));
		// Effects hud
		SceneElement effectsHud = new SceneElement();
		effectsHud.setId(GUI.EFFECTS_HUD_ID);
		hudRoot.addSceneElement(sceneElementFactory.get(effectsHud));
		// Debugger hud
		SceneElement debuggerHud = new SceneElement();
		debuggerHud.setId(GUI.DEBBUGERS_HUD_ID);
		hudRoot.addSceneElement(sceneElementFactory.get(debuggerHud));
		// Add huds
		hudRoot.addSceneElement(sceneElementFactory.get(new MouseHud()));
	}

	@Override
	public EAdScene getPreviousScene() {
		if (previousSceneStack.isEmpty()) {
			return null;
		}
		return previousSceneStack.pop();
	}

	@Override
	public void addHud(SceneElementGO hud) {
		hudRoot.addSceneElement(hud);
	}

	@Override
	public void removeHUD(SceneElementGO hud) {
		hud.remove();
	}

	public SceneElementGO getHUD(String id) {
		SceneElementGO a = (SceneElementGO) hudRoot.findActor(id);
		if (a == null) {
			logger.warn("Hud with id {} is not present", id);
		}
		return a;
	}

	public SceneElementGO getGameObjectIn(int x, int y, boolean touchable) {
		return root.getFirstGOIn(x, y, touchable);
	}

	@Override
	public SceneGO getScene() {
		if (scene == null) {
			logger.debug("null scene, Loading screen: "
					+ (loadingScreen != null));
			this.scene = (SceneGO) sceneElementFactory.get(loadingScreen);
			previousSceneStack.push(loadingScreen);
		}
		return scene;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.GameState#setScene(es.eucm.eadventure.
	 * engine.core.gameobjects.SceneGO)
	 */
	@Override
	public void setScene(SceneGO newScene) {
		// We remove any remaining non-persistent effect in the scene
		gameState.clearEffects(false);

		// We add the scene to stack if it is returnable
		if (this.scene != null
				&& this.scene.getElement() != null
				&& scene.getReturnable()
				&& (previousSceneStack.isEmpty() || previousSceneStack.peek() != scene
						.getElement())) {
			previousSceneStack.push((EAdScene) scene.getElement());
		}
		// Set the scene
		this.scene = newScene;
		gameState.setValue(SystemFields.CURRENT_SCENE,
				scene.getElement() == null ? null : scene.getElement().getId());
		sceneRoot.getChildren().clear();
		sceneRoot.addSceneElement(scene);
	}

	public SceneElementGO getSceneElement(EAdSceneElement element) {
		return sceneElementFactory.get(element);
	}

	public SceneElementGO getRoot() {
		return root;
	}

	public SceneElementGO getGameObjectUnderPointer() {
		Actor a = root.hit(gameState.getValue(SystemFields.MOUSE_X), gameState
				.getValue(SystemFields.MOUSE_Y), true);
		if (a instanceof SceneElementGO) {
			return (SceneElementGO) a;
		}
		return null;
	}

	@Override
	public int getSkippedMilliseconds() {
		return gameState.isPaused() ? 0
				: (int) (Gdx.graphics.getDeltaTime() * 1000);
	}

	@Override
	public int getTicksPerSecond() {
		return Gdx.graphics.getFramesPerSecond();
	}

	@Override
	public void finish() {
		if (gameState.getValue(SystemFields.EXIT_WHEN_CLOSE)) {
			Gdx.app.exit();
		}
	}

	public void reset() {
		previousSceneStack.clear();
		sceneElementFactory.clean();
		addHierarchy();
	}

}
