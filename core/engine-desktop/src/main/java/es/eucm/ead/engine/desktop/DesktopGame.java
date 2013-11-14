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

package es.eucm.ead.engine.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import es.eucm.ead.engine.EAdEngine;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.desktop.platform.DesktopGUI;
import es.eucm.ead.engine.desktop.platform.DesktopModule;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.tools.java.JavaToolsModule;
import es.eucm.ead.tools.java.reflection.JavaReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class DesktopGame {

	protected Injector injector;

	private Map<TypeLiteral<?>, Class<?>> binds;

	private String path;

	protected boolean exitAtClose;

	protected int windowWidth = 800;

	protected int windowHeight = 600;

	protected boolean fullscreen;
	protected boolean debug;

	/**
	 * Creates a desktop game that ends when the user press the exit button
	 */
	public DesktopGame() {
		this(true);
		binds = new HashMap<TypeLiteral<?>, Class<?>>();
	}

	/**
	 * Creates a desktop game
	 *
	 * @param exitAtClose whether the application should end win the user press the exit button
	 */
	public DesktopGame(boolean exitAtClose) {
		this(exitAtClose, null);
	}

	public DesktopGame(boolean exitAtClose, String path) {
		this.exitAtClose = exitAtClose;
		setPath(path);
	}

	public void initInjector() {
		if (injector == null) {
			injector = Guice.createInjector(new DesktopModule(binds),
					new JavaToolsModule());
			// If we have the resources in a path
			if (path != null) {
				AssetHandler assetHandler = injector
						.getInstance(AssetHandler.class);
				assetHandler.setResourcesLocation(path);
			}
		}
	}

	public void setPath(String path) {
		this.path = path;
		if (injector != null) {
			AssetHandler assetHandler = injector
					.getInstance(AssetHandler.class);
			assetHandler.setResourcesLocation(path);
		}
	}

	/**
	 * Creates the frame that contains the game
	 *
	 * @return
	 */
	public JFrame start() {
		initInjector();
		// Init class loader
		ReflectionClassLoader.init(new JavaReflectionClassLoader());
		// Prepare Gdx configuration
		int width = windowWidth;
		int height = windowHeight;
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "";
		cfg.useGL20 = true;
		cfg.width = width;
		cfg.height = height;
		cfg.fullscreen = fullscreen;
		cfg.forceExit = this.exitAtClose;
		DesktopGUI gui = (DesktopGUI) injector.getInstance(GUI.class);
		final EAdEngine engine = (EAdEngine) injector
				.getInstance(ApplicationListener.class);
		engine.setDebug(debug);

		JFrame frame = null;
		if (cfg.fullscreen) {
			cfg.setFromDisplayMode(LwjglApplicationConfiguration
					.getDesktopDisplayMode());
			new LwjglApplication(engine, cfg);
		} else {
			new LwjglApplication(engine, cfg, gui.createCanvas(width, height,
					exitAtClose));
			frame = gui.getFrame();
		}
		return frame;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public <T> void setBind(Class<T> clazz, Class<?> bindTo) {
		binds.put(TypeLiteral.get(clazz), bindTo);
	}

	public void setBind(TypeLiteral<?> type, Class<?> bindTo) {
		binds.put(type, bindTo);
	}

	public Injector getInjector() {
		return injector;
	}
}
