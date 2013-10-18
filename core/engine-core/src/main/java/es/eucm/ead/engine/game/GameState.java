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
import es.eucm.ead.engine.operators.OperatorFactory;
import es.eucm.ead.model.elements.EAdCondition;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.EAdOperation;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.tools.MathEvaluator;
import es.eucm.ead.tools.StringHandler;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Singleton
public class GameState extends ValueMap implements
		MathEvaluator.OperationResolver, TweenAccessor<EAdField<?>> {

	private static final char BEGIN_VAR_CHAR = '[';

	private static final char END_VAR_CHAR = ']';

	private static final char BEGIN_CONDITION_CHAR = '(';

	private static final char END_CONDITION_CHAR = ')';

	private static Logger logger = LoggerFactory.getLogger(GameState.class);

	/**
	 * Operator factory
	 */
	protected OperatorFactory operatorFactory;

	/**
	 * Map containing field watchers by element and variable
	 */
	private Map<String, Map<String, List<FieldWatcher>>> fieldWatchers;

	@Inject
	public GameState(StringHandler stringHandler,
			ReflectionProvider reflectionProvider) {
		super(reflectionProvider, stringHandler);
		this.operatorFactory = new OperatorFactory(reflectionProvider, this,
				stringHandler);

		// Field watcher
		fieldWatchers = new HashMap<String, Map<String, List<FieldWatcher>>>();

	}

	/**
	 * Evaluates a condition, using the required evaluator
	 *
	 * @param <T>       The actual condition class
	 * @param condition The condition to be evaluated
	 * @return The result of evaluating the condition according to the current
	 *         values of the game state
	 */
	public <T extends EAdCondition> boolean evaluate(T condition) {
		if (condition == null) {
			return true;
		}
		return operatorFactory.operate(Boolean.class, condition);
	}

	public <T extends EAdOperation, S> S operate(Class<S> eAdVar, T eAdOperation) {
		return operatorFactory.operate(eAdVar, eAdOperation);
	}

	/**
	 * Sets the field to the result value of the operation
	 *
	 * @param field     the field
	 * @param operation the operation
	 */
	public <S> void setValue(EAdField<S> field, EAdOperation operation) {
		setValue(field.getElement(), field.getVarDef(), operation);
	}

	/**
	 * Sets the element's variable value to the given operation
	 *
	 * @param element   the element holding the variable. If the element is
	 *                  {@code null}, it's considered that the variable belongs to the
	 *                  system (it's a global field)
	 * @param var       the variable definition
	 * @param operation the operation whose result will be assigned to the variable
	 */
	public <S> void setValue(Identified element, EAdVarDef<S> var,
			EAdOperation operation) {
		S result = operatorFactory.operate(var.getType(), operation);
		setValue(element, var, result);
	}

	public void setValue(String elementId, String varName, Object value) {
		super.setValue(elementId, varName, value);
		notifyWatchers(elementId, varName, value);
	}

	public int getValues(EAdField<?> field, int type, float[] values) {
		switch (type) {
		default:
			Object o = getValue(field);
			if (o instanceof Number) {
				values[0] = ((Number) o).floatValue();
				return 1;
			} else {
				return 0;
			}
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	public void setValues(EAdField field, int type, float[] values) {
		switch (type) {
		default:
			if (field.getVarDef().getType() == Float.class) {
				setValue(field, values[0]);
			} else if (field.getVarDef().getType() == Integer.class) {
				setValue(field, (int) values[0]);
			}
		}

	}

	// ---------------------------------//
	// TEXT PROCESSING
	// ---------------------------------//

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
	public String processTextVars(String text, EAdList<EAdOperation> operations) {
		text = processConditionalExpressions(text, operations);
		return processVars(text, operations);
	}

	private String processVars(String text, EAdList<EAdOperation> operations) {
		boolean done = false;
		while (!done) {
			int i = text.indexOf(BEGIN_VAR_CHAR);
			if (i != -1) {
				int separatorIndex = text.indexOf(END_VAR_CHAR, i + 1);
				if (separatorIndex != -1) {
					String varName = text.substring(i + 1, separatorIndex);
					int index = 0;
					try {
						index = Integer.parseInt(varName);
					} catch (NumberFormatException e) {
						logger.warn("{} is not a valid var index", varName);
					}
					Object o = operatorFactory.operate(Object.class, operations
							.get(index));

					if (o == null) {
						o = "";
					}

					String value = o instanceof EAdString ? stringHandler
							.getString((EAdString) o) : o.toString();
					text = text.substring(0, i) + value
							+ text.substring(separatorIndex + 1);

				}
			} else {
				done = true;
			}
		}
		return text;
	}

	private String processConditionalExpressions(String text,
			EAdList<EAdOperation> fields) {
		String newText = "";
		if (text != null) {
			int i = 0;
			boolean finished = false;
			while (!finished && i < text.length()) {
				int beginCondition = text.indexOf(BEGIN_CONDITION_CHAR, i);
				int endCondition = text.indexOf(END_CONDITION_CHAR,
						beginCondition);
				if (beginCondition != -1 && endCondition != -1
						&& endCondition > beginCondition) {
					String condition = text.substring(beginCondition + 1,
							endCondition);
					String result = evaluateExpression(condition, fields);
					newText += text.substring(i, beginCondition) + result;
					i = endCondition + 1;
				} else {
					newText += text.substring(i);
					finished = true;
				}
			}
		}
		return newText;
	}

	/**
	 * Evaluates conditional expressions (#boolean_var? value_1 : value_2 )
	 *
	 * @param expression the expression to evaluate
	 * @param operations a list of operations. In the expression, [i] will be substituted by the result of operations[i]
	 * @return the expression evaluated
	 */
	private String evaluateExpression(String expression,
			EAdList<EAdOperation> operations) {

		if (expression.contains("?") && expression.contains(":")) {
			int questionMark = expression.indexOf('?');
			int points = expression.indexOf(':');
			String condition = expression.substring(0, questionMark);
			String trueValue = expression.substring(questionMark + 1, points);
			String falseValue = expression.substring(points + 1, expression
					.length());

			int beginVar = condition.indexOf(BEGIN_VAR_CHAR);
			int endVar = condition.indexOf(END_VAR_CHAR);
			if (beginVar != -1 && endVar != -1 && endVar > beginVar) {

				int indexCondition;
				String varName = "";
				try {
					varName = expression.substring(beginVar + 1, endVar);
					indexCondition = Integer.parseInt(varName);
				} catch (NumberFormatException e) {
					logger.warn(varName + " is not a valid index in "
							+ expression);
					return BEGIN_CONDITION_CHAR + expression
							+ END_CONDITION_CHAR;
				}

				Object o = operatorFactory.operate(Object.class, operations
						.get(indexCondition));

				if (o != null && o instanceof Boolean) {
					Boolean b = (Boolean) o;
					if (b)
						return trueValue;
					else
						return falseValue;
				} else if (o != null && o instanceof Number) {
					Number n = (Number) o;
					if (n.floatValue() != 0)
						return trueValue;
					else
						return falseValue;
				}
				return falseValue;
			}
		}

		return BEGIN_CONDITION_CHAR + expression + END_CONDITION_CHAR;
	}

	@SuppressWarnings( { "all" })
	private <T> void notifyWatchers(String elementId, String varName, T value) {
		Map<String, List<FieldWatcher>> map = fieldWatchers.get(elementId);
		if (map != null) {
			List<FieldWatcher> list = map.get(varName);
			if (list != null) {
				for (FieldWatcher fw : list) {
					fw.fieldUpdated(elementId, varName, value);
				}
			}
		}
	}

	/**
	 * Adds a field watcher that is notified every time the given field is
	 * updated
	 *
	 * @param fieldWatcher the field watcher
	 * @param field        the field to watch
	 */
	public void addFieldWatcher(FieldWatcher fieldWatcher, EAdField<?> field) {
		addFieldWatcher(fieldWatcher, field.getElement() == null ? null : field
				.getElement().getId(), field.getVarDef().getName());
	}

	/**
	 * Adds a field watcher that is notified every time the given field is
	 * updated
	 *
	 * @param elementId the element's id
	 * @param varName   the variable name
	 */
	public void addFieldWatcher(FieldWatcher fieldWatcher, String elementId,
			String varName) {
		Map<String, List<FieldWatcher>> map = fieldWatchers.get(elementId);
		if (map == null) {
			map = new HashMap<String, List<FieldWatcher>>();
			fieldWatchers.put(elementId, map);
		}
		List<FieldWatcher> list = map.get(varName);
		if (list == null) {
			list = new ArrayList<FieldWatcher>();
			map.put(varName, list);
		}
		list.add(fieldWatcher);
	}

	/**
	 * Removes a field watcher
	 *
	 * @param fieldWatcher the field watcher to remove
	 */
	public void removeFieldWatcher(FieldWatcher fieldWatcher) {
		for (Entry<String, Map<String, List<FieldWatcher>>> e : fieldWatchers
				.entrySet()) {
			for (List<FieldWatcher> l : e.getValue().values()) {
				l.remove(fieldWatcher);
			}
		}
	}

	/**
	 * Resets the game state to its initial state
	 */
	public void reset() {
		fieldWatchers.clear();
		valuesMap.clear();
		updateList.clear();
	}

	public int countWatchers(String element, String varName) {
		Map<String, List<FieldWatcher>> watchers = fieldWatchers.get(element);
		if (watchers != null) {
			List<FieldWatcher> list = watchers.get(varName);
			return list == null ? 0 : list.size();
		}
		return 0;
	}

	public interface FieldWatcher {
		/**
		 * Call whenever the field is updated
		 */
		<T> void fieldUpdated(String elementId, String varName, T value);

	}
}
