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

package es.eucm.eadventure.engine.core.evaluators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.conditions.impl.VarCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarVarCondition;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.Evaluator;

@Singleton
public class VarConditionEvaluator implements Evaluator<VarCondition> {

	private static final Logger logger = LoggerFactory.getLogger(VarConditionEvaluator.class);
	
	@Inject
	private ValueMap valueMap;

	@Override
	public boolean evaluate(VarCondition condition) {
		Number value1 = null;
		Number value2 = null;
		if (condition instanceof VarVarCondition) {
			value1 = (Number) valueMap.getValue(((VarVarCondition) condition).getVar1());
			value2 = (Number) valueMap.getValue(((VarVarCondition) condition).getVar2());
		} else if (condition instanceof VarValCondition) {
			value1 = (Number) valueMap.getValue(((VarValCondition) condition).getVar());
			
			value2 = (((VarValCondition) condition).getVal());
		}
		if (value1 == null || value2 == null)
			return false;
		switch(condition.getOperator()) {
		case EQUAL:
			return value1.floatValue() == value2.floatValue();
		case DIFFERENT:
			return value1.floatValue() != value2.floatValue();
		case GREATER:
			return value1.floatValue() > value2.floatValue();
		case GREATER_EQUAL:
			return value1.floatValue() >= value2.floatValue();
		case LESS:
			return value1.floatValue() < value2.floatValue();
		case LESS_EQUAL:
			return value1.floatValue() <= value2.floatValue();
		default:
			logger.error("Invalid value for var-var condition");
			return false;
		}
	}

}
