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

package ead.editor.view.scene;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.VolatileImage;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.resources.assets.drawable.basics.Image;
import ead.engine.core.game.GameController;
import ead.engine.core.game.GameLoop;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.assets.RuntimeAsset;
import ead.engine.desktop.core.input.DesktopInputListener;
import ead.engine.desktop.core.platform.DesktopGUI;
import ead.engine.desktop.core.platform.rendering.DesktopCanvas;
import ead.utils.swing.SwingUtilities;

@Singleton
public class DesktopEditorGUI extends DesktopGUI {

	private static final Logger logger = LoggerFactory.getLogger("DesktopEditorGUI");

	/**
	 * The {@code Container} where the game is represented
	 */
	protected JPanel panel;

	protected Object currentComponent;

	protected VolatileImage backbufferImage;

	private DesktopInputListener listener;

	@Inject
	public DesktopEditorGUI(EngineConfiguration platformConfiguration,
			GameObjectManager gameObjectManager, InputHandler inputHandler,
			GameState gameState, SceneElementGOFactory gameObjectFactory,
			DesktopCanvas canvas, GameLoop gameLoop, GameController gameController ) {
		super(platformConfiguration, gameObjectManager, inputHandler, gameState,
				gameObjectFactory, canvas, gameLoop, gameController );
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
				if (panel != null && panel.getVisibleRect() != null
						&& panel.getVisibleRect().getWidth() > 0
						&& panel.getVisibleRect().getHeight() > 0) {
					if (backbufferImage == null) {
						logger.debug("New backbuffer: {}x{}",
								(int) panel.getVisibleRect().getWidth(),
								(int) panel.getVisibleRect().getHeight());
						backbufferImage = panel.createVolatileImage((int) panel
								.getVisibleRect().getWidth(), (int) panel
								.getVisibleRect().getHeight());
					} else if (backbufferImage != null
							&& panel.getVisibleRect().getWidth() != backbufferImage
									.getWidth()
							&& panel.getVisibleRect().getHeight() != backbufferImage
									.getHeight()) {
						logger.debug("Resized backbuffer: {}x{}",
								(int) panel.getVisibleRect().getWidth(),
								(int) panel.getVisibleRect().getHeight());
						backbufferImage = panel.createVolatileImage((int) panel
								.getVisibleRect().getWidth(), (int) panel
								.getVisibleRect().getHeight());
					}
				}
				if (backbufferImage != null) {
					Graphics2D g = (Graphics2D) backbufferImage.getGraphics();
					eAdCanvas.setGraphicContext(g);

					g.setClip(0, 0, backbufferImage.getWidth(),
							backbufferImage.getHeight());
					g.clearRect(0, 0, backbufferImage.getWidth(),
							backbufferImage.getHeight());

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
				if (backbufferImage != null && panel.getGraphics() != null) {
					panel.getGraphics().drawImage(backbufferImage,
							(int) panel.getVisibleRect().getX(),
							(int) panel.getVisibleRect().getY(), null);
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
	 * @see es.eucm.eadventure.engine.core.platform.GUI#initialize()
	 */
	@Override
	public void initialize() {
		try {
			SwingUtilities.doInEDTNow(new Runnable() {
				@Override
				public void run() {
					panel = new GamePanel(engineConfiguration);
					panel.setSize(engineConfiguration.getWidth(),
							engineConfiguration.getHeight());
					panel.setPreferredSize(new Dimension(engineConfiguration
							.getWidth(), engineConfiguration.getHeight()));
					panel.setIgnoreRepaint(true);
					panel.setLayout(new BorderLayout());
					panel.setMinimumSize(new Dimension(200, 150));

					panel.setVisible(true);

					listener = new DesktopInputListener(inputHandler);
					panel.addMouseListener(listener);
					panel.addMouseMotionListener(listener);
					panel.addKeyListener(listener);

					panel.addComponentListener(new ComponentAdapter() {

						@Override
						public void componentResized(ComponentEvent arg0) {
							if (backbufferImage != null)
								backbufferImage.flush();
							backbufferImage = null;
						}

					});
				}
			});
		} catch (RuntimeException e) {
            throw new IllegalArgumentException(
                    "could not initialize Desktop EditorGui", e);
		}

		logger.info("Desktop EditorGui initialized");
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

		private EngineConfiguration platformConfiguration;

		public GamePanel(EngineConfiguration platformConfiguration) {
			this.platformConfiguration = platformConfiguration;
		}

        @Override
		public void setPreferredSize(Dimension d) {
			super.setPreferredSize(d);
			platformConfiguration.setSize(d.width, d.height);
			if (backbufferImage != null)
				backbufferImage.flush();
			backbufferImage = null;
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return super.getPreferredSize();
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			// TODO check
			return 30;
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
			// TODO check
			return 5;
		}

	}

}
