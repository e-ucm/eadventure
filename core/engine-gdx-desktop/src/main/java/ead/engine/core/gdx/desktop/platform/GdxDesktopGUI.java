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

package ead.engine.core.gdx.desktop.platform;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

import javax.swing.JFrame;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.variables.SystemFields;
import ead.engine.core.debuggers.DebuggersHandler;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameState;
import ead.engine.core.gdx.GdxEngine;
import ead.engine.core.gdx.platform.GdxCanvas;
import ead.engine.core.gdx.platform.GdxGUI;
import ead.engine.core.input.InputHandler;
import ead.utils.swing.SwingUtilities;

@Singleton
public class GdxDesktopGUI extends GdxGUI {

	private JFrame frame;

	private Canvas canvas;

	private Component component;

	@Inject
	public GdxDesktopGUI(GdxCanvas canvas, GdxEngine engine) {
		super(canvas, engine);
	}

	@Override
	public void initialize(Game game, GameState gameState,
			SceneElementGOFactory sceneElementFactory,
			InputHandler inputHandler, DebuggersHandler debuggerHandler) {
		super.initialize(game, gameState, sceneElementFactory, inputHandler,
				debuggerHandler);
		frame = new JFrame();

		// Sets a null cursor (so the in-game one is used)
		frame.setCursor(frame.getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "null"));

		int width = gameState.getValue(SystemFields.GAME_WIDTH);
		int height = gameState.getValue(SystemFields.GAME_HEIGHT);
		canvas = new Canvas();
		canvas.setSize(width, height);
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				finish();
			}
		});

		// Prepare Gdx configuration
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ead-engine";
		cfg.useGL20 = true;
		cfg.width = width;
		cfg.height = height;
		cfg.fullscreen = gameState.getValue(SystemFields.FULLSCREEN);
		cfg.forceExit = gameState.getValue(SystemFields.EXIT_WHEN_CLOSE);

		// Frame needs to be visible so Gdx can create the right context
		frame.setVisible(true);
		new LwjglApplication(engine, cfg, canvas);

		// Set transparent mouse
		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				try {
					Mouse.setNativeCursor(new Cursor(16, 16, 0, 0, 1,
							getCursor(), null));
				} catch (LWJGLException e) {

				}
			}
		});
	}

	@Override
	public void showSpecialResource(Object object, int x, int y,
			boolean fullscreen) {
		if (object == component) {

		} else if (object == null) {
			SwingUtilities.doInEDTNow(new Runnable() {
				@Override
				public void run() {
					canvas.setCursor(java.awt.Cursor.getDefaultCursor());
					canvas.setVisible(true);
					if (component != null) {
						frame.remove((Component) component);
					}
					frame.validate();
				}
			});
		} else if (object != component) {
			component = (Component) object;
			SwingUtilities.doInEDTNow(new Runnable() {
				@Override
				public void run() {
					canvas.setVisible(false);
					canvas.setCursor(null);

					frame.getContentPane().setFocusable(true);

					frame.add((Component) component);
					frame.validate();

				}
			});
		}
	}

	private IntBuffer getCursor() {
		BufferedImage biCursor = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_ARGB);
		int[] data = biCursor.getRaster().getPixels(0, 0, 16, 16, (int[]) null);

		IntBuffer ib = BufferUtils.createIntBuffer(16 * 16);
		for (int i = 0; i < data.length; i += 4) {
			ib.put(data[i] | data[i + 1] << 8 | data[i + 2] << 16
					| data[i + 3] << 24);
		}
		ib.flip();
		return ib;
	}

	@Override
	public void finish() {
		if (frame != null) {
			frame.setVisible(false);
		}
		super.finish();
	}

	/**
	 * Returns the JFrame holding the engine
	 * 
	 * @return
	 */
	public JFrame getFrame() {
		return frame;
	}
}
