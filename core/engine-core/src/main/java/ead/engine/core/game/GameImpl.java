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
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.guievents.KeyGEv;
import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.guievents.enums.KeyGEvCode;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.VarDef;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.EffectGO;
import ead.engine.core.gameobjects.go.EventGO;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.huds.ActionsHUD;
import ead.engine.core.gameobjects.huds.BottomHUD;
import ead.engine.core.gameobjects.huds.EffectHUD;
import ead.engine.core.gameobjects.huds.HudGO;
import ead.engine.core.gameobjects.huds.InventoryHUD;
import ead.engine.core.gameobjects.huds.MenuHUD;
import ead.engine.core.gameobjects.huds.TopBasicHUD;
import ead.engine.core.input.InputHandler;
import ead.engine.core.inventory.InventoryHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.LoadingScreen;
import ead.engine.core.platform.TweenController;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.AssetHandler.TextHandler;
import ead.engine.core.plugins.PluginHandler;
import ead.engine.core.tracking.GameTracker;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;
import ead.reader.strings.StringsReader;
import ead.tools.PropertiesReader;
import ead.tools.SceneGraph;
import ead.tools.StringHandler;

@Singleton
public class GameImpl implements Game, TextHandler {

	/**
	 * Default properties file. Loaded during initialization
	 */
	private static final String DEFAULT_PROPERTIES = "ead/engine/resources/ead.properties";

	/**
	 * Default strings file. Loaded during initialization
	 */
	private static final String DEFAULT_STRINGS = "@strings";

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

	private EAdAdventureModel adventure;

	private EAdChapter currentChapter;

	private GameState gameState;

	private EffectHUD effectHUD;

	private InventoryHUD inventoryHUD;

	private ActionsHUD actionsHUD;

	private static final Logger logger = LoggerFactory
			.getLogger("GameImpl");

	// Auxiliary variable, to avoid new every time
	private static ArrayList<EffectGO<?>> finishedEffects = new ArrayList<EffectGO<?>>();

	private DebuggerHandler debuggerHandler;

	private InventoryHandler inventoryHandler;

	private EAdTransformation initialTransformation;

	private EventGOFactory eventFactory;

	private List<EventGO<?>> events;

	private GameTracker tracker;

	private SceneGraph sceneGraph;

	private GameLoader gameLoader;

	private TweenController tweenController;

	private EAdList<EAdEffect> launchEffects;

	private TopBasicHUD topBasicHUD;

	private StringsReader stringsReader;

	private MenuHUD menuHud;

	private String currentLanguage = "";

	@Inject
	public GameImpl(GUI gui, StringHandler stringHandler,
			InputHandler inputHandler, PluginHandler pluginHandler,
			GameState gameState,
			SceneElementGOFactory sceneElementFactory,
			EffectHUD effectHUD, AssetHandler assetHandler,
			GameObjectManager gameObjectManager,
			DebuggerHandler debugger, ValueMap valueMap,
			TopBasicHUD basicHud, InventoryHUD inventoryHud,
			InventoryHandler inventoryHandler,
			EventGOFactory eventFactory, ActionsHUD actionsHUD,
			GameTracker tracker, SceneGraph sceneGraph,
			TweenController tweenController,
			StringsReader stringsReader) {
		this.gui = gui;
		this.stringHandler = stringHandler;
		this.inputHandler = inputHandler;
		this.sceneElementFactory = sceneElementFactory;
		this.gameState = gameState;
		this.assetHandler = assetHandler;
		this.pluginHandler = pluginHandler;
		this.stringsReader = stringsReader;

		this.effectHUD = effectHUD;
		this.actionsHUD = actionsHUD;
		this.topBasicHUD = basicHud;

		this.adventure = null;
		this.debuggerHandler = debugger;
		this.inventoryHUD = inventoryHud;
		this.inventoryHandler = inventoryHandler;
		this.eventFactory = eventFactory;
		this.tracker = tracker;
		this.adventure = new BasicAdventureModel();
		this.sceneGraph = sceneGraph;
		events = new ArrayList<EventGO<?>>();
		this.tweenController = tweenController;

	}

	@Override
	public void initialize() {
		loadDefaultProperties();
	}

	@Override
	public void setUp() {
		assetHandler.initialize();

		// Load strings
		loadDefaultStrings();

		// Load plugins
		pluginHandler.initialize();

		// HUDs
		gui.addHud(new BottomHUD(assetHandler, sceneElementFactory,
				gui, gameState, eventFactory));
		// gui.addHud(inventoryHUD, 1);
		// gui.addHud(actionsHUD, 5);
		// gui.addHud(effectHUD, 10);
		menuHud = new MenuHUD(assetHandler, sceneElementFactory, gui,
				gameState, eventFactory);
		gui.addHud(menuHud);
		gui.addHud(topBasicHUD);

		for (HudGO hud : gui.getHUDs()) {
			hud.init();
		}

		updateInitialTransformation();
		inputHandler.setInitialTransformation(initialTransformation);

		LoadingScreen loadingScreen = new LoadingScreen();
		gameState.setScene((SceneGO<?>) sceneElementFactory
				.get(loadingScreen));

		// Add default events processor
		addDefaultInputProcessor();
		setGame();
	}

	private void loadDefaultProperties() {
		assetHandler.getTextfileAsync(DEFAULT_PROPERTIES, this);
	}

	private void addDefaultInputProcessor() {
		SceneElement defaultProcessor = new SceneElement();
		defaultProcessor.setId("ead.defaultProcessor");
		BasicField<Boolean> field = new BasicField<Boolean>(
				menuHud.getElement(), SceneElement.VAR_VISIBLE);

		defaultProcessor.addBehavior(new KeyGEv(
				KeyEventType.KEY_PRESSED, KeyGEvCode.ESCAPE),
				new ChangeFieldEf(field, new BooleanOp(new NOTCond(
						new OperationCond(field)))));

		gui.setDefaultInputActionProcessor(sceneElementFactory
				.get(defaultProcessor));
	}

	@Override
	public void handle(String text) {
		processProperties(text);
		// It is necessary to load the default properties before set up
		// GUI initialization
		gui.initialize(this, gameState);
	}

	private void processProperties(String text) {
		Map<String, Map<String, String>> map = PropertiesReader
				.parse("defaultProperties", text);
		for (Entry<String, Map<String, String>> e : map.entrySet()) {
			String type = e.getKey();
			if (type == null || type.equals("String")) {
				for (Entry<String, String> e2 : e.getValue()
						.entrySet()) {
					VarDef<String> varDef = new VarDef<String>(
							e2.getKey(), String.class, e2.getValue());
					gameState.getValueMap().setValue(null, varDef,
							e2.getValue());
				}
			} else if (type.equals("Integer")) {
				for (Entry<String, String> e2 : e.getValue()
						.entrySet()) {
					try {
						Integer i = Integer.parseInt(e2.getValue());
						VarDef<Integer> varDef = new VarDef<Integer>(
								e2.getKey(), Integer.class, i);
						gameState.getValueMap().setValue(null,
								varDef, i);
					} catch (NumberFormatException ex) {
						logger.warn(
								"{} is not a number valid for property {}",
								new Object[] { e2.getValue(),
										e2.getKey() });
					}
				}
			} else if (type.equals("Float")) {
				for (Entry<String, String> e2 : e.getValue()
						.entrySet()) {
					try {
						Float i = Float.parseFloat(e2.getValue());
						VarDef<Float> varDef = new VarDef<Float>(
								e2.getKey(), Float.class, i);
						gameState.getValueMap().setValue(null,
								varDef, i);
					} catch (NumberFormatException ex) {
						logger.warn(
								"{} is not a number valid for property {}",
								new Object[] { e2.getValue(),
										e2.getKey() });
					}
				}
			} else if (type.equals("Boolean")) {
				for (Entry<String, String> e2 : e.getValue()
						.entrySet()) {
					try {
						Boolean b = Boolean.parseBoolean(e2
								.getValue());
						VarDef<Boolean> varDef = new VarDef<Boolean>(
								e2.getKey(), Boolean.class, b);
						gameState.getValueMap().setValue(null,
								varDef, b);
					} catch (NumberFormatException ex) {
						logger.warn(
								"{} is not a number valid for property {}",
								new Object[] { e2.getValue(),
										e2.getKey() });
					}
				}
			} else {
				logger.warn(
						"{} is not a valid type in ead.properties",
						type);
			}
		}
	}

	private void loadDefaultStrings() {
		// Default strings
		String strings = assetHandler.getTextFile(DEFAULT_STRINGS
				+ ".xml");
		stringHandler.addStrings(stringsReader.readStrings(strings));

		String languagesProperty = gameState.getValueMap().getValue(
				null,
				new VarDef<String>("languages", String.class, null));
		if (languagesProperty != null) {
			String[] languages = languagesProperty.split(",");
			for (String language : languages) {
				strings = assetHandler.getTextFile(DEFAULT_STRINGS
						+ "-" + language + ".xml");
				if (strings == null) {
					logger.info(
							"{} language was not loaded. Maybe the strings.xml file associated is not present",
							language);
				} else {
					logger.info("{} language loaded", language);
					stringHandler.addLanguage(language);
					stringHandler.setLanguage(language);
					stringHandler.addStrings(stringsReader
							.readStrings(strings));
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

		// Update language. Update this every loop is probably too much
		updateLanguage();

		// We load one possible asset in the background
		// assetHandler.loadStep();
		// We load some possible game
		// gameLoader.step();

		gameState.getValueMap().setValue(
				SystemFields.ELAPSED_TIME_PER_UPDATE,
				gui.getSkippedMilliseconds());

		tweenController.update(gui.getSkippedMilliseconds());

		// Scene
		if (!gameState.isPaused()) {
			processEffects();
			updateGameEvents();
			gameState.getScene().update();
		}

		gui.addElement(gameState.getScene(), initialTransformation);

		// HUDs
		for (HudGO hud : gui.getHUDs()) {
			hud.update();
			if (hud.isVisible()) {
				gui.addElement(hud, initialTransformation);
			}
		}

		// updateDebuggers();

	}

	private void updateLanguage() {
		String newLanguage = gameState.getValueMap().getValue(
				SystemFields.LANGUAGE);

		if (newLanguage != null
				&& !newLanguage.equals(currentLanguage)) {
			currentLanguage = newLanguage;
			stringHandler.setLanguage(currentLanguage);
			assetHandler.clean(null);
		}

	}

	private void updateDebuggers() {
		if (debuggerHandler != null) {
			debuggerHandler.doLayout(gui, initialTransformation);
		}
	}

	private void updateGameEvents() {
		Long l = gameState.getValueMap().getValue(
				SystemFields.GAME_TIME);
		l += gui.getSkippedMilliseconds();
		gameState.getValueMap().setValue(SystemFields.GAME_TIME, l);

		for (EventGO<?> e : events) {
			e.update();
		}
	}

	private void processEffects() {
		gameState.updateEffectsQueue();
		finishedEffects.clear();
		boolean block = false;
		int i = 0;
		while (i < gameState.getEffects().size()) {
			EffectGO<?> effectGO = gameState.getEffects().get(i);
			i++;

			if (block)
				continue;

			if (effectGO.isStopped() || effectGO.isFinished()) {
				finishedEffects.add(effectGO);
				if (effectGO.isFinished())
					effectGO.finish();
			} else {
				if (effectGO.isBlocking())
					// If effect is blocking, get out of the loop
					block = true;

				effectGO.update();
			}

		}

		// Delete finished effects
		for (EffectGO<?> e : finishedEffects) {
			// logger.info("Finished or discarded effect {}", e.getClass());
			gameState.getEffects().remove(e);
		}

		boolean visualEffect = false;
		int index = 0;
		while (!visualEffect && index < gameState.getEffects().size()) {
			visualEffect = gameState.getEffects().get(index++)
					.isVisualEffect();
		}
		effectHUD.setVisible(visualEffect);
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
		if (adventure != null)
			gameState.setInitialScene(adventure.getChapters().get(0)
					.getInitialScene(), launchEffects);
		// logger.info("Setting the game");
		// gameState.getValueMap().setValue(SystemFields.GAME_WIDTH,
		// adventure.getGameWidth());
		// gameState.getValueMap().setValue(SystemFields.GAME_HEIGHT,
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

	private void updateInitialTransformation() {
		if (initialTransformation != null) {
			initialTransformation.setValidated(true);
		}

		// currentWidth = gameState.getValueMap().getValue(
		// SystemFields.GAME_WIDTH);
		// currentWidth = gameState.getValueMap().getValue(
		// SystemFields.GAME_HEIGHT);
		//
		// float scaleX = currentWidth
		// / (float) adventure.getGameWidth();
		// float scaleY = currentHeight
		// / (float) adventure.getGameHeight();
		//
		// float scale = scaleX < scaleY ? scaleX : scaleY;
		// float dispX = Math.abs(adventure.getGameWidth() * scaleX
		// - adventure.getGameWidth() * scale) / 2;
		// float dispY = Math.abs(adventure.getGameHeight() * scaleY
		// - adventure.getGameHeight() * scale) / 2;
		//
		initialTransformation = new EAdTransformationImpl();
		// initialTransformation.getMatrix().translate(dispX, dispY,
		// true);
		// initialTransformation.getMatrix().scale(scale, scale, true);
		initialTransformation.setValidated(false);

	}

	@Override
	public EAdChapter getCurrentChapter() {
		return currentChapter;
	}

	@Override
	public void setAdventureModel(EAdAdventureModel model) {
		this.adventure = model;
	}

}
