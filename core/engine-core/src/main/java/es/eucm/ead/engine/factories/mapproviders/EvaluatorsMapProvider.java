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

package es.eucm.ead.engine.factories.mapproviders;

import java.util.HashMap;
import java.util.Map;

import es.eucm.ead.engine.operators.evaluators.EmptyCondEvaluator;
import es.eucm.ead.engine.operators.evaluators.Evaluator;
import es.eucm.ead.engine.operators.evaluators.ListedCondEvaluator;
import es.eucm.ead.model.elements.conditions.ANDCond;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.ORCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.engine.game.interfaces.ValueMap;
import es.eucm.ead.engine.operators.OperatorFactory;
import es.eucm.ead.engine.operators.evaluators.EvaluatorFactory;
import es.eucm.ead.engine.operators.evaluators.NOTCondEvaluator;
import es.eucm.ead.engine.operators.evaluators.OperationCondEvaluator;

public class EvaluatorsMapProvider extends
		AbstractMapProvider<Class<?>, Evaluator<?>> {

	private static Map<Class<?>, Evaluator<?>> tempMap = new HashMap<Class<?>, Evaluator<?>>();

	public EvaluatorsMapProvider(ValueMap valueMap,
			EvaluatorFactory evaluatorFactory, OperatorFactory operatorFactory) {
		super();
		factoryMap.put(EmptyCond.class, new EmptyCondEvaluator());
		factoryMap.put(OperationCond.class, new OperationCondEvaluator(
				operatorFactory));
		factoryMap
				.put(ANDCond.class, new ListedCondEvaluator(evaluatorFactory));
		factoryMap.put(ORCond.class, new ListedCondEvaluator(evaluatorFactory));
		factoryMap.put(NOTCond.class, new NOTCondEvaluator(evaluatorFactory));
		factoryMap.putAll(tempMap);
	}

	public static void add(Class<?> condition, Evaluator<?> evaluator) {
		tempMap.put(condition, evaluator);
	}

}
