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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.params.fills.ColorFill;
import ead.common.util.EAdRectangle;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameLoop;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.input.InputHandler;
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
	 * Platform configuration parameters
	 */
	protected EngineConfiguration platformConfiguration;

	/**
	 * Game object manager
	 */
	protected GameObjectManager gameObjects;

	/**
	 * The current mouse state
	 */
	protected InputHandler inputHandler;

	protected GameState gameState;

	protected SceneElementGOFactory gameObjectFactory;

	protected GenericCanvas eAdCanvas;

	protected GameLoop gameLoop;

	private Game game;

	public AbstractGUI(EngineConfiguration platformConfiguration,
			GameObjectManager gameObjectManager, InputHandler inputHandler,
			GameState gameState, SceneElementGOFactory gameObjectFactory,
			GenericCanvas<T> canvas, GameLoop gameLoop) {
		this.platformConfiguration = platformConfiguration;
		this.gameObjects = gameObjectManager;
		this.inputHandler = inputHandler;
		this.gameState = gameState;
		this.gameObjectFactory = gameObjectFactory;
		this.eAdCanvas = canvas;
		this.gameLoop = gameLoop;
		logger.info("Created abstract GUI");
	}

	public void setGame(Game game) {
		this.game = game;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#prepareGUI()
	 */
	@Override
	public void prepareGUI() {
		gameObjects.swap();
		for (DrawableGO<?> go : gameObjects.getGameObjects()) {
			go.getTransformation().setValidated(true);
		}
	}

	/**
	 * Process the different sort of inputs received by the game
	 */
	protected void processInput() {
		inputHandler.processActions();
	}

	/**
	 * Render the game objects into the graphic context
	 * 
	 * @param interpolation
	 *            The current interpolation between ideal game frames
	 */
	protected void render(float interpolation) {
		synchronized (GameObjectManager.lock) {

			eAdCanvas.setClip(getClip());
			for (DrawableGO<?> go : gameObjects.getGameObjects()) {
				if (go != null) {
					EAdTransformation t = go.getTransformation();
					eAdCanvas.setTransformation(t);
					RuntimeDrawable<?, ?> r = go.getRuntimeDrawable();
					if (r != null) {
						go.getRuntimeDrawable().render(eAdCanvas);
					}
					else {
						eAdCanvas.setPaint(ColorFill.MAGENTA);
						eAdCanvas.fillRect(0, 0, go.getWidth(), go.getHeight());
					}
				}

			}
		}
	}

	private EAdRectangle getClip() {
		float[] init = game.getInitialTransformation().getMatrix()
				.multiplyPoint(0, 0, true);
		float[] end = game
				.getInitialTransformation()
				.getMatrix()
				.multiplyPoint(game.getAdventureModel().getGameWidth(),
						game.getAdventureModel().getGameHeight(), true);
		return new EAdRectangle((int) init[0], (int) init[1],
				(int) (end[0] - init[0]), (int) (end[1] - init[1]));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#getWidth()
	 */
	@Override
	public int getWidth() {
		return platformConfiguration.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#getHeight()
	 */
	@Override
	public int getHeight() {
		return platformConfiguration.getHeight();
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

	@Override
	public void setInitialTransformation(EAdTransformation initialTransformation) {
		inputHandler.setInitialTransformation(initialTransformation);
	}

	public int getSkippedMilliseconds() {
		return gameLoop.getSkipMillisTick();
	}

	public int getTicksPerSecond() {
		return gameLoop.getTicksPerSecond();
	}
}
