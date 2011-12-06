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

package es.eucm.eadventure.engine.core.impl;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.features.Conditioned;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.debuggers.EAdDebugger;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.gameobjects.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.EffectGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.InventoryHUD;
import es.eucm.eadventure.engine.core.inventory.InventoryHandler;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.impl.EAdTransformationImpl;

@Singleton
public class GameImpl implements Game {

	private EvaluatorFactory evaluatorFactory;

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

	private EAdDebugger debugger;

	private ValueMap valueMap;

	private MouseState mouseState;

	private InventoryHandler inventoryHandler;

	private EAdTransformation initialTransformation = new EAdTransformationImpl();
	
	private PlatformConfiguration platformConfiguration;

	@Inject
	public GameImpl(GUI gui, EvaluatorFactory evaluatorFactory,
			GameState gameState, EffectHUD effectHUD,
			AssetHandler assetHandler, GameObjectManager gameObjectManager,
			EAdDebugger debugger, ValueMap valueMap, MouseState mouseState,
			PlatformConfiguration platformConfiguration, BasicHUD basicHud,
			InventoryHUD inventoryHud, InventoryHandler inventoryHandler) {
		this.gui = gui;
		this.evaluatorFactory = evaluatorFactory;
		this.gameState = gameState;
		this.effectHUD = effectHUD;
		this.assetHandler = assetHandler;
		this.effectHUDon = false;
		this.adventure = null;
		this.gameObjectManager = gameObjectManager;
		this.debugger = debugger;
		this.valueMap = valueMap;
		this.mouseState = mouseState;
		this.inventoryHUD = inventoryHud;
		this.inventoryHandler = inventoryHandler;
		this.platformConfiguration = platformConfiguration;
		initialTransformation.getMatrix().scale(
				(float) platformConfiguration.getScale(),
				(float) platformConfiguration.getScale(), true);
		gameObjectManager.setBasicHUD(basicHud);
	}

	@Override
	public void update() {
		updateSystemVars();
		if (!gameState.isPaused()) {
			processEffects();
			updateTimers();
			gameState.getScene().update();
		}
		initialTransformation  = new EAdTransformationImpl();
		initialTransformation.getMatrix().scale(
				(float) platformConfiguration.getScale(),
				(float) platformConfiguration.getScale(), true);
		gui.addElement(gameState.getScene(), initialTransformation);

		if (debugger != null && debugger.getGameObjects() != null)
			for (DrawableGO<?> go : debugger.getGameObjects()) {
				gui.addElement(go, initialTransformation);
			}

		gui.prepareGUI(initialTransformation);

	}

	/**
	 * Updates the system variables. Any time a system variable is added to
	 * {@link SystemFields}, its update must be added in here
	 */
	private void updateSystemVars() {
		// Mouse
		float mouse[] = initialTransformation.getMatrix().multiplyPointInverse(
				mouseState.getMouseX(), mouseState.getMouseY(), true);
		valueMap.setValue(SystemFields.MOUSE_X, (int) mouse[0]);
		valueMap.setValue(SystemFields.MOUSE_Y, (int) mouse[1]);

	}

	private void updateTimers() {
		// TODO remove timers
		// for (EAdTimer timer : gameState.getCurrentChapter().getTimers())
		// gameObjectFactory.get(timer).update();
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
	public boolean evaluateCondition(Conditioned condition) {
		return evaluatorFactory.evaluate(condition.getCondition());
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
		this.adventure = model;
		if (adventure.getInventory() != null) {
			for (EAdSceneElementDef def : adventure.getInventory()
					.getInitialItems())
				inventoryHandler.add(def);
			gameObjectManager.addHUD(inventoryHUD);
		}
		gameState.setCurrentChapter(eAdChapter);
		gameState.setInitialScene(eAdChapter.getInitialScene());
	}

}
