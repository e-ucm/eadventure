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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.variables.SystemFields;
import ead.engine.core.debuggers.DebuggerHandler;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.EffectGO;
import ead.engine.core.gameobjects.go.EventGO;
import ead.engine.core.gameobjects.huds.ActionsHUD;
import ead.engine.core.gameobjects.huds.BottomBasicHUD;
import ead.engine.core.gameobjects.huds.EffectHUD;
import ead.engine.core.gameobjects.huds.InventoryHUD;
import ead.engine.core.gameobjects.huds.TopBasicHUD;
import ead.engine.core.inventory.InventoryHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.tracking.Tracker;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;

@Singleton
public class GameImpl implements Game {

	private AssetHandler assetHandler;

	private EAdAdventureModel adventure;

	private GUI gui;

	private GameState gameState;

	private EffectHUD effectHUD;

	private InventoryHUD inventoryHUD;

	private ActionsHUD actionsHUD;

	private static final Logger logger = LoggerFactory.getLogger("GameImpl");

	// Auxiliary variable, to avoid new every time
	private static ArrayList<EffectGO<?>> finishedEffects = new ArrayList<EffectGO<?>>();

	private GameObjectManager gameObjectManager;

	private DebuggerHandler debuggerHandler;

	private InventoryHandler inventoryHandler;

	private EAdTransformation initialTransformation;

	private EventGOFactory eventFactory;

	private List<EventGO<?>> events;

	private EngineConfiguration configuration;

	private int currentWidth = -1;

	private int currentHeight = -1;
	
	private Tracker tracker;

	@Inject
	public GameImpl(GUI gui, GameState gameState, EffectHUD effectHUD,
			AssetHandler assetHandler, GameObjectManager gameObjectManager,
			DebuggerHandler debugger, ValueMap valueMap, TopBasicHUD basicHud,
			BottomBasicHUD bottomBasicHud, InventoryHUD inventoryHud,
			InventoryHandler inventoryHandler, EventGOFactory eventFactory,
			EngineConfiguration configuration, ActionsHUD actionsHUD, Tracker tracker) {
		this.gui = gui;
		this.gameState = gameState;
		this.effectHUD = effectHUD;
		this.actionsHUD = actionsHUD;
		this.assetHandler = assetHandler;
		this.adventure = null;
		this.gameObjectManager = gameObjectManager;
		this.debuggerHandler = debugger;
		this.inventoryHUD = inventoryHud;
		this.inventoryHandler = inventoryHandler;
		this.eventFactory = eventFactory;
		this.configuration = configuration;
		this.tracker = tracker;
		events = new ArrayList<EventGO<?>>();
		gameObjectManager.setBasicHUDs(basicHud, bottomBasicHud);
		gui.setGame(this);
	}

	@Override
	public void update() {

		updateInitialTransformation();
		if (!gameState.isPaused()) {
			processEffects();
			updateGameEvents();
			gameState.getScene().update();

		}
		gameObjectManager.updateHUDs();
		gui.addElement(gameState.getScene(), initialTransformation);

		updateDebuggers();
		// Add huds
		gameObjectManager.addHUDs(gui, initialTransformation);

		gui.prepareGUI();

	}

	private void updateDebuggers() {
		if (debuggerHandler != null && debuggerHandler.getGameObjects() != null) {
			for (DrawableGO<?> go : debuggerHandler.getGameObjects()) {
				go.update();
				gui.addElement(go, initialTransformation);
			}
		}
	}

	private void updateGameEvents() {
		Long l = gameState.getValueMap().getValue(SystemFields.GAME_TIME);
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
			logger.info("Finished or discarded effect {}", e.getClass());
			gameState.getEffects().remove(e);
		}

		boolean visualEffect = false;
		int index = 0;
		while (!visualEffect && index < gameState.getEffects().size()) {
			visualEffect = gameState.getEffects().get(index++).isVisualEffect();
		}

		boolean effectHUDon = gameObjectManager.getHUDs().contains(effectHUD);
		if (visualEffect) {
			if (!effectHUDon) {
				gameObjectManager.addHUD(effectHUD);
				gameObjectManager.removeHUD(actionsHUD);
			}
			effectHUD.setEffects(gameState.getEffects());
		} else {
			if (effectHUDon) {
				gameObjectManager.removeHUD(effectHUD);
				effectHUDon = false;
			}
		}
	}

	@Override
	public void render(float interpolation) {
		gui.commit(interpolation);
	}

	@Override
	public EAdAdventureModel getAdventureModel() {
		return adventure;
	}

	@Override
	public void loadGame() {
		assetHandler.initialize();
	}

	@Override
	public void setGame(EAdAdventureModel model, EAdChapter eAdChapter) {
		logger.info("Setting the game");
		gameState.getValueMap().setValue(SystemFields.GAME_WIDTH,
				model.getGameWidth());
		gameState.getValueMap().setValue(SystemFields.GAME_HEIGHT,
				model.getGameHeight());

		this.adventure = model;

		if (adventure.getInventory() != null) {
			logger.info("Building inventory...");
			for (EAdSceneElementDef def : adventure.getInventory()
					.getInitialItems())
				inventoryHandler.add(def);
			gameObjectManager.addHUD(inventoryHUD);
		}
		gameState.setCurrentChapter(eAdChapter);

		logger.info("Init game events...");
		events.clear();
		for (EAdEvent e : eAdChapter.getEvents()) {
			EventGO<?> eventGO = eventFactory.get(e);
			eventGO.setParent(null);
			eventGO.initialize();
			events.add(eventGO);
		}

		// Set the debuggers
		setDebuggers(model);

		gameState.setInitialScene(eAdChapter.getInitialScene());
		updateInitialTransformation();
		
		// Start tracking
		Boolean track = Boolean.parseBoolean( model.getProperties().get(Tracker.TRACKING_ENABLE) );
		if ( track ){
			tracker.startTracking(model);
		}

	}

	private void setDebuggers(EAdAdventureModel model) {
		if (debuggerHandler != null) {
			debuggerHandler.setUp(model);
		}
	}

	public void updateInitialTransformation() {
		if (initialTransformation != null) {
			initialTransformation.setValidated(true);
		}

		if (currentWidth != configuration.getWidth()
				|| currentHeight != configuration.getHeight()) {

			currentWidth = configuration.getWidth();
			currentHeight = configuration.getHeight();

			float scaleX = configuration.getWidth()
					/ (float) adventure.getGameWidth();
			float scaleY = configuration.getHeight()
					/ (float) adventure.getGameHeight();

			float scale = scaleX < scaleY ? scaleX : scaleY;
			float dispX = Math.abs(adventure.getGameWidth() * scaleX - adventure.getGameWidth() * scale) / 2;
			float dispY = Math.abs(adventure.getGameHeight() * scaleY - adventure.getGameHeight() * scale) / 2;

			initialTransformation = new EAdTransformationImpl();
			initialTransformation.getMatrix().translate(dispX, dispY, true);
			initialTransformation.getMatrix().scale(scale, scale, true);
			initialTransformation.setValidated(false);
			gui.setInitialTransformation(initialTransformation);
		}
	}

	public EAdTransformation getInitialTransformation() {
		return initialTransformation;
	}

}
