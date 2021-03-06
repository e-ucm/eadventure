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

package es.eucm.ead.engine.operators.evaluators;

import es.eucm.ead.engine.game.ValueMap;
import es.eucm.ead.engine.operators.Operator;
import es.eucm.ead.engine.operators.OperatorFactory;
import es.eucm.ead.model.elements.conditions.ANDCond;
import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.ORCond;
import es.eucm.ead.model.elements.conditions.OperationCond;

import java.util.HashMap;
import java.util.Map;

public class EvaluatorFactory implements Operator<Condition> {

	private Map<Class<?>, Evaluator<?>> factoryMap;

	public EvaluatorFactory(OperatorFactory operatorFactory) {
		factoryMap = new HashMap<Class<?>, Evaluator<?>>();
		factoryMap.put(EmptyCond.class, new EmptyCondEvaluator());
		factoryMap.put(OperationCond.class, new OperationCondEvaluator(
				operatorFactory));
		factoryMap.put(ANDCond.class, new ListedCondEvaluator(this));
		factoryMap.put(ORCond.class, new ListedCondEvaluator(this));
		factoryMap.put(NOTCond.class, new NOTCondEvaluator(this));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S operate(Condition condition) {
		return (S) Boolean.valueOf(evaluate(condition));
	}

	/**
	 * Evaluates a condition, using the required evaluator, based on a given
	 * {@link ValueMap}.
	 * 
	 * @param <T>
	 *            The actual condition class
	 * @param condition
	 *            The condition to be evaluated
	 * @return The result of evaluating the condition according to the given set
	 *         of values
	 */
	@SuppressWarnings("unchecked")
	public <T extends Condition> boolean evaluate(T condition) {
		if (condition == null)
			return true;
		Evaluator<T> evaluator = (Evaluator<T>) factoryMap.get(condition
				.getClass());
		return evaluator.evaluate(condition);
	}

}
