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

import java.util.List;

import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.engine.core.gameobjects.go.EffectGO;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.input.InputAction;

/**
 * The state of the game.
 */
public interface GameState {

	/**
	 * The {@link ValueMap} stores the current value of different elements in
	 * the game, such as flags, variables and the like.
	 * 
	 * @return a {@link ValueMap}
	 */
	ValueMap getValueMap();
	
	/**
	 * Evaluates a condition, using the required evaluator, based on a given
	 * {@link ValeMap}.
	 * 
	 * @param <T>
	 *            The actual condition class
	 * @param condition
	 *            The condition to be evaluated
	 * @return The result of evaluating the condition according to the given set
	 *         of values
	 */
	<T extends EAdCondition> boolean evaluate(T condition);

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
	EffectGO<?> addEffect(EAdEffect e, InputAction<?> action,
			EAdSceneElement parent);
	
	/**
	 * Clears all the current effects
	 * 
	 * @param persisten
	 *            sets if persistent effects should also be deleted
	 */
	void clearEffects(boolean persistent);
	
	/**
	 * <p>
	 * Sets the current {@link SceneGO} of the game.
	 * </p>
	 * <p>
	 * Implementation should check if the scene is stackable (not all scenes
	 * are, such as video scenes, cutscenes, loading screens) and stack it if
	 * necessary.
	 * </p>
	 * 
	 * @param screen
	 *            the current {@link SceneGO}.
	 */
	void setScene(SceneGO<?> scene);
	
	/**
	 * Returns the current game screen object. The {@link SceneGO} element is
	 * the root of all what is drawn to the screen.
	 * 
	 * @return a {@link SceneGO} object
	 */
	SceneGO<?> getScene();

	/**
	 * @return the {@link EAdScene} previously visible in the game
	 */
	EAdScene getPreviousScene();

	/**
	 * @return true if the game loop is paused
	 */
	boolean isPaused();

	/**
	 * Change the paused status of the game loop
	 * 
	 * @param paused
	 */
	void setPaused(boolean paused);

	void saveState();

	void loadState();

}
