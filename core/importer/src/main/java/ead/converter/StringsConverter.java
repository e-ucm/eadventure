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

package ead.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.params.text.EAdString;
import ead.importer.interfaces.EAdElementFactory;

@Singleton
public class StringsConverter {

	private static final String PREFIX = "converter.string";

	private ModelQuerier modelQuerier;

	private Map<EAdString, String> strings;
	private Map<String, EAdString> reverse;

	@Inject
	public StringsConverter(ModelQuerier modelQuerier) {
		this.modelQuerier = modelQuerier;
		strings = new HashMap<EAdString, String>();
		reverse = new HashMap<String, EAdString>();
	}

	/**
	 * Converts a string to an EAdString
	 * 
	 * @param text
	 * @return
	 */
	public EAdString convert(String text) {
		EAdString string = reverse.get(text);
		if (string == null) {
			string = new EAdString(PREFIX + strings.size());
			strings.put(string, translateLine(text));
			reverse.put(text, string);
		}
		return string;
	}

	/**
	 * Returns the strings contained by this converter
	 * 
	 * @return
	 */
	public Map<EAdString, String> getStrings() {
		return strings;
	}

	/**
	 * Generates a list of operations for the given text
	 * @param text
	 * @return
	 */
	public List<EAdOperation> getOperations(String text) {
		int i = 0;
		ArrayList<EAdOperation> operations = new ArrayList<EAdOperation>();
		boolean finished = false;
		while (!finished && i < text.length()) {
			int beginIndex = text.indexOf('(', i);
			int endIndex = text.indexOf(')', i);
			int questionMark = text.indexOf('?', i);
			if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex
					&& questionMark > beginIndex) {
				EAdOperation op = createOperation(text.substring(
						beginIndex + 2, questionMark));
				if (op != null) {
					operations.add(op);
				}
				i = endIndex + 1;
			} else {
				finished = true;
			}
		}
		return operations;
	}

	/**
	 * Creates an operation for the conditional expression
	 * @param condition
	 * @return
	 */
	private EAdOperation createOperation(String condition) {
		Comparator comparator = Comparator.DIFFERENT;
		String[] comparison = new String[] { new String(condition) };
		if (condition.contains(">=")) {
			comparator = Comparator.GREATER_EQUAL;
			comparison = condition.split(">=");
		} else if (condition.contains(">")) {
			comparator = Comparator.GREATER;
			comparison = condition.split(">");
		} else if (condition.contains("<=")) {
			comparator = Comparator.LESS_EQUAL;
			comparison = condition.split("<=");
		} else if (condition.contains("<")) {
			comparator = Comparator.LESS;
			comparison = condition.split("<");
		} else if (condition.contains("==")) {
			comparator = Comparator.EQUAL;
			comparison = condition.split("==");
		} else {
			return modelQuerier.getFlag(condition);
		}

		if (comparison.length == 2) {
			EAdField<?> op1 = modelQuerier.getVariable(condition);
			Integer number = null;
			try {
				number = new Integer(comparison[1]);
			} catch (NumberFormatException e) {
				return null;
			}
			if (op1 != null && number != null)
				return new OperationCond(op1, number, comparator);
		}

		return null;
	}

	/**
	 * Translates strings with conditional operations to strings with operations
	 * in the new model. Use
	 * {@link StringsConverter#getOperations(String, EAdElementFactory)} to
	 * obtain the operations associated to this string
	 * 
	 * @param text
	 * @return
	 */
	public String translateLine(String text) {
		String finalText = text;
		int i = 0;
		int varNumber = 0;
		boolean finished = false;
		while (!finished && i < text.length()) {
			int beginIndex = text.indexOf('(', i);
			int endIndex = text.indexOf(')', i);
			int questionMark = text.indexOf('?', i);
			if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex
					&& questionMark > beginIndex) {
				String varName = text.substring(beginIndex + 2, questionMark);
				finalText = finalText.replace("#" + varName, "[" + varNumber
						+ "]");
				varNumber++;
				i = endIndex + 1;
			} else {
				finished = true;
			}
		}
		return finalText;
	}

}
