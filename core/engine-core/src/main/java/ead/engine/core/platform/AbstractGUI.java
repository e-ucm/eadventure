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

import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.fills.ColorFill;
import ead.common.util.EAdRectangle;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameImpl;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.GameObjectFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.huds.HudGO;
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
			SceneElementGOFactory sceneElementFactory, InputHandler inputHandler) {
		this.loadingScreen = new LoadingScreen();
		this.game = game;
		this.gameState = gameState;
		this.sceneElementFactory = sceneElementFactory;

		eAdCanvas.setWidth(gameState.getValue(SystemFields.GAME_WIDTH));
		eAdCanvas.setHeight(gameState.getValue(SystemFields.GAME_HEIGHT));
		root = sceneElementFactory.get(new GroupElement());
		hudRoot = sceneElementFactory.get(new GroupElement());
		sceneRoot = sceneElementFactory.get(new GroupElement());

		root.addSceneElement(hudRoot);
		root.addSceneElement(sceneRoot);
		updateInitialTransformation();
		inputHandler.setInitialTransformation(initialTransformation);
	}

	@Override
	public EAdScene getPreviousScene() {
		return previousSceneStack.pop();
	}

	@Override
	public void addHud(HudGO hud) {
		hudRoot.addSceneElement(hud);
	}

	@Override
	public void removeHUD(HudGO hud) {
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
		t.setValidated(true);
		eAdCanvas.setTransformation(t);
		RuntimeDrawable<?, ?> r = go.getDrawable();
		if (r != null) {
			go.getDrawable().render(eAdCanvas);
		} else {
			eAdCanvas.setPaint(ColorFill.MAGENTA);
			eAdCanvas.fillRect(0, 0, go.getWidth(), go.getHeight());
		}
		for (SceneElementGO<?> g : go.getChildren()) {
			commit(g);
		}
	}

	public void update() {
		root.update();
	}

	@Override
	public EAdTransformation addTransformation(EAdTransformation t1,
			EAdTransformation t2) {
		float alpha = t1.getAlpha() * t2.getAlpha();
		boolean visible = t1.isVisible() && t2.isVisible();

		t1.setAlpha(alpha);
		t1.setVisible(visible);
		t1.getMatrix().multiply(t2.getMatrix().getFlatMatrix(), false);

		EAdRectangle clip1 = t1.getClip();
		EAdRectangle clip2 = t2.getClip();
		EAdRectangle newclip = new EAdRectangle(0, 0, 0, 0);

		// FIXME multiply for matrix to know where the clip actually is
		if (clip1 == null) {
			newclip = clip2;
		} else if (clip2 == null) {
			newclip = clip1;
		} else if (clip1 != null && clip2 != null) {
			newclip = clip2;
		}

		if (newclip != null)
			t1.setClip(newclip.x, newclip.y, newclip.width, newclip.height);

		return t1;
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
			// only the active element gets a try to consume it
			if (element != null) {
				go = root.getChild(element);
				if (go != null) {
					go.processAction(k);
				}
			}
		}
		game.applyFilters(GameImpl.FILTER_PROCESS_ACTION, action, null);
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

}
