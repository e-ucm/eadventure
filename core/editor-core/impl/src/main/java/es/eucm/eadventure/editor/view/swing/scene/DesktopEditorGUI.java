package es.eucm.eadventure.editor.view.swing.scene;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.VolatileImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.Scrollable;

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
import es.eucm.eadventure.utils.swing.SwingUtilities;

@Singleton
public class DesktopEditorGUI extends DesktopGUI {

	private static final Logger logger = Logger.getLogger("DesktopEditorGUI");
	
	/**
	 * The {@code Container} where the game is represented
	 */
	protected JPanel panel;

	protected Object currentComponent;
	
	protected VolatileImage backbufferImage;

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
		processInput();

		if (currentComponent != null)
			return;

		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				backbufferImage = panel.createVolatileImage(panel.getWidth(), panel.getHeight());
				if (backbufferImage != null) {
					Graphics2D g = (Graphics2D) backbufferImage.getGraphics();
					eAdCanvas.setGraphicContext(g);
	
					//				g.setClip(0, 0, panel.getWidth(), panel.getHeight());
					g.setClip(0, 0, platformConfiguration.getWidth(),
							platformConfiguration.getHeight());
	
					g.clearRect(0, 0, getWidth(), getHeight());
	
					setRenderingHints(g);
	
					g.setFont(g.getFont().deriveFont(20.0f));
	
					render(interpolation);
					
					g.dispose();
				}
			}
		});

		SwingUtilities.doInEDT(new Runnable() {
			@Override
			public void run() {
				if (backbufferImage != null) {
					panel.getGraphics().drawImage(backbufferImage, 0, 0, null);
					Toolkit.getDefaultToolkit().sync();
				}
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
					panel = new GamePanel(platformConfiguration);
					panel.setSize(platformConfiguration.getWidth(),
							platformConfiguration.getHeight());
					panel.setPreferredSize(new Dimension(platformConfiguration.getWidth(),
							platformConfiguration.getHeight()));
					panel.setIgnoreRepaint(true);
					panel.setLayout(new BorderLayout());
					panel.setMinimumSize(new Dimension(200, 150));

					panel.setVisible(true);
					
					DesktopInputListener listener = new DesktopInputListener(mouseState,
							keyboardState);
					panel.addMouseListener(listener);
					panel.addMouseMotionListener(listener);
					panel.addKeyListener(listener);
				}
			});
		} catch (RuntimeException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		logger.info("Desktop GUI initilized");
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
	
	private class GamePanel extends JPanel implements Scrollable {

		private static final long serialVersionUID = -8779328786327371343L;
		
		private PlatformConfiguration platformConfiguration;
		
		public GamePanel(PlatformConfiguration platformConfiguration) {
			this.platformConfiguration = platformConfiguration;
		}
		
		public void setPreferredSize(Dimension d) {
			super.setPreferredSize(d);
			platformConfiguration.setHeight((int) d.getHeight());
			platformConfiguration.setWidth((int) d.getWidth());
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return super.getPreferredSize();
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			//TODO check
			return 10;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return false;
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			//TODO check
			return 1;
		}
		
	}
	
}
