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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.VarDef;
import ead.common.params.text.EAdString;
import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.game.enginefilters.EngineFilter;
import ead.engine.core.game.enginefilters.EngineStringFilter;
import ead.engine.core.game.enginefilters.HudsCreationFilter;
import ead.engine.core.gameobjects.events.EventGO;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.huds.HudGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.input.InputHandler;
import ead.engine.core.inventory.InventoryHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.LoadingScreen;
import ead.engine.core.platform.TweenController;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.AssetHandler.TextHandler;
import ead.engine.core.plugins.PluginHandler;
import ead.engine.core.tracking.GameTracker;
import ead.reader.strings.StringsReader;
import ead.tools.PropertiesReader;
import ead.tools.SceneGraph;
import ead.tools.StringHandler;

@Singleton
public class GameImpl implements Game, TextHandler {

	private static final Logger logger = LoggerFactory.getLogger("GameImpl");

	public static final String FILTER_STRING_FILES = "stringFiles";

	public static final String FILTER_HUDS_CREATION = "huds_creation";

	public static final String FILTER_PROCESS_ACTION = "action_generated";

	/**
	 * Default properties file. Loaded during initialization
	 */
	private static final String DEFAULT_PROPERTIES = "ead/engine/resources/ead.properties";

	/**
	 * Game gui
	 */
	private GUI gui;

	/**
	 * String handler
	 */
	private StringHandler stringHandler;

	/**
	 * Input handler
	 */
	private InputHandler inputHandler;

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
	 * Current adventure model
	 */
	private EAdAdventureModel adventure;

	/**
	 * Engine filters
	 */
	private Map<String, List<EngineFilter<?>>> filters;

	private EAdChapter currentChapter;

	private DebuggerHandler debuggerHandler;

	private InventoryHandler inventoryHandler;

	private EventGOFactory eventFactory;

	private List<EventGO<?>> events;

	private GameTracker tracker;

	private SceneGraph sceneGraph;

	private GameLoader gameLoader;

	private TweenController tweenController;

	private StringsReader stringsReader;

	private String currentLanguage = "";

	@Inject
	public GameImpl(GUI gui, StringHandler stringHandler,
			InputHandler inputHandler, PluginHandler pluginHandler,
			GameState gameState, SceneElementGOFactory sceneElementFactory,
			AssetHandler assetHandler, DebuggerHandler debugger,
			ValueMap valueMap, InventoryHandler inventoryHandler,
			EventGOFactory eventFactory, GameTracker tracker,
			SceneGraph sceneGraph, TweenController tweenController,
			StringsReader stringsReader) {
		this.gui = gui;
		this.stringHandler = stringHandler;
		this.inputHandler = inputHandler;
		this.sceneElementFactory = sceneElementFactory;
		this.gameState = gameState;
		this.assetHandler = assetHandler;
		this.pluginHandler = pluginHandler;
		this.stringsReader = stringsReader;
		this.adventure = null;
		this.debuggerHandler = debugger;
		this.inventoryHandler = inventoryHandler;
		this.eventFactory = eventFactory;
		this.tracker = tracker;
		this.adventure = new BasicAdventureModel();
		this.sceneGraph = sceneGraph;
		events = new ArrayList<EventGO<?>>();
		this.tweenController = tweenController;

		filters = new HashMap<String, List<EngineFilter<?>>>();

	}

	@Override
	public void initialize() {
		loadDefaultProperties();
	}

	@Override
	public void setUp() {
		addFilters();

		assetHandler.initialize();

		// Load strings
		loadStrings();

		// Load plugins
		pluginHandler.initialize();

		// HUDs
		List<HudGO> huds = new ArrayList<HudGO>();
		huds = applyFilters(FILTER_HUDS_CREATION, huds, new Object[] {
				assetHandler, sceneElementFactory, gui, gameState,
				eventFactory, this });
		for (HudGO h : huds) {
			//			gui.addHud(h);
		}

		for (SceneElementGO<?> hud : gui.getHUDs()) {
			((HudGO) hud).init();
		}

		LoadingScreen loadingScreen = new LoadingScreen();
		gui.setScene((SceneGO) sceneElementFactory.get(loadingScreen));

		setGame();
	}

	private void loadDefaultProperties() {
		assetHandler.getTextfileAsync(DEFAULT_PROPERTIES, this);
	}

	@Override
	public void handle(String text) {
		processProperties(text);
		// It is necessary to load the default properties before set up
		// GUI initialization
		gui.initialize(this, gameState, sceneElementFactory, inputHandler);
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
				stringHandler.setLanguage(language);
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
	public void dispose() {
		gui.finish();
		tracker.stop();
	}

	@Override
	public void update() {
		inputHandler.processActions();

		// Update language. Check this every loop is probably too much
		updateLanguage();

		// We load one possible asset in the background
		// assetHandler.loadStep();
		// We load some possible game
		// gameLoader.step();

		gameState.setValue(SystemFields.ELAPSED_TIME_PER_UPDATE, gui
				.getSkippedMilliseconds());

		// Scene
		if (!gameState.isPaused()) {
			updateGameEvents();
			gui.update();
			tweenController.update(gui.getSkippedMilliseconds());
		}

	}

	private void updateLanguage() {
		String newLanguage = gameState.getValue(SystemFields.LANGUAGE);

		if (newLanguage != null && !newLanguage.equals(currentLanguage)) {
			currentLanguage = newLanguage;
			stringHandler.setLanguage(currentLanguage);
			assetHandler.refresh();
		}

	}

	private void updateGameEvents() {
		Long l = gameState.getValue(SystemFields.GAME_TIME);
		l += gui.getSkippedMilliseconds();
		gameState.setValue(SystemFields.GAME_TIME, l);

		for (EventGO<?> e : events) {
			e.update();
		}
	}

	@Override
	public void render() {
		gui.commit();
	}

	@Override
	public EAdAdventureModel getAdventureModel() {
		return adventure;
	}

	private void setGame() {
		if (adventure != null) {
			SceneGO scene = (SceneGO) sceneElementFactory.get(adventure
					.getChapters().get(0).getInitialScene());
			gui.setScene(scene);
		}
		// logger.info("Setting the game");
		// gameState.setValue(SystemFields.GAME_WIDTH,
		// adventure.getGameWidth());
		// gameState.setValue(SystemFields.GAME_HEIGHT,
		// adventure.getGameHeight());
		//
		// if (adventure.getInventory() != null) {
		// logger.info("Building inventory...");
		// for (EAdSceneElementDef def : adventure.getInventory()
		// .getInitialItems())
		// inventoryHandler.add(def);
		// inventoryHUD.setVisible(true);
		// } else {
		// inventoryHUD.setVisible(false);
		// }
		//
		// logger.info("Init game events...");
		// events.clear();
		// for (EAdEvent e : currentChapter.getEvents()) {
		// EventGO<?> eventGO = eventFactory.get(e);
		// eventGO.setParent(null);
		// eventGO.initialize();
		// events.add(eventGO);
		// }
		//
		// // Set the debuggers
		// setDebuggers(adventure);
		//
		// sceneGraph.generateGraph(currentChapter.getInitialScene());
		//
		// updateInitialTransformation();
		//
		// // Start tracking
		// Boolean track = Boolean.parseBoolean(adventure
		// .getProperties().get(GameTracker.TRACKING_ENABLE));
		// if (track) {
		// tracker.startTracking(adventure);
		// }

	}

	private void setDebuggers(EAdAdventureModel model) {
		if (debuggerHandler != null) {
			debuggerHandler.setUp(model);
		}
	}

	@Override
	public EAdChapter getCurrentChapter() {
		return currentChapter;
	}

	@Override
	public void setAdventureModel(EAdAdventureModel model) {
		this.adventure = model;
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

	private void addFilters() {
		addFilter(FILTER_STRING_FILES, new EngineStringFilter());
		addFilter(FILTER_HUDS_CREATION, new HudsCreationFilter());
	}

}
