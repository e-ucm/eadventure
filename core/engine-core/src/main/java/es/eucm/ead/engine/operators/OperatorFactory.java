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

package es.eucm.ead.engine.operators;

import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.operators.evaluators.EvaluatorFactory;
import es.eucm.ead.model.elements.conditions.ANDCond;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.ORCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.operations.ConcatenateStringsOp;
import es.eucm.ead.model.elements.operations.ConditionedOp;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.elements.operations.StringOp;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.tools.StringHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory with all {@link Operator} for all {@link es.eucm.ead.model.elements.operations.Operation}. The Game
 * State is the only class that should access to this factory. If you're using
 * it somewhere else, try to use
 */
public class OperatorFactory {

	static private Logger logger = LoggerFactory
			.getLogger(OperatorFactory.class);

	private Map<Class<?>, Operator<?>> factoryMap;

	public OperatorFactory(GameState gameState, StringHandler stringHandler) {
		factoryMap = new HashMap<Class<?>, Operator<?>>();
		EvaluatorFactory evaluatorFactory = new EvaluatorFactory(this);
		FieldOperator fieldOperator = new FieldOperator(gameState);
		// Conditions
		factoryMap.put(ANDCond.class, evaluatorFactory);
		factoryMap.put(EmptyCond.class, evaluatorFactory);
		factoryMap.put(NOTCond.class, evaluatorFactory);
		factoryMap.put(ORCond.class, evaluatorFactory);
		factoryMap.put(OperationCond.class, evaluatorFactory);

		factoryMap.put(MathOp.class, new MathOperator(gameState));
		factoryMap.put(ValueOp.class, new ValueOperator());
		factoryMap.put(ElementField.class, fieldOperator);
		factoryMap.put(ElementField.class, fieldOperator);
		factoryMap.put(ConditionedOp.class, new ConditionedOperator(
				evaluatorFactory, this));
		factoryMap.put(ConcatenateStringsOp.class,
				new ConcatenateStringsOperator(gameState));
		factoryMap.put(StringOp.class, new StringOperator(stringHandler,
				gameState));
	}

	/**
	 * <p/>
	 * Calculates the result of the given {@link es.eucm.ead.model.elements.operations.Operation}
	 *
	 * @param <T>
	 * @param operation operation to be done
	 * @return operation's result. If operation is {@code null}, a null is
	 *         returned.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Operation, S> S operate(T operation) {
		if (operation == null) {
			logger.warn("Null operation attempted for operation.");
			return null;
		}
		Operator<T> operator = (Operator<T>) factoryMap.get(operation
				.getClass());
		return operator.operate(operation);
	}

}
