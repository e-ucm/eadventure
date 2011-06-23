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

import es.eucm.eadventure.common.model.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdConditionedElement;
import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.engine.core.EvaluatorFactory;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.EffectGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

@Singleton
public class GameImpl implements Game {

	private EvaluatorFactory evaluatorFactory;

	private AssetHandler assetHandler;

	private EAdAdventureModel adventure;

	private GUI gui;

	private GameState gameState;

	private EffectHUD effectHUD;

	private BasicHUD<?> basicHud;

	private static final Logger logger = Logger.getLogger("GameImpl");

	// Auxiliary variable, to avoid new every time
	private static ArrayList<EffectGO<?>> finishedEffects = new ArrayList<EffectGO<?>>();

	private boolean effectHUDon;
	
	private GameObjectFactory gameObjectFactory;
	
	private GameObjectManager gameObjectManager;

	@Inject
	public GameImpl(GUI gui, EvaluatorFactory evaluatorFactory,
			GameState gameState, EffectHUD effectHUD,
			AssetHandler assetHandler,
			@SuppressWarnings("rawtypes") BasicHUD basicHud,
			GameObjectFactory gameObjectFactory,
			GameObjectManager gameObjectManager) {
		this.gui = gui;
		this.evaluatorFactory = evaluatorFactory;
		this.gameState = gameState;
		this.effectHUD = effectHUD;
		this.assetHandler = assetHandler;
		this.effectHUDon = false;
		this.basicHud = basicHud;
		this.gameObjectFactory = gameObjectFactory;
		this.adventure = new EAdAdventureModelImpl();
		this.adventure.getChapters().add(new EAdChapterImpl(""));
		this.gameObjectFactory = gameObjectFactory;
		this.gameObjectManager = gameObjectManager;
	}

	@Override
	public void update() {
			processEffects();
		if (!gameState.isPaused()) {
			updateTimers();
	
			gameState.getScene().update(gameState);
			gui.addElement(gameState.getScene(), 0, 0);
	
			basicHud.update(gameState);
			gui.addElement(basicHud, 0, 0);
		}
		gui.prepareGUI();
	}
	
	private void updateTimers() {
		for (EAdTimer timer : gameState.getCurrentChapter().getTimers())
			gameObjectFactory.get(timer).update(gameState);
	}

	private void processEffects() {
		gameState.updateEffectsQueue();
		finishedEffects.clear();
		boolean block = false;
		int i = 0;
		while (i < gameState.getEffects().size() && !block ) {
			EffectGO<?> effectGO = gameState.getEffects().get(i);
			if (!effectGO.isInitilized()) {
				if (evaluatorFactory.evaluate(effectGO.getEffect()
						.getCondition())) {
					effectGO.initilize();
					effectGO.run();
				} else {
					finishedEffects.add(effectGO);
				}
			} 
			
			if ( effectGO.isInitilized() ){
				// The order must be: update, then check if it's finished.
				// Some effects take only one update to finish
				effectGO.update(gameState);

				if (effectGO.isStopped() || effectGO.isFinished()) {
					effectGO.finish();
					finishedEffects.add(effectGO);
				} else if (effectGO.isBlocking())
					// If effect is blocking, get out of the loop
					block = true;

			}
			i++;
		}

		// Delete finished effects
		for (EffectGO<?> e : finishedEffects) {
			logger.log(Level.INFO, "Finished or discarded effect " + e.getClass());
			gameState.getEffects().remove(e);
		}

		// TODO Should check if any is visual, not just the first one
		if (gameState.getEffects().size() > 0
				&& gameState.getEffects().get(0).isVisualEffect()) {
			if (!effectHUDon) {
				gameObjectManager.addHUD(effectHUD);
				effectHUDon = true;
			}
		} else {
			if (effectHUDon) {
				gameObjectManager.removeHUD(effectHUD);
				effectHUDon = false;
			}
		}

		effectHUD.setElement(gameState.getEffects());
	}

	@Override
	public void render(float interpolation) {
		gui.commit(interpolation);
	}

	@Override
	public boolean evaluateCondition(EAdConditionedElement condition) {
		return evaluatorFactory.evaluate(condition.getCondition());
	}

	@Override
	public EAdAdventureModel getAdventureModel() {
		return adventure;
	}

	@Override
	public void loadGame() {
		assetHandler.initilize();
		
		//TODO should probably find multiplatform reading?
		//reader.read(assetHandler.getResourceAsStream("@adventure.xml"));
		
		//TODO should probably be more careful loading chapter
		gameState.setCurrentChapter(adventure.getChapters().get(0));
	}

}
