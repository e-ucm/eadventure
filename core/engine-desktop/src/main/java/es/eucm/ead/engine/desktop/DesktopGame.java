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
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.desktop.debugger.DebuggerFrame;
import es.eucm.ead.engine.desktop.platform.DesktopGUI;
import es.eucm.ead.engine.desktop.platform.DesktopModule;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.game.interfaces.GameLoader;
import es.eucm.ead.model.elements.BasicAdventureModel;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.reader2.model.Manifest;
import es.eucm.ead.tools.java.JavaToolsModule;
import es.eucm.ead.tools.java.reflection.JavaReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;

import javax.swing.*;

public class DesktopGame {

	private Injector injector;

	private DebuggerFrame debuggerFrame;

	private GameLoader gameLoader;

	private boolean exitAtClose;

	private String path;

	/**
	 * Creates a desktop game that ends when the user press the exit button
	 */
	public DesktopGame() {
		this(true);
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
		this.path = path;
		initInjector();
	}

	private void initInjector() {
		if (injector == null)
			injector = Guice.createInjector(new DesktopModule(),
					new JavaToolsModule());
	}

	public void setPath(String path) {
		this.path = path;
	}

	public JFrame start() {
		// Init class loader
		ReflectionClassLoader.init(new JavaReflectionClassLoader());

		// If we have the resources in a path
		if (path != null) {
			AssetHandler assetHandler = injector
					.getInstance(AssetHandler.class);
			assetHandler.setResourcesLocation(path);
		}

		// Load the manifest
		gameLoader = injector.getInstance(GameLoader.class);
		Manifest manifest = gameLoader.loadManifest();
		EAdAdventureModel model = manifest.getModel();
		model.setVarInitialValue(BasicAdventureModel.EXIT_WHEN_CLOSE,
				this.exitAtClose);

		// Prepare Gdx configuration
		int width = model.getVarInitialValue(BasicAdventureModel.GAME_WIDTH);
		int height = model.getVarInitialValue(BasicAdventureModel.GAME_HEIGHT);
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = model.getVarInitialValue(BasicAdventureModel.GAME_TITLE);
		cfg.useGL20 = true;
		cfg.width = width;
		cfg.height = height;
		cfg.fullscreen = model
				.getVarInitialValue(BasicAdventureModel.FULLSCREEN);
		cfg.forceExit = model
				.getVarInitialValue(BasicAdventureModel.EXIT_WHEN_CLOSE);

		//	cfg.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());

		// FIXME This has to go
		DesktopGUI gui = (DesktopGUI) injector.getInstance(GUI.class);
		ApplicationListener engine = injector
				.getInstance(ApplicationListener.class);
		new LwjglApplication(engine, cfg, gui.getCanvas());
		//	new LwjglApplication(engine, cfg);
		return gui.getFrame();
	}

	public void load() {
		gameLoader.loadGame();
	}
}
