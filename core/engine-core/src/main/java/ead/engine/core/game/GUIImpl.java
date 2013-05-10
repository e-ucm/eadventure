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

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
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
import ead.engine.core.gameobjects.debuggers.DebuggersHandler;
import ead.engine.core.gameobjects.debuggers.DebuggersHandlerImpl;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneGO;

public abstract class GUIImpl implements GUI {

	public static final boolean DEBUG = true;

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

	private DebuggersHandler debuggerHandler;

	@Inject
	public GUIImpl() {
		super();
		logger.info("Created GUI");
		previousSceneStack = new Stack<EAdScene>();
	}

	public void initialize(final Game game, GameState gameState,
			SceneElementGOFactory sceneElementFactory,
			DebuggersHandler debuggerHandler) {
		this.loadingScreen = new LoadingScreen();
		this.game = game;
		this.gameState = gameState;
		this.sceneElementFactory = sceneElementFactory;
		this.debuggerHandler = debuggerHandler;
		addHierarchy();
	}

	public void addHierarchy() {
		root = sceneElementFactory.get(new GroupElement());
		root.getElement().setId("#engine.root");
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
		addDebug();
	}

	private void addDebug() {
		if (DEBUG) {
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
					case Input.Keys.F4:
						debuggerHandler
								.toggleDebugger(DebuggersHandlerImpl.CHANGE_SCENE_DEBUGGER);
						break;
					case Input.Keys.F5:
						debuggerHandler
								.toggleDebugger(DebuggersHandlerImpl.MODEL_FIELDS_DEBUGGER);
						break;
					case Input.Keys.F6:
						debuggerHandler
								.toggleDebugger(DebuggersHandlerImpl.PROFILER_DEBUGGER);
						break;
					case Input.Keys.F7:
						game.restart();
					default:
						break;
					}

					return true;
				}

			}, false);
		}
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
		return root.getChild(element);
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
		Gdx.app.exit();
	}

	public void reset() {
		previousSceneStack.clear();
		sceneElementFactory.clean();
		addHierarchy();
		setScene((SceneGO) sceneElementFactory.get(loadingScreen));
	}

	public class DragSource extends Source {

		public DragSource(SceneElementGO sceneElement) {
			super(sceneElement);
		}

		@Override
		public Payload dragStart(InputEvent event, float x, float y, int pointer) {

			// getActor().fire(new DragEvent( event, DragEvent.Type.dragStart));

			Payload payload = new Payload();
			payload.setDragActor(getActor());
			payload.setInvalidDragActor(getActor());
			payload.setValidDragActor(getActor());
			return payload;
		}

		@Override
		public void dragStop(InputEvent event, float x, float y, int pointer,
				Target target) {
			// getActor().fire(new DragEvent( event, DragEvent.Type.dragStop));
		}

	}

	public class DragTarget extends Target {

		public DragTarget(SceneElementGO sceneElement) {
			super(sceneElement);
		}

		@Override
		public boolean drag(Source source, Payload payload, float x, float y,
				int pointer) {
			InputEvent i = new InputEvent();
			i.setStageX(x);
			i.setStageY(y);
			i.setPointer(pointer);
			// getActor().fire(new DragEvent( i, DragEvent.Type.dragMove,
			// this.getActor()));
			return false;
		}

		@Override
		public void drop(Source source, Payload payload, float x, float y,
				int pointer) {
			InputEvent i = new InputEvent();
			i.setStageX(x);
			i.setStageY(y);
			i.setPointer(pointer);
			// getActor().fire(new DragEvent( i, DragEvent.Type.drop,
			// source.getActor()));

		}

		@Override
		public void reset(Source source, Payload payload) {
			// getActor().fire(new DragEvent( new InputEvent(),
			// DragEvent.Type.dropExit, source.getActor()));
		}

	}
}
