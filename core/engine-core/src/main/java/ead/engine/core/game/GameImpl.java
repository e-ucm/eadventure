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
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.assets.AbstractAssetDescriptor;
import ead.common.model.assets.AssetDescriptor;
import ead.common.model.assets.multimedia.Video;
import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.huds.MouseHud;
import ead.common.model.elements.operations.SystemFields;
import ead.common.model.elements.predef.LoadingScreen;
import ead.common.model.params.text.EAdString;
import ead.common.model.params.variables.VarDef;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.enginefilters.EngineFilter;
import ead.engine.core.game.enginefilters.EngineStringFilter;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.game.interfaces.PluginHandler;
import ead.engine.core.gameobjects.debuggers.DebuggersHandler;
import ead.engine.core.gameobjects.events.EventGO;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.tracking.GameTracker;
import ead.reader.AdventureReader;
import ead.reader.model.XMLVisitor.VisitorListener;
import ead.reader.strings.StringsReader;
import ead.tools.PropertiesReader;
import ead.tools.SceneGraph;
import ead.tools.StringHandler;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLParser;

@Singleton
public class GameImpl implements Game, VisitorListener {

	private static final Logger logger = LoggerFactory.getLogger("GameImpl");

	public static final String FILTER_STRING_FILES = "stringFiles";

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
	 * Game tracker
	 */
	private GameTracker tracker;

	private AdventureReader reader;

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

	private boolean readingXML;

	private boolean readingAssets;

	private Stack<AssetDescriptor> assetsToLoad;

	private int assetsToLoadNumber;

	private boolean firstUpdate;

	private DebuggersHandler debuggersHandler;

	@Inject
	public GameImpl(GUI gui, StringHandler stringHandler,
			PluginHandler pluginHandler, GameState gameState,
			SceneElementGOFactory sceneElementFactory,
			AssetHandler assetHandler, EventGOFactory eventFactory,
			GameTracker tracker, SceneGraph sceneGraph,
			StringsReader stringsReader, AdventureReader reader,
			XMLParser xmlReader, DebuggersHandler debuggersHandler) {
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
		this.reader = reader;
		this.debuggersHandler = debuggersHandler;
		this.adventure = new BasicAdventureModel();
		filters = new HashMap<String, List<EngineFilter<?>>>();
		events = new ArrayList<EventGO<?>>();
		assetsToLoad = new Stack<AssetDescriptor>();
	}

	@Override
	public void setResourcesLocation(String path) {
		this.path = path;
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
		// Set assets root
		assetHandler.setResourcesLocation(path);
		// Adds filters
		addFilters();
		// Load game properties
		processProperties(assetHandler.getTextFile(DEFAULT_PROPERTIES));

		// It is necessary to load the default properties before set up
		// GUI initialization
		gui.initialize(GameImpl.this, gameState, sceneElementFactory,
				debuggersHandler);

		// Load strings
		loadStrings();
		// Read model
		readModel();
		assetHandler.initialize();
		pluginHandler.initialize();
	}

	@Override
	public void dispose() {
		tracker.stop();
		gui.finish();
	}

	@Override
	public void act(float delta) {
		if (readingXML) {
			if (firstUpdate) {
				LoadingScreen loadingScreen = new LoadingScreen();
				gui.setScene((SceneGO) sceneElementFactory.get(loadingScreen));
				firstUpdate = false;
			}
			for (int i = 0; i < 200; i++) {
				if (reader.step()) {
					break;
				}
			}

			if (reader.step()) {
				gameState.setValue(SystemFields.LOADING, 30);
				readingAssets = true;
				readingXML = false;
				assetsToLoad.addAll(reader.getAssets());
				logger.info("Loading {} assets", reader.getAssets().size());
				assetsToLoadNumber = reader.getAssets().size();
			}
		} else if (readingAssets) {
			for (int i = 0; i < 10; i++) {
				if (assetsToLoad.isEmpty()) {
					readingAssets = false;
					setGame();
					break;
				}
				AssetDescriptor a = assetsToLoad.pop();
				if (!(a instanceof Video)) {
					assetHandler.getRuntimeAsset(a);
				}
			}
			if (assetsToLoadNumber > 0) {
				gameState.setValue(SystemFields.LOADING, (int) (30.0f + 70.f
						* (float) (assetsToLoadNumber - assetsToLoad.size())
						/ (float) assetsToLoadNumber));
			} else {
				gameState.setValue(SystemFields.LOADING, 100);
			}
		} else {
			gameState.update(delta);

			// Update language. Check this every loop is probably too much
			updateLanguage();

			// We load one possible asset in the background
			assetHandler.loadStep();

			gameState.setValue(SystemFields.ELAPSED_TIME_PER_UPDATE, gui
					.getSkippedMilliseconds());

			// Scene
			if (!gameState.isPaused()) {
				updateGameEvents(delta);
			}
		}

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

	private void setGame() {
		if (adventure != null) {
			BasicElement.idPrefix = AbstractAssetDescriptor.idPrefix = "eng";
			currentChapter = adventure.getChapters().get(0);
			ChangeSceneEf initGame = new ChangeSceneEf(currentChapter
					.getInitialScene());
			gameState.addEffect(initGame);
		}

		logger.info("Init game events...");
		events.clear();
		for (EAdEvent e : currentChapter.getEvents()) {
			EventGO<?> eventGO = eventFactory.get(e);
			eventGO.setParent(null);
			eventGO.initialize();
			events.add(eventGO);
		}

		for (EAdEvent e : adventure.getEvents()) {
			EventGO<?> eventGO = eventFactory.get(e);
			eventGO.setParent(null);
			eventGO.initialize();
			events.add(eventGO);
		}

		// Start tracking
		Boolean track = Boolean.parseBoolean(adventure.getProperties().get(
				GameTracker.TRACKING_ENABLE));
		if (track) {
			tracker.startTracking(adventure);
		}
		// Set mouse visible
		sceneElementFactory.get(MouseHud.CURSOR_ID).setVisible(true);
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
	public boolean loaded(XMLNode node, Object object, boolean isNullInOrigin) {
		this.adventure = (EAdAdventureModel) object;
		return true;
	}

	private void readModel() {
		reader.readXML(assetHandler.getTextFile("@data.xml"), this);
		firstUpdate = true;
		readingXML = true;
	}

}
