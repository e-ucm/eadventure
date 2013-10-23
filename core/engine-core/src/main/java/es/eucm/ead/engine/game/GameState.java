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

import aurelienribon.tweenengine.TweenAccessor;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.game.interfaces.TextProcessor;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.engine.operators.OperatorFactory;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.tools.MathEvaluator.OperationResolver;
import es.eucm.ead.tools.StringHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class GameState extends ValueMap implements OperationResolver,
		TweenAccessor<ElementField> {

	private static Logger logger = LoggerFactory.getLogger(GameState.class);

	/**
	 * Operator factory
	 */
	protected OperatorFactory operatorFactory;

	private TextProcessor textProcessor;

	/**
	 * Map containing field watchers by element and variable
	 */
	private Map<String, List<FieldWatcher>> fieldWatchers;
	private Map<String, FieldGetter> fieldGetters;

	@Inject
	public GameState(StringHandler stringHandler) {
		super(stringHandler);
		this.operatorFactory = new OperatorFactory(this, stringHandler);
		this.textProcessor = new TextProcessor(operatorFactory, stringHandler);

		// Field watcher
		fieldWatchers = new HashMap<String, List<FieldWatcher>>();
		fieldGetters = new HashMap<String, FieldGetter>();
	}

	/**
	 * Evaluates a condition, using the required evaluator
	 *
	 * @param <T>       The actual condition class
	 * @param condition The condition to be evaluated
	 * @return The result of evaluating the condition according to the current
	 *         values of the game state
	 */
	public <T extends Condition> boolean evaluate(T condition) {
		if (condition == null) {
			return true;
		}
		return operatorFactory.operate(condition);
	}

	public <T extends Operation, S> S operate(T operation) {
		return operatorFactory.operate(operation);
	}

	public String processTextVars(String text, EAdList<Operation> operations) {
		return textProcessor.processTextVars(text, operations);
	}

	@Override
	public void setValue(String element, String varName, Object value) {
		if (!notifyWatchers(element, varName, value)) {
			super.setValue(element, varName, value);
		}
	}

	@Override
	public <S> S getValue(String element, String varName, S defaultValue) {
		return fieldGetters.containsKey(element) ? fieldGetters.get(element)
				.getField(element, varName, defaultValue) : super.getValue(
				element, varName, defaultValue);
	}

	private <T> boolean notifyWatchers(String elementId, String varName, T value) {
		List<FieldWatcher> list = fieldWatchers.get(elementId);
		boolean handled = false;
		if (list != null) {
			for (FieldWatcher fw : list) {
				handled |= fw.setField(elementId, varName, value);
			}
		}
		return handled;
	}

	/**
	 * Adds a field watcher that is notified every time the given field is
	 * updated
	 *
	 * @param elementId the element's id
	 */
	public void addFieldWatcher(String elementId, FieldWatcher fieldWatcher) {
		List<FieldWatcher> list = fieldWatchers.get(elementId);
		if (list == null) {
			list = new ArrayList<FieldWatcher>();
			fieldWatchers.put(elementId, list);
		}
		list.add(fieldWatcher);
	}

	/**
	 * Removes a field watcher
	 *
	 * @param fieldWatcher the field watcher to remove
	 */
	public void removeFieldWatcher(FieldWatcher fieldWatcher) {
		for (List<FieldWatcher> l : fieldWatchers.values()) {
			l.remove(fieldWatcher);
		}
	}

	public void setFieldGetter(String elementId, FieldGetter fieldGetter) {
		this.fieldGetters.put(elementId, fieldGetter);
	}

	/**
	 * Resets the game state to its initial state
	 */
	public void reset() {
		fieldWatchers.clear();
		valuesMap.clear();
	}

	public int countWatchers(String element) {
		List<FieldWatcher> list = fieldWatchers.get(element);
		return list == null ? 0 : list.size();
	}

	public void addFieldWatcher(FieldWatcher fieldWatcher, BasicElement element) {
		Identified i = maybeDecodeField(element);
		addFieldWatcher(i == null ? null : i.getId(), fieldWatcher);
	}

	public void removeGetter(SceneElementGO sceneElementGO) {
		fieldGetters.remove(sceneElementGO.getElement() + "");
	}

	public interface FieldWatcher {

		/**
		 * Call whenever the field is updated.
		 * If true is returned, then the update has been handled by the watcher and it is not
		 * necessary store the value in the value map. If false is returned, the value is stored
		 * in the value map for subsequent queries
		 */
		<T> boolean setField(String elementId, String varName, T value);

	}

	public interface FieldGetter {
		/**
		 * Called when something is asking for the value of the field. If null is returned, the value
		 * returned is the one stored for the variable in the value map
		 */
		<T> T getField(String elementId, String varName, T defaultvalue);
	}
}
