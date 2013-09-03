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

package es.eucm.ead.json.reader;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.internal.StringMap;

import es.eucm.ead.model.elements.EAdCondition;
import es.eucm.ead.model.elements.conditions.ANDCond;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.ORCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.conditions.enums.Comparator;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.reader.model.ObjectsFactory;

@SuppressWarnings("unchecked")
public class ConditionReader {

	static private Logger logger = LoggerFactory
			.getLogger(ConditionReader.class);

	private OperationReader operationReader;

	public ConditionReader(ObjectsFactory objectsFactory) {

	}

	public void setOperationReader(OperationReader operationReader) {
		this.operationReader = operationReader;
	}

	public EAdCondition read(StringMap<Object> c) {
		String cond = (String) c.get("cond");
		if (cond.equals("empty")) {
			Boolean value = (Boolean) c.get("value");
			return value ? EmptyCond.TRUE : EmptyCond.FALSE;
		} else if (cond.equals("comparison")) {
			return parseComparison(c);
		} else if (cond.equals("booleanfield")) {
			return parseBooleanField(c);
		} else if (cond.equals("not")) {
			return parseNot(c);
		} else if (cond.equals("or")) {
			return parseOr(c);
		} else if (cond.equals("and")) {
			return parseAnd(c);
		}
		return null;
	}

	private EAdCondition parseAnd(StringMap<Object> c) {
		Collection<StringMap<Object>> operations = (Collection<StringMap<Object>>) c
				.get("operations");
		EAdList<EAdCondition> conditions = new EAdList<EAdCondition>();
		for (StringMap<Object> op : operations) {
			conditions.add(read(op));
		}
		return new ANDCond(conditions);
	}

	private EAdCondition parseOr(StringMap<Object> c) {
		StringMap<Object> op1 = (StringMap<Object>) c.get("op1");
		StringMap<Object> op2 = (StringMap<Object>) c.get("op2");
		EAdCondition condition1 = read(op1);
		EAdCondition condition2 = read(op2);
		return new ORCond(condition1, condition2);
	}

	private EAdCondition parseNot(StringMap<Object> c) {
		EAdCondition cond = read((StringMap<Object>) c.get("value"));
		return new NOTCond(cond);
	}

	private EAdCondition parseBooleanField(StringMap<Object> c) {
		String field = (String) c.get("field");
		return new OperationCond((EAdField<Boolean>) operationReader
				.translateField(field));
	}

	private EAdCondition parseComparison(StringMap<Object> c) {
		OperationCond cond = new OperationCond();
		String comparator = (String) c.get("comparator");
		Comparator comp = null;
		if (comparator.equals("<")) {
			comp = Comparator.LESS;
		} else if (comparator.equals("=")) {
			comp = Comparator.EQUAL;
		} else if (comparator.equals("<=")) {
			comp = Comparator.LESS_EQUAL;
		} else if (comparator.equals(">")) {
			comp = Comparator.GREATER;
		} else if (comparator.equals(">=")) {
			comp = Comparator.GREATER_EQUAL;
		} else if (comparator.equals("!=")) {
			comp = Comparator.DIFFERENT;
		} else {
			logger.warn("No comparator for {}", comparator);
		}
		cond.setComparator(comp);
		cond.setOp1(operationReader.read((StringMap<Object>) c.get("op1")));
		cond.setOp2(operationReader.read((StringMap<Object>) c.get("op2")));
		return cond;
	}

}
