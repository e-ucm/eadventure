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

package ead.engine.core.platform;

import java.util.Map.Entry;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import ead.common.model.elements.huds.MouseHud;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.variables.EAdVarDef;
import ead.common.model.params.variables.SystemFields;
import ead.engine.core.factories.GameObjectFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.debuggers.DebuggersHandler;
import ead.engine.core.gameobjects.debuggers.DebuggersHandlerImpl;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.platform.rendering.GenericCanvas;

/**
 * <p>
 * Abstract implementation of the GUI (Graphic User Interface) for the
 * eAdventure 2 games
 * </p>
 * 
 * @param <T>
 *            A parameter for the graphic context of the GUI (e.g. in AWT Java
 *            it will be Graphics2D)
 */
public abstract class AbstractGUI<T> implements GUI {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger("AbstractGUI");

	protected GenericCanvas eAdCanvas;

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

	private GameObjectFactory<EAdSceneElement, SceneElementGO> sceneElementFactory;

	public AbstractGUI(GenericCanvas canvas) {
		logger.info("Created abstract GUI");
		this.eAdCanvas = canvas;
		previousSceneStack = new Stack<EAdScene>();
	}

	public void initialize(Game game, GameState gameState,
			SceneElementGOFactory sceneElementFactory,
			final DebuggersHandler debuggerHandler) {
		this.loadingScreen = new LoadingScreen();
		this.game = game;
		this.gameState = gameState;
		this.sceneElementFactory = sceneElementFactory;

		eAdCanvas.setWidth(gameState.getValue(SystemFields.GAME_WIDTH));
		eAdCanvas.setHeight(gameState.getValue(SystemFields.GAME_HEIGHT));
		root = sceneElementFactory.get(new GroupElement());
		root.getElement().setId("#engine.root");
		hudRoot = sceneElementFactory.get(new GroupElement());
		hudRoot.getElement().setId("#engine.huds");
		sceneRoot = sceneElementFactory.get(new GroupElement());
		sceneRoot.getElement().setId("#engine.sceneContainer");

		root.addSceneElement(sceneRoot);
		root.setInputProcessor(new InputListener() {

			@Override
			public boolean keyDown(InputEvent event, int keycode) {

				switch (keycode) {
				case Input.Keys.F1:
					debuggerHandler
							.toggleDebugger(DebuggersHandlerImpl.TRAJECTORY_DEBUGGER);
					break;
				case Input.Keys.F2:
					debuggerHandler
							.toggleDebugger(DebuggersHandlerImpl.GHOST_DEBUGGER);
					break;
				case Input.Keys.F3:
					debuggerHandler
							.toggleDebugger(DebuggersHandlerImpl.FIELDS_DEBUGGER);
					break;
				default:
					break;
				}

				return true;
			}

		});
		root.addSceneElement(hudRoot);
		addHuds();
	}

	public void addHuds() {
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

	public SceneElementGO getGameObjectIn(int x, int y) {
		return root.getFirstGOIn(x, y);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setScene(SceneGO newScene) {
		if (this.scene != null && this.scene.getElement() != null) {
			gameState.setValue(scene.getElement(), BasicScene.VAR_SCENE_LOADED,
					Boolean.FALSE);
			if (scene.getReturnable()) {
				previousSceneStack.push((EAdScene) scene.getElement());
			}
		}
		// Remove old scene
		if (scene != null)
			scene.remove();
		this.scene = newScene;
		if (this.scene != null && this.scene.getElement() != null) {
			gameState.setValue(scene.getElement(), BasicScene.VAR_SCENE_LOADED,
					Boolean.TRUE);
			for (Entry<EAdVarDef<?>, Object> e : scene.getElement().getVars()
					.entrySet()) {
				EAdVarDef def = e.getKey();
				gameState.setValue(scene.getElement(), def, e.getValue());
			}
		}
		// Add new scene
		sceneRoot.addSceneElement(scene);
	}

	public SceneElementGO getSceneElement(EAdSceneElement element) {
		return root.getChild(element);
	}

	public SceneElementGO getRoot() {
		return root;
	}

	public SceneElementGO getGameObjectUnderPointer() {
		return (SceneElementGO) root.hit(
				gameState.getValue(SystemFields.MOUSE_SCENE_X),
				gameState.getValue(SystemFields.MOUSE_SCENE_Y), true);
	}

}
