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

package ead.engine.core.game.interfaces;

import java.util.List;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.scenes.scene2d.Event;

import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.params.variables.EAdVarDef;
import ead.engine.core.gameobjects.effects.EffectGO;
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
	<S> void setValue(EAdField<S> var, EAdOperation operation);

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
	<S> void setValue(Object element, EAdVarDef<S> var, EAdOperation operation);

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
	 * 
	 * @param delta
	 *            TODO
	 */
	void update(float delta);

	/**
	 * Returns the tween manager
	 * 
	 * @return
	 */
	TweenManager getTweenManager();

	/**
	 * <p>
	 * Substitutes the variables in a text for its values.
	 * </p>
	 * 
	 * <p>
	 * The text format for the correct substitution should be:
	 * </p>
	 * 
	 * <ul>
	 * <li><b>[op_index]:</b> The index of the operation whose result will be
	 * used to substitute the reference {@code 0 <= op_index < fields.size()}
	 * <li><b>{[condition]? true text : false text } </b> A conditional text,
	 * depending of the operation whose index is {@code condition} value.</p>
	 * 
	 * @param text
	 *            the text to be processed by the value map
	 * @return the processed text
	 */
	String processTextVars(String text, EAdList<EAdOperation> operations);

	/**
	 * Adds a field watcher that is notified every time the given field is
	 * updated
	 * 
	 * @param fieldWatcher
	 * @param field
	 */
	void addFieldWatcher(FieldWatcher fieldWatcher, EAdField<?> field);

	/**
	 * Removes a field watcher
	 * 
	 * @param fieldWatcher
	 */
	void removeFieldWatcher(FieldWatcher fieldWatcher);

	public interface FieldWatcher {
		/**
		 * Call whenever the field is updated
		 */
		void fieldUpdated();

	}

	void saveState();

	void loadState();

}
