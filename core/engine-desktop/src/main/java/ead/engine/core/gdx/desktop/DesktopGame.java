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

package ead.engine.core.gdx.desktop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.operations.SystemFields;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gdx.desktop.platform.GdxDesktopGUI;
import ead.engine.core.gdx.desktop.platform.GdxDesktopModule;
import ead.tools.java.JavaToolsModule;
import ead.tools.java.reflection.JavaReflectionClassLoader;
import ead.tools.reflection.ReflectionClassLoader;

public class DesktopGame {

	private Injector injector;

	private boolean exitAtClose;

	private Map<Class<?>, Class<?>> binds;

	private List<Class<? extends SceneElementGO>> debuggers;

	private String resourcesLocation;

	public DesktopGame(boolean exitAtClose) {
		this.exitAtClose = exitAtClose;
		this.binds = new HashMap<Class<?>, Class<?>>();
		debuggers = new ArrayList<Class<? extends SceneElementGO>>();
	}

	public void setModel(String path) {
		this.resourcesLocation = path;
	}

	public void start() {
		injector = Guice.createInjector(new GdxDesktopModule(binds),
				new JavaToolsModule());

		GameState gameState = injector.getInstance(GameState.class);
		gameState.setValue(SystemFields.EXIT_WHEN_CLOSE, exitAtClose);
		Game g = injector.getInstance(Game.class);
		g.setResourcesLocation(resourcesLocation);
		ReflectionClassLoader.init(new JavaReflectionClassLoader());
		ApplicationListener engine = injector
				.getInstance(ApplicationListener.class);

		// Prepare Gdx configuration
		int width = gameState.getValue(SystemFields.GAME_WIDTH);
		int height = gameState.getValue(SystemFields.GAME_HEIGHT);
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ead-engine";
		cfg.useGL20 = true;
		cfg.width = width;
		cfg.height = height;
		cfg.fullscreen = gameState.getValue(SystemFields.FULLSCREEN);
		cfg.forceExit = gameState.getValue(SystemFields.EXIT_WHEN_CLOSE);

		GdxDesktopGUI gui = (GdxDesktopGUI) injector.getInstance(GUI.class);
		gui.create(width, height);
		new LwjglApplication(engine, cfg, gui.getCanvas());
	}

	public DesktopGame() {
		this(true);
	}

	public void addDebugger(Class<? extends SceneElementGO> debuggerClass) {
		debuggers.add(debuggerClass);
	}

	public void setResourcesLocation(String path) {
		this.resourcesLocation = path;
	}

	public void exit() {
		injector.getInstance(GUI.class).finish();
	}

}
