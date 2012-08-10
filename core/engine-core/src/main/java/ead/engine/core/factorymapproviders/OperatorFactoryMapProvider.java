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

package ead.engine.core.factorymapproviders;

import java.util.HashMap;
import java.util.Map;

import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.model.elements.variables.operations.ConditionedOp;
import ead.common.model.elements.variables.operations.ListOp;
import ead.common.model.elements.variables.operations.MathOp;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.game.ValueMap;
import ead.engine.core.operators.BooleanOperator;
import ead.engine.core.operators.ConditionedOperator;
import ead.engine.core.operators.FieldOperator;
import ead.engine.core.operators.ListOperator;
import ead.engine.core.operators.MathOperator;
import ead.engine.core.operators.Operator;
import ead.engine.core.operators.OperatorFactory;
import ead.engine.core.operators.ValueOperator;
import ead.tools.reflection.ReflectionProvider;

public class OperatorFactoryMapProvider extends AbstractMapProvider<Class<?>, Operator<?>> {

	private static Map<Class<?>, Operator<?>> tempMap = new HashMap<Class<?>, Operator<?>>();
	
	private EvaluatorFactory evaluatorFactory;
	
	private ReflectionProvider reflectionProvider;
	
	private ValueMap valueMap;
	
	private OperatorFactory operatorFactory;
	
	public OperatorFactoryMapProvider(OperatorFactory operatorFactory, EvaluatorFactory evaluatorFactory, ValueMap valueMap,
			ReflectionProvider reflectionProvider) {
		super();
		this.valueMap = valueMap;
		this.evaluatorFactory = evaluatorFactory;
		this.reflectionProvider = reflectionProvider;
		this.operatorFactory = operatorFactory;
	}
	
	@Override
	public Map<Class<?>, Operator<?>> getMap() {
		FieldOperator fieldOperator = new FieldOperator(valueMap);
		factoryMap.put(MathOp.class, new MathOperator(valueMap));
		factoryMap.put(BooleanOp.class, new BooleanOperator(evaluatorFactory));
		factoryMap.put(ValueOp.class, new ValueOperator(reflectionProvider));
		factoryMap.put(EAdField.class, fieldOperator);
		factoryMap.put(BasicField.class, fieldOperator);
		factoryMap.put(ListOp.class, new ListOperator(valueMap));
		factoryMap.put(ConditionedOp.class, new ConditionedOperator(evaluatorFactory, operatorFactory));
		factoryMap.putAll(tempMap);
		return super.getMap();
	}
	
	public static void add(Class<?> operation, Operator<?> operator) {
		tempMap.put(operation, operator);
	}

	
}
