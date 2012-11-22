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
import java.util.Map.Entry;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdEffect;
import ead.common.params.text.EAdString;
import ead.common.util.EAdURI;
import ead.engine.core.debuggers.Debugger;
import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.game.GameLoader;
import ead.engine.core.gameobjects.factories.EffectGOFactory;
import ead.engine.core.gameobjects.go.EffectGO;
import ead.engine.core.gdx.desktop.platform.GdxDesktopModule;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.tools.java.JavaToolsModule;

public class DesktopGame {

	private Injector injector;

	private boolean exitAtClose;

	private Map<Class<?>, Class<?>> binds;

	private List<Class<? extends Debugger>> debuggers;

	private String resourcesLocation;

	private Map<Class<? extends EAdEffect>, Class<? extends EffectGO<? extends EAdEffect>>> effectsPlugin;

	public DesktopGame(boolean exitAtClose) {
		this.exitAtClose = exitAtClose;
		this.binds = new HashMap<Class<?>, Class<?>>();
		debuggers = new ArrayList<Class<? extends Debugger>>();
		effectsPlugin = new HashMap<Class<? extends EAdEffect>, Class<? extends EffectGO<? extends EAdEffect>>>();
	}

	public <T> void setBind(Class<T> clazz, Class<? extends T> implementation) {
		binds.put(clazz, implementation);
	}

	private void prepare() {
		if (injector == null) {
			injector = Guice.createInjector(new GdxDesktopModule(binds),
					new JavaToolsModule());
			EngineConfiguration conf = injector
					.getInstance(EngineConfiguration.class);
			conf.setExitWhenFinished(exitAtClose);
			injector.getInstance(AssetHandler.class).setResourcesLocation(
					new EAdURI(resourcesLocation));
			if (debuggers.size() > 0) {
				DebuggerHandler debuggerHandler = injector
						.getInstance(DebuggerHandler.class);
				for (Class<? extends Debugger> d : debuggers) {
					debuggerHandler.add(d);
				}
			}

			// Add effects plugins
			EffectGOFactory effectFactory = injector
					.getInstance(EffectGOFactory.class);
			for (Entry<Class<? extends EAdEffect>, Class<? extends EffectGO<? extends EAdEffect>>> e : effectsPlugin
					.entrySet()) {
				effectFactory.put(e.getKey(), e.getValue());
			}
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
		GameLoader g = injector.getInstance(GameLoader.class);
		g.loadGameFromFiles(dataFile, stringsFile, propertiesFile);
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
		GameLoader g = injector.getInstance(GameLoader.class);
		if (strings == null) {
			strings = new HashMap<EAdString, String>();
		}

		if (properties == null) {
			properties = new HashMap<String, String>();
		}
		g.loadGame(model, strings, properties);
	}

	public void setResourcesLocation(String path) {
		this.resourcesLocation = path;
	}

	public void exit() {
		injector.getInstance(GUI.class).finish();
	}

	public void addEffectPlugin(Class<? extends EAdEffect> effect,
			Class<? extends EffectGO<? extends EAdEffect>> gameObject) {
		effectsPlugin.put(effect, gameObject);
	}

}
