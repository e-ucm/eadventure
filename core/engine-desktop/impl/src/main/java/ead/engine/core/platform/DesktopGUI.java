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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.resources.assets.drawable.basics.EAdBasicDrawable;
import ead.engine.core.game.GameLoop;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.DesktopInputListener;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.assets.DesktopEngineImage;
import ead.engine.core.platform.rendering.DesktopCanvas;
import ead.utils.swing.SwingExceptionHandler;
import ead.utils.swing.SwingUtilities;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.MemoryImageSource;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Desktop implementation of the {@link AbstractGUI} uses awt {@link Graphics2D}
 * as a graphic framework, and works within a Swing {@link JFrame}.
 * </p>
 */
@Singleton
public class DesktopGUI extends AbstractGUI<Graphics2D> implements GUI {

	/**
	 * The class logger
	 */
	private static final Logger logger = LoggerFactory.getLogger("DesktopGUI");

	/**
	 * The {@code JFrame} where the game is represented
	 */
	private JFrame frame;

	/**
	 * The {@code Canvas} object where the actual game is drawn
	 */
	private Canvas canvas;

	private GameLoop gameLoop;

	/**
	 * AWT Robot, used to move the mouse in the screen
	 */
	// private Robot robot;

	/**
	 * Represent how many pixels the mouse moves when the arrow keys are pressed
	 */
	// private int MOUSE_MOVE = 20;

	private Object currentComponent;

	@Inject
	public DesktopGUI(EngineConfiguration conf,
			GameObjectManager gameObjectManager, InputHandler inputHandler,
			GameState gameState, SceneElementGOFactory gameObjectFactory,
			DesktopCanvas canvas, GameLoop gameLoop) {
		super(conf, gameObjectManager, inputHandler, gameState,
				gameObjectFactory, canvas);
		this.gameLoop = gameLoop;
		// try {
		// this.robot = new Robot();
		// } catch (AWTException e) {
		// }
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
						canvas.createBufferStrategy(2);
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

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.eucm.eadventure.engine.core.platform.GUI#commit(float)
	 */
	@Override
	public void commit(final float interpolation) {
		if (frame.isVisible()) {
			processInput();

			if (currentComponent != null)
				return;

			SwingUtilities.doInEDTNow(new Runnable() {
				@Override
				public void run() {
					try {
						BufferStrategy bs = canvas.getBufferStrategy();
						Graphics2D g = (Graphics2D) bs.getDrawGraphics();
						eAdCanvas.setGraphicContext(g);
						g.setClip(0, 0, platformConfiguration.getWidth(),
								platformConfiguration.getHeight());

						setRenderingHints(g);

						g.setFont(g.getFont().deriveFont(20.0f));

						render(interpolation);

						g.dispose();
					} catch (IllegalStateException e) {
						if (gameLoop.isRunning()) {
                            logger.warn("error commiting GUI phase 1; "
                                + "will use fallback bufferStrategy", e);
							canvas.createBufferStrategy(2);
						}
					}
				}
			});

			SwingUtilities.doInEDT(new Runnable() {
				@Override
				public void run() {
					try {
						BufferStrategy bs = canvas.getBufferStrategy();
						bs.show();
						Toolkit.getDefaultToolkit().sync();
					} catch (IllegalStateException e) {
					    if (gameLoop.isRunning()) {
                            logger.warn("error commiting GUI phase 1; "
                                + "will use fallback bufferStrategy", e);
							canvas.createBufferStrategy(2);
						}
					}
				}
			});
		}
	}

	public RuntimeAsset<? extends EAdBasicDrawable> commitToImage() {

		DesktopEngineImage image = new DesktopEngineImage(
				platformConfiguration.getWidth(),
				platformConfiguration.getHeight());

		Graphics2D g = (Graphics2D) image.getImage().getGraphics();
		g.setClip(0, 0, image.getWidth(), image.getHeight());
		setRenderingHints(g);
		g.setFont(g.getFont().deriveFont(20.0f));
		eAdCanvas.setGraphicContext(g);
		render(0.0f);
		g.dispose();
		return image;
	}

	private void setHint(Graphics2D g, Object value, Key key) {
		if (!g.getRenderingHints().containsValue(value))
			g.setRenderingHint(key, value);
	}

	/**
	 * Set the appropriate rendering hints to get the best graphic results.
	 *
	 * @param g
	 */
	protected void setRenderingHints(Graphics2D g) {
		// TODO test effects, probably should allow disabling
		setHint(g, RenderingHints.VALUE_ANTIALIAS_ON,
				RenderingHints.KEY_ANTIALIASING);
		// setHint(g, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,
		// RenderingHints.KEY_ALPHA_INTERPOLATION);
		setHint(g, RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
				RenderingHints.KEY_TEXT_ANTIALIASING);
		setHint(g, RenderingHints.VALUE_COLOR_RENDER_QUALITY,
				RenderingHints.KEY_COLOR_RENDERING);
		// setHint(g, RenderingHints.VALUE_FRACTIONALMETRICS_ON,
		// RenderingHints.KEY_FRACTIONALMETRICS);
		// setHint(g, RenderingHints.VALUE_INTERPOLATION_BICUBIC,
		// RenderingHints.KEY_INTERPOLATION);
		// setHint(g, RenderingHints.VALUE_RENDER_QUALITY,
		// RenderingHints.KEY_RENDERING);
		setHint(g, RenderingHints.VALUE_STROKE_NORMALIZE,
				RenderingHints.KEY_STROKE_CONTROL);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.eucm.eadventure.engine.core.platform.GUI#initialize()
	 */
	@Override
	public void initialize() {
		try {
			SwingUtilities.doInEDTNow(new Runnable() {
				@Override
				public void run() {
					Thread.currentThread().setUncaughtExceptionHandler(
							SwingExceptionHandler.getInstance());
					Thread.setDefaultUncaughtExceptionHandler(SwingExceptionHandler
							.getInstance());

					frame = new JFrame();
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setSize(platformConfiguration.getWidth(),
							platformConfiguration.getHeight());
					frame.setUndecorated(false);
					frame.setIgnoreRepaint(true);

					if (platformConfiguration.isFullscreen()) {
						// TODO this might not work in windows
						GraphicsDevice gd = GraphicsEnvironment
								.getLocalGraphicsEnvironment()
								.getDefaultScreenDevice();
						gd.setFullScreenWindow(frame);
						platformConfiguration.setSize(frame.getWidth(),
								frame.getHeight());
						logger.info("Frame size: " + frame.getWidth() + " x "
								+ frame.getHeight());
					} else {
						frame.setLocationRelativeTo(null);
					}

					frame.setVisible(true);

					initializeCanvas();
				}
			});
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("Error initializing desktop GUI", e);
		}

		logger.info("Desktop GUI initialized");
	}

	/**
	 * Initialize the {@code Canvas} element where the actual game is drawn
	 */
	private void initializeCanvas() {
		canvas = new Canvas();
		canvas.setSize(frame.getSize());
		canvas.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		frame.add(canvas);
		frame.pack();
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {

			}

			@Override
			public void windowClosed(WindowEvent arg0) {

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				gameLoop.stop();
				frame.dispose();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {

			}

			@Override
			public void windowIconified(WindowEvent arg0) {

			}

			@Override
			public void windowOpened(WindowEvent arg0) {

			}

		});

		canvas.setEnabled(true);
		canvas.setVisible(true);
		canvas.setFocusable(true);
		// Create transparent cursor
		int[] pixels = new int[16 * 16];
		java.awt.Image image = (java.awt.Image) Toolkit.getDefaultToolkit()
				.createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(image, new Point(0, 0), "invisibleCursor");
		canvas.setCursor(transparentCursor);

		canvas.createBufferStrategy(2);
		BufferStrategy bs = canvas.getBufferStrategy();
		bs.getDrawGraphics().getFontMetrics();

		DesktopInputListener listener = new DesktopInputListener(inputHandler);
		canvas.addMouseListener(listener);
		canvas.addMouseMotionListener(listener);
		canvas.addKeyListener(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.eucm.eadventure.engine.core.platform.impl.AbstractGUI#processKeyAction
	 * (es.eucm.eadventure.engine.core.guiactions.KeyAction)
	 *
	 * In desktop games, arrow keys are used to move the mouse if not consumed
	 * by a game object
	 */
	// @Override
	// protected void processKeyAction(KeyActionImpl action) {
	// super.processKeyAction(action);
	// if (action != null && !action.isConsumed()) {
	// if (robot != null) {
	// int x = MouseInfo.getPointerInfo().getLocation().x;
	// int y = MouseInfo.getPointerInfo().getLocation().y;
	// boolean move = false;
	//
	// switch (action.getKeyCode()) {
	// case ARROW_UP:
	// y = y - MOUSE_MOVE;
	// move = true;
	// break;
	// case ARROW_DOWN:
	// y = y + MOUSE_MOVE;
	// move = true;
	// break;
	// case ARROW_LEFT:
	// x = x - MOUSE_MOVE;
	// move = true;
	// break;
	// case ARROW_RIGHT:
	// x = x + MOUSE_MOVE;
	// move = true;
	// break;
	// }
	// if (move) {
	// x = Math.max(x, frame.getX());
	// y = Math.max(y, frame.getY());
	// x = Math.min(x, frame.getX() + frame.getWidth());
	// y = Math.min(y, frame.getY() + frame.getHeight());
	// robot.mouseMove(x, y);
	// }
	//
	// }
	// }
	// }

	@Override
	public void finish() {
		if (frame != null) {
			frame.setVisible(false);
		}

	}

}
