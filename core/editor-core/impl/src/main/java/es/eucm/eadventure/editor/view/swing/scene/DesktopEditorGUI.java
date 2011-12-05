package es.eucm.eadventure.editor.view.swing.scene;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.MemoryImageSource;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.impl.DesktopCanvas;
import es.eucm.eadventure.engine.core.platform.impl.DesktopGUI;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopInputListener;
import es.eucm.eadventure.utils.swing.SwingExceptionHandler;
import es.eucm.eadventure.utils.swing.SwingUtilities;

@Singleton
public class DesktopEditorGUI extends DesktopGUI {

	private static final Logger logger = Logger.getLogger("DesktopEditorGUI");
	
	/**
	 * The {@code Container} where the game is represented
	 */
	protected JPanel panel;

	/**
	 * The {@code Canvas} object where the actual game is drawn
	 */
	protected Canvas canvas;

	protected Object currentComponent;

	@Inject
	public DesktopEditorGUI(PlatformConfiguration platformConfiguration,
			GameObjectManager gameObjectManager, MouseState mouseState,
			KeyboardState keyboardState, GameState gameState,
			SceneElementGOFactory gameObjectFactory, DesktopCanvas canvas) {
		super(platformConfiguration, gameObjectManager, mouseState, keyboardState,
				gameState, gameObjectFactory, canvas);
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#commit(float)
	 */
	@Override
	public void commit(final float interpolation) {
		if (canvas.getBufferStrategy() == null) {
			try {
				canvas.createBufferStrategy(2);
				BufferStrategy bs = canvas.getBufferStrategy();
				bs.getDrawGraphics().getFontMetrics();
			} catch (IllegalStateException e) {
				logger.severe("No support for back buffer");
			}
			return;
		}
		
		processInput();

		if (currentComponent != null)
			return;

		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				BufferStrategy bs = canvas.getBufferStrategy();
				Graphics2D g = (Graphics2D) bs.getDrawGraphics();
				eAdCanvas.setGraphicContext(g);
				//g.setClip(0, 0, platformConfiguration.getWidth(),
				//		platformConfiguration.getHeight());

				g.clearRect(0, 0, getWidth(), getHeight());
				setRenderingHints(g);

				g.setFont(g.getFont().deriveFont(20.0f));

				render(interpolation);

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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#commitToImage()
	 */
	@Override
	public RuntimeAsset<Image> commitToImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.GUI#initilize()
	 */
	@Override
	public void initilize() {
		try {
			SwingUtilities.doInEDTNow(new Runnable() {
				@Override
				public void run() {
					panel = new JPanel();
					panel.setSize(platformConfiguration.getWidth(),
							platformConfiguration.getHeight());
					panel.setIgnoreRepaint(true);

					panel.setVisible(true);
					
					initializeCanvas();
				}
			});
		} catch (RuntimeException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		logger.info("Desktop GUI initilized");
	}

	/**
	 * Initialize the {@code Canvas} element where the actual game is drawn
	 */
	protected void initializeCanvas() {
		canvas = new Canvas();
		canvas.setSize(panel.getSize());
		canvas.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		panel.add(canvas);

		canvas.setEnabled(true);
		canvas.setVisible(true);
		canvas.setFocusable(true);
		
		try {
			canvas.createBufferStrategy(2);
			BufferStrategy bs = canvas.getBufferStrategy();
			bs.getDrawGraphics().getFontMetrics();
		} catch (IllegalStateException e) {
			logger.severe("No support for back buffer");
		}

		DesktopInputListener listener = new DesktopInputListener(mouseState,
				keyboardState);
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
	@Override
	protected void processKeyAction(KeyAction action) {
		super.processKeyAction(action);
	}

	@Override
	public void finish() {
		if (panel != null) {
			panel.setVisible(false);
		}
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	
}
