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

import java.util.logging.Logger;

import ead.common.util.EAdMatrixImpl;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.Renderable;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GUI;
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
	protected static final Logger logger = Logger.getLogger("AbstractGUI");

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
	protected InputHandler mouseState;

	protected GameState gameState;

	protected SceneElementGOFactory gameObjectFactory;

	protected GenericCanvas<T> eAdCanvas;

	public AbstractGUI(EngineConfiguration platformConfiguration,
			GameObjectManager gameObjectManager, InputHandler mouseState, GameState gameState,
			SceneElementGOFactory gameObjectFactory, GenericCanvas<T> canvas) {
		this.platformConfiguration = platformConfiguration;
		this.gameObjects = gameObjectManager;
		this.mouseState = mouseState;
		this.gameState = gameState;
		this.gameObjectFactory = gameObjectFactory;
		this.eAdCanvas = canvas;
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
			EAdTransformation transformation) {
		EAdTransformation t = element.getTransformation();
		if (t != null) {
			EAdTransformation tResult = addTransformation(transformation, t);
			if (tResult.isVisible()) {
				gameObjects.add(element, tResult);
				element.doLayout(tResult);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#prepareGUI()
	 */
	@Override
	public void prepareGUI(EAdTransformation t) {
		gameObjects.swap();
	}

	/**
	 * Process the different sort of inputs received by the game
	 */
	protected void processInput() {		
		mouseState.processActions();
	}

	/**
	 * Render the game objects into the graphic context
	 * 
	 * @param interpolation
	 *            The current interpolation between ideal game frames
	 */
	protected void render(float interpolation) {
		// TODO use interpolation
		synchronized (GameObjectManager.lock) {
			for (int i = 0; i < gameObjects.getGameObjects().size(); i++) {
				EAdTransformation t = gameObjects.getTransformations().get(i);
				eAdCanvas.setTransformation(t);
				((Renderable) gameObjects.getGameObjects().get(i))
						.render(eAdCanvas);
			}
		}
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
		EAdMatrixImpl m = new EAdMatrixImpl();
		m.multiply(t1.getMatrix().getFlatMatrix(), true);
		m.multiply(t2.getMatrix().getFlatMatrix(), true);
		float alpha = t1.getAlpha() * t2.getAlpha();
		boolean visible = t1.isVisible() && t2.isVisible();
		EAdTransformationImpl t = new EAdTransformationImpl(m, visible, alpha);
		return t;
	}

}
