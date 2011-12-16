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

package es.eucm.eadventure.engine.core.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.game.ValueMap;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;

@Singleton
public class VariableMap extends ValueMapImpl implements ValueMap {
	
	@Inject
	public VariableMap(ReflectionProvider reflectionProvider, OperatorFactory operatorFactory, EvaluatorFactory evaluatorFactory ) {
		super(reflectionProvider, operatorFactory, evaluatorFactory);
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
	 * <li><b>#var_name:</b> this text is substituted for the value in the map
	 * associated with the {@link String} "var_name" . If there's no such var,
	 * it remains the same.</li>
	 * <li><b>(#boolean_var? text if 'boolean_var' is true : text if
	 * 'boolean_var' is false )</b> A conditional text, depending of
	 * "boolean_var" value. This text can be plain text or contains some other
	 * var values with the first format</p>
	 * 
	 * @param text
	 *            the text to be processed by the value map
	 * @return the processed text
	 */
	public String processTextVars(String text, EAdList<EAdField<?>> fields) {
		text = processConditionalExpressions(text, fields);
		return processVars(text, fields);
	}

	private String processVars(String text, EAdList<EAdField<?>> fields) {
		char[] separators = new char[] { ' ', ',', '.', '?', '!', '-', ')' };
		int i = 0;
		int space = 0;
		boolean done = false;
		while (i < text.length() && !done) {
			i = text.indexOf('#', i);
			if (i != -1) {
				space = -1;
				int j = 0;
				while (j < separators.length) {
					int separatorIndex = text.indexOf(separators[j], i + 1);
					if (separatorIndex != -1) {
						space = space == -1 || separatorIndex < space ? separatorIndex
								: space;
					}
					j++;
				}

				space = space == -1 ? text.length() : space;
				String varName = text.substring(i + 1, space);

				Object o = this.getValue(varName, fields);
				if (o != null) {
					text = text.substring(0, i) + o.toString()
							+ text.substring(space);
				}
				i = space + 1;

			} else {
				done = true;
			}
		}
		return text;
	}

	private Object getValue(String varName, EAdList<EAdField<?>> fields) {
		int pos = Integer.parseInt(varName);
		if ( pos >= 0 && pos < fields.size()){
			return getValue(fields.get(pos));
		}

		return null;
	}
	
	private String processConditionalExpressions(String text, EAdList<EAdField<?>> fields) {
		String newText = "";
		if (text != null) {
			String[] parts = text.split("\\(");
			if (parts.length == 1)
				return text;

			for (int i = 0; i < parts.length; i++) {
				String part = parts[i];
				if (part.length() > 0 && part.charAt(0) == '#') {
					String[] parts2 = part.split("\\)");

					parts2[0] = evaluateExpression(parts2[0], fields);

					parts[i] = parts2[0];
					for (int j = 1; j < parts2.length; j++) {
						parts[i] += parts2[j];
					}

				} else if (i > 0) {
					parts[i] = "(" + part;
				}

				newText += parts[i];
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
	public String evaluateExpression(String expression, EAdList<EAdField<?>> fields) {

		if (expression.contains("?") && expression.contains(":")) {
			String[] values = expression.substring(1).split("\\?|\\:");

			if (values.length != 3)
				return "(" + expression + ")";

			Object o = getValue(values[0], fields);
			
			if (o != null && o instanceof Boolean) {
				Boolean b = (Boolean) o;
				if (b.booleanValue() == true)
					return values[1];
				else
					return values[2];
			} else if (o != null && o instanceof Number) {
				Number n = (Number) o;
				if (n.floatValue() != 0)
					return values[1];
				else
					return values[2];
			}
			return values[2];
		}

		return "(" + expression + ")";
	}
	
}
