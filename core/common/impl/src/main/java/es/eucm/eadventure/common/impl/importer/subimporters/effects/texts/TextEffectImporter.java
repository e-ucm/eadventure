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

package es.eucm.eadventure.common.impl.importer.subimporters.effects.texts;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.EffectImporter;
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.conditions.impl.enums.Comparator;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.resources.StringHandler;

public abstract class TextEffectImporter<T extends AbstractEffect> extends
		EffectImporter<T, EAdSpeakEffect> {

	protected static int ID_GENERATOR = 0;

	protected StringHandler stringHandler;

	protected EAdElementFactory factory;

	public TextEffectImporter(StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(conditionImporter);
		this.stringHandler = stringHandler;
		this.factory = factory;
	}

	@Override
	public EAdSpeakEffect init(T oldObject) {
		return new EAdSpeakEffect();
	}

	public EAdSpeakEffect convert(T oldObject, Object object) {
		EAdSpeakEffect showText = super.convert(oldObject, object);

		showText.setBlocking(true);
		showText.setOpaque(true);
		showText.setQueueable(true);

		return showText;
	}

	public static List<EAdOperation> getOperations(String text,
			EAdElementFactory factory) {
		int i = 0;
		ArrayList<EAdOperation> operations = new ArrayList<EAdOperation>();
		boolean finished = false;
		while (!finished && i < text.length()) {
			int beginIndex = text.indexOf('(', i);
			int endIndex = text.indexOf(')', i);
			int questionMark = text.indexOf('?', i);
			if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex
					&& questionMark > beginIndex) {
				EAdOperation op = createOperation(
						text.substring(beginIndex + 2, questionMark), factory);
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

	private static EAdOperation createOperation(String condition,
			EAdElementFactory factory) {
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
			return factory.getVarByOldId(condition, Condition.FLAG_CONDITION);
		}

		if (comparison.length == 2) {
			EAdField<?> op1 = factory.getVarByOldId(comparison[0],
					Condition.VAR_CONDITION);
			Integer number = null;
			try {
				number = new Integer(comparison[1]);
			} catch (NumberFormatException e) {
				return null;
			}
			if (op1 != null && number != null)
				return new BooleanOperation(new OperationCondition(op1, number,
						comparator));
		}

		return null;
	}

	public static String translateLine(String text) {
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
