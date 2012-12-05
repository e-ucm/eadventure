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

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.text.EAdString;
import ead.common.util.EAdURI;
import ead.engine.core.debuggers.Debugger;
import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameLoader;
import ead.engine.core.game.GameState;
import ead.engine.core.gdx.desktop.platform.GdxDesktopModule;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.tools.java.JavaToolsModule;

public class DesktopGame {

	private Injector injector;

	private boolean exitAtClose;

	private Map<Class<?>, Class<?>> binds;

	private List<Class<? extends Debugger>> debuggers;

	private String resourcesLocation;

	private GameLoader loader;

	private EAdAdventureModel model;

	public DesktopGame(boolean exitAtClose) {
		this.exitAtClose = exitAtClose;
		this.binds = new HashMap<Class<?>, Class<?>>();
		debuggers = new ArrayList<Class<? extends Debugger>>();
	}

	public void setModel(EAdAdventureModel model) {
		this.model = model;
	}

	public void start() {
		injector = Guice.createInjector(new GdxDesktopModule(binds),
				new JavaToolsModule());
		Game g = injector.getInstance(Game.class);
		g.setAdventureModel(model);
		g.initialize();
	}

	public <T> void setBind(Class<T> clazz, Class<? extends T> implementation) {
		binds.put(clazz, implementation);
	}

	public GameLoader getPreparedLoader() {
		prepare();
		return loader;
	}

	private void prepare() {
		if (injector == null) {
			injector = Guice.createInjector(new GdxDesktopModule(binds),
					new JavaToolsModule());
			GameState g = injector.getInstance(GameState.class);
			g.setValue(SystemFields.EXIT_WHEN_CLOSE, exitAtClose);
			injector.getInstance(AssetHandler.class).setResourcesLocation(
					new EAdURI(resourcesLocation));
			if (debuggers.size() > 0) {
				DebuggerHandler debuggerHandler = injector
						.getInstance(DebuggerHandler.class);
				for (Class<? extends Debugger> d : debuggers) {
					debuggerHandler.add(d);
				}
			}

			loader = injector.getInstance(GameLoader.class);
		}
	}

	public DesktopGame() {
		this(true);
	}

	public void addDebugger(Class<? extends Debugger> debuggerClass) {
		debuggers.add(debuggerClass);
	}

	public void load(String dataFile, String stringsFile, String propertiesFile) {
		prepare();
		loader.loadGameFromFiles(dataFile, stringsFile, propertiesFile);
	}

	/**
	 * Loads a game from its model, with strings and properties.
	 * 
	 * @param model
	 *            the model
	 * @param strings
	 *            the game strings. It can be {@code null}
	 * @param properties
	 *            the game properties. It can be {@code null}
	 */
	public void load(EAdAdventureModel model, Map<EAdString, String> strings,
			Map<String, String> properties) {
		prepare();
		if (strings == null) {
			strings = new HashMap<EAdString, String>();
		}

		if (properties == null) {
			properties = new HashMap<String, String>();
		}
		loader.loadGame(model, strings, properties);
	}

	public void setResourcesLocation(String path) {
		this.resourcesLocation = path;
	}

	public void exit() {
		injector.getInstance(GUI.class).finish();
	}

}
