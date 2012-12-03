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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.variables.EAdOperation;
import ead.common.params.text.EAdString;
import ead.engine.core.operators.OperatorFactory;
import ead.tools.StringHandler;
import ead.tools.reflection.ReflectionProvider;

@Singleton
public class VariableMap extends ValueMapImpl {

	private static final char BEGIN_VAR_CHAR = '[';

	private static final char END_VAR_CHAR = ']';

	private static final char BEGIN_CONDITION_CHAR = '(';

	private static final char END_CONDITION_CHAR = ')';

	private StringHandler stringHandler;

	private OperatorFactory operatorFactory;

	@Inject
	public VariableMap(ReflectionProvider reflectionProvider,
			OperatorFactory operatorFactory,
			StringHandler stringHandler) {
		super(reflectionProvider);
		this.stringHandler = stringHandler;
		this.operatorFactory = operatorFactory;
	}

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
	public String processTextVars(String text,
			EAdList<EAdOperation> operations) {
		text = processConditionalExpressions(text, operations);
		return processVars(text, operations);
	}

	private String processVars(String text,
			EAdList<EAdOperation> operations) {
		int i = 0;
		boolean done = false;
		while (i < text.length() && !done) {
			i = text.indexOf(BEGIN_VAR_CHAR, i);
			if (i != -1) {
				int separatorIndex = text
						.indexOf(END_VAR_CHAR, i + 1);
				if (separatorIndex != -1) {
					String varName = text.substring(i + 1,
							separatorIndex);
					Integer index = new Integer(varName);
					Object o = operatorFactory.operate(Object.class,
							operations.get(index));
					if (o != null) {
						String value = o instanceof EAdString ? stringHandler
								.getString((EAdString) o) : o
								.toString();
						text = text.substring(0, i) + value
								+ text.substring(separatorIndex + 1);
					}
				}
				i = separatorIndex + 1;

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
				int beginCondition = text.indexOf(
						BEGIN_CONDITION_CHAR, i);
				int endCondition = text.indexOf(END_CONDITION_CHAR,
						beginCondition);
				if (beginCondition != -1 && endCondition != -1
						&& endCondition > beginCondition) {
					String condition = text.substring(
							beginCondition + 1, endCondition);
					String result = evaluateExpression(condition,
							fields);
					newText += text.substring(i, beginCondition)
							+ result;
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
	 * @param expression
	 * @return
	 */
	public String evaluateExpression(String expression,
			EAdList<EAdOperation> operations) {

		if (expression.contains("?") && expression.contains(":")) {
			int questionMark = expression.indexOf('?');
			int points = expression.indexOf(':');
			String condition = expression.substring(0, questionMark);
			String trueValue = expression.substring(questionMark + 1,
					points);
			String falseValue = expression.substring(points + 1,
					expression.length());

			int beginVar = condition.indexOf(BEGIN_VAR_CHAR);
			int endVar = condition.indexOf(END_VAR_CHAR);
			if (beginVar != -1 && endVar != -1 && endVar > beginVar) {

				Integer indexCondition = 0;
				String varName = "";
				try {
					varName = expression.substring(beginVar + 1,
							endVar);
					indexCondition = new Integer(varName);
				} catch (NumberFormatException e) {
					logger.warn(varName + " is not a valid index in "
							+ expression);
					return BEGIN_CONDITION_CHAR + expression
							+ END_CONDITION_CHAR;
				}

				Object o = operatorFactory.operate(Object.class,
						operations.get(indexCondition));

				if (o != null && o instanceof Boolean) {
					Boolean b = (Boolean) o;
					if (b.booleanValue())
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
}
