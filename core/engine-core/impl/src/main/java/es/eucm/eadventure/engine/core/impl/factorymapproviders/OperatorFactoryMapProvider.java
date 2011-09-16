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

package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.EAdRuntimeException;
import es.eucm.eadventure.common.model.variables.impl.operations.AssignOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.operator.Operator;
import es.eucm.eadventure.engine.core.operators.impl.AssignOperator;
import es.eucm.eadventure.engine.core.operators.impl.BooleanOperator;
import es.eucm.eadventure.engine.core.operators.impl.LiteralExpressionOperator;

@Singleton
public class OperatorFactoryMapProvider extends AbstractMapProvider<Class<?>, Operator<?>> {

	private static Map<Class<?>, Operator<?>> tempMap = new HashMap<Class<?>, Operator<?>>();

	private static final Logger logger = Logger.getLogger("OperatorFactoryMapProvider");
	
	private EvaluatorFactory evaluatorFactory;
	
	@Inject
	public OperatorFactoryMapProvider(EvaluatorFactory evaluatorFactory) {
		super();
		this.evaluatorFactory = evaluatorFactory;
	}
	
	@Override
	public Map<Class<?>, Operator<?>> getMap() {
		logger.log(Level.SEVERE, "Must call getMap(ValueMap), not getMap()");
		throw new EAdRuntimeException("Must call getMap(ValueMap), not getMap()");
	}
	
	public Map<Class<?>, Operator<?>> getMap(ValueMap valueMap) {
		factoryMap.put(LiteralExpressionOperation.class, new LiteralExpressionOperator(valueMap));
		factoryMap.put(BooleanOperation.class, new BooleanOperator(evaluatorFactory));
		factoryMap.put(AssignOperation.class, new AssignOperator());
		factoryMap.putAll(tempMap);
		return super.getMap();
	}
	
	public static void add(Class<?> operation, Operator<?> operator) {
		tempMap.put(operation, operator);
	}

	
}
