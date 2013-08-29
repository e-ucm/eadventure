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

package es.eucm.ead.engine.game.interfaces;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.scenes.scene2d.Event;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.gameobjects.effects.EffectGO;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.model.elements.EAdChapter;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.reader.model.XMLVisitor.VisitorListener;

import java.util.List;

/**
 * Main game interface. Include the methods to update the game state, render the
 * current game state, evaluate conditions according to the current game state,
 * etc.
 */
public interface Game {

	/**
	 * Returns the current adventure game model ({@link EAdAdventureModel})
	 * 
	 * @return The adventure game model
	 */
	EAdAdventureModel getAdventureModel();

	/**
	 * Returns the current chapter
	 * 
	 * @return
	 */
	EAdChapter getCurrentChapter();

	/**
	 * Initialize the whole engine, loading default properties, string, creating
	 * the GUI.
	 * 
	 * This method first read ead.properties, and then create the game context
	 * with its data. After that, it reads the strings (from strings.xml, or any
	 * of its i18n), and then reads the model (from data.xml).
	 */
	void initialize();

	/**
	 * Returns the milliseconds since last update
	 *
	 * @return
	 */
	int getSkippedMilliseconds();

	/**
	 * Updates the game state
	 * 
	 * @param delta
	 *            TODO
	 */
	void act(float delta);

	/**
	 * Disposes all the resources allocated by the engine and destroys the GUI
	 */
	void dispose();

	void addHook(String hookName, EngineHook hook);

	void removeHook(String hookName, EngineHook hook);

	void doHook(String hookName);

	TweenManager getTweenManager();

	/**
	 * Adds an effect without any gui action associated
	 *
	 * @param e
	 *            the effect
	 */
	void addEffect(EAdEffect e);

	/**
	 * Returns a list with all game objects linked to the current effects.
	 *
	 * @return a list with all game objects linked to the current effects.
	 */
	List<EffectGO<?>> getEffects();

	/**
	 * Adds a new effect to the effects' tail
	 *
	 * @param e
	 *            the new effect
	 * @param action
	 *            the action that launched the effect
	 * @param parent
	 *            scene element who launched the effect
	 * @return the effect game object create from the effect element
	 */
	EffectGO<?> addEffect(EAdEffect e, Event action, EAdSceneElement parent);

	/**
	 * Clears all the current effects
	 *
	 * @param persisten
	 *            sets if persistent effects should also be deleted
	 */
	void clearEffects(boolean persistent);

	GameState getGameState();

	GUI getGUI();

	/**
	 * @return true if the game loop is paused
	 */
	boolean isPaused();

	/**
	 * Change the paused status of the game loop
	 *
	 * @param paused sets if the game is paused
	 */
	void setPaused(boolean paused);

}
