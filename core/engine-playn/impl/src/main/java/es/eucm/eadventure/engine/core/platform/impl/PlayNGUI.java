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

package es.eucm.eadventure.engine.core.platform.impl;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import playn.core.Canvas;
import playn.core.SurfaceLayer;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineImage;
import es.eucm.eadventure.engine.core.platform.impl.extra.PlayNInputListener;
import es.eucm.eadventure.utils.swing.SwingExceptionHandler;
import es.eucm.eadventure.utils.swing.SwingUtilities;

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
	private static final Logger logger = Logger.getLogger("PlayNGUI");

	/**
	 * The {@code Canvas} object where the actual game is drawn
	 */
	private Canvas canvas;

	/**
	 * Represent how many pixels the mouse moves when the arrow keys are pressed
	 */
	private int MOUSE_MOVE = 20;

	private Object currentComponent;

	@Inject
	public PlayNGUI(PlatformConfiguration platformConfiguration,
			GraphicRendererFactory<?> assetRendererFactory,
			GameObjectManager gameObjectManager, MouseState mouseState,
			KeyboardState keyboardState, BasicHUD basicDesktopHUD,
			ValueMap valueMap, GameState gameState,
			GameObjectFactory gameObjectFactory) {
		super(platformConfiguration, assetRendererFactory, gameObjectManager,
				mouseState, keyboardState, valueMap, gameState,
				gameObjectFactory);
		this.gameObjects.addHUD(basicDesktopHUD);
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
		/*
		if (this.currentComponent == resource)
			return;
		if (this.currentComponent != null) {
			SwingUtilities.doInEDTNow(new Runnable() {
				@Override
				public void run() {
					frame.remove((Component) currentComponent);
				}
			});
			currentComponent = null;
		}
		if (this.currentComponent == null) {
			if (resource == null) {
				SwingUtilities.doInEDTNow(new Runnable() {
					@Override
					public void run() {
						frame.add(canvas);
					}
				});
			} else {
				SwingUtilities.doInEDTNow(new Runnable() {
					@Override
					public void run() {
						frame.remove(canvas);
						((Component) resource).setBounds(0, 0,
								frame.getWidth(), frame.getHeight());
						frame.add((Component) resource);
						frame.validate();
					}
				});
				currentComponent = resource;
			}
		}
		*/
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
		
		render(canvas, interpolation);

		/*
		
		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				BufferStrategy bs = canvas.getBufferStrategy();
				Graphics2D g = (Graphics2D) bs.getDrawGraphics();
				g.setClip(0, 0, platformConfiguration.getWidth(),
						platformConfiguration.getHeight());

				if (!g.getRenderingHints().containsValue(
						RenderingHints.VALUE_ANTIALIAS_ON))
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
				if (!g.getRenderingHints().containsValue(
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON))
					g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

				g.setFont(g.getFont().deriveFont(20.0f));

				g.scale(platformConfiguration.getScale(),
						platformConfiguration.getScale());

				render(g, interpolation);

				g.dispose();
			}
		});

		SwingUtilities.doInEDT(new Runnable() {
			@Override
			public void run() {
				BufferStrategy bs = canvas.getBufferStrategy();
				bs.show();
				Toolkit.getDefaultToolkit().sync();
			}
		});
		
		*/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#commitToImage()
	 */
	@Override
	public RuntimeAsset<Image> commitToImage() {
		int width = platformConfiguration.getWidth() * GUI.VIRTUAL_HEIGHT
				/ platformConfiguration.getHeight();
		int height = GUI.VIRTUAL_HEIGHT;
		PlayNEngineImage image = new PlayNEngineImage(width, height);
/*
		Graphics2D g = (Graphics2D) image.getImage().getGraphics();
		g.setClip(0, 0, width, height);

		setRenderingHints(g);

		g.setFont(g.getFont().deriveFont(20.0f));

		// g.scale(platformConfiguration.getScale(),
		// platformConfiguration.getScale());

		render(g, 0.0f);

		g.dispose();
*/

		return image;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#initilize()
	 */
	@Override
	public void initilize() {
		//TODO initialize Canvas
		logger.info("PlayN GUI initilized");
	}

	/**
	 * Initialize the {@code Canvas} element where the actual game is drawn
	 */
	private void initializeCanvas() {
		
		canvas = new Canvas();
		canvas.setSize(frame.getSize());
		canvas.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		frame.add(canvas);

		canvas.setEnabled(true);
		canvas.setVisible(true);
		canvas.setFocusable(true);

		canvas.createBufferStrategy(2);
		BufferStrategy bs = canvas.getBufferStrategy();
		bs.getDrawGraphics().getFontMetrics();

		PlayNInputListener listener = new PlayNInputListener(mouseState,
				keyboardState);
		canvas.addMouseListener(listener);
		canvas.addMouseMotionListener(listener);
		canvas.addKeyListener(listener);
	}

}
