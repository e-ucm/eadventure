package ead.engine.core.plugins;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.gameobjects.factories.EffectGOFactory;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.platform.assets.AssetHandler;
import ead.tools.PropertiesReader;
import ead.tools.reflection.ReflectionProvider;

@Singleton
public class PluginHandlerImpl implements PluginHandler {

	public static final int EFFECTS = 0, EVENTS = 1,
			SCENE_ELEMENTS = 2;

	protected ReflectionProvider reflectionProvider;

	protected AssetHandler assetHandler;

	protected EffectGOFactory effectFactory;

	protected SceneElementGOFactory sceneElementFactory;

	protected EventGOFactory eventGOFactory;

	private static final String PLUGINS_TXT = "@plugins.txt";

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
		this.eventGOFactory = eventGOFactory;
	}

	@Override
	public void initialize() {
		load(PLUGINS_TXT);

	}

	protected void load(String pluginsFile) {
		String text = assetHandler.getTextFile(pluginsFile);
		Map<String, Map<String, String>> map = PropertiesReader
				.parse("Plugins", text);
		for (Entry<String, Map<String, String>> e : map.entrySet()) {
			if (e.getKey() == null) {
				logger.warn(
						"All plugins with no section were ignored: {}",
						e.getValue());
			} else if (e.getKey().equals("effects")) {
				loadClasses(EFFECTS, e.getValue());
			} else if (e.getKey().equals("events")) {
				loadClasses(EVENTS, e.getValue());
			} else if (e.getKey().equals("sceneElement")) {
				loadClasses(SCENE_ELEMENTS, e.getValue());
			} else {
				logger.warn(
						"Unkown section {} while loading plugins",
						e.getKey());
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
				logger.warn("{}={} is declared out of any section. It won't be loaded.");
			} else {
				switch (section) {
				case EFFECTS:
					effectFactory.put(c1, c2);
					break;
				case EVENTS:
					eventGOFactory.put(c1, c2);
					break;
				case SCENE_ELEMENTS:
					sceneElementFactory.put(c1, c2);
					break;
				default:
					logger.warn("{}={} is declared out of any known section. It won't be loaded.");
				}
			}
		}
	}

}
