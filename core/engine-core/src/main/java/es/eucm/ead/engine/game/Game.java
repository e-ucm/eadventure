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
import es.eucm.ead.engine.factories.EffectFactory;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.game.interfaces.EffectsHandler;
import es.eucm.ead.engine.game.interfaces.EngineHook;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.gameobjects.EventedGO;
import es.eucm.ead.engine.gameobjects.effects.EffectGO;
import es.eucm.ead.engine.tracking.GameTracker;
import es.eucm.ead.model.elements.AdventureGame;
import es.eucm.ead.model.elements.Chapter;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.scenes.SceneElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class Game {

	static private Logger logger = LoggerFactory.getLogger(Game.class);

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
	private AdventureGame adventure;

	/**
	 * Current chapter
	 */
	private Chapter chapter;

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
	public Game(GUI gui, GameState gameState, EventFactory eventFactory,
			GameTracker tracker, EffectFactory effectFactory) {
		this.gui = gui;
		this.gameState = gameState;
		this.tweenManager = new TweenManager();
		this.tracker = tracker;
		this.effectsHandler = new EffectsHandler(gameState, effectFactory);
		// Init tween manager
		Tween.registerAccessor(ElementField.class, gameState);
		Tween.registerAccessor(ElementField.class, gameState);
		hooks = new HashMap<String, List<EngineHook>>();
		// Aux
		hookNameDelete = new ArrayList<String>();
		hookDelete = new ArrayList<EngineHook>();
		// Adventure and chapter game object
		adventureGO = new EventedGO(eventFactory);
		chapterGO = new EventedGO(eventFactory);
	}

	/**
	 * Returns the game state
	 *
	 * @return
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * @return Returns the gui
	 */
	public GUI getGUI() {
		return gui;
	}

	/**
	 * Sets the current adventure
	 * @param adventure the current adventure
	 */
	public void setAdventure(AdventureGame adventure) {
		logger.debug("Setting adventure");
		this.adventure = adventure;
		adventureGO.setElement(adventure);
		reset();
	}

	/**
	 * Sets the current chapter
	 * @param chapter the current chapter
	 */
	public void setChapter(Chapter chapter) {
		logger.debug("Setting chapter");
		this.chapter = chapter;
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				chapterGO.setElement(Game.this.chapter);
			}
		});
		reset();
	}

	public TweenManager getTweenManager() {
		return tweenManager;
	}

	/**
	 * @return true if the game loop is paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Change the paused status of the game loop
	 *
	 * @param paused sets if the game is paused
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * Returns the milliseconds since last update
	 *
	 * @return
	 */
	public int getSkippedMilliseconds() {
		return isPaused() ? 0 : (int) (Gdx.graphics.getDeltaTime() * 1000);
	}

	/**
	 * Returns the current adventure game model ({@link AdventureGame})
	 *
	 * @return The adventure game model
	 */
	public AdventureGame getAdventureModel() {
		return adventure;
	}

	/**
	 * Returns the current chapter
	 *
	 * @return
	 */
	public Chapter getCurrentChapter() {
		return chapter;
	}

	/**
	 * Disposes all the resources allocated by the engine and destroys the GUI
	 */
	public void dispose() {
		tracker.stop();
	}

	/**
	 * Resets all necessary in the game to start a new chapter (effects, tweens...)
	 */
	public void reset() {
		effectsHandler.clearEffects(true);
		tweenManager.killAll();
	}

	/**
	 * Updates the game state
	 *
	 * @param delta milliseconds since last update
	 */
	public void act(float delta) {
		// Remove hooks
		for (int i = 0; i < this.hookNameDelete.size(); i++) {
			removeHookImpl(hookNameDelete.get(i), hookDelete.get(i));
		}
		hookNameDelete.clear();
		hookDelete.clear();

		// Scene
		if (!isPaused()) {
			effectsHandler.act(delta);
			// Tween manager
			tweenManager.update(delta);
			this.adventureGO.act(delta);
			this.chapterGO.act(delta);
		}

		doHook(HOOK_AFTER_UPDATE);
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

	/**
	 * Returns a list with all game objects linked to the current effects.
	 *
	 * @return a list with all game objects linked to the current effects.
	 */
	public List<EffectGO<?>> getEffects() {
		return effectsHandler.getEffects();
	}

	/**
	 * Clears all the current effects
	 *
	 * @param clearPersistents sets if persistent effects should also be deleted
	 */
	public void clearEffects(boolean clearPersistents) {
		effectsHandler.clearEffects(clearPersistents);
	}

	/**
	 * Adds an effect without any gui action associated
	 *
	 * @param e the effect
	 */
	public void addEffect(Effect e) {
		addEffect(e, null, null);
	}

	/**
	 * Adds a new effect to the effects' tail
	 *
	 * @param e      the new effect
	 * @param action the action that launched the effect
	 * @param parent scene element who launched the effect
	 * @return the effect game object create from the effect element
	 */
	public EffectGO<?> addEffect(Effect e, Event action, SceneElement parent) {
		return effectsHandler.addEffect(e, action, parent);

	}

}
