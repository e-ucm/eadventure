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

import es.eucm.ead.model.elements.conditions.ANDCond;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.ORCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.ConcatenateStringsOp;
import es.eucm.ead.model.elements.operations.ConditionedOp;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.ListOp;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.StringOp;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.operators.ConcatenateStringsOperator;
import es.eucm.ead.engine.operators.ConditionedOperator;
import es.eucm.ead.engine.operators.FieldOperator;
import es.eucm.ead.engine.operators.ListOperator;
import es.eucm.ead.engine.operators.MathOperator;
import es.eucm.ead.engine.operators.Operator;
import es.eucm.ead.engine.operators.OperatorFactory;
import es.eucm.ead.engine.operators.StringOperator;
import es.eucm.ead.engine.operators.ValueOperator;
import es.eucm.ead.engine.operators.evaluators.EvaluatorFactory;
import es.eucm.ead.tools.StringHandler;
import es.eucm.ead.tools.reflection.ReflectionProvider;

public class OperatorsMapProvider extends
		AbstractMapProvider<Class<?>, Operator<?>> {

	private static Map<Class<?>, Operator<?>> tempMap = new HashMap<Class<?>, Operator<?>>();

	private EvaluatorFactory evaluatorFactory;

	private ReflectionProvider reflectionProvider;

	private GameState gameState;

	private OperatorFactory operatorFactory;

	private StringHandler stringHandler;

	public OperatorsMapProvider(OperatorFactory operatorFactory,
			GameState valueMap, EvaluatorFactory evaluatorFactory,
			ReflectionProvider reflectionProvider, StringHandler stringHandler) {
		super();
		this.gameState = valueMap;
		this.evaluatorFactory = evaluatorFactory;
		this.reflectionProvider = reflectionProvider;
		this.operatorFactory = operatorFactory;
		this.stringHandler = stringHandler;
	}

	@Override
	public Map<Class<?>, Operator<?>> getMap() {
		FieldOperator fieldOperator = new FieldOperator(gameState);

		// Conditions
		factoryMap.put(ANDCond.class, evaluatorFactory);
		factoryMap.put(EmptyCond.class, evaluatorFactory);
		factoryMap.put(NOTCond.class, evaluatorFactory);
		factoryMap.put(ORCond.class, evaluatorFactory);
		factoryMap.put(OperationCond.class, evaluatorFactory);

		factoryMap.put(MathOp.class, new MathOperator(gameState));
		factoryMap.put(ValueOp.class, new ValueOperator(reflectionProvider));
		factoryMap.put(EAdField.class, fieldOperator);
		factoryMap.put(BasicField.class, fieldOperator);
		factoryMap.put(ListOp.class, new ListOperator(gameState));
		factoryMap.put(ConditionedOp.class, new ConditionedOperator(
				evaluatorFactory, operatorFactory));
		factoryMap.put(ConcatenateStringsOp.class,
				new ConcatenateStringsOperator(gameState));
		factoryMap.put(StringOp.class, new StringOperator(stringHandler,
				gameState));
		factoryMap.putAll(tempMap);
		return super.getMap();
	}

	public static void add(Class<?> operation, Operator<?> operator) {
		tempMap.put(operation, operator);
	}

}
