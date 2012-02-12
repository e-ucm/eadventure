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

import static playn.core.PlayN.graphics;

import java.awt.Graphics2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;

import playn.core.Canvas;
import playn.core.CanvasLayer;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.rendering.PlayNCanvas;

/**
 * <p>
 * Desktop implementation of the {@link AbstractGUI} uses awt {@link Graphics2D}
 * as a graphic framework, and works within a Swing {@link JFrame}.
 * </p>
 */
@Singleton
public class PlayNGUI extends AbstractGUI<Canvas> implements GUI {

	/**
	 * The class logger
	 */
	private static final Logger logger = LoggerFactory.getLogger("PlayNGUI");

	/**
	 * The {@code Canvas} object where the actual game is drawn
	 */
	@SuppressWarnings("unused")
	private Canvas canvas;

	@SuppressWarnings("unused")
	private CanvasLayer canvasLayer;

	private Object currentComponent;

	@Inject
	public PlayNGUI(EngineConfiguration platformConfiguration,
			GameObjectManager gameObjectManager, InputHandler inputHandler, GameState gameState,
			SceneElementGOFactory gameObjectFactory, PlayNCanvas canvas) {
		super(platformConfiguration, gameObjectManager, inputHandler,
				gameState, gameObjectFactory, canvas);
		logger.info("New instance");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.GUI#showSpecialResource(java.
	 * lang.Object, int, int, boolean)
	 */
	@Override
	public void showSpecialResource(final Object resource, int x, int y,
			boolean fullscreen) {
		if (resource != null) {
			if (currentComponent != resource) {
				Widget widget = (Widget) resource;
				RootPanel.get().add(widget, 0, 0);
				// graphics().rootLayer().remove(canvasLayer);
				graphics().rootLayer().setVisible(false);
				currentComponent = resource;
			}
		} else {
			if (currentComponent != null) {
				// graphics().rootLayer().add(canvasLayer);
				RootPanel.get().remove((Widget) currentComponent);
				graphics().rootLayer().setVisible(true);
			}
			currentComponent = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#commit(float)
	 */
	@Override
	public void commit(final float interpolation) {
		processInput();

		if (currentComponent != null)
			return;

		render(interpolation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#initialize()
	 */
	@Override
	public void initialize() {
		// TODO initialize Canvas
		logger.info("PlayN GUI initialized");
	}

	/**
	 * Initialize the {@code Canvas} element where the actual game is drawn
	 * 
	 * @param layer
	 */
	public void initializeCanvas(Canvas canvas, CanvasLayer layer) {
		eAdCanvas.setGraphicContext(canvas);
		this.canvasLayer = layer;
		/*
		 * canvas.addMouseListener(listener);
		 * canvas.addMouseMotionListener(listener);
		 * canvas.addKeyListener(listener);
		 */
	}

	public void changeCursor(ImageImpl image) {
		// NOT COMPATIBLE?
	}

	@Override
	public void finish() {

	}

}
