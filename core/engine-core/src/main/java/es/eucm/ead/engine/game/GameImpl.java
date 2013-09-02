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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.factories.EffectGOFactory;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.game.interfaces.*;
import es.eucm.ead.engine.gameobjects.EventedGO;
import es.eucm.ead.engine.gameobjects.effects.EffectGO;
import es.eucm.ead.engine.tracking.GameTracker;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.EAdChapter;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class GameImpl implements Game {

	private static final Logger logger = LoggerFactory.getLogger("GameImpl");

	public static final String HOOK_AFTER_UPDATE = "after_update";

	public static final String HOOK_AFTER_MODEL_READ = "after_model_read";

	public static final String HOOK_AFTER_CHAPTER_READ = "after_chapter_read";

	public static final String HOOK_AFTER_RENDER = "after_render";

	/**
	 * Game gui
	 */
	private GUI gui;

	/**
	 * Game state
	 */
	private GameState gameState;

	/**
	 * Game tween manager
	 */
	private TweenManager tweenManager;

	/**
	 * If the game state is paused
	 */
	private boolean paused;

	/**
	 * Events factory
	 */
	private EventGOFactory eventFactory;

	/**
	 * Engine hooks
	 */
	private Map<String, List<EngineHook>> hooks;

	/**
	 * Game tracker
	 */
	private GameTracker tracker;

	/**
	 * Current adventure
	 */
	private EAdAdventureModel adventure;

	/**
	 * Current chapter
	 */
	private EAdChapter chapter;

	/**
	 * Current adventure game object
	 */
	private EventedGO adventureGO;

	/**
	 * Current chapter game object
	 */
	private EventedGO chapterGO;

	/**
	 * Effects handler. Deals with adding, removing and updating effects
	 */
	private EffectsHandler effectsHandler;

	// Aux
	private ArrayList<String> hookNameDelete;
	private ArrayList<EngineHook> hookDelete;

	@Inject
	public GameImpl(GUI gui, GameState gameState, EventGOFactory eventFactory,
			GameTracker tracker, EffectGOFactory effectFactory) {
		this.gui = gui;
		this.gameState = gameState;
		this.tweenManager = new TweenManager();
		this.eventFactory = eventFactory;
		this.tracker = tracker;
		this.effectsHandler = new EffectsHandler(gameState, effectFactory);
		// Init tween manager
		Tween.registerAccessor(EAdField.class, gameState);
		Tween.registerAccessor(BasicField.class, gameState);
		hooks = new HashMap<String, List<EngineHook>>();
		// Aux
		hookNameDelete = new ArrayList<String>();
		hookDelete = new ArrayList<EngineHook>();
		// Adventure and chapter game object
		adventureGO = new EventedGO(eventFactory);
		chapterGO = new EventedGO(eventFactory);
	}

	@Override
	public GameState getGameState() {
		return gameState;
	}

	@Override
	public GUI getGUI() {
		return gui;
	}

	@Override
	public void setAdventure(EAdAdventureModel adventure) {
		this.adventure = adventure;
		adventureGO.setElement(adventure);
	}

	@Override
	public void setChapter(EAdChapter chapter) {
		this.chapter = chapter;
		chapterGO.setElement(chapter);
	}

	@Override
	public TweenManager getTweenManager() {
		return tweenManager;
	}

	@Override
	public boolean isPaused() {
		return paused;
	}

	@Override
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	@Override
	public int getSkippedMilliseconds() {
		return isPaused() ? 0 : (int) (Gdx.graphics.getDeltaTime() * 1000);
	}

	@Override
	public EAdAdventureModel getAdventureModel() {
		return adventure;
	}

	@Override
	public EAdChapter getCurrentChapter() {
		return chapter;
	}

	@Override
	public void initialize() {
		gui.initialize();
	}

	@Override
	public void dispose() {
		tracker.stop();
		// All this down here should be called when restarting the engine without exiting
		tweenManager.killAll();
	}

	@Override
	public void act(float delta) {
		// Remove hooks
		for (int i = 0; i < this.hookNameDelete.size(); i++) {
			removeHookImpl(hookNameDelete.get(i), hookDelete.get(i));
		}
		hookNameDelete.clear();
		hookDelete.clear();

		gameState.setValue(SystemFields.ELAPSED_TIME_PER_UPDATE,
				getSkippedMilliseconds());

		// Scene
		if (!isPaused()) {
			effectsHandler.act(delta);
			// Tween manager
			tweenManager.update(delta);
			this.adventureGO.act(delta);
			this.chapterGO.act(delta);
		}

		doHook(HOOK_AFTER_UPDATE);
		// Update game time
		Long l = gameState.getValue(SystemFields.GAME_TIME);
		l += getSkippedMilliseconds();
		gameState.setValue(SystemFields.GAME_TIME, l);
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

	@Override
	public List<EffectGO<?>> getEffects() {
		return effectsHandler.getEffects();
	}

	@Override
	public void clearEffects(boolean clearPersistents) {
		effectsHandler.clearEffects(clearPersistents);
	}

	@Override
	public void addEffect(EAdEffect e) {
		addEffect(e, null, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.eucm.eadventure.engine.engine.GameState#addEffect(int,
	 * es.eucm.eadventure.common.model.effects.EAdEffect)
	 */
	@Override
	public EffectGO<?> addEffect(EAdEffect e, Event action,
			EAdSceneElement parent) {
		return effectsHandler.addEffect(e, action, parent);

	}

}
