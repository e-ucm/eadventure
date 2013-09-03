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

import aurelienribon.tweenengine.TweenAccessor;
import es.eucm.ead.model.elements.EAdCondition;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.EAdOperation;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.tools.MathEvaluator.OperationResolver;

/**
 * The state of the game.
 */
public interface GameState extends ValueMap, OperationResolver,
		TweenAccessor<EAdField<?>> {

	/**
	 * Evaluates a condition, using the required evaluator
	 *
	 * @param <T>       The actual condition class
	 * @param condition The condition to be evaluated
	 * @return The result of evaluating the condition according to the current
	 *         values of the game state
	 */
	<T extends EAdCondition> boolean evaluate(T condition);

	/**
	 * <p>
	 * Calculates the result of the given {@link EAdOperation} with the current
	 * game state
	 * </p>
	 *
	 * @param <T>          the operation class
	 * @param eAdVar       the class for the result
	 * @param eAdOperation operation to be done
	 * @return operation's result. If operation is {@code null}, a null is
	 *         returned.
	 */
	<T extends EAdOperation, S> S operate(Class<S> eAdVar, T eAdOperation);

	/**
	 * Sets the field to the result value of the operation
	 *
	 * @param field     the field
	 * @param operation the operation
	 */
	<S> void setValue(EAdField<S> field, EAdOperation operation);

	/**
	 * Sets the element's variable value to the given operation
	 *
	 * @param element   the element holding the variable. If the element is
	 *                  {@code null}, it's considered that the variable belongs to the
	 *                  system (it's a global field)
	 * @param var       the variable definition
	 * @param operation the operation whose result will be assigned to the variable
	 */
	<S> void setValue(Identified element, EAdVarDef<S> var,
			EAdOperation operation);

	/**
	 * <p>
	 * Substitutes the variables in a text for its values.
	 * </p>
	 * <p>
	 * The text format for the correct substitution should be:
	 * </p>
	 * <ul>
	 * <li><b>[op_index]:</b> The index of the operation whose result will be
	 * used to substitute the reference {@code 0 <= op_index < fields.size()}
	 * <li><b>{[condition]? true text : false text } </b> A conditional text,
	 * depending of the operation whose index is {@code condition} value.</p>
	 *
	 * @param text the text to be processed by the value map
	 * @return the processed text
	 */
	String processTextVars(String text, EAdList<EAdOperation> operations);

	/**
	 * Adds a field watcher that is notified every time the given field is
	 * updated
	 *
	 * @param fieldWatcher the field watcher
	 * @param field        the field to watch
	 */
	void addFieldWatcher(FieldWatcher fieldWatcher, EAdField<?> field);

	/**
	 * Removes a field watcher
	 *
	 * @param fieldWatcher the field watcher to remove
	 */
	void removeFieldWatcher(FieldWatcher fieldWatcher);

	public interface FieldWatcher {
		/**
		 * Call whenever the field is updated
		 */
		void fieldUpdated();

	}

	/**
	 * Resets the game state to its initial state
	 */
	void reset();

}
