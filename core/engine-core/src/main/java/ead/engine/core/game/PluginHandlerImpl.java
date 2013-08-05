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

package ead.engine.core.game;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.assets.AssetHandler;
import ead.engine.core.factories.EffectGOFactory;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.PluginHandler;
import es.eucm.ead.tools.PropertiesReader;
import es.eucm.ead.tools.reflection.ReflectionProvider;

@Singleton
public class PluginHandlerImpl implements PluginHandler {

	public static final int EFFECTS = 0, EVENTS = 1, SCENE_ELEMENTS = 2;

	protected ReflectionProvider reflectionProvider;

	protected AssetHandler assetHandler;

	protected EffectGOFactory effectFactory;

	protected SceneElementGOFactory sceneElementFactory;

	protected EventGOFactory eventFactory;

	private static final String PLUGINS_TXT = "@plugins.txt";

	private static final String GDX_PLUGIN_TXT = "@gdx/plugins.txt";

	private static final Logger logger = LoggerFactory
			.getLogger("PluginHandler");

	@Inject
	public PluginHandlerImpl(ReflectionProvider reflectionProvider,
			AssetHandler assetHandler, EffectGOFactory effectFactory,
			SceneElementGOFactory sceneElementFactory,
			EventGOFactory eventGOFactory) {
		this.reflectionProvider = reflectionProvider;
		this.assetHandler = assetHandler;
		this.effectFactory = effectFactory;
		this.sceneElementFactory = sceneElementFactory;
		this.eventFactory = eventGOFactory;
	}

	@Override
	public void initialize() {
		load(PLUGINS_TXT);
		load(GDX_PLUGIN_TXT);
	}

	protected void load(String pluginsFile) {
		String text = assetHandler.getTextFile(pluginsFile);
		Map<String, Map<String, String>> map = PropertiesReader.parse(
				"Plugins", text);
		for (Entry<String, Map<String, String>> e : map.entrySet()) {
			if (e.getKey() == null) {
				logger.warn("All plugins with no section were ignored: {}", e
						.getValue());
			} else if (e.getKey().equals("effects")) {
				loadClasses(EFFECTS, e.getValue());
			} else if (e.getKey().equals("events")) {
				loadClasses(EVENTS, e.getValue());
			} else if (e.getKey().equals("sceneElement")) {
				loadClasses(SCENE_ELEMENTS, e.getValue());
			} else {
				logger.warn("Unkown section {} while loading plugins", e
						.getKey());
			}
		}
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	private void loadClasses(int section, Map<String, String> classes) {
		for (Entry<String, String> e : classes.entrySet()) {
			String key = e.getKey();
			String value = e.getValue();

			Class c1 = reflectionProvider.getClassforName(key);
			if (c1 == null) {
				logger.warn(
						"Class for string {} not found. Plugin won't load.",
						key);
				continue;
			}

			Class c2 = reflectionProvider.getClassforName(value);
			if (c2 == null) {
				logger.warn(
						"Class for string {} not found. Plugin won't load.",
						value);
				continue;
			}

			if (section == -1) {
				logger
						.warn("{}={} is declared out of any section. It won't be loaded.");
			} else {
				switch (section) {
				case EFFECTS:
					effectFactory.put(c1, c2);
					break;
				case EVENTS:
					eventFactory.put(c1, c2);
					break;
				case SCENE_ELEMENTS:
					sceneElementFactory.put(c1, c2);
					break;
				default:
					logger
							.warn("{}={} is declared out of any known section. It won't be loaded.");
				}
			}
		}
	}

}
