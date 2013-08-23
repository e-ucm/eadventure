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

package es.eucm.ead.engine.game;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.game.enginefilters.EngineFilter;
import es.eucm.ead.engine.game.enginefilters.EngineHook;
import es.eucm.ead.engine.game.enginefilters.EngineStringFilter;
import es.eucm.ead.engine.game.interfaces.*;
import es.eucm.ead.engine.gameobjects.events.EventGO;
import es.eucm.ead.engine.tracking.GameTracker;
import es.eucm.ead.model.elements.BasicAdventureModel;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.EAdChapter;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.variables.VarDef;
import es.eucm.ead.reader.strings.StringsReader;
import es.eucm.ead.tools.PropertiesReader;
import es.eucm.ead.tools.StringHandler;
import es.eucm.ead.tools.xml.XMLNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

@Singleton
public class GameImpl implements Game {

	private static final Logger logger = LoggerFactory.getLogger("GameImpl");

	public static final String FILTER_STRING_FILES = "stringFiles";

	public static final String FILTER_PROCESS_ACTION = "action_generated";

	public static final String HOOK_AFTER_UPDATE = "after_update";

	public static final String HOOK_AFTER_MODEL_READ = "after_model_read";

	public static final String HOOK_AFTER_CHAPTER_READ = "after_chapter_read";

	public static final String HOOK_AFTER_RENDER = "after_render";

	private GameLoader gameLoader;

	/**
	 * Game gui
	 */
	private GUI gui;

	/**
	 * String handler
	 */
	private StringHandler stringHandler;

	/**
	 * Plugin handler
	 */
	private PluginHandler pluginHandler;

	/**
	 * Scene element game objects factory
	 */
	private SceneElementGOFactory sceneElementFactory;

	/**
	 * Asset handler
	 */
	private AssetHandler assetHandler;

	/**
	 * Game state
	 */
	private GameState gameState;

	/**
	 * Events factory
	 */
	private EventGOFactory eventFactory;

	/**
	 * Engine filters
	 */
	private Map<String, List<EngineFilter<?>>> filters;

	/**
	 * Engine hooks
	 */
	private Map<String, List<EngineHook>> hooks;

	/**
	 * Game tracker
	 */
	private GameTracker tracker;

	/**
	 * Strings reader
	 */
	private StringsReader stringsReader;

	/**
	 * Assets path root
	 */
	private String path;

	/**
	 * Current adventure model
	 */
	private EAdAdventureModel adventure;

	private EAdChapter currentChapter;

	private List<EventGO<?>> events;

	private String currentLanguage = "";

	private SoundManager soundManager;

	private TweenManager tweenManager;

	// Aux
	private ArrayList<String> hookNameDelete;
	private ArrayList<EngineHook> hookDelete;
	private ArrayList<String> filterNameDelete;
	private ArrayList<EngineFilter<?>> filterDelete;

	@Inject
	public GameImpl(GUI gui, StringHandler stringHandler,
			PluginHandler pluginHandler, GameState gameState,
			SceneElementGOFactory sceneElementFactory,
			AssetHandler assetHandler, EventGOFactory eventFactory,
			GameTracker tracker, StringsReader stringsReader,
			SoundManager soundManager, GameLoader gameLoader,
			TweenManager tweenManager) {
		this.gui = gui;
		this.stringHandler = stringHandler;
		this.sceneElementFactory = sceneElementFactory;
		this.gameState = gameState;
		this.assetHandler = assetHandler;
		this.pluginHandler = pluginHandler;
		this.stringsReader = stringsReader;
		this.adventure = null;
		this.eventFactory = eventFactory;
		this.tracker = tracker;
		this.soundManager = soundManager;
		this.adventure = new BasicAdventureModel();
		this.gameLoader = gameLoader;
		// Init tween manager
		this.tweenManager = new TweenManager();
		Tween.registerAccessor(EAdField.class, gameState);
		Tween.registerAccessor(BasicField.class, gameState);
		filters = new HashMap<String, List<EngineFilter<?>>>();
		hooks = new HashMap<String, List<EngineHook>>();
		events = new ArrayList<EventGO<?>>();
		// Aux
		hookNameDelete = new ArrayList<String>();
		hookDelete = new ArrayList<EngineHook>();
		filterNameDelete = new ArrayList<String>();
		filterDelete = new ArrayList<EngineFilter<?>>();
	}

	@Override
	public EAdAdventureModel getAdventureModel() {
		return adventure;
	}

	@Override
	public EAdChapter getCurrentChapter() {
		return currentChapter;
	}

	@Override
	public void initialize() {
		// Adds filters
		addFilters();

		// It is necessary to load the default properties before set up
		// GUI initialization
		gui.initialize(GameImpl.this, gameState, sceneElementFactory);

		pluginHandler.initialize();
	}

	@Override
	public void dispose() {
		tracker.stop();
		tweenManager.killAll();
	}

	@Override
	public void act(float delta) {
		gameLoader.act(delta);
		// Tween manager
		tweenManager.update(delta);
		// Remove hooks and filters
		for (int i = 0; i < this.filterNameDelete.size(); i++) {
			removeFilterImpl(filterNameDelete.get(i), filterDelete.get(i));
		}
		filterNameDelete.clear();
		filterDelete.clear();
		for (int i = 0; i < this.hookNameDelete.size(); i++) {
			removeHookImpl(hookNameDelete.get(i), hookDelete.get(i));
		}
		hookNameDelete.clear();
		hookDelete.clear();

		gameState.update(delta);

		// TODO Update language. Check this every loop is probably too much
		updateLanguage();

		gameState.setValue(SystemFields.ELAPSED_TIME_PER_UPDATE, gui
				.getSkippedMilliseconds());

		// Scene
		if (!gameState.isPaused()) {
			updateGameEvents(delta);
		}

		doHook(HOOK_AFTER_UPDATE);
	}

	private void updateLanguage() {
		String newLanguage = gameState.getValue(SystemFields.LANGUAGE);

		if (newLanguage != null && !newLanguage.equals(currentLanguage)) {
			currentLanguage = newLanguage;
			stringHandler.setLanguage(currentLanguage);
			assetHandler.setLanguage(currentLanguage);
		}

	}

	private void updateGameEvents(float delta) {
		Long l = gameState.getValue(SystemFields.GAME_TIME);
		l += gui.getSkippedMilliseconds();
		gameState.setValue(SystemFields.GAME_TIME, l);

		for (EventGO<?> e : events) {
			e.act(delta);
		}
	}

	@Override
	public void addFilter(String filterName, EngineFilter<?> filter) {

		List<EngineFilter<?>> filtersList = filters.get(filterName);
		if (filtersList == null) {
			filtersList = new ArrayList<EngineFilter<?>>();
			filters.put(filterName, filtersList);
		}

		filtersList.add(filter);
		Collections.sort(filtersList);
	}

	public void removeFilter(String filterName, EngineFilter<?> filter) {
		this.filterNameDelete.add(filterName);
		this.filterDelete.add(filter);
	}

	private void removeFilterImpl(String filterName, EngineFilter<?> filter) {
		List<EngineFilter<?>> filtersList = filters.get(filterName);
		if (filtersList != null) {
			filtersList.remove(filter);
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	public <T> T applyFilters(String filterName, T o, Object[] params) {
		List<EngineFilter<?>> filtersList = filters.get(filterName);
		T result = o;
		if (filtersList != null) {
			for (EngineFilter f : filtersList) {
				result = (T) f.filter(o, params);
			}
		}
		return result;

	}

	public void addHook(String hookName, EngineHook hook) {
		List<EngineHook> hooksList = hooks.get(hookName);
		if (hooksList == null) {
			hooksList = new ArrayList<EngineHook>();
			hooks.put(hookName, hooksList);
		}

		hooksList.add(hook);
		Collections.sort(hooksList);
	}

	public void removeHook(String filterName, EngineHook hook) {
		this.hookNameDelete.add(filterName);
		this.hookDelete.add(hook);
	}

	private void removeHookImpl(String hookName, EngineHook hook) {
		List<EngineHook> hooksList = hooks.get(hookName);
		if (hooksList != null) {
			hooksList.remove(hook);
		}
	}

	public void doHook(String hookName) {
		List<EngineHook> hooksList = hooks.get(hookName);
		if (hooksList != null) {
			for (EngineHook h : hooksList) {
				h.execute(this, gameState, gui);
			}
		}
	}

	private void addFilters() {
		addFilter(FILTER_STRING_FILES, new EngineStringFilter());
	}

	private void processProperties(String text) {
		Map<String, Map<String, String>> map = PropertiesReader.parse(
				"defaultProperties", text);
		for (Entry<String, Map<String, String>> e : map.entrySet()) {
			String type = e.getKey();
			if (type == null || type.equals("String")) {
				for (Entry<String, String> e2 : e.getValue().entrySet()) {
					VarDef<String> varDef = new VarDef<String>(e2.getKey(),
							String.class, e2.getValue());
					gameState.setValue(null, varDef, e2.getValue());
				}
			} else if (type.equals("Integer")) {
				for (Entry<String, String> e2 : e.getValue().entrySet()) {
					try {
						Integer i = Integer.parseInt(e2.getValue());
						VarDef<Integer> varDef = new VarDef<Integer>(e2
								.getKey(), Integer.class, i);
						gameState.setValue(null, varDef, i);
					} catch (NumberFormatException ex) {
						logger.warn("{} is not a number valid for property {}",
								new Object[] { e2.getValue(), e2.getKey() });
					}
				}
			} else if (type.equals("Float")) {
				for (Entry<String, String> e2 : e.getValue().entrySet()) {
					try {
						Float i = Float.parseFloat(e2.getValue());
						VarDef<Float> varDef = new VarDef<Float>(e2.getKey(),
								Float.class, i);
						gameState.setValue(null, varDef, i);
					} catch (NumberFormatException ex) {
						logger.warn("{} is not a number valid for property {}",
								new Object[] { e2.getValue(), e2.getKey() });
					}
				}
			} else if (type.equals("Boolean")) {
				for (Entry<String, String> e2 : e.getValue().entrySet()) {
					try {
						Boolean b = Boolean.parseBoolean(e2.getValue());
						VarDef<Boolean> varDef = new VarDef<Boolean>(e2
								.getKey(), Boolean.class, b);
						gameState.setValue(null, varDef, b);
					} catch (NumberFormatException ex) {
						logger.warn("{} is not a number valid for property {}",
								new Object[] { e2.getValue(), e2.getKey() });
					}
				}
			} else {
				logger.warn("{} is not a valid type in ead.properties", type);
			}
		}
	}

	private void loadStrings() {
		stringHandler.clear();
		// Map containing all the files with strings (keys) and its associated
		// language (value)
		Map<String, String> stringFiles = new HashMap<String, String>();
		stringFiles = applyFilters(FILTER_STRING_FILES, stringFiles,
				new Object[] { gameState });

		for (Entry<String, String> file : stringFiles.entrySet()) {
			String strings = assetHandler.getTextFile(file.getKey());
			String language = file.getValue();
			if (strings == null || strings.equals("")) {
				logger
						.info(
								"{} language was not loaded. Maybe the strings.xml file associated is not present",
								language);
			} else {
				stringHandler.addLanguage(language);
				Map<EAdString, String> stringsMap = stringsReader
						.readStrings(strings);
				if (stringsMap != null) {
					stringHandler.addStrings(stringsMap);
					logger.info("{} language loaded", language);
				} else {
					logger.info("{} language not loaded. See previous erros.",
							language);
				}
			}
		}
	}

	@Override
	public boolean loaded(XMLNode node, Object object, boolean isNullInOrigin) {
		this.adventure = (EAdAdventureModel) object;
		return true;
	}

	@Override
	public SceneElementGOFactory getSceneElementFactory() {
		return sceneElementFactory;
	}

	public TweenManager getTweenManager() {
		return tweenManager;
	}
}
