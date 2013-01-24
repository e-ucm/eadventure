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

import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.huds.MouseHud;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.guievents.enums.KeyEventType;
import ead.common.model.params.guievents.enums.KeyGEvCode;
import ead.common.model.params.variables.EAdVarDef;
import ead.common.model.params.variables.SystemFields;
import ead.engine.core.debuggers.DebuggersHandler;
import ead.engine.core.debuggers.DebuggersHandlerImpl;
import ead.engine.core.factories.GameObjectFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameImpl;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.InputActionProcessor;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;

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

	@SuppressWarnings("rawtypes")
	protected GenericCanvas eAdCanvas;

	protected Game game;

	protected GameState gameState;

	protected SceneElementGO<?> root;

	protected SceneElementGO<?> hudRoot;

	protected SceneElementGO<?> sceneRoot;

	/**
	 * Stack with all visited scenes
	 */
	private Stack<EAdScene> previousSceneStack;

	/**
	 * Current scene
	 */
	private SceneGO scene;

	private EAdScene loadingScreen;

	private EAdTransformation initialTransformation;

	private GameObjectFactory<EAdSceneElement, SceneElementGO<? extends EAdSceneElement>> sceneElementFactory;

	public AbstractGUI(GenericCanvas<T> canvas) {
		logger.info("Created abstract GUI");
		this.eAdCanvas = canvas;
		previousSceneStack = new Stack<EAdScene>();
	}

	public void initialize(Game game, GameState gameState,
			SceneElementGOFactory sceneElementFactory,
			InputHandler inputHandler, final DebuggersHandler debuggerHandler) {
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
		root.setInputProcessor(new InputActionProcessor() {

			@Override
			public SceneElementGO<?> processAction(InputAction<?> action) {
				if (action instanceof KeyInputAction) {
					KeyInputAction k = (KeyInputAction) action;
					if (k.getKeyCode().equals(KeyGEvCode.F1)
							&& k.getType().equals(KeyEventType.KEY_PRESSED)) {
						debuggerHandler
								.toggleDebugger(DebuggersHandlerImpl.TRAJECTORY_DEBUGGER);
					}
					action.consume();
					return root;
				}
				return null;
			}

		});
		root.addSceneElement(hudRoot);
		updateInitialTransformation();
		inputHandler.setInitialTransformation(initialTransformation);
	}

	public void setUp() {
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
		return previousSceneStack.pop();
	}

	@Override
	public void addHud(SceneElementGO<?> hud) {
		hudRoot.addSceneElement(hud);
	}

	@Override
	public void removeHUD(SceneElementGO<?> hud) {
		hudRoot.removeSceneElement(hud);
	}

	@Override
	public List<SceneElementGO<?>> getHUDs() {
		return hudRoot.getChildren();
	}

	public SceneElementGO<?> getHUD(String id) {
		for (SceneElementGO<?> hud : hudRoot.getChildren()) {
			if (hud.getElement().getId().equals(id)) {
				return hud;
			}
		}
		logger.warn("Hud with id {} is not present", id);
		return null;
	}

	/**
	 * Render the game objects into the graphic context
	 * 
	 */
	public void commit() {
		commit(root);
	}

	@SuppressWarnings("unchecked")
	public void commit(SceneElementGO<?> go) {
		EAdTransformation t = go.getTransformation();
		eAdCanvas.setTransformation(t);
		RuntimeDrawable<?, ?> r = go.getDrawable();
		if (r != null) {
			go.getDrawable().render(eAdCanvas);
		}
		for (SceneElementGO<?> g : go.getChildren()) {
			commit(g);
		}
	}

	public void update() {
		root.update();
	}

	public SceneElementGO<?> processAction(InputAction<?> action) {
		SceneElementGO<?> go = null;
		if (action instanceof MouseInputAction) {
			MouseInputAction m = (MouseInputAction) action;
			go = root.getFirstGOIn(m.getVirtualX(), m.getVirtualY());
			if (go != null) {
				go.processAction(action);
			}
		} else if (action instanceof KeyInputAction) {
			KeyInputAction k = (KeyInputAction) action;
			EAdSceneElement element = gameState
					.getValue(SystemFields.ACTIVE_ELEMENT);
			// The active element gets a try to consume it
			if (element != null) {
				go = root.getChild(element);
				if (go != null) {
					go.processAction(k);
				}
			}

			if (!k.isConsumed()) {
				processKeyAction(k, root);
			}

		}
		game.applyFilters(GameImpl.FILTER_PROCESS_ACTION, action, null);
		return go;
	}

	public SceneElementGO<?> processKeyAction(KeyInputAction k,
			SceneElementGO<?> sceneElement) {
		SceneElementGO<?> go = null;
		sceneElement.processAction(k);
		if (!k.isConsumed()) {
			for (SceneElementGO<?> s : sceneElement.getChildren()) {
				go = processKeyAction(k, s);
				if (go != null) {
					break;
				}
			}
		} else {
			go = sceneElement;
		}
		return go;
	}

	public SceneElementGO<?> getGameObjectIn(int x, int y) {
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
		sceneRoot.removeSceneElement(scene);
		this.scene = newScene;
		if (this.scene != null && this.scene.getElement() != null) {
			gameState.setValue(scene.getElement(), BasicScene.VAR_SCENE_LOADED,
					Boolean.TRUE);
			for (Entry<EAdVarDef<?>, Object> e : scene.getElement().getVars()
					.entrySet()) {
				gameState
						.setValue(scene.getElement(), e.getKey(), e.getValue());
			}
		}
		// Add new scene
		sceneRoot.addSceneElement(scene);
	}

	private void updateInitialTransformation() {
		if (initialTransformation != null) {
			initialTransformation.setValidated(true);
		}

		// currentWidth = gameState.getValue(
		// SystemFields.GAME_WIDTH);
		// currentWidth = gameState.getValue(
		// SystemFields.GAME_HEIGHT);
		//
		// float scaleX = currentWidth
		// / (float) adventure.getGameWidth();
		// float scaleY = currentHeight
		// / (float) adventure.getGameHeight();
		//
		// float scale = scaleX < scaleY ? scaleX : scaleY;
		// float dispX = Math.abs(adventure.getGameWidth() * scaleX
		// - adventure.getGameWidth() * scale) / 2;
		// float dispY = Math.abs(adventure.getGameHeight() * scaleY
		// - adventure.getGameHeight() * scale) / 2;
		//
		initialTransformation = new EAdTransformationImpl();
		// initialTransformation.getMatrix().translate(dispX, dispY,
		// true);
		// initialTransformation.getMatrix().scale(scale, scale, true);
		initialTransformation.setValidated(false);

	}

	public SceneElementGO<?> getSceneElement(EAdSceneElement element) {
		return root.getChild(element);
	}

}
