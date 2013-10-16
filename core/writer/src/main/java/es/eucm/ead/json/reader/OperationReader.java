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

import com.google.gson.internal.StringMap;
import es.eucm.ead.model.elements.operations.*;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.reader.ObjectsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class OperationReader {

	static private Logger logger = LoggerFactory
			.getLogger(OperationReader.class);

	private ConditionReader conditionReader;
	private ObjectsFactory objectsFactory;

	public OperationReader(ObjectsFactory objectsFactory,
			ConditionReader conditionReader) {
		this.conditionReader = conditionReader;
		this.objectsFactory = objectsFactory;
		this.conditionReader.setOperationReader(this);
	}

	public EAdOperation read(StringMap<Object> op) {
		String type = (String) op.get("type");
		EAdOperation operation = null;
		if (type.equals("field")) {
			operation = parseField(op);
		} else if (type.equals("cond")) {
			operation = conditionReader.read(op);
		} else if (type.equals("math")) {
			operation = parseMath(op);
		} else if (type.equals("valuenumber")) {
			operation = parseValueNumber(op);
		} else if (type.equals("concatstring")) {
			operation = parseConcatString(op);
		} else if (type.equals("value")) {
			operation = new ValueOp(op.get("value"));
		} else if (type.equals("valueeadstring")) {
			operation = new ValueOp(new EAdString((String) op.get("value")));
		} else if (type.equals("conditioned")) {
			operation = parseConditioned(op);
		} else {
			logger.warn("Unknown operation type: {}", type);
		}

		// Add operations
		Collection<StringMap<Object>> operations = (Collection<StringMap<Object>>) op
				.get("operations");
		if (operations != null)
			for (StringMap<Object> o : operations) {
				operation.getOperations().add(read(o));
			}
		return operation;
	}

	private EAdOperation parseConditioned(StringMap<Object> op) {
		StringMap<Object> opT = (StringMap<Object>) op.get("opTrue");
		StringMap<Object> opF = (StringMap<Object>) op.get("opFalse");
		StringMap<Object> cond = (StringMap<Object>) op.get("cond");
		return new ConditionedOp(conditionReader.read(cond), read(opT),
				read(opF));
	}

	private EAdOperation parseConcatString(StringMap<Object> op) {
		String preffix = (String) op.get("preffix");
		String suffix = (String) op.get("suffix");
		ConcatenateStringsOp concat = new ConcatenateStringsOp(preffix, suffix);

		return concat;
	}

	private EAdOperation parseValueNumber(StringMap<Object> op) {
		Number value = (Number) op.get("value");
		Boolean integer = (Boolean) op.get("integer");
		if (integer != null && integer.booleanValue()) {
			return new ValueOp(new Integer(value.intValue()));
		} else {
			return new ValueOp(new Float(value.floatValue()));
		}
	}

	private EAdOperation parseMath(StringMap<Object> op) {
		String expression = (String) op.get("expression");
		MathOp operation = new MathOp(expression);
		Collection<StringMap<Object>> operations = (Collection<StringMap<Object>>) op
				.get("operations");
		for (StringMap<Object> o : operations) {
			operation.getOperations().add(read(o));
		}
		Boolean integer = (Boolean) op.get("integer");
		operation.setResultAsInteger(integer != null && integer.booleanValue());
		return operation;
	}

	private EAdOperation parseField(StringMap<Object> op) {
		String field = (String) op.get("field");
		return translateField(field);
	}

	public EAdField<?> translateField(String field) {
		String[] fs = field.split("\\.");
		if (fs.length != 2) {
			logger.error("{} is not a valid field", field);
		}
		return objectsFactory.getField(fs[0], fs[1]);
	}

}
