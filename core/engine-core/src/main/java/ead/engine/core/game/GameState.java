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
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.params.variables.EAdVarDef;
import ead.engine.core.gameobjects.effects.EffectGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.operators.Operator;

/**
 * The state of the game.
 */
public interface GameState extends ValueMap {

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
	 * <p>
	 * Calculates the result of the given {@link EAdOperation} with the current
	 * values in the {@link ValueMap}
	 * </p>
	 * The value should be stored in the {@link ValueMap} by the actual
	 * {@link Operator}
	 * 
	 * @param <T>
	 * @param eAdVar
	 *            the class for the result
	 * @param eAdOperation
	 *            operation to be done
	 * @return operation's result. If operation is {@code null}, a null is
	 *         returned.
	 */
	<T extends EAdOperation, S> S operate(Class<S> eAdVar, T eAdOperation);

	/**
	 * Sets the variable to the result value of the operation
	 * 
	 * @param var
	 * @param operation
	 */
	void setValue(EAdField<?> var, EAdOperation operation);

	/**
	 * Sets the variable value for the given element
	 * 
	 * @param element
	 *            the element holding the variable. If the element is
	 *            {@code null}, it's considered that the variable belongs to the
	 *            system
	 * @param var
	 *            the variable definition
	 * @param operation
	 *            the operation whose result will be assigned to the variable
	 */
	void setValue(Object element, EAdVarDef<?> var, EAdOperation operation);

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
	 * @return true if the game loop is paused
	 */
	boolean isPaused();

	/**
	 * Change the paused status of the game loop
	 * 
	 * @param paused
	 */
	void setPaused(boolean paused);

	/**
	 * Updates the game state
	 */
	void update();

	void saveState();

	void loadState();

}
