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

package ead.json.reader;

import com.google.gson.internal.StringMap;

import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.operations.EAdField;
import ead.reader.model.ObjectsFactory;

public class ConditionReader {

	private OperationReader operationReader;

	private ObjectsFactory objectsFactory;

	public ConditionReader(ObjectsFactory objectsFactory) {
		this.objectsFactory = objectsFactory;
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
		}
		return null;
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
		} else if (comparator.equals("==")) {
			comp = Comparator.EQUAL;
		} else if (comparator.equals("<=")) {
			comp = Comparator.LESS_EQUAL;
		} else if (comparator.equals(">")) {
			comp = Comparator.GREATER;
		} else if (comparator.equals(">=")) {
			comp = Comparator.GREATER_EQUAL;
		}
		cond.setComparator(comp);
		cond.setOp1(operationReader.read((StringMap<Object>) c.get("op1")));
		cond.setOp2(operationReader.read((StringMap<Object>) c.get("op2")));
		return cond;
	}

}
