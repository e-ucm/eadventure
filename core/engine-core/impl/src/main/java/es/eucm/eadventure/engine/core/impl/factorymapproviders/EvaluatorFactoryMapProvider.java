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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.conditions.impl.ORCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarVarCondition;
import es.eucm.eadventure.engine.core.evaluators.Evaluator;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.evaluators.impl.EmptyConditionEvaluator;
import es.eucm.eadventure.engine.core.evaluators.impl.FlagConditionEvaluator;
import es.eucm.eadventure.engine.core.evaluators.impl.ListedConditionEvaluator;
import es.eucm.eadventure.engine.core.evaluators.impl.NOTConditionEvaluator;
import es.eucm.eadventure.engine.core.evaluators.impl.VarConditionEvaluator;

@Singleton
public class EvaluatorFactoryMapProvider extends AbstractMapProvider<Class<?>, Evaluator<?>> {

	private static Map<Class<?>, Evaluator<?>> tempMap = new HashMap<Class<?>, Evaluator<?>>();
	
	@Inject
	public EvaluatorFactoryMapProvider(EvaluatorFactory evaluatorFactory,
			EmptyConditionEvaluator emptyConditionEvaluator,
			FlagConditionEvaluator flagConditionEvaluator,
			VarConditionEvaluator varConditionEvaluator,
			ListedConditionEvaluator listedConditionEvaluator,
			NOTConditionEvaluator notConditionEvaluator) {
		super();
		factoryMap.put(EmptyCondition.class, emptyConditionEvaluator);
		factoryMap.put(FlagCondition.class, flagConditionEvaluator);
		factoryMap.put(VarVarCondition.class, varConditionEvaluator);
		factoryMap.put(VarValCondition.class, varConditionEvaluator);
		factoryMap.put(ANDCondition.class, listedConditionEvaluator);
		factoryMap.put(ORCondition.class, listedConditionEvaluator);
		factoryMap.put(NOTCondition.class, notConditionEvaluator);
		factoryMap.putAll(tempMap);
	}
	
	public static void add(Class<?> condition, Evaluator<?> evaluator) {
		tempMap.put(condition, evaluator);
	}
	
}
