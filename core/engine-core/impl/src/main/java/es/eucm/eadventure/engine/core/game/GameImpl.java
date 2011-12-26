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

package es.eucm.eadventure.engine.core.game;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.EAdEvent;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.variables.SystemFields;
import es.eucm.eadventure.engine.core.debuggers.Debugger;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.factories.EventGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.go.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.go.EffectGO;
import es.eucm.eadventure.engine.core.gameobjects.go.EventGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.InventoryHUD;
import es.eucm.eadventure.engine.core.input.InputHandler;
import es.eucm.eadventure.engine.core.inventory.InventoryHandler;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.EngineConfiguration;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.EAdTransformationImpl;

@Singleton
public class GameImpl implements Game {

	private AssetHandler assetHandler;

	private EAdAdventureModel adventure;

	private GUI gui;

	private GameState gameState;

	private EffectHUD effectHUD;

	private InventoryHUD inventoryHUD;

	private static final Logger logger = Logger.getLogger("GameImpl");

	// Auxiliary variable, to avoid new every time
	private static ArrayList<EffectGO<?>> finishedEffects = new ArrayList<EffectGO<?>>();

	private boolean effectHUDon;

	private GameObjectManager gameObjectManager;

	private Debugger debugger;

	private InventoryHandler inventoryHandler;

	private EAdTransformation initialTransformation;

	private EventGOFactory eventFactory;

	private List<EventGO<?>> events;

	private EngineConfiguration configuration;

	@Inject
	public GameImpl(GUI gui, GameState gameState, EffectHUD effectHUD,
			AssetHandler assetHandler, GameObjectManager gameObjectManager,
			Debugger debugger, ValueMap valueMap, InputHandler mouseState,
			BasicHUD basicHud, InventoryHUD inventoryHud,
			InventoryHandler inventoryHandler, EventGOFactory eventFactory,
			EngineConfiguration configuration) {
		this.gui = gui;
		this.gameState = gameState;
		this.effectHUD = effectHUD;
		this.assetHandler = assetHandler;
		this.effectHUDon = false;
		this.adventure = null;
		this.gameObjectManager = gameObjectManager;
		this.debugger = debugger;
		this.inventoryHUD = inventoryHud;
		this.inventoryHandler = inventoryHandler;
		this.eventFactory = eventFactory;
		this.configuration = configuration;
		events = new ArrayList<EventGO<?>>();
		gameObjectManager.setBasicHUD(basicHud);
	}

	@Override
	public void update() {

		if (!gameState.isPaused()) {
			processEffects();
			updateChapterEvents();
			gameState.getScene().update();
			
		}
		
		gameObjectManager.updateHUDs();
		gui.addElement(gameState.getScene(), initialTransformation);
		// Add huds
		gameObjectManager.addHUDs(gui, initialTransformation);

		if (debugger != null && debugger.getGameObjects() != null)
			for (DrawableGO<?> go : debugger.getGameObjects()) {
				gui.addElement(go, initialTransformation);
			}

		gui.prepareGUI(initialTransformation);

	}

	private void updateChapterEvents() {
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
			logger.log(Level.INFO,
					"Finished or discarded effect " + e.getClass());
			gameState.getEffects().remove(e);
		}

		boolean visualEffect = false;
		int index = 0;
		while (!visualEffect && index < gameState.getEffects().size()) {
			visualEffect = gameState.getEffects().get(index++).isVisualEffect();
		}

		if (visualEffect) {
			if (!effectHUDon) {
				gameObjectManager.addHUD(effectHUD);
				effectHUDon = true;
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
		assetHandler.initilize();

		// TODO should probably find multiplatform reading?
		// reader.read(assetHandler.getResourceAsStream("@adventure.xml"));

		// TODO should probably be more careful loading chapter
		// if (gameState.getCurrentChapter() == null)
		// gameState.setCurrentChapter(adventure.getChapters().get(0));
	}

	@Override
	public void setGame(EAdAdventureModel model, EAdChapter eAdChapter) {
		gameState.getValueMap().setValue(SystemFields.GAME_WIDTH,
				model.getGameWidth());
		gameState.getValueMap().setValue(SystemFields.GAME_HEIGHT,
				model.getGameHeight());

		this.adventure = model;
		if (adventure.getInventory() != null) {
			for (EAdSceneElementDef def : adventure.getInventory()
					.getInitialItems())
				inventoryHandler.add(def);
			gameObjectManager.addHUD(inventoryHUD);
		}
		gameState.setCurrentChapter(eAdChapter);

		events.clear();
		for (EAdEvent e : eAdChapter.getEvents()) {
			EventGO<?> eventGO = eventFactory.get(e);
			eventGO.setParent(null);
			eventGO.initialize();
			events.add(eventGO);
		}

		gameState.setInitialScene(eAdChapter.getInitialScene());
		updateInitialTransformation();
		
		// FIXME probably move this to other place
		gameState.getValueMap().setValue(SystemFields.ELAPSED_TIME_PER_UPDATE, GameLoop.SKIP_MILLIS_TICK);
	}

	public void updateInitialTransformation() {
		initialTransformation = new EAdTransformationImpl();
		initialTransformation.getMatrix().scale(
				configuration.getWidth() / (float) adventure.getGameWidth(),
				configuration.getHeight() / (float) adventure.getGameHeight(),
				true);
	}
	
	public EAdTransformation getInitialTransformation( ){
		return initialTransformation;
	}

}
