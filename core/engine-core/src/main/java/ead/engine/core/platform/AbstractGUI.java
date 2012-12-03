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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.fills.ColorFill;
import ead.common.util.EAdRectangle;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameImpl;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.gameobjects.huds.HudGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.util.EAdTransformation;

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

	/**
	 * Game object manager
	 */
	protected GameObjectManager gameObjects;

	@SuppressWarnings("rawtypes")
	protected GenericCanvas eAdCanvas;

	protected Game game;

	protected GameState gameState;

	public AbstractGUI(GameObjectManager gameObjectManager,
			GenericCanvas<T> canvas) {
		this.gameObjects = gameObjectManager;
		this.eAdCanvas = canvas;
		logger.info("Created abstract GUI");
	}

	public void initialize(Game game, GameState gameState) {
		this.game = game;
		this.gameState = gameState;
		eAdCanvas.setWidth(gameState.getValueMap().getValue(
				SystemFields.GAME_WIDTH));
		eAdCanvas.setHeight(gameState.getValueMap().getValue(
				SystemFields.GAME_HEIGHT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.GUI#addElement(es.eucm.eadventure
	 * .engine.core.gameobjects.GameObject)
	 * 
	 * The element should not be offset as it is being dragged in the scene
	 */
	@Override
	public void addElement(DrawableGO<?> element,
			EAdTransformation parentTransformation) {
		EAdTransformation t = element.getTransformation();

		if (t != null) {

			if (!t.isValidated() || !parentTransformation.isValidated()) {
				element.resetTransfromation();
				addTransformation(t, parentTransformation);
			}
			if (t.isVisible()) {
				gameObjects.add(element, t);
				element.doLayout(t);
			}
		}
	}

	@Override
	public void addHud(HudGO hud) {
		gameObjects.addHUD(hud);

	}

	@Override
	public void removeHUD(HudGO hud) {
		gameObjects.removeHud(hud);

	}

	@Override
	public List<HudGO> getHUDs() {
		return gameObjects.getHUDs();
	}

	/**
	 * Render the game objects into the graphic context
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void commit() {
		for (DrawableGO<?> go : gameObjects.getGameObjects()) {
			if (go != null) {
				EAdTransformation t = go.getTransformation();
				t.setValidated(true);
				eAdCanvas.setTransformation(t);
				RuntimeDrawable<?, ?> r = go.getRuntimeDrawable();
				if (r != null) {
					go.getRuntimeDrawable().render(eAdCanvas);
				} else {
					eAdCanvas.setPaint(ColorFill.MAGENTA);
					eAdCanvas.fillRect(0, 0, go.getWidth(), go.getHeight());
				}
			}

		}
		gameObjects.getGameObjects().clear();
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

	public DrawableGO<?> processAction(InputAction<?> action) {
		DrawableGO<?> go = null;
		if (action instanceof MouseInputAction) {
			MouseInputAction m = (MouseInputAction) action;
			go = hudProcess(m);
			if (go == null) {
				go = gameState.getScene().processAction(action);
			}
		} else if (action instanceof KeyInputAction) {
			KeyInputAction k = (KeyInputAction) action;
			EAdSceneElement element = gameState.getValueMap().getValue(
					SystemFields.ACTIVE_ELEMENT);
			// only the active element gets a try to consume it
			if (element != null) {
				go = gameObjects.getGameObject(element);
				if (go != null) {
					go.processAction(k);
				}
			}
		}
		game.applyFilters(GameImpl.FILTER_PROCESS_ACTION, action, null);
		return go;
	}

	public SceneElementGO<?> getGameObjectIn(int x, int y) {
		SceneElementGO<?> go = null;
		int i = getHUDs().size() - 1;
		while (go == null && i >= 0) {
			HudGO hud = getHUDs().get(i--);
			if (hud.contains(x, y)) {
				go = hud.getFirstGOIn(x, y);
			}
		}
		if (go == null) {
			return gameState.getScene().getFirstGOIn(x, y);
		}
		return go;
	}

	private DrawableGO<?> hudProcess(MouseInputAction a) {
		DrawableGO<?> go = null;
		int i = getHUDs().size() - 1;
		while (!a.isConsumed() && i >= 0) {
			HudGO hud = getHUDs().get(i--);
			if (hud.contains(a.getVirtualX(), a.getVirtualY())) {
				go = hud.processAction(a);
			}
		}
		return go;
	}

}
